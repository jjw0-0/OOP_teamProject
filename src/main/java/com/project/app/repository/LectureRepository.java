package com.project.app.repository;

import com.project.app.model.Lecture;

import java.util.List;

/**
 * Lecture Data Access Interface
 *
 * 특징:
 * - 강의 정보 조회
 * - 강사별, 과목별 필터링 지원
 */
public interface LectureRepository {

    // ========== 조회 메서드 ==========

    /**
     * 강의 ID로 강의 조회
     */
    Lecture findById(String id);

    /**
     * 모든 강의 목록 조회
     */
    List<Lecture> findAll();

    /**
     * 강사명으로 강의 목록 조회
     */
    List<Lecture> findByInstructorName(String instructorName);

    /**
     * 강사 ID로 강의 목록 조회
     * (강사 ID는 강사명과 매칭)
     */
    List<Lecture> findByInstructorId(String instructorId);

    /**
     * 과목으로 강의 목록 조회
     */
    List<Lecture> findBySubject(String subject);

    /**
     * 강의 ID 목록으로 강의 목록 조회
     */
    List<Lecture> findByIds(List<String> lectureIds);
}
