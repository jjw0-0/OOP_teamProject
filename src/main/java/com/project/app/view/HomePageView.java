package com.project.app.view;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

import com.project.app.model.HomePageModel;
import com.project.app.controller.HomePageController;

/**
 * 홈 화면 뷰
 *
 * 기능:
 * - SidePanel 우측 콘텐츠 영역에 들어갈 "홈" 화면
 * - 내부에 MVC 구조(HomeImageModel, HomeImageView, HomeImageController)를 사용하여
 *   이미지 슬라이더(이전/다음) 기능 제공
 * - 싱글톤 패턴을 사용하여 애플리케이션 전체에서 하나의 인스턴스만 유지
 *
 */
public class HomePageView extends JPanel {

    // 싱글톤 패턴: private static 인스턴스 변수
    private static HomePageView instance;

    /**
     * 싱글톤 인스턴스를 반환하는 메서드
     *
     * 기능:
     * - 인스턴스가 없으면 새로 생성하고, 있으면 기존 인스턴스 반환
     * - 메모리 효율성과 상태 유지를 위함
     *
     * @return HomePageView의 싱글톤 인스턴스
     */
    public static HomePageView getInstance() {
        if (instance == null) {
            instance = new HomePageView();
        }
        return instance;
    }

    private final HomePageModel model;
    private final HomeImageView view;

    // Controller는 View와 Model을 연결하는 역할만 하므로 필드로 유지
    @SuppressWarnings("unused")
    private final HomePageController controller;

    // 싱글톤 패턴: private 생성자
    private HomePageView() {
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

        /**
         * 이미지를 패널 크기에 맞춰 비율을 유지하면서 스케일링하여 표시
         * - 패널 크기: 760x600 (preferred size)
         * - 이미지 비율을 유지하면서 패널에 맞게 조정
         */
        public void setImageIcon(ImageIcon icon) {
            imageLabel.setText(null);

            if (icon == null || icon.getImage() == null) {
                imageLabel.setIcon(null);
                return;
            }

            // 원본 이미지 크기
            int originalWidth = icon.getIconWidth();
            int originalHeight = icon.getIconHeight();

            // 패널 크기 (preferred size 기준)
            int panelWidth = 760;
            int panelHeight = 600;

            // 비율을 유지하면서 패널에 맞게 스케일링
            double widthRatio = (double) panelWidth / originalWidth;
            double heightRatio = (double) panelHeight / originalHeight;
            double scale = Math.min(widthRatio, heightRatio); // 비율 유지를 위해 더 작은 비율 사용

            int scaledWidth = (int) (originalWidth * scale);
            int scaledHeight = (int) (originalHeight * scale);

            // 이미지 스케일링 (SCALE_SMOOTH: 부드러운 스케일링)
            Image scaledImage = icon.getImage().getScaledInstance(
                scaledWidth,
                scaledHeight,
                Image.SCALE_SMOOTH
            );

            imageLabel.setIcon(new ImageIcon(scaledImage));
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
}
