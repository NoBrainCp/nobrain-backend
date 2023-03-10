package com.nobrain.bookmarking.global.security;

import com.nobrain.bookmarking.domain.auth.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final TokenService tokenService;

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

//    @Bean
//    protected SecurityFilterChain corsFilterChain(HttpSecurity http) throws Exception {
//        http
//                .cors().configurationSource(request -> {
//                    var cors = new CorsConfiguration();
//                    cors.setAllowedOrigins(List.of("http://localhost:5173"));
//                    cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//                    cors.setAllowedHeaders(List.of("*"));
//                    return cors;
//                });
//
//        return http.build();
//    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .httpBasic().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // ?????? ?????? ??????.
            .and()
                .authorizeHttpRequests()
                .antMatchers("/*/signin", "/*/signin/**/" , "/*/signup", "/*/signup/**" , "/social/**").permitAll()
//                .anyRequest().hasRole("USER")
            .and()
            .addFilterBefore(new JwtAuthenticationFilter(tokenService),
                    UsernamePasswordAuthenticationFilter.class); // JwtAuthenticationFilter??? UsernamePasswordAuthenticationFilter ?????? ?????????.

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers(
                "/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html",
                "/webjars/**", "/swagger/**");
    }
}
