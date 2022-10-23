package com.godchigam.godchigam.domain.refrigerator.repository;

import com.godchigam.godchigam.domain.refrigerator.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient,Long> {

    Optional<Ingredient> findByIngredientIdx(Long ingredientIdx);
}
