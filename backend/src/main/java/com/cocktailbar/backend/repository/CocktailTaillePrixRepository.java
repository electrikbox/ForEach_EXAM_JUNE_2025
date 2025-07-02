package com.cocktailbar.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cocktailbar.backend.model.CocktailTaillePrix;
import com.cocktailbar.backend.model.CocktailTaillePrixId;

@Repository
public interface CocktailTaillePrixRepository extends JpaRepository<CocktailTaillePrix, CocktailTaillePrixId> {
    // Récupérer toutes les tailles/prix d'un cocktail donné
    List<CocktailTaillePrix> findByCocktail_IdCocktail(Integer idCocktail);
}