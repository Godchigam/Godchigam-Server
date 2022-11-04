package com.godchigam.godchigam.domain.recipesWish.repository;


import com.godchigam.godchigam.domain.recipesWish.model.Wish;
import com.godchigam.godchigam.domain.recipes.entity.Recipes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
public interface WishRepository extends JpaRepository<Wish,Long>{
    @Query("select b from Wish b where b.user.loginId= :userId and b.recipes.idx= :recipesId")
    Optional<Wish> getWish(@Param("userId") String userId, @Param("recipesId") Long recipesId);


    /**
     * 나중에 마이페이지에서 북마크한 테마 조회할 때 사용
     */

    @Query("select b.recipes from Wish b where b.user.loginId= :userId and b.status= :status")
    List<Recipes> getRecipesList(@Param("userId") String userId, @Param("status") Boolean status);


    @Query("select b from Wish b where b.user.loginId= :userId")
    List<Wish> getWishStatus(@Param("userId") String userId);




}
