package com.project.app.view;

import com.project.app.controller.InstructorController;
import com.project.app.dto.InstructorCardView;
import com.project.app.dto.InstructorDetailResponse;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    private static final int COLUMN_COUNT = 4;
    private static final int CARD_WIDTH = 156;
    private static final int CARD_HEIGHT = 176;
    private static final int CARD_GAP = 10;
    private static final int VIEWPORT_WIDTH = 701;

    private static final Map<String, String> ACADEMY_OPTIONS = new LinkedHashMap<>();
    static {
        ACADEMY_OPTIONS.put("전체", null);
        ACADEMY_OPTIONS.put("메가스터디", "A1");
        ACADEMY_OPTIONS.put("이투스", "A2");
        ACADEMY_OPTIONS.put("대성마이맥", "A3");
    }

    private InstructorController controller;
    private JPanel instructorsListPanel;  // 스크롤 컨테이너
    private JPanel cardsPanel;            // 강사 카드 패널
    private JScrollPane instructorsScrollPane;
    private JTextField searchField;  // 검색 필드
    private String selectedSubject;  // 선택된 과목
    private String selectedAcademy;  // 선택된 학원
    private Map<String, JButton> subjectButtons = new HashMap<>();
    private JComboBox<String> sortCombo;
    private JComboBox<String> academyCombo;
    private JButton searchButton;

    /**
     * Controller 반환
     */
    public InstructorController getController() {
        return controller;
    }

    // 싱글톤 패턴: private 생성자
    private InstructorsPageView() {
        selectedSubject = null;
        selectedAcademy = null;

        setPreferredSize(new Dimension(760, 600));
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        instructorsListPanel = setupInstructorsList();
        add(instructorsListPanel, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(null);
        header.setPreferredSize(new Dimension(760, 160));
        header.setBackground(Color.WHITE);

        initSubjectButtons(header);
        initSearchArea(header);
        initSortAndAcademyArea(header);

        return header;
    }

    private void initSubjectButtons(JPanel parent) {
        String[] subjects = {"전체", "국어", "수학", "영어", "사회", "과학", "한국사"};
        int btnWidth = 90;
        int btnHeight = 60;
        int startX = 35;
        int startY = 40;
        int gap = 10;

        for (int i = 0; i < subjects.length; i++) {
            String subject = subjects[i];
            int x = startX + i * (btnWidth + gap);

            JButton button = new JButton(subject) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    g2.setColor(new Color(30, 110, 160));
                    g2.setStroke(new BasicStroke(1));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);

                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            button.setBorderPainted(false);
            button.setBounds(x, startY, btnWidth, btnHeight);
            button.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
            button.setForeground(Color.BLACK);
            button.setBackground(Color.WHITE);
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setFocusPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));

            button.addActionListener(e -> {
                selectedSubject = "전체".equals(subject) ? null : subject;
                updateSubjectButtons();
                if (controller != null) {
                    controller.handleSubjectFilter(selectedSubject);
                }
            });

            subjectButtons.put(subject, button);
            parent.add(button);
        }

        updateSubjectButtons();
    }

    private void initSearchArea(JPanel parent) {
        searchField = new JTextField();
        searchField.setBounds(505, 120, 160, 30);
        searchField.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(30, 110, 160), 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 75)
        ));
        parent.add(searchField);

        searchButton = new JButton("검색");
        searchButton.setBounds(665, 120, 60, 30);
        searchButton.setFont(new Font("Malgun Gothic", Font.BOLD, 12));
        searchButton.setForeground(Color.WHITE);
        searchButton.setBackground(new Color(30, 110, 160));
        searchButton.setFocusPainted(false);
        searchButton.setBorderPainted(false);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                searchButton.setBackground(new Color(25, 95, 135));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                searchButton.setBackground(new Color(30, 110, 160));
            }
        });

        searchButton.addActionListener(e -> {
            if (controller != null) {
                controller.handleSearch(searchField.getText());
            }
        });
        searchField.addActionListener(e -> searchButton.doClick());

        parent.add(searchButton);
    }

    private void initSortAndAcademyArea(JPanel parent) {
        JLabel sortLabel = new JLabel("정렬");
        sortLabel.setBounds(40, 120, 50, 30);
        sortLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
        parent.add(sortLabel);

        String[] sortLabels = {"평점순", "이름순"};
        sortCombo = new JComboBox<>(sortLabels);
        sortCombo.setBounds(80, 120, 100, 30);
        sortCombo.setFont(new Font("Malgun Gothic", Font.PLAIN, 12));
        sortCombo.addActionListener(e -> {
            if (controller != null) {
                String label = (String) sortCombo.getSelectedItem();
                String sortValue = "이름순".equals(label) ? "name" : "reviewScore";
                controller.handleSortChange(sortValue);
            }
        });
        parent.add(sortCombo);

        JLabel academyLabel = new JLabel("학원");
        academyLabel.setBounds(210, 120, 50, 30);
        academyLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
        parent.add(academyLabel);

        academyCombo = new JComboBox<>(ACADEMY_OPTIONS.keySet().toArray(new String[0]));
        academyCombo.setBounds(250, 120, 120, 30);
        academyCombo.setFont(new Font("Malgun Gothic", Font.PLAIN, 12));
        academyCombo.addActionListener(e -> {
            String label = (String) academyCombo.getSelectedItem();
            selectedAcademy = ACADEMY_OPTIONS.get(label);
            if (controller != null) {
                controller.handleAcademyFilter(selectedAcademy);
            }
        });
        parent.add(academyCombo);
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
        instructorsListPanel = new JPanel(new BorderLayout());
        instructorsListPanel.setMaximumSize(new Dimension(VIEWPORT_WIDTH, 378));
        instructorsListPanel.setBackground(Color.WHITE);

        cardsPanel = new JPanel();
        cardsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, CARD_GAP, CARD_GAP));
        cardsPanel.setBorder(new EmptyBorder(8, 18, 8, 18));
        cardsPanel.setBackground(Color.WHITE);

        instructorsScrollPane = createScrollPane(cardsPanel, VIEWPORT_WIDTH, 378);
        instructorsScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        instructorsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        instructorsScrollPane.setBorder(null);

        instructorsListPanel.add(instructorsScrollPane, BorderLayout.CENTER);
        return instructorsListPanel;
    }

    /**
     * 강사 카드 생성
     */
    private JPanel createInstructorCard(InstructorCardView cardView) {
        JPanel instructorCard = new RoundedPanel(18);
        Dimension cardSize = new Dimension(CARD_WIDTH, CARD_HEIGHT);
        instructorCard.setPreferredSize(cardSize);
        instructorCard.setMaximumSize(cardSize);
        instructorCard.setMinimumSize(cardSize);
        instructorCard.setAlignmentX(Component.LEFT_ALIGNMENT);
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
        instructorCard.add(createSubjectLabelPanel(cardView.getSubject()));

        // 프로필 (사진 + 이름 + 별점)
        JPanel profile = new JPanel();
        profile.setLayout(new BoxLayout(profile, BoxLayout.X_AXIS));
        profile.setMaximumSize(new Dimension(145, 83));
        profile.setOpaque(false);

        // 사진 (이미지 패스)
        profile.add(createImagePlaceholder(83, 83, cardView.getProfileImagePath()));

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
     * 과목 표시 패널 생성
     */
    private JPanel createSubjectLabelPanel(String subject) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setMaximumSize(new Dimension(CARD_WIDTH-20, 30));
        panel.setOpaque(false);

        String text = subject != null ? subject.trim() : "";
        if (text.isEmpty()) {
            text = "과목 정보 없음";
        }
        text = truncateWithEllipsis("[" + text + "]", 20);

        JLabel label = new JLabel(text, SwingConstants.LEFT);
        label.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        panel.add(label, BorderLayout.CENTER);

        return panel;
    }

    /**
     * 이미지 플레이스홀더 생성 (이미지 패스)
     */
    private static JLabel createImagePlaceholder(int width, int height, String imagePath) {
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(width, height));
        imageLabel.setOpaque(true);
        imageLabel.setBackground(Color.LIGHT_GRAY);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        
        // 이미지 경로가 있으면 이미지 로드
        if (imagePath != null && !imagePath.trim().isEmpty()) {
            try {
                String fullPath = "src/main/resources/InstructorThumbnail/" + imagePath.trim();
                java.io.File imageFile = new java.io.File(fullPath);
                
                if (imageFile.exists()) {
                    ImageIcon originalIcon = new ImageIcon(fullPath);
                    Image originalImage = originalIcon.getImage();
                    
                    // 이미지 크기 조정
                    Image scaledImage = originalImage.getScaledInstance(
                        width, height, 
                        java.awt.Image.SCALE_SMOOTH
                    );
                    ImageIcon scaledIcon = new ImageIcon(scaledImage);
                    imageLabel.setIcon(scaledIcon);
                    imageLabel.setText(""); // 텍스트 제거
                } else {
                    // 이미지를 찾을 수 없을 때 플레이스홀더 표시
                    imageLabel.setText("👤");
                    imageLabel.setFont(new Font("Dialog", Font.PLAIN, 40));
                }
            } catch (Exception e) {
                // 이미지 로드 실패 시 플레이스홀더 표시
                imageLabel.setText("👤");
                imageLabel.setFont(new Font("Dialog", Font.PLAIN, 40));
            }
        } else {
            // 이미지 경로가 없을 때 플레이스홀더 표시
            imageLabel.setText("👤");
            imageLabel.setFont(new Font("Dialog", Font.PLAIN, 40));
        }
        
        return imageLabel;
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
     * 과목 버튼 UI 업데이트
     */
    private void updateSubjectButtons() {
        subjectButtons.forEach((label, button) -> {
            boolean isSelected = (selectedSubject == null && "전체".equals(label)) ||
                    (selectedSubject != null && selectedSubject.equals(label));
            button.setBackground(isSelected ? new Color(30, 110, 160) : Color.WHITE);
            button.setForeground(isSelected ? Color.WHITE : Color.BLACK);
        });
    }

    /**
     * 강사 카드 목록 업데이트
     */
    public void updateInstructorCards(List<InstructorCardView> instructors) {
        if (instructors == null || instructors.isEmpty()) {
            showEmptyMessage("검색 결과가 없습니다.");
            return;
        }

        instructorsListPanel.removeAll();
        instructorsListPanel.add(instructorsScrollPane, BorderLayout.CENTER);

        cardsPanel.removeAll();
        for (InstructorCardView instructor : instructors) {
            cardsPanel.add(createInstructorCard(instructor));
        }

        int rows = Math.max(2, (int) Math.ceil(instructors.size() / (double) COLUMN_COUNT));
        int contentHeight = rows * (CARD_HEIGHT + CARD_GAP) + 30;
        cardsPanel.setPreferredSize(new Dimension(VIEWPORT_WIDTH, contentHeight));

        cardsPanel.revalidate();
        cardsPanel.repaint();
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
     * 선택된 학원 반환
     */
    public String getSelectedAcademy() {
        return selectedAcademy;
    }

    /**
     * 검색 키워드 반환
     */
    public String getSearchKeyword() {
        return searchField != null ? searchField.getText() : "";
    }

    private static String truncateWithEllipsis(String text, int maxLength) {
        if (text == null) {
            return "";
        }
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, Math.max(0, maxLength - 3)) + "...";
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
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
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
        wrapPanel.add(Box.createVerticalStrut(40));
        wrapPanel.setOpaque(false);

        wrapPanel.add(createDescriptionSection(detail));
        wrapPanel.add(Box.createVerticalStrut(15));

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

            JLabel nameLabel = new JLabel(InstructorsPageView.truncateWithEllipsis(lecture.getName(), 25));
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
            JLabel imageLabel = createImagePlaceholder(138, 138, detail.getProfileImagePath());
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

        private JPanel createDescriptionSection(InstructorDetailResponse detail) {
            JPanel container = new JPanel();
            container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
            container.setOpaque(false);

            JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            titlePanel.setOpaque(false);
            JLabel titleLabel = new JLabel("강사설명");
            titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
            titlePanel.add(titleLabel);
            container.add(titlePanel);

            JTextArea descriptionArea = new JTextArea(detail.getIntroduction() != null ? detail.getIntroduction() : "강사 소개 정보가 없습니다.");
            descriptionArea.setLineWrap(true);
            descriptionArea.setWrapStyleWord(true);
            descriptionArea.setEditable(false);
            descriptionArea.setOpaque(false);
            descriptionArea.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
            descriptionArea.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

            container.add(descriptionArea);
            return container;
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
