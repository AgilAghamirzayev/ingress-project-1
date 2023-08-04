package com.ingress.userms.service;

import com.ingress.userms.entity.UserEntity;
import com.ingress.userms.exception.ResourceNotFoundException;
import com.ingress.userms.model.client.UserClientResponse;
import com.ingress.userms.model.request.UserRequest;
import com.ingress.userms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.ingress.userms.mapper.UserMapper.mapEntityToEntity;
import static com.ingress.userms.model.constants.ExceptionConstants.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void saveUser(UserRequest userRequest) {
        UserEntity userEntity = mapEntityToEntity(userRequest);
        userRepository.save(userEntity);
    }

    public UserClientResponse getUserByUsername(String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException(USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        return UserClientResponse.of(userEntity.getId());
    }

}
