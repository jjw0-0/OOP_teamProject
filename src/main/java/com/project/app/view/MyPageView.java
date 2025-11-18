package com.project.app.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
 * - GridLayout을 사용하여 3열로 분할 (프로필/일정, 강의 목록, 결제 내역)
 * - 각 섹션은 스크롤 가능한 리스트로 구성
 * - 현재는 더미 데이터를 사용하며, 추후 Model 레이어와 연동
 *
 * 화면 구성:
 * - 좌측 그리드 (약 250px): 프로필 및 내 정보, 내 일정
 * - 중앙 그리드 (약 255px): 내 강의 목록
 * - 우측 그리드 (약 255px): 결제 내역
 */
public class MyPageView extends JPanel {

    // 싱글톤 패턴: private static 인스턴스 변수
    private static MyPageView instance;

    /**
     * 싱글톤 인스턴스를 반환하는 메서드
     *
     * 기능:
     * - 인스턴스가 없으면 새로 생성하고, 있으면 기존 인스턴스 반환
     * - 메모리 효율성과 상태 유지를 위함
     *
     * @return MyPageView의 싱글톤 인스턴스
     */
    public static MyPageView getInstance() {
        if (instance == null) {
            instance = new MyPageView();
        }
        return instance;
    }

    // ======================== 스타일 상수 ========================

    /**
     * UI 스타일 상수 클래스
     *
     * 기능:
     * - 디자인 일관성 유지 및 향후 스타일 가이드 변경 시 한 곳에서 관리
     */
    private static class StyleConstants {
        // 색상 관련 상수
        static final Color BACKGROUND_COLOR = new Color(245, 245, 245);  // 연한 회색 배경
        static final Color PANEL_BACKGROUND = Color.WHITE;
        static final Color PRIMARY_COLOR = new Color(26, 90, 107);       // 브랜드 컬러
        static final Color SECONDARY_COLOR = new Color(96, 125, 139);    // 세컨더리 컬러
        static final Color ACCENT_COLOR = new Color(255, 193, 7);        // 강조 색상 (노란색)
        static final Color TEXT_PRIMARY = new Color(33, 33, 33);
        static final Color TEXT_SECONDARY = new Color(117, 117, 117);
        static final Color BORDER_COLOR = new Color(224, 224, 224);
        static final Color NEGATIVE_COLOR = new Color(244, 67, 54);      // 음수 표시 (빨간색)

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

    // ======================== 모의 데이터 클래스 ========================

    /**
     * 사용자 정보 모델
     *
     * 기능:
     * - 사용자 데이터를 구조화하여 관리
     * - 추후 실제 User 모델로 교체
     */
    private static class UserInfo {
        String userId;       // 사용자 ID
        String name;         // 이름
        String birthDate;    // 생년월일

        UserInfo(String userId, String name, String birthDate) {
            this.userId = userId;
            this.name = name;
            this.birthDate = birthDate;
        }
    }

    /**
     * 일정 정보 모델
     *
     * 기능:
     * - 요일별 강의 개수를 관리
     * - 주간 학습 스케줄을 시각화
     */
    private static class ScheduleInfo {
        String[] days = {"월", "화", "수", "목", "금", "토", "일"};
        int[] lectureCounts;  // 각 요일의 강의 개수

        ScheduleInfo(int[] lectureCounts) {
            this.lectureCounts = lectureCounts;
        }
    }

    /**
     * 강의 정보 모델
     *
     * 기능:
     * - 수강 중인 강의 데이터를 구조화
     * - 추후 실제 Lecture 모델로 교체 용이
     */
    private static class LectureInfo {
        String lectureName;  // 강의명
        String dayOfWeek;    // 수업 요일

        LectureInfo(String lectureName, String dayOfWeek) {
            this.lectureName = lectureName;
            this.dayOfWeek = dayOfWeek;
        }
    }

    /**
     * 결제 항목 모델
     *
     * 기능:
     * - 결제 내역 데이터를 구조화
     * - 강의와 교재를 구분하여 관리
     */
    private static class PaymentItem {
        String itemName;     // 항목명
        int amount;          // 금액 (음수로 저장)
        String type;         // 타입 (강의/교재)

