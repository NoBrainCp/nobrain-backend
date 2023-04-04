package com.nobrain.bookmarking.domain.auth.service;

import com.nobrain.bookmarking.domain.auth.dto.UserPayload;
import com.nobrain.bookmarking.domain.auth.entity.RefreshToken;
import com.nobrain.bookmarking.domain.auth.exception.TokenInvalidException;
import com.nobrain.bookmarking.domain.auth.repository.RefreshTokenRepository;
import com.nobrain.bookmarking.domain.auth.util.JwtTokenExtractor;
import com.nobrain.bookmarking.domain.user.entity.User;
import com.nobrain.bookmarking.domain.user.exception.UserNotFoundException;
import com.nobrain.bookmarking.domain.user.repository.UserRepository;
import io.jsonwebtoken.*;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class JwtTokenProviderImpl implements JwtTokenProvider {

    private static final String TOKEN_TYPE = "Bearer";
    private static final String USER_ID = "userId";
    private static final String USERNAME = "username";
    private static final String ROLES = "roles";

    private final SecretKey secretKey;
    private final String accessTokenSubject;
    private final long expirationTimeMilliseconds;

    private final JwtTokenExtractor tokenExtractor;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtTokenProviderImpl(
            @Value("${security.jwt.secret-key}") String securityKey,
            @Value("${security.jwt.subject}") String accessTokenSubject,
            @Value("${security.jwt.expiration-time}") long expirationTimeMilliseconds,
            JwtTokenExtractor tokenExtractor,
            UserRepository userRepository,
            RefreshTokenRepository refreshTokenRepository) {

        this.secretKey = new SecretKeySpec(securityKey.getBytes(), SignatureAlgorithm.HS256.getJcaName());
        this.accessTokenSubject = accessTokenSubject;
        this.expirationTimeMilliseconds = expirationTimeMilliseconds;
        this.tokenExtractor = tokenExtractor;
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
    public String resolveToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        return tokenExtractor.extract(authorizationHeader, TOKEN_TYPE);
    }

    @Override
    public String generateToken(UserPayload payload) {
        Date now = new Date();
        Date expirationTime = new Date(now.getTime() + expirationTimeMilliseconds);

        return Jwts.builder()
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
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = getClaimsJws(token);
            return isAccessToken(claims) && isNotExpired(claims);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public UserPayload getPayload(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = tokenExtractor.extract(header, TOKEN_TYPE);
        Claims body = getClaimsJws(token).getBody();

        try {
            Long userId = body.get(USER_ID, Long.class);
            String username = body.get(USERNAME, String.class);
            List<String> roles = (List<String>) body.get(ROLES, List.class);
            return new UserPayload(userId, username, roles);
        } catch (RequiredTypeException | NullPointerException | IllegalArgumentException e) {
            throw new TokenInvalidException(token);
        }
    }

    private boolean isAccessToken(Jws<Claims> claims) {
        return claims.getBody()
                .getSubject()
                .equals(accessTokenSubject);
    }

    private boolean isNotExpired(Jws<Claims> claims) {
        return claims.getBody()
                .getExpiration()
                .after(new Date());
    }

    private Jws<Claims> getClaimsJws(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
    }
}
