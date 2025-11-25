package com.project.app.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import com.project.app.controller.LoginController;
import com.project.app.service.SignInService;

/**
 * 메인 애플리케이션 프레임
 *
 * 기능:
 * - 좌측에 고정된 네비게이션 메뉴와 우측의 교체 가능한 콘텐츠 영역으로 구성된 메인 프레임
 * - 모든 화면의 기본 레이아웃을 제공하며, 콘텐츠만 교체하여 다양한 페이지를 표시
 *
 * 핵심 내용:
 * - 싱글톤 패턴을 사용하여 애플리케이션 전체에서 하나의 메인 프레임만 존재
 * - 좌측(WEST): 네비게이션 메뉴 패널 (고정)
 * - 우측(CENTER): 콘텐츠 영역 (동적 교체)
 * - showContent(JPanel) 메서드를 통해 콘텐츠를 쉽게 교체 가능
 *
 * 설계 패턴:
 * - 싱글톤 패턴: 전역적으로 하나의 메인 프레임 인스턴스만 유지
 * - 템플릿 메서드 패턴: 프레임 구조는 고정하고 콘텐츠만 교체
 * - 옵저버 패턴: 메뉴 클릭 시 등록된 리스너에게 이벤트 전달
 */
public class SidePanel extends JFrame {

    // ======================== 싱글톤 패턴 구현 ========================

    private static SidePanel instance;

    /**
     * 싱글톤 인스턴스를 반환
     *
     * 기능:
     * - 애플리케이션 전체에서 동일한 사이드바를 공유하기 위함
     * - 메모리 효율성과 일관성 유지
     */
    public static SidePanel getInstance() {
        if (instance == null) {
            instance = new SidePanel();
        }
        return instance;
    }

    // ======================== 스타일 상수 ========================

    /**
     * UI 스타일 상수 클래스
     *
     * 기능:
     * - 디자인 일관성 유지 및 향후 스타일 변경 시 한 곳에서 관리
     * - 매직 넘버를 제거하여 코드 가독성 향상
     */
    private static class StyleConstants {
        // 프레임 크기 관련 상수
        static final int FRAME_WIDTH = 1000;
        static final int FRAME_HEIGHT = 600;

        // 메뉴 패널 크기 관련 상수
        static final int PANEL_WIDTH = 240;
        static final int PANEL_HEIGHT = 600;
        static final int BUTTON_HEIGHT = 55;
        static final int LOGO_HEIGHT = 80;

        // 색상 관련 상수
        static final Color BACKGROUND_COLOR = new Color(26, 90, 107);
        static final Color PRIMARY_COLOR = new Color(26, 90, 107);   
        static final Color SECONDARY_COLOR = new Color(96, 125, 139);
        static final Color SELECTED_COLOR = new Color(96, 125, 139);
        static final Color TEXT_COLOR = Color.WHITE;
        static final Color HOVER_COLOR = new Color(69, 112, 125);      // 호버 시 색상

        // 폰트 관련 상수
        static final Font LOGO_FONT = new Font("맑은 고딕", Font.BOLD, 22);
        static final Font MENU_FONT = new Font("맑은 고딕", Font.PLAIN, 14);

        // 여백 관련 상수
        static final int PADDING_VERTICAL = 12;
        static final int PADDING_HORIZONTAL = 15;
        static final int ICON_TEXT_GAP = 10;
        static final int LOGO_BOTTOM_MARGIN = 20;
        static final int MENU_GAP = 5;
        static final int ICON_SIZE = 20;
    }

    // ======================== 메뉴 아이템 열거형 ========================

    /**
     * 메뉴 항목 정의
     *
     * 기능:
     * - 메뉴 항목을 타입 안전하게 관리
     * - 메뉴 추가/수정 시 한 곳에서 관리 가능
     */
    public enum MenuItem {
        HOME("홈", "home.png"),
        LECTURE("강의", "lecture.png"),
        INSTRUCTOR("강사", "instructor.png"),
        MYPAGE("마이페이지", "mypage.png"),
        HELP("도움말", "help.png"),
        LOGIN("로그인", "login.png"),
        SIGNUP("회원가입", "signup.png");

