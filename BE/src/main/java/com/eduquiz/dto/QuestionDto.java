package com.eduquiz.dto;

import lombok.Data;

public class QuestionDto {

    @Data
    public static class CreateQuestionRequest {
        private String content;
        private String optionA;
        private String optionB;
        private String optionC;
        private String optionD;
        private String correctAnswer;
        private Integer orderIndex;
    }

    @Data
    public static class QuestionResponse {
        private Long id;
        private String content;
        private String optionA;
        private String optionB;
        private String optionC;
        private String optionD;
        private Integer orderIndex;
        // correctAnswer KHÔNG trả về khi học sinh làm bài
    }
}
