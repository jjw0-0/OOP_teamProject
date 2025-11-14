package user;

import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

// 파일명과 엔드포인트 수정 필요

public class SignInService extends JFrame {

    private JTextField idField;
    private JPasswordField pwField;
    private Map<String, String> users = new HashMap<>(); // 아이디-비밀번호 저장

    public SignInService() {
        super("ILTAGANGSA - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);

        // users.txt에서 사용자 데이터 로드
        loadUsers();

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        add(root);

        // 타이틀
        JLabel title = new JLabel("ILTAGANGSA", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 36));
        root.add(title, BorderLayout.NORTH);

        // 입력 폼
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

        // 버튼 영역
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

        // 로그인 버튼 이벤트
        loginBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            String pw = new String(pwField.getPassword());
            
            // 빈 값 체크
            if (id.isEmpty() || pw.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID와 PW를 입력하세요.", "오류", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // 아이디 존재 여부 확인
            if (!users.containsKey(id)) {
                JOptionPane.showMessageDialog(this, "등록되지 않은 아이디입니다. 회원가입을 진행해주세요.", "로그인 실패", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 비밀번호 일치 여부 확인
            if (!users.get(id).equals(pw)) {
                JOptionPane.showMessageDialog(this, "비밀번호가 일치하지 않습니다.", "로그인 실패", JOptionPane.ERROR_MESSAGE);
                pwField.setText("");
                pwField.requestFocus();
                return;
            }
            
            // 로그인 성공
            JOptionPane.showMessageDialog(this, "로그인 성공!\n환영합니다, " + id + "님!", "로그인 성공", JOptionPane.INFORMATION_MESSAGE);
            idField.setText("");
            pwField.setText("");
            //new GangsaFrame().setVisible(true);
        });

        // 엔터키로 로그인
        pwField.addActionListener(e -> loginBtn.doClick());
        
        // 회원가입 버튼 이벤트
        signupBtn.addActionListener(e -> {
            //new SignupFrame().setVisible(true);
            this.dispose();
        });
    }

    // users.txt 파일에서 사용자 정보 로드
    // 파일 형식: 아이디,비밀번호,이름,생년월일
    private void loadUsers() {
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String id = parts[0].trim();
                    String pw = parts[1].trim();
                    users.put(id, pw);
                }
            }
        } catch (IOException e) {
            System.out.println("users.txt 파일을 읽을 수 없습니다: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SignInService().setVisible(true));
    }
}