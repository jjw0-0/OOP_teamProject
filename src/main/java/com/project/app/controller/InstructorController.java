package com.project.app.controller;

import com.project.app.dto.InstructorCardView;
import com.project.app.dto.InstructorDetailResponse;
import com.project.app.dto.InstructorListResponse;
import com.project.app.dto.InstructorSearchRequest;
import com.project.app.model.Lecture;
import com.project.app.service.InstructorService;
import com.project.app.service.LectureService;
import com.project.app.view.InstructorsPageView;
import com.project.app.view.LectureDetailView;

import javax.swing.*;
import java.util.List;

/**
 * 강사 페이지 컨트롤러
 *
 * 역할:
 * - View와 Service 사이의 연결
 * - 사용자 이벤트 처리
 * - View 업데이트
 */
public class InstructorController {

    private final InstructorService instructorService;
    private final LectureService lectureService;
    private final InstructorsPageView view;
    private String currentSort = "reviewScore";

    public InstructorController(InstructorService instructorService, InstructorsPageView view) {
        this.instructorService = instructorService;
        this.lectureService = null; // LectureService는 나중에 설정
        this.view = view;
        
        // 초기 강사 목록 로드
        initialize();
    }
    
    public InstructorController(InstructorService instructorService, LectureService lectureService, InstructorsPageView view) {
        this.instructorService = instructorService;
        this.lectureService = lectureService;
        this.view = view;
        
        // 초기 강사 목록 로드
        initialize();
    }

    /**
     * 초기 강사 목록 로드
     */
    public void initialize() {
        refreshList();
    }

    /**
     * 강사 검색 처리
     *
     * @param keyword 검색 키워드
     */
    public void handleSearch(String keyword) {
        refreshList();
    }

    /**
     * 과목 필터 처리
     *
     * @param subject 선택된 과목
     */
    public void handleSubjectFilter(String subject) {
        refreshList();
    }

    /**
     * 학원 필터 처리
     *
     * @param academyId 선택된 학원 ID
     */
    public void handleAcademyFilter(String academyId) {
        refreshList();
    }

    /**
     * 정렬 옵션 변경 처리
     *
     * @param sortOrder 정렬 키
     */
    public void handleSortChange(String sortOrder) {
        this.currentSort = sortOrder != null ? sortOrder : "reviewScore";
        refreshList();
    }

    /**
     * 강사 클릭 처리
     *
     * @param instructorId 강사 ID
     */
    public void handleInstructorClick(String instructorId) {
        InstructorDetailResponse detail = instructorService.getInstructorDetail(instructorId);
        if (detail != null) {
            view.showInstructorDetailPopup(detail);
        } else {
            JOptionPane.showMessageDialog(
                    view,
                    "강사 정보를 찾을 수 없습니다.",
                    "오류",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * 강사 목록 업데이트
     *
     * @param response 강사 목록 응답
     */
    private void updateInstructorList(InstructorListResponse response) {
        if (response == null || response.isEmpty()) {
            view.showEmptyMessage("검색 결과가 없습니다.");
            return;
        }

        List<InstructorCardView> instructors = response.getInstructors();
        view.updateInstructorCards(instructors);
    }

    private void refreshList() {
        InstructorListResponse response = instructorService.searchInstructors(buildCurrentRequest());
        updateInstructorList(response);
    }

    private InstructorSearchRequest buildCurrentRequest() {
        return new InstructorSearchRequest(
                view.getSearchKeyword(),
                view.getSelectedSubject(),
                view.getSelectedAcademy(),
                currentSort
        );
    }
    
    /**
     * 강의 상세 정보 표시
     * 
     * @param lectureId 강의 ID
     */
    public void showLectureDetail(String lectureId) {
        if (lectureService == null) {
            JOptionPane.showMessageDialog(
                    view,
                    "강의 서비스를 사용할 수 없습니다.",
                    "오류",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        // 강의 정보 조회
        Lecture lecture = lectureService.getLecture(lectureId);
        if (lecture == null) {
            JOptionPane.showMessageDialog(
                    view,
                    "강의 정보를 찾을 수 없습니다.",
                    "오류",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        // LectureDetailView 생성
        LectureDetailView detailView = new LectureDetailView(
                lecture.getTitle(),
                lecture.getInstructor(),
                lecture.getRating(),
                lecture.getDayOfWeek(),
                lecture.getTime(),
                lecture.getLocation(),
                lecture.getTextbook(),
                lecture.getTextbookPrice(),
                lecture.getDescription(),
                lecture.getCurrentEnrolled(),
                lecture.getCapacity(),
                lecture.getGrade(),
                lecture.getThumbnailPath()
        );
        
        // LectureDetailController 생성 및 연결
        String currentUserId = "testUser"; // 실제로는 로그인된 사용자 ID를 가져와야 함
        new com.project.app.controller.LectureDetailController(
                detailView, 
                lectureService, 
                currentUserId, 
                lectureId
        );
        
        // 다이얼로그 표시
        detailView.show();
    }
}

