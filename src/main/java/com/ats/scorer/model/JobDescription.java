package com.ats.scorer.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_descriptions")
public class JobDescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String company;
    private String category;
    private String experienceLevel;

    private int shortlistThreshold = 75; // Default 75% for shortlisting
    private String postedBy;

    @Column(columnDefinition = "LONGTEXT")
    private String rawText;

    @Column(columnDefinition = "TEXT")
    private String requiredSkills;

    private LocalDateTime createdAt;

    public JobDescription() {
        this.createdAt = LocalDateTime.now();
    }

    public JobDescription(String title, String company, String category, String experienceLevel, String rawText, String requiredSkills, int shortlistThreshold, String postedBy) {
        this.title = title;
        this.company = company;
        this.category = category;
        this.experienceLevel = experienceLevel;
        this.rawText = rawText;
        this.requiredSkills = requiredSkills;
        this.shortlistThreshold = shortlistThreshold > 0 ? shortlistThreshold : 75;
        this.postedBy = postedBy != null ? postedBy : "Recruiter";
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getExperienceLevel() { return experienceLevel; }
    public void setExperienceLevel(String experienceLevel) { this.experienceLevel = experienceLevel; }

    public int getShortlistThreshold() { return shortlistThreshold; }
    public void setShortlistThreshold(int shortlistThreshold) { this.shortlistThreshold = shortlistThreshold; }

    public String getPostedBy() { return postedBy; }
    public void setPostedBy(String postedBy) { this.postedBy = postedBy; }

    public String getRawText() { return rawText; }
    public void setRawText(String rawText) { this.rawText = rawText; }

    public String getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(String requiredSkills) { this.requiredSkills = requiredSkills; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
