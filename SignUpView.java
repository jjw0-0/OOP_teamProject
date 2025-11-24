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
 *
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

    // ====== SignInView와 폰트/크기 통일 ======
    private static final Font TITLE_FONT = new Font("Inknut Antiqua", Font.BOLD, 24);
    private static final Font KO_LABEL_FONT = new Font("맑은 고딕", Font.BOLD, 16);
    private static final Font INPUT_FONT = new Font("맑은 고딕", Font.PLAIN, 16);
    private static final Font KO_BUTTON_FONT = new Font("맑은 고딕", Font.BOLD, 16);

    // 싱글톤 패턴: private 생성자
    private SignUpView() {

        service = new SignUpService();

        setPreferredSize(new Dimension(400, 500));
        setLayout(new BorderLayout());
        setBackground(Color.WHITE); // 배경색 white

        // SignInView와 동일한 root 구조
        JPanel root = new JPanel(new BorderLayout());
        root.setBorder(BorderFactory.createEmptyBorder(48, 40, 30, 40)); // 상단/좌우 동일
        root.setBackground(Color.WHITE);
        add(root, BorderLayout.CENTER);

        // 타이틀 (위치/폰트 로그인과 통일)
        JLabel title = new JLabel("ILTAGANGSA 회원가입", SwingConstants.CENTER);
        title.setFont(TITLE_FONT);
        title.setForeground(Color.BLACK);

        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 55, 0));
        root.add(title, BorderLayout.NORTH);

        // 중앙정렬 컨테이너
        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        center.setBackground(Color.WHITE);
        root.add(center, BorderLayout.CENTER);

        // 폼 영역
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        center.add(form);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(0, 0, 0, 0);

        int fieldW = 180;  // ID/PW 입력 박스 크기
        int fieldH = 40;   // ID/PW 입력 박스 높이
        int gap = 18;      // 요소 간 간격 18px

        int row = 0;

        // ======================= 이름 =======================
        addLabel(form, c, "이름", row, gap);
        tfName = createTextField(fieldW, fieldH);
        addField(form, c, tfName, row++, gap);

        // ======================= 생년월일 =======================
        addLabel(form, c, "생년월일", row, gap);

        cbYear = new JComboBox<>();
        cbMonth = new JComboBox<>();
        cbDay = new JComboBox<>();

        // 생년월일 콤보박스 크기 유지(높이 fieldH 동일)
        cbYear.setPreferredSize(new Dimension(70, fieldH));
        cbMonth.setPreferredSize(new Dimension(50, fieldH));
        cbDay.setPreferredSize(new Dimension(50, fieldH));

        cbYear.setFont(INPUT_FONT);
        cbMonth.setFont(INPUT_FONT);
        cbDay.setFont(INPUT_FONT);

        JPanel birthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
        birthPanel.setBackground(Color.WHITE);
        birthPanel.add(cbYear);
        birthPanel.add(cbMonth);
        birthPanel.add(cbDay);

        addField(form, c, birthPanel, row++, gap);

        // 콤보박스 날짜 설정
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int y = 2000; y <= currentYear; y++) cbYear.addItem(y);
        for (int m = 1; m <= 12; m++) cbMonth.addItem(m);
        updateDays();
        cbYear.addActionListener(e -> updateDays());
        cbMonth.addActionListener(e -> updateDays());

        // ======================= ID =======================
        addLabel(form, c, "ID", row, gap);
        tfId = createTextField(fieldW, fieldH);
        addField(form, c, tfId, row++, gap);

        // ======================= PW =======================
        addLabel(form, c, "PW", row, gap);
        pfPw = new JPasswordField();
        pfPw.setFont(INPUT_FONT);
        pfPw.setPreferredSize(new Dimension(fieldW, fieldH));
        addField(form, c, pfPw, row++, gap);

        // ======================= 버튼 영역 =======================
        JPanel actions = new JPanel();
        actions.setLayout(new BoxLayout(actions, BoxLayout.Y_AXIS));
        actions.setBackground(Color.WHITE);


        actions.setBorder(BorderFactory.createEmptyBorder(0, 0, 95, 0));
        root.add(actions, BorderLayout.SOUTH);

        btnSignUp = new JButton("회원가입 완료");
        btnSignUp.setFont(KO_BUTTON_FONT);
        btnSignUp.setPreferredSize(new Dimension(250, 45));
        btnSignUp.setMaximumSize(new Dimension(250, 45));
        btnSignUp.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnSignUp.setBackground(new Color(3, 105, 161));
        btnSignUp.setForeground(Color.WHITE);
        btnSignUp.setOpaque(true);
        btnSignUp.setBorderPainted(false);
        btnSignUp.setFocusPainted(false);

        actions.add(btnSignUp);

        // 이벤트: 회원가입
        btnSignUp.addActionListener(e -> {
            service.handleSignUp(tfId, pfPw, tfName, cbYear, cbMonth, cbDay, this);
        });
    }

    private void addLabel(JPanel form, GridBagConstraints c, String text, int row, int gap) {
        JLabel label = new JLabel(text);
        label.setFont(KO_LABEL_FONT);
        label.setForeground(Color.BLACK);

        c.gridx = 0;
        c.gridy = row;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(0, 10, gap, 10);
        form.add(label, c);
    }

    private void addField(JPanel form, GridBagConstraints c, Component field, int row, int gap) {
        c.gridx = 1;
        c.gridy = row;
        c.insets = new Insets(0, 0, gap, 10);
        form.add(field, c);
    }

    /** 텍스트필드 생성 Helper */
    private JTextField createTextField(int w, int h) {
        JTextField tf = new JTextField();
        tf.setFont(INPUT_FONT);
        tf.setPreferredSize(new Dimension(w, h));
        return tf;
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
