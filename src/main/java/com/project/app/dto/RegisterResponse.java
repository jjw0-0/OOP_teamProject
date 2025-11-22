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
 *
 * 이유: 실패 원인을 명확히 전달. 에러 타입별로 다른 처리 가능.
 */
public class RegisterResponse {

}
