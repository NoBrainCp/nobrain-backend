package com.nobrain.bookmarking.domain.user.repository;

import com.nobrain.bookmarking.domain.user.entity.User;
import com.nobrain.bookmarking.domain.user.dto.projection.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);
    Optional<User> findByEmail(String email);
    Optional<User> findByLoginId(String loginId);
    Optional<User> findByPhoneNumber(String phoneNumber);

    boolean existsByName(String username);
    boolean existsByEmail(String email);
    boolean existsByLoginId(String loginId);
    boolean existsByPhoneNumber(String phoneNumber);

    Optional<UserInfo> findUserInfoByName(String name);
}
