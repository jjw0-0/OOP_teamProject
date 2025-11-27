package com.project.app.controller;

import javax.swing.JOptionPane;

import com.project.app.dto.LoginRequest;
import com.project.app.dto.LoginResponse;
import com.project.app.model.User;
import com.project.app.service.AuthService;
import com.project.app.service.SignInService;
import com.project.app.view.HomePageView;
import com.project.app.view.MyPageView;
import com.project.app.view.SignInView;
import com.project.app.view.SignUpView;
import com.project.app.view.SidePanel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
        try {
            // 1. View에서 DTO로 입력값 가져오기
            LoginRequest request = view.getLoginInput();

            // 2. Service 호출
            LoginResponse response = service.login(request);

            // 3. 결과 처리
            if (response.isSuccess()) {

                // users.txt에서 전체 정보를 읽어온 User 생성 시도
                User loggedInUser = loadUserFromFile(response.getUserId(), response.getGrade());

                // 파일에서 못 찾았을 때는 DTO 기반으로 fallback 생성
                if (loggedInUser == null) {
                    loggedInUser = new User(
                            response.getUserId(),
                            request.getPassword(),     // DTO에 들고 있는 PW
                            response.getUserName(),
                            response.getGrade(),
                            null                       // birth 미등록 → MyPageView에서 "미등록" 처리
                    );
                }

                // 현재 로그인 사용자 저장
                AuthService.setCurrentUser(loggedInUser);

                // 마이페이지 내용 갱신 (내 정보 / 내 강의 / 결제 내역)
                MyPageView.getInstance().refresh();

                String gradeLabel = switch (response.getGrade()) {
                    case 1 -> "고1";
                    case 2 -> "고2";
                    case 3 -> "고3";
                    case 4 -> "N수";
                    default -> "미지정";
                };

                String welcome = String.format(
                    "로그인 성공!\n%s님 (%s), 환영합니다.",
                    response.getUserName(),
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

    private void navigateToHome() {
        SidePanel.getInstance().showContent(HomePageView.getInstance());
        SidePanel.getInstance().setSelectedItem(SidePanel.MenuItem.HOME);
    }

    /**
     * users.txt에서 해당 ID의 사용자 정보를 읽어서 User 객체로 변환
     * 파일 형식: id,password,name,birth
     */
    private User loadUserFromFile(String id, int grade) {
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length < 4) continue;

                if (p[0].equals(id)) {
                    // p[3] = birth
                    return new User(
                            p[0],      // id
                            p[1],      // password
                            p[2],      // name
                            grade,     // 로그인 결과로 받은 학년
                            p[3]       // birth
                    );
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
