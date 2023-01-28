package com.nobrain.bookmarking.domain.user.service;

import com.nobrain.bookmarking.domain.user.dto.UserRequest;
import com.nobrain.bookmarking.domain.user.entity.User;
import com.nobrain.bookmarking.domain.user.exception.*;
import com.nobrain.bookmarking.domain.user.repository.UserRepository;
import com.nobrain.bookmarking.global.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserSignService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Transactional
    public Long signUp(UserRequest.SignUp dto) {
        validateUserDuplication(dto);
        dto.encodePassword(passwordEncoder.encode(dto.getPassword()));
        return userRepository.save(dto.toEntity()).getId();
    }

    public String signIn(UserRequest.SignIn dto) {
        User user = userRepository.findByLoginId(dto.getLoginId())
                .orElseThrow(() -> new UserLoginIdNotFoundException(dto.getLoginId()));
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new UserNotCorrectPasswordException(dto.getPassword());
        }

        return tokenService.createToken(user.getUsername(), user.getRoles());
    }

    private void validateUserDuplication(UserRequest.SignUp dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new UserEmailDuplicationException(dto.getEmail());
        }

        if (userRepository.existsByLoginId(dto.getLoginId())) {
            throw new UserLoginIdDuplicationException(dto.getLoginId());
        }

        if (userRepository.existsByName(dto.getName())) {
            throw new UsernameDuplicationException(dto.getName());
        }

        if (userRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new UserPhoneNumberDuplicationException(dto.getPhoneNumber());
        }
    }
}
