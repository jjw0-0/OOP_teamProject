package com.project.app.view;

import com.project.app.controller.InstructorController;
import com.project.app.dto.InstructorCardView;
import com.project.app.dto.InstructorDetailResponse;
import com.project.app.dto.InstructorListResponse;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
/**
 * 강사 페이지 뷰
 *
 * 기능:
 * - 싱글톤 패턴을 사용하여 애플리케이션 전체에서 하나의 인스턴스만 유지
 */
public class InstructorsPageView extends JPanel {

    // 싱글톤 패턴: private static 인스턴스 변수
    private static InstructorsPageView instance;

    static {
        try {
            // 스크롤바의 기본 너비를 10 픽셀로 설정
            UIManager.put("ScrollBar.width", 10);
            UIManager.put("ScrollBar.thumbWidth", 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
    private InstructorController controller;
    private List<InstructorCardView> currentInstructors;

    private InstructorsPageView() {
        this.controller = InstructorController.getInstance();
        this.currentInstructors = new ArrayList<>();

        setPreferredSize(new Dimension(760, 600));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);

        add(Box.createVerticalStrut(30));
        add(setupSubjectsPanel());

        add(Box.createVerticalStrut(30));
        JPanel wrapSearchBar = new RoundedPanel(13);
        wrapSearchBar.setMaximumSize(new Dimension(674, 29));
        wrapSearchBar.setBackground(Color.WHITE);
        wrapSearchBar.setLayout(new BoxLayout(wrapSearchBar, BoxLayout.X_AXIS));
        wrapSearchBar.add(Box.createHorizontalStrut(405));
        wrapSearchBar.add(setupSearchBar());
        add(wrapSearchBar);

        add(Box.createVerticalStrut(30));

        // 초기 데이터 로드
        loadInstructors(null);
    }

    /**
     * 강사 목록 로드 및 UI 갱신
     */
    private void loadInstructors(String subject) {
        InstructorListResponse response;
        if (subject == null || subject.isEmpty()) {
            response = controller.getAllInstructors();
        } else {
            response = controller.getInstructorsBySubject(subject);
        }

        this.currentInstructors = response.getInstructors();
        refreshInstructorsList();
    }

    /**
     * 강사 목록 UI 갱신
     */
    private void refreshInstructorsList() {
        // 기존 강사 목록 패널 제거
        Component[] components = getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                if (panel.getLayout() instanceof GridLayout) {
                    remove(panel);
                    break;
                }
            }
        }

        // 새로운 강사 목록 추가
        add(setupInstructorsList());
        revalidate();
        repaint();
    }

    JPanel setupInstructorsList() {
        JPanel instructorsList = new JPanel();
        instructorsList.setMaximumSize(new Dimension(701, 378));
        instructorsList.setLayout(new GridLayout(2, 4, 10, 10));
        instructorsList.setBorder(new EmptyBorder(8, 18, 8, 18));
        instructorsList.setBackground(Color.WHITE);

        // 실제 데이터로 강사 카드 생성 (최대 8개)
        int count = Math.min(currentInstructors.size(), 8);
        for (int i = 0; i < count; i++) {
            instructorsList.add(Instructor(currentInstructors.get(i)));
        }

        // 8개 미만이면 빈 카드로 채우기
        for (int i = count; i < 8; i++) {
            JPanel emptyCard = new JPanel();
            emptyCard.setBackground(new Color(0xF5F5F5));
            instructorsList.add(emptyCard);
        }

        return instructorsList;
    }

