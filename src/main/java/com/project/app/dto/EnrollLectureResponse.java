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

}
