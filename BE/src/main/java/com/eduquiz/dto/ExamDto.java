package com.eduquiz.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

public class ExamDto {

    @Data
    public static class CreateExamRequest {
        private String title;
        private String description;
        private Integer durationMinutes;
        private List<QuestionDto.CreateQuestionRequest> questions;
    }

    @Data
    public static class ExamResponse {
        private Long id;
        private String title;
        private String description;
        private Integer durationMinutes;
        private String teacherName;
        private int totalQuestions;
        private LocalDateTime createdAt;
    }

    @Data
    public static class ExamDetailResponse {
        private Long id;
        private String title;
        private String description;
        private Integer durationMinutes;
        private List<QuestionDto.QuestionResponse> questions;
    }

    // Submit exam
    @Data
    public static class SubmitRequest {
        private Long examId;
        private List<AnswerItem> answers;

        @Data
        public static class AnswerItem {
            private Long questionId;
            private String selectedAnswer; // A, B, C, D
        }
    }

    @Data
    public static class SubmitResponse {
        private int score;
        private int totalQuestion;
        private double percentage;
        private List<AnswerResult> details;

        @Data
        public static class AnswerResult {
            private Long questionId;
            private String selectedAnswer;
            private String correctAnswer;
            private boolean correct;
        }
    }
}
