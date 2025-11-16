package main.java.com.project.app.view;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

import main.java.com.project.app.model.HomePageModel;
import main.java.com.project.app.controller.HomePageController;

/**
 * 홈 화면 뷰
 *
 * 역할:
 * - SidePanel 우측 콘텐츠 영역에 들어갈 "홈" 화면
 * - 내부에 MVC 구조(HomeImageModel, HomeImageView, HomeImageController)를 사용하여
 *   이미지 슬라이더(이전/다음) 기능 제공
 *
 * 사용 예 (나중에 SidePanel에서 쓸 때):
 *   SidePanel.getInstance().showContent(new HomePageView());
 */
public class HomePageView extends JPanel {

    private final HomePageModel model;
    private final HomeImageView view;
    private final HomePageController controller;

    public HomePageView() {
        // 이 패널 자체가 SidePanel의 우측 CENTER에 들어간다고 가정
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // View 생성
        view = new HomeImageView();

        // 실제 사용할 이미지 경로 리스트
        // => 프로젝트에 맞게 경로를 수정해서 사용하세요.
        List<String> imagePaths = Arrays.asList(
            "src/main/resources/HomePageImages/340x200_5.png",
            "src/main/resources/HomePageImages/main_251113_allpass.jpg"
        );

        // Model 생성
        model = new HomePageModel(imagePaths);

        // Controller 생성 (버튼 리스너 등록 + 첫 이미지 표시)
        controller = new HomePageController(model, view);

        // 이 패널의 CENTER에 View 추가
        add(view, BorderLayout.CENTER);
    }


    // ======== View ========

    /**
     * 오른쪽 영역 안에
     * - 중앙: 이미지
     * - 이미지 위에 겹쳐서 좌우: 이전/다음 버튼
     * 을 보여주는 화면
     */
    public class HomeImageView extends JPanel {

        private final JLabel imageLabel; // 이미지를 표시할 라벨
        private final RoundButton prevButton;
        private final RoundButton nextButton;

        public HomeImageView() {
            // 우측 영역 기본 크기 (SidePanel과 합쳐졌을 때 자연스럽게 보이도록)
            setPreferredSize(new Dimension(760, 600));

            // 바깥쪽은 BorderLayout 사용 (CENTER에 이미지 라벨 하나만 둘 예정)
            setLayout(new BorderLayout());
            setBackground(Color.WHITE);

            // 이미지 표시용 라벨
            imageLabel = new JLabel();
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setVerticalAlignment(SwingConstants.CENTER);

            // 라벨도 컨테이너처럼 사용하기 위해 레이아웃을 설정
            imageLabel.setLayout(new BorderLayout());

            // 동그란 반투명 버튼 생성
            prevButton = new RoundButton("<");
            nextButton = new RoundButton(">");

            // 버튼 크기
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



    // ======== 단독 테스트용 main (원하면 삭제해도 됨) ========

    // public static void main(String[] args) {
    //     SwingUtilities.invokeLater(() -> {
    //         JFrame testFrame = new JFrame("홈 화면 테스트");
    //         testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //         testFrame.setSize(760, 600);
    //         testFrame.setLocationRelativeTo(null);

    //         testFrame.setLayout(new BorderLayout());
    //         testFrame.add(new HomePageView(), BorderLayout.CENTER);

    //         testFrame.setVisible(true);
    //     });
    // }
}
