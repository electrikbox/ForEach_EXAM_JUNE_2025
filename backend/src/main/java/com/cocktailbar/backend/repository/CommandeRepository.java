package com.cocktailbar.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cocktailbar.backend.model.Commande;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Integer> {
}