package com.ingress.authms.factory;

import com.ingress.authms.security.model.AccessTokenClaimsSet;
import com.ingress.authms.security.model.RefreshTokenClaimsSet;

import java.time.LocalDateTime;
import java.util.Date;

import static com.ingress.authms.model.constants.AuthConstants.ISSUER;

public interface TokenFactory {
    static AccessTokenClaimsSet buildAccessTokenClaimsSet(Long userId, Date expirationTime) {
        return AccessTokenClaimsSet.builder()
                .iss(ISSUER)
                .userId(userId)
                .createdTime(new Date())
                .expirationTime(expirationTime)
                .build();
    }

    static RefreshTokenClaimsSet buildRefreshTokenClaimsSet(Long userId,
                                                            Integer refreshTokenExpirationCount,
                                                            Date expirationTime) {
        return RefreshTokenClaimsSet.builder()
                .iss(ISSUER)
                .userId(userId)
                .expirationTime(expirationTime)
                .count(refreshTokenExpirationCount)
                .build();
    }

}
