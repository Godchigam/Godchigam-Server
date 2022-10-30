package com.godchigam.godchigam.domain.recipes.repository;


import com.godchigam.godchigam.domain.recipes.entity.Recipes;
import com.godchigam.godchigam.domain.recipesBookmark.model.BookmarkStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface recipesRepository extends JpaRepository<Recipes, Long >{

    List<Recipes> findByCategoryContaining(String theme);
    Optional<Recipes> findById(String Id);
    List<Recipes> findByBookmarks(BookmarkStatus status);


}
