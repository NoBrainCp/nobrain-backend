package com.nobrain.bookmarking.domain.tag.repository;

import com.nobrain.bookmarking.domain.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
