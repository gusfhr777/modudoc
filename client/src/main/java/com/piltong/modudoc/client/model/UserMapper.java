package com.piltong.modudoc.client.model;

import com.piltong.modudoc.common.model.UserDto;


public class UserMapper {
    public static UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getPassword());
    }

    public static User toEntity(UserDto userDto) {
        return new User(userDto.getId(), userDto.getUsername(), userDto.getPassword());
    }

}
