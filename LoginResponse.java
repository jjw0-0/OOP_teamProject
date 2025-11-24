package com.project.app.dto;

/**
 * 로그인 응답 DTO
 *
 * 목적: 로그인 결과 및 사용자 정보 반환
 *
 * 사용 시점:
 * AuthService -> Controller -> SignInView
 *
 * 필드:
 * - success (boolean)
 * - message (String)
 * - userId (String) - 성공 시
 * - userName (String) - 성공 시
 * - grade (int) - 성공 시
 *
 * 이유:
 * 성공/실패 + 사용자 정보를 함께 반환. 여러 정보를 묶어야 함.
 */
public class LoginResponse {

    private final boolean success;
    private final String message;
    private final String userId;
    private final String userName;
    private final int grade;  // 0 또는 -1 등을 기본값으로 둘 수도 있음

    private LoginResponse(boolean success, String message, String userId, String userName, int grade) {
        this.success = success;
        this.message = message;
        this.userId = userId;
        this.userName = userName;
        this.grade = grade;
    }

    /**
     * 로그인 성공 시 사용하는 팩토리 메서드
     */
    public static LoginResponse success(String userId, String userName, int grade) {
        return new LoginResponse(true, "로그인 성공", userId, userName, grade);
    }

    /**
     * 로그인 실패 시 사용하는 팩토리 메서드
     * - 사용자 정보는 null / 0 으로 채움
     */
    public static LoginResponse failure(String message) {
        return new LoginResponse(false, message, null, null, 0);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public int getGrade() {
        return grade;
    }
}
