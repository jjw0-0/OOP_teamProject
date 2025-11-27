package com.project.app.repository;
import com.project.app.model.User;

/**
 * UserRepository
 *
 * - User 엔티티에 대한 데이터 접근 인터페이스
 * - 구현체(UserRepositoryImpl)는 현재 인메모리 기반 임시 구현이며,
 *   추후 파일(users.txt) 또는 DB 연동으로 교체해야 함.
 */
public interface UserRepository {

    /**
     * ID로 사용자 조회
     *
     * @param id 사용자 ID
     * @return 존재하면 User, 없으면 null
     */
    User findById(String id);

    /**
     * 사용자 저장 (회원가입 등)
     *
     * @param user 저장할 사용자
     */
    void save(User user);

    /**
     * ID 중복 여부 확인
     *
     * @param id 사용자 ID
     * @return 이미 해당 ID가 존재하면 true
     */
    default boolean existsById(String id) {
        return findById(id) != null;
    }
}