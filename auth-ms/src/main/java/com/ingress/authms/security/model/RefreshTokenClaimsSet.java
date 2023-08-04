package com.ingress.authms.security.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RefreshTokenClaimsSet {
    private Long userId;
    private Date expirationTime;
    private Integer count;
    private String iss;
}
