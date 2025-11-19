package com.project.app.dto;

/**
 * 강의 신청 요청 DTO
 *
 * 목적: 강의 신청 요청 (결제 정보 포함)
 *
 * 사용 시점: LectureDetailView -> Controller -> LectureService
 *
 * 필드:
 *   - lectureId (int)
 *   - userId (String)
 *   - purchaseTextbook (boolean) - 교재 구매 여부
 *   - paymentMethod (String) - 결제 수단
 *
 * 이유: 신청 + 결제 정보 통합. 4개 매개변수를 DTO로 묶어 명확성 향상.
 */
public class EnrollLectureRequest {

}
