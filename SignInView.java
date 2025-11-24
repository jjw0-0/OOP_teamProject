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

    // 폰트 상수
    private static final Font TITLE_FONT = new Font("Inknut Antiqua", Font.BOLD, 24);
    private static final Font EN_LABEL_FONT = new Font("Inknut Antiqua", Font.BOLD, 16);
    private static final Font EN_INPUT_FONT = new Font("Inknut Antiqua", Font.PLAIN, 16);
    private static final Font KO_BUTTON_FONT = new Font("맑은 고딕", Font.BOLD, 16);
    private static final Font KO_SUB_FONT = new Font("맑은 고딕", Font.PLAIN, 14);

    // 싱글톤 패턴: private 생성자
    private SignInView() {

        setPreferredSize(new Dimension(400, 500));
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(48, 40, 30, 40));
        root.setBackground(Color.WHITE);
        add(root, BorderLayout.CENTER);

        JLabel title = new JLabel("ILTAGANGSA", SwingConstants.CENTER);
        title.setFont(TITLE_FONT);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 70, 0));
        title.setForeground(Color.BLACK);
        root.add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        center.setBackground(Color.WHITE);
        root.add(center, BorderLayout.CENTER);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        center.add(form);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 0, 0, 0);
        c.fill = GridBagConstraints.NONE;

        // ID Label
        JLabel idLabel = new JLabel("ID");
        idLabel.setFont(EN_LABEL_FONT);
        idLabel.setForeground(Color.BLACK);
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(8, 10, 19, 10);
        form.add(idLabel, c);

        // ID Field
        idField = new JTextField();
        idField.setFont(EN_INPUT_FONT);
        idField.setPreferredSize(new Dimension(180, 40));
        c.gridx = 1;
        c.gridx = 1;
        c.insets = new Insets(8, 0, 19, 10);
        form.add(idField, c);

        // PW Label
        JLabel pwLabel = new JLabel("PW");
        pwLabel.setFont(EN_LABEL_FONT);
        pwLabel.setForeground(Color.BLACK);
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(19, 10, 8, 10);
        form.add(pwLabel, c);

        // PW Field
        pwField = new JPasswordField();
        pwField.setFont(EN_INPUT_FONT);
        pwField.setPreferredSize(new Dimension(180, 40));
        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(19, 0, 8, 10);
        form.add(pwField, c);

        // Buttons
        JPanel actions = new JPanel();
        actions.setLayout(new BoxLayout(actions, BoxLayout.Y_AXIS));
        actions.setBackground(Color.WHITE);
        actions.setBorder(BorderFactory.createEmptyBorder(0, 0, 45, 0));
        root.add(actions, BorderLayout.SOUTH);

        // 로그인 버튼
        loginBtn = new JButton("로그인");
        loginBtn.setFont(KO_BUTTON_FONT);
        loginBtn.setPreferredSize(new Dimension(250, 45));
        loginBtn.setMaximumSize(new Dimension(250, 45));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setBackground(new Color(3, 105, 161));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setOpaque(true);
        loginBtn.setBorderPainted(false);
        loginBtn.setFocusPainted(false);
        actions.add(loginBtn);

        actions.add(Box.createVerticalStrut(10));

        // 회원가입 버튼
        signupBtn = new JButton("회원가입");
        signupBtn.setFont(KO_SUB_FONT);
        signupBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        signupBtn.setContentAreaFilled(false);
        signupBtn.setBorderPainted(false);
        signupBtn.setFocusPainted(false);
        signupBtn.setForeground(Color.BLACK);

        actions.add(Box.createVerticalStrut(10));
        actions.add(signupBtn);
    }

    /** Controller가 로그인 버튼 이벤트를 등록하기 위한 메서드 */
    public void addLoginListener(ActionListener listener) {
        loginBtn.addActionListener(listener);
        pwField.addActionListener(listener); // 엔터키 지원
    }

    /** Controller가 "회원가입" 버튼 이벤트 등록하기 위한 메서드 */
    public void addGoToSignUpListener(ActionListener listener) {
        signupBtn.addActionListener(listener);
    }

    /** 입력값 기반 DTO 생성 */
    public LoginRequest getLoginInput() {
        String id = idField.getText().trim();
        String pw = new String(pwField.getPassword()).trim();
        return new LoginRequest(id, pw);
    }

    /** 로그인 후 입력 초기화 */
    public void clearFields() {
        idField.setText("");
        pwField.setText("");
    }
}
