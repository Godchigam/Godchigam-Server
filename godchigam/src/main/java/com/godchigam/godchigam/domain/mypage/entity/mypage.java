package com.godchigam.godchigam.domain.mypage.entity;

import javax.persistence.*;

import com.godchigam.godchigam.global.entity.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor

@Table(name="Mypage")
public class mypage extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="myIdx")
    private Long idx;

}