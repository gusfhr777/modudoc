package com.piltong.modudoc.server.registry;

import com.piltong.modudoc.server.model.User;

import java.util.Collections;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;


/**
 * 협제 접속죽인 유저 목록을 관리하는 클래스이다.
 * Registry는 Repository와는 다르게 서버 메모리 내의 상태 관리용 객체이다. (DB에 저장하지 않는 repository라고 생각하면 된다.)
 */
public class AccessingUserRegistry {
    private final ConcurrentHashMap<Integer, Set<User>> accessingUserMap = new ConcurrentHashMap<>();

    // 문서에 접근중인 유저를 추가한다.
//    public void addUser(Integer docId, User user) {
//        // 파라미터 검사
//        if (docId == null | user == null) {
//            throw new IllegalArgumentException("document ID or user is null.");
//        }
//
//        if (!accessingUserMap.containsKey(docId)) accessingUserMap.put(docId, new HashSet<>()); // 접근인이 없으면 문서 생성
//
//        accessingUserMap.get(docId).add(user); // 유저 추가
//
//    }


    public void addUser(int documentId, User user) {
        accessingUserMap.computeIfAbsent(documentId, k -> new CopyOnWriteArraySet<>()).add(user);
    }

    // 문서에 접근중인 유저를 제거한다.
    public void removeUser(int documentId, User user) {
        Set<User> users = accessingUserMap.get(documentId);
        if (users != null) {
            users.remove(user);
            if (users.isEmpty()) {
                accessingUserMap.remove(documentId);
            }
        }
    }

    // 문서에 접속중인 유저 목록을 리턴한다.

    public Set<User> getUsers(int documentId) {
        return accessingUserMap.getOrDefault(documentId, Collections.emptySet());
    }

    public int getUserCount(int documentId) {
        return getUsers(documentId).size();
    }

    public void clearAll() {
        accessingUserMap.clear();
    }

    @Override
    public String toString() {
        return accessingUserMap.toString();
    }
}
