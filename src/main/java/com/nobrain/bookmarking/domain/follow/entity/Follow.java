package com.nobrain.bookmarking.domain.follow.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id", "following_id"})
)
@IdClass(Follow.PK.class)
@Entity
public class Follow {

    @Id
    @Column(name = "follower_id", insertable = false, updatable = false)
    private Long follower_id;

    @Id
    @Column(name = "following_id", insertable = false, updatable = false)
    private Long following_id;

    public static class PK implements Serializable {
        Long follower_id;
        Long following_id;
    }
}
