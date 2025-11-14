package com.project.app.view.Instructors;

import javax.swing.*;
import java.awt.*;

public class RoundedPanel extends JPanel {
    int radius;
    int borderWidth; // 테두리 두께
    Color borderColor; // 테두리 색

    RoundedPanel(int radius){ // 테두리 없는 경우
        this.radius=radius;
        this.borderWidth=0;
        this.borderColor=null;
        setOpaque(false); // 배경 투명
    }
    RoundedPanel(int radius, int borderWidth, Color borderColor){
        this.radius=radius;
        this.borderWidth=borderWidth;
        this.borderColor=borderColor;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g){
        Graphics2D g2= (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON); // 계단현상 제거

        super.paintComponent(g2);

        g2.setColor(getBackground());
        g2.fillRoundRect(0,0,getWidth(),getHeight(),radius,radius);

        if(borderColor!=null) {
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(borderWidth));
            g2.drawRoundRect(borderWidth / 2, borderWidth / 2, getWidth() - borderWidth, getHeight() - borderWidth, radius, radius);
        }
    }


}
