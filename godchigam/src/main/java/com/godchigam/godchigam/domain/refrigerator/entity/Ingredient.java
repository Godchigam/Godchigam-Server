package com.godchigam.godchigam.domain.refrigerator.entity;

import com.godchigam.godchigam.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "INGREDIENT")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredientIdx_PK")
    private Long ingredientIdx;

    @Column(nullable = false)
    private String ingredientName;

    @Column(nullable = false)
    private Timestamp ingredientBuy;

    @Column(nullable = false)
    private Timestamp ingredientLimit;

    @Column(nullable = false)
    private Integer ingredientCnt;

    @Column(nullable = false)
    private String ingredientStatus;

    @ManyToOne
    @JoinColumn(name = "frigeIdx_FK")
    private Refrigerator refrigerator;
}
