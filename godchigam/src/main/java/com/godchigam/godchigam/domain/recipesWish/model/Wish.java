package com.godchigam.godchigam.domain.recipesWish.model;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.godchigam.godchigam.domain.recipes.entity.Recipes;
import com.godchigam.godchigam.domain.recipesBookmark.model.Bookmark;
import com.godchigam.godchigam.domain.recipesBookmark.model.BookmarkStatus;
import com.godchigam.godchigam.domain.user.entity.User;
import com.godchigam.godchigam.global.entity.BaseTimeEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "RECIPES_WISH")
public class Wish extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="wishIdx")
    private Long wishIdx;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WishStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userIdx")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="recipesIdx")
    @JsonManagedReference
    private Recipes recipes;


    public Wish(WishStatus status, User user, Recipes recipes){
        this.status = status;
        this.user = user;
        this.recipes = recipes;
    }

    public Wish updateStatus(WishStatus status){
        this.status = status;
        return this;
    }

}
