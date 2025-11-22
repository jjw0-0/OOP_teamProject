package javaStudy;

import java.awt.*;
import javax.swing.*;

public class SignInFrame extends JFrame {

    private JTextField idField;
    private JPasswordField pwField;
    private SignInService service;

    public SignInFrame() {
        super("ILTAGANGSA - 로그인");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        service = new SignInService();

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(48, 40, 30, 40));
        root.setBackground(Color.WHITE);
        add(root);

        // 로고
        JLabel title = new JLabel("ILTAGANGSA", SwingConstants.CENTER);
        title.setFont(new Font("Inknut Antiqua", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
        root.add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        center.setBackground(Color.WHITE);
        root.add(center, BorderLayout.CENTER);

        // 폼 영역
        JPanel form = new JPanel(new GridBagLayout());
        center.add(form);

        form.setBackground(Color.WHITE);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 0, 0, 0);
        c.fill = GridBagConstraints.NONE;

        JLabel idLabel = new JLabel("ID");
        idLabel.setFont(new Font("Inknut Antiqua", Font.BOLD, 16));
        idLabel.setForeground(Color.BLACK);
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 0;
        c.insets = new Insets(8, 10, 19, 10);   // 아래 여백
        form.add(idLabel, c);

        idField = new JTextField();
        idField.setFont(new Font("Inknut Antiqua", Font.PLAIN, 16));
        idField.setPreferredSize(new Dimension(180, 40));
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(8, 0, 19, 10);
        form.add(idField, c);

        JLabel pwLabel = new JLabel("PW");
        pwLabel.setFont(new Font("Inknut Antiqua", Font.BOLD, 16));
        pwLabel.setForeground(Color.BLACK);
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(19, 10, 8, 10);
        form.add(pwLabel, c);

        pwField = new JPasswordField();
        pwField.setFont(new Font("Inknut Antiqua", Font.PLAIN, 16));
        pwField.setPreferredSize(new Dimension(180, 40));
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(19, 0, 8, 10);
        form.add(pwField, c);

        // 버튼 영역
        JPanel actions = new JPanel();
        actions.setLayout(new BoxLayout(actions, BoxLayout.Y_AXIS));
        actions.setBackground(Color.WHITE);
        actions.setBorder(BorderFactory.createEmptyBorder(0, 0, 69, 0));
        root.add(actions, BorderLayout.SOUTH);

        JButton loginBtn = new JButton("로그인");
        loginBtn.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        loginBtn.setPreferredSize(new Dimension(250, 45));
        loginBtn.setMaximumSize(new Dimension(250, 45));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginBtn.setBackground(new Color(3, 105, 161));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setOpaque(true);
        loginBtn.setBorderPainted(false);
        loginBtn.setFocusPainted(false);
        actions.add(loginBtn);

        JButton signupBtn = new JButton("회원가입");
        signupBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        signupBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        signupBtn.setContentAreaFilled(false);
        signupBtn.setBorderPainted(false);
        actions.add(signupBtn);

        loginBtn.addActionListener(e -> handleLogin());
        pwField.addActionListener(e -> loginBtn.doClick());
        signupBtn.addActionListener(e -> 
        // TODO: SignupFrame 연결
        this.dispose());

        setSize(400, 500);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void handleLogin() {
        String id = idField.getText().trim();
        String pw = new String(pwField.getPassword());

        String result = service.login(id, pw);

        if (result.equals("SUCCESS")) {
            JOptionPane.showMessageDialog(this, "로그인 성공!\n환영합니다, " + id + "님!", "로그인 성공", JOptionPane.INFORMATION_MESSAGE);
            idField.setText("");
            pwField.setText("");
         // TODO: GangsaFrame 연결
        } else if (result.equals("EMPTY")) {
            JOptionPane.showMessageDialog(this, "ID와 PW를 입력하세요.", "오류", JOptionPane.WARNING_MESSAGE);
        } else if (result.equals("USER_NOT_FOUND")) {
            JOptionPane.showMessageDialog(this, "등록되지 않은 아이디입니다. 회원가입을 진행해주세요.", "로그인 실패", JOptionPane.ERROR_MESSAGE);
        } else if (result.equals("WRONG_PASSWORD")) {
            JOptionPane.showMessageDialog(this, "비밀번호가 일치하지 않습니다.", "로그인 실패", JOptionPane.ERROR_MESSAGE);
            pwField.setText("");
            pwField.requestFocus();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SignInFrame().setVisible(true));
    }
}
