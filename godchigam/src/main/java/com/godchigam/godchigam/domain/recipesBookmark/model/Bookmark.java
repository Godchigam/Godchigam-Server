package com.godchigam.godchigam.domain.recipesBookmark.model;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.godchigam.godchigam.domain.recipes.entity.Recipes;
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
@Table(name = "RECIPES_BOOKMARK")
public class Bookmark extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="bookmarkIdx")
    private Long bookmarkIdx;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookmarkStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userIdx")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="recipesIdx")
    @JsonManagedReference
    private Recipes recipes;


    public Bookmark(BookmarkStatus status, User user, Recipes recipes){
        this.status = status;
        this.user = user;
        this.recipes = recipes;
    }

    public Bookmark updateStatus(BookmarkStatus status){
        this.status = status;
        return this;
    }

}




