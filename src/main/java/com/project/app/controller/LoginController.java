package com.project.app.controller;

import javax.swing.JOptionPane;

import com.project.app.dto.LoginRequest;
import com.project.app.dto.LoginResponse;
import com.project.app.model.User;
import com.project.app.service.AuthService;
import com.project.app.service.SignInService;
import com.project.app.view.MyPageView;
import com.project.app.view.SignInView;
import com.project.app.view.SignUpView;
import com.project.app.view.SidePanel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class LoginController {

    private final SignInView view;
    private final SignInService service;

    public LoginController(SignInView view, SignInService service) {
        this.view = view;
        this.service = service;
        initListeners();
    }

    private void initListeners() {
        view.addLoginListener(e -> handleLogin());
        view.addGoToSignUpListener(e -> navigateToSignUp());
    }

    private void handleLogin() {
    	System.out.println("[LoginController] handleLogin() 호출됨");
        try {
            // 1. View에서 DTO로 입력값 가져오기
            LoginRequest request = view.getLoginInput();

            // 2. Service 호출
            LoginResponse response = service.login(request);

            // 3. 결과 처리
            if (response.isSuccess()) {

                // UserData.txt에서 전체 정보를 읽어온 User 생성 시도
                User loggedInUser = loadUserFromFile(response.getUserId(), response.getGrade());

                // 파일에서 못 찾았을 때는 DTO 기반으로 fallback 생성
                if (loggedInUser == null) {
                    loggedInUser = new User(
                            response.getUserId(),
                            request.getPassword(),     // DTO에 들고 있는 PW
                            response.getUserName(),
                            response.getGrade(),
                            null
                    );
                }

                // 현재 로그인 사용자 저장
                AuthService.setCurrentUser(loggedInUser);

                // 마이페이지 내용 갱신 (내 정보 / 내 강의 / 결제 내역)
                MyPageView.getInstance().refresh();

                String gradeLabel = loggedInUser.getGradeLabel();

                String welcome = String.format(
                    "로그인 성공!\n%s님 (%s), 환영합니다.",
                    loggedInUser.getName(),
                    gradeLabel
                );

                JOptionPane.showMessageDialog(
                    view,
                    welcome,
                    "로그인 성공",
                    JOptionPane.INFORMATION_MESSAGE
                );

                view.clearFields();
                navigateToHome();

            } else {
                JOptionPane.showMessageDialog(
                    view,
                    response.getMessage(),
                    "로그인 실패",
                    JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                view,
                "로그인 처리 중 오류가 발생했습니다.\n" + ex.getMessage(),
                "오류",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void navigateToSignUp() {
        SidePanel.getInstance().showContent(SignUpView.getInstance());
        SidePanel.getInstance().setSelectedItem(SidePanel.MenuItem.SIGNUP);
    }

    // HomePageView 대신 마이페이지로 이동
    private void navigateToHome() {
        SidePanel.getInstance().showContent(MyPageView.getInstance());
        SidePanel.getInstance().setSelectedItem(SidePanel.MenuItem.MYPAGE);
    }

    /**
     * UserData.txt에서 해당 ID의 사용자 정보를 읽어서 User 객체로 변환
     * 파일 형식: 사용자ID/비밀번호/이름/생년월일/학년/신청강의ID리스트/결제ID리스트
     */
    private static final String USER_DATA_FILE = "UserData.txt";

    /**
     * UserData.txt에서 해당 ID의 사용자 정보를 읽어서 User 객체로 변환
     * 형식: 사용자ID/비밀번호/이름/생년월일/학년/신청강의목록/결제내역목록
     */
    private User loadUserFromFile(String id, int gradeFromLogin) {
        File file = new File(USER_DATA_FILE);
        System.out.println("[LoginController] USER_DATA_FILE 절대 경로: " + file.getAbsolutePath());

        if (!file.exists()) {
            System.err.println("[LoginController] UserData.txt 파일을 찾을 수 없습니다.");
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

                if (!p[0].trim().equals(id)) {
                    continue;
                }

                String pw         = p[1].trim();
                String name       = p[2].trim();
                String birth      = p[3].trim();
                String gradeLabel = p[4].trim();

                int grade = gradeLabelToInt(gradeLabel);
                // SignInService에서 이미 판정한 gradeFromLogin이 있으면 그걸 우선 사용
                if (gradeFromLogin > 0) {
                    grade = gradeFromLogin;
                }

                return new User(id, pw, name, grade, birth);
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
