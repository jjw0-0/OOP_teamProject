package com.project.app.dto;

/**
 * 리뷰 UI 표시용 DTO
 *
 * 목적: 리뷰 UI 표시용
 *
 * 사용 시점: ReviewService -> View (리뷰 목록 렌더링)
 *
 * 필드:
 *   - id (int)
 *   - userName (String)
 *   - userGrade (int) - 작성자 학년
 *   - rating (double)
 *   - content (String)
 *   - createdAt (String)
 *
 * 이유: Review 엔티티 + User 정보(이름, 학년) 조합. 여러 엔티티 정보 결합.
 */
public class ReviewView {

}