        private final String label;
        private final String iconPath;

        MenuItem(String label, String iconPath) {
            this.label = label;
            this.iconPath = iconPath;
        }

        public String getLabel() {
            return label;
        }

        public String getIconPath() {
            return iconPath;
        }
    }

    // ======================== 인스턴스 변수 ========================

    private JPanel sideMenuPanel;  // 좌측 메뉴 패널
    private JPanel contentPanel;   // 우측 콘텐츠 영역

    private MenuItem selectedItem;  // 현재 선택된 메뉴 항목
    private Map<MenuItem, JButton> menuButtons;  // 메뉴 항목과 버튼 매핑
    private Map<MenuItem, ActionListener> menuListeners;  // 각 메뉴의 액션 리스너

    // ✅ 로그인 화면용 컨트롤러 (한 번만 생성해서 재사용)
    private LoginController loginController;

    // ======================== 생성자 ========================

    /**
     * SidePanel 생성자
     *
     * 기능:
     * - private 생성자로 외부에서의 직접 인스턴스 생성 방지
     * - 싱글톤 패턴 구현을 위함
     *
     * 핵심 내용:
     * - 프레임의 기본 속성 설정 (크기, 제목, 종료 동작 등)
     * - 좌측에 메뉴 패널, 우측에 콘텐츠 영역 배치
     * - UI 컴포넌트 초기화 및 배치
     */
    private SidePanel() {
        menuButtons = new HashMap<>();
        menuListeners = new HashMap<>();
        selectedItem = MenuItem.MYPAGE;  // 기본 선택: 마이페이지

        initializeFrame();
        createUI();

        // ✅ 여기서 LoginController를 한 번만 생성해서
        //    SignInView와 SignInService를 연결해 둔다.
        SignInView signInView = SignInView.getInstance();
        SignInService signInService = new SignInService();
        loginController = new LoginController(signInView, signInService);

        setupDefaultMenuListeners();  // 기본 메뉴 리스너 설정
        showContent(MyPageView.getInstance());  // 초기 콘텐츠: 마이페이지
        setVisible(true);
    }

    /**
     * 프레임 기본 속성 초기화
     *
     * 기능:
     * - JFrame의 기본 속성을 설정
     * - 화면 크기, 레이아웃, 종료 동작 등을 구성
     *
     * 핵심 내용:
     * - BorderLayout을 사용하여 좌측(메뉴)과 우측(콘텐츠)을 명확하게 분리
     * - 프레임을 화면 중앙에 배치하여 사용자 경험 향상
     */
    private void initializeFrame() {
        setTitle("일타강사");
        setSize(StyleConstants.FRAME_WIDTH, StyleConstants.FRAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // 화면 중앙에 배치
        setResizable(false);  // 크기 조절 비활성화
        setLayout(new BorderLayout());
    }

    // ======================== UI 생성 메서드 ========================

    /**
     * UI 컴포넌트 생성 및 배치
     *
     * 기능:
     * - 프레임을 좌우로 나누어 메뉴 패널과 콘텐츠 영역으로 구성
     *
     * 핵심 내용:
     * 1. 좌측: 네비게이션 메뉴 패널 생성 및 배치
     * 2. 우측: 빈 콘텐츠 영역 생성 (추후 showContent로 교체)
     */
    private void createUI() {
        // 좌측: 메뉴 패널 생성 및 추가
        sideMenuPanel = createSideMenuPanel();
        add(sideMenuPanel, BorderLayout.WEST);

        // 우측: 콘텐츠 영역 생성 및 추가
        contentPanel = new JPanel();
        contentPanel.setBackground(new Color(245, 245, 245));  // 연한 회색 배경
        add(contentPanel, BorderLayout.CENTER);
    }

    /**
     * 사이드 메뉴 패널 생성
     *
     * 기능:
     * - 좌측에 고정될 네비게이션 메뉴 패널 생성
     * - 로고와 메뉴 항목들을 수직으로 배치
     *
     * 핵심 내용:
     * 1. 상단에 로고 추가
     * 2. 중앙에 주요 메뉴 추가 (홈, 강의, 강사, 마이페이지, 도움말)
     * 3. 하단에 인증 메뉴 추가 (로그인, 회원가입)
     */
    private JPanel createSideMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setPreferredSize(new Dimension(StyleConstants.PANEL_WIDTH, StyleConstants.PANEL_HEIGHT));
        menuPanel.setBackground(StyleConstants.BACKGROUND_COLOR);
        menuPanel.setLayout(new BorderLayout());
        // 상단: 로고 패널
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(StyleConstants.BACKGROUND_COLOR);
        topPanel.add(createLogoPanel());
        topPanel.add(Box.createRigidArea(new Dimension(0, StyleConstants.LOGO_BOTTOM_MARGIN)));
        menuPanel.add(topPanel, BorderLayout.NORTH);

        // 중앙: 주요 메뉴 (홈, 강의, 강사, 마이페이지, 도움말)
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(StyleConstants.BACKGROUND_COLOR);

        // 주요 메뉴 아이템: HOME, LECTURE, INSTRUCTOR, MYPAGE, HELP
        MenuItem[] mainMenuItems = {
            MenuItem.HOME,
            MenuItem.LECTURE,
            MenuItem.INSTRUCTOR,
            MenuItem.MYPAGE,
            MenuItem.HELP
        };

        for (int i = 0; i < mainMenuItems.length; i++) {
            MenuItem item = mainMenuItems[i];

            JPanel buttonWrapper = new JPanel();
            buttonWrapper.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
            buttonWrapper.setBackground(StyleConstants.BACKGROUND_COLOR);
            buttonWrapper.setMaximumSize(new Dimension(StyleConstants.PANEL_WIDTH, StyleConstants.BUTTON_HEIGHT));

            JButton menuButton = createMenuButton(item);
            menuButtons.put(item, menuButton);

            menuButton.setMaximumSize(new Dimension(180, StyleConstants.BUTTON_HEIGHT));
            menuButton.setPreferredSize(new Dimension(180, StyleConstants.BUTTON_HEIGHT));

            buttonWrapper.add(menuButton);
            centerPanel.add(buttonWrapper);

            // 마지막 항목이 아니면 간격 추가
            if (i < mainMenuItems.length - 1) {
                centerPanel.add(Box.createRigidArea(new Dimension(0, StyleConstants.MENU_GAP)));
            }
        }

        // 중앙 패널에 여유 공간 추가 (하단 메뉴와의 간격)
        centerPanel.add(Box.createVerticalGlue());

        menuPanel.add(centerPanel, BorderLayout.CENTER);

        // 하단: 인증 메뉴 (로그인, 회원가입)
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(StyleConstants.BACKGROUND_COLOR);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));  // 상하 여백

        // 인증 메뉴 아이템: LOGIN, SIGNUP
        MenuItem[] authMenuItems = {
            MenuItem.LOGIN,
            MenuItem.SIGNUP
        };

        for (int i = 0; i < authMenuItems.length; i++) {
            MenuItem item = authMenuItems[i];

            JPanel buttonWrapper = new JPanel();
            buttonWrapper.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
            buttonWrapper.setBackground(StyleConstants.BACKGROUND_COLOR);
            buttonWrapper.setMaximumSize(new Dimension(StyleConstants.PANEL_WIDTH, StyleConstants.BUTTON_HEIGHT));

            JButton menuButton = createMenuButton(item);
            menuButtons.put(item, menuButton);

            menuButton.setMaximumSize(new Dimension(180, StyleConstants.BUTTON_HEIGHT));
            menuButton.setPreferredSize(new Dimension(180, StyleConstants.BUTTON_HEIGHT));

            buttonWrapper.add(menuButton);
            bottomPanel.add(buttonWrapper);

            // 마지막 항목이 아니면 간격 추가
            if (i < authMenuItems.length - 1) {
                bottomPanel.add(Box.createRigidArea(new Dimension(0, StyleConstants.MENU_GAP)));
            }
        }

        menuPanel.add(bottomPanel, BorderLayout.SOUTH);

        return menuPanel;
    }

    /**
     * 기본 메뉴 리스너 설정
     *
     * 기능:
     * - 각 메뉴 항목에 기본 동작을 연결
     * - 구현된 View는 실제 화면으로, 미구현된 메뉴는 플레이스홀더로 표시
     *
     * 핵심 내용:
     * - HOME: HomePageView 싱글톤 인스턴스 반환 및 표시
     * - LECTURE: LecturePageView 싱글톤 인스턴스 반환 및 표시
     * - INSTRUCTOR: InstructorsPageView 싱글톤 인스턴스 반환 및 표시
     * - MYPAGE: MyPageView 싱글톤 인스턴스 반환 및 표시
     * - HELP: 아직 미구현으로 플레이스홀더 표시
     * - LOGIN: SignInView 싱글톤 인스턴스 반환 및 표시
     * - SIGNUP: SignUpView 싱글톤 인스턴스 반환 및 표시
     */
    private void setupDefaultMenuListeners() {
        // 홈 페이지
        setMenuListener(MenuItem.HOME, e -> {
            showContent(HomePageView.getInstance());
        });

        // 강의 목록
        setMenuListener(MenuItem.LECTURE, e -> {
            showContent(LecturePageView.getInstance());
        });

        // 강사 목록
        setMenuListener(MenuItem.INSTRUCTOR, e -> {
            showContent(InstructorsPageView.getInstance());
        });

        // 마이페이지
        setMenuListener(MenuItem.MYPAGE, e -> {
            MyPageView myPage = MyPageView.getInstance();
            myPage.refresh();
            showContent(myPage);
        });


        // 도움말 (아직 미구현)
        setMenuListener(MenuItem.HELP, e -> {
            showContent(createPlaceholderContent("도움말"));
        });

        // 로그인
        setMenuListener(MenuItem.LOGIN, e -> {
            showContent(SignInView.getInstance());
        });

        // 회원가입
        setMenuListener(MenuItem.SIGNUP, e -> {
            showContent(SignUpView.getInstance());
        });
    }

    /**
     * 임시 콘텐츠 패널 생성
     *
     * 기능:
     * - 아직 구현되지 않은 메뉴를 위한 플레이스홀더 패널 생성
     * - 개발 중임을 명확히 표시
     *
     * @param title 표시할 제목
     * @return 플레이스홀더 패널
     */
    private JPanel createPlaceholderContent(String title) {
        JPanel placeholder = new JPanel();
        placeholder.setLayout(new BorderLayout());
        placeholder.setBackground(Color.WHITE);

        JLabel label = new JLabel(title + " (개발 중)", SwingConstants.CENTER);
        label.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        label.setForeground(StyleConstants.SECONDARY_COLOR);

        placeholder.add(label, BorderLayout.CENTER);

        return placeholder;
    }

    /**
     * 아이콘 이미지 로드 및 크기 조정
     *
     * 기능:
     * - resources/icons 폴더에서 아이콘 이미지 로드
     * - 아이콘 크기를 StyleConstants.ICON_SIZE에 맞게 조정
     *
     * @param iconPath 아이콘 파일명
     * @return 크기가 조정된 ImageIcon, 로드 실패 시 null
     */
    private ImageIcon loadIcon(String iconPath) {
        try {
            java.net.URL iconURL = getClass().getClassLoader().getResource("icons/" + iconPath);
            if (iconURL != null) {
                ImageIcon originalIcon = new ImageIcon(iconURL);
                Image scaledImage = originalIcon.getImage().getScaledInstance(
                    StyleConstants.ICON_SIZE,
                    StyleConstants.ICON_SIZE,
                    Image.SCALE_SMOOTH
                );
                return new ImageIcon(scaledImage);
            }
        } catch (Exception e) {
            System.err.println("아이콘 로드 실패: " + iconPath + " - " + e.getMessage());
        }
        return null;
    }

    /**
     * 로고 패널 생성
     *
     * 기능:
     * - 브랜드 아이덴티티를 나타내는 로고 영역
     *
     * 핵심 내용:
     * - JLabel을 사용하여 "ILTAGANGSA" 텍스트 표시
     * - 중앙 정렬 및 스타일 적용
     */
    private JPanel createLogoPanel() {
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(StyleConstants.BACKGROUND_COLOR);
        logoPanel.setMaximumSize(new Dimension(
            StyleConstants.PANEL_WIDTH,
            StyleConstants.LOGO_HEIGHT
        ));
        logoPanel.setLayout(new BorderLayout());
        logoPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JLabel logoLabel = new JLabel("ILTAGANGSA", SwingConstants.CENTER);
        logoLabel.setFont(StyleConstants.LOGO_FONT);
        logoLabel.setForeground(StyleConstants.TEXT_COLOR);

        logoPanel.add(logoLabel, BorderLayout.CENTER);

        return logoPanel;
    }

    /**
     * 메뉴 버튼 생성
     *
     * 기능:
     * - 각 메뉴 항목을 클릭 가능한 버튼으로 구현
     * - 일관된 스타일과 동작을 적용
     *
     * 핵심 내용:
     * 1. JButton 생성 및 아이콘과 텍스트 설정
     * 2. 스타일 적용 (배경색, 폰트, 정렬 등)
     * 3. 클릭 이벤트 리스너 추가
     * 4. 현재 선택된 항목이면 선택 표시
     *
     * @param item 메뉴 항목
     * @return 생성된 메뉴 버튼
     */
    private JButton createMenuButton(MenuItem item) {
        JButton button = new JButton(item.getLabel());

        // 아이콘 로드 및 설정
        ImageIcon icon = loadIcon(item.getIconPath());
        if (icon != null) {
            button.setIcon(icon);
            button.setHorizontalTextPosition(SwingConstants.RIGHT);
            button.setIconTextGap(StyleConstants.ICON_TEXT_GAP);
        }

        // 버튼 스타일 설정
        button.setFont(StyleConstants.MENU_FONT);
        button.setForeground(StyleConstants.TEXT_COLOR);
        button.setBackground(
            item == selectedItem ?
            StyleConstants.SELECTED_COLOR :
            StyleConstants.BACKGROUND_COLOR
        );
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setMaximumSize(new Dimension(
            StyleConstants.PANEL_WIDTH,
            StyleConstants.BUTTON_HEIGHT
        ));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);

        button.setBorder(BorderFactory.createEmptyBorder(
            StyleConstants.PADDING_VERTICAL,
            StyleConstants.PADDING_HORIZONTAL,
            StyleConstants.PADDING_VERTICAL,
            StyleConstants.PADDING_HORIZONTAL
        ));

        // 마우스 호버 효과
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (item != selectedItem) {
                    button.setBackground(StyleConstants.HOVER_COLOR);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (item != selectedItem) {
                    button.setBackground(StyleConstants.BACKGROUND_COLOR);
                }
            }
        });

        // 클릭 이벤트 처리
        button.addActionListener(e -> {
            handleMenuClick(item);
        });

        return button;
    }

    // ======================== 이벤트 처리 메서드 ========================

    /**
     * 메뉴 클릭 처리
     *
     * 기능:
     * - 메뉴 클릭 시 선택 상태 업데이트 및 이벤트 전파
     *
     * 핵심 내용:
     * 1. 이전 선택 항목의 배경색을 기본색으로 변경
     * 2. 새로운 선택 항목의 배경색을 선택색으로 변경
     * 3. 등록된 액션 리스너가 있으면 실행
     *
     * @param item 클릭된 메뉴 항목
     */
    private void handleMenuClick(MenuItem item) {
        // 이전 선택 항목의 스타일 초기화
        if (selectedItem != null && menuButtons.containsKey(selectedItem)) {
            menuButtons.get(selectedItem).setBackground(StyleConstants.BACKGROUND_COLOR);
        }

        // 새로운 선택 항목 업데이트
        selectedItem = item;
        menuButtons.get(item).setBackground(StyleConstants.SELECTED_COLOR);

        // 등록된 리스너 실행
        ActionListener listener = menuListeners.get(item);
        if (listener != null) {
            listener.actionPerformed(
                new java.awt.event.ActionEvent(this, java.awt.event.ActionEvent.ACTION_PERFORMED, item.name())
            );
        }
    }

    // ======================== 공개 메서드 ========================

    /**
     * 콘텐츠 영역에 새로운 패널 표시
     *
     * 기능:
     * - 우측 콘텐츠 영역의 내용을 동적으로 교체
     * - 팀원들은 JPanel을 상속받은 콘텐츠만 만들면 이 메서드로 쉽게 표시 가능
     *
     * 핵심 내용:
     * 1. 기존 콘텐츠 영역의 모든 컴포넌트 제거
     * 2. 새로운 콘텐츠 패널을 CENTER에 추가
     * 3. revalidate()와 repaint()를 호출하여 화면 갱신
     *
     * @param content 표시할 콘텐츠 패널 (JPanel 상속 객체)
     */
    public void showContent(JPanel content) {
        // 기존 콘텐츠 제거
        contentPanel.removeAll();

        // 새로운 콘텐츠 추가
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(content, BorderLayout.CENTER);

        // 화면 갱신
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    /**
     * 특정 메뉴 항목에 액션 리스너 등록
     *
     * 기능:
     * - 외부(Controller)에서 메뉴 클릭 이벤트를 처리할 수 있도록 함
     * - View와 Controller의 분리를 지원
     *
     * @param item 메뉴 항목
     * @param listener 실행할 액션 리스너
     */
    public void setMenuListener(MenuItem item, ActionListener listener) {
        menuListeners.put(item, listener);
    }

    /**
     * 현재 선택된 메뉴 항목을 프로그래밍 방식으로 변경
     *
     * 기능:
     * - 외부에서 특정 메뉴를 선택 상태로 표시해야 할 때 사용
     *
     * @param item 선택할 메뉴 항목
     */
    public void setSelectedItem(MenuItem item) {
        handleMenuClick(item);
    }

    /**
     * 현재 선택된 메뉴 항목 반환
     *
     * @return 현재 선택된 메뉴 항목
     */
    public MenuItem getSelectedItem() {
        return selectedItem;
    }

    // ======================== 테스트용 메인 메서드 ========================

    /**
     * SidePanel 단독 테스트를 위한 메인 메서드
     *
     * 기능:
     * - 개발 중 SidePanel을 독립적으로 실행하여 UI 확인
     *
     * 핵심 내용:
     * - SidePanel 인스턴스 생성 (JFrame)
     * - 생성자에서 자동으로 setupDefaultMenuListeners() 호출됨
     * - 마이페이지는 MyPageView, 나머지는 플레이스홀더 표시
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // SidePanel 인스턴스 생성 (싱글톤 패턴으로 자동으로 화면에 표시됨)
            SidePanel.getInstance();
            System.out.println("SidePanel 테스트 실행 중...");
            System.out.println("좌측 메뉴를 클릭하여 콘텐츠 교체를 확인하세요.");
            System.out.println("이제 모든 View가 실제로 연결되어 있습니다.");
        });
    }
}
