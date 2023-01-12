package com.nobrain.bookmarking.domain.user;

import com.nobrain.bookmarking.domain.user.dto.request.UserSignupRequest;
import com.nobrain.bookmarking.domain.user.exception.UserEmailDuplicationException;
import com.nobrain.bookmarking.domain.user.exception.UserLoginIdDuplicationException;
import com.nobrain.bookmarking.domain.user.exception.UserPhoneNumberDuplicationException;
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

    @Transactional
    public void signUp(UserSignupRequest dto) {
        validateUserDuplication(dto);
        userRepository.save(dto.encodePasswordToEntity(passwordEncoder));
    }

    private void validateUserDuplication(UserSignupRequest dto) {
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
}
