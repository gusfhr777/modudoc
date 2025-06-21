package com.piltong.modudoc.common.model;

import java.io.Serializable;

public class LoginRequestDto implements Serializable {

    private String id;
    private String password;

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public LoginRequestDto(String id, String password) {
        this.id = id;
        this.password = password;
    }


    @Override
    public String toString() {
        return "LoginRequestDto{" +
                "id='" + id + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
