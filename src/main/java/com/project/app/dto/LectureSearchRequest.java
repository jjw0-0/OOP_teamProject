package com.project.app.dto;

/**
 * 강의 검색 요청 DTO
 *
 * 목적: 강의 검색 조건
 *
 * 사용 시점: LecturePageView -> Controller -> LectureService
 *
 * 필드:
 *   - keyword (String) - 검색어
 *   - subject (String) - 과목 필터 (선택)
 *   - academyName (String) - 학원 필터 (선택)
 *   - targetGrade (Integer) - 학년 필터 (선택)
 *   - sortOrder (String) - 정렬 순서
 *
 * 이유: 여러 검색 조건을 조합. 선택적 필터가 많아 DTO로 관리해야 함.
 */
public class LectureSearchRequest {

}
