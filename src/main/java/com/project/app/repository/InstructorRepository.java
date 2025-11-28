package com.project.app.repository;

import com.project.app.model.Instructor;

import java.util.List;

/**
 * Instructor Data Access Interface
 *
 * 특징:
 * - 강사 정보는 고정 (추가/삭제 없음)
 * - 변경 가능한 데이터: 수강 학생 목록
 * - 주요 기능: 조회, 학생 관리
 */
public interface InstructorRepository {

    // ========== 조회 메서드 ==========

    /**
     * 강사 ID로 강사 조회
     */
    Instructor findById(String id);

    /**
     * 모든 강사 목록 조회
     */
    List<Instructor> findAll();
    List<Instructor> getAllInstructors();

    /**
     * 학원 ID로 소속 강사 목록 조회
     */
    List<Instructor> findByAcademyId(String academyId);

    /**
     * 이름으로 강사 검색 (부분 일치)
     */
    List<Instructor> findByName(String name);

    // ========== 학생 관리 메서드 ==========

    /**
     * 강사의 수강 학생 추가
     *
     * @param instructorId 강사 ID
     * @param studentId 학생 ID
     * @return 추가 성공 여부
     */
    boolean addStudentToInstructor(String instructorId, String studentId);

    /**
     * 강사의 모든 수강 학생 ID 조회
     *
     * @param instructorId 강사 ID
     * @return 학생 ID 목록
     */
    List<String> getStudentIdsByInstructor(String instructorId);

}
