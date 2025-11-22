package com.project.app.dto;

/**
 * 강의 카드 UI 표시용 DTO
 *
 * 목적: 강의 카드 UI 표시용
 *
 * 사용 시점: LectureService -> View (UI 렌더링)
 *
 * 필드:
 *   - id (int)
 *   - name (String)
 *   - subject (String)
 *   - instructorName (String)
 *   - academyName (String)
 *   - thumbnailPath (String)
 *   - rating (double)
 *   - price (int)
 *   - dayOfWeek (String)
 *   - time (String)
 *   - remainingSeats (int)
 *
 * 이유: Lecture 엔티티의 모든 정보가 아닌 카드 표시에 필요한 정보만 선택. Entity와 분리 필수.
 */
public class LectureCardView {

}
