package com.ingress.userms.controller.internal;

import com.ingress.userms.model.client.UserClientResponse;
import com.ingress.userms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/v1/users")
public class UserControllerInternal {

    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    UserClientResponse getUser(@RequestParam String username) {
        return userService.getUserByUsername(username);
    }
}