        PaymentItem(String itemName, int amount, String type) {
            this.itemName = itemName;
            this.amount = amount;
            this.type = type;
        }
    }

    // ======================== 생성자 ========================

    /**
     * MyPageView 생성자
     *
     * 기능:
     * - 마이페이지 콘텐츠의 전체 레이아웃 구성
     *
     * 핵심 내용:
     * 1. JPanel 기본 설정
     * 2. GridLayout으로 3개의 열 생성
     * 3. 각 열에 프로필/일정, 강의 목록, 결제 내역 패널 추가
     */
    // 싱글톤 패턴: private 생성자
    private MyPageView() {
        setLayout(new GridLayout(1, 3, StyleConstants.PADDING_SMALL, 0));
        setBackground(StyleConstants.BACKGROUND_COLOR);
        setBorder(new EmptyBorder(
            StyleConstants.PADDING_MEDIUM,
            StyleConstants.PADDING_MEDIUM,
            StyleConstants.PADDING_MEDIUM,
            StyleConstants.PADDING_MEDIUM
        ));

        // 3개의 열 추가
        add(createLeftGridPanel());    // 좌측: 프로필 및 일정
        add(createCenterGridPanel());  // 중앙: 내 강의
        add(createRightGridPanel());   // 우측: 결제 내역
    }

    // ======================== 좌측 그리드 (프로필 및 일정) ========================

