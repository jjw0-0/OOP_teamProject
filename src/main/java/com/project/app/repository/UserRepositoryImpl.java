package com.project.app.repository;
import com.project.app.model.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * UserRepository 구현체 (임시 인메모리 버전)
 *
 * - 현재는 애플리케이션 메모리에만 사용자 정보를 저장
 * - 추후 파일(users.txt) 또는 DB 연동으로 교체해야 함.
 *
 * TODO:
 *  - SignUpService 리팩토링 시, users.txt 대신 이 Repository를 사용하도록 변경
 *  - 파일/DB 연동 구현 후, 인메모리 store 제거 또는 테스트용으로만 사용
 */
public class UserRepositoryImpl implements UserRepository {

    /**
     * 인메모리 저장소
     * key: userId, value: User
     */
    private final Map<String, User> store = new ConcurrentHashMap<>();

    public UserRepositoryImpl() {
        // 필요하다면 여기에 테스트용 기본 계정 삽입 가능
    }

    @Override
    public User findById(String id) {
        if (id == null) {
            return null;
        }
        return store.get(id);
    }

    @Override
    public void save(User user) {
        if (user == null || user.getId() == null) {
            return;
        }
        store.put(user.getId(), user);
    }

    public boolean checkEnrolled(String userId, String lecId) {
        return true;
    }

    public void enrollLecture(String userId, String lecId) {
    }
}
