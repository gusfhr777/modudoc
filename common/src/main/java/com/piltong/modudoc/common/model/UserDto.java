package com.piltong.modudoc.common.model;

import java.io.Serializable;

public class UserDto implements Serializable {

    private String id; // 유저 아이디.

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    private String username; // 유저 이름
    private String password; // 유저 비밀번호


    public UserDto(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
