package com.nobrain.bookmarking.domain.user.service;

import com.nobrain.bookmarking.domain.auth.dto.UserPayload;
import com.nobrain.bookmarking.domain.follow.entity.Follow;
import com.nobrain.bookmarking.domain.follow.repository.FollowRepository;
import com.nobrain.bookmarking.domain.user.dto.UserRequest;
import com.nobrain.bookmarking.domain.user.dto.UserResponse;
import com.nobrain.bookmarking.domain.user.dto.projection.UserInfo;
import com.nobrain.bookmarking.domain.user.entity.User;
import com.nobrain.bookmarking.domain.user.exception.UserNotCorrectPasswordException;
import com.nobrain.bookmarking.domain.user.exception.UserNotFoundException;
import com.nobrain.bookmarking.domain.user.exception.UsernameDuplicationException;
import com.nobrain.bookmarking.domain.user.repository.UserRepository;
import com.nobrain.bookmarking.global.security.Encryptor;
import com.nobrain.bookmarking.infra.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final S3Service s3Service;
    private final Encryptor encryptor;

    public UserResponse.Profile getMyProfile(UserPayload payload) {
        User user = findById(payload.getUserId());
        return UserResponse.Profile.toDto(user);
    }

    public UserResponse.Info getUserInfo(String username) {
        UserInfo userInfo = userRepository.findUserInfoByName(username).orElseThrow(() -> new UserNotFoundException(username));
        return new UserResponse.Info().toDto(userInfo);
    }

    @Transactional
    public String changeName(UserPayload payload, UserRequest.ChangeName changeName) {
        String newName = changeName.getNewName();
        if (userRepository.existsByName(newName)) {
            throw new UsernameDuplicationException(newName);
        }

        findById(payload.getUserId()).changeName(newName);
        return newName;
    }

    @Transactional
    public void changeForgotPassword(UserRequest.ChangeForgotPassword dto) {
        validateCheckPassword(dto.getPassword(), dto.getPasswordCheck());

        User user = findByName(dto.getUsername());
        user.changePassword(encryptor.encrypt(dto.getPassword()));
    }

    @Transactional
    public void changePassword(final UserPayload payload, UserRequest.ChangePassword dto) {
        Long userId = payload.getUserId();
        User user = findById(userId);

        validateCheckPassword(dto.getNewPassword(), dto.getPasswordCheck());
        validatePassword(dto.getPrePassword(), user.getPassword());

        user.changePassword(encryptor.encrypt(dto.getNewPassword()));
    }

    @Transactional
    public void changeProfileImage(final UserPayload payload, MultipartFile image) throws IOException {
        Long userId = payload.getUserId();
        User user = findById(userId);

        if (!image.isEmpty()) {
            String storedFileName = s3Service.upload(image, "images");
            user.changeProfileImage(storedFileName);
        }
    }

    @Transactional
    public void delete(UserPayload payload, UserRequest.RemoveUser removeUser) {
        User user = findById(payload.getUserId());
        validatePassword(removeUser.getPassword(), user.getPassword());

        deleteAllFollows(user);
        userRepository.delete(user);
    }

    @Transactional
    public void deleteProfileImage(UserPayload payload) {
        Long userId = payload.getUserId();
        findById(userId).changeProfileImage(null);
    }

    private User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(String.valueOf(id)));
    }

    private User findByName(String username) {
        return userRepository.findByName(username).orElseThrow(() -> new UserNotFoundException(username));
    }

    private User findByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId).orElseThrow(() -> new UserNotFoundException(loginId));
    }

    private void validatePassword(String password, String hashed) {
        if (!encryptor.isMatch(password, hashed)) {
            throw new UserNotCorrectPasswordException(password);
        }
    }

    private void validateCheckPassword(String password, String checkPassword) {
        if (!password.equals(checkPassword)) {
            throw new UserNotCorrectPasswordException(password);
        }
    }

    private void deleteAllFollows(User user) {
        List<Follow> allFollows = followRepository.findAllByToUserOrFromUser(user, user);
        followRepository.deleteAllInBatch(allFollows);
    }
}
