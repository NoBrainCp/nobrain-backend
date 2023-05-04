package com.nobrain.bookmarking.domain.user.entity;

import com.nobrain.bookmarking.domain.category.entity.Category;
import com.nobrain.bookmarking.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.REMOVE;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@Entity
public class User extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String loginId;
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private LocalDate birthDate;
    private String profileImage;
    private String oauthId;

    @OneToMany(mappedBy = "user", cascade = REMOVE)
    private List<Category> categories;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @Builder
    public User(Long id, String loginId, String email, String password, String name, String phoneNumber, LocalDate birthDate, String profileImage, String oauthId, List<Category> categories, List<String> roles) {
        this.id = id;
        this.loginId = loginId;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.profileImage = profileImage;
        this.oauthId = oauthId;
        this.categories = categories;
        this.roles = roles;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
