package com.ats.scorer.model;

import java.util.List;

public class EvaluationResponse {
    private Long id;
    private String candidateName;
    private String jobTitle;
    private String email;
    private String phone;

    private int overallScore;
    private int hardSkillScore;
    private int softSkillScore;
    private int actionVerbScore;
    private int formatScore;
    private int shortlistThreshold;

    private String decisionStatus; // "SHORTLISTED" vs "REJECTED"
    private String emailSubject;
    private String emailBody;
    private String smsBody;

    private List<String> matchedKeywords;
    private List<String> missingKeywords;
    private List<String> recommendations;
    private String highlightedResumeHtml;
    private String rawResumeText;
    private String evaluatedAt;

    public EvaluationResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCandidateName() { return candidateName; }
    public void setCandidateName(String candidateName) { this.candidateName = candidateName; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public int getOverallScore() { return overallScore; }
    public void setOverallScore(int overallScore) { this.overallScore = overallScore; }

    public int getHardSkillScore() { return hardSkillScore; }
    public void setHardSkillScore(int hardSkillScore) { this.hardSkillScore = hardSkillScore; }

    public int getSoftSkillScore() { return softSkillScore; }
    public void setSoftSkillScore(int softSkillScore) { this.softSkillScore = softSkillScore; }

    public int getActionVerbScore() { return actionVerbScore; }
    public void setActionVerbScore(int actionVerbScore) { this.actionVerbScore = actionVerbScore; }

    public int getFormatScore() { return formatScore; }
    public void setFormatScore(int formatScore) { this.formatScore = formatScore; }

    public int getShortlistThreshold() { return shortlistThreshold; }
    public void setShortlistThreshold(int shortlistThreshold) { this.shortlistThreshold = shortlistThreshold; }

    public String getDecisionStatus() { return decisionStatus; }
    public void setDecisionStatus(String decisionStatus) { this.decisionStatus = decisionStatus; }

    public String getEmailSubject() { return emailSubject; }
    public void setEmailSubject(String emailSubject) { this.emailSubject = emailSubject; }

    public String getEmailBody() { return emailBody; }
    public void setEmailBody(String emailBody) { this.emailBody = emailBody; }

    public String getSmsBody() { return smsBody; }
    public void setSmsBody(String smsBody) { this.smsBody = smsBody; }

    public List<String> getMatchedKeywords() { return matchedKeywords; }
    public void setMatchedKeywords(List<String> matchedKeywords) { this.matchedKeywords = matchedKeywords; }

    public List<String> getMissingKeywords() { return missingKeywords; }
    public void setMissingKeywords(List<String> missingKeywords) { this.missingKeywords = missingKeywords; }

    public List<String> getRecommendations() { return recommendations; }
    public void setRecommendations(List<String> recommendations) { this.recommendations = recommendations; }

    public String getHighlightedResumeHtml() { return highlightedResumeHtml; }
    public void setHighlightedResumeHtml(String highlightedResumeHtml) { this.highlightedResumeHtml = highlightedResumeHtml; }

    public String getRawResumeText() { return rawResumeText; }
    public void setRawResumeText(String rawResumeText) { this.rawResumeText = rawResumeText; }

    public String getEvaluatedAt() { return evaluatedAt; }
    public void setEvaluatedAt(String evaluatedAt) { this.evaluatedAt = evaluatedAt; }
}
