package com.project.app.dto;

/**
 * 강의 신청 결과 응답 DTO
 *
 * 목적: 강의 신청 결과 (결제 ID 포함)
 *
 * 사용 시점: LectureService -> Controller -> LectureDetailView
 *
 * 필드:
 *   - success (boolean)
 *   - message (String)
 *   - errorType (ErrorType enum)
 *     - ALREADY_ENROLLED
 *     - LECTURE_FULL
 *     - TIME_CONFLICT
 *     - GRADE_MISMATCH
 *     - SYSTEM_ERROR
 *   - paymentId (int) - 성공 시
 *
 * 이유: 실패 원인이 다양함. 에러 타입별로 다른 안내 메시지 표시 필요.
 */
public class EnrollLectureResponse {
    private boolean success;
    private String message; // 실패 시 에러타입 표기

    EnrollLectureResponse(boolean success, String message){
        this.success=success;
        this.message=message;
    }

    // 신청 성공
    public static EnrollLectureResponse success(){
        return new EnrollLectureResponse(true, "강의 신청 성공");
    }

    // 신청 실패
    public static EnrollLectureResponse failure(String message){
        return new EnrollLectureResponse(false, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

}