    JPanel Instructor(InstructorCardView data) {
        JPanel instructorCard = new RoundedPanel(18);
        instructorCard.setMaximumSize(new Dimension(156, 176));
        instructorCard.setLayout(new BoxLayout(instructorCard, BoxLayout.Y_AXIS));
        instructorCard.setBackground(new Color(0xF5F5F5));

        // 마우스 커서 및 이벤트 설정
        instructorCard.setCursor(new Cursor(Cursor.HAND_CURSOR));
        instructorCard.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // 강사 상세 정보 조회
                InstructorDetailResponse detailData = controller.getInstructorDetail(data.getId());
                if (detailData != null) {
                    new InstructorDetailPopup(
                            (JFrame) SwingUtilities.getWindowAncestor(InstructorsPageView.this),
                            detailData
                    ).setVisible(true);
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

    JPanel introPanel(InstructorCardView data) {
        JPanel introductionPanel = new JPanel();
        introductionPanel.setMaximumSize(new Dimension(150, 46));
        introductionPanel.setOpaque(false);

        String intro = data.getIntroduction();

        JLabel introduction = new JLabel(intro);
        introduction.setFont(new Font("맑은 고딕", Font.BOLD, 15));

        // FontMetrics로 실제 텍스트 너비 계산
        FontMetrics fm = introduction.getFontMetrics(introduction.getFont());
        int textWidth = fm.stringWidth(intro);
        int maxWidth = 145; // 패널 너비보다 약간 작게

        // 텍스트가 너비를 초과하면 잘라내기
        if (textWidth > maxWidth) {
            String ellipsis = "...";
            int ellipsisWidth = fm.stringWidth(ellipsis);
            int availableWidth = maxWidth - ellipsisWidth;

            // 글자를 하나씩 줄여가며 맞는 길이 찾기
            for (int i = intro.length() - 1; i > 0; i--) {
                String shortened = intro.substring(0, i);
                if (fm.stringWidth(shortened) <= availableWidth) {
                    intro = shortened + ellipsis;
                    break;
                }
            }
            introduction.setText(intro);
        }

        introductionPanel.add(introduction);

        return introductionPanel;
    }

    static JLabel createImage(InstructorCardView data, int width, int height) {
        ImageIcon imageIcon;

        System.out.println("=== 이미지 로드 디버깅 ===");
        System.out.println("ProfileImagePath: " + data.getProfileImagePath());

        if (data.getProfileImagePath() != null && !data.getProfileImagePath().isEmpty()) {
            try {
                String path = data.getProfileImagePath();
                String fileName = path.substring(path.lastIndexOf("/") + 1);

                System.out.println("파일명 추출: " + fileName);
                String fullPath = "InstructorThumbnail/" + fileName;
                System.out.println("최종 경로: " + fullPath);

                java.net.URL imageURL = InstructorsPageView.class.getClassLoader()
                        .getResource(fullPath);

                System.out.println("URL 결과: " + imageURL);

                if (imageURL != null) {
                    imageIcon = new ImageIcon(imageURL);
                    System.out.println("이미지 로드 성공!");
                } else {
                    System.err.println("이미지 파일을 찾을 수 없음: " + fullPath);
                    imageIcon = getDefaultImage();
                }
            } catch (Exception e) {
                System.err.println("이미지 로드 실패: " + e.getMessage());
                e.printStackTrace();
                imageIcon = getDefaultImage();
            }
        } else {
            System.out.println("ProfileImagePath가 null 또는 비어있음");
            imageIcon = getDefaultImage();
        }

        Image scaledImage = imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledImageIcon = new ImageIcon(scaledImage);

        return new JLabel(scaledImageIcon);
    }

    static ImageIcon getDefaultImage() {
        try {
            java.net.URL imageURL = InstructorsPageView.class.getClassLoader().getResource("person.png");
            if (imageURL != null) {
                return new ImageIcon(imageURL);
            }
        } catch (Exception e) {
            System.err.println("기본 이미지 로드 실패: " + e.getMessage());
        }
        return new ImageIcon();
    }

    static JPanel setupName(InstructorCardView data) {
        JLabel name = new JLabel(data.getName());
        name.setFont(new Font("맑은 고딕", Font.BOLD, 18));

        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
        namePanel.setMaximumSize(new Dimension(54, 19));
        namePanel.setOpaque(false);
        namePanel.add(name);

        return namePanel;
    }

    static JPanel createStar(InstructorCardView data) {
        JPanel starPanel = new JPanel();
        starPanel.setLayout(new BoxLayout(starPanel, BoxLayout.X_AXIS));
        starPanel.setMaximumSize(new Dimension(60, 40));
        starPanel.setOpaque(false);

        JLabel star = new JLabel("<html><font color='#FFD700'>⭐</font>" +
                " <font color='black'>" + String.format("%.1f", data.getReviewScore()) + "</font></html>");
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

        // 검색 버튼 클릭 이벤트
        searchButton.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            if (!keyword.isEmpty()) {
                InstructorListResponse response = controller.searchInstructors(keyword);
                currentInstructors = response.getInstructors();
                refreshInstructorsList();
            }
        });

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
        subjectName.setHorizontalAlignment(JLabel.CENTER);
        subjectName.setVerticalAlignment(JLabel.CENTER);

        JPanel subjectPanel = new RoundedPanel(8, 1, new Color(0x1E6EA0));
        subjectPanel.setLayout(new BorderLayout());
        subjectPanel.setMaximumSize(new Dimension(92, 60));
        subjectPanel.setPreferredSize(new Dimension(92, 60));
        subjectPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        subjectPanel.add(subjectName);
        subjectPanel.setBackground(Color.WHITE);

        // 과목 버튼 클릭 이벤트
        subjectPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                loadInstructors(text);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                subjectPanel.setBackground(new Color(0xE5E5E5));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                subjectPanel.setBackground(Color.WHITE);
            }
        });

        return subjectPanel;
    }

    // ========== 내부 클래스: InstructorDetailPopup ==========

    static class InstructorDetailPopup extends JDialog {
        public InstructorDetailPopup(JFrame page, InstructorDetailResponse data) {
            super(page, "강사 세부 정보", true);
            setSize(600, 500);
            setLocationRelativeTo(page);
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            setResizable(false);

            setupInstructorDetailPopup(data);
        }

        void setupInstructorDetailPopup(InstructorDetailResponse data) {
            // BorderLayout 대신 null layout 사용
            JPanel popupPanel = new JPanel(null);
            popupPanel.setBackground(Color.WHITE);
            setContentPane(popupPanel);

            // 프로필 (왼쪽 상단)
            JPanel profilePanel = setupProfile(data);
            profilePanel.setBounds(20, 20, 160, 180);
            popupPanel.add(profilePanel);

            // 강의평 (왼쪽 하단)
            JPanel reviewPanel = setupReviews(data);
            reviewPanel.setBounds(20, 210, 160, 230);
            popupPanel.add(reviewPanel);

            // 강의소개 내용 (오른쪽 상단)
            JTextPane introPane = new JTextPane();
            introPane.setText(data.getIntroduction());
            introPane.setFont(new Font("맑은 고딕", Font.BOLD, 18));
            introPane.setEditable(false);
            introPane.setFocusable(false);
            introPane.setBackground(new Color(0xF5F5F5));
            introPane.setBorder(new EmptyBorder(10, 10, 10, 10));

            StyledDocument doc = introPane.getStyledDocument();
            SimpleAttributeSet center = new SimpleAttributeSet();
            StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
            doc.setParagraphAttributes(0, doc.getLength(), center, false);

            introPane.setBounds(200, 30, 350, 80);
            popupPanel.add(introPane);

            // 강의목록 제목 (2번째 이미지처럼 아래쪽)
            JLabel lectureTitle = new JLabel("강의목록");
            lectureTitle.setFont(new Font("맑은 고딕", Font.BOLD, 24));
            lectureTitle.setBounds(200, 150, 100, 30);
            popupPanel.add(lectureTitle);

            // 강의목록
            JPanel wrapLecturesPanel = new JPanel();
            wrapLecturesPanel.setLayout(new BoxLayout(wrapLecturesPanel, BoxLayout.Y_AXIS));
            wrapLecturesPanel.setBackground(new Color(0xEEEEEE));
            wrapLecturesPanel.add(Box.createVerticalStrut(8));

            for (InstructorDetailResponse.LectureSummary lecture : data.getLectures()) {
                wrapLecturesPanel.add(createLecturePanel(lecture));
                wrapLecturesPanel.add(Box.createVerticalStrut(5));
            }

            JScrollPane scrollPane = InstructorsPageView.createScrollPane(wrapLecturesPanel, 360, 180);
            scrollPane.setBounds(200, 190, 360, 230);
            popupPanel.add(scrollPane);
        }

        JPanel createLecturePanel(InstructorDetailResponse.LectureSummary lecture) {
            JPanel lecturePanel = new JPanel();
            lecturePanel.setMaximumSize(new Dimension(352, 31));
            lecturePanel.setBackground(Color.WHITE);

            lecturePanel.add(new JLabel(lecture.getName()) {{
                setFont(new Font("맑은 고딕", Font.BOLD, 18));
            }});

            // 별점 표시
            JPanel starPanel = new JPanel();
            starPanel.setLayout(new BoxLayout(starPanel, BoxLayout.X_AXIS));
            starPanel.setOpaque(false);
            JLabel star = new JLabel("<html><font color='#FFD700'>⭐</font>" +
                    " <font color='black'>" + String.format("%.1f", lecture.getReviewScore()) + "</font></html>");
            star.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
            starPanel.add(star);

            lecturePanel.add(starPanel);

            return lecturePanel;
        }

        JPanel setupProfile(InstructorDetailResponse data) {
            JPanel profile = new JPanel();
            profile.setLayout(new BoxLayout(profile, BoxLayout.Y_AXIS));
            profile.setMaximumSize(new Dimension(160, 180));
            profile.setPreferredSize(new Dimension(160,180));
            profile.setOpaque(false);

            /// 이미지
            ImageIcon imageIcon = getDefaultImage();
            if (data.getProfileImagePath() != null && !data.getProfileImagePath().isEmpty()) {
                try {
                    String path = data.getProfileImagePath();
                    String fileName = path.substring(path.lastIndexOf("/") + 1);

                    java.net.URL imageURL = InstructorsPageView.class.getClassLoader()
                            .getResource("InstructorThumbnail/" + fileName);
                    if (imageURL != null) {
                        imageIcon = new ImageIcon(imageURL);
                    }
                } catch (Exception e) {
                    // 기본 이미지 사용
                }
            }
            Image scaledImage = imageIcon.getImage().getScaledInstance(138, 138, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));

            JPanel imagePanel = new JPanel();
            imagePanel.setOpaque(false);
            imagePanel.add(imageLabel);

            profile.add(imagePanel);

            JPanel namePanel = new JPanel();
            namePanel.setOpaque(false);
            JLabel subjectInfo = new JLabel("[" + data.getSubject() + "]");
            subjectInfo.setFont(new Font("맑은 고딕", Font.BOLD, 18));
            namePanel.add(subjectInfo);

            JLabel name = new JLabel(data.getName());
            name.setFont(new Font("맑은 고딕", Font.BOLD, 18));
            namePanel.add(name);

            profile.add(namePanel);

            return profile;
        }

        JPanel setupReviews(InstructorDetailResponse data) {
            JPanel starRating = new JPanel();
            starRating.setOpaque(false);
            starRating.setLayout(new BorderLayout(0,3));
            starRating.setMaximumSize(new Dimension(160, 280));
            starRating.setPreferredSize(new Dimension(160, 280));

            JLabel reviewLabel = new JLabel("강의평");
            reviewLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
            JPanel reviewPanel = new JPanel();
            reviewPanel.setMaximumSize(new Dimension(160, 30));
            reviewPanel.setPreferredSize(new Dimension(160, 30));
            reviewPanel.setOpaque(false);

            reviewPanel.add(reviewLabel);

            // 평점 표시
            JPanel starPanel = new JPanel();
            starPanel.setOpaque(false);
            JLabel star = new JLabel("<html><font color='#FFD700'>⭐</font>" +
                    " <font color='black'>" + String.format("%.1f", data.getReviewScore()) + "</font></html>");
            star.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
            starPanel.add(star);
            reviewPanel.add(starPanel);

            starRating.add(reviewPanel,BorderLayout.NORTH);

            JPanel reviewsListContainer = new JPanel();
            reviewsListContainer.setLayout(new BoxLayout(reviewsListContainer, BoxLayout.Y_AXIS));
            reviewsListContainer.setBackground(new Color(0xEEEEEE));

            // 1. **더미 데이터 리스트** 정의 (실제 DTO 구현 시 대체 필요)
            // 강사 ID I031(김현수쌤)의 리뷰와 다른 강사 리뷰 일부를 사용합니다.
            String rawReviews =
                    "I031/R098/user002/개념 설명 명쾌하고 JUST THIS 교재 최고!\n" +
                            "I031/R099/user008/생윤 강의 듣고 3등급에서 1등급 됐어요\n" +
                            "I031/R100/user027/설명 체계적이고 이해하기 쉬워요\n" +
                            "I031/R101/user029/사문 김현수쌤이 최고예요\n" +
                            "I001/R001/user005/유형 분석 체계적이고 실전 적용 가능해요\n" +
                            "I002/R005/user008/개념과 문풀 간극 메워주는 강의!\n" +
                            "I002/R006/user012/수학 본질 이해하게 해주세요";

            String[] lines = rawReviews.split("\n");

            // 2. 데이터 수만큼 리뷰 패널 생성 및 추가
            for (String line : lines) {
                String[] parts = line.split("/");
                if (parts.length >= 4) {
                    String userId = parts[2];
                    String content = parts[3];

                    reviewsListContainer.add(Box.createVerticalStrut(5));
                    // createReviewPanel 호출하여 리뷰 카드 생성 및 추가
                    reviewsListContainer.add(createReviewPanel(userId, content));
                }
            }
            reviewsListContainer.add(Box.createVerticalStrut(5));

            // 3. 스크롤 패널로 감싸서 starRating의 CENTER에 추가
            // InstructorsPageView 클래스에 있는 static createScrollPane 메서드를 사용해야 합니다.
            JScrollPane scrollPane = InstructorsPageView.createScrollPane(reviewsListContainer, 160, 230);
            scrollPane.setPreferredSize(new Dimension(160, 230));
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            starRating.add(scrollPane, BorderLayout.CENTER);
            // ----------------------------------------------------------------------

            return starRating;
        }
        /**
         * 단일 강의평 (리뷰) 카드 패널을 생성하여 반환 (레이아웃 관리자 사용)
         * (InstructorDetailPopup 클래스 내부 메서드)
         */
        JPanel createReviewPanel(String userId, String content) {
            JPanel reviewPanel = new RoundedPanel(8);
            reviewPanel.setMaximumSize(new Dimension(155, 65));
            reviewPanel.setPreferredSize(new Dimension(155, 65));
            // BorderLayout을 사용하여 NORTH에 유저 ID, CENTER에 리뷰 내용을 배치
            reviewPanel.setLayout(new BorderLayout(5, 2)); // 수평 5, 수직 2 간격
            reviewPanel.setBackground(Color.WHITE);
            reviewPanel.setBorder(new EmptyBorder(5, 5, 5, 5)); // 내부 여백

            // 1. 유저 ID (NORTH)
            JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            userPanel.setOpaque(false);
            JLabel userLabel = new JLabel(userId);
            userLabel.setFont(new Font("맑은 고딕", Font.BOLD, 12));
            userPanel.add(userLabel);
            reviewPanel.add(userPanel, BorderLayout.NORTH);

            // 2. 리뷰 내용 (CENTER)
            JTextPane contentPane = new JTextPane();
            contentPane.setText(content);
            contentPane.setFont(new Font("맑은 고딕", Font.PLAIN, 11));
            contentPane.setEditable(false);
            contentPane.setFocusable(false);
            contentPane.setBackground(Color.WHITE);

            // JTextPane을 CENTER에 배치하면 남은 공간을 모두 차지합니다.
            reviewPanel.add(contentPane, BorderLayout.CENTER);

            return reviewPanel;
        }
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
