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
 *   - targetGrade (String) - 학년 필터 (선택)
 *   - sortOrder (String) - 정렬 순서
 *
 * 이유: 여러 검색 조건을 조합. 선택적 필터가 많아 DTO로 관리해야 함.
 */
public class LectureSearchRequest {

    private String keyword;         // 검색어 (강의명, 강사명 등)
    private String subject;         // 과목 필터 (수학, 국어, 영어 등)
    private String academyName;     // 학원 필터 (메가스터디, 이투스 등)
    private String targetGrade;     // 학년 필터 (고1, 고2, 고3, N수)
    private String sortOrder;       // 정렬 순서 (rating, price, popularity 등)

    // 기본 생성자
    public LectureSearchRequest() {
        this.sortOrder = "rating"; // 기본값: 평점순
    }

    // 전체 필드 생성자
    public LectureSearchRequest(String keyword, String subject, String academyName,
                                String targetGrade, String sortOrder) {
        this.keyword = keyword;
        this.subject = subject;
        this.academyName = academyName;
        this.targetGrade = targetGrade;
        this.sortOrder = sortOrder != null ? sortOrder : "rating";
    }

    // Getter & Setter
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAcademyName() {
        return academyName;
    }

    public void setAcademyName(String academyName) {
        this.academyName = academyName;
    }

    public String getTargetGrade() {
        return targetGrade;
    }

    public void setTargetGrade(String targetGrade) {
        this.targetGrade = targetGrade;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * 검색 조건이 있는지 확인
     */
    public boolean hasSearchCondition() {
        return (keyword != null && !keyword.trim().isEmpty()) ||
                (subject != null && !subject.trim().isEmpty()) ||
                (academyName != null && !academyName.trim().isEmpty()) ||
                (targetGrade != null && !targetGrade.trim().isEmpty());
    }

    /**
     * 키워드 검색이 있는지 확인
     */
    public boolean hasKeyword() {
        return keyword != null && !keyword.trim().isEmpty();
    }

    @Override
    public String toString() {
        return String.format(
                "LectureSearchRequest{keyword='%s', subject='%s', academy='%s', " +
                        "grade='%s', sortOrder='%s'}",
                keyword, subject, academyName, targetGrade, sortOrder
        );
    }
}