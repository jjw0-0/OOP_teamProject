package com.project.app.dto;

/**
 * 회원가입 요청 DTO
 *
 * 목적: 회원가입 요청 데이터
 *
 * 사용 시점: SignUpView -> Controller -> AuthService
 *
 * 필드:
 *   - userId (String)
 *   - password (String)
 *   - name (String)
 *   - birthDate (String)
 *   - grade (int)
 *
 * 이유: 5개의 입력 필드를 묶어 전달. DTO 없으면 매개변수 5개 필요.
 */
public class RegisterRequest {

}
