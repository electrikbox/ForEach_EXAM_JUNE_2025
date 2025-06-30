package com.cocktailbar.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cocktailbar.backend.model.CocktailTaillePrix;
import com.cocktailbar.backend.model.CocktailTaillePrixId;

@Repository
public interface CocktailTaillePrixRepository extends JpaRepository<CocktailTaillePrix, CocktailTaillePrixId> {
}