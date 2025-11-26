package com.project.app.repository;

import java.util.List;

import com.project.app.model.Lecture;

/**
 * LectureRepository
 *
 * - Lecture 엔티티에 대한 데이터 접근 인터페이스
 * - 현재는 인메모리 구현(LectureRepositoryImpl)을 사용
 * - 추후 DB/파일 연동으로 교체 가능
 */
public interface LectureRepository {

    Lecture findById(int id);

    List<Lecture> findAll();

    void save(Lecture lecture);
}
