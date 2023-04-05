package com.nobrain.bookmarking.domain.user.service;

import com.nobrain.bookmarking.domain.auth.service.TokenService;
import com.nobrain.bookmarking.domain.user.dto.UserRequest;
import com.nobrain.bookmarking.domain.user.dto.UserResponse;
import com.nobrain.bookmarking.domain.user.entity.User;
import com.nobrain.bookmarking.domain.user.exception.*;
import com.nobrain.bookmarking.domain.user.repository.UserRepository;
import com.nobrain.bookmarking.global.security.Encryptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserSignService {

    private final UserRepository userRepository;
    private final Encryptor encryptor;
    private final TokenService tokenService;

    @Transactional
    public void signUp(UserRequest.SignUp dto) {
        validateUser(dto);
        dto.encodePassword(encryptor.encrypt(dto.getPassword()));
        userRepository.save(dto.toEntity());
    }

    public UserResponse.SignIn signIn(UserRequest.SignIn dto) {
        User user = userRepository.findByLoginId(dto.getLoginId()).orElseThrow(() -> new UserLoginIdNotFoundException(dto.getLoginId()));
        if (!encryptor.isMatch(dto.getPassword(), user.getPassword())) {
            throw new UserNotCorrectPasswordException(dto.getPassword());
        }

        String accessToken = tokenService.createToken(String.valueOf(user.getId()), user.getRoles());
        return UserResponse.SignIn.builder()
                .userId(user.getId())
                .username(user.getName())
                .email(user.getEmail())
                .accessToken(accessToken)
                .tokenType("Bearer")
                .build();
    }

    private void validateUser(UserRequest.SignUp dto) {
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

        if (!dto.getPassword().equals(dto.getPasswordCheck())) {
            throw new UserNotCorrectPasswordException(dto.getPassword());
        }
    }
}
