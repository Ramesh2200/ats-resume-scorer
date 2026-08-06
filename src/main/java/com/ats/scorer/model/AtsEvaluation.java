package com.ats.scorer.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ats_evaluations")
public class AtsEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String candidateName;
    private String candidateEmail;
    private String jobTitle;

    private int overallScore;
    private int hardSkillScore;
    private int softSkillScore;
    private int actionVerbScore;
    private int formatScore;
    private int shortlistThreshold = 75;

    private String decisionStatus; // "SHORTLISTED" or "REJECTED"

    @Column(columnDefinition = "TEXT")
    private String emailSubject;

    @Column(columnDefinition = "LONGTEXT")
    private String emailBody;

    @Column(columnDefinition = "TEXT")
    private String smsBody;

    @Column(columnDefinition = "TEXT")
    private String matchedKeywordsJson;

    @Column(columnDefinition = "TEXT")
    private String missingKeywordsJson;

    @Column(columnDefinition = "TEXT")
    private String recommendationsJson;

    @Column(columnDefinition = "LONGTEXT")
    private String highlightedResumeHtml;

    private LocalDateTime evaluatedAt;

    public AtsEvaluation() {
        this.evaluatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCandidateName() { return candidateName; }
    public void setCandidateName(String candidateName) { this.candidateName = candidateName; }

    public String getCandidateEmail() { return candidateEmail; }
    public void setCandidateEmail(String candidateEmail) { this.candidateEmail = candidateEmail; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

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

    public String getMatchedKeywordsJson() { return matchedKeywordsJson; }
    public void setMatchedKeywordsJson(String matchedKeywordsJson) { this.matchedKeywordsJson = matchedKeywordsJson; }

    public String getMissingKeywordsJson() { return missingKeywordsJson; }
    public void setMissingKeywordsJson(String missingKeywordsJson) { this.missingKeywordsJson = missingKeywordsJson; }

    public String getRecommendationsJson() { return recommendationsJson; }
    public void setRecommendationsJson(String recommendationsJson) { this.recommendationsJson = recommendationsJson; }

    public String getHighlightedResumeHtml() { return highlightedResumeHtml; }
    public void setHighlightedResumeHtml(String highlightedResumeHtml) { this.highlightedResumeHtml = highlightedResumeHtml; }

    public LocalDateTime getEvaluatedAt() { return evaluatedAt; }
    public void setEvaluatedAt(LocalDateTime evaluatedAt) { this.evaluatedAt = evaluatedAt; }
}
