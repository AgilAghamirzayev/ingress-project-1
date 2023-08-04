package com.ingress.authms.model.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class UserClientResponse {
    private Long id;
}
