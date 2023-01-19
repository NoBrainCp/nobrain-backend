package com.nobrain.bookmarking.domain.category.repository;

import com.nobrain.bookmarking.domain.category.entity.Category;
import com.nobrain.bookmarking.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);
    Optional<Category> findByUserAndName(User user, String categoryName);
    List<Category> findAllByUser(User user);

    boolean existsByName(String name);
}
