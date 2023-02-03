package com.nobrain.bookmarking.domain.user.repository;

import com.nobrain.bookmarking.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByLoginId(String loginId);
    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findByName(String name);
    Optional<User> findByNameAndPhoneNumber(String name, String phoneNumber);
    Optional<User> findByNameAndEmail(String name, String email);

    boolean existsByEmail(String email);
    boolean existsByLoginId(String loginId);
    boolean existsByName(String username);
    boolean existsByPhoneNumber(String phoneNumber);
}
