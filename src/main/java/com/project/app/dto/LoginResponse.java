package com.project.app.dto;

/**
 * 로그인 응답 DTO
 *
 * 목적: 로그인 결과 및 사용자 정보 반환
 *
 * 사용 시점: AuthService -> Controller -> SignInView
 *
 * 필드:
 *   - success (boolean)
 *   - message (String)
 *   - userId (String) - 성공 시
 *   - userName (String) - 성공 시
 *   - grade (int) - 성공 시
 *
 * 이유: 성공/실패 + 사용자 정보를 함께 반환. 여러 정보를 묶어야 함.
 */
public class LoginResponse {

}
