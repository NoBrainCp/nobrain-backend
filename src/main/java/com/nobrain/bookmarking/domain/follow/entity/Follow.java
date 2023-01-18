package com.nobrain.bookmarking.domain.follow.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;


@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"to_user", "from_user"})
)
@IdClass(Follow.PK.class)
@Entity
public class Follow {

    @Id
    @Column(name = "to_user", insertable = false, updatable = false)
    private Long toUser;

    @Id
    @Column(name = "from_user", insertable = false, updatable = false)
    private Long fromUser;

    public static class PK implements Serializable {
        Long toUser;
        Long fromUser;
    }
}
