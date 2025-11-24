package com.project.app.service;

import com.project.app.dto.LoginRequest;
import com.project.app.dto.LoginResponse;
import com.project.app.model.User;
import com.project.app.repository.UserRepository;

/**
 * 로그인 비즈니스 로직을 처리하는 서비스 클래스
 *
 * 역할:
 * - LoginRequest 검증
 * - UserRepository를 통해 사용자 조회
 * - 비밀번호 검증
 * - 결과를 LoginResponse DTO로 반환
 *
 * UI, View, Controller에 대한 의존성 없음
 */
public class SignInService {

    private final UserRepository userRepository;

    public SignInService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 로그인 처리
     * [검증 → 처리 → 응답] 패턴
     */
    public LoginResponse login(LoginRequest request) {
        // 1. 요청 객체 자체 검증
        if (request == null) {
            return LoginResponse.failure("ID와 비밀번호를 입력하세요.");
        }

        String id = request.getUserId();
        String password = request.getPassword();

        // 2. 기본 값 검증
        if (id == null || id.isBlank() || password == null || password.isBlank()) {
            return LoginResponse.failure("ID와 비밀번호를 입력하세요.");
        }

        // 3. 사용자 조회
        User user = userRepository.findById(id);
        if (user == null) {
            return LoginResponse.failure("등록되지 않은 아이디입니다. 회원가입을 진행해주세요.");
        }

        // 4. 비밀번호 검증
        if (!user.getPassword().equals(password)) {
            return LoginResponse.failure("비밀번호가 일치하지 않습니다.");
        }

        // 5. 성공 응답 생성
        return LoginResponse.success(
                user.getId(),
                user.getName(),
                user.getGrade() // grade 필드가 없다면 일단 0 등으로 처리해도 됨
        );
    }
}
