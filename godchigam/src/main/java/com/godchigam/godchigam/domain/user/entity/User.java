package com.godchigam.godchigam.domain.user.entity;


import com.godchigam.godchigam.global.entity.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "USER")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userIdx_PK")
    private Long userIdx;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String profileImageUrl;

    @Column(columnDefinition = "VARCHAR(20) default 'ACTIVE'")
    private String status;

    @Column(nullable = true)
    private String accessToken;


}
