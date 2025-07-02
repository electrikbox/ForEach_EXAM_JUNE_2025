package com.cocktailbar.backend.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    private final CommandeRepository commandeRepository;
    private final UtilisateurRepository utilisateurRepository;

    public CommandeController(CommandeRepository commandeRepository,
                              UtilisateurRepository utilisateurRepository) {
        this.commandeRepository = commandeRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    @GetMapping
    public List<Commande> getAllCommandes() {
        return commandeRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Commande> getCommandeById(@PathVariable Integer id) {
        Optional<Commande> commande = commandeRepository.findById(id);
        return commande.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createCommande(@RequestBody Commande newCommande) {
        // Vérifie si l'utilisateur associé à la commande existe
        if (newCommande.getUtilisateur() != null && newCommande.getUtilisateur().getIdUtilisateur() != null) {
            Optional<Utilisateur> utilisateur = utilisateurRepository.findById(newCommande.getUtilisateur().getIdUtilisateur());
            if (utilisateur.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "L'utilisateur n'existe pas"));
            }
            newCommande.setUtilisateur(utilisateur.get());
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", "L'utilisateur est obligatoire pour une commande"));
        }

        // Définir la date de commande si elle n'est pas déjà définie dans le JSON
        if (newCommande.getDateCommande() == null) {
            newCommande.setDateCommande(LocalDateTime.now());
        }

        // Définir le statut initial de la commande
        if (newCommande.getStatutCommande() == null || newCommande.getStatutCommande().isEmpty()) {
            newCommande.setStatutCommande("Commandée");
        }

        Commande savedCommande = commandeRepository.save(newCommande);
        return ResponseEntity.ok(savedCommande);
    }

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
            return ResponseEntity.badRequest().body(null); // idUtilisateur ne peut pas être null
        }

        Commande savedCommande = commandeRepository.save(existingCommande);
        return ResponseEntity.ok(savedCommande);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommande(@PathVariable Integer id) {
        if (!commandeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        commandeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}