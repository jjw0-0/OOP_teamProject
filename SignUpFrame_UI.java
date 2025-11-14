package user;

import javax.swing.*;
import java.awt.*;

public class SignUpFrame_UI extends JFrame {

    public SignUpFrame_UI() {

        setTitle("회원가입");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);  // 화면 정가운데

        JPanel panel = new JPanel();
        panel.setLayout(null);
        add(panel);

        // 타이틀
        JLabel titleLabel = new JLabel("회원가입");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        titleLabel.setBounds(140, 20, 200, 30);
        panel.add(titleLabel);

        // 이름
        JLabel nameLabel = new JLabel("이름");
        nameLabel.setBounds(50, 80, 80, 25);
        panel.add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(150, 80, 180, 25);
        panel.add(nameField);

        // 생년월일
        JLabel birthLabel = new JLabel("생년월일");
        birthLabel.setBounds(50, 130, 80, 25);
        panel.add(birthLabel);

        // 연도
        String[] years = new String[30];
        int idx = 0;
        for (int y = 2000; y <= 2029; y++) {
            years[idx++] = String.valueOf(y);
        }

        JComboBox<String> yearBox = new JComboBox<>(years);
        yearBox.setBounds(150, 130, 60, 25);
        panel.add(yearBox);

        // 월
        String[] months = new String[12];
        for (int m = 1; m <= 12; m++) {
            months[m - 1] = String.valueOf(m);
        }

        JComboBox<String> monthBox = new JComboBox<>(months);
        monthBox.setBounds(220, 130, 50, 25);
        panel.add(monthBox);

        // 일 (기본 1~31일)
        JComboBox<String> dayBox = new JComboBox<>();
        dayBox.setBounds(280, 130, 50, 25);
        panel.add(dayBox);

        // 초기 일자 세팅
        updateDays(yearBox, monthBox, dayBox);

        // 날짜 자동 업데이트 (연도 or 월 변경 시)
        yearBox.addActionListener(e -> updateDays(yearBox, monthBox, dayBox));
        monthBox.addActionListener(e -> updateDays(yearBox, monthBox, dayBox));

        // ID
        JLabel idLabel = new JLabel("ID");
        idLabel.setBounds(50, 180, 80, 25);
        panel.add(idLabel);

        JTextField idField = new JTextField();
        idField.setBounds(150, 180, 180, 25);
        panel.add(idField);

        // PW
        JLabel pwLabel = new JLabel("PW");
        pwLabel.setBounds(50, 230, 80, 25);
        panel.add(pwLabel);

        JPasswordField pwField = new JPasswordField();
        pwField.setBounds(150, 230, 180, 25);
        panel.add(pwField);

        // 버튼
        JButton signUpButton = new JButton("회원가입");
        signUpButton.setBounds(80, 320, 110, 35);
        panel.add(signUpButton);

        JButton cancelButton = new JButton("취소");
        cancelButton.setBounds(210, 320, 110, 35);
        panel.add(cancelButton);

        setVisible(true);
    }
    
    private void updateDays(JComboBox<String> yearBox, JComboBox<String> monthBox, JComboBox<String> dayBox) {
        if (yearBox.getSelectedItem() == null || monthBox.getSelectedItem() == null) return;

        int year = Integer.parseInt(yearBox.getSelectedItem().toString());
        int month = Integer.parseInt(monthBox.getSelectedItem().toString());

        int days = getDaysInMonth(year, month);

        dayBox.removeAllItems();
        for (int d = 1; d <= days; d++) {
            dayBox.addItem(String.valueOf(d));
        }
    }
 
    private int getDaysInMonth(int year, int month) {
        switch (month) {
            case 2: // 2월
                boolean leap = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
                return leap ? 29 : 28;
            case 4: case 6: case 9: case 11: // 30일
                return 30;
            default: // 그 외 31일
                return 31;
        }
    }

    public static void main(String[] args) {
        new SignUpFrame_UI();
    }
}
