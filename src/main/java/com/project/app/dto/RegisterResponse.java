package com.project.app.dto;

/**
 * 회원가입 결과 응답 DTO
 *
 * 목적: 회원가입 결과 반환
 *
 * 사용 시점: AuthService -> Controller -> SignUpView
 *
 * 필드:
 *   - success (boolean)
 *   - message (String)
 *   - errorType (ErrorType enum)
 *     - EMPTY_FIELD
 *     - INVALID_PASSWORD
 *     - DUPLICATE_ID
 *     - SYSTEM_ERROR
 */
public class RegisterResponse {

    public enum ErrorType {
        EMPTY_FIELD,
        INVALID_PASSWORD,
        DUPLICATE_ID,
        SYSTEM_ERROR
    }

    private boolean success;
    private String message;
    private ErrorType errorType;

    // 성공 시
    public RegisterResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.errorType = null;
    }

    // 실패 시
    public RegisterResponse(boolean success, String message, ErrorType errorType) {
        this.success = success;
        this.message = message;
        this.errorType = errorType;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public ErrorType getErrorType() { return errorType; }
}
