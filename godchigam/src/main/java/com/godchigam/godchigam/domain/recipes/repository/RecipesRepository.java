package com.godchigam.godchigam.domain.recipes.repository;


import com.godchigam.godchigam.domain.recipes.entity.Recipes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipesRepository extends JpaRepository<Recipes, Long >{

    List<Recipes> findByCategoryContaining(String theme);
    List<Recipes> findAll();
    List<Recipes> findByDifficultyContaining(String difficulty);
    List<Recipes> findByCookingTimeContaining(String time);
    List<Recipes> findByDifficultyContainingAndCategoryContaining(String difficulty, String category);
    List<Recipes> findByCookingTimeContainingAndCategoryContaining(String time, String category);
    List<Recipes> findByCookingTimeStartsWith(String time);
    List<Recipes> findByCookingTimeStartsWithAndCategoryContaining(String time, String category);
    List<Recipes> findByCategoryContainingOrderByLikeCountDesc(String theme);



}
