package com.project.app.view;

import javax.swing.*;

import com.project.app.controller.LectureController;
import com.project.app.model.Lecture;
import com.project.app.repository.LectureRepositoryImpl;
import com.project.app.service.LectureService;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 강의 목록 화면 뷰
 *
 * 기능:
 * - SidePanel 우측 콘텐츠 영역에 들어갈 "강의" 화면
 * - 과목별 필터링, 정렬, 검색 기능 제공
 * - 강의 카드 클릭 시 상세 정보 다이얼로그 표시
 * - 싱글톤 패턴을 사용하여 애플리케이션 전체에서 하나의 인스턴스만 유지
 */
public class LecturePageView extends JPanel {

    // 싱글톤 패턴: private static 인스턴스 변수
    private static LecturePageView instance;

    // UI 컴포넌트들
    private Map<String, JButton> subjectButtons = new HashMap<>();
    private Map<String, JButton> gradeButtons = new HashMap<>();
    private JComboBox<String> sortCombo;
    private JComboBox<String> academyCombo;
    private JTextField searchField;
    private JButton searchBtn;
    private JPanel gridPanel;



    // 이벤트 리스너
    private LectureCardClickListener cardClickListener;
    /**
     * 싱글톤 인스턴스를 반환하는 메서드
     *
     * 기능:
     * - 인스턴스가 없으면 새로 생성하고, 있으면 기존 인스턴스 반환
     * - 메모리 효율성과 상태 유지를 위함
     *
     * @return LecturePageView의 싱글톤 인스턴스
     */
    public static LecturePageView getInstance() {
        if (instance == null) {
            instance = new LecturePageView();
        }
        return instance;
    }

    // 싱글톤 패턴: private 생성자
    private LecturePageView() {
        // 760 x 600 크기의 메인 콘텐츠 패널
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 콘텐츠 영역 생성
        JPanel right = createContentPanel();

        // 스크롤 패널에 담아서 추가
        JScrollPane scrollPane = new JScrollPane(right);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        System.out.println("현재 작업 디렉토리: " + System.getProperty("user.dir"));
        String filePath = System.getProperty("user.dir") + "\\src\\main\\data\\LectureData.txt";
        System.out.println("파일 경로: " + filePath);


        LectureRepositoryImpl repository = new LectureRepositoryImpl();
        repository.loadLecturesFromFile(filePath);

        LectureService service = new LectureService(repository);
        new LectureController(this, service, repository);
    }

    private JPanel contentPanel;

    private JPanel createContentPanel() {
        contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setPreferredSize(new Dimension(760, 900));
        contentPanel.setLayout(null);

        // 상단 과목 버튼
        addSubjectButtons(contentPanel);

        // 검색 영역
        addSearchArea(contentPanel);

        // 정렬, 학원 콤보박스
        addSortAndAcademy(contentPanel);

        // 학년 선택 버튼
        addGradeButtons(contentPanel);

        // 강의 카드 그리드 패널 생성
        createLectureGrid(contentPanel);

        return contentPanel;
    }

