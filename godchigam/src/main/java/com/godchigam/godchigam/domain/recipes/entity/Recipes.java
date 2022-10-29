package com.godchigam.godchigam.domain.recipes.entity;

import javax.persistence.*;
import com.godchigam.godchigam.global.entity.BaseTimeEntity;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.godchigam.godchigam.domain.user.entity.User;

@Entity
@Getter
@Setter
@NoArgsConstructor

@Table(name="Recipes")

public class Recipes extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="recipesIdx")
    private Long idx;

    @Column
    private String id;

    @Column
    private String name;

    @Column
    private String image_url;

    @Column
    private String category;

    @Column
    private String site_id;

    @Column
    private String link;

    @Column
    private String dish;

    @Column
    private String cooking_time;

    @Column
    private String difficulty;

    @Column
    private String ingredient;

    @Column
    private String cooking_method;



}
