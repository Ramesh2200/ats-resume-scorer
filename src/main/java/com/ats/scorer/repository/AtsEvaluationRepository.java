package com.ats.scorer.repository;

import com.ats.scorer.model.AtsEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtsEvaluationRepository extends JpaRepository<AtsEvaluation, Long> {
    List<AtsEvaluation> findAllByOrderByIdDesc();
}
