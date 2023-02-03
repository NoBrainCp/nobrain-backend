package com.nobrain.bookmarking.domain.auth.service;

import com.nobrain.bookmarking.domain.auth.dto.AccessTokenRequest;
import com.nobrain.bookmarking.domain.auth.dto.RefreshTokenRequest;
import com.nobrain.bookmarking.domain.auth.entity.RefreshToken;
import com.nobrain.bookmarking.domain.auth.exception.InvalidRefreshTokenException;
import com.nobrain.bookmarking.domain.auth.repository.RefreshTokenRepository;
import com.nobrain.bookmarking.domain.user.entity.User;
import com.nobrain.bookmarking.domain.user.exception.UserNotFoundException;
import com.nobrain.bookmarking.domain.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TokenService {

    @Value("${spring.jwt.secret}")
    private String secretStringKey;
    private static final int ACCESS_TOKEN_EXPIRES = 30 * 60 * 1000;

    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;
    private SecretKey secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Keys.hmacShaKeyFor(secretStringKey.getBytes(StandardCharsets.UTF_8));
    }

    public RefreshToken generateRefreshToken(RefreshTokenRequest request) {
        Long userId = userRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new UserNotFoundException(request.getLoginId())).getId();

        /**
         * --- 회원 인증 추가 로직 ----
         */

        RefreshToken refreshToken = new RefreshToken(UUID.randomUUID().toString(), userId);
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public String generateAccessToken(AccessTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findById(request.getRefreshToken())
                .orElseThrow(() -> new InvalidRefreshTokenException(request.getRefreshToken()));
        Long userId = refreshToken.getUserId();
        Date now = new Date();
        Date expiration = new Date(now.getTime() + ACCESS_TOKEN_EXPIRES);

        return Jwts.builder()
                .signWith(secretKey)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .setSubject(String.valueOf(userId))
                .compact();
    }

    // JWT Token 생성
    public String createToken(String userPk, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put("roles", roles);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRES))
                .signWith(secretKey)
                .compact();
    }

    // JWT Token 으로 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getId(String token) {
//        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject(); // deprecation
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
    }

    public Long getId() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getId();
    }

    // Request 의 Header 에서 Token 값을 가져옴. "Authorization" : "Token value"
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    // JWT Token 의 유효성 + 만료일자 확인
    public boolean validateToken(String token) {
        try {
//            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token); // deprecation
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
