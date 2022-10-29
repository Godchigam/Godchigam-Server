package com.godchigam.godchigam.domain.recipes.repository;


import com.godchigam.godchigam.domain.recipes.entity.Recipes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface recipesRepository extends JpaRepository<Recipes, Long >{
    //전체 레시피 보기
    Optional<Recipes> findById(Long Id);

}
