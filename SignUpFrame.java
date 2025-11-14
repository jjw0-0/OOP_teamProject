package user;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Calendar;

public class SignUpFrame extends JFrame {

    private JTextField tfId, tfName;
    private JPasswordField pfPw;
    private JComboBox<Integer> cbYear, cbMonth, cbDay;
    private JButton btnSignUp, btnCancel;

    public SignUpFrame() {
        setTitle("회원가입");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLayout(null);

        JLabel lblTitle = new JLabel("ILTAGANGSA 회원가입", SwingConstants.CENTER);
        lblTitle.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        lblTitle.setBounds(0, 20, 400, 40);
        add(lblTitle);

        int labelX = 60;
        int fieldX = 150;
        int fieldWidth = 180;
        int height = 30;
        int gap = 45;

        // 이름
        JLabel lblName = new JLabel("이름:");
        lblName.setBounds(labelX, 90, 80, height);
        add(lblName);

        tfName = new JTextField();
        tfName.setBounds(fieldX, 90, fieldWidth, height);
        add(tfName);

        // 생년월일
        JLabel lblBirth = new JLabel("생년월일:");
        lblBirth.setBounds(labelX, 90 + gap, 80, height);
        add(lblBirth);

        cbYear = new JComboBox<>();
        cbMonth = new JComboBox<>();
        cbDay = new JComboBox<>();

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int y = 2000; y <= currentYear; y++) cbYear.addItem(y);
        for (int m = 1; m <= 12; m++) cbMonth.addItem(m);
        updateDays();

        cbYear.addActionListener(e -> updateDays());
        cbMonth.addActionListener(e -> updateDays());

        int yearWidth = 70;
        int monthWidth = 50;
        int dayWidth = 50;
        int spacing = 5;

        cbYear.setBounds(fieldX, 90 + gap, yearWidth, height);
        cbMonth.setBounds(fieldX + yearWidth + spacing, 90 + gap, monthWidth, height);
        cbDay.setBounds(fieldX + yearWidth + monthWidth + spacing * 2, 90 + gap, dayWidth, height);

        add(cbYear);
        add(cbMonth);
        add(cbDay);

        // 아이디
        JLabel lblId = new JLabel("아이디:");
        lblId.setBounds(labelX, 90 + gap * 2, 80, height);
        add(lblId);

        tfId = new JTextField();
        tfId.setBounds(fieldX, 90 + gap * 2, fieldWidth, height);
        add(tfId);

        // 비밀번호
        JLabel lblPw = new JLabel("비밀번호:");
        lblPw.setBounds(labelX, 90 + gap * 3, 80, height);
        add(lblPw);

        pfPw = new JPasswordField();
        pfPw.setBounds(fieldX, 90 + gap * 3, fieldWidth, height);
        add(pfPw);

        // 버튼
        btnSignUp = new JButton("회원가입");
        btnCancel = new JButton("취소");

        btnSignUp.setBounds(100, 90 + gap * 4 + 10, 100, 40);
        btnCancel.setBounds(210, 90 + gap * 4 + 10, 100, 40);

        add(btnSignUp);
        add(btnCancel);

        // 이벤트 등록
        btnSignUp.addActionListener(e -> handleSignUp());
        btnCancel.addActionListener(e -> handleCancel());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateDays() {
        int selectedYear = (int) cbYear.getSelectedItem();
        int selectedMonth = (int) cbMonth.getSelectedItem();
        int daysInMonth = getDaysInMonth(selectedYear, selectedMonth);

        cbDay.removeAllItems();
        for (int d = 1; d <= daysInMonth; d++) cbDay.addItem(d);
    }

    private int getDaysInMonth(int year, int month) {
        switch (month) {
            case 2:
                return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0) ? 29 : 28;
            case 4: case 6: case 9: case 11:
                return 30;
            default:
                return 31;
        }
    }

    private void handleSignUp() {
        String name = tfName.getText().trim();
        String id = tfId.getText().trim();
        String password = new String(pfPw.getPassword()).trim();

        // 빈칸 검사
        if (name.isEmpty() || id.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "모든 정보를 입력하세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 비밀번호 특수문자 체크
        String forbidden = "\"'\\/:;,=?*";
        for (char c : password.toCharArray()) {
            if (forbidden.indexOf(c) != -1) {
                JOptionPane.showMessageDialog(this,
                        "비밀번호에 사용할 수 없는 특수문자가 포함되어 있습니다.\n" +
                                "사용 불가 문자: \" ' \\ / : ; , = ? *",
                        "비밀번호 오류",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // 비밀번호 길이 검사
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this,
                    "비밀번호는 6자리 이상이어야 합니다.",
                    "비밀번호 오류",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 생년월일
        int year = (int) cbYear.getSelectedItem();
        int month = (int) cbMonth.getSelectedItem();
        int day = (int) cbDay.getSelectedItem();
        String birth = String.format("%04d-%02d-%02d", year, month, day);

        // ID 중복 검사
        if (isDuplicateId(id)) {
            JOptionPane.showMessageDialog(this,
                    "이미 존재하는 아이디입니다.",
                    "중복 오류",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 파일 저장
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", true))) {
            writer.write(id + "," + password + "," + name + "," + birth);
            writer.newLine();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "회원정보 저장 중 오류가 발생했습니다.",
                    "파일 오류",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "회원가입이 완료되었습니다!");
        clearFields();
    }

    private boolean isDuplicateId(String id) {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(id)) {
                    return true;
                }
            }
        } catch (IOException e) {
            // 파일이 없으면 신규 가입으로 처리
        }
        return false;
    }

    private void handleCancel() {
        dispose(); // 현재 창 닫기
        // 필요하면 이전 화면 호출 가능
    }

    private void clearFields() {
        tfName.setText("");
        tfId.setText("");
        pfPw.setText("");
        cbYear.setSelectedIndex(0);
        cbMonth.setSelectedIndex(0);
        cbDay.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        new SignUpFrame();
    }
}
