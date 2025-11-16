package table_demo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ShowDetail {
	private JDialog dialog;
    private String courseName;
    private String instructor;
    private double rating;
    private String schedule;
    private String location;
    private String textbook;
    private int textbookPrice;
    
    // 기본 생성자 (테스트용 더미 데이터)
    public ShowDetail() {
        this("기초 확률과 통계","홍길동",3.0,"월요일","경기도 수원시 영통구 광교산로 154-42","기초 확률과 통계",16000);
    }
    
    // 매개변수 생성자 (실제 데이터 전달)
    public ShowDetail(String courseName, String instructor, double rating,String schedule , String location,String textbook, int textbookPrice) {
        this.courseName = courseName;
        this.instructor = instructor;
        this.rating = rating;
        this.schedule = schedule;
        this.location = location;
        this.textbook = textbook;
        this.textbookPrice = textbookPrice;
    }
    
    // 다이얼로그 표시
    public void show() {
        createDialog();
        dialog.setVisible(true);
    }
    
    private void createDialog() {
        // 팝업 다이얼로그 생성
        dialog = new JDialog((Frame) null, "강의 상세 정보", true);
        dialog.setSize(820,900);
        //dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(Color.WHITE);
        
        
        // 메인 콘텐츠 패널
        JPanel contentPanel = createContentPanel();
        JScrollPane scrollPane = new JScrollPane(contentPanel);
    
        JPanel buttonPanel = createButtonPanel();
        
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
       
    }
    
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setPreferredSize(new Dimension(800,700));
        
        // 상단 헤더
        JPanel header = new JPanel();
        header.setBounds(0,0,820,70);
        header.setBackground(new Color(18,90,130));
        header.setLayout(null);
        
        contentPanel.add(header);
        
        //프로필
        JPanel profileImage = new JPanel();
        profileImage.setBounds(35,90,165,165);
        profileImage.setBackground(Color.WHITE);
        profileImage.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        contentPanel.add(profileImage);
        
        // 강의명
        JLabel titleLabel = new JLabel(courseName);
        titleLabel.setBounds(230,100,400,40);
        titleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 26));
        contentPanel.add(titleLabel);
        
        
        //별점
        JPanel starPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,3,0));
        starPanel.setBounds(640,105,120,30);
        starPanel.setBackground(Color.WHITE);
        
        int starCount = (int) rating;
        for(int i=0;i<starCount;i++) {
        	JLabel star = new JLabel("⭐");
            star.setFont(new Font("Dialog", Font.BOLD, 22));
            starPanel.add(star);
        }
        contentPanel.add(starPanel);
        
        // 강사명
        JLabel instructorLabel = new JLabel(instructor + "강사");
        instructorLabel.setBounds(230,150,200,25);
        instructorLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 15));
        contentPanel.add(instructorLabel);
        
        
        // 교재
        JLabel textbookLabel = new JLabel("교재: " + textbook);
        textbookLabel.setBounds(460,150, 250, 25);
        textbookLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 15));
        contentPanel.add(textbookLabel);
        
        // 교재 가격
        JLabel priceLabel = new JLabel("("+String.format("%,d",textbookPrice)+"원)");
        priceLabel.setBounds(460,170,170,25);
        priceLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 15));
        contentPanel.add(priceLabel);
        
        // ===== 교재 구매 버튼 =====
        JButton buyBtn = new JButton("교재 구매");
        buyBtn.setBounds(690, 150, 90, 30);
        buyBtn.setFont(new Font("Malgun Gothic", Font.PLAIN, 12));
        buyBtn.setBackground(Color.WHITE);
        buyBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        buyBtn.setFocusPainted(false);
        buyBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        buyBtn.addActionListener(e->showTextbookPurchaseDialog());
        contentPanel.add(buyBtn);
        
        // 스케쥴
        JLabel scheduleLabel = new JLabel("매주 " + schedule);
        scheduleLabel.setBounds(230,180, 200, 25);
        scheduleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 15));
        contentPanel.add(scheduleLabel);
        
        
        // 강의실 위치
        JLabel locationLabel = new JLabel("강의실: " + location);
        locationLabel.setBounds(230, 200, 600, 25);
        locationLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 15));
        contentPanel.add(locationLabel);
        
        // 강의 설명 섹션
        JLabel descTitle = new JLabel("〈강의 설명〉");
        descTitle.setBounds(230,220, 200, 30);
        descTitle.setFont(new Font("Malgun Gothic", Font.BOLD, 15));
        contentPanel.add(descTitle);
        
        // 강의 계획 섹션
        JLabel planTitle = new JLabel("〈강의 계획〉");
        planTitle.setBounds(35,275, 200, 30);
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
            planItem.setBorder(BorderFactory.createEmptyBorder(18, 25, 18, 20));
            planItem.setAlignmentX(Component.LEFT_ALIGNMENT);
            planBox.add(planItem);
            
            // 구분선
            JSeparator sep = new JSeparator();
            sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
            planBox.add(sep);
        }
        
        JScrollPane planScroll = new JScrollPane(planBox);
        planScroll.setBounds(40, 320, 700, 270);
        planScroll.setBorder(BorderFactory.createLineBorder(new Color(180,180,180),1));
        planScroll.getVerticalScrollBar().setUnitIncrement(16);
        contentPanel.add(planScroll);
        
        return contentPanel;
    }
    
    private JPanel createButtonPanel() {
        // 하단 버튼 영역 패널
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0,20));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setPreferredSize(new Dimension(820, 80));
       
        
        // 예약/신청 버튼
        JButton reserveBtn = new JButton("예약/신청");
        reserveBtn.setPreferredSize(new Dimension(270, 50));
        reserveBtn.setFont(new Font("Malgun Gothic", Font.BOLD, 17));
        reserveBtn.setBackground(Color.WHITE);
        reserveBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        reserveBtn.setFocusPainted(false);
        reserveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        reserveBtn.addActionListener(e -> {
        	showSuccessDialog();
        });
        
        buttonPanel.add(reserveBtn);
        
        return buttonPanel;
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
    
    private void showSuccessDialog() {
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
