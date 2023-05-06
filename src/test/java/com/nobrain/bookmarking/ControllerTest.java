package com.nobrain.bookmarking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nobrain.bookmarking.domain.bookmark.service.BookmarkService;
import com.nobrain.bookmarking.domain.category.service.CategoryService;
import com.nobrain.bookmarking.domain.follow.service.FollowService;
import com.nobrain.bookmarking.domain.tag.service.TagService;
import com.nobrain.bookmarking.domain.user.service.UserService;
import com.nobrain.bookmarking.domain.user.service.UserSignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected UserSignService userSignService;

    @MockBean
    protected UserService userService;

    @MockBean
    protected CategoryService categoryService;

    @MockBean
    protected BookmarkService bookmarkService;

    @MockBean
    protected TagService tagService;

    @MockBean
    protected FollowService followService;
}
