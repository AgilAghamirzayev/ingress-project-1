package com.ingress.authms.service;

import com.ingress.authms.client.UserClient;
import com.ingress.authms.model.client.UserClientResponse;
import com.ingress.authms.model.request.LoginRequest;
import com.ingress.authms.model.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    @Value("${jwt.refreshToken.expiration.count}")
    private Integer refreshTokenExpirationTime;

    private final TokenService tokenService;
    private final UserClient userClient;
    long count = 1L;

    public TokenResponse login(LoginRequest request) {
//        var user = userClient.getUserName(request.getEmail());
        var user = UserClientResponse.of(count++);
        return tokenService.generateToken(user.getId(), refreshTokenExpirationTime);
    }
}
