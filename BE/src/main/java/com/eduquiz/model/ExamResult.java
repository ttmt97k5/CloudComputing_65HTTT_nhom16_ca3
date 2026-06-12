package com.eduquiz.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "exam_results")
@Data
@NoArgsConstructor
public class ExamResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @Column(nullable = false)
    private Integer score;        // số câu đúng

    @Column(nullable = false)
    private Integer totalQuestion;

    @Column(nullable = false)
    private Double percentage;    // % điểm

    private LocalDateTime submittedAt = LocalDateTime.now();
}
