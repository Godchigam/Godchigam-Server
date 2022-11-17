package com.godchigam.godchigam.domain.recipes.repository;


import com.godchigam.godchigam.domain.recipes.entity.Recipes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipesRepository extends JpaRepository<Recipes, Long >{


    List<Recipes> findAll();
    List<Recipes> findByDifficultyContaining(String difficulty);
    List<Recipes> findByDifficultyContainingAndCategoryContaining(String difficulty, String category);

    List<Recipes> findByCookingTimeStartsWith(String time);
    List<Recipes> findByCategoryContainingOrderByLikeCountDesc(String theme);

    List<Recipes> findByCategoryContainingAndCookingTimeStartingWith(@Param("theme") String theme, @Param("time") String time);


}
