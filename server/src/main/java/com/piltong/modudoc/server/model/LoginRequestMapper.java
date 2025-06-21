package com.piltong.modudoc.server.model;

import com.piltong.modudoc.common.model.LoginRequestDto;


public class LoginRequestMapper {
    public static LoginRequestDto toDto(LoginRequestDto loginRequestDto) {
        return new LoginRequestDto(loginRequestDto.getId(), loginRequestDto.getPassword());
    }

    public static LoginRequest toEntity(LoginRequestDto loginRequestDto) {
        return new LoginRequest(loginRequestDto.getId(), loginRequestDto.getPassword());
    }
}
