package com.collins.Wallet.System.mapper;

import com.collins.Wallet.System.dtos.ResponseDtos.UserResponseDto;
import com.collins.Wallet.System.dtos.ResquestDto.CreateUserRequestDto;
import com.collins.Wallet.System.model.Users;

public class UserMapper {

    private UserMapper(){}

    public static Users mapToUser(CreateUserRequestDto createUserRequestDto){
        Users users = new Users();
        users.setFullName(createUserRequestDto.getFullName());
        users.setEmail(createUserRequestDto.getEmail());

        return users;
    }

    public static UserResponseDto mapToResponse(Users users){
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(users.getId());
        responseDto.setFullName(users.getFullName());
        responseDto.setEmail(users.getEmail());

        return responseDto;
    }
}
