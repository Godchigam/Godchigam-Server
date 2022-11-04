package com.godchigam.godchigam.domain.recipesWish.model;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.godchigam.godchigam.domain.recipes.entity.Recipes;
import com.godchigam.godchigam.domain.user.entity.User;
import com.godchigam.godchigam.global.entity.BaseTimeEntity;

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


    @Column(nullable = false)
    private Boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userIdx")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="recipesIdx")
    @JsonManagedReference
    private Recipes recipes;



    public Wish(Boolean status, User user, Recipes recipes){
        this.recipes = recipes;
        this.user = user;
        this.status = Boolean.TRUE;
    }

    public void Wishes(Boolean status, Recipes recipes){
        this.recipes = recipes;
        this.status = Boolean.TRUE;
    }

    public Boolean getWishStatus(){
        return this.status;
    }

    public Boolean updateStatusToFalse(Boolean status){
        return this.status = Boolean.FALSE;

    }

    public Boolean updateStatusToTrue(Boolean status){
        return this.status = Boolean.TRUE;

    }

}