    private void addSubjectButtons(JPanel parent){

        // 상단 과목 버튼
        String[] subjects = { "국어", "수학", "영어", "사회", "과학", "한국사", "모의고사" };
        int btnWidth = 90;
        int btnHeight = 60;
        int startX = 35;
        int startY = 40;
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
            subjectButtons.put(subjects[i], subjectBtn);
            parent.add(subjectBtn);
        }

    }

    private void addSearchArea(JPanel parent) {
        // 검색창
        searchField = new JTextField();
        searchField.setBounds(505,120,160,30);
        searchField.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(30, 110, 160), 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 75)
        ));
        parent.add(searchField);

        // 검색 버튼
        searchBtn = new JButton("검색");
        searchBtn.setBounds(665,120,60,30);
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

        parent.add(searchBtn);

    }
    private void addSortAndAcademy(JPanel parent) {
        // 정렬 라벨
        JLabel sortLabel = new JLabel("정렬");
        sortLabel.setBounds(40, 120, 50, 30);
        sortLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
        parent.add(sortLabel);


        String[] sortOptions = {"최신순", "인기순", "평점순"};
        sortCombo = new JComboBox<>(sortOptions);
        sortCombo.setBounds(80, 120, 100, 30);
        sortCombo.setFont(new Font("Malgun Gothic", Font.PLAIN, 12));
        parent.add(sortCombo);

        // 학원 라벨
        JLabel academyLabel = new JLabel("학원");
        academyLabel.setBounds(210, 120, 50, 30);
        academyLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
        parent.add(academyLabel);

        String[] academyOptions={"전체","메가스터디","이투스","대성마이맥"};
        academyCombo = new JComboBox<>(academyOptions);
        academyCombo.setBounds(250, 120, 120, 30);
        academyCombo.setFont(new Font("Malgun Gothic", Font.PLAIN, 12));
        parent.add(academyCombo);

        // 학원 선택 이벤트
        academyCombo.addActionListener(e -> {
            String selected = (String) academyCombo.getSelectedItem();
            System.out.println("학원: " + selected);
        });
        parent.add(academyCombo);
    }
    // 학년 선택 버튼
    private void addGradeButtons(JPanel parent) {
        String[] grades = {"고1", "고2", "고3/N수"};
        int btnWidth = 70;
        int btnHeight = 35;
        int startX = 40;
        int startY = 175;
        int btnGap = 10;

        for (int i = 0; i < grades.length; i++) {
            int x = startX + i * (btnWidth + btnGap);

            JButton gradeBtn = new JButton(grades[i]);
            gradeBtn.setBounds(x, startY, btnWidth, btnHeight);
            gradeBtn.setFont(new Font("Malgun Gothic", Font.BOLD, 13));
            gradeBtn.setForeground(new Color(100, 110, 100));
            gradeBtn.setBackground(Color.WHITE);
            gradeBtn.setFocusPainted(false);
            gradeBtn.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
            gradeBtn.setContentAreaFilled(true);
            gradeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            gradeBtn.setActionCommand(grades[i]);

            // 호버 효과
            gradeBtn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    if (gradeBtn.getBackground().equals(Color.WHITE)) {
                        gradeBtn.setBackground(new Color(245, 248, 252));
                    }
                }
                public void mouseExited(MouseEvent e) {
                    if (!gradeBtn.getBackground().equals(new Color(30, 110, 160))) {
                        gradeBtn.setBackground(Color.WHITE);
                    }
                }
            });
            gradeButtons.put(grades[i], gradeBtn);
            parent.add(gradeBtn);
        }
    }

    // 강의 카드 그리드 패널 생성
    private JScrollPane lectureScroll;  // ← 필드 추가

    private void createLectureGrid(JPanel parent) {
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(0, 4, 20, 20));
        gridPanel.setBackground(Color.WHITE);
        gridPanel.setBounds(30, 240, 700, 2000);
        parent.add(gridPanel);
    }

    // 강의 카드 생성
    private JPanel createCourseCard(Lecture lecture) {
        int width = 160;
        int height = 180;

        JPanel card = new JPanel();
        card.setLayout(null);
        card.setOpaque(true);
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(width, height));
        card.setMaximumSize(new Dimension(width, height));
        card.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 썸네일 영역
        JPanel thumbnail = new JPanel();
        thumbnail.setBounds(10, 10, width - 20, 100);
        thumbnail.setBackground(new Color(240, 240, 240));
        thumbnail.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        JLabel thumbText = new JLabel("썸네일", JLabel.CENTER);
        thumbText.setFont(new Font("Malgun Gothic", Font.PLAIN, 12));
        thumbText.setForeground(new Color(150, 150, 150));
        thumbnail.add(thumbText);
        card.add(thumbnail);

        // 강의명
        String shortTitle = lecture.getTitle().length() > 15
                ? lecture.getTitle().substring(0, 15) + "..."
                : lecture.getTitle();
        JLabel titleLabel = new JLabel(shortTitle, JLabel.CENTER);
        titleLabel.setBounds(10, 115, width - 20, 25);
        titleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 12));
        card.add(titleLabel);

        // 별점
        String ratingHtml = String.format(
                "<html><span style='color:#FFD700; font-size:14px;'>★</span> " +
                        "<span style='font-size:12px;'>%.1f</span></html>",
                lecture.getRating()
        );
        JLabel ratingLabel = new JLabel(ratingHtml);
        ratingLabel.setBounds(15, 145, width - 20, 20);
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
                // Controller에 이벤트 전달
                if (cardClickListener != null) {
                    cardClickListener.onCardClick((lecture.getLectureId()));
                }
            }
        });

        return card;
    }


