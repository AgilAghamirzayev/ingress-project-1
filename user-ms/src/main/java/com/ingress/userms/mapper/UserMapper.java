package com.ingress.userms.mapper;

import com.ingress.userms.entity.UserEntity;
import com.ingress.userms.model.request.UserRequest;
import com.ingress.userms.model.response.UserResponse;

public class UserMapper {
    public static UserResponse mapEntityToResponse(UserEntity userEntity) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(userEntity.getId());
        userResponse.setEmail(userEntity.getEmail());
        userResponse.setPassword(userEntity.getPassword());
        return userResponse;
    }

    public static UserEntity mapEntityToEntity(UserRequest userRequest) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userRequest.getEmail());
        userEntity.setPassword(userEntity.getPassword());
        return userEntity;
    }
}
