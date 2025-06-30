package com.cocktailbar.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cocktailbar.backend.model.Taille;

@Repository
public interface TailleRepository extends JpaRepository<Taille, Integer> {
    boolean existsByNomTailleIgnoreCase(String nomTaille);
}