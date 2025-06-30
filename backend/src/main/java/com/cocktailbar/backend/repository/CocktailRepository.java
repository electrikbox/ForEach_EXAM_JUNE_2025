package com.cocktailbar.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cocktailbar.backend.model.Cocktail;

@Repository
public interface CocktailRepository extends JpaRepository<Cocktail, Integer> {
}