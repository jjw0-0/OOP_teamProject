package main.java.com.project.app.view;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * 한 파일 안에 MVC 구조를 전부 넣은 예제
 * - MainFrame : 전체 창 + 좌측 사이드바 + 우측 홈화면
 * - HomeImageModel : 이미지 목록 + 현재 인덱스 관리 (Model)
 * - HomeImageView : 이미지 + 좌우 버튼 화면 (View)
 * - HomeImageController : 버튼 이벤트 처리, Model↔View 연결 (Controller)
 */
public class MainFrame extends JFrame {

    // ======== 메인 프레임 ========
    public MainFrame() {
        setTitle("이미지 홈 화면 예제 (MVC, 단일 파일)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 전체 크기 1000 x 600
        setSize(1000, 600);
        setLocationRelativeTo(null); // 화면 중앙에 띄우기

        // 전체 레이아웃: 좌측(WEST)에 사이드바, CENTER에 홈 화면
        getContentPane().setLayout(new BorderLayout());

        // 1) 왼쪽 사이드바 (기능 없음, 크기만 맞춰둠)
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setPreferredSize(new Dimension(240, 600));
        sidebarPanel.setBackground(Color.LIGHT_GRAY); // 눈에만 보이게 색 넣음

        // 2) 오른쪽 홈 화면 (View + Model + Controller)
        HomeImageView homeView = new HomeImageView();

        // 실제 사용할 이미지 경로 리스트
        // => 여기를 네가 가진 이미지 파일명으로 바꿔줘야 한다.
        HomeImageModel homeModel = new HomeImageModel(
                Arrays.asList(
                        "src/main/resources/1.png",
                        "src/main/resources/2.png"
                )
        );

        // Controller 생성 (버튼 리스너 등록 + 첫 이미지 표시)
        new HomeImageController(homeModel, homeView);

        // 프레임에 추가
        getContentPane().add(sidebarPanel, BorderLayout.WEST);
        getContentPane().add(homeView, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }

    // ======== Model ========
    /**
     * 이미지 경로 목록과 현재 인덱스를 관리하는 Model
     */
    static class HomeImageModel {

        private final List<String> imagePaths; // 이미지 파일 경로 리스트
        private int currentIndex = 0;          // 현재 인덱스 (0부터 시작)

        public HomeImageModel(List<String> imagePaths) {
            this.imagePaths = imagePaths;
        }

        public boolean isEmpty() {
            return imagePaths.isEmpty();
        }

        public String getCurrentImagePath() {
            return imagePaths.get(currentIndex);
        }

        public void next() {
            if (currentIndex < imagePaths.size() - 1) {
                currentIndex++;
            }
            else if (currentIndex == imagePaths.size() - 1) {
                currentIndex = 0;
            }
        }

        public void prev() {
            if (currentIndex > 0) {
                currentIndex--;
            }
            else if (currentIndex == 0) {
                currentIndex = imagePaths.size() - 1;
            }
        }
    }

        // ======== View ========
    /**
     * 오른쪽 영역(760x600) 안에
     * - 중앙: 이미지
     * - 이미지 위에 겹쳐서 좌우: 이전/다음 버튼
     * 을 보여주는 화면
     */
    static class HomeImageView extends JPanel {

        private final JLabel imageLabel; // 이미지를 표시할 라벨
        private final RoundButton prevButton;
        private final RoundButton nextButton;

        public HomeImageView() {
            // 오른쪽 영역 크기
            setPreferredSize(new Dimension(760, 600));

            // 바깥쪽은 BorderLayout 사용 (CENTER에 이미지 라벨 하나만 둘 예정)
            setLayout(new BorderLayout());

            // 이미지 표시용 라벨
            imageLabel = new JLabel();
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setVerticalAlignment(SwingConstants.CENTER);

            // 중요: 라벨도 컨테이너처럼 사용하기 위해 레이아웃을 설정
            // 이렇게 하면 라벨에 아이콘(이미지)을 깔고, 그 위에 버튼을 올릴 수 있다.
            imageLabel.setLayout(new BorderLayout());

            // 동그란 반투명 버튼 생성
            prevButton = new RoundButton("<");
            nextButton = new RoundButton(">");

            // 버튼 크기 (동그랗게 그릴 예정이므로 가로/세로 같게)
            Dimension buttonSize = new Dimension(60, 60);
            prevButton.setPreferredSize(buttonSize);
            nextButton.setPreferredSize(buttonSize);

            // 이 패널에 이미지 라벨 추가
            add(imageLabel, BorderLayout.CENTER);

            // 이미지 라벨의 왼쪽/오른쪽에 버튼을 올린다 (이미지 위에 겹쳐진 느낌)
            imageLabel.add(prevButton, BorderLayout.WEST);
            imageLabel.add(nextButton, BorderLayout.EAST);
        }

        public void setImageIcon(ImageIcon icon) {
            imageLabel.setText(null);
            imageLabel.setIcon(icon);
        }

        public void setNoImageText(String text) {
            imageLabel.setIcon(null);
            imageLabel.setText(text);
        }

        public void addPrevButtonListener(java.awt.event.ActionListener listener) {
            prevButton.addActionListener(listener);
        }

        public void addNextButtonListener(java.awt.event.ActionListener listener) {
            nextButton.addActionListener(listener);
        }
    }

    /**
     * 동그란 반투명 버튼
     * - 배경: 살짝 투명한 검정 원
     * - 글자: 흰색 ("<", ">")
     * - hover 색 변화 같은 거 없음 (최소 기능만)
     */
    static class RoundButton extends JButton {

        public RoundButton(String text) {
            super(text);
            // 기본 버튼 느낌을 빼고, 우리가 직접 그릴 것이라 설정
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            // 안티앨리어싱(계단현상 방지) 켜기
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int diameter = Math.min(width, height);      // 정원(동그라미) 크기
            int x = (width - diameter) / 2;
            int y = (height - diameter) / 2;

            // 반투명 검정 배경 (알파값 80 정도)
            g2.setColor(new Color(0, 0, 0, 80));
            g2.fillOval(x, y, diameter, diameter);

            // 텍스트(화살표) 그리기 - 흰색
            g2.setColor(Color.WHITE);
            String text = getText();
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getAscent();

            int textX = (width - textWidth) / 2;
            int textY = (height + textHeight) / 2 - 3; // 살짝 위아래 중앙 맞추기용 -3

            g2.drawString(text, textX, textY);

            g2.dispose();
        }
    }


    // ======== Controller ========
    /**
     * 버튼 이벤트를 처리하고,
     * Model의 인덱스를 바꾼 뒤 View의 이미지를 갱신한다.
     */
    static class HomeImageController {

        private final HomeImageModel model;
        private final HomeImageView view;

        public HomeImageController(HomeImageModel model, HomeImageView view) {
            this.model = model;
            this.view = view;

            initListeners();
            updateImage(); // 시작할 때 첫 이미지 보여주기
        }

        private void initListeners() {
            view.addPrevButtonListener(e -> {
                model.prev();
                updateImage();
            });

            view.addNextButtonListener(e -> {
                model.next();
                updateImage();
            });
        }

        private void updateImage() {
            if (model.isEmpty()) {
                view.setNoImageText("표시할 이미지가 없습니다.");
                return;
            }

            String path = model.getCurrentImagePath();
            ImageIcon icon = new ImageIcon(path);
            view.setImageIcon(icon);
        }
    }
}