    /**
     * 좌측 그리드 패널 생성
     *
     * 기능:
     * - 사용자 프로필 정보와 주간 일정을 표시
     *
     * 핵심 내용:
     * - BoxLayout을 사용하여 수직 배치
     * - 상단: 프로필 이미지
     * - 중단: 내 정보
     * - 하단: 내 일정
     */
    private JPanel createLeftGridPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(StyleConstants.PANEL_BACKGROUND);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(StyleConstants.BORDER_COLOR),
            new EmptyBorder(StyleConstants.PADDING_MEDIUM, 5,
                          StyleConstants.PADDING_MEDIUM, StyleConstants.PADDING_MEDIUM)
        ));

        // 프로필 이미지
        panel.add(createProfileImagePanel());
        panel.add(Box.createRigidArea(new Dimension(0, StyleConstants.PADDING_MEDIUM)));

        // 내 정보
        panel.add(createUserInfoPanel());
        panel.add(Box.createRigidArea(new Dimension(0, StyleConstants.PADDING_MEDIUM)));

        // 내 일정
        panel.add(createSchedulePanel());

        // 하단 여백
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    /**
     * 프로필 이미지 패널 생성
     *
     * 기능:
     * - 사용자를 시각적으로 식별할 수 있는 프로필 이미지 표시
     *
     * 핵심 내용:
     * - 원형 프로필 이미지를 중앙에 배치
     * - 기본 아이콘으로 사용자 이모지 사용
     */
    private JPanel createProfileImagePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(StyleConstants.PANEL_BACKGROUND);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, StyleConstants.PROFILE_IMAGE_SIZE + 20));

        // 원형 프로필 이미지 (간단하게 라벨로 구현)
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

    /**
     * 사용자 정보 패널 생성
     *
     * 기능:
     * - 사용자의 기본 정보를 표시 (ID, 이름, 생년월일)
     *
     * 핵심 내용:
     * - 모의 사용자 데이터를 사용하여 정보 표시
     * - 각 정보를 라벨로 구성하여 수직 배치
     */
    private JPanel createUserInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(StyleConstants.PANEL_BACKGROUND);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 모의 데이터
        UserInfo user = getMockUserInfo();

        // 제목
        JLabel titleLabel = new JLabel("내 정보");
        titleLabel.setFont(StyleConstants.SUBTITLE_FONT);
        titleLabel.setForeground(StyleConstants.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titleLabel);

        panel.add(Box.createRigidArea(new Dimension(0, StyleConstants.PADDING_SMALL)));

        // 구분선
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        panel.add(separator);

        panel.add(Box.createRigidArea(new Dimension(0, StyleConstants.PADDING_SMALL)));

        // 정보 항목들
        panel.add(createInfoLabel("ID: " + user.userId));
        panel.add(createInfoLabel("이름: " + user.name));
        panel.add(createInfoLabel("생년월일: " + user.birthDate));

        return panel;
    }

    /**
     * 정보 라벨 생성 헬퍼 메서드
     *
     * 기능:
     * - 반복적인 라벨 생성 코드를 줄이기 위함
     * - 일관된 스타일 적용
     */
    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(StyleConstants.NORMAL_FONT);
        label.setForeground(StyleConstants.TEXT_SECONDARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(new EmptyBorder(StyleConstants.PADDING_TINY, 0, StyleConstants.PADDING_TINY, 0));
        return label;
    }

    /**
     * 일정 패널 생성
     *
     * 기능:
     * - 주간 강의 일정을 시각적으로 표시
     *
     * 핵심 내용:
     * - 월요일부터 일요일까지 각 요일의 강의 개수 표시
     * - 강의가 있는 요일은 개수를, 없는 요일은 "I" 표시
     */
    private JPanel createSchedulePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(StyleConstants.PANEL_BACKGROUND);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 제목
        JLabel titleLabel = new JLabel("내 일정");
        titleLabel.setFont(StyleConstants.SUBTITLE_FONT);
        titleLabel.setForeground(StyleConstants.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titleLabel);

        panel.add(Box.createRigidArea(new Dimension(0, StyleConstants.PADDING_SMALL)));

        // 구분선
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        panel.add(separator);

        panel.add(Box.createRigidArea(new Dimension(0, StyleConstants.PADDING_SMALL)));

        // 모의 일정 데이터
        ScheduleInfo schedule = getMockScheduleInfo();

        // 요일별 일정 표시
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

    // ======================== 중앙 그리드 (내 강의) ========================

    /**
     * 중앙 그리드 패널 생성
     *
     * 기능:
     * - 사용자가 신청한 강의 목록을 표시
     * - 각 강의에 대해 별점 남기기 기능 제공
     *
     * 핵심 내용:
     * - 스크롤 가능한 리스트로 강의 목록 표시
     * - 각 항목은 강의명, 요일, 버튼으로 구성
     */
    private JPanel createCenterGridPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(StyleConstants.PANEL_BACKGROUND);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(StyleConstants.BORDER_COLOR),
            new EmptyBorder(StyleConstants.PADDING_MEDIUM, StyleConstants.PADDING_SMALL,
                          StyleConstants.PADDING_MEDIUM, StyleConstants.PADDING_SMALL)
        ));

        // 제목
        JLabel titleLabel = new JLabel("내 강의");
        titleLabel.setFont(StyleConstants.TITLE_FONT);
        titleLabel.setForeground(StyleConstants.TEXT_PRIMARY);
        panel.add(titleLabel, BorderLayout.NORTH);

        // 강의 목록 패널
        JPanel lectureListPanel = new JPanel();
        lectureListPanel.setLayout(new BoxLayout(lectureListPanel, BoxLayout.Y_AXIS));
        lectureListPanel.setBackground(StyleConstants.PANEL_BACKGROUND);

        // 모의 강의 데이터
        List<LectureInfo> lectures = getMockLectures();

        for (LectureInfo lecture : lectures) {
            lectureListPanel.add(createLectureItem(lecture));
            lectureListPanel.add(Box.createRigidArea(new Dimension(0, StyleConstants.PADDING_SMALL)));
        }

        // 스크롤 가능하게 설정
        JScrollPane scrollPane = new JScrollPane(lectureListPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(0, 500));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * 강의 항목 패널 생성
     *
     * 기능:
     * - 각 강의를 시각적으로 구분하여 표시
     * - 리뷰 작성 버튼을 통해 사용자 상호작용 제공
     *
     * 핵심 내용:
     * - BorderLayout을 사용하여 정보와 버튼을 배치
     * - 좌측: 강의 정보, 우측: 리뷰 작성 버튼
     */
    private JPanel createLectureItem(LectureInfo lecture) {
        JPanel itemPanel = new JPanel(new BorderLayout(5, 0));
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(StyleConstants.BORDER_COLOR),
            new EmptyBorder(StyleConstants.PADDING_TINY, StyleConstants.PADDING_TINY,
                          StyleConstants.PADDING_TINY, StyleConstants.PADDING_TINY)
        ));
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        // 좌측: 강의 정보
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(lecture.lectureName);
        nameLabel.setFont(StyleConstants.SUBTITLE_FONT);
        nameLabel.setForeground(StyleConstants.TEXT_PRIMARY);

        JLabel dayLabel = new JLabel("수업: " + lecture.dayOfWeek);
        dayLabel.setFont(StyleConstants.SMALL_FONT);
        dayLabel.setForeground(StyleConstants.TEXT_SECONDARY);

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, StyleConstants.PADDING_TINY)));
        infoPanel.add(dayLabel);

        // 우측: 리뷰 작성 버튼
        JButton ratingButton = new JButton("리뷰 작성");
        ratingButton.setFont(new Font("맑은 고딕", Font.PLAIN, 10));
        ratingButton.setBackground(StyleConstants.ACCENT_COLOR);
        ratingButton.setForeground(StyleConstants.TEXT_PRIMARY);
        ratingButton.setFocusPainted(false);
        ratingButton.setBorderPainted(false);
        ratingButton.setPreferredSize(new Dimension(95, 28));

        // 버튼 클릭 이벤트 (현재는 콘솔 출력 및 다이얼로그, 추후 실제 기능 구현)
        ratingButton.addActionListener(e -> {
            System.out.println(lecture.lectureName + "에 리뷰 작성 클릭됨");
            JOptionPane.showMessageDialog(
                this,
                lecture.lectureName + "에 리뷰를 작성했습니다.",
                "리뷰 작성",
                JOptionPane.INFORMATION_MESSAGE
            );
        });

        itemPanel.add(infoPanel, BorderLayout.CENTER);
        itemPanel.add(ratingButton, BorderLayout.EAST);

        return itemPanel;
    }

    // ======================== 우측 그리드 (결제 내역) ========================

    /**
     * 우측 그리드 패널 생성
     *
     * 기능:
     * - 사용자의 결제 내역을 투명하게 표시
     *
     * 핵심 내용:
     * - 스크롤 가능한 리스트로 결제 항목 표시
     * - 하단에 총 합계 표시
     */
    private JPanel createRightGridPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(StyleConstants.PANEL_BACKGROUND);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(StyleConstants.BORDER_COLOR),
            new EmptyBorder(StyleConstants.PADDING_MEDIUM, StyleConstants.PADDING_MEDIUM,
                          StyleConstants.PADDING_MEDIUM, StyleConstants.PADDING_MEDIUM)
        ));

        // 제목
        JLabel titleLabel = new JLabel("결제 내역");
        titleLabel.setFont(StyleConstants.TITLE_FONT);
        titleLabel.setForeground(StyleConstants.TEXT_PRIMARY);
        panel.add(titleLabel, BorderLayout.NORTH);

        // 결제 항목 리스트 패널
        JPanel paymentListPanel = new JPanel();
        paymentListPanel.setLayout(new BoxLayout(paymentListPanel, BoxLayout.Y_AXIS));
        paymentListPanel.setBackground(StyleConstants.PANEL_BACKGROUND);

        // 모의 결제 데이터
        List<PaymentItem> payments = getMockPayments();
        int totalAmount = 0;

        for (PaymentItem payment : payments) {
            paymentListPanel.add(createPaymentItem(payment));
            paymentListPanel.add(Box.createRigidArea(new Dimension(0, StyleConstants.PADDING_SMALL)));
            totalAmount += payment.amount;
        }

        // 스크롤 가능하게 설정
        JScrollPane scrollPane = new JScrollPane(paymentListPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        panel.add(scrollPane, BorderLayout.CENTER);

        // 하단: 총 합계
        JPanel totalPanel = createTotalPanel(totalAmount);
        panel.add(totalPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * 결제 항목 패널 생성
     *
     * 기능:
     * - 각 결제 항목을 명확하게 표시
     *
     * 핵심 내용:
     * - 항목명과 금액을 좌우로 배치
     * - 음수 금액은 빨간색으로 표시
     */
    private JPanel createPaymentItem(PaymentItem payment) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(StyleConstants.BORDER_COLOR),
            new EmptyBorder(StyleConstants.PADDING_SMALL, StyleConstants.PADDING_SMALL,
                          StyleConstants.PADDING_SMALL, StyleConstants.PADDING_SMALL)
        ));
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        // 좌측: 항목명
        JLabel nameLabel = new JLabel(payment.itemName);
        nameLabel.setFont(StyleConstants.NORMAL_FONT);
        nameLabel.setForeground(StyleConstants.TEXT_PRIMARY);

        // 우측: 금액
        NumberFormat formatter = NumberFormat.getInstance(Locale.KOREA);
        String formattedAmount = formatter.format(payment.amount);
        JLabel amountLabel = new JLabel(formattedAmount + "원");
        amountLabel.setFont(StyleConstants.SUBTITLE_FONT);
        amountLabel.setForeground(StyleConstants.NEGATIVE_COLOR);

        itemPanel.add(nameLabel, BorderLayout.WEST);
        itemPanel.add(amountLabel, BorderLayout.EAST);

        return itemPanel;
    }

    /**
     * 총 합계 패널 생성
     *
     * 기능:
     * - 전체 결제 금액을 명확하게 표시
     */
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

    // ======================== 모의 데이터 메서드 ========================

    /**
     * 모의 사용자 정보 반환
     *
     * 기능:
     * - 개발 단계에서 실제 데이터 없이 UI 구현 및 테스트
     * - 추후 실제 User Model이나 API 응답으로 교체 예정
     */
    private UserInfo getMockUserInfo() {
        return new UserInfo("hong1234", "홍길동", "2008-01-01");
    }

    /**
     * 모의 일정 정보 반환
     *
     * 기능:
     * - 주간 일정을 시각적으로 테스트하기 위한 모의 데이터
     * - 실제로는 사용자가 수강하는 강의의 요일 정보를 집계
     */
    private ScheduleInfo getMockScheduleInfo() {
        return new ScheduleInfo(new int[]{2, 1, 2, 1, 2, 0, 0});
    }

    /**
     * 모의 강의 목록 반환
     *
     * 기능:
     * - 여러 강의를 표시하여 스크롤 및 레이아웃 테스트
     * - 실제로는 LectureService.getMyLectures() 등으로 교체
     */
    private List<LectureInfo> getMockLectures() {
        List<LectureInfo> lectures = new ArrayList<>();
        lectures.add(new LectureInfo("고등 수학 I", "월, 수"));
        lectures.add(new LectureInfo("물리 기본 개념", "수, 금"));
        lectures.add(new LectureInfo("한국사 입문", "월"));
        lectures.add(new LectureInfo("문학 읽기 I", "화"));
        lectures.add(new LectureInfo("수학 II", "목, 금"));
        return lectures;
    }

    /**
     * 모의 결제 내역 반환
     *
     * 기능:
     * - 강의와 교재를 섞어서 표시하여 다양한 결제 항목 테스트
     * - 실제로는 PaymentService.getMyPayments() 등으로 교체
     */
    private List<PaymentItem> getMockPayments() {
        List<PaymentItem> payments = new ArrayList<>();
        payments.add(new PaymentItem("고등 수학 I", -150000, "강의"));
        payments.add(new PaymentItem("수학의 정석", -25000, "교재"));
        payments.add(new PaymentItem("영어 리딩 마스터", -22000, "교재"));
        payments.add(new PaymentItem("물리 기본 개념", -140000, "강의"));
        payments.add(new PaymentItem("한국사 입문", -120000, "강의"));
        payments.add(new PaymentItem("한국사 바로알기", -15000, "교재"));
        payments.add(new PaymentItem("문학 읽기 I", -135000, "강의"));
        payments.add(new PaymentItem("현대문학 작품 읽기", -20000, "교재"));
        payments.add(new PaymentItem("수학 II", -155000, "강의"));
        return payments;
    }
}
