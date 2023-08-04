package com.ingress.userms.model.request;

import lombok.Data;

@Data
public class UserRequest {
    private String password;
    private String email;
}
