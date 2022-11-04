package com.godchigam.godchigam.domain.recipes.repository;


import com.godchigam.godchigam.domain.recipes.entity.Recipes;
import com.godchigam.godchigam.domain.recipesBookmark.model.Bookmark;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface recipesRepository extends JpaRepository<Recipes, Long >{

    List<Recipes> findByCategoryContaining(String theme);
    List<Recipes> findAll();
    Optional<Recipes> findById(String Id);
    List<Recipes> findByBookmarks(Boolean status);
    List<Recipes> findByDifficultyContaining(String difficulty);
    List<Recipes> findByCookingTimeContaining(String time);
    List<Recipes> findByDifficultyContainingAndCategoryContaining(String difficulty, String category);
    List<Recipes> findByCookingTimeContainingAndCategoryContaining(String time, String category);
    List<Recipes> findByCookingTimeStartsWith(String time);
    List<Recipes> findByCookingTimeStartsWithAndCategoryContaining(String time, String category);
    List<Recipes> findByCategoryContainingOrderByLikeCountDesc(String theme);

}
