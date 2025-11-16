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
 * - 회원가입/취소 버튼 제공
 *
 */
public class SignUpView extends JPanel {

    JTextField tfId, tfName;
    JPasswordField pfPw;
    JComboBox<Integer> cbYear, cbMonth, cbDay;
    JButton btnSignUp, btnCancel;

    private SignUpService service; // 로직 클래스

    public SignUpView() {
        service = new SignUpService(); // 서비스 객체 생성

        // JPanel 기본 설정 (기존 JFrame 크기와 유사하게 설정)
        setPreferredSize(new Dimension(400, 500));
        setLayout(null); // 기존 null layout 유지

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

        // 취소 버튼 클릭 시 로그인 화면으로 전환
        btnCancel.addActionListener(e -> {
            SidePanel.getInstance().showContent(new SignInView());
            SidePanel.getInstance().setSelectedItem(SidePanel.MenuItem.LOGIN);
        });
    }

    /**
     * 생년월일의 일(day) 콤보박스를 업데이트하는 메서드
     *
     * 동작 원리:
     * - 선택된 연도와 월에 따라 해당 월의 날짜 수를 계산
     * - SignUpService의 getDaysInMonth 메서드를 활용하여 윤년 처리
     * - 콤보박스를 다시 채워서 사용자가 올바른 날짜를 선택할 수 있도록 함
     */
    private void updateDays() {
        int selectedYear = (int) cbYear.getSelectedItem();
        int selectedMonth = (int) cbMonth.getSelectedItem();
        int daysInMonth = service.getDaysInMonth(selectedYear, selectedMonth);

        cbDay.removeAllItems();
        for (int d = 1; d <= daysInMonth; d++) cbDay.addItem(d);
    }
}
