package com.ats.scorer.model;

import jakarta.persistence.*;

@Entity
@Table(name = "interview_questions")
public class InterviewQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String topic; // "Java", "Spring Boot", "SQL", "System Design", "Algorithms"
    private String category; // "Conceptual", "Coding", "Architecture"
    private String difficulty; // "Easy", "Medium", "Hard"
    private String title;
    private String company; // "Tap Academy", "Google", "Amazon", "Microsoft"
    private String score; // "10 / 10"
    private Integer attempts = 1;
    private Boolean isSolved = false;

    @Column(name = "question_text", columnDefinition = "LONGTEXT")
    private String questionText;

    @Column(name = "input_format", columnDefinition = "TEXT")
    private String inputFormat;

    @Column(name = "output_format", columnDefinition = "TEXT")
    private String outputFormat;

    @Column(name = "constraints_text", columnDefinition = "TEXT")
    private String constraintsText;

    @Column(name = "sample_case1_input", columnDefinition = "TEXT")
    private String sampleCase1Input;

    @Column(name = "sample_case1_output", columnDefinition = "TEXT")
    private String sampleCase1Output;

    @Column(name = "sample_case2_input", columnDefinition = "TEXT")
    private String sampleCase2Input;

    @Column(name = "sample_case2_output", columnDefinition = "TEXT")
    private String sampleCase2Output;

    @Column(name = "answer_explanation", columnDefinition = "LONGTEXT")
    private String answerExplanation;

    @Column(name = "code_snippet", columnDefinition = "LONGTEXT")
    private String codeSnippet;

    @Column(name = "sample_test_case", columnDefinition = "TEXT")
    private String sampleTestCase;

    // Multi-Language Logic Starter Templates
    @Column(name = "template_java", columnDefinition = "LONGTEXT")
    private String templateJava;

    @Column(name = "template_python", columnDefinition = "LONGTEXT")
    private String templatePython;

    @Column(name = "template_c", columnDefinition = "LONGTEXT")
    private String templateC;

    @Column(name = "template_cpp", columnDefinition = "LONGTEXT")
    private String templateCpp;

    @Column(name = "template_js", columnDefinition = "LONGTEXT")
    private String templateJs;

    @Column(name = "template_sql", columnDefinition = "LONGTEXT")
    private String templateSql;

    @Column(name = "test_cases_json", columnDefinition = "LONGTEXT")
    private String testCasesJson;

    public InterviewQuestion() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getScore() { return score; }
    public void setScore(String score) { this.score = score; }

    public Integer getAttempts() { return attempts; }
    public void setAttempts(Integer attempts) { this.attempts = attempts; }

    public Boolean getIsSolved() { return isSolved != null ? isSolved : false; }
    public void setIsSolved(Boolean isSolved) { this.isSolved = isSolved; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public String getInputFormat() { return inputFormat; }
    public void setInputFormat(String inputFormat) { this.inputFormat = inputFormat; }

    public String getOutputFormat() { return outputFormat; }
    public void setOutputFormat(String outputFormat) { this.outputFormat = outputFormat; }

    public String getConstraintsText() { return constraintsText; }
    public void setConstraintsText(String constraintsText) { this.constraintsText = constraintsText; }

    public String getSampleCase1Input() { return sampleCase1Input; }
    public void setSampleCase1Input(String sampleCase1Input) { this.sampleCase1Input = sampleCase1Input; }

    public String getSampleCase1Output() { return sampleCase1Output; }
    public void setSampleCase1Output(String sampleCase1Output) { this.sampleCase1Output = sampleCase1Output; }

    public String getSampleCase2Input() { return sampleCase2Input; }
    public void setSampleCase2Input(String sampleCase2Input) { this.sampleCase2Input = sampleCase2Input; }

    public String getSampleCase2Output() { return sampleCase2Output; }
    public void setSampleCase2Output(String sampleCase2Output) { this.sampleCase2Output = sampleCase2Output; }

    public String getAnswerExplanation() { return answerExplanation; }
    public void setAnswerExplanation(String answerExplanation) { this.answerExplanation = answerExplanation; }

    public String getCodeSnippet() { return codeSnippet; }
    public void setCodeSnippet(String codeSnippet) { this.codeSnippet = codeSnippet; }

    public String getSampleTestCase() { return sampleTestCase; }
    public void setSampleTestCase(String sampleTestCase) { this.sampleTestCase = sampleTestCase; }

    public String getTemplateJava() { return templateJava; }
    public void setTemplateJava(String templateJava) { this.templateJava = templateJava; }

    public String getTemplatePython() { return templatePython; }
    public void setTemplatePython(String templatePython) { this.templatePython = templatePython; }

    public String getTemplateC() { return templateC; }
    public void setTemplateC(String templateC) { this.templateC = templateC; }

    public String getTemplateCpp() { return templateCpp; }
    public void setTemplateCpp(String templateCpp) { this.templateCpp = templateCpp; }

    public String getTemplateJs() { return templateJs; }
    public void setTemplateJs(String templateJs) { this.templateJs = templateJs; }

    public String getTemplateSql() { return templateSql; }
    public void setTemplateSql(String templateSql) { this.templateSql = templateSql; }

    public String getTestCasesJson() { return testCasesJson; }
    public void setTestCasesJson(String testCasesJson) { this.testCasesJson = testCasesJson; }
}
