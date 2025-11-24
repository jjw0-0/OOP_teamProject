package com.project.app.service;

import com.project.app.dto.LoginRequest;
import com.project.app.dto.LoginResponse;
import com.project.app.model.User;
import com.project.app.repository.UserRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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

    public SignInService() {
        // 기존 loadUsers() 제거
    }

    /**
     * 로그인 처리
     */
    public LoginResponse login(LoginRequest request) {

        if (request == null) {
            return LoginResponse.failure("ID와 비밀번호를 입력하세요.");
        }

        String id = request.getUserId();
        String password = request.getPassword();

        if (id == null || id.isBlank() || password == null || password.isBlank()) {
            return LoginResponse.failure("ID와 비밀번호를 입력하세요.");
        }

        // ★ users.txt 직접 읽기
        User user = loadUserFromFile(id);

        if (user == null) {
            return LoginResponse.failure("등록되지 않은 아이디입니다. 회원가입을 진행해주세요.");
        }

        if (!user.getPassword().equals(password)) {
            return LoginResponse.failure("비밀번호가 일치하지 않습니다.");
        }

        return LoginResponse.success(
                user.getId(),
                user.getName(),
                user.getGrade()
        );
    }

    private User loadUserFromFile(String id) {
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");

                if (p.length < 4) continue;

                if (p[0].equals(id)) {
                    // grade 정보는 없음 → default 0 사용
                    return new User(p[0], p[1], p[2], 0, p[3]);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
