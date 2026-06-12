package com.eduquiz.controller;

import com.eduquiz.dto.ExamDto;
import com.eduquiz.model.ExamResult;
import com.eduquiz.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final ExamService examService;

    // Xem danh sách đề thi đang mở
    @GetMapping("/exams")
    public ResponseEntity<List<ExamDto.ExamResponse>> getAllExams() {
        return ResponseEntity.ok(examService.getAllActiveExams());
    }

    // Lấy chi tiết đề thi để làm bài (không có đáp án)
    @GetMapping("/exams/{examId}")
    public ResponseEntity<ExamDto.ExamDetailResponse> getExamDetail(@PathVariable Long examId) {
        return ResponseEntity.ok(examService.getExamForStudent(examId));
    }

    // Nộp bài
    @PostMapping("/exams/submit")
    public ResponseEntity<ExamDto.SubmitResponse> submitExam(
            @RequestBody ExamDto.SubmitRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(examService.submitExam(request, userDetails.getUsername()));
    }

    // Xem kết quả các bài đã thi
    @GetMapping("/results")
    public ResponseEntity<List<ExamResult>> getMyResults(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(examService.getMyResults(userDetails.getUsername()));
    }
}
