package com.piltong.modudoc.client.model;

import com.piltong.modudoc.common.model.LoginRequestDto;


public class LoginRequestMapper {
    public static LoginRequestDto toDto(LoginRequest loginRequest) {
        return new LoginRequestDto(loginRequest.getId(), loginRequest.getPassword());
    }

    public static LoginRequest toEntity(LoginRequestDto loginRequestDto) {
        return new LoginRequest(loginRequestDto.getId(), loginRequestDto.getPassword());
    }
}
