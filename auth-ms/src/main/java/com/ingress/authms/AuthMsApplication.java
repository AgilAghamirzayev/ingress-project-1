package com.ingress.authms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class AuthMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthMsApplication.class, args);
    }

}
