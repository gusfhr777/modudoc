package com.piltong.modudoc.client.model;

public class LoginRequest {


    private String id; // 유저 아이디
    private String password; // 유저 비밀번호



    public LoginRequest(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter 및 Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
