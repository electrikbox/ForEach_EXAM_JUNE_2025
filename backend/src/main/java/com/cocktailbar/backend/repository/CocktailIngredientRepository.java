package com.cocktailbar.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cocktailbar.backend.model.CocktailIngredient;
import com.cocktailbar.backend.model.CocktailIngredientId;

@Repository
public interface CocktailIngredientRepository extends JpaRepository<CocktailIngredient, CocktailIngredientId> {
    // Récupérer tous les ingrédients d'un cocktail donné
    List<CocktailIngredient> findByCocktail_IdCocktail(Integer idCocktail);
}