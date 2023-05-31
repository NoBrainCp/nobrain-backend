package com.nobrain.bookmarking.domain.user.service;

import com.nobrain.bookmarking.domain.user.dto.UserRequest;
import com.nobrain.bookmarking.domain.user.exception.UserEmailDuplicationException;
import com.nobrain.bookmarking.domain.user.exception.UserNotCorrectPasswordException;
import com.nobrain.bookmarking.domain.user.exception.UsernameDuplicationException;
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

    @Transactional
    public Long signUp(UserRequest.SignUp dto) {
        validateUser(dto);
        dto.encodePassword(encryptor.encrypt(dto.getPassword()));
        return userRepository.save(dto.toEntity()).getId();
    }

    private void validateUser(UserRequest.SignUp dto) {
        if (userRepository.existsByName(dto.getName())) {
            throw new UsernameDuplicationException(dto.getName());
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new UserEmailDuplicationException(dto.getEmail());
        }

        if (!dto.getPassword().equals(dto.getPasswordCheck())) {
            throw new UserNotCorrectPasswordException(dto.getPassword());
        }
    }
}
