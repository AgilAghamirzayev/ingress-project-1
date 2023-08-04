package com.ingress.authms.client;

import com.ingress.authms.model.client.UserClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ms-user", url = "http://localhost:8082")
public interface UserClient {
    @GetMapping("internal/v1/users")
    UserClientResponse getUserName(@RequestParam String username);
}
