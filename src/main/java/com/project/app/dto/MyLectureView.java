package com.project.app.dto;

/**
 * 내 강의 카드 UI 표시용 DTO
 *
 * 목적: 내 강의 카드 UI 표시용
 *
 * 사용 시점: LectureService -> MyPageView
 *
 * 필드:
 *   - lectureId (int)
 *   - lectureName (String)
 *   - instructorName (String)
 *   - subject (String)
 *   - dayOfWeek (String)
 *   - time (String)
 *   - location (String)
 *   - nextScheduleDate (String) - 다음 수업 날짜 (계산된 값)
 *
 * 이유: Lecture 엔티티 + 계산된 값(다음 수업 날짜). LectureCardView와 다른 정보 구성.
 */
public class MyLectureView {

}
