package com.project.app;

import com.project.app.controller.*;
import com.project.app.repository.*;
import com.project.app.service.*;
import com.project.app.view.*;

import javax.swing.*;

/**
 * 애플리케이션 메인 진입점
 * 
 * 역할:
 * - Repository, Service, Controller 인스턴스 생성 및 의존성 주입
 * - View와 Controller 연결
 * - 애플리케이션 초기화 및 실행
 */
public class App {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // ========== 공통 Repository 생성 ==========
                LectureRepository lectureRepository = new LectureRepositoryImpl();
                ReviewRepository reviewRepository = new ReviewRepositoryImpl();
                
                // ========== Lecture 관련 초기화 ==========
                // 1. Service 인스턴스 생성 (Repository 주입)
                LectureService lectureService = new LectureService((LectureRepositoryImpl) lectureRepository);
                
                // 2. View 인스턴스 생성
                LecturePageView lecturePageView = LecturePageView.getInstance();
                
                // 3. Controller 인스턴스 생성 및 연결
                LectureController lectureController = new LectureController(
                    lecturePageView, 
                    lectureService, 
                    (LectureRepositoryImpl) lectureRepository
                );
                
                // 4. View에 Controller 설정
                lecturePageView.setController(lectureController);
                
                // ========== Instructor 관련 초기화 ==========
                // 1. Repository 인스턴스 생성
                InstructorRepository instructorRepository = new InstructorRepositoryImpl();
                TextbookRepository textbookRepository = new TextbookRepositoryImpl();
                
                // 2. Service 인스턴스 생성 (Repository 주입)
                InstructorService instructorService = new InstructorService(
                    instructorRepository,
                    lectureRepository,
                    textbookRepository,
                    reviewRepository
                );
                
                // 3. View 인스턴스 생성
                InstructorsPageView instructorsPageView = InstructorsPageView.getInstance();
                
                // 4. Controller 인스턴스 생성 및 연결 (LectureService도 주입)
                InstructorController instructorController = new InstructorController(
                    instructorService,
                    lectureService,
                    instructorsPageView
                );
                
                // 5. View에 Controller 설정
                instructorsPageView.setController(instructorController);
                
                // ========== 메인 프레임 생성 및 표시 ==========
                SidePanel sidePanel = SidePanel.getInstance();
                sidePanel.showContent(lecturePageView);
                sidePanel.setSelectedItem(SidePanel.MenuItem.LECTURE);
                sidePanel.setVisible(true);
                
            } catch (Exception e) {
                System.err.println("애플리케이션 초기화 중 오류 발생: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    "애플리케이션을 시작할 수 없습니다.\n" + e.getMessage(), 
                    "오류", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}

