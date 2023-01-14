package com.nobrain.bookmarking.domain.user.service;

import com.nobrain.bookmarking.domain.user.dto.request.UserSignInRequest;
import com.nobrain.bookmarking.domain.user.dto.request.UserSignUpRequest;
import com.nobrain.bookmarking.domain.user.entity.User;
import com.nobrain.bookmarking.domain.user.exception.*;
import com.nobrain.bookmarking.domain.user.repository.UserRepository;
import com.nobrain.bookmarking.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void signUp(UserSignUpRequest dto) {
        validateUserDuplication(dto);
        dto.encodePassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(dto.encodePasswordToEntity());
    }

    public String signIn(UserSignInRequest dto) {
        User user = userRepository.findByLoginId(dto.getLoginId()).orElseThrow(() -> new UserLoginIdNotFoundException(dto.getLoginId()));
        if (isNotSamePassword(dto.getPassword(), user.getPassword())) {
            throw new UserNotCorrectPasswordException(dto.getPassword());
        }

        return jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
    }

    private void validateUserDuplication(UserSignUpRequest dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new UserEmailDuplicationException(dto.getEmail());
        }

        if (userRepository.existsByLoginId(dto.getLoginId())) {
            throw new UserLoginIdDuplicationException(dto.getLoginId());
        }

        if (userRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new UserPhoneNumberDuplicationException(dto.getPhoneNumber());
        }
    }

    private boolean isNotSamePassword(String rawPassword, String encodedPassword) {
        return !passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
