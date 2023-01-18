package com.nobrain.bookmarking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BookmarkingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookmarkingApplication.class, args);
    }

}
