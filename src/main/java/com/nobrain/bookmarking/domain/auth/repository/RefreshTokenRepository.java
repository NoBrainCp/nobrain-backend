package com.nobrain.bookmarking.domain.auth.repository;

import com.nobrain.bookmarking.domain.auth.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
