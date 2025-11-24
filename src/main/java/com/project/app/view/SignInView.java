package com.project.app.view;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

import com.project.app.dto.LoginRequest;

/**
 * 로그인 화면 뷰 (JPanel)
 *
 * 역할:
 * - 로그인 UI 구성
 * - 사용자 입력(ID/PW) 제공
 * - 버튼에 리스너를 붙일 수 있는 메서드 제공
 *
 * 비즈니스 로직, Service/Repository 호출, 화면 전환은 Controller에서 처리한다.
 */
public class SignInView extends JPanel {

    // 싱글톤 패턴: private static 인스턴스 변수
    private static SignInView instance;

    public static SignInView getInstance() {
        if (instance == null) {
            instance = new SignInView();
        }
        return instance;
    }

    private JTextField idField;
    private JPasswordField pwField;
    private JButton loginBtn;
    private JButton signupBtn;

    // 싱글톤 패턴: private 생성자
    private SignInView() {
        // JPanel 기본 설정
        setPreferredSize(new Dimension(400, 500));
        setLayout(new BorderLayout());

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        add(root);

        // 상단 타이틀
        JLabel title = new JLabel("ILTAGANGSA", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 36));
        root.add(title, BorderLayout.NORTH);

        // 중앙 폼 영역
        JPanel form = new JPanel(new GridBagLayout());
        root.add(form, BorderLayout.CENTER);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(8, 0, 8, 0);

        // ID 라벨 + 입력 필드
        JLabel idLabel = new JLabel("ID");
        idLabel.setFont(new Font("Dialog", Font.BOLD, 18));
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        form.add(idLabel, c);

        idField = new JTextField();
        idField.setFont(new Font("Dialog", Font.PLAIN, 16));
        idField.setPreferredSize(new Dimension(280, 35));
        c.gridy = 1;
        form.add(idField, c);

        // PW 라벨 + 입력 필드
        JLabel pwLabel = new JLabel("PW");
        pwLabel.setFont(new Font("Dialog", Font.BOLD, 18));
        c.gridy = 2;
        c.insets = new Insets(20, 0, 8, 0);
        form.add(pwLabel, c);

        pwField = new JPasswordField();
        pwField.setFont(new Font("Dialog", Font.PLAIN, 16));
        pwField.setPreferredSize(new Dimension(280, 35));
        c.gridy = 3;
        c.insets = new Insets(8, 0, 8, 0);
        form.add(pwField, c);

        // 하단 버튼 영역
        JPanel actions = new JPanel();
        actions.setLayout(new BoxLayout(actions, BoxLayout.Y_AXIS));
        root.add(actions, BorderLayout.SOUTH);

        loginBtn = new JButton("로그인");
        loginBtn.setFont(new Font("Dialog", Font.BOLD, 18));
        loginBtn.setPreferredSize(new Dimension(280, 45));
        loginBtn.setMaximumSize(new Dimension(280, 45));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setBackground(Color.LIGHT_GRAY);
        actions.add(loginBtn);

        actions.add(Box.createVerticalStrut(15));

        signupBtn = new JButton("회원가입");
        signupBtn.setFont(new Font("Dialog", Font.PLAIN, 14));
        signupBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        signupBtn.setContentAreaFilled(false);
        signupBtn.setBorderPainted(false);
        actions.add(signupBtn);
    }

    /**
     * Controller가 로그인 버튼 이벤트를 등록하기 위한 메서드
     */
    public void addLoginListener(ActionListener listener) {
        loginBtn.addActionListener(listener);
        // 비밀번호 필드에서 엔터 눌러도 로그인 되게
        pwField.addActionListener(listener);
    }

    /**
     * Controller가 "회원가입" 버튼 클릭 이벤트를 등록하기 위한 메서드
     */
    public void addGoToSignUpListener(ActionListener listener) {
        signupBtn.addActionListener(listener);
    }

    /**
     * 현재 View의 입력값을 기반으로 LoginRequest DTO를 생성
     * Controller → Service로 넘길 때 사용
     */
    public LoginRequest getLoginInput() {
        String id = idField.getText().trim();
        String pw = new String(pwField.getPassword()).trim();
        return new LoginRequest(id, pw);
    }

    /**
     * 로그인 성공 후 입력 필드 초기화할 때 사용
     */
    public void clearFields() {
        idField.setText("");
        pwField.setText("");
    }
}
