package com.ats.scorer.service;

import com.ats.scorer.model.AtsEvaluation;
import com.ats.scorer.model.EvaluationRequest;
import com.ats.scorer.model.EvaluationResponse;
import com.ats.scorer.repository.AtsEvaluationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class AtsScoringService {

    @Autowired
    private AtsEvaluationRepository evaluationRepository;

    @Autowired
    private ResumeParserService resumeParserService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final List<String> COMMON_ACTION_VERBS = Arrays.asList(
            "built", "developed", "designed", "implemented", "spearheaded", "optimized",
            "managed", "automated", "scaled", "architected", "engineered", "increased",
            "reduced", "transformed", "led", "directed", "created", "refactored"
    );

    private static final List<String> SOFT_SKILLS = Arrays.asList(
            "leadership", "communication", "problem solving", "teamwork", "agile",
            "collaboration", "adaptability", "critical thinking", "time management",
            "analytical", "creativity", "decision making"
    );

    public EvaluationResponse evaluateResume(EvaluationRequest request) {
        String resumeText = request.getResumeText() != null ? request.getResumeText() : "";
        String jdText = request.getJobDescriptionText() != null ? request.getJobDescriptionText() : "";

        String candidateName = request.getCandidateName();
        if (candidateName == null || candidateName.isBlank() || candidateName.equals("Candidate")) {
            candidateName = resumeParserService.extractCandidateName(resumeText);
        }

        String email = request.getCandidateEmail();
        if (email == null || email.isBlank() || email.equals("Not Provided")) {
            email = resumeParserService.extractEmail(resumeText);
        }

        String phone = resumeParserService.extractPhone(resumeText);
        int threshold = request.getShortlistThreshold() > 0 ? request.getShortlistThreshold() : 75;

        // 1. Extract Keywords from Job Description
        Set<String> jdKeywords = extractKeywords(jdText);

        // Categorize JD Keywords into Hard Skills & Soft Skills
        List<String> matchedKeywords = new ArrayList<>();
        List<String> missingKeywords = new ArrayList<>();

        int matchedHardSkills = 0;
        int totalHardSkills = 0;
        int matchedSoftSkills = 0;
        int totalSoftSkills = 0;

        for (String kw : jdKeywords) {
            boolean isSoft = SOFT_SKILLS.contains(kw.toLowerCase());
            boolean isMatched = containsKeyword(resumeText, kw);

            if (isSoft) {
                totalSoftSkills++;
                if (isMatched) {
                    matchedSoftSkills++;
                    matchedKeywords.add(kw);
                } else {
                    missingKeywords.add(kw);
                }
            } else {
                totalHardSkills++;
                if (isMatched) {
                    matchedHardSkills++;
                    matchedKeywords.add(kw);
                } else {
                    missingKeywords.add(kw);
                }
            }
        }

        // Calculate Sub-Scores
        int hardSkillScore = totalHardSkills > 0 ? (int) Math.round(((double) matchedHardSkills / totalHardSkills) * 100) : 75;
        int softSkillScore = totalSoftSkills > 0 ? (int) Math.round(((double) matchedSoftSkills / totalSoftSkills) * 100) : 70;

        // Action Verbs Analysis
        int actionVerbMatches = 0;
        String lowerResume = resumeText.toLowerCase();
        for (String verb : COMMON_ACTION_VERBS) {
            if (lowerResume.contains(verb)) {
                actionVerbMatches++;
            }
        }
        int actionVerbScore = Math.min(100, (actionVerbMatches * 15) + 40);

        // Formatting & Section Structure Score
        int formatScore = calculateFormatScore(resumeText);

        // Overall Weighted ATS Score
        int overallScore = (int) Math.round((hardSkillScore * 0.45) + (softSkillScore * 0.20) + (actionVerbScore * 0.15) + (formatScore * 0.20));
        overallScore = Math.min(100, Math.max(15, overallScore));

        // Decision Logic: Shortlisted vs Rejected
        String decisionStatus = (overallScore >= threshold) ? "SHORTLISTED" : "REJECTED";

        // Generate Actionable Recommendations
        List<String> recommendations = generateRecommendations(overallScore, missingKeywords, actionVerbMatches, formatScore);

        // Generate Highlighted Resume Preview HTML
        String highlightedHtml = generateHighlightedHtml(resumeText, matchedKeywords);

        // Generate Candidate Email Notification
        String jobTitle = request.getJobTitle() != null ? request.getJobTitle() : "Target Role";
        String emailSubject = decisionStatus.equals("SHORTLISTED")
                ? "🎉 Congratulations! Your Application for " + jobTitle + " has been SHORTLISTED"
                : "Update regarding your Application for " + jobTitle;

        String emailBody = generateCandidateEmailBody(candidateName, jobTitle, overallScore, threshold, decisionStatus, matchedKeywords, missingKeywords);

        String smsBody = decisionStatus.equals("SHORTLISTED")
                ? "🎉 CONGRATULATIONS " + candidateName + "! You have been SHORTLISTED for " + jobTitle + " (ATS Score: " + overallScore + "%). Check email (" + email + ") for your interview invitation!"
                : "Hi " + candidateName + ", your application for " + jobTitle + " has been evaluated (ATS Score: " + overallScore + "%). Feedback sent to email.";

        // Persist Evaluation Record to MySQL DB
        AtsEvaluation eval = new AtsEvaluation();
        eval.setCandidateName(candidateName);
        eval.setCandidateEmail(email);
        eval.setJobTitle(jobTitle);
        eval.setOverallScore(overallScore);
        eval.setHardSkillScore(hardSkillScore);
        eval.setSoftSkillScore(softSkillScore);
        eval.setActionVerbScore(actionVerbScore);
        eval.setFormatScore(formatScore);
        eval.setShortlistThreshold(threshold);
        eval.setDecisionStatus(decisionStatus);
        eval.setEmailSubject(emailSubject);
        eval.setEmailBody(emailBody);
        eval.setSmsBody(smsBody);

        try {
            eval.setMatchedKeywordsJson(objectMapper.writeValueAsString(matchedKeywords));
            eval.setMissingKeywordsJson(objectMapper.writeValueAsString(missingKeywords));
            eval.setRecommendationsJson(objectMapper.writeValueAsString(recommendations));
        } catch (Exception e) {
            eval.setMatchedKeywordsJson("[]");
            eval.setMissingKeywordsJson("[]");
            eval.setRecommendationsJson("[]");
        }

        eval.setHighlightedResumeHtml(highlightedHtml);
        eval = evaluationRepository.save(eval);

        // Map to Response DTO
        EvaluationResponse response = new EvaluationResponse();
        response.setId(eval.getId());
        response.setCandidateName(candidateName);
        response.setJobTitle(jobTitle);
        response.setEmail(email);
        response.setPhone(phone);
        response.setOverallScore(overallScore);
        response.setHardSkillScore(hardSkillScore);
        response.setSoftSkillScore(softSkillScore);
        response.setActionVerbScore(actionVerbScore);
        response.setFormatScore(formatScore);
        response.setShortlistThreshold(threshold);
        response.setDecisionStatus(decisionStatus);
        response.setEmailSubject(emailSubject);
        response.setEmailBody(emailBody);
        response.setSmsBody(smsBody);
        response.setMatchedKeywords(matchedKeywords);
        response.setMissingKeywords(missingKeywords);
        response.setRecommendations(recommendations);
        response.setHighlightedResumeHtml(highlightedHtml);
        response.setRawResumeText(resumeText);
        response.setEvaluatedAt(eval.getEvaluatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

        return response;
    }

    private Set<String> extractKeywords(String text) {
        Set<String> keywords = new LinkedHashSet<>();
        if (text == null || text.isBlank()) return keywords;

        List<String> knownTechTerms = Arrays.asList(
                "Java", "Spring Boot", "Spring", "MySQL", "PostgreSQL", "MongoDB", "REST API",
                "Microservices", "Docker", "Kubernetes", "AWS", "Azure", "GCP", "React",
                "Angular", "Vue", "JavaScript", "TypeScript", "HTML", "CSS", "Git", "GitHub",
                "CI/CD", "Jenkins", "Maven", "Gradle", "Kafka", "Redis", "Python", "SQL",
                "Node.js", "Express", "Tailwind", "JUnit", "Mockito", "Security", "Linux",
                "Agile", "Scrum", "Jira", "Architecture", "System Design"
        );

        String lower = text.toLowerCase();
        for (String term : knownTechTerms) {
            if (Pattern.compile("\\b" + Pattern.quote(term.toLowerCase()) + "\\b").matcher(lower).find()) {
                keywords.add(term);
            }
        }

        for (String soft : SOFT_SKILLS) {
            if (lower.contains(soft.toLowerCase())) {
                keywords.add(soft);
            }
        }

        return keywords;
    }

    private boolean containsKeyword(String text, String keyword) {
        if (text == null || keyword == null) return false;
        return Pattern.compile("\\b" + Pattern.quote(keyword.toLowerCase()) + "\\b")
                .matcher(text.toLowerCase())
                .find();
    }

    private int calculateFormatScore(String resumeText) {
        if (resumeText == null || resumeText.isBlank()) return 20;
        int score = 50;
        String lower = resumeText.toLowerCase();

        if (lower.contains("experience") || lower.contains("work history") || lower.contains("employment")) score += 10;
        if (lower.contains("education") || lower.contains("academic")) score += 10;
        if (lower.contains("skills") || lower.contains("technologies") || lower.contains("technical")) score += 10;
        if (lower.contains("projects") || lower.contains("key achievements")) score += 10;
        if (resumeParserService.extractEmail(resumeText).contains("@")) score += 10;

        return Math.min(100, score);
    }

    private List<String> generateRecommendations(int score, List<String> missingKw, int actionVerbs, int formatScore) {
        List<String> recs = new ArrayList<>();

        if (!missingKw.isEmpty()) {
            String topMissing = missingKw.stream().limit(4).collect(Collectors.joining(", "));
            recs.add("Include missing core skills identified in the Job Description: " + topMissing + ".");
        }

        if (actionVerbs < 4) {
            recs.add("Incorporate strong impact action verbs (e.g. 'Spearheaded', 'Architected', 'Optimized', 'Automated') to quantify project outcomes.");
        } else {
            recs.add("Great usage of action verbs! Ensure achievements feature quantifiable metrics (e.g. 'Improved efficiency by 35%').");
        }

        if (formatScore < 80) {
            recs.add("Ensure standard section headers ('Work Experience', 'Education', 'Technical Skills', 'Projects') are clearly demarcated for ATS parsers.");
        }

        if (score >= 80) {
            recs.add("Outstanding ATS alignment! Your profile matches or exceeds the required threshold.");
        } else {
            recs.add("Re-align your resume summary and skills section to match key prerequisites.");
        }

        return recs;
    }

    private String generateHighlightedHtml(String text, List<String> matchedKeywords) {
        if (text == null) return "";
        String html = text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
        String[] lines = html.split("\n");
        StringBuilder sb = new StringBuilder();

        for (String line : lines) {
            String processedLine = line;
            for (String kw : matchedKeywords) {
                String pattern = "(?i)\\b(" + Pattern.quote(kw) + ")\\b";
                processedLine = processedLine.replaceAll(pattern, "<mark class=\"ats-highlight\">$1</mark>");
            }
            sb.append("<div class=\"resume-line\">").append(processedLine).append("</div>");
        }

        return sb.toString();
    }

    private String generateCandidateEmailBody(String candidateName, String jobTitle, int score, int threshold, String status, List<String> matched, List<String> missing) {
        StringBuilder sb = new StringBuilder();
        sb.append("Dear ").append(candidateName).append(",\n\n");

        if ("SHORTLISTED".equalsIgnoreCase(status)) {
            sb.append("Great news! We have reviewed your resume for the position of ")
              .append(jobTitle).append(".\n\n")
              .append("Your application achieved an ATS Match Score of ").append(score).append("% ")
              .append("(passing our shortlisting criteria threshold of ").append(threshold).append("%).\n\n")
              .append("Our talent acquisition team was impressed by your matched technical competencies: ")
              .append(matched.stream().limit(6).collect(Collectors.joining(", "))).append(".\n\n")
              .append("We would like to invite you for an initial technical interview round. Please reply to this email with your availability for the coming week.\n\n")
              .append("Best regards,\nTalent Acquisition Team");
        } else {
            sb.append("Thank you for your interest in the position of ").append(jobTitle).append(".\n\n")
              .append("After evaluating your resume against our current job requirements, your ATS Match Score was ")
              .append(score).append("% (below our shortlist threshold of ").append(threshold).append("%).\n\n")
              .append("To improve future ATS matches, we recommend highlighting key technical prerequisites: ")
              .append(missing.stream().limit(5).collect(Collectors.joining(", "))).append(".\n\n")
              .append("We appreciate your time and wish you great success in your career search.\n\n")
              .append("Best regards,\nRecruitment Team");
        }

        return sb.toString();
    }
}
