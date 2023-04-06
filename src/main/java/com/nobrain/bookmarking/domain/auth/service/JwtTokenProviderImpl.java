package com.nobrain.bookmarking.domain.auth.service;

import com.nobrain.bookmarking.domain.auth.dto.UserPayload;
import com.nobrain.bookmarking.domain.auth.entity.RefreshToken;
import com.nobrain.bookmarking.domain.auth.exception.TokenExpiredException;
import com.nobrain.bookmarking.domain.auth.exception.TokenInvalidException;
import com.nobrain.bookmarking.domain.auth.repository.RefreshTokenRepository;
import com.nobrain.bookmarking.domain.user.entity.User;
import com.nobrain.bookmarking.domain.user.exception.UserNotFoundException;
import com.nobrain.bookmarking.domain.user.repository.UserRepository;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class JwtTokenProviderImpl implements JwtTokenProvider {

    private static final String USER_ID = "userId";
    private static final String USERNAME = "username";
    private static final String ROLES = "roles";

    private final SecretKey secretKey;
    private final String accessTokenSubject;
    private final long expirationTimeMilliseconds;

    private final JwtParser jwtParser;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtTokenProviderImpl(
            @Value("${security.jwt.secret-key}") String secretKey,
            @Value("${security.jwt.subject}") String accessTokenSubject,
            @Value("${security.jwt.expiration-time}") long expirationTimeMilliseconds,
            UserRepository userRepository,
            RefreshTokenRepository refreshTokenRepository) {

        this.secretKey = new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS256.getJcaName());
        this.jwtParser = Jwts.parserBuilder().setSigningKey(this.secretKey).build();
        this.accessTokenSubject = accessTokenSubject;
        this.expirationTimeMilliseconds = expirationTimeMilliseconds;
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public String generateAccessToken(String refreshTokenRequest) {
        RefreshToken refreshToken = refreshTokenRepository.findById(refreshTokenRequest)
                .orElseThrow(() -> new TokenInvalidException(refreshTokenRequest));

        User user = userRepository.findById(refreshToken.getUserId()).orElseThrow(UserNotFoundException::new);
        return generateToken(new UserPayload(user.getId(), user.getName(), user.getRoles()));
    }

    @Override
    public RefreshToken generateRefreshToken(Long userPk) {
        RefreshToken refreshToken = new RefreshToken(UUID.randomUUID().toString(), userPk);
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    @Override
    public String generateToken(UserPayload payload) {
        Date now = new Date();
        Date expirationTime = new Date(now.getTime() + expirationTimeMilliseconds);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(accessTokenSubject)
                .claim(USER_ID, payload.getUserId())
                .claim(USERNAME, payload.getUsername())
                .claim(ROLES, payload.getRoles())
                .setIssuedAt(now)
                .setExpiration(expirationTime)
                .signWith(secretKey)
                .compact();
    }

    @Override
    public void validateToken(String token) {
        try {
            jwtParser.parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException(token);
        } catch (JwtException e) {
            throw new TokenInvalidException(token);
        }
    }

    @Override
    public UserPayload getPayload(String token) {

        try {
            Claims body = jwtParser.parseClaimsJws(token).getBody();
            return UserPayload.builder()
                    .userId(body.get(USER_ID, Long.class))
                    .username(body.get(USERNAME, String.class))
                    .roles(body.get(ROLES, List.class))
                    .build();
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException(token);
        } catch (JwtException | NullPointerException | IllegalArgumentException e) {
            throw new TokenInvalidException(token);
        }
    }
}
