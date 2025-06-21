package com.piltong.modudoc.server.repository;

import com.piltong.modudoc.server.model.User;
import java.util.*;

public class MapUserRepository implements UserRepository {
    // Map을 사용. userId를 키값으로 가진다.
    private final Map<String, User> userStorage = new HashMap<>();

    // DB 또는 파일 시스템에 저장
    public synchronized void save(User user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("저장할 유저나 유저 ID를 찾을 수 없음");
        }
        userStorage.put(user.getId(), user);
    }

    // 저장소에서 유저 불러오기
    public synchronized Optional<User> findById(String userId) {
        Optional<User> user = Optional.ofNullable(userStorage.get(userId));
        if (user == null) {
            throw new IllegalArgumentException("해당 ID의 유저를 찾을 수 없음");
        }
        return user;
    }

    // 유저 삭제
    public synchronized boolean delete(String userId) {
        userStorage.remove(userId);
        return true;
    }

    // 저장된 모든 유저를 리스트로 반환
    public synchronized List<User> findAll() {
        return new ArrayList<>(userStorage.values());
    }
}