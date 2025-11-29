package com.project.app.service;

import com.project.app.dto.LoginRequest;
import com.project.app.dto.LoginResponse;
import com.project.app.model.User;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 로그인 비즈니스 로직을 처리하는 서비스 클래스
 *
 * - UserData.txt에서 사용자 정보를 읽어서 검증
 * - 형식: 사용자ID/비밀번호/이름/생년월일/학년/신청강의목록/결제내역목록
 */
public class SignInService {

    private static final String USER_DATA_FILE = "UserData.txt";

    public SignInService() {
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

        // ★ UserData.txt에서 사용자 조회
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

    /**
     * UserData.txt 에서 사용자 조회
     * 형식: 사용자ID/비밀번호/이름/생년월일/학년/신청강의목록/결제내역목록
     */
    private User loadUserFromFile(String id) {
        File file = new File(USER_DATA_FILE);

        // 디버그용: 실제로 어디를 보고 있는지 확인
        System.out.println("[SignInService] USER_DATA_FILE 절대 경로: " + file.getAbsolutePath());

        if (!file.exists()) {
            System.err.println("[SignInService] UserData.txt 파일을 찾을 수 없습니다.");
            return null;
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            String line;
            boolean first = true;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // 헤더 스킵
                if (first) {
                    first = false;
                    if (line.startsWith("사용자ID")) {
                        continue;
                    }
                }

                String[] p = line.split("/");
                if (p.length < 5) continue;

                String userId = p[0].trim();
                if (!userId.equals(id)) {
                    continue;
                }

                String pw          = p[1].trim();
                String name        = p[2].trim();
                String birth       = p[3].trim();   // YYYY-MM-DD
                String gradeLabel  = p[4].trim();   // "고1" / "고2" / "고3" / "N수"

                int grade = gradeLabelToInt(gradeLabel);

                return new User(userId, pw, name, grade, birth);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private int gradeLabelToInt(String label) {
        return switch (label) {
            case "고1" -> 1;
            case "고2" -> 2;
            case "고3" -> 3;
            case "N수" -> 4;
            default -> 0;
        };
    }
}
