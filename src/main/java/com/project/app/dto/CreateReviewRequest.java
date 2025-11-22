package com.project.app.dto;

/**
 * 리뷰 작성 요청 DTO
 *
 * 목적: 리뷰 작성 요청
 *
 * 사용 시점: LectureDetailView -> Controller -> ReviewService
 *
 * 필드:
 *   - userId (String)
 *   - targetType (String) - LECTURE or INSTRUCTOR
 *   - targetId (int)
 *   - rating (double)
 *   - content (String)
 *
 * 이유: 5개 필드를 전달. DTO 없으면 매개변수가 너무 많음.
 */
public class CreateReviewRequest {

}
