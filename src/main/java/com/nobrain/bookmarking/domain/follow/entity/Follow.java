package com.nobrain.bookmarking.domain.follow.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id", "following_id"})
)
@IdClass(Follow.PK.class)
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
