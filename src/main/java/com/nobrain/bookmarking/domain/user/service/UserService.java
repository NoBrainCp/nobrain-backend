package com.nobrain.bookmarking.domain.user.service;

import com.nobrain.bookmarking.domain.auth.service.TokenService;
import com.nobrain.bookmarking.domain.user.dto.UserRequest;
import com.nobrain.bookmarking.domain.user.dto.UserResponse;
import com.nobrain.bookmarking.domain.user.entity.User;
import com.nobrain.bookmarking.domain.user.exception.UserNotCorrectPasswordException;
import com.nobrain.bookmarking.domain.user.exception.UserNotFoundException;
import com.nobrain.bookmarking.domain.user.exception.UsernameDuplicationException;
import com.nobrain.bookmarking.domain.user.repository.UserRepository;
import com.nobrain.bookmarking.domain.user.dto.projection.UserInfo;
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
        Long userId = tokenService.getId();
        User user = findById(userId);

        return UserResponse.Profile.builder()
                .userId(userId)
                .loginId(user.getLoginId())
                .email(user.getEmail())
                .username(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .birthDate(user.getBirthDate())
                .roles(user.getRoles())
                .build();
    }

    public UserResponse.Info getUserInfo(String username) {
        UserInfo userInfo = userRepository.findUserInfoByName(username).orElseThrow(() -> new UserNotFoundException(username));

        return UserResponse.Info.builder()
                .userId(userInfo.getId())
                .username(userInfo.getName())
                .email(userInfo.getEmail())
                .build();
    }

    @Transactional
    public String changeName(Long id, String username) {
        if (userRepository.existsByName(username)) {
            throw new UsernameDuplicationException(username);
        }

        findById(id).changeName(username);
        return username;
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
