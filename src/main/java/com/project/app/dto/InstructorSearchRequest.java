package com.project.app.dto;

/**
 * 강사 검색 요청 DTO
 *
 * 역할:
 * - View에서 Service로 강사 검색 조건을 전달
 * - 사용자의 검색/필터 입력을 담아 전달
 *
 * 사용 위치:
 * - InstructorsPageView의 검색 기능
 * - InstructorService의 검색 메서드 파라미터
 *
 * 포함 정보:
 * - 검색 키워드
 * - 과목 필터
 * - 학원 필터
 * - 정렬 순서
 */
public class InstructorSearchRequest {

    // ========== 필드 ==========

    /**
     * 검색 키워드
     * 강사명 또는 소개글에서 검색
     */
    private final String keyword;

    /**
     * 과목 필터
     * 특정 과목으로 필터링 (null이면 전체)
     */
    private final String subject;

    /**
     * 학원 필터
     * 특정 학원으로 필터링 (null이면 전체)
     */
    private final String academyId;

    /**
     * 정렬 순서
     * "reviewScore": 평점순
     * "name": 이름순
     */
    private final String sortOrder;

    // ========== 생성자 ==========

    /**
     * 기본 생성자 (검색 조건 없음)
     */
    public InstructorSearchRequest() {
        this(null, null, null, "reviewScore");
    }

    /**
     * 키워드만 받는 생성자
     *
     * @param keyword 검색 키워드
     */
    public InstructorSearchRequest(String keyword) {
        this(keyword, null, null, "reviewScore");
    }

    /**
     * 모든 필드를 받는 생성자
     *
     * @param keyword 검색 키워드
     * @param subject 과목 필터
     * @param academyId 학원 필터
     * @param sortOrder 정렬 순서
     */
    public InstructorSearchRequest(String keyword, String subject,
                                  String academyId, String sortOrder) {
        this.keyword = keyword;
        this.subject = subject;
        this.academyId = academyId;
        this.sortOrder = sortOrder != null ? sortOrder : "reviewScore";
    }

    // ========== Getter 메서드 ==========

    public String getKeyword() {
        return keyword;
    }

    public String getSubject() {
        return subject;
    }

    public String getAcademyId() {
        return academyId;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    /**
     * 검색 조건이 비어있는지 확인
     *
     * @return 모든 검색 조건이 null이면 true
     */
    public boolean isEmpty() {
        return (keyword == null || keyword.trim().isEmpty()) &&
               subject == null &&
               academyId == null;
    }

    @Override
    public String toString() {
        return "InstructorSearchRequest{" +
                "keyword='" + keyword + '\'' +
                ", subject='" + subject + '\'' +
                ", academyId='" + academyId + '\'' +
                ", sortOrder='" + sortOrder + '\'' +
                '}';
    }
}
