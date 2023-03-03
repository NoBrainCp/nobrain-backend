package com.nobrain.bookmarking.domain.user.service;

import com.nobrain.bookmarking.domain.auth.service.TokenService;
import com.nobrain.bookmarking.domain.user.dto.UserRequest;
import com.nobrain.bookmarking.domain.user.dto.UserResponse;
import com.nobrain.bookmarking.domain.user.entity.User;
import com.nobrain.bookmarking.domain.user.exception.UserNotCorrectPasswordException;
import com.nobrain.bookmarking.domain.user.exception.UserNotFoundException;
import com.nobrain.bookmarking.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public UserResponse.Profile getMyProfile() {
        Long id = tokenService.getId();
        User user = findById(id);

        return UserResponse.Profile.builder()
                .userId(id)
                .loginId(user.getLoginId())
                .email(user.getEmail())
                .username(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .birthDate(user.getBirthDate())
                .roles(user.getRoles())
                .build();
    }

    @Transactional
    public Long changeName(Long id, UserRequest.ChangeUserName dto) {
        findById(id).changeName(dto.getUsername());
        return id;
    }

    @Transactional
    public void changePassword(UserRequest.ChangePassword dto) {
        if (!dto.getPassword().equals(dto.getPasswordCheck())) {
            throw new UserNotCorrectPasswordException(dto.getPassword());
        }

        User user = findByLoginId(dto.getLoginId());
        user.changePassword(passwordEncoder.encode(dto.getPassword()));
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    private User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(String.valueOf(id)));
    }

    private User findByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId).orElseThrow(() -> new UserNotFoundException(loginId));
    }
}
