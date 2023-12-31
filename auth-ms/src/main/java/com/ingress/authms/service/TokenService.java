package com.ingress.authms.service;

import com.ingress.authms.cache.CacheUtil;
import com.ingress.authms.exception.AuthException;
import com.ingress.authms.model.client.UserClientResponse;
import com.ingress.authms.model.response.TokenResponse;
import com.ingress.authms.security.jwt.JwtUtil;
import com.ingress.authms.security.model.AuthCacheData;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.ingress.authms.factory.TokenFactory.buildAccessTokenClaimsSet;
import static com.ingress.authms.factory.TokenFactory.buildRefreshTokenClaimsSet;
import static com.ingress.authms.model.constants.AuthConstants.AUTH_CACHE_DATA_PREFIX;
import static com.ingress.authms.model.constants.AuthConstants.TOKEN_EXPIRE_DAY_COUNT;
import static com.ingress.authms.model.constants.ExceptionConstants.REFRESH_TOKEN_COUNT_EXPIRED;
import static com.ingress.authms.model.constants.ExceptionConstants.REFRESH_TOKEN_EXPIRED;
import static com.ingress.authms.model.constants.ExceptionConstants.TOKEN_EXPIRED;
import static com.ingress.authms.model.constants.ExceptionConstants.USER_UNAUTHORIZED;
import static java.util.concurrent.TimeUnit.DAYS;

@Log4j2
@Service
@RequiredArgsConstructor
public class TokenService {

    @Value("${jwt.accessToken.expiration.time}")
    private Integer accessTokenExpirationTime;

    @Value("${jwt.refreshToken.expiration.time}")
    private Integer refreshTokenExpirationTime;  // 15 DAYS

    private final JwtUtil jwtUtil;
    private final CacheUtil<AuthCacheData> cacheUtil;

    public TokenResponse generateToken(Long userId, Integer refreshTokenExpCount) {
        var accessExpirationTime = jwtUtil.generateSessionExpirationTime(accessTokenExpirationTime);
        var refreshExpirationTime = jwtUtil.generateSessionExpirationTime(refreshTokenExpirationTime);

        var accessTokenClaimsSet = buildAccessTokenClaimsSet(userId, accessExpirationTime);
        var refreshTokenClaimsSet = buildRefreshTokenClaimsSet(userId, refreshTokenExpCount, refreshExpirationTime);

        var keyPair = jwtUtil.generateKeyPair();
        var authCacheData = AuthCacheData.of(accessTokenClaimsSet, jwtUtil.encodePublicKey(keyPair));

        cacheUtil.saveToCache(AUTH_CACHE_DATA_PREFIX + userId, authCacheData, TOKEN_EXPIRE_DAY_COUNT, DAYS);

        var accessToken = jwtUtil.generateToken(accessTokenClaimsSet, keyPair.getPrivate());
        var refreshToken = jwtUtil.generateToken(refreshTokenClaimsSet, keyPair.getPrivate());

        return TokenResponse.of(accessToken, refreshToken);
    }

    public TokenResponse refreshTokens(String refreshToken) {
        var refreshTokenClaimsSet = jwtUtil.getClaimsFromRefreshToken(refreshToken);
        var userId = refreshTokenClaimsSet.getUserId();

        AuthCacheData authCacheData = cacheUtil.getBucket(AUTH_CACHE_DATA_PREFIX + userId);

        if (authCacheData == null) {
            throw new AuthException(USER_UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }

        jwtUtil.verifyToken(refreshToken, jwtUtil.decodePublicKey(authCacheData.getPublicKey()));

        if (jwtUtil.isRefreshTokenTimeExpired(refreshTokenClaimsSet)) {
            throw new AuthException(REFRESH_TOKEN_EXPIRED, HttpStatus.NOT_ACCEPTABLE);
        }

        if (jwtUtil.isRefreshTokenCountExpired(refreshTokenClaimsSet)) {
            throw new AuthException(REFRESH_TOKEN_COUNT_EXPIRED, HttpStatus.NOT_ACCEPTABLE);
        }

        return generateToken(userId, refreshTokenClaimsSet.getCount() - 1);
    }

    public UserClientResponse validateToken(String accessToken) {
        var userId = jwtUtil.getClaimsFromAccessToken(accessToken).getUserId();
        AuthCacheData authCacheData = cacheUtil.getBucket(AUTH_CACHE_DATA_PREFIX + userId);

        if (authCacheData == null) {
            throw new AuthException(TOKEN_EXPIRED, HttpStatus.NOT_ACCEPTABLE);
        }

        jwtUtil.verifyToken(accessToken, jwtUtil.decodePublicKey(authCacheData.getPublicKey()));

        if (jwtUtil.isTokenExpired(authCacheData.getAccessTokenClaimsSet().getExpirationTime())) {
            throw new AuthException(TOKEN_EXPIRED, HttpStatus.NOT_ACCEPTABLE);
        }

        return UserClientResponse.of(userId);
    }
}
