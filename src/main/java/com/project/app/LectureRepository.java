package com.project.app.repository;

import com.project.app.model.Lecture;

import java.util.List;

/**
 * LectureRepository
 *
 * - Lecture 엔티티에 대한 데이터 접근 인터페이스
 * - 현재는 파일 기반 구현체(LectureRepositoryImpl)를 사용
 * - 추후 DB, 다른 저장소로 교체 가능
 *
 * 주의:
 * - 기존 코드 호환을 위해 findById 메서드를 파라미터 타입으로 오버로드해서 사용
 *   * String  : "L001" 같은 강의 ID
 *   * int     : 1 같은 숫자 ID (결제/내 강의에서 사용)
 *   * List<String> : 강사 → 여러 강의 ID 조회(InstructorService)
 */
public interface LectureRepository {

    /**
     * 문자열 강의 ID (예: "L001")로 강의 조회
     */
    Lecture findById(String lectureId);

    /**
     * 숫자 강의 ID (예: 1)로 강의 조회
     */
    Lecture findById(int numericId);

    /**
     * 문자열 강의 ID 목록으로 여러 강의 조회
     */
    List<Lecture> findById(List<String> lectureIds);

    /**
     * 전체 강의 목록 조회
     */
    List<Lecture> findAll();

    /**
     * 강의 저장 또는 갱신
     */
    void save(Lecture lecture);
}
