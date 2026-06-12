package com.eduquiz.dto;

import lombok.Data;

// ===== AUTH =====
public class AuthDto {

    @Data
    public static class RegisterRequest {
        private String username;
        private String password;
        private String fullName;
        private String email;
        private String role; // STUDENT or TEACHER
    }

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    public static class AuthResponse {
        private String token;
        private String username;
        private String fullName;
        private String role;

        public AuthResponse(String token, String username, String fullName, String role) {
            this.token = token;
            this.username = username;
            this.fullName = fullName;
            this.role = role;
        }
    }
}
