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
@AllArgsConstructor
@Builder
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
}
