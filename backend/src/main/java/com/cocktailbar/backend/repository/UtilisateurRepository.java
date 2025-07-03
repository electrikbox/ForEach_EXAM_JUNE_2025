package com.cocktailbar.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cocktailbar.backend.model.Utilisateur;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
    // Pas besoin d'ajouter de méthodes ici pour les opérations CRUD de base.
    // JpaRepository fournit déjà tout (save, findById, findAll, deleteById, etc.).
    Optional<Utilisateur> findByEmailUtilisateur(String email);
}