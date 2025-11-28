package com.project.app.repository;

import com.project.app.model.Review;

import java.util.List;

/**
 * Review Data Access Interface
 *
 * 특징:
 * - 리뷰 정보 조회
 * - 강사별, 강의별 필터링 지원
 */
public interface ReviewRepository {

    /**
     * 리뷰 ID로 리뷰 조회
     */
    Review findById(String id);

    /**
     * 모든 리뷰 목록 조회
     */
    List<Review> findAll();

    /**
     * 강사 ID로 리뷰 목록 조회
     */
    List<Review> findByInstructorId(String instructorId);

    /**
     * 강의 ID로 리뷰 목록 조회
     */
    List<Review> findByLectureId(String lectureId);

    /**
     * 강사 ID와 강의 ID로 리뷰 목록 조회
     */
    List<Review> findByInstructorIdAndLectureId(String instructorId, String lectureId);
}

