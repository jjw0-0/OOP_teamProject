package com.project.app.view;

import com.project.app.controller.InstructorController;
import com.project.app.dto.InstructorCardView;
import com.project.app.dto.InstructorDetailResponse;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * 강사 페이지 뷰
 *
 * 기능:
 * - 싱글톤 패턴을 사용하여 애플리케이션 전체에서 하나의 인스턴스만 유지
 * - Controller와 연동하여 실제 데이터 표시
 */
public class InstructorsPageView extends JPanel {

    // 싱글톤 패턴: private static 인스턴스 변수
    private static InstructorsPageView instance;

    /**
     * 싱글톤 인스턴스를 반환하는 메서드
     */
    public static InstructorsPageView getInstance() {
        if (instance == null) {
            instance = new InstructorsPageView();
        }
        return instance;
    }

    // ========== 필드 ==========

    private InstructorController controller;
    private JPanel instructorsListPanel;  // 강사 목록 패널
    private JTextField searchField;  // 검색 필드
    private String selectedSubject;  // 선택된 과목
    private JPanel subjectsPanel;  // 과목 패널 (필터용)

    /**
     * Controller 반환
     */
    public InstructorController getController() {
        return controller;
    }

    // 싱글톤 패턴: private 생성자
    private InstructorsPageView() {
        setPreferredSize(new Dimension(760, 600));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);

        add(Box.createVerticalStrut(30));
        subjectsPanel = setupSubjectsPanel();
        add(subjectsPanel);

        add(Box.createVerticalStrut(30));
        JPanel wrapSearchBar = new RoundedPanel(13);
        wrapSearchBar.setMaximumSize(new Dimension(674, 29));
        wrapSearchBar.setBackground(Color.WHITE);
        wrapSearchBar.setLayout(new BoxLayout(wrapSearchBar, BoxLayout.X_AXIS));
        wrapSearchBar.add(Box.createHorizontalStrut(405));
        wrapSearchBar.add(setupSearchBar());
        add(wrapSearchBar);

