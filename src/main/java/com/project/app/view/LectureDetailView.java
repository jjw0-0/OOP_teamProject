package com.project.app.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LectureDetailView {
    private JDialog dialog;

    private String courseName;
    private String instructor;
    private double rating;
    private String schedule;
    private String location;
    private String textbook;
    private int textbookPrice;
    private String courseInstroduction;
    private int currentEnrolled; // 현재 인원
    private int capacity; // 최대 정원
    private String priorityGrade; // 우선 학년

    // 기본 생성자 (테스트용 더미 데이터)
    public LectureDetailView() {
        this("기초 확률과 통계","홍길동",3.0,"월요일","경기도 수원시 영통구 광교산로 154-42","기초 확률과 통계",16000,"기본 문장 구조부터 6줄 이상의 긴 문장까지 단계적으로 해석", 16,25,"고2");
    }

    // 매개변수 생성자 (실제 데이터 전달)
    public LectureDetailView(String courseName, String instructor, double rating,String schedule , String location,String textbook, int textbookPrice, String courseInstroduction, int currentEnrolled, int capacity, String priorityGrade) {
        this.courseName = courseName;
        this.instructor = instructor;
        this.rating = rating;
        this.schedule = schedule;
        this.location = location;
        this.textbook = textbook;
        this.textbookPrice = textbookPrice;
        this.courseInstroduction=courseInstroduction;
        this.currentEnrolled=currentEnrolled;
        this.capacity=capacity;
        this.priorityGrade=priorityGrade;
    }

    // 다이얼로그 표시
    public void show() {
        createDialog();
        dialog.setVisible(true);
    }

    private void createDialog() {
        // 팝업 다이얼로그 생성
        dialog = new JDialog((Frame) null, "강의 상세 정보", true);
        dialog.setSize(650,700);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(Color.WHITE);


        // 메인 콘텐츠 패널
        JPanel contentPanel = createContentPanel();
        //JScrollPane scrollPane = new JScrollPane(contentPanel);
        //scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel buttonPanel = createButtonPanel();

        dialog.add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setPreferredSize(new Dimension(650,700));

        // 상단 헤더
        JPanel header = new JPanel();
        header.setBounds(0,0,650,55);
        header.setBackground(new Color(18,90,130));
        header.setLayout(null);

        contentPanel.add(header);

        //프로필
        JPanel profileImage = new JPanel();
        profileImage.setBounds(15,80,140,150);
        profileImage.setBackground(Color.WHITE);
        profileImage.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        contentPanel.add(profileImage);

        // 강의명
        JLabel titleLabel = new JLabel(courseName);
        titleLabel.setBounds(170,70,250,40);
        titleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 24));
        contentPanel.add(titleLabel);

        //별점
        JPanel starPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,3,0));
        starPanel.setBounds(400,80,200,30);
        starPanel.setBackground(Color.WHITE);

        int starCount = (int) rating;
        for(int i=0;i<starCount;i++) {
            JLabel star = new JLabel("<html><font color='#FFD700'>⭐</font>" +
                    "<font color='black'>"+"</font></html>");
            star.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
            starPanel.add(star);
        }
        JLabel ratingLabel = new JLabel(String.format("(%.1f", rating)+") ");
        ratingLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 18));
        starPanel.add(ratingLabel);

        contentPanel.add(starPanel);

        // 강사명
        JLabel instructorLabel = new JLabel("강사 : "+instructor);
        instructorLabel.setBounds(170,120,100,25);
        instructorLabel.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        contentPanel.add(instructorLabel);

        // 교재
        JLabel textbookLabel = new JLabel("교재: " + textbook);
        textbookLabel.setBounds(330,120, 200, 25);
        textbookLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 15));
        contentPanel.add(textbookLabel);

        // 교재 가격
        JLabel priceLabel = new JLabel("("+String.format("%,d",textbookPrice)+"원)");
        priceLabel.setBounds(370,145,150,25);
        priceLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 15));
        contentPanel.add(priceLabel);

        // ===== 교재 구매 버튼 =====
        JButton buyBtn = new JButton("교재 구매");
        buyBtn.setBounds(510, 130, 90, 30);
        buyBtn.setFont(new Font("Malgun Gothic", Font.BOLD, 12));
        buyBtn.setBackground(Color.WHITE);
        buyBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        buyBtn.setFocusPainted(false);
        buyBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buyBtn.addActionListener(e->showTextbookPurchaseDialog());
        contentPanel.add(buyBtn);

        // 현재 인원/최대 정원
        JLabel enrollmentLabel=new JLabel(String.format("정원 : [ %d / %d ]",currentEnrolled,capacity));
        enrollmentLabel.setBounds(170,150,120,25);
        enrollmentLabel.setFont(new Font("Malgun Gothic",Font.BOLD,15));
        contentPanel.add(enrollmentLabel);

        // 스케쥴
        JLabel scheduleLabel = new JLabel("시간 : 매주 " + schedule);
        scheduleLabel.setBounds(170,180, 150, 25);
        scheduleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 15));
        contentPanel.add(scheduleLabel);

        //우선 학년
        JLabel gradeLabel=new JLabel("학년 : "+priorityGrade);
        gradeLabel.setBounds(330,178,150,25);
        gradeLabel.setFont(new Font("Malgun Gothic",Font.BOLD,15));
        contentPanel.add(gradeLabel);

        // "강의실 :" 패널
        JLabel locationTitleLabel = new JLabel("강의실: ");
        locationTitleLabel.setBounds(170, 210, 60, 25);
        locationTitleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 15));
        contentPanel.add(locationTitleLabel);

        // 강의실 위치
        JTextArea locationLabel = new JTextArea(location);
        locationLabel.setLineWrap(true); // 자동 줄바꿈
        locationLabel.setWrapStyleWord(false); // 단어 단위 줄바꿈
        locationLabel.setEditable(false);
        locationLabel.setBounds(230, 212, 500, 30);
        locationLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 15));
        contentPanel.add(locationLabel);

        // "〈강의 설명〉" 패널
        JLabel descTitle = new JLabel("〈강의 설명〉");
        descTitle.setBounds(35,250, 100, 25);
        descTitle.setFont(new Font("Malgun Gothic", Font.BOLD, 15));
        contentPanel.add(descTitle);

        // 강의 설명
        JTextArea descIntro = new JTextArea(courseInstroduction);
        descIntro.setLineWrap(true); // 자동 줄바꿈
        descIntro.setWrapStyleWord(false); // 단어 단위 줄바꿈
        descIntro.setEditable(false);
        descIntro.setBounds(135, 252, 565, 25);
        descIntro.setFont(new Font("Malgun Gothic", Font.BOLD, 15));
        contentPanel.add(descIntro);

        // 강의 계획 섹션
        JLabel planTitle = new JLabel("〈강의 계획〉");
        planTitle.setBounds(80,290, 200, 30);
        planTitle.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
        contentPanel.add(planTitle);

        // 강의 계획 스크롤 박스
        JPanel planBox = new JPanel();
        planBox.setLayout(new BoxLayout(planBox, BoxLayout.Y_AXIS));
        planBox.setBackground(new Color(225,238,248));

        String [] plans = {
                "1주차: 1장 ~",
                "2주차",
                "3주차",
                "4주차",
                "5주차",
                "6주차"
        };
        for (String plan : plans) {
            JLabel planItem = new JLabel(plan);
            planItem.setFont(new Font("Malgun Gothic", Font.PLAIN, 15));
            planItem.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            planItem.setAlignmentX(Component.LEFT_ALIGNMENT);
            planBox.add(planItem);

            // 구분선
            JSeparator sep = new JSeparator();
            sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
            planBox.add(sep);
        }

        JScrollPane planScroll = new JScrollPane(planBox);
        planScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // 가로스크롤 X
        planScroll.setBounds(80, 325, 500, 250);
        planScroll.setBorder(BorderFactory.createLineBorder(new Color(180,180,180),1));
        planScroll.getVerticalScrollBar().setUnitIncrement(16);
        contentPanel.add(planScroll);

        JPanel buttonPanel = createButtonPanel();
        buttonPanel.setBounds(0, 585, 650, 80);
        contentPanel.add(buttonPanel);

        return contentPanel;
    }

    // LectureDetailView 클래스에 추가
    private ActionListener enrollButtonListener;

    // 신청 버튼 리스너 등록 메서드
    public void addEnrollButtonListener(ActionListener listener) {
        this.enrollButtonListener = listener;
    }


    private JPanel createButtonPanel() {
        // 하단 버튼 영역 패널
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0,20));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setPreferredSize(new Dimension(650, 50));


        // 예약/신청 버튼
        JButton reserveBtn = new JButton("예약/신청");
        reserveBtn.setPreferredSize(new Dimension(230, 50));
        reserveBtn.setFont(new Font("Malgun Gothic", Font.BOLD, 15));
        reserveBtn.setBackground(Color.WHITE);
        reserveBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        reserveBtn.setFocusPainted(false);
        reserveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        reserveBtn.addActionListener(e -> {
            if(enrollButtonListener != null) {
                enrollButtonListener.actionPerformed(e);
            }
        });

        buttonPanel.add(reserveBtn);

        return buttonPanel;
    }

    public void showErrorDialog(String message) {
        JDialog errorDialog = new JDialog(dialog, "신청 실패", true);
        errorDialog.setSize(400, 250);
        errorDialog.setLocationRelativeTo(dialog);
        errorDialog.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(200, 200, 200));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 40, 40, 40));

        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(messageLabel);
        panel.add(Box.createVerticalStrut(30));

        JButton confirmBtn = new JButton("확인");
        confirmBtn.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
        confirmBtn.setPreferredSize(new Dimension(100, 40));
        confirmBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmBtn.setBackground(Color.WHITE);
        confirmBtn.setFocusPainted(false);
        confirmBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        confirmBtn.addActionListener(e -> errorDialog.dispose());

        panel.add(confirmBtn);
        errorDialog.add(panel);
        errorDialog.setVisible(true);
    }

    private void showTextbookPurchaseDialog() {
        // 교재 구매 확인 다이얼로그
        JDialog purchaseDialog = new JDialog(dialog, "교재 구매", true);
        purchaseDialog.setSize(400, 250);
        purchaseDialog.setLocationRelativeTo(dialog);
        purchaseDialog.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(200, 200, 200));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 40, 40, 40));

        JLabel message1 = new JLabel(String.format("%,d원 입니다.", textbookPrice));
        message1.setFont(new Font("Malgun Gothic", Font.BOLD, 18));
        message1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel message2 = new JLabel("결제하시겠습니까?");
        message2.setFont(new Font("Malgun Gothic", Font.PLAIN, 16));
        message2.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(message1);
        panel.add(Box.createVerticalStrut(20));
        panel.add(message2);
        panel.add(Box.createVerticalStrut(30));

        JButton confirmBtn = new JButton("확인");
        confirmBtn.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
        confirmBtn.setPreferredSize(new Dimension(100, 40));
        confirmBtn.setMaximumSize(new Dimension(100, 40));
        confirmBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmBtn.setBackground(Color.WHITE);
        confirmBtn.setFocusPainted(false);
        confirmBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        confirmBtn.addActionListener(e -> {
            purchaseDialog.dispose();
        });

        panel.add(confirmBtn);
        purchaseDialog.add(panel);
        purchaseDialog.setVisible(true);
    }

    public void showSuccessDialog() {
        // 신청 완료 다이얼로그
        JDialog successDialog = new JDialog(dialog, "신청 완료", true);
        successDialog.setSize(400, 250);
        successDialog.setLocationRelativeTo(dialog);
        successDialog.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(200, 200, 200));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // 메시지
        JLabel message1 = new JLabel("신청이 완료되었습니다.");
        message1.setFont(new Font("Malgun Gothic", Font.BOLD, 18));
        message1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel message2 = new JLabel("마이로그인 > 로그인창");
        message2.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));
        message2.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalGlue());
        panel.add(message1);
        panel.add(Box.createVerticalStrut(15));
        panel.add(message2);
        panel.add(Box.createVerticalGlue());

        // 확인 버튼
        JButton confirmBtn = new JButton("확인");
        confirmBtn.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
        confirmBtn.setPreferredSize(new Dimension(100, 40));
        confirmBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmBtn.setBackground(Color.WHITE);
        confirmBtn.setFocusPainted(false);
        confirmBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        confirmBtn.addActionListener(e -> {
            successDialog.dispose();
            dialog.dispose();
        });

        panel.add(confirmBtn);
        panel.add(Box.createVerticalStrut(20));

        successDialog.add(panel);
        successDialog.setVisible(true);
    }
}
