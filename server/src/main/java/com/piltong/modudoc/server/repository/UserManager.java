package com.piltong.modudoc.server.repository;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class UserManager {
    private static final UserManager instance = new UserManager();
    private final Set<String> loggedInUsers = ConcurrentHashMap.newKeySet();

    private UserManager() {}

    public static UserManager getInstance() {
        return instance;
    }

    public void login(String userId) {
        loggedInUsers.add(userId);
    }

    public void logout(String userId) {
        loggedInUsers.remove(userId);
    }

    public boolean isLoggedIn(String userId) {
        return loggedInUsers.contains(userId);
    }
}
