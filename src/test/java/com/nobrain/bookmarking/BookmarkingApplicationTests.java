package com.nobrain.bookmarking;

import com.nobrain.bookmarking.domain.bookmark.Bookmark;
import com.nobrain.bookmarking.domain.user.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@SpringBootTest
@Transactional
class BookmarkingApplicationTests {

    @Autowired
    EntityManager em;

    @Test
    void addUser() {

        User user = User.builder()
                .id("leeyt1201")
                .email("leeyt1201@gmail.com")
                .password("1234")
                .build();
        em.persist(user);
        em.clear();
        em.close();

        Bookmark bookmark = Bookmark.builder()
                .url("naver.com")
                .title("네이버")
                .build();

        User findUser = em.find(User.class, user.getUserId());
        bookmark.addUser(findUser);
        em.persist(bookmark);

        Assertions.assertThat(findUser).isEqualTo(bookmark.getUser());
    }

    @Test
    void equal() {
        User user1 = User.builder()
                .id("leeyt1201")
                .build();

        User user2 = User.builder()
                .id("leeyt1201")
                .build();

        Bookmark b1 = Bookmark.builder()
                .url("naver.com")
                .user(user1)
                .title("Naver")
                .build();

        Bookmark b2 = Bookmark.builder()
                .url("naver.com")
                .user(user1)
                .title("네이버")
                .build();

        Bookmark b3 = Bookmark.builder()
                .url("naver.com")
                .user(user2)
                .build();

        Assertions.assertThat(b1).isEqualTo(b2);
        Assertions.assertThat(b1).isNotEqualTo(b3);
    }

}
