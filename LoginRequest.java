package com.project.app.dto;

/**
 * 로그인 요청 DTO
 *
 * 목적: 로그인 입력값 전달
 *
 * 사용 시점:
 * SignInView -> SignInController -> AuthService
 *
 * 필드:
 * - userId (String)
 * - password (String)
 *
 * 이유: 2개의 입력값을 하나의 객체로 묶어 전달
 */
public class LoginRequest {

    private final String userId;
    private final String password;

    public LoginRequest(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }
}
