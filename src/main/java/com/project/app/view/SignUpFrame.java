package user;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;

// UI 전용
public class SignUpFrame extends JFrame {

    JTextField tfId, tfName;
    JPasswordField pfPw;
    JComboBox<Integer> cbYear, cbMonth, cbDay;
    JButton btnSignUp, btnCancel;

    private SignUpService service; // 로직 클래스

    public SignUpFrame() {
        service = new SignUpService(); // 서비스 객체 생성

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

        // ID
        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(labelX, 90 + gap * 2, 80, height);
        add(lblId);

        tfId = new JTextField();
        tfId.setBounds(fieldX, 90 + gap * 2, fieldWidth, height);
        add(tfId);

        // PW
        JLabel lblPw = new JLabel("PW:");
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
        btnSignUp.addActionListener(e -> service.handleSignUp(tfId, pfPw, tfName, cbYear, cbMonth, cbDay, this));
        btnCancel.addActionListener(e -> service.handleCancel(this));

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateDays() {
        int selectedYear = (int) cbYear.getSelectedItem();
        int selectedMonth = (int) cbMonth.getSelectedItem();
        int daysInMonth = service.getDaysInMonth(selectedYear, selectedMonth);

        cbDay.removeAllItems();
        for (int d = 1; d <= daysInMonth; d++) cbDay.addItem(d);
    }

    public static void main(String[] args) {
        new SignUpFrame();
    }
}
