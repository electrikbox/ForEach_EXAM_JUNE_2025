package com.cocktailbar.backend.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cocktailbar.backend.model.Commande;
import com.cocktailbar.backend.model.Utilisateur;
import com.cocktailbar.backend.repository.CommandeRepository;
import com.cocktailbar.backend.repository.UtilisateurRepository;

@RestController
@RequestMapping("/api/commandes")
public class CommandeController {

    @Autowired
    private CommandeRepository commandeRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository; // Pour vérifier l'utilisateur associé à la commande

    // --- Endpoint 1: Récupérer toutes les commandes ---
    @GetMapping
    public List<Commande> getAllCommandes() {
        return commandeRepository.findAll();
    }

    // --- Endpoint 2: Récupérer une commande par son ID ---
    @GetMapping("/{id}")
    public ResponseEntity<Commande> getCommandeById(@PathVariable Integer id) {
        Optional<Commande> commande = commandeRepository.findById(id);
        return commande.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }

    // --- Endpoint 3: Créer une nouvelle commande ---
    @PostMapping
    public ResponseEntity<Commande> createCommande(@RequestBody Commande newCommande) {
        // Vérifie si l'utilisateur associé à la commande existe
        if (newCommande.getUtilisateur() != null && newCommande.getUtilisateur().getIdUtilisateur() != null) {
            Optional<Utilisateur> utilisateur = utilisateurRepository.findById(newCommande.getUtilisateur().getIdUtilisateur());
            if (utilisateur.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Retourne une 400 si l'utilisateur n'existe pas
            }
            newCommande.setUtilisateur(utilisateur.get()); // Assigne l'entité utilisateur gérée par JPA
        } else {
            return ResponseEntity.badRequest().body(null); // L'utilisateur est obligatoire pour une commande
        }

        // Définir la date de commande si elle n'est pas déjà définie dans le JSON
        if (newCommande.getDateCommande() == null) {
            newCommande.setDateCommande(LocalDateTime.now());
        }

        // Définir le statut initial de la commande
        if (newCommande.getStatutCommande() == null || newCommande.getStatutCommande().isEmpty()) {
            newCommande.setStatutCommande("Commandée"); // Statut par défaut
        }

        Commande savedCommande = commandeRepository.save(newCommande);
        return ResponseEntity.ok(savedCommande);
    }

    // --- Endpoint 4: Mettre à jour une commande existante ---
    @PutMapping("/{id}")
    public ResponseEntity<Commande> updateCommande(@PathVariable Integer id, @RequestBody Commande updatedCommande) {
        Optional<Commande> existingCommandeOptional = commandeRepository.findById(id);

        if (existingCommandeOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Commande existingCommande = existingCommandeOptional.get();

        // Mettez à jour les champs
        existingCommande.setDateCommande(updatedCommande.getDateCommande() != null ? updatedCommande.getDateCommande() : existingCommande.getDateCommande());
        existingCommande.setStatutCommande(updatedCommande.getStatutCommande() != null ? updatedCommande.getStatutCommande() : existingCommande.getStatutCommande());

        // Gère la mise à jour de l'utilisateur (si modifié)
        if (updatedCommande.getUtilisateur() != null && updatedCommande.getUtilisateur().getIdUtilisateur() != null) {
            Optional<Utilisateur> utilisateur = utilisateurRepository.findById(updatedCommande.getUtilisateur().getIdUtilisateur());
            if (utilisateur.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Erreur si le nouvel utilisateur n'existe pas
            }
            existingCommande.setUtilisateur(utilisateur.get());
        } else if (updatedCommande.getUtilisateur() == null) {
             // Si le client est explicitement mis à null dans la requête, cela peut être géré ici
             // existingCommande.setUtilisateur(null); // Décommenter si vous autorisez les commandes sans utilisateur
        }

        Commande savedCommande = commandeRepository.save(existingCommande);
        return ResponseEntity.ok(savedCommande);
    }

    // --- Endpoint 5: Supprimer une commande ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommande(@PathVariable Integer id) {
        if (!commandeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        commandeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}