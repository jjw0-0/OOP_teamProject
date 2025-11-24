package com.project.app.controller;

import com.project.app.dto.EnrollLectureRequest;
import com.project.app.dto.EnrollLectureResponse;
import com.project.app.service.LectureService;
import com.project.app.view.LectureDetailView;

public class LectureDetailController {

    private final LectureDetailView view;
    private final LectureService lectureService;
    private String currentUserId;  // 로그인된 사용자 ID
    private String lectureId;   // 현재 강의 ID

    public LectureDetailController(LectureDetailView view, LectureService lectureService,
                                   String userId, String lectureId) {
        this.view = view;
        this.lectureService = lectureService;
        this.currentUserId = userId;
        this.lectureId = lectureId;

        initListeners();
    }

    private void initListeners() {
        // 신청 버튼 리스너 등록
        view.addEnrollButtonListener(e -> handleEnrollLecture());
    }

    private void handleEnrollLecture() {
        System.out.println("[Controller] 신청 시작 - 사용자: " + currentUserId + ", 강의: " + lectureId);

        // EnrollLectureRequest 생성
        EnrollLectureRequest request = new EnrollLectureRequest(currentUserId, lectureId);

        // Service 호출
        EnrollLectureResponse response = lectureService.enrollLecture(request);

        // 결과 처리
        if (response.isSuccess()) {
            System.out.println("[Controller] 신청 성공");
            view.showSuccessDialog();
        } else {
            System.out.println("[Controller] 신청 실패: " + response.getMessage());
            view.showErrorDialog(response.getMessage());
        }
    }
}

