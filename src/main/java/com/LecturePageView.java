package table_demo;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class LecturePageView {
    private void createAndShowGUI(){
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // 루트 패널: BorderLayout 사용
            JPanel root = new JPanel(new BorderLayout());
            root.setPreferredSize(new Dimension(1000, 600)); // 240 + 760 = 1000

            // === 왼쪽 비어있는 공간(원래 사이드바 자리) ===
            JPanel leftEmpty = new JPanel();
            leftEmpty.setPreferredSize(new Dimension(240, 600));
            root.add(leftEmpty, BorderLayout.WEST);

            
            // === 오른쪽: 콘텐츠 영역 (760 x 600) ===
            JPanel right = createRightPanel();

           

            // 스크롤
            JScrollPane rightScroll = new JScrollPane(right);
            rightScroll.setBorder(null);
            rightScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            root.add(rightScroll, BorderLayout.CENTER);
            // 프레임 세팅
            frame.setContentPane(root);
            frame.pack();               // preferred size 기준으로 프레임 맞춤
            frame.setResizable(false);  // 픽셀 정확히 유지
            frame.setLocationRelativeTo(null); // 화면 중아에 뜸
            frame.setVisible(true);
    }

  
    private JPanel createRightPanel(){
        JPanel right = new JPanel();
        right.setBackground(Color.WHITE); // 흰색 배경
        right.setPreferredSize(new Dimension(760, 600));
        right.setLayout(null);
        
        // 상단 과목 버튼
        String[] subjects = { "국어", "수학", "영어", "사회", "과학", "한국사", "모의고사" };
        int btnWidth = 92;
        int btnHeight = 60;
        int startX = 35;
        int startY = 60;
        int btnGap = 10;
        
        for (int i = 0; i < subjects.length; i++) {
            int x = startX + i * (btnWidth + btnGap);
            
            JButton subjectBtn = new JButton(subjects[i]);
            subjectBtn.setBounds(x, startY, btnWidth, btnHeight);
            subjectBtn.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
            subjectBtn.setForeground(Color.BLACK);
            subjectBtn.setBackground(Color.WHITE);
            subjectBtn.setFocusPainted(false);
            subjectBtn.setBorder(BorderFactory.createLineBorder(new Color(30, 110, 160)));
            subjectBtn.setContentAreaFilled(false);
            subjectBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            // 호버 효과
            subjectBtn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    subjectBtn.setBackground(new Color(245, 248, 252));
                }
                public void mouseExited(MouseEvent e) {
                    subjectBtn.setBackground(Color.WHITE);
                }
            });
           right.add(subjectBtn);
        }
        // 정렬
        addSortControls(right);
        // 검색 영역
        JPanel searchArea = createSearchArea();
        right.add(searchArea);
        // 강의 카드
        addCourseCards(right);
        right.setPreferredSize(new Dimension(760, 800));
        
        return right;
    }
    private JPanel createSearchArea() {
        // 검색 영역 컨테이너
        JPanel searchPanel = new JPanel();
        searchPanel.setBounds(0, 0, 760,200);  // right 패널 내 위치
        searchPanel.setLayout(null);
        searchPanel.setOpaque(true);
        searchPanel.setBackground(Color.WHITE);

        // 검색창
        JTextField searchField = new JTextField();
        searchField.setBounds(515,130,160,30);
        searchField.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(30, 110, 160), 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 75)
        ));
        searchPanel.add(searchField);

        // 검색 버튼
        JButton searchBtn = new JButton("검색");
        searchBtn.setBounds(680,130,60,30);
        searchBtn.setFont(new Font("Malgun Gothic", Font.BOLD, 12));
        searchBtn.setForeground(Color.WHITE); // 검색글씨
        searchBtn.setBackground(new Color(30,110,160));
        searchBtn.setOpaque(true);             
        searchBtn.setContentAreaFilled(true); 
        searchBtn.setBorderPainted(false);
        searchBtn.setFocusPainted(false);
        searchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

      
        searchBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                searchBtn.setBackground(new Color(25, 95, 135));
            }
            public void mouseExited(MouseEvent e) {
                searchBtn.setBackground(new Color(30, 110, 160));
            }
        });
        
        // 검색 버튼 클릭 
        searchBtn.addActionListener(e -> {
            String keyword = searchField.getText();
            if (!keyword.isEmpty()) {
                System.out.println("검색: " + keyword);
                
            }
        });
        
        // Enter 키로도 검색 가능
        searchField.addActionListener(e -> searchBtn.doClick());

        searchPanel.add(searchBtn);
        
        return searchPanel;
     
    }
    private void addSortControls(JPanel parent) {
        // 정렬 라벨
        JLabel sortLabel = new JLabel("정렬");
        sortLabel.setBounds(40, 130, 50, 30);
        sortLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
        parent.add(sortLabel);
        
        
        String[] sortOptions = {"최신순", "인기순", "평점순", "가격순"};
        JComboBox<String> sortCombo = new JComboBox<>(sortOptions);
        sortCombo.setBounds(95, 130, 120, 30);
        sortCombo.setFont(new Font("Malgun Gothic", Font.PLAIN, 12));
        
        // 정렬 옵션 선택 이벤트
        sortCombo.addActionListener(e -> {
            String selected = (String) sortCombo.getSelectedItem();
            System.out.println("정렬: " + selected);
            
        });
        
        parent.add(sortCombo);
    }
    private void addCourseCards(JPanel parent) {
    	JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(3, 4, 20, 20)); // 3행 4열, 간격 20px
        gridPanel.setBackground(Color.WHITE);
        gridPanel.setBounds(40, 170, 700, 600); // 위치와 크기 지정 (right 내부)

        // 카드 생성
        for (int i = 0; i < 12; i++) {
            JPanel card = createCourseCard(160, 180);
            gridPanel.add(card);
        }

        parent.add(gridPanel);
    }
    private JPanel createCourseCard(int width, int height) {
        // 카드 패널
        JPanel card = new JPanel();
        card.setLayout(null);
        card.setOpaque(true);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200))); 
        
        card.setLayout(null);
        card.setOpaque(false);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // 썸네일 영역
        JPanel thumbnail = new JPanel();
        thumbnail.setBounds(10, 30, width - 20, 100);
        thumbnail.setBackground(new Color(240, 240, 240));
        thumbnail.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        
        JLabel thumbText = new JLabel("썸네일", JLabel.CENTER);
        thumbText.setFont(new Font("Malgun Gothic", Font.PLAIN, 12));
        thumbText.setForeground(new Color(150, 150, 150));
        thumbnail.add(thumbText);
        card.add(thumbnail);
        
        // 강의명
        JLabel titleLabel = new JLabel("강의명", JLabel.CENTER);
        titleLabel.setBounds(10, 130, width - 20, 25);
        titleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
        card.add(titleLabel);
        
        // 별점
        JLabel ratingLabel = new JLabel("<html><span style='color:#FFD700; font-size:14px;'>⭐</span> <span style='font-size:12px;'>5.0</span></html>");
        ratingLabel.setBounds(10, 160, width - 20, 20);
        ratingLabel.setFont(new Font("Malgun Gothic", Font.PLAIN, 12));
        card.add(ratingLabel);
        
        // 호버 효과
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(245, 250, 255));
            }
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
            }
            public void mouseClicked(MouseEvent e) {
            	ShowDetail detail = new ShowDetail();
            	detail.show();
            }
        });
        
        return card;
    }
   
        
    public static void main(String[] args) {
    	  SwingUtilities.invokeLater(() -> {
              new LecturePageView().createAndShowGUI();
          });
      }
  }
