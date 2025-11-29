package com.project.app.dto;

/**
 * 회원가입 요청 DTO
 *
 * 목적: 회원가입 요청 데이터
 *
 * 사용 시점: SignUpView -> Controller -> AuthService(또는 SignUpService 적용 가능)
 *
 * 필드:
 *   - userId (String)
 *   - password (String)
 *   - name (String)
 *   - birthDate (String)
 *   - grade (int)
 */
public class RegisterRequest {

    private String userId;
    private String password;
    private String name;
    private String birthDate;
    private int grade;

    public RegisterRequest(String userId, String password, String name, String birthDate, int grade) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.birthDate = birthDate;
        this.grade = grade;
    }

    public String getUserId() { return userId; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public String getBirthDate() { return birthDate; }
    public int getGrade() { return grade; }
}