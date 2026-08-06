package com.ats.scorer.repository;

import com.ats.scorer.model.InterviewQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewQuestionRepository extends JpaRepository<InterviewQuestion, Long> {
    List<InterviewQuestion> findByTopicContainingIgnoreCase(String topic);
    List<InterviewQuestion> findByDifficultyIgnoreCase(String difficulty);
}
