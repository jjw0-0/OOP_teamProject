package com.project.app.repository;

import com.project.app.model.Textbook;

import java.util.List;

/**
 * Textbook Data Access Interface
 *
 * 특징:
 * - 교재 정보 조회
 * - 강사별, 강의별 필터링 지원
 */
public interface TextbookRepository {

    // ========== 조회 메서드 ==========

    /**
     * 교재 ID로 교재 조회
     */
    Textbook findById(String id);

    /**
     * 모든 교재 목록 조회
     */
    List<Textbook> findAll();

    /**
     * 강사명으로 교재 목록 조회
     */
    List<Textbook> findByInstructorName(String instructorName);

    /**
     * 강의 ID로 교재 조회
     */
    Textbook findByLectureId(String lectureId);

    /**
     * 교재 ID 목록으로 교재 목록 조회
     */
    List<Textbook> findByIds(List<String> textbookIds);
}