        add(Box.createVerticalStrut(30));
        instructorsListPanel = setupInstructorsList();
        add(instructorsListPanel);
    }

    /**
     * Controller 설정
     */
    public void setController(InstructorController controller) {
        this.controller = controller;
    }

    /**
     * 강사 목록 패널 생성
     */
    private JPanel setupInstructorsList() {
        JPanel instructorsList = new JPanel();
        instructorsList.setMaximumSize(new Dimension(701, 378));
        instructorsList.setLayout(new GridLayout(2, 4, 10, 10));
        instructorsList.setBorder(new EmptyBorder(8, 18, 8, 18));
        instructorsList.setBackground(Color.WHITE);

        // 초기에는 빈 상태
        return instructorsList;
    }

    /**
     * 강사 카드 생성
     */
    private JPanel createInstructorCard(InstructorCardView cardView) {
        JPanel instructorCard = new RoundedPanel(18);
        instructorCard.setMaximumSize(new Dimension(156, 176));
        instructorCard.setLayout(new BoxLayout(instructorCard, BoxLayout.Y_AXIS));
        instructorCard.setBackground(new Color(0xF5F5F5));

        // 마우스 커서 및 이벤트 설정
        instructorCard.setCursor(new Cursor(Cursor.HAND_CURSOR));
        instructorCard.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (controller != null) {
                    controller.handleInstructorClick(cardView.getId());
                }
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                instructorCard.setBackground(new Color(0xE5E5E5));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                instructorCard.setBackground(new Color(0xF5F5F5));
            }
        });

        // 소개글
        instructorCard.add(Box.createVerticalStrut(26));
        instructorCard.add(introPanel(cardView.getIntroduction()));

        // 프로필 (사진 + 이름 + 별점)
        JPanel profile = new JPanel();
        profile.setLayout(new BoxLayout(profile, BoxLayout.X_AXIS));
        profile.setMaximumSize(new Dimension(145, 83));
        profile.setOpaque(false);

        // 사진 (이미지 패스)
        profile.add(createImagePlaceholder(83, 83));

        JPanel wrapInfo = new JPanel();
        wrapInfo.setLayout(new BoxLayout(wrapInfo, BoxLayout.Y_AXIS));
        wrapInfo.setMaximumSize(new Dimension(70, 90));
        wrapInfo.setOpaque(false);

        // 이름
        wrapInfo.add(Box.createVerticalStrut(14));
        wrapInfo.add(setupName(cardView.getName()));

        // 별점
        wrapInfo.add(Box.createVerticalStrut(11));
        wrapInfo.add(createStar(cardView.getReviewScore()));

        profile.add(wrapInfo);
        instructorCard.add(profile);

        return instructorCard;
    }

    /**
     * 소개글 패널 생성
     */
    private JPanel introPanel(String introduction) {
        JPanel introductionPanel = new JPanel();
        introductionPanel.setMaximumSize(new Dimension(120, 46));
        introductionPanel.setOpaque(false);

        // 소개글이 너무 길면 자르기
        String displayText = introduction;
        if (displayText.length() > 15) {
            displayText = displayText.substring(0, 15) + "...";
        }

        JLabel introLabel = new JLabel(" " + displayText);
        introLabel.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        introductionPanel.add(introLabel);

        return introductionPanel;
    }

    /**
     * 이미지 플레이스홀더 생성 (이미지 패스)
     */
    private JLabel createImagePlaceholder(int width, int height) {
        JLabel placeholder = new JLabel("👤", SwingConstants.CENTER);
        placeholder.setPreferredSize(new Dimension(width, height));
        placeholder.setOpaque(true);
        placeholder.setBackground(Color.LIGHT_GRAY);
        placeholder.setFont(new Font("Dialog", Font.PLAIN, 40));
        return placeholder;
    }

    /**
     * 이름 패널 생성
     */
    private JPanel setupName(String name) {
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));

        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
        namePanel.setMaximumSize(new Dimension(54, 19));
        namePanel.setOpaque(false);
        namePanel.add(nameLabel);

        return namePanel;
    }

    /**
     * 별점 패널 생성
     */
    private JPanel createStar(double reviewScore) {
        JPanel starPanel = new JPanel();
        starPanel.setLayout(new BoxLayout(starPanel, BoxLayout.X_AXIS));
        starPanel.setMaximumSize(new Dimension(60, 40));
        starPanel.setOpaque(false);

        String scoreText = reviewScore > 0 ? String.format("%.1f", reviewScore) : "0.0";
        JLabel star = new JLabel("<html><font color='#FFD700'>⭐</font>" +
                " <font color='black'>" + scoreText + "</font></html>");
        star.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
        starPanel.add(star);

        return starPanel;
    }

    /**
     * 검색바 설정
     */
    private JPanel setupSearchBar() {
        JPanel searchPanel = new RoundedPanel(13);
        searchPanel.setLayout(new BorderLayout());
        searchPanel.setMaximumSize(new Dimension(300, 29));

        searchField = new JTextField();
        searchField.setMaximumSize(new Dimension(269, 29));
        searchField.setBackground(Color.WHITE);
        searchPanel.add(searchField, BorderLayout.CENTER);

        JButton searchButton = new JButton("검색");
        searchButton.setForeground(Color.WHITE);
        searchButton.setMaximumSize(new Dimension(26, 26));
        searchButton.setBackground(new Color(0x0C4A6E));
        searchButton.setFocusPainted(false);
        
        // 검색 버튼 클릭 이벤트
        searchButton.addActionListener(e -> {
            if (controller != null) {
                controller.handleSearch(searchField.getText());
            }
        });
        
        // Enter 키로 검색
        searchField.addActionListener(e -> searchButton.doClick());
        
        searchPanel.add(searchButton, BorderLayout.EAST);

        return searchPanel;
    }

    /**
     * 과목 패널 설정
     */
    private JPanel setupSubjectsPanel() {
        JPanel subjectsPanel = new JPanel();
        subjectsPanel.setLayout(new BoxLayout(subjectsPanel, BoxLayout.X_AXIS));
        subjectsPanel.setMaximumSize(new Dimension(674, 57));
        subjectsPanel.setBackground(Color.WHITE);

        String[] subjects = {"전체", "국어", "수학", "영어", "사회", "과학", "한국사"};

        for (String subject : subjects) {
            subjectsPanel.add(Box.createHorizontalStrut(15));
            subjectsPanel.add(createSubjectPanel(subject));
        }

        return subjectsPanel;
    }

    /**
     * 과목 버튼 생성
     */
    private JPanel createSubjectPanel(String text) {
        JLabel subjectName = new JLabel(text);
        subjectName.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        subjectName.setHorizontalAlignment(JLabel.CENTER);
        subjectName.setVerticalAlignment(JLabel.CENTER);

        JPanel subjectPanel = new RoundedPanel(8, 1, new Color(0x1E6EA0));
        subjectPanel.setLayout(new BorderLayout());
        subjectPanel.setMaximumSize(new Dimension(92, 60));
        subjectPanel.setPreferredSize(new Dimension(92, 60));
        subjectPanel.setBackground(Color.WHITE);
        subjectPanel.add(subjectName);

        // 클릭 이벤트
        subjectPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        subjectPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // 선택된 과목 업데이트
                String subject = text.equals("전체") ? null : text;
                selectedSubject = subject;
                
                // Controller에 알림
                if (controller != null) {
                    controller.handleSubjectFilter(subject);
                }
                
                // UI 업데이트 (선택된 버튼 강조)
                updateSubjectButtons();
            }
        });

        return subjectPanel;
    }

    /**
     * 과목 버튼 UI 업데이트
     */
    private void updateSubjectButtons() {
        // 모든 과목 버튼의 배경색 업데이트
        for (Component comp : subjectsPanel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                if (panel.getComponentCount() > 0 && panel.getComponent(0) instanceof JLabel) {
                    JLabel label = (JLabel) panel.getComponent(0);
                    String subjectText = label.getText();
                    String subject = subjectText.equals("전체") ? null : subjectText;
                    
                    if ((selectedSubject == null && subject == null) ||
                        (selectedSubject != null && selectedSubject.equals(subject))) {
                        panel.setBackground(new Color(0xE3F2FD));  // 선택된 색상
                    } else {
                        panel.setBackground(Color.WHITE);
                    }
                }
            }
        }
    }

    /**
     * 강사 카드 목록 업데이트
     */
    public void updateInstructorCards(List<InstructorCardView> instructors) {
        instructorsListPanel.removeAll();
        
        if (instructors == null || instructors.isEmpty()) {
            showEmptyMessage("검색 결과가 없습니다.");
            return;
        }

        // 최대 8개까지만 표시 (2행 4열)
        int count = Math.min(instructors.size(), 8);
        for (int i = 0; i < count; i++) {
            instructorsListPanel.add(createInstructorCard(instructors.get(i)));
        }

        // 나머지 공간은 빈 패널로 채우기
        for (int i = count; i < 8; i++) {
            instructorsListPanel.add(new JPanel());
        }

        instructorsListPanel.revalidate();
        instructorsListPanel.repaint();
    }

    /**
     * 빈 메시지 표시
     */
    public void showEmptyMessage(String message) {
        instructorsListPanel.removeAll();
        
        JLabel emptyLabel = new JLabel(message, SwingConstants.CENTER);
        emptyLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        emptyLabel.setForeground(Color.GRAY);
        
        JPanel emptyPanel = new JPanel(new BorderLayout());
        emptyPanel.add(emptyLabel, BorderLayout.CENTER);
        emptyPanel.setOpaque(false);
        
        instructorsListPanel.setLayout(new BorderLayout());
        instructorsListPanel.removeAll();
        instructorsListPanel.add(emptyPanel, BorderLayout.CENTER);
        
        instructorsListPanel.revalidate();
        instructorsListPanel.repaint();
    }

    /**
     * 강사 상세 팝업 표시
     */
    public void showInstructorDetailPopup(InstructorDetailResponse detail) {
        java.awt.Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JFrame parentFrame = null;
        
        if (parentWindow instanceof JFrame) {
            parentFrame = (JFrame) parentWindow;
        }
        
        new InstructorDetailPopup(parentFrame, detail).setVisible(true);
    }

    /**
     * 선택된 과목 반환
     */
    public String getSelectedSubject() {
        return selectedSubject;
    }

    /**
     * 검색 키워드 반환
     */
    public String getSearchKeyword() {
        return searchField != null ? searchField.getText() : "";
    }

    /**
     * 스크롤 패널 생성 헬퍼 메서드
     */
    static JScrollPane createScrollPane(JPanel panel, int width, int height) {
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setMaximumSize(new Dimension(width, height));
        scrollPane.setPreferredSize(new Dimension(width, height));
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        return scrollPane;
    }

    // ========== 내부 클래스: InstructorDetailPopup ==========

    static class InstructorDetailPopup extends JDialog {
        public InstructorDetailPopup(JFrame page, InstructorDetailResponse detail) {
            super(page, "강사 세부 정보", true);
            setSize(600, 500);
            if (page != null) {
                setLocationRelativeTo(page);
            } else {
                setLocationRelativeTo(null);
            }
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            setResizable(false);

            setupInstructorDetailPopup(detail);
        }

        void setupInstructorDetailPopup(InstructorDetailResponse detail) {
            JPanel popupPanel = new JPanel(new BorderLayout(20, 0));
            popupPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            popupPanel.setBackground(Color.WHITE);
            setContentPane(popupPanel);

            // 프로필 + 강의평
            JPanel profileAndReview = new JPanel();
            profileAndReview.setLayout(new BoxLayout(profileAndReview, BoxLayout.Y_AXIS));
            profileAndReview.setAlignmentY(Component.TOP_ALIGNMENT);
            profileAndReview.setOpaque(false);

            profileAndReview.add(setupProfile(detail));
            profileAndReview.add(Box.createVerticalStrut(10));
            profileAndReview.add(setupReviews(detail));
            profileAndReview.add(Box.createVerticalGlue());

            popupPanel.add(profileAndReview, BorderLayout.WEST);

            // 강의목록
            JPanel wrapPanel = new JPanel();
            wrapPanel.setLayout(new BoxLayout(wrapPanel, BoxLayout.Y_AXIS));
            wrapPanel.setAlignmentY(Component.TOP_ALIGNMENT);
            wrapPanel.add(Box.createVerticalStrut(80));
            wrapPanel.setOpaque(false);

            JPanel titlePanel = new JPanel();
            titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
            titlePanel.setMaximumSize(new Dimension(387, 40));
            titlePanel.setOpaque(false);
            titlePanel.add(new JLabel("강의목록") {{
                setFont(new Font("맑은 고딕", Font.BOLD, 24));
            }});

            wrapPanel.add(titlePanel);

            JPanel wrapLecturesPanel = new JPanel();
            wrapLecturesPanel.setLayout(new BoxLayout(wrapLecturesPanel, BoxLayout.Y_AXIS));
            wrapLecturesPanel.setBackground(new Color(0xEEEEEE));

            wrapLecturesPanel.add(Box.createVerticalStrut(8));

            if (detail.getLectures() != null && !detail.getLectures().isEmpty()) {
                for (InstructorDetailResponse.LectureSummary lecture : detail.getLectures()) {
                    wrapLecturesPanel.add(createLecturePanel(lecture));
                    wrapLecturesPanel.add(Box.createVerticalStrut(5));
                }
            } else {
                // 강의 목록이 비어있을 때 메시지 표시
                JLabel emptyLabel = new JLabel("등록된 강의가 없습니다.", SwingConstants.CENTER);
                emptyLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
                emptyLabel.setForeground(Color.GRAY);
                wrapLecturesPanel.add(emptyLabel);
            }

            JScrollPane scrollPane = InstructorsPageView.createScrollPane(wrapLecturesPanel, 387, 284);
            wrapPanel.add(scrollPane);

            popupPanel.add(wrapPanel, BorderLayout.CENTER);
        }

        JPanel createLecturePanel(InstructorDetailResponse.LectureSummary lecture) {
            JPanel lecturePanel = new JPanel();
            lecturePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
            lecturePanel.setMaximumSize(new Dimension(352, 31));
            lecturePanel.setBackground(Color.WHITE);

            JLabel nameLabel = new JLabel(lecture.getName());
            nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
            lecturePanel.add(nameLabel);

            // 별점 표시
            String scoreText = lecture.getReviewScore() > 0 ? 
                    String.format("%.1f", lecture.getReviewScore()) : "0.0";
            JLabel starLabel = new JLabel("<html><font color='#FFD700'>⭐</font>" +
                    " <font color='black'>" + scoreText + "</font></html>");
            starLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
            lecturePanel.add(starLabel);

            return lecturePanel;
        }

        JPanel setupProfile(InstructorDetailResponse detail) {
            JPanel profile = new JPanel();
            profile.setLayout(new BoxLayout(profile, BoxLayout.Y_AXIS));
            profile.setMaximumSize(new Dimension(160, 180));
            profile.setPreferredSize(new Dimension(160, 180));
            profile.setOpaque(false);

            // 이미지 플레이스홀더
            JLabel imageLabel = new JLabel("👤", SwingConstants.CENTER);
            imageLabel.setPreferredSize(new Dimension(138, 138));
            imageLabel.setOpaque(true);
            imageLabel.setBackground(Color.LIGHT_GRAY);
            imageLabel.setFont(new Font("Dialog", Font.PLAIN, 60));
            JPanel imagePanel = new JPanel();
            imagePanel.setOpaque(false);
            imagePanel.add(imageLabel);
            profile.add(imagePanel);

            // 이름 및 과목
            JPanel namePanel = new JPanel();
            namePanel.setOpaque(false);
            JLabel subjectInfo = new JLabel("[" + detail.getSubject() + "]");
            subjectInfo.setFont(new Font("맑은 고딕", Font.BOLD, 18));
            namePanel.add(subjectInfo);
            
            JLabel nameLabel = new JLabel(detail.getName());
            nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
            namePanel.add(nameLabel);
            profile.add(namePanel);

            return profile;
        }

        JPanel setupReviews(InstructorDetailResponse detail) {
            JPanel starRating = new JPanel();
            starRating.setOpaque(false);
            starRating.setLayout(new BorderLayout(0, 3));
            starRating.setMaximumSize(new Dimension(160, 280));
            starRating.setPreferredSize(new Dimension(160, 280));

            JLabel reviewLabel = new JLabel("강의평");
            reviewLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
            JPanel reviewPanel = new JPanel();
            reviewPanel.setMaximumSize(new Dimension(160, 30));
            reviewPanel.setPreferredSize(new Dimension(160, 30));
            reviewPanel.setOpaque(false);

            reviewPanel.add(reviewLabel);
            
            // 별점 표시
            String scoreText = detail.getReviewScore() > 0 ? 
                    String.format("%.1f", detail.getReviewScore()) : "0.0";
            JLabel starLabel = new JLabel("<html><font color='#FFD700'>⭐</font>" +
                    " <font color='black'>" + scoreText + "</font></html>");
            starLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
            reviewPanel.add(starLabel);

            starRating.add(reviewPanel, BorderLayout.NORTH);

            // 리뷰 목록은 비어있음 (리뷰 제외)
            JPanel reviews = new JPanel();
            reviews.setLayout(new BoxLayout(reviews, BoxLayout.Y_AXIS));
            reviews.setBackground(new Color(0xEEEEEE));

            starRating.add(reviews, BorderLayout.CENTER);

            return starRating;
        }
    }

    // ========== 내부 클래스: RoundedPanel ==========

    static class RoundedPanel extends JPanel {
        int radius;
        int borderWidth;
        Color borderColor;

        RoundedPanel(int radius) {
            this.radius = radius;
            this.borderWidth = 0;
            this.borderColor = null;
            setOpaque(false);
        }

        RoundedPanel(int radius, int borderWidth, Color borderColor) {
            this.radius = radius;
            this.borderWidth = borderWidth;
            this.borderColor = borderColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            super.paintComponent(g2);

            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

            if (borderColor != null) {
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(borderWidth));
                g2.drawRoundRect(borderWidth / 2, borderWidth / 2, 
                        getWidth() - borderWidth, getHeight() - borderWidth, radius, radius);
            }
        }
    }
}
