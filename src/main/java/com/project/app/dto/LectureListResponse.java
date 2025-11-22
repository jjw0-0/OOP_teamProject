package com.project.app.dto;

/**
 * 강의 목록 조회 결과 DTO
 *
 * 목적: 강의 목록 조회 결과
 *
 * 사용 시점: LectureService -> Controller -> LecturePageView
 *
 * 필드:
 *   - lectures (List<LectureCardView>)
 *   - totalCount (int)
 *
 * 이유: List를 감싸서 추가 정보(총 개수) 제공. 없어도 되지만 있으면 편리.
 */
public class LectureListResponse {

}
