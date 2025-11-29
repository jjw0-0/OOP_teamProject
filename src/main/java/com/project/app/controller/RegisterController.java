package com.project.app.controller;

import com.project.app.dto.RegisterRequest;
import com.project.app.dto.RegisterResponse;
import com.project.app.model.User;
import com.project.app.repository.UserRepository;

/**
 * 회원가입 Controller
 *
 * SignUpView의 기존 구조를 유지하면서
 * DTO 기반 회원가입 요청 처리를 가능하게 하는 역할
 */
public class RegisterController {

    private final UserRepository userRepository;

    public RegisterController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public RegisterResponse register(RegisterRequest request) {

        // 빈 값 체크
        if (request.getUserId().isEmpty() ||
            request.getPassword().isEmpty() ||
            request.getName().isEmpty() ||
            request.getBirthDate().isEmpty()) {
            return new RegisterResponse(
                    false,
                    "모든 정보를 입력하세요.",
                    RegisterResponse.ErrorType.EMPTY_FIELD
            );
        }

        // 비밀번호 검증
        if (request.getPassword().length() < 6) {
            return new RegisterResponse(
                    false,
                    "비밀번호는 6자리 이상이어야 합니다.",
                    RegisterResponse.ErrorType.INVALID_PASSWORD
            );
        }

        // 중복 체크
        if (userRepository.findById(request.getUserId()) != null) {
            return new RegisterResponse(
                    false,
                    "이미 존재하는 아이디입니다.",
                    RegisterResponse.ErrorType.DUPLICATE_ID
            );
        }

        // 저장
        try {
            User user = new User(
                    request.getUserId(),
                    request.getPassword(),
                    request.getName(),
                    request.getGrade(),
                    request.getBirthDate()
            );

            userRepository.save(user);

            return new RegisterResponse(true, "회원가입 완료!");
        } catch (Exception e) {
            return new RegisterResponse(
                    false,
                    "회원정보 저장 중 오류 발생",
                    RegisterResponse.ErrorType.SYSTEM_ERROR
            );
        }
    }
}