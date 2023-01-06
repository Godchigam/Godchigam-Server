package com.godchigam.godchigam.domain.refrigerator.entity;

import com.godchigam.godchigam.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

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

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate ingredientBuyDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate ingredientLimitDate;

    @Column(nullable = false)
    private Integer ingredientCnt;

    @Column(nullable = false)
    private String ingredientStatus;

    @ManyToOne
    @JoinColumn(name = "frigeIdx_FK")
    private Refrigerator refrigerator;
}
