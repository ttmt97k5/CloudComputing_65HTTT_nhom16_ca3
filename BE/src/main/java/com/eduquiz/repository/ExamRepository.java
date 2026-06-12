package com.eduquiz.repository;

import com.eduquiz.model.Exam;
import com.eduquiz.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findByActiveTrue();
    List<Exam> findByTeacher(User teacher);
}
