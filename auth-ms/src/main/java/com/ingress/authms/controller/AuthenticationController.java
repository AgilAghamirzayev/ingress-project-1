package com.ingress.authms.controller;

import com.ingress.authms.model.request.LoginRequest;
import com.ingress.authms.model.response.TokenResponse;
import com.ingress.authms.service.AuthenticationService;
import com.ingress.authms.service.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/auths")
public class AuthenticationController {

    private final TokenService tokenService;
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @ResponseStatus(code = HttpStatus.OK)
    public TokenResponse login(@RequestBody @Valid LoginRequest request) {
        return authenticationService.login(request);
    }

    @PostMapping("/validate")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Void> activateAccount(String accessToken) {
        Long id = tokenService.validateToken(accessToken).getId();
        return ResponseEntity.ok()
                .header("user-id", String.valueOf(id))
                .build();
    }

    @PostMapping("/refresh")
    public TokenResponse refreshToken(String refreshToken) {
        return tokenService.refreshTokens(refreshToken);
    }

}
