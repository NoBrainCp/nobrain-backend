package com.nobrain.bookmarking.domain.user;

import com.nobrain.bookmarking.domain.bookmark.Bookmark;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String id;
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private LocalDate birthDate;

    @OneToMany(mappedBy = "user")
    private List<Bookmark> bookmarks = new ArrayList<>();

    @Builder
    public User(String id, String email, String password, String name, String phoneNumber, LocalDate birthDate, List<Bookmark> bookmarks) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.bookmarks = bookmarks;
    }
}
