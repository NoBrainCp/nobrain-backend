package com.nobrain.bookmarking;

import com.nobrain.bookmarking.domain.bookmark.repository.BookmarkQueryRepository;
import com.nobrain.bookmarking.domain.bookmark.repository.BookmarkRepository;
import com.nobrain.bookmarking.domain.bookmark_tag.repository.BookmarkTagQueryRepository;
import com.nobrain.bookmarking.domain.bookmark_tag.repository.BookmarkTagRepository;
import com.nobrain.bookmarking.domain.category.repository.CategoryQueryRepository;
import com.nobrain.bookmarking.domain.category.repository.CategoryRepository;
import com.nobrain.bookmarking.domain.follow.repository.FollowQueryRepository;
import com.nobrain.bookmarking.domain.follow.repository.FollowRepository;
import com.nobrain.bookmarking.domain.tag.repository.TagQueryRepository;
import com.nobrain.bookmarking.domain.tag.repository.TagRepository;
import com.nobrain.bookmarking.domain.user.repository.UserRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public abstract class ServiceTest {

    @MockBean
    protected UserRepository users;

    @MockBean
    protected CategoryRepository categories;

    @MockBean
    protected CategoryQueryRepository categoryQueryRepository;

    @MockBean
    protected BookmarkRepository bookmarks;

    @MockBean
    protected BookmarkQueryRepository bookmarkQueryRepository;

    @MockBean
    protected TagRepository tags;

    @MockBean
    protected TagQueryRepository tagQueryRepository;

    @MockBean
    protected BookmarkTagRepository bookmarkTags;

    @MockBean
    protected BookmarkTagQueryRepository bookmarkTagQueryRepository;

    @MockBean
    protected FollowRepository follows;

    @MockBean
    protected FollowQueryRepository followQueryRepository;
}
