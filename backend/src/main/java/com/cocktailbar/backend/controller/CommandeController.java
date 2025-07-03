package com.cocktailbar.backend.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cocktailbar.backend.DTO.CommandeRequestDTO;
import com.cocktailbar.backend.DTO.CommandeResponseDTO;
import com.cocktailbar.backend.model.Cocktail;
import com.cocktailbar.backend.model.Commande;
import com.cocktailbar.backend.model.LigneCommande;
import com.cocktailbar.backend.model.Taille;
import com.cocktailbar.backend.model.Utilisateur;
import com.cocktailbar.backend.repository.CocktailRepository;
import com.cocktailbar.backend.repository.CommandeRepository;
import com.cocktailbar.backend.repository.TailleRepository;
import com.cocktailbar.backend.repository.UtilisateurRepository;

@RestController
@RequestMapping("/api/commandes")
public class CommandeController {

    private final CommandeRepository commandeRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final CocktailRepository cocktailRepository;
    private final TailleRepository tailleRepository;

    public CommandeController(CommandeRepository commandeRepository,
                              UtilisateurRepository utilisateurRepository,
                              CocktailRepository cocktailRepository,
                              TailleRepository tailleRepository) {
        this.commandeRepository = commandeRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.cocktailRepository = cocktailRepository;
        this.tailleRepository = tailleRepository;
    }

    @GetMapping
    public List<CommandeResponseDTO> getAllCommandes() {
        // Récupérer l'utilisateur authentifié
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        
        // Récupérer l'utilisateur depuis la base de données
        Optional<Utilisateur> utilisateur = utilisateurRepository.findByEmailUtilisateur(userEmail);
        
        if (utilisateur.isEmpty()) {
            return List.of(); // Retourner une liste vide si l'utilisateur n'est pas trouvé
        }
        
        // Récupérer uniquement les commandes de l'utilisateur connecté
        return commandeRepository.findByUtilisateur(utilisateur.get()).stream()
            .map(CommandeResponseDTO::fromCommande)
            .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommandeResponseDTO> getCommandeById(@PathVariable Integer id) {
        Optional<Commande> commande = commandeRepository.findById(id);
        return commande.map(c -> ResponseEntity.ok(CommandeResponseDTO.fromCommande(c)))
                       .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createCommande(@RequestBody CommandeRequestDTO commandeDTO) {
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findById(commandeDTO.getUtilisateurId());
        if (utilisateurOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "L'utilisateur n'existe pas"));
        }

        Commande newCommande = new Commande();
        newCommande.setUtilisateur(utilisateurOpt.get());
        newCommande.setDateCommande(LocalDateTime.now());
        newCommande.setStatutCommande("Commandée");

        List<LigneCommande> lignes = new java.util.ArrayList<>();
        for (var ligneDTO : commandeDTO.getLignes()) {
            Optional<Cocktail> cocktailOpt = cocktailRepository.findById(ligneDTO.getCocktailId());
            Optional<Taille> tailleOpt = tailleRepository.findById(ligneDTO.getTailleId());

            if (cocktailOpt.isEmpty() || tailleOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Cocktail ou taille invalide"));
            }

            LigneCommande ligne = new LigneCommande();
            ligne.setCommande(newCommande);
            ligne.setCocktail(cocktailOpt.get());
            ligne.setTaille(tailleOpt.get());
            ligne.setQuantite(ligneDTO.getQuantite());
            lignes.add(ligne);
        }

        newCommande.setLignes(lignes);

        Commande savedCommande = commandeRepository.save(newCommande);
        return ResponseEntity.ok(CommandeResponseDTO.fromCommande(savedCommande));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommandeResponseDTO> updateCommande(@PathVariable Integer id, @RequestBody Commande updatedCommande) {
        Optional<Commande> existingCommandeOptional = commandeRepository.findById(id);

        if (existingCommandeOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Commande existingCommande = existingCommandeOptional.get();

        existingCommande.setDateCommande(updatedCommande.getDateCommande() != null ? updatedCommande.getDateCommande() : existingCommande.getDateCommande());
        existingCommande.setStatutCommande(updatedCommande.getStatutCommande() != null ? updatedCommande.getStatutCommande() : existingCommande.getStatutCommande());

        if (updatedCommande.getUtilisateur() != null && updatedCommande.getUtilisateur().getIdUtilisateur() != null) {
            Optional<Utilisateur> utilisateur = utilisateurRepository.findById(updatedCommande.getUtilisateur().getIdUtilisateur());
            if (utilisateur.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            existingCommande.setUtilisateur(utilisateur.get());
        } else if (updatedCommande.getUtilisateur() == null) {
            return ResponseEntity.badRequest().build();
        }

        Commande savedCommande = commandeRepository.save(existingCommande);
        return ResponseEntity.ok(CommandeResponseDTO.fromCommande(savedCommande));
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