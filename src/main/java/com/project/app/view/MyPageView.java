package com.project.app.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.project.app.model.User;
import com.project.app.model.Payment;
import com.project.app.dto.MyLectureView;
import com.project.app.dto.UserProfileResponse;
import com.project.app.service.AuthService;
import com.project.app.service.UserService;

/**
 * 마이페이지 뷰
 *
 * 기능:
 * - 사용자의 개인 정보, 수강 중인 강의, 결제 내역을 표시하는 콘텐츠 패널
 * - SidePanel의 우측 콘텐츠 영역에 표시될 수 있는 재사용 가능한 컴포넌트
 * - 싱글톤 패턴을 사용하여 애플리케이션 전체에서 하나의 인스턴스만 유지
 *
 * 핵심 내용:
 * - JPanel을 상속받아 SidePanel.showContent()로 쉽게 표시 가능
 * - 로그인 여부에 따라 화면 구성 변경
 *   - 로그인 X: "로그인 필요" 텍스트 중앙 표시
 *   - 로그인 O: 3열(GridLayout)로 프로필/일정, 내 강의, 결제 내역 표시
 */
public class MyPageView extends JPanel {

    // 싱글톤 패턴: private static 인스턴스 변수
    private static MyPageView instance;

    // 마이페이지 전용 서비스
    private final UserService userService;

    /**
     * 싱글톤 인스턴스를 반환
     */
    public static MyPageView getInstance() {
        if (instance == null) {
            instance = new MyPageView();
        }
        return instance;
    }

    // ======================== 스타일 상수 ========================

    private static class StyleConstants {
        // 색상 관련 상수
        static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
        static final Color PANEL_BACKGROUND = Color.WHITE;
        static final Color PRIMARY_COLOR = new Color(26, 90, 107);
        static final Color SECONDARY_COLOR = new Color(96, 125, 139);
        static final Color ACCENT_COLOR = new Color(255, 193, 7);
        static final Color TEXT_PRIMARY = new Color(33, 33, 33);
        static final Color TEXT_SECONDARY = new Color(117, 117, 117);
        static final Color BORDER_COLOR = new Color(224, 224, 224);
        static final Color NEGATIVE_COLOR = new Color(244, 67, 54);

        // 크기 관련 상수
        static final int PROFILE_IMAGE_SIZE = 100;

        // 폰트 관련 상수
        static final Font TITLE_FONT = new Font("맑은 고딕", Font.BOLD, 18);
        static final Font SUBTITLE_FONT = new Font("맑은 고딕", Font.BOLD, 14);
        static final Font NORMAL_FONT = new Font("맑은 고딕", Font.PLAIN, 13);
        static final Font SMALL_FONT = new Font("맑은 고딕", Font.PLAIN, 11);

        // 여백 관련 상수
        static final int PADDING_LARGE = 20;
        static final int PADDING_MEDIUM = 15;
        static final int PADDING_SMALL = 10;
        static final int PADDING_TINY = 5;
    }

    // ======================== 내부 모델 클래스 ========================

    /** 사용자 정보 */
    private static class UserInfo {
        String userId;
        String name;
        String birthDate;

        UserInfo(String userId, String name, String birthDate) {
            this.userId = userId;
            this.name = name;
            this.birthDate = birthDate;
        }
    }

    /** 요일별 강의 개수 */
    private static class ScheduleInfo {
        String[] days = {"월", "화", "수", "목", "금", "토", "일"};
        int[] lectureCounts;

        ScheduleInfo(int[] lectureCounts) {
            this.lectureCounts = lectureCounts;
        }
    }

    /** 결제 항목 (View용) */
    private static class PaymentItem {
        String itemName;
        int amount;
        String type;

        PaymentItem(String itemName, int amount, String type) {
            this.itemName = itemName;
            this.amount = amount;
            this.type = type;
        }
    }

    // ======================== 생성자 ========================

    private MyPageView() {
        this.userService = new UserService();

        setBorder(new EmptyBorder(
            StyleConstants.PADDING_MEDIUM,
            StyleConstants.PADDING_MEDIUM,
            StyleConstants.PADDING_MEDIUM,
            StyleConstants.PADDING_MEDIUM
        ));

        // 현재 로그인 상태에 맞게 화면 구성
        refresh();
    }

    /**
     * 로그인 상태에 따라 마이페이지 화면을 다시 구성
     */
    public void refresh() {
        removeAll();

        if (!AuthService.isLoggedIn()) {
            // 로그인 안 된 상태
            setLayout(new BorderLayout());
            setBackground(StyleConstants.BACKGROUND_COLOR);

            JLabel label = new JLabel("로그인 필요", SwingConstants.CENTER);
            label.setFont(new Font("맑은 고딕", Font.BOLD, 22));
            label.setForeground(StyleConstants.TEXT_PRIMARY);

            add(label, BorderLayout.CENTER);
        } else {
            // 로그인 된 상태
            setLayout(new GridLayout(1, 3, StyleConstants.PADDING_SMALL, 0));
            setBackground(StyleConstants.BACKGROUND_COLOR);

            add(createLeftGridPanel());    // 프로필 + 일정
            add(createCenterGridPanel());  // 내 강의
            add(createRightGridPanel());   // 결제 내역
        }

        revalidate();
        repaint();
    }

    // ======================== 좌측 그리드 (프로필 및 일정) ========================

    private JPanel createLeftGridPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(StyleConstants.PANEL_BACKGROUND);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(StyleConstants.BORDER_COLOR),
            new EmptyBorder(StyleConstants.PADDING_MEDIUM, 5,
                          StyleConstants.PADDING_MEDIUM, StyleConstants.PADDING_MEDIUM)
        ));

        panel.add(createProfileImagePanel());
        panel.add(Box.createRigidArea(new Dimension(0, StyleConstants.PADDING_MEDIUM)));
        panel.add(createUserInfoPanel());
        panel.add(Box.createRigidArea(new Dimension(0, StyleConstants.PADDING_MEDIUM)));
        panel.add(createSchedulePanel());
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel createProfileImagePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(StyleConstants.PANEL_BACKGROUND);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, StyleConstants.PROFILE_IMAGE_SIZE + 20));

        JLabel profileLabel = new JLabel("👤");
        profileLabel.setFont(new Font("Dialog", Font.PLAIN, 60));
        profileLabel.setPreferredSize(new Dimension(
            StyleConstants.PROFILE_IMAGE_SIZE,
            StyleConstants.PROFILE_IMAGE_SIZE
        ));
        profileLabel.setHorizontalAlignment(SwingConstants.CENTER);
        profileLabel.setOpaque(true);
        profileLabel.setBackground(StyleConstants.SECONDARY_COLOR);
        profileLabel.setBorder(BorderFactory.createLineBorder(StyleConstants.PRIMARY_COLOR, 3));

        panel.add(profileLabel);

        return panel;
    }

    private JPanel createUserInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(StyleConstants.PANEL_BACKGROUND);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        UserInfo user = getUserInfoFromAuth();

        JLabel titleLabel = new JLabel("내 정보");
        titleLabel.setFont(StyleConstants.SUBTITLE_FONT);
        titleLabel.setForeground(StyleConstants.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titleLabel);

        panel.add(Box.createRigidArea(new Dimension(0, StyleConstants.PADDING_SMALL)));

        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        panel.add(separator);
        panel.add(Box.createRigidArea(new Dimension(0, StyleConstants.PADDING_SMALL)));

        panel.add(createInfoLabel("ID: " + user.userId));
        panel.add(createInfoLabel("이름: " + user.name));
        panel.add(createInfoLabel("생년월일: " + user.birthDate));

        return panel;
    }

    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(StyleConstants.NORMAL_FONT);
        label.setForeground(StyleConstants.TEXT_SECONDARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(new EmptyBorder(StyleConstants.PADDING_TINY, 0, StyleConstants.PADDING_TINY, 0));
        return label;
    }

    /**
     * 내 일정: 실제 내가 수강 중인 강의 요일 기반으로 요일별 개수 계산
     */
    private JPanel createSchedulePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(StyleConstants.PANEL_BACKGROUND);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel("내 일정");
        titleLabel.setFont(StyleConstants.SUBTITLE_FONT);
        titleLabel.setForeground(StyleConstants.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titleLabel);

        panel.add(Box.createRigidArea(new Dimension(0, StyleConstants.PADDING_SMALL)));

        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        panel.add(separator);
        panel.add(Box.createRigidArea(new Dimension(0, StyleConstants.PADDING_SMALL)));

        // ★ 실제 강의 데이터에서 요일별 개수 계산
        ScheduleInfo schedule = buildScheduleInfoFromLectures();

        for (int i = 0; i < schedule.days.length; i++) {
            String displayText = schedule.days[i] + "요일: " +
                (schedule.lectureCounts[i] > 0 ? schedule.lectureCounts[i] + "개" : "I");

            JLabel dayLabel = new JLabel(displayText);
            dayLabel.setFont(StyleConstants.NORMAL_FONT);
            dayLabel.setForeground(
                schedule.lectureCounts[i] > 0 ?
                StyleConstants.PRIMARY_COLOR :
                StyleConstants.TEXT_SECONDARY
            );
            dayLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            dayLabel.setBorder(new EmptyBorder(StyleConstants.PADDING_TINY, 0, StyleConstants.PADDING_TINY, 0));

            panel.add(dayLabel);
        }

        return panel;
    }

    /**
     * 로그인한 사용자의 수강 강의 목록을 기반으로
     * 요일별 강의 개수를 계산해서 ScheduleInfo로 반환
     *
     * - MyLectureView.getDayOfWeek() 예시:
     *   "월", "화, 목", "월, 수, 금" 등
     */
    private ScheduleInfo buildScheduleInfoFromLectures() {
        int[] counts = new int[7]; // 월, 화, 수, 목, 금, 토, 일

        User currentUser = AuthService.getCurrentUser();
        if (currentUser == null) {
            return new ScheduleInfo(counts);
        }

        List<MyLectureView> lectures = userService.getMyLectures(currentUser);
        if (lectures == null) {
            return new ScheduleInfo(counts);
        }

        for (MyLectureView lecture : lectures) {
            String dow = lecture.getDayOfWeek(); // 예: "월, 수"
            if (dow == null || dow.isBlank()) continue;

            // "월, 수" / "화,목" / "토" 등을 쉼표 기준으로 나누고, 공백 제거
            String[] tokens = dow.split(",");
            for (String token : tokens) {
                String day = token.trim(); // "월" / "수" 등

                switch (day) {
                    case "월" -> counts[0]++;
                    case "화" -> counts[1]++;
                    case "수" -> counts[2]++;
                    case "목" -> counts[3]++;
                    case "금" -> counts[4]++;
                    case "토" -> counts[5]++;
                    case "일" -> counts[6]++;
                    default -> {
                        // 혹시 다른 문자열이 와도 무시
                    }
                }
            }
        }

        return new ScheduleInfo(counts);
    }

    // ======================== 중앙 그리드 (내 강의) ========================

    private JPanel createCenterGridPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(StyleConstants.PANEL_BACKGROUND);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(StyleConstants.BORDER_COLOR),
            new EmptyBorder(StyleConstants.PADDING_MEDIUM, StyleConstants.PADDING_SMALL,
                          StyleConstants.PADDING_MEDIUM, StyleConstants.PADDING_SMALL)
        ));

        JLabel titleLabel = new JLabel("내 강의");
        titleLabel.setFont(StyleConstants.TITLE_FONT);
        titleLabel.setForeground(StyleConstants.TEXT_PRIMARY);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel lectureListPanel = new JPanel();
        lectureListPanel.setLayout(new BoxLayout(lectureListPanel, BoxLayout.Y_AXIS));
        lectureListPanel.setBackground(StyleConstants.PANEL_BACKGROUND);

        User currentUser = AuthService.getCurrentUser();
        List<MyLectureView> lectures = userService.getMyLectures(currentUser);

        if (lectures == null || lectures.isEmpty()) {
            JLabel emptyLabel = new JLabel("신청한 강의가 없습니다.");
            emptyLabel.setFont(StyleConstants.NORMAL_FONT);
            emptyLabel.setForeground(StyleConstants.TEXT_SECONDARY);
            emptyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            lectureListPanel.add(emptyLabel);
        } else {
            for (MyLectureView lecture : lectures) {
                lectureListPanel.add(createLectureItem(lecture));
                lectureListPanel.add(Box.createRigidArea(new Dimension(0, StyleConstants.PADDING_SMALL)));
            }
        }

        JScrollPane scrollPane = new JScrollPane(lectureListPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(0, 500));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createLectureItem(MyLectureView lecture) {
        JPanel itemPanel = new JPanel(new BorderLayout(5, 0));
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(StyleConstants.BORDER_COLOR),
            new EmptyBorder(StyleConstants.PADDING_TINY, StyleConstants.PADDING_TINY,
                          StyleConstants.PADDING_TINY, StyleConstants.PADDING_TINY)
        ));
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(lecture.getLectureName());
        nameLabel.setFont(StyleConstants.SUBTITLE_FONT);
        nameLabel.setForeground(StyleConstants.TEXT_PRIMARY);

        JLabel dayLabel = new JLabel("수업: " + lecture.getDayOfWeek());
        dayLabel.setFont(StyleConstants.SMALL_FONT);
        dayLabel.setForeground(StyleConstants.TEXT_SECONDARY);

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, StyleConstants.PADDING_TINY)));
        infoPanel.add(dayLabel);

        JButton ratingButton = new JButton("리뷰 작성");
        ratingButton.setFont(new Font("맑은 고딕", Font.PLAIN, 10));
        ratingButton.setBackground(StyleConstants.ACCENT_COLOR);
        ratingButton.setForeground(StyleConstants.TEXT_PRIMARY);
        ratingButton.setFocusPainted(false);
        ratingButton.setBorderPainted(false);
        ratingButton.setPreferredSize(new Dimension(95, 28));

        ratingButton.addActionListener(e -> {
            System.out.println(lecture.getLectureName() + "에 리뷰 작성 클릭됨");
            JOptionPane.showMessageDialog(
                this,
                lecture.getLectureName() + "에 리뷰를 작성했습니다.",
                "리뷰 작성",
                JOptionPane.INFORMATION_MESSAGE
            );
        });

        itemPanel.add(infoPanel, BorderLayout.CENTER);
        itemPanel.add(ratingButton, BorderLayout.EAST);

        return itemPanel;
    }

    // ======================== 우측 그리드 (결제 내역) ========================

    private JPanel createRightGridPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(StyleConstants.PANEL_BACKGROUND);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(StyleConstants.BORDER_COLOR),
            new EmptyBorder(StyleConstants.PADDING_MEDIUM, StyleConstants.PADDING_MEDIUM,
                          StyleConstants.PADDING_MEDIUM, StyleConstants.PADDING_MEDIUM)
        ));

        JLabel titleLabel = new JLabel("결제 내역");
        titleLabel.setFont(StyleConstants.TITLE_FONT);
        titleLabel.setForeground(StyleConstants.TEXT_PRIMARY);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel paymentListPanel = new JPanel();
        paymentListPanel.setLayout(new BoxLayout(paymentListPanel, BoxLayout.Y_AXIS));
        paymentListPanel.setBackground(StyleConstants.PANEL_BACKGROUND);

        User currentUser = AuthService.getCurrentUser();
        UserProfileResponse profile = userService.getUserProfile(currentUser);

        int totalAmount = 0;
        List<Payment> payments = new ArrayList<>();

        if (profile != null) {
            totalAmount = profile.getTotalPaymentAmount();
            payments = profile.getRecentPayments();
        }

        if (payments == null || payments.isEmpty()) {
            JLabel emptyLabel = new JLabel("결제 내역이 없습니다.");
            emptyLabel.setFont(StyleConstants.NORMAL_FONT);
            emptyLabel.setForeground(StyleConstants.TEXT_SECONDARY);
            emptyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            paymentListPanel.add(emptyLabel);
        } else {
            for (Payment payment : payments) {
                PaymentItem item = new PaymentItem(
                        payment.getItemName(),
                        payment.getAmount(),
                        payment.getType()
                );
                paymentListPanel.add(createPaymentItem(item));
                paymentListPanel.add(Box.createRigidArea(new Dimension(0, StyleConstants.PADDING_SMALL)));
            }
        }

        JScrollPane scrollPane = new JScrollPane(paymentListPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel totalPanel = createTotalPanel(totalAmount);
        panel.add(totalPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createPaymentItem(PaymentItem payment) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(StyleConstants.BORDER_COLOR),
            new EmptyBorder(StyleConstants.PADDING_SMALL, StyleConstants.PADDING_SMALL,
                          StyleConstants.PADDING_SMALL, StyleConstants.PADDING_SMALL)
        ));
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel nameLabel = new JLabel(payment.itemName);
        nameLabel.setFont(StyleConstants.NORMAL_FONT);
        nameLabel.setForeground(StyleConstants.TEXT_PRIMARY);

        NumberFormat formatter = NumberFormat.getInstance(Locale.KOREA);
        String formattedAmount = formatter.format(payment.amount);
        JLabel amountLabel = new JLabel(formattedAmount + "원");
        amountLabel.setFont(StyleConstants.SUBTITLE_FONT);
        amountLabel.setForeground(StyleConstants.NEGATIVE_COLOR);

        itemPanel.add(nameLabel, BorderLayout.WEST);
        itemPanel.add(amountLabel, BorderLayout.EAST);

        return itemPanel;
    }

    private JPanel createTotalPanel(int totalAmount) {
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setBackground(StyleConstants.PANEL_BACKGROUND);
        totalPanel.setBorder(new EmptyBorder(
            StyleConstants.PADDING_MEDIUM, 0, 0, 0
        ));

        JLabel totalLabel = new JLabel("총 합계");
        totalLabel.setFont(StyleConstants.SUBTITLE_FONT);
        totalLabel.setForeground(StyleConstants.TEXT_PRIMARY);

        NumberFormat formatter = NumberFormat.getInstance(Locale.KOREA);
        String formattedTotal = formatter.format(totalAmount);
        JLabel amountLabel = new JLabel(formattedTotal + "원");
        amountLabel.setFont(StyleConstants.TITLE_FONT);
        amountLabel.setForeground(StyleConstants.NEGATIVE_COLOR);

        totalPanel.add(totalLabel, BorderLayout.WEST);
        totalPanel.add(amountLabel, BorderLayout.EAST);

        return totalPanel;
    }

    // ======================== 유틸 메서드 ========================

    private UserInfo getUserInfoFromAuth() {
        User user = AuthService.getCurrentUser();
        if (user != null) {
            String birth = user.getBirth();
            if (birth == null || birth.isBlank()) {
                birth = "미등록";
            }
            return new UserInfo(user.getId(), user.getName(), birth);
        }
        return new UserInfo("guest", "게스트", "-");
    }
}
