package com.nobrain.bookmarking.domain.tag.repository;

import com.nobrain.bookmarking.domain.tag.entity.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagQueryRepository {

    List<Tag> findAllByUser(Long userId);
}
