package com.project.app.dto;

/**
 * 강의 신청 요청 DTO
 *
 * 목적: 강의 신청 시 필요한 데이터 전달
 *
 * 사용 시점: Controller -> Service
 *
 * 필드:
 *   - userId (String) - 신청자 ID
 *   - lectureId (String) - 신청할 강의 ID
 */
public class EnrollLectureRequest {
    private String userId;
    private String lectureId;

    // 생성자
    public EnrollLectureRequest(String userId, String lectureId) {
        this.userId = userId;
        this.lectureId = lectureId;
    }

    // Getter
    public String getUserId() {
        return userId;
    }

    public String getLectureId() {
        return lectureId;
    }

    // Setter
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setLectureId(String lectureId) {
        this.lectureId = lectureId;
    }

    @Override
    public String toString() {
        return String.format("EnrollLectureRequest{userId='%s', lectureId='%s'}", userId, lectureId);
    }
}