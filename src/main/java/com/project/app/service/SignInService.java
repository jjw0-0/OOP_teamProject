package com.project.app.service;

import com.project.app.dto.LoginRequest;
import com.project.app.dto.LoginResponse;
import com.project.app.model.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * 로그인 비즈니스 로직을 처리하는 서비스 클래스
 *
 * 역할:
 * - LoginRequest 검증
 * - users.txt 파일에서 사용자 조회
 * - 비밀번호 검증
 * - 결과를 LoginResponse DTO로 반환
 *
 * 변경 사항:
 * - users.txt에서 grade까지 읽어와 User에 반영
 *   파일 형식: id,password,name,birth,grade
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
                user.getGrade()   // ✅ 이제 실제 grade (1~4 또는 0)가 들어감
        );
    }

    /**
     * users.txt 에서 사용자 조회
     * 형식: id,password,name,birth,grade
     * 기존 4개 컬럼(id,password,name,birth)만 있는 경우도 대비
     */
    private User loadUserFromFile(String id) {
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");

                if (p.length < 4) continue; // 최소 id,pw,name,birth

                if (p[0].equals(id)) {
                    String birth = p[3];
                    int grade = 0;

                    // 새 형식: id,password,name,birth,grade
                    if (p.length >= 5) {
                        try {
                            grade = Integer.parseInt(p[4]);
                        } catch (NumberFormatException e) {
                            grade = 0;
                        }
                    }

                    return new User(p[0], p[1], p[2], grade, birth);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
