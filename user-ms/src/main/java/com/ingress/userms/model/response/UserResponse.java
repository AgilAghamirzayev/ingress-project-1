package com.ingress.userms.model.response;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String password;
    private String email;
}
