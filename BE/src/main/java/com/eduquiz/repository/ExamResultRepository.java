package com.eduquiz.repository;

import com.eduquiz.model.ExamResult;
import com.eduquiz.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface ExamResultRepository extends JpaRepository<ExamResult, Long> {
    List<ExamResult> findByStudent(User student);
    List<ExamResult> findByExamId(Long examId);
    Optional<ExamResult> findByStudentIdAndExamId(Long studentId, Long examId);

    @Query("SELECT AVG(r.percentage) FROM ExamResult r WHERE r.exam.id = :examId")
    Double findAvgScoreByExamId(Long examId);
}