// ========== Controller 연동 메서드 ==========

    /**
     * 과목 버튼에 리스너 등록
     */
    public void addSubjectButtonListener(String subject, ActionListener listener) {
        JButton btn = subjectButtons.get(subject);
        if (btn != null) {
            btn.addActionListener(listener);
        }
    }

    /**
     * 학년 버튼에 리스너 등록
     */
    public void addGradeButtonListener(String grade, ActionListener listener) {
        JButton btn = gradeButtons.get(grade);
        if (btn != null) {
            btn.addActionListener(listener);
        }
    }

    /**
     * 학원 콤보박스에 리스너 등록
     */
    public void addAcademyComboListener(ActionListener listener) {
        academyCombo.addActionListener(listener);
    }

    /**
     * 정렬 콤보박스에 리스너 등록
     */
    public void addSortComboListener(ActionListener listener) {
        sortCombo.addActionListener(listener);
    }

    /**
     * 검색 버튼에 리스너 등록
     */
    public void addSearchButtonListener(ActionListener listener) {
        searchBtn.addActionListener(listener);
        // Enter 키로도 검색 가능
        searchField.addActionListener(listener);
    }

    /**
     * 강의 카드 클릭 리스너 등록
     */
    public void addLectureCardClickListener(LectureCardClickListener listener) {
        this.cardClickListener = listener;
    }

    // ========== 데이터 가져오기 ==========

    /**
     * 검색 키워드 가져오기
     */
    public String getSearchKeyword() {
        return searchField.getText().trim();
    }

    /**
     * 선택된 정렬 기준 가져오기
     */
    public String getSelectedSort() {
        return (String) sortCombo.getSelectedItem();
    }

    /**
     * 선택된 학원 가져오기
     */
    public String getSelectedAcademy() {
        return (String) academyCombo.getSelectedItem();
    }

    // ========== 화면 업데이트 ==========

    /**
     * 강의 카드 목록 업데이트
     */
    public void updateLectureCards(List<Lecture> lectures) {
        gridPanel.removeAll();

        for (Lecture lecture : lectures) {
            JPanel card = createCourseCard(lecture);
            gridPanel.add(card);
        }

        // GridLayout 높이 계산 (4열 기준)
        int cardHeight = 180;
        int gap = 20;
        int cardsPerRow = 4;
        int rows = (lectures.size() + cardsPerRow - 1) / cardsPerRow;
        int newHeight = rows * (cardHeight + gap) + gap;

        // gridPanel 높이 조정
        gridPanel.setBounds(30, 240, 700, newHeight);

        // contentPanel 높이도 조정 (전체 페이지가 커짐)
        int contentHeight = 240 + newHeight + 50;
        contentPanel.setPreferredSize(new Dimension(760, contentHeight));

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    /**
     * 강의 상세 정보 다이얼로그 표시
     */
    public void showLectureDetail(Lecture lecture) {
        LectureDetailView detail = new LectureDetailView(
                lecture.getTitle(),
                lecture.getInstructor(),
                lecture.getRating(),
                lecture.getDayOfWeek(),
                lecture.getLocation(),
                lecture.getTextbook(),
                lecture.getTextbookPrice(),
                lecture.getDescription(),
                lecture.getCurrentEnrolled(),
                lecture.getCapacity(),
                lecture.getGrade()
        );
        detail.show();
    }

    /**
     * 메시지 다이얼로그 표시
     */
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "알림", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 필터 초기화
     */
    public void resetFilters() {
        searchField.setText("");
        sortCombo.setSelectedIndex(0);
        academyCombo.setSelectedIndex(0);

        // 모든 과목 버튼 초기화
        for (JButton btn : subjectButtons.values()) {
            btn.setBackground(Color.WHITE);
            btn.setForeground(Color.BLACK);
        }

        // 모든 학년 버튼 초기화
        for (JButton btn : gradeButtons.values()) {
            btn.setForeground(new Color(100, 100, 100));
            btn.setBackground(Color.WHITE);
            btn.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        }
    }

    // ========== 함수형 인터페이스 ==========

    /**
     * 강의 카드 클릭 이벤트 리스너
     */
    @FunctionalInterface
    public interface LectureCardClickListener {
        void onCardClick(String lectureId);
    }
}
