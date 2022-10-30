package com.godchigam.godchigam.domain.recipes.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.godchigam.godchigam.domain.recipesBookmark.model.Bookmark;
import com.godchigam.godchigam.domain.recipesWish.model.Wish;
import com.godchigam.godchigam.global.entity.BaseTimeEntity;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.godchigam.godchigam.domain.user.entity.User;

import java.util.ArrayList;
import java.util.List;

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

    @Column(length=5000)
    private String ingredient;

    @Column(length=5000)
    private String cooking_method;



    @JsonBackReference
    @OneToMany(mappedBy= "recipes",
    fetch = FetchType.LAZY, cascade = CascadeType.ALL,
    orphanRemoval = true)
    private List<Bookmark> bookmarks = new ArrayList<>();

    @JsonBackReference
    @OneToMany(mappedBy= "recipes",
            fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Wish> wishes = new ArrayList<>();
}
