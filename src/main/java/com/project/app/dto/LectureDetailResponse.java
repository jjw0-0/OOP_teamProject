package com.project.app.dto;

/**
 * 강의 상세 정보 조회 결과 DTO
 *
 * 목적: 강의 상세 정보 조회 결과
 *
 * 사용 시점: LectureService -> Controller -> LectureDetailView
 *
 * 필드:
 *   - 기본 정보 (id, name, subject, academyName)
 *   - 강사/교재 정보
 *   - 가격, 정원, 현재 수강생 수
 *   - 시간/장소
 *   - 설명, 강의 계획
 *   - isEnrolled (boolean) - 현재 사용자가 신청했는지
 *   - canEnroll (boolean) - 신청 가능 여부
 *
 * 이유: Lecture 엔티티 + 사용자별 상태(신청 여부) 조합. Entity만으로 불가능.
 */
public class LectureDetailResponse {

}
