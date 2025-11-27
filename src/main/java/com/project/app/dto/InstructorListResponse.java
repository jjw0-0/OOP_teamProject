package com.project.app.dto;

import java.util.List;

/**
 * 강사 목록 조회 결과 DTO
 *
 * 역할:
 * - Service에서 View로 강사 목록 조회 결과를 전달
 * - 검색/필터링된 강사 목록과 총 개수를 포함
 *
 * 사용 위치:
 * - InstructorsPageView (강사 목록 페이지)
 * - InstructorService의 목록 조회 메서드 반환값
 *
 * 포함 정보:
 * - 강사 카드 목록 (InstructorCardView 리스트)
 * - 총 강사 수
 */
public class InstructorListResponse {

    // ========== 필드 ==========

    /**
     * 강사 카드 목록
     * 목록 페이지에 표시할 강사 카드 데이터들
     */
    private final List<InstructorCardView> instructors;

    /**
     * 총 강사 수
     * 검색/필터 조건에 맞는 전체 강사 수
     */
    private final int totalCount;

    // ========== 생성자 ==========

    /**
     * 모든 필드를 받는 생성자
     *
     * @param instructors 강사 카드 목록
     * @param totalCount 총 강사 수
     */
    public InstructorListResponse(List<InstructorCardView> instructors, int totalCount) {
        this.instructors = instructors;
        this.totalCount = totalCount;
    }

    // ========== Getter 메서드 ==========

    public List<InstructorCardView> getInstructors() {
        return instructors;
    }

    public int getTotalCount() {
        return totalCount;
    }

    /**
     * 결과가 비어있는지 확인
     *
     * @return 강사 목록이 비어있으면 true
     */
    public boolean isEmpty() {
        return instructors == null || instructors.isEmpty();
    }

    @Override
    public String toString() {
        return "InstructorListResponse{" +
                "totalCount=" + totalCount +
                ", instructorCount=" + (instructors != null ? instructors.size() : 0) +
                '}';
    }
}
