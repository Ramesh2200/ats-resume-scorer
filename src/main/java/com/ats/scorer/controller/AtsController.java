package com.ats.scorer.controller;

import com.ats.scorer.model.*;
import com.ats.scorer.repository.AtsEvaluationRepository;
import com.ats.scorer.repository.JobDescriptionRepository;
import com.ats.scorer.service.AtsScoringService;
import com.ats.scorer.service.ResumeParserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ats")
@CrossOrigin(origins = "*")
public class AtsController {

    @Autowired
    private AtsScoringService atsScoringService;

    @Autowired
    private ResumeParserService resumeParserService;

    @Autowired
    private AtsEvaluationRepository evaluationRepository;

    @Autowired
    private JobDescriptionRepository jobDescriptionRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String mailUsername;

    @Value("${twilio.account-sid:}")
    private String twilioAccountSid;

    @Value("${twilio.auth-token:}")
    private String twilioAuthToken;

    @Value("${twilio.phone-number:}")
    private String twilioPhoneNumber;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/evaluate")
    public ResponseEntity<EvaluationResponse> evaluate(@RequestBody EvaluationRequest request) {
        EvaluationResponse response = atsScoringService.evaluateResume(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadResume(@RequestParam("file") MultipartFile file) {
        Map<String, String> result = new HashMap<>();
        try {
            String text = resumeParserService.parseFile(file);
            String candidateName = resumeParserService.extractCandidateName(text);
            String email = resumeParserService.extractEmail(text);
            String phone = resumeParserService.extractPhone(text);

            result.put("fileName", file.getOriginalFilename());
            result.put("candidateName", candidateName);
            result.put("email", email);
            result.put("phone", phone);
            result.put("text", text);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("error", "Failed to parse file: " + e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<JobDescription>> getJobPresets() {
        List<JobDescription> jobs = jobDescriptionRepository.findAll();
        return ResponseEntity.ok(jobs);
    }

    @PostMapping("/jobs")
    public ResponseEntity<Map<String, Object>> addJobPosting(@RequestBody JobDescription job) {
        Map<String, Object> res = new HashMap<>();
        JobDescription saved = jobDescriptionRepository.save(job);
        res.put("success", true);
        res.put("message", "Job posting created successfully!");
        res.put("job", saved);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/history")
    public ResponseEntity<List<EvaluationResponse>> getEvaluationHistory() {
        List<AtsEvaluation> list = evaluationRepository.findAllByOrderByIdDesc();
        List<EvaluationResponse> responses = list.stream().map(eval -> {
            EvaluationResponse res = new EvaluationResponse();
            res.setId(eval.getId());
            res.setCandidateName(eval.getCandidateName());
            res.setEmail(eval.getCandidateEmail());
            res.setJobTitle(eval.getJobTitle());
            res.setOverallScore(eval.getOverallScore());
            res.setHardSkillScore(eval.getHardSkillScore());
            res.setSoftSkillScore(eval.getSoftSkillScore());
            res.setActionVerbScore(eval.getActionVerbScore());
            res.setFormatScore(eval.getFormatScore());
            res.setShortlistThreshold(eval.getShortlistThreshold());
            res.setDecisionStatus(eval.getDecisionStatus());
            res.setEmailSubject(eval.getEmailSubject());
            res.setEmailBody(eval.getEmailBody());
            res.setSmsBody(eval.getSmsBody());
            res.setHighlightedResumeHtml(eval.getHighlightedResumeHtml());
            res.setEvaluatedAt(eval.getEvaluatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

            try {
                if (eval.getMatchedKeywordsJson() != null) {
                    res.setMatchedKeywords(objectMapper.readValue(eval.getMatchedKeywordsJson(), new TypeReference<List<String>>(){}));
                }
                if (eval.getMissingKeywordsJson() != null) {
                    res.setMissingKeywords(objectMapper.readValue(eval.getMissingKeywordsJson(), new TypeReference<List<String>>(){}));
                }
                if (eval.getRecommendationsJson() != null) {
                    res.setRecommendations(objectMapper.readValue(eval.getRecommendationsJson(), new TypeReference<List<String>>(){}));
                }
            } catch (Exception e) {
                res.setMatchedKeywords(Collections.emptyList());
                res.setMissingKeywords(Collections.emptyList());
                res.setRecommendations(Collections.emptyList());
            }

            return res;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/evaluations/{id}")
    public ResponseEntity<EvaluationResponse> getEvaluationDetail(@PathVariable Long id) {
        Optional<AtsEvaluation> opt = evaluationRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        AtsEvaluation eval = opt.get();
        EvaluationResponse res = new EvaluationResponse();
        res.setId(eval.getId());
        res.setCandidateName(eval.getCandidateName());
        res.setEmail(eval.getCandidateEmail());
        res.setJobTitle(eval.getJobTitle());
        res.setOverallScore(eval.getOverallScore());
        res.setHardSkillScore(eval.getHardSkillScore());
        res.setSoftSkillScore(eval.getSoftSkillScore());
        res.setActionVerbScore(eval.getActionVerbScore());
        res.setFormatScore(eval.getFormatScore());
        res.setShortlistThreshold(eval.getShortlistThreshold());
        res.setDecisionStatus(eval.getDecisionStatus());
        res.setEmailSubject(eval.getEmailSubject());
        res.setEmailBody(eval.getEmailBody());
        res.setHighlightedResumeHtml(eval.getHighlightedResumeHtml());
        res.setEvaluatedAt(eval.getEvaluatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

        try {
            if (eval.getMatchedKeywordsJson() != null) {
                res.setMatchedKeywords(objectMapper.readValue(eval.getMatchedKeywordsJson(), new TypeReference<List<String>>(){}));
            }
            if (eval.getMissingKeywordsJson() != null) {
                res.setMissingKeywords(objectMapper.readValue(eval.getMissingKeywordsJson(), new TypeReference<List<String>>(){}));
            }
            if (eval.getRecommendationsJson() != null) {
                res.setRecommendations(objectMapper.readValue(eval.getRecommendationsJson(), new TypeReference<List<String>>(){}));
            }
        } catch (Exception e) {
            res.setMatchedKeywords(Collections.emptyList());
            res.setMissingKeywords(Collections.emptyList());
            res.setRecommendations(Collections.emptyList());
        }

        return ResponseEntity.ok(res);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadResume(@PathVariable Long id) {
        Optional<AtsEvaluation> opt = evaluationRepository.findById(id);
        String name = "Candidate";
        String content = "Resume Document Content";

        if (opt.isPresent()) {
            AtsEvaluation eval = opt.get();
            name = eval.getCandidateName() != null ? eval.getCandidateName().replaceAll("\\s+", "_") : "Candidate";
            content = "=================================================\n" +
                      " RESUME DOCUMENT - " + eval.getCandidateName() + "\n" +
                      " Target Role: " + eval.getJobTitle() + "\n" +
                      " ATS Score: " + eval.getOverallScore() + "% (" + eval.getDecisionStatus() + ")\n" +
                      "=================================================\n\n" +
                      (eval.getEmailBody() != null ? eval.getEmailBody() : "");
        }

        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + name + "_Resume.txt")
                .contentType(MediaType.TEXT_PLAIN)
                .body(bytes);
    }

    @PostMapping("/send-notification")
    public ResponseEntity<Map<String, Object>> sendNotification(@RequestBody Map<String, String> payload) {
        Map<String, Object> res = new HashMap<>();
        String email = payload.get("email");
        if (email == null || email.isBlank()) email = "candidate@example.com";
        
        String phone = payload.get("phone");
        if (phone == null || phone.isBlank()) phone = "+91-9876543210";

        String candidateName = payload.get("candidateName");
        if (candidateName == null || candidateName.isBlank()) candidateName = "Candidate";

        String subject = payload.get("subject");
        if (subject == null || subject.isBlank()) subject = "Application Status Notification";

        String decisionStatus = payload.get("decisionStatus") != null ? payload.get("decisionStatus") : "SHORTLISTED";
        String emailBody = payload.get("emailBody");
        if (emailBody == null || emailBody.isBlank()) {
            emailBody = "Hello " + candidateName + ",\n\n"
                    + "Thank you for applying. We are pleased to inform you that your application status is: " + decisionStatus + ".\n\n"
                    + "Best regards,\nTalent Acquisition Team";
        }
        String smsBody = payload.get("smsBody");

        String emailDeliveryStatus = "SIMULATED_SUCCESS (To deliver live emails to Gmail inboxes, set your Gmail App Password in application.properties or environment variable SPRING_MAIL_PASSWORD)";
        String smsDeliveryStatus = "SIMULATED_SUCCESS (To deliver live SMS to mobile phones, set Twilio credentials in application.properties)";

        if (mailSender != null && emailBody != null && !emailBody.isBlank()) {
            try {
                System.out.println("--> Attempting Live Gmail SMTP Delivery to: " + email);
                SimpleMailMessage msg = new SimpleMailMessage();
                msg.setFrom(mailUsername != null && !mailUsername.isBlank() ? mailUsername : "ballariramesh0825@gmail.com");
                msg.setTo(email);
                msg.setSubject(subject);
                msg.setText(emailBody);
                mailSender.send(msg);
                System.out.println("--> LIVE GMAIL SMTP SENT SUCCESSFULLY TO: " + email);
                emailDeliveryStatus = "DELIVERED_TO_INBOX (200 OK via Gmail SMTP Server)";
            } catch (Exception e) {
                System.err.println("--> GMAIL SMTP ERROR: " + e.getMessage());
                e.printStackTrace();
                emailDeliveryStatus = "SMTP_ATTEMPT_FAILED (" + e.getMessage() + ")";
            }
        } else {
            System.err.println("--> mailSender is NULL or emailBody is empty!");
        }

        // Live Twilio SMS Delivery Integration
        if (twilioAccountSid != null && !twilioAccountSid.isBlank()
                && twilioAuthToken != null && !twilioAuthToken.isBlank()
                && twilioPhoneNumber != null && !twilioPhoneNumber.isBlank()
                && phone != null && !phone.isBlank()
                && smsBody != null && !smsBody.isBlank()) {
            try {
                System.out.println("--> Attempting Live Twilio SMS Delivery to: " + phone);
                String twilioUrl = "https://api.twilio.com/2010-04-01/Accounts/" + twilioAccountSid + "/Messages.json";
                java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
                String authStr = twilioAccountSid + ":" + twilioAuthToken;
                String base64Auth = java.util.Base64.getEncoder().encodeToString(authStr.getBytes());
                String formData = "To=" + java.net.URLEncoder.encode(phone, java.nio.charset.StandardCharsets.UTF_8)
                        + "&From=" + java.net.URLEncoder.encode(twilioPhoneNumber, java.nio.charset.StandardCharsets.UTF_8)
                        + "&Body=" + java.net.URLEncoder.encode(smsBody, java.nio.charset.StandardCharsets.UTF_8);
                java.net.http.HttpRequest req = java.net.http.HttpRequest.newBuilder()
                        .uri(java.net.URI.create(twilioUrl))
                        .header("Authorization", "Basic " + base64Auth)
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .POST(java.net.http.HttpRequest.BodyPublishers.ofString(formData))
                        .build();
                java.net.http.HttpResponse<String> resp = client.send(req, java.net.http.HttpResponse.BodyHandlers.ofString());
                if (resp.statusCode() >= 200 && resp.statusCode() < 300) {
                    System.out.println("--> LIVE TWILIO SMS SENT SUCCESSFULLY TO: " + phone);
                    smsDeliveryStatus = "DELIVERED_TO_PHONE (200 OK via Twilio)";
                } else {
                    System.err.println("--> Twilio API Error (Status " + resp.statusCode() + "): " + resp.body());
                    smsDeliveryStatus = "TWILIO_API_ERROR (Status " + resp.statusCode() + ")";
                }
            } catch (Exception e) {
                System.err.println("--> TWILIO SMS ERROR: " + e.getMessage());
                e.printStackTrace();
                smsDeliveryStatus = "TWILIO_ATTEMPT_FAILED (" + e.getMessage() + ")";
            }
        } else {
            System.err.println("--> Twilio details are empty or not configured. Running in SIMULATED mode.");
        }

        res.put("success", true);
        res.put("message", "Direct Email & Mobile SMS Dispatched!");
        res.put("candidateName", candidateName);
        res.put("email", email);
        res.put("phone", phone);
        res.put("subject", subject);
        res.put("emailStatus", emailDeliveryStatus);
        res.put("smsStatus", smsDeliveryStatus);
        res.put("decisionStatus", decisionStatus);
        res.put("emailBody", emailBody);
        res.put("smsBody", smsBody != null ? smsBody : "🎉 Congratulations " + candidateName + "! You have been SHORTLISTED. Check email for details.");
        res.put("dispatchTime", new Date().toString());
        return ResponseEntity.ok(res);
    }
}
