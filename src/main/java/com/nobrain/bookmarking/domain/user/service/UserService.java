package com.nobrain.bookmarking.domain.user.service;

import com.nobrain.bookmarking.domain.auth.service.TokenService;
import com.nobrain.bookmarking.domain.user.dto.UserRequest;
import com.nobrain.bookmarking.domain.user.dto.UserResponse;
import com.nobrain.bookmarking.domain.user.entity.User;
import com.nobrain.bookmarking.domain.user.exception.UserNotFoundException;
import com.nobrain.bookmarking.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    public UserResponse.Profile getMyProfile() {
        User user = findById(tokenService.getId());

        return UserResponse.Profile.builder()
                .loginId(user.getLoginId())
                .email(user.getEmail())
                .username(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .birthDate(user.getBirthDate())
                .roles(user.getRoles())
                .build();
    }

    public String findForgotLoginIdByPhoneNumber(UserRequest.FindLoginIdByPhoneNumber request){
        return userRepository.findByNameAndPhoneNumber(request.getName(), request.getPhoneNumber())
                .orElseThrow(()->new UserNotFoundException(request.getName())).getLoginId();
    }
  
    public String findForgotLoginIdByEmail(UserRequest.FindLoginIdByEmail request){
        return userRepository.findByNameAndEmail(request.getName(), request.getEmail())
                .orElseThrow(()->new UserNotFoundException(request.getName())).getLoginId();
    }
  
    public boolean existsUsername(String username) {
        return userRepository.existsByName(username);
    }

    public boolean existsLoginId(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    public boolean existsEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public Long update(Long id, String username) {
        findById(id).updateName(username);
        return id;
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    private User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(String.valueOf(id)));
    }
}
