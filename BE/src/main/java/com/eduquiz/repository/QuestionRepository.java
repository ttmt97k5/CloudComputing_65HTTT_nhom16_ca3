package com.eduquiz.repository;

import com.eduquiz.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByExamIdOrderByOrderIndex(Long examId);
    void deleteByExamId(Long examId);
}
