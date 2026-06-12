package com.eduquiz.controller;

import com.eduquiz.dto.ExamDto;
import com.eduquiz.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final ExamService examService;

    // Tạo đề thi mới
    @PostMapping("/exams")
    public ResponseEntity<ExamDto.ExamResponse> createExam(
            @RequestBody ExamDto.CreateExamRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(examService.createExam(request, userDetails.getUsername()));
    }

    // Xem danh sách đề thi của mình
    @GetMapping("/exams")
    public ResponseEntity<List<ExamDto.ExamResponse>> getMyExams(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(examService.getMyExams(userDetails.getUsername()));
    }

    // Xóa đề thi
    @DeleteMapping("/exams/{examId}")
    public ResponseEntity<Void> deleteExam(
            @PathVariable Long examId,
            @AuthenticationPrincipal UserDetails userDetails) {
        examService.deleteExam(examId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
