package com.project.app.dto;

/**
 * 강사 카드 UI 표시용 DTO
 *
 * 역할:
 * - 강사 목록 페이지에서 각 강사 카드에 표시할 최소 정보만 포함
 * - UI 렌더링에 최적화된 데이터 구조
 *
 * 사용 위치:
 * - InstructorsPageView의 강사 카드 생성 시
 * - 강사 검색 결과 표시 시
 *
 * 포함 정보:
 * - 강사 기본 정보 (ID, 이름, 소개)
 * - 프로필 이미지 경로
 * - 평점 정보
 * - 과목 정보
 */
public class InstructorCardView {

    // ========== 필드 ==========

    /**
     * 강사 ID
     */
    private final String id;

    /**
     * 강사명
     */
    private final String name;

    /**
     * 강사 소개 (요약)
     * 카드에 표시되는 짧은 소개글
     */
    private final String introduction;

    /**
     * 프로필 이미지 경로
     */
    private final String profileImagePath;

    /**
     * 평점
     * 강사의 평균 평점 (0.0 ~ 5.0)
     */
    private final double reviewScore;

    /**
     * 과목
     * 강사가 담당하는 주요 과목
     */
    private final String subject;

    /**
     * 학원
     * 강사가 속한 학원
     */
    private final String academy;

    // ========== 생성자 ==========

    /**
     * 모든 필드를 받는 생성자
     *
     * @param id 강사 ID
     * @param name 강사명
     * @param introduction 강사 소개 (요약)
     * @param profileImagePath 프로필 이미지 경로
     * @param reviewScore 평점
     * @param subject 과목
     */
    public InstructorCardView(String id, String name, String introduction,
                             String profileImagePath, double reviewScore,
                             String subject, String academy) {
        this.id = id;
        this.name = name;
        this.introduction = introduction;
        this.profileImagePath = profileImagePath;
        this.reviewScore = reviewScore;
        this.subject = subject;
        this.academy = academy;
    }

    // ========== Getter 메서드 ==========

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public double getReviewScore() {
        return reviewScore;
    }

    public String getSubject() {
        return subject;
    }
    public String getAcademy() {
        return academy;
    }

    @Override
    public String toString() {
        return "InstructorCardView{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", subject='" + subject + '\'' +
                ", academy='" + academy + '\'' +
                ", reviewScore=" + reviewScore +
                '}';
    }
}
