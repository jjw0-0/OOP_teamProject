package com.project.app.controller;

import javax.swing.JOptionPane;

import com.project.app.dto.LoginRequest;
import com.project.app.dto.LoginResponse;
import com.project.app.service.SignInService;
import com.project.app.view.HomePageView;
import com.project.app.view.SignInView;
import com.project.app.view.SignUpView;
import com.project.app.view.SidePanel;

/**
 * 로그인 화면을 제어하는 Controller.
 *
 * 역할:
 * - SignInView의 이벤트 리스너 등록
 * - View에서 입력값을 읽어 LoginRequest DTO로 변환
 * - SignInService를 호출하여 로그인 로직 실행
 * - LoginResponse DTO를 받아 View 업데이트 및 화면 전환
 */
public class LoginController {

    private final SignInView view;
    private final SignInService service;

    public LoginController(SignInView view, SignInService service) {
        this.view = view;
        this.service = service;
        initListeners();
    }

    /**
     * View의 버튼/입력 컴포넌트에 이벤트 리스너를 등록하는 메서드
     */
    private void initListeners() {
        view.addLoginListener(e -> handleLogin());

        view.addGoToSignUpListener(e -> navigateToSignUp());
    }

    /**
     * 로그인 처리 메서드
     *
     * 순서:
     *  1. View에서 LoginRequest DTO 가져오기
     *  2. Service.login(request) 호출
     *  3. LoginResponse에 따라 메시지/화면 전환
     */
    private void handleLogin() {
        try {
            // 1. View에서 DTO로 입력값 가져오기
            LoginRequest request = view.getLoginInput();

            // 2. Service 호출
            LoginResponse response = service.login(request);

            // 3. 결과에 따른 처리
            if (response.isSuccess()) {
                String welcome = String.format(
                        "로그인 성공!\n%s님 (%s학년), 환영합니다.",
                        response.getUserName(),
                        response.getGrade()
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

    /**
     * 회원가입 화면으로 전환
     */
    private void navigateToSignUp() {
        SidePanel.getInstance().showContent(SignUpView.getInstance());
        SidePanel.getInstance().setSelectedItem(SidePanel.MenuItem.SIGNUP);
    }

    /**
     * 홈 화면으로 전환 (로그인 성공 후)
     */
    private void navigateToHome() {
        SidePanel.getInstance().showContent(HomePageView.getInstance());
        SidePanel.getInstance().setSelectedItem(SidePanel.MenuItem.HOME);
    }
}
