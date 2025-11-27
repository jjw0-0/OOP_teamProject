package com.project.app.repository;

import com.project.app.model.User;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepositoryImpl implements UserRepository {

    private final Map<String, User> store = new ConcurrentHashMap<>();

    public UserRepositoryImpl() {
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

    // 내가 추가한 메서드들
    public boolean checkEnrolled(String userId, String lecId) {
        return true;
    }

    public void enrollLecture(String userId, String lecId) {
    }
}