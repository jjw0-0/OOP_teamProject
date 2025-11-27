package com.project.app.dto;

import java.util.List;

/**
 * 강사 상세 정보 조회 결과 DTO
 *
 * 역할:
 * - Service에서 View로 강사의 상세 정보를 전달
 * - 강사 팝업 또는 상세 페이지에 표시할 모든 정보를 포함
 *
 * 사용 위치:
 * - InstructorDetailPopup (강사 상세 팝업)
 * - InstructorService의 조회 메서드 반환값
 *
 * 포함 정보:
 * - 강사 기본 정보 (ID, 이름, 소개, 프로필 이미지)
 * - 소속 학원 정보
 * - 평점 및 리뷰 정보
 * - 강의 목록
 * - 교재 목록
 * - 수강생 수
 */
public class InstructorDetailResponse {

    // ========== 기본 정보 ==========

    /**
     * 강사 ID
     */
    private final String id;

    /**
     * 강사명
     */
    private final String name;

    /**
     * 강사 소개 (전체)
     */
    private final String introduction;

    /**
     * 프로필 이미지 경로
     */
    private final String profileImagePath;

    /**
     * 학원 ID
     */
    private final String academyId;

    /**
     * 학원 이름
     */
    private final String academyName;

    /**
     * 과목
     */
    private final String subject;

    // ========== 평가 정보 ==========

    /**
     * 평점 (리뷰 점수)
     * 강사의 평균 평점 (0.0 ~ 5.0)
     */
    private final double reviewScore;

    /**
     * 리뷰 개수
     */
    private final int reviewCount;

    // ========== 관련 정보 ==========

    /**
     * 강의 목록
     * 강사가 진행하는 강의들의 간단한 정보
     */
    private final List<LectureSummary> lectures;

    /**
     * 교재 정보
     * 강사가 집필한 교재의 간단한 정보 (없을 경우 null)
     */
    private final TextbookSummary textbook;

    /**
     * 수강생 수
     * 강사의 강의를 듣는 총 학생 수
     */
    private final int studentCount;

    // ========== 생성자 ==========

    /**
     * 모든 필드를 받는 생성자
     *
     * @param id 강사 ID
     * @param name 강사명
     * @param introduction 강사 소개
     * @param profileImagePath 프로필 이미지 경로
     * @param academyId 학원 ID
     * @param academyName 학원 이름
     * @param subject 과목
     * @param reviewScore 평점 (리뷰 점수)
     * @param reviewCount 리뷰 개수
     * @param lectures 강의 목록
     * @param textbook 교재 정보
     * @param studentCount 수강생 수
     */
    public InstructorDetailResponse(String id, String name, String introduction,
                                   String profileImagePath, String academyId,
                                   String academyName, String subject,
                                   double reviewScore, int reviewCount,
                                   List<LectureSummary> lectures,
                                   TextbookSummary textbook,
                                   int studentCount) {
        this.id = id;
        this.name = name;
        this.introduction = introduction;
        this.profileImagePath = profileImagePath;
        this.academyId = academyId;
        this.academyName = academyName;
        this.subject = subject;
        this.reviewScore = reviewScore;
        this.reviewCount = reviewCount;
        this.lectures = lectures;
        this.textbook = textbook;
        this.studentCount = studentCount;
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

    public String getAcademyId() {
        return academyId;
    }

    public String getAcademyName() {
        return academyName;
    }

    public String getSubject() {
        return subject;
    }

    public double getReviewScore() {
        return reviewScore;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public List<LectureSummary> getLectures() {
        return lectures;
    }

    public TextbookSummary getTextbook() {
        return textbook;
    }

    public int getStudentCount() {
        return studentCount;
    }

    @Override
    public String toString() {
        return "InstructorDetailResponse{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", academyName='" + academyName + '\'' +
                ", subject='" + subject + '\'' +
                ", reviewScore=" + reviewScore +
                ", lectureCount=" + (lectures != null ? lectures.size() : 0) +
                ", studentCount=" + studentCount +
                '}';
    }

    // ========== 내부 클래스: 강의 요약 정보 ==========

    /**
     * 강의 요약 정보
     * 강사 상세 정보에서 강의 목록을 표시할 때 사용
     */
    public static class LectureSummary {
        private final String id;
        private final String name;
        private final double reviewScore;
        private final int price;

        public LectureSummary(String id, String name, double reviewScore, int price) {
            this.id = id;
            this.name = name;
            this.reviewScore = reviewScore;
            this.price = price;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public double getReviewScore() {
            return reviewScore;
        }

        public int getPrice() {
            return price;
        }
    }

    // ========== 내부 클래스: 교재 요약 정보 ==========

    /**
     * 교재 요약 정보
     * 강사 상세 정보에서 교재 목록을 표시할 때 사용
     */
    public static class TextbookSummary {
        private final String id;
        private final String name;
        private final int price;

        public TextbookSummary(String id, String name, int price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getPrice() {
            return price;
        }
    }
}
