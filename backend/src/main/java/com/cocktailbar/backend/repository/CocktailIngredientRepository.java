package com.cocktailbar.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cocktailbar.backend.model.CocktailIngredient;
import com.cocktailbar.backend.model.CocktailIngredientId;

@Repository
public interface CocktailIngredientRepository extends JpaRepository<CocktailIngredient, CocktailIngredientId> {
} 