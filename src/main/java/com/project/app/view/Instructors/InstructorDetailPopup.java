package com.project.app.view.Instructors;

import javax.swing.*;
import java.awt.*;


public class InstructorDetailPopup extends JDialog {
    public InstructorDetailPopup(JFrame page, InstructorData data){
        super(page, "강사 세부 정보", true); // 팝업창 초기화, 제목
        setSize(600,500);
        setLocationRelativeTo(page);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); // 창 닫으면 메모리 해제
        setResizable(false);

        setupInstructorDetailPopup(data);
    }

    void setupInstructorDetailPopup(InstructorData data){
        JPanel popupPanel=new JPanel();
        popupPanel.setLayout(new BoxLayout(popupPanel,BoxLayout.X_AXIS)); // 수평정렬
        popupPanel.setBackground(Color.white);
        setContentPane(popupPanel);

        //프로필 + 강의평
        JPanel ProfileAndReview=new JPanel();
        ProfileAndReview.setLayout(new BoxLayout(ProfileAndReview,BoxLayout.Y_AXIS)); // 수직정렬
        ProfileAndReview.setAlignmentY(Component.TOP_ALIGNMENT);
        ProfileAndReview.setMaximumSize(new Dimension(160,460));
        ProfileAndReview.setOpaque(false);

        ProfileAndReview.add(setupProfile(data));
        ProfileAndReview.add(setupReviews(data));

        //ProfileAndReview.add(Box.createVerticalGlue());

        popupPanel.add(ProfileAndReview);

        //강의목록
        JPanel wrapPanel =new JPanel();
        wrapPanel.setLayout(new BoxLayout(wrapPanel,BoxLayout.Y_AXIS));
        wrapPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        wrapPanel.add(Box.createVerticalStrut(80));
        wrapPanel.setOpaque(false);

        JPanel titlePanel=new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
        titlePanel.setMaximumSize(new Dimension(387,40));
        titlePanel.setOpaque(false);
        titlePanel.add(new JLabel("강의목록"){{
            setFont(new Font("맑은 고딕", Font.BOLD,24));
        }});

        wrapPanel.add(titlePanel);

        JPanel wrapLecturesPanel=new JPanel();
        wrapLecturesPanel.setLayout(new BoxLayout(wrapLecturesPanel,BoxLayout.Y_AXIS));
        //wrapLecturesPanel.setMaximumSize(new Dimension(387,284));
        wrapLecturesPanel.setBackground(new Color(0xEEEEEE));

        wrapLecturesPanel.add(Box.createVerticalStrut(8));

        for(String lectureName:data.lectureList){
            wrapLecturesPanel.add(createLecturePanel(data,lectureName));
            wrapLecturesPanel.add(Box.createVerticalStrut(5));

        }

        JScrollPane scorllPane=InstructorsPage.createScrollPane(wrapLecturesPanel,387,284);
        wrapPanel.add(scorllPane);
        //wrapPanel.add(wrapLecturesPanel);


        popupPanel.add(wrapPanel);

    }

    JPanel createLecturePanel(InstructorData data,String lectureName){
        JPanel lecturePanel=new JPanel();
        lecturePanel.setMaximumSize(new Dimension(352,31));
        lecturePanel.setBackground(Color.white);

        lecturePanel.add(new JLabel(lectureName){{
            setFont(new Font("맑은 고딕",Font.BOLD,18));
        }});
        lecturePanel.add(InstructorsPage.createStar(data));
        //lecturePanel.add(Box.createHorizontalGlue());

        return lecturePanel;
    }

    JPanel setupProfile(InstructorData data){

        JPanel Profile=new JPanel();
        Profile.setLayout(new BoxLayout(Profile,BoxLayout.Y_AXIS));
        Profile.setMaximumSize(new Dimension(140,180));
        Profile.setOpaque(false);

        JLabel ImageLabel=InstructorsPage.createImage(data,138,138);
        JPanel ImagePanel=new JPanel();
        ImagePanel.setOpaque(false); // 사진 배경 투명하게
        ImagePanel.add(ImageLabel);

        Profile.add(ImagePanel);

        JPanel namePanel=new JPanel();
        namePanel.setOpaque(false);
        JLabel subjectInfo=new JLabel("["+data.subject+"]");
        subjectInfo.setFont(new Font("맑은 고딕",Font.BOLD,18));
        namePanel.add(subjectInfo);
        namePanel.add(InstructorsPage.setupName(data));
        Profile.add(namePanel);

        return Profile;

    }

    JPanel setupReviews(InstructorData data){
        JPanel starRating =new JPanel();
        starRating.setOpaque(false);
        starRating.setLayout(new BoxLayout(starRating,BoxLayout.Y_AXIS));
        starRating.setMaximumSize(new Dimension(160,250));

        JLabel reviewLabel=new JLabel("강의평");
        reviewLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        JPanel reviewPanel=new JPanel();
        reviewPanel.setMaximumSize(new Dimension(150,30));
        reviewPanel.setOpaque(false);

        reviewPanel.add(reviewLabel);
        reviewPanel.add(InstructorsPage.createStar(data));

        starRating.add(reviewPanel);
        starRating.add(Box.createVerticalStrut(3));

        JPanel reviews=new JPanel();
        reviews.setLayout(new BoxLayout(reviews,BoxLayout.Y_AXIS));
        reviews.setBackground(new Color( 0xEEEEEE));
        reviews.setMaximumSize(new Dimension(140,185));
//        reviews.setPreferredSize(new Dimension(140,185));
//        reviews.setMinimumSize(new Dimension(140,185));
        reviews.add(Box.createVerticalGlue());

        starRating.add(reviews);

        return starRating;
    }


}
