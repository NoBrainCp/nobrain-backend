package com.nobrain.bookmarking.global.redis;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class RedisUtilTest {

    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void redisTest() throws Exception {
        //given
        String email = "test@test.com";
        String code = "test1234";

        //when
        redisUtil.setDataExpire(email, code, 60 * 60L);

        //then
        Assertions.assertThat(redisUtil.existData(email)).isTrue();
        Assertions.assertThat(redisUtil.existData("fail@fail.com")).isFalse();
        Assertions.assertThat(redisUtil.getData(email)).isEqualTo(code);
    }
}