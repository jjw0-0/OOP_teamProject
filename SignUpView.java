package com.project.app.view;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import com.project.app.service.SignUpService;

/**
 * 회원가입 화면 뷰 (JPanel)
 *
 * 기능:
 * - SidePanel 우측 콘텐츠 영역에 들어갈 "회원가입" 화면
 * - 사용자 정보 입력 (이름, 생년월일, ID, PW)
 * - 회원가입 버튼 제공
 * - 싱글톤 패턴을 사용하여 애플리케이션 전체에서 하나의 인스턴스만 유지
 */
public class SignUpView extends JPanel {

    private static SignUpView instance;

    public static SignUpView getInstance() {
        if (instance == null) {
            instance = new SignUpView();
        }
        return instance;
    }

    JTextField tfId, tfName;
    JPasswordField pfPw;
    JComboBox<Integer> cbYear, cbMonth, cbDay;
    JButton btnSignUp;

    private SignUpService service;

    private SignUpView() {

        service = new SignUpService();

        setPreferredSize(new Dimension(400, 500));
        setLayout(null);
        setBackground(Color.WHITE); // 배경색 white

        int labelX = 60;
        int fieldX = 150;
        int fieldWidth = 180;   // ID/PW 입력 박스 크기
        int fieldHeight = 40;
        int margin = 18;        // 요소 간 간격

        // 타이틀
        JLabel lblTitle = new JLabel("ILTAGANGSA 회원가입", SwingConstants.CENTER);
        lblTitle.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        lblTitle.setBounds(0, 48, 400, 40);  // 위쪽 여백 48px
        add(lblTitle);

        // 이름
        int nameY = 48 + 40 + margin; // 48 + 타이틀 높이 + margin = 106
        JLabel lblName = new JLabel("이름");
        lblName.setBounds(labelX, nameY, 80, 30);
        add(lblName);

        tfName = new JTextField();
        tfName.setBounds(fieldX, nameY, fieldWidth, fieldHeight);
        add(tfName);

        // 생년월일
        int birthY = nameY + fieldHeight + margin; // 106 + 40 + 18 = 164
        JLabel lblBirth = new JLabel("생년월일");
        lblBirth.setBounds(labelX, birthY, 80, 30);
        add(lblBirth);

        cbYear = new JComboBox<>();
        cbMonth = new JComboBox<>();
        cbDay = new JComboBox<>();

        int yearWidth = 66, monthWidth = 49, dayWidth = 49, spacing = 8;

        cbYear.setBounds(fieldX, birthY, yearWidth, fieldHeight);
        cbMonth.setBounds(fieldX + yearWidth + spacing, birthY, monthWidth, fieldHeight);
        cbDay.setBounds(fieldX + yearWidth + monthWidth + spacing * 2, birthY, dayWidth, fieldHeight);

        add(cbYear);
        add(cbMonth);
        add(cbDay);

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int y = 2000; y <= currentYear; y++) cbYear.addItem(y);
        for (int m = 1; m <= 12; m++) cbMonth.addItem(m);

        updateDays();
        cbYear.addActionListener(e -> updateDays());
        cbMonth.addActionListener(e -> updateDays());

        // ID
        int idY = birthY + fieldHeight + margin; // 164 + 40 + 18 = 222
        JLabel lblId = new JLabel("ID");
        lblId.setBounds(labelX, idY, 80, 30);
        add(lblId);

        tfId = new JTextField();
        tfId.setBounds(fieldX, idY, fieldWidth, fieldHeight);
        add(tfId);

        // PW
        int pwY = idY + fieldHeight + margin; // 222 + 40 + 18 = 280
        JLabel lblPw = new JLabel("PW");
        lblPw.setBounds(labelX, pwY, 80, 30);
        add(lblPw);

        pfPw = new JPasswordField();
        pfPw.setBounds(fieldX, pwY, fieldWidth, fieldHeight);
        add(pfPw);

        // 회원가입 버튼 (하단 여백 69px)
        int btnY = 500 - 69 - 45; // 500 패널 높이 - 69px 여백 - 45 버튼 높이 = 386
        btnSignUp = new JButton("회원가입 완료");

        Color themeBlue = new Color(3, 105, 161);
        btnSignUp.setBackground(themeBlue);
        btnSignUp.setForeground(Color.WHITE);
        btnSignUp.setFocusPainted(false);
        btnSignUp.setBounds(75, btnY, 250, 45);
        add(btnSignUp);

        // 이벤트: 회원가입
        btnSignUp.addActionListener(e -> {
            service.handleSignUp(tfId, pfPw, tfName, cbYear, cbMonth, cbDay, this);
        });
    }

    /** 생년월일 - 일(day) 콤보박스 업데이트 */
    private void updateDays() {
        int year = (int) cbYear.getSelectedItem();
        int month = (int) cbMonth.getSelectedItem();
        int days = service.getDaysInMonth(year, month);

        cbDay.removeAllItems();
        for (int d = 1; d <= days; d++) cbDay.addItem(d);
    }
}
