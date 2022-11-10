package com.godchigam.godchigam.domain.groupbuying.entity;

import com.godchigam.godchigam.domain.user.entity.User;
import com.godchigam.godchigam.global.entity.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Product")
public class Product extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productIdx_PK")
    private Long productIdx;

    @ManyToOne
    @JoinColumn(name = "writerUserIdx_FK")
    private User writer;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private Integer productPrice;

    @Column(nullable = false)
    private String productImageUrl;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Integer goalPeopleCount;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String dealingMethod;

    @Column(nullable = false)
    private String status;

}
