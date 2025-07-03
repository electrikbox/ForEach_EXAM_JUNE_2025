package com.cocktailbar.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cocktailbar.backend.model.Commande;
import com.cocktailbar.backend.model.Utilisateur;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Integer> {
    List<Commande> findByUtilisateur(Utilisateur utilisateur);
}