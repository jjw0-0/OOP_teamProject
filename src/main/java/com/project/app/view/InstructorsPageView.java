package com.project.app.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * 강사 페이지 뷰
 *
 * 기능:
 * - 싱글톤 패턴을 사용하여 애플리케이션 전체에서 하나의 인스턴스만 유지
 */
public class InstructorsPageView extends JPanel {

    // 싱글톤 패턴: private static 인스턴스 변수
    private static InstructorsPageView instance;

    /**
     * 싱글톤 인스턴스를 반환하는 메서드
     *
     * 기능:
     * - 인스턴스가 없으면 새로 생성하고, 있으면 기존 인스턴스 반환
     * - 메모리 효율성과 상태 유지를 위함
     *
     * @return InstructorsPageView의 싱글톤 인스턴스
     */
    public static InstructorsPageView getInstance() {
        if (instance == null) {
            instance = new InstructorsPageView();
        }
        return instance;
    }

    // 싱글톤 패턴: private 생성자
    private InstructorsPageView() {
        setPreferredSize(new Dimension(760, 600));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);

        add(Box.createVerticalStrut(30));
        add(setupSubjectsPanel());

        add(Box.createVerticalStrut(30));
        JPanel wrapSearchBar = new RoundedPanel(13);
        wrapSearchBar.setMaximumSize(new Dimension(674, 29));
        wrapSearchBar.setBackground(Color.WHITE);
        wrapSearchBar.setLayout(new BoxLayout(wrapSearchBar, BoxLayout.X_AXIS)); // 수평
        wrapSearchBar.add(Box.createHorizontalStrut(405));
        wrapSearchBar.add(setupSearchBar());
        add(wrapSearchBar);

        add(Box.createVerticalStrut(30));
        add(setupInstructorsList());
    }

    JPanel setupInstructorsList() {
        JPanel instructorsList = new JPanel();
        instructorsList.setMaximumSize(new Dimension(701, 378));
        instructorsList.setLayout(new GridLayout(2, 4, 10, 10));
        instructorsList.setBorder(new EmptyBorder(8, 18, 8, 18));
        instructorsList.setBackground(Color.WHITE);

        // 8개의 강사 카드 생성
        for (int i = 0; i < 8; i++) {
            instructorsList.add(Instructor());
        }

        return instructorsList;
    }

    JPanel Instructor() {
        JPanel instructorCard = new RoundedPanel(18);
        instructorCard.setMaximumSize(new Dimension(156, 176));
        instructorCard.setLayout(new BoxLayout(instructorCard, BoxLayout.Y_AXIS)); // 수직정렬
        instructorCard.setBackground(new Color(0xF5F5F5));

        InstructorData data = new InstructorData();

        // 마우스 커서 및 이벤트 설정
        instructorCard.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 손가락 커서
        instructorCard.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // 강사 상세 팝업 표시
                new InstructorDetailPopup(
                    (JFrame) SwingUtilities.getWindowAncestor(InstructorsPageView.this),
                    data
                ).setVisible(true);
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) { // hover 효과
                instructorCard.setBackground(new Color(0xE5E5E5));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                instructorCard.setBackground(new Color(0xF5F5F5));
            }
        });

        // 소개글
        instructorCard.add(Box.createVerticalStrut(26));
        instructorCard.add(introPanel(data));

        // 프로필 (사진 + 이름 + 별점)
        JPanel profile = new JPanel();
        profile.setLayout(new BoxLayout(profile, BoxLayout.X_AXIS));
        profile.setMaximumSize(new Dimension(145, 83));
        profile.setOpaque(false);

        // 사진
        profile.add(createImage(data, 83, 83));

        JPanel wrapInfo = new JPanel();
        wrapInfo.setLayout(new BoxLayout(wrapInfo, BoxLayout.Y_AXIS));
        wrapInfo.setMaximumSize(new Dimension(70, 90));
        wrapInfo.setOpaque(false);

        // 이름
        wrapInfo.add(Box.createVerticalStrut(14));
        wrapInfo.add(setupName(data));

        // 별점
        wrapInfo.add(Box.createVerticalStrut(11));
        wrapInfo.add(createStar(data));

        profile.add(wrapInfo);
        instructorCard.add(profile);

        return instructorCard;
    }

    JPanel introPanel(InstructorData data) {
        JPanel introductionPanel = new JPanel();
        introductionPanel.setMaximumSize(new Dimension(106, 46));
        introductionPanel.setOpaque(false);

        JLabel introduction = new JLabel(" " + data.Introduction);
        introduction.setFont(new Font("맑은 고딕", Font.BOLD, 15));

        introductionPanel.add(introduction);

        return introductionPanel;
    }

    static JLabel createImage(InstructorData data, int width, int height) {
        ImageIcon imageIcon = data.getImage();
        Image scaledImage = imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledImageIcon = new ImageIcon(scaledImage);

        return new JLabel(scaledImageIcon);
    }

    static JPanel setupName(InstructorData data) {
        JLabel name = new JLabel(data.name);
        name.setFont(new Font("맑은 고딕", Font.BOLD, 18));

        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
        namePanel.setMaximumSize(new Dimension(54, 19));
        namePanel.setOpaque(false);
        namePanel.add(name);

        return namePanel;
    }

    static JPanel createStar(InstructorData data) {
        JPanel starPanel = new JPanel();
        starPanel.setLayout(new BoxLayout(starPanel, BoxLayout.X_AXIS));
        starPanel.setMaximumSize(new Dimension(60, 40));
        starPanel.setOpaque(false);

        JLabel star = new JLabel("<html><font color='#FFD700'>⭐</font>" +
                " <font color='black'>" + data.star + "</font></html>");
        star.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
        starPanel.add(star);

        return starPanel;
    }

    JPanel setupSearchBar() {
        JPanel searchPanel = new RoundedPanel(13);
        searchPanel.setLayout(new BorderLayout());
        searchPanel.setMaximumSize(new Dimension(300, 29));

        JTextField searchField = new JTextField();
        searchField.setMaximumSize(new Dimension(269, 29));
        searchField.setBackground(Color.WHITE);
        searchPanel.add(searchField, BorderLayout.CENTER);

        JButton searchButton = new JButton("검색");
        searchButton.setForeground(Color.WHITE);
        searchButton.setMaximumSize(new Dimension(26, 26));
        searchButton.setBackground(new Color(0x0C4A6E));
        searchButton.setFocusPainted(false);
        searchPanel.add(searchButton, BorderLayout.EAST);

        return searchPanel;
    }

    JPanel setupSubjectsPanel() {
        JPanel subjectsPanel = new JPanel();
        subjectsPanel.setLayout(new BoxLayout(subjectsPanel, BoxLayout.X_AXIS)); // 수평정렬
        subjectsPanel.setMaximumSize(new Dimension(674, 57));
        subjectsPanel.setBackground(Color.WHITE);

        String[] subjects = {"국어", "수학", "영어", "사회", "과학", "한국사"};

        for (String subject : subjects) {
            subjectsPanel.add(Box.createHorizontalStrut(15));
            subjectsPanel.add(createSubjectPanel(subject));
        }

        return subjectsPanel;
    }

    JPanel createSubjectPanel(String text) {
        JLabel subjectName = new JLabel(text);
        subjectName.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        subjectName.setHorizontalAlignment(JLabel.CENTER); // 수평 가운데
        subjectName.setVerticalAlignment(JLabel.CENTER); // 수직 가운데

        JPanel subjectPanel = new RoundedPanel(8, 1, new Color(0x1E6EA0));
        subjectPanel.setLayout(new BorderLayout());
        subjectPanel.setMaximumSize(new Dimension(92, 60));
        subjectPanel.setPreferredSize(new Dimension(92, 60));
        subjectPanel.setBackground(Color.RED);

        subjectPanel.add(subjectName); // 과목이름
        subjectPanel.setBackground(Color.WHITE);

        return subjectPanel;
    }

    static JScrollPane createScrollPane(JPanel panel, int width, int height) {
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setMaximumSize(new Dimension(width, height));
        scrollPane.setPreferredSize(new Dimension(width, height));
        scrollPane.setBorder(null);
        // 필요할 때만 스크롤
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        return scrollPane;
    }

    // ========== 내부 클래스: InstructorData ==========

    static class InstructorData {
        String Introduction = "고등 수학의 정석";
        String imagepath = "src/main/java/com/project/app/view/Instructors/Person.png";
        String name = "김선생";
        String subject = "수학";
        double star = 4.8;

        String[] lectureList = {"고등 수학 I", "수학 II", "미적분 심화"};

        public String getIntroduction() {
            return Introduction;
        }

        public ImageIcon getImage() {
            // resources 폴더에서 이미지 로드
            try {
                java.net.URL imageURL = getClass().getClassLoader().getResource("person.png");
                if (imageURL != null) {
                    return new ImageIcon(imageURL);
                }
            } catch (Exception e) {
                System.err.println("이미지 로드 실패: " + e.getMessage());
            }
            // 실패 시 빈 ImageIcon 반환
            return new ImageIcon();
        }

        public String getName() {
            return name;
        }

        public double getStar() {
            return star;
        }

        public String[] getLectureList() {
            return lectureList;
        }
    }

    // ========== 내부 클래스: InstructorDetailPopup ==========

    static class InstructorDetailPopup extends JDialog {
        public InstructorDetailPopup(JFrame page, InstructorData data) {
            super(page, "강사 세부 정보", true); // 팝업창 초기화, 제목, 모달
            setSize(600, 500);
            setLocationRelativeTo(page);
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); // 창 닫으면 메모리 해제
            setResizable(false);

            setupInstructorDetailPopup(data);
        }

        void setupInstructorDetailPopup(InstructorData data) {
            JPanel popupPanel = new JPanel();
            popupPanel.setLayout(new BoxLayout(popupPanel, BoxLayout.X_AXIS)); // 수평정렬
            popupPanel.setBackground(Color.WHITE);
            setContentPane(popupPanel);

            // 프로필 + 강의평
            JPanel profileAndReview = new JPanel();
            profileAndReview.setLayout(new BoxLayout(profileAndReview, BoxLayout.Y_AXIS)); // 수직정렬
            profileAndReview.setAlignmentY(Component.TOP_ALIGNMENT);
            profileAndReview.setMaximumSize(new Dimension(160, 460));
            profileAndReview.setOpaque(false);

            profileAndReview.add(setupProfile(data));
            profileAndReview.add(setupReviews(data));

            popupPanel.add(profileAndReview);

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

            for (String lectureName : data.lectureList) {
                wrapLecturesPanel.add(createLecturePanel(data, lectureName));
                wrapLecturesPanel.add(Box.createVerticalStrut(5));
            }

            JScrollPane scrollPane = InstructorsPageView.createScrollPane(wrapLecturesPanel, 387, 284);
            wrapPanel.add(scrollPane);

            popupPanel.add(wrapPanel);
        }

        JPanel createLecturePanel(InstructorData data, String lectureName) {
            JPanel lecturePanel = new JPanel();
            lecturePanel.setMaximumSize(new Dimension(352, 31));
            lecturePanel.setBackground(Color.WHITE);

            lecturePanel.add(new JLabel(lectureName) {{
                setFont(new Font("맑은 고딕", Font.BOLD, 18));
            }});
            lecturePanel.add(InstructorsPageView.createStar(data));

            return lecturePanel;
        }

        JPanel setupProfile(InstructorData data) {
            JPanel profile = new JPanel();
            profile.setLayout(new BoxLayout(profile, BoxLayout.Y_AXIS));
            profile.setMaximumSize(new Dimension(140, 180));
            profile.setOpaque(false);

            JLabel imageLabel = InstructorsPageView.createImage(data, 138, 138);
            JPanel imagePanel = new JPanel();
            imagePanel.setOpaque(false); // 사진 배경 투명하게
            imagePanel.add(imageLabel);

            profile.add(imagePanel);

            JPanel namePanel = new JPanel();
            namePanel.setOpaque(false);
            JLabel subjectInfo = new JLabel("[" + data.subject + "]");
            subjectInfo.setFont(new Font("맑은 고딕", Font.BOLD, 18));
            namePanel.add(subjectInfo);
            namePanel.add(InstructorsPageView.setupName(data));
            profile.add(namePanel);

            return profile;
        }

        JPanel setupReviews(InstructorData data) {
            JPanel starRating = new JPanel();
            starRating.setOpaque(false);
            starRating.setLayout(new BoxLayout(starRating, BoxLayout.Y_AXIS));
            starRating.setMaximumSize(new Dimension(160, 250));

            JLabel reviewLabel = new JLabel("강의평");
            reviewLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
            JPanel reviewPanel = new JPanel();
            reviewPanel.setMaximumSize(new Dimension(150, 30));
            reviewPanel.setOpaque(false);

            reviewPanel.add(reviewLabel);
            reviewPanel.add(InstructorsPageView.createStar(data));

            starRating.add(reviewPanel);
            starRating.add(Box.createVerticalStrut(3));

            JPanel reviews = new JPanel();
            reviews.setLayout(new BoxLayout(reviews, BoxLayout.Y_AXIS));
            reviews.setBackground(new Color(0xEEEEEE));
            reviews.setMaximumSize(new Dimension(140, 185));
            reviews.add(Box.createVerticalGlue());

            starRating.add(reviews);

            return starRating;
        }
    }

    // ========== 내부 클래스: RoundedPanel ==========

    static class RoundedPanel extends JPanel {
        int radius;
        int borderWidth; // 테두리 두께
        Color borderColor; // 테두리 색

        RoundedPanel(int radius) {
            this.radius = radius;
            this.borderWidth = 0;
            this.borderColor = null;
            setOpaque(false); // 배경 투명
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
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // 계단현상 제거

            super.paintComponent(g2);

            // 배경 그리기
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

            // 테두리 그리기
            if (borderColor != null) {
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(borderWidth));
                g2.drawRoundRect(borderWidth / 2, borderWidth / 2, getWidth() - borderWidth,
                        getHeight() - borderWidth, radius, radius);
            }
        }
    }
}
