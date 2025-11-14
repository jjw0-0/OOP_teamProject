package javaStudy;

import java.awt.*;
import javax.swing.*;

public class SignInFrame extends JFrame {

    private JTextField idField;
    private JPasswordField pwField;
    private SignInService service;

    public SignInFrame() {
        super("ILTAGANGSA - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);

        service = new SignInService();

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        add(root);

        JLabel title = new JLabel("ILTAGANGSA", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 36));
        root.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        root.add(form, BorderLayout.CENTER);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(8, 0, 8, 0);

        JLabel idLabel = new JLabel("ID");
        idLabel.setFont(new Font("Dialog", Font.BOLD, 18));
        c.gridx = 0; c.gridy = 0; c.anchor = GridBagConstraints.WEST;
        form.add(idLabel, c);

        idField = new JTextField();
        idField.setFont(new Font("Dialog", Font.PLAIN, 16));
        idField.setPreferredSize(new Dimension(280, 35));
        c.gridy = 1;
        form.add(idField, c);

        JLabel pwLabel = new JLabel("PW");
        pwLabel.setFont(new Font("Dialog", Font.BOLD, 18));
        c.gridy = 2; c.insets = new Insets(20, 0, 8, 0);
        form.add(pwLabel, c);

        pwField = new JPasswordField();
        pwField.setFont(new Font("Dialog", Font.PLAIN, 16));
        pwField.setPreferredSize(new Dimension(280, 35));
        c.gridy = 3; c.insets = new Insets(8, 0, 8, 0);
        form.add(pwField, c);

        JPanel actions = new JPanel();
        actions.setLayout(new BoxLayout(actions, BoxLayout.Y_AXIS));
        root.add(actions, BorderLayout.SOUTH);

        JButton loginBtn = new JButton("로그인");
        loginBtn.setFont(new Font("Dialog", Font.BOLD, 18));
        loginBtn.setPreferredSize(new Dimension(280, 45));
        loginBtn.setMaximumSize(new Dimension(280, 45));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setBackground(Color.LIGHT_GRAY);
        actions.add(loginBtn);

        actions.add(Box.createVerticalStrut(15));

        JButton signupBtn = new JButton("회원가입");
        signupBtn.setFont(new Font("Dialog", Font.PLAIN, 14));
        signupBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        signupBtn.setContentAreaFilled(false);
        signupBtn.setBorderPainted(false);
        actions.add(signupBtn);

        loginBtn.addActionListener(e -> handleLogin());

        pwField.addActionListener(e -> loginBtn.doClick());
        
        signupBtn.addActionListener(e -> {
            //new SignupFrame().setVisible(true);
            this.dispose();
        });
    }

    private void handleLogin() {
        String id = idField.getText().trim();
        String pw = new String(pwField.getPassword());
        
        String result = service.login(id, pw);
        
        if (result.equals("SUCCESS")) {
            JOptionPane.showMessageDialog(this, "로그인 성공!\n환영합니다, " + id + "님!", "로그인 성공", JOptionPane.INFORMATION_MESSAGE);
            idField.setText("");
            pwField.setText("");
            //new GangsaFrame().setVisible(true);
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