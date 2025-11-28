package com.project.app.service;

import com.project.app.dto.*;
import com.project.app.model.Instructor;
import com.project.app.model.Lecture;
import com.project.app.model.Review;
import com.project.app.model.Textbook;
import com.project.app.repository.InstructorRepository;
import com.project.app.repository.LectureRepository;
import com.project.app.repository.ReviewRepository;
import com.project.app.repository.TextbookRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 강사 관련 비즈니스 로직을 처리하는 서비스 클래스
 *
 * 기능:
 * - 강사 목록 조회 및 검색
 * - 강사 상세 정보 조회
 * - 강사 필터링 및 정렬
 */
public class InstructorService {

    private final InstructorRepository instructorRepository;
    private final LectureRepository lectureRepository;
    private final TextbookRepository textbookRepository;
    private final ReviewRepository reviewRepository;

    private static final double DEFAULT_REVIEW_SCORE = 0.0;
    private static final int DEFAULT_REVIEW_COUNT = 0;

    public InstructorService(InstructorRepository instructorRepository,
                            LectureRepository lectureRepository,
                            TextbookRepository textbookRepository,
                            ReviewRepository reviewRepository) {
        this.instructorRepository = instructorRepository;
        this.lectureRepository = lectureRepository;
        this.textbookRepository = textbookRepository;
        this.reviewRepository = reviewRepository;
    }

    // ========== Public Business Methods ==========

    /**
     * 강사 목록 검색
     *
     * @param request 검색 조건
     * @return 강사 목록 응답
     */
    public InstructorListResponse searchInstructors(InstructorSearchRequest request) {
        // 1. 모든 강사 조회
        List<Instructor> instructors = instructorRepository.findAll();

        // 2. 필터링
        instructors = filterInstructors(instructors, request);

        // 3. 정렬
        instructors = sortInstructors(instructors, request.getSortOrder());

        // 4. Entity를 DTO로 변환
        List<InstructorCardView> cardViews = instructors.stream()
                .map(instructor -> convertToCardView(instructor))
                .collect(Collectors.toList());

        return new InstructorListResponse(cardViews, cardViews.size());
    }

    /**
     * 강사 상세 정보 조회
     *
     * @param instructorId 강사 ID
     * @return 강사 상세 정보 응답
     */
    public InstructorDetailResponse getInstructorDetail(String instructorId) {
        // 1. 강사 조회
        Instructor instructor = instructorRepository.findById(instructorId);
        if (instructor == null) {
            return null;
        }

        // 2. 강의 목록 조회 및 강의별 평균 별점 계산
        List<String> lectureIds = instructor.getLectureIds();
        List<Lecture> lectures = lectureRepository.findByIds(lectureIds);
        List<InstructorDetailResponse.LectureSummary> lectureSummaries = lectures.stream()
                .map(lecture -> {
                    // 강의별 리뷰 평균 별점 계산
                    List<Review> lectureReviews = reviewRepository.findByLectureId(lecture.getLectureId());
                    double avgRating = calculateAverageRating(lectureReviews);
                    return new InstructorDetailResponse.LectureSummary(
                            lecture.getLectureId(),
                            lecture.getTitle(),
                            avgRating,
                            lecture.getLecturePrice()
                    );
                })
                .collect(Collectors.toList());

        // 3. 교재 정보 조회 (첫 번째 교재 ID 사용)
        InstructorDetailResponse.TextbookSummary textbookSummary = null;
        if (instructor.getTextbookId() != null && !instructor.getTextbookId().isEmpty()) {
            Textbook textbook = textbookRepository.findById(instructor.getTextbookId());
            if (textbook != null) {
                textbookSummary = new InstructorDetailResponse.TextbookSummary(
                        textbook.getId(),
                        textbook.getName(),
                        textbook.getPrice()
                );
            }
        }

        // 4. 수강생 수 계산
        int studentCount = instructor.getStudentIds().size();

        // 5. 강사 전체 리뷰 평균 별점 계산
        List<Review> instructorReviews = reviewRepository.findByInstructorId(instructorId);
        double avgReviewScore = calculateAverageRating(instructorReviews);
        int reviewCount = instructorReviews.size();

        // 6. DTO 생성
        return new InstructorDetailResponse(
                instructor.getId(),
                instructor.getName(),
                instructor.getIntroduction(),
                instructor.getProfileImagePath(),  // null일 수 있음
                instructor.getAcademyId(),
                null,  // Academy 이름은 패스
                instructor.getSubject(),
                avgReviewScore,
                reviewCount,
                lectureSummaries,
                textbookSummary,
                studentCount
        );
    }

    // ========== Private Helper Methods ==========

    /**
     * 강사 목록 필터링
     */
    private List<Instructor> filterInstructors(List<Instructor> instructors, InstructorSearchRequest request) {
        List<Instructor> filtered = new ArrayList<>(instructors);

        // 키워드 필터
        if (request.getKeyword() != null && !request.getKeyword().trim().isEmpty()) {
            String keyword = request.getKeyword().toLowerCase().trim();
            filtered = filtered.stream()
                    .filter(instructor ->
                            instructor.getName().toLowerCase().contains(keyword) ||
                            instructor.getIntroduction().toLowerCase().contains(keyword))
                    .collect(Collectors.toList());
        }

        // 과목 필터
        if (request.getSubject() != null && !request.getSubject().trim().isEmpty()) {
            filtered = filtered.stream()
                    .filter(instructor -> instructor.getSubject().equals(request.getSubject()))
                    .collect(Collectors.toList());
        }

        // 학원 필터
        if (request.getAcademyId() != null && !request.getAcademyId().trim().isEmpty()) {
            String academyId = request.getAcademyId().trim();
            filtered = filtered.stream()
                    .filter(instructor -> instructor.getAcademyId().equalsIgnoreCase(academyId))
                    .collect(Collectors.toList());
        }

        return filtered;
    }

    /**
     * 강사 목록 정렬
     */
    private List<Instructor> sortInstructors(List<Instructor> instructors, String sortOrder) {
        if (sortOrder == null || sortOrder.isEmpty()) {
            sortOrder = "reviewScore";
        }

        List<Instructor> sorted = new ArrayList<>(instructors);

        switch (sortOrder) {
            case "name":
                sorted.sort(Comparator.comparing(Instructor::getName));
                break;
            case "reviewScore":
            default:
                // 평점순은 리뷰가 없으므로 이름순으로 대체
                sorted.sort(Comparator.comparing(Instructor::getName));
                break;
        }

        return sorted;
    }

    /**
     * Instructor Entity를 InstructorCardView DTO로 변환
     */
    private InstructorCardView convertToCardView(Instructor instructor) {
        // 강사별 리뷰 평균 별점 계산
        List<Review> reviews = reviewRepository.findByInstructorId(instructor.getId());
        double avgRating = calculateAverageRating(reviews);
        
        return new InstructorCardView(
                instructor.getId(),
                instructor.getName(),
                instructor.getIntroduction(),
                instructor.getProfileImagePath(),  // null일 수 있음
                avgRating,
                instructor.getSubject(),
                instructor.getAcademyId()
        );
    }

    /**
     * 리뷰 목록의 평균 별점 계산
     */
    private double calculateAverageRating(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return DEFAULT_REVIEW_SCORE;
        }
        double sum = reviews.stream()
                .mapToDouble(Review::getRating)
                .sum();
        return sum / reviews.size();
    }

}

