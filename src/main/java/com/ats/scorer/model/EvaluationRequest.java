package com.ats.scorer.model;

public class EvaluationRequest {
    private String candidateName;
    private String candidateEmail;
    private String resumeText;
    private String jobTitle;
    private String jobDescriptionText;
    private int shortlistThreshold = 75;

    public EvaluationRequest() {}

    public String getCandidateName() { return candidateName; }
    public void setCandidateName(String candidateName) { this.candidateName = candidateName; }

    public String getCandidateEmail() { return candidateEmail; }
    public void setCandidateEmail(String candidateEmail) { this.candidateEmail = candidateEmail; }

    public String getResumeText() { return resumeText; }
    public void setResumeText(String resumeText) { this.resumeText = resumeText; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getJobDescriptionText() { return jobDescriptionText; }
    public void setJobDescriptionText(String jobDescriptionText) { this.jobDescriptionText = jobDescriptionText; }

    public int getShortlistThreshold() { return shortlistThreshold; }
    public void setShortlistThreshold(int shortlistThreshold) { this.shortlistThreshold = shortlistThreshold; }
}
