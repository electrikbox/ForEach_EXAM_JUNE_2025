package com.cocktailbar.backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cocktailbar.backend.model.Cocktail;
import com.cocktailbar.backend.model.Commande;
import com.cocktailbar.backend.model.LigneCommande;
import com.cocktailbar.backend.model.Taille;
import com.cocktailbar.backend.repository.CocktailRepository;
import com.cocktailbar.backend.repository.CommandeRepository;
import com.cocktailbar.backend.repository.LigneCommandeRepository;
import com.cocktailbar.backend.repository.TailleRepository;

@RestController
@RequestMapping("/api/ligne-commandes")
public class LigneCommandeController {

    @Autowired
    private LigneCommandeRepository ligneCommandeRepository;

    @Autowired
    private CommandeRepository commandeRepository;

    @Autowired
    private CocktailRepository cocktailRepository;

    @Autowired
    private TailleRepository tailleRepository;

    // --- Endpoint 1: Récupérer toutes les lignes de commande ---
    @GetMapping
    public List<LigneCommande> getAllLigneCommandes() {
        return ligneCommandeRepository.findAll();
    }

    // --- Endpoint 2: Récupérer une ligne de commande par son ID ---
    @GetMapping("/{id}")
    public ResponseEntity<LigneCommande> getLigneCommandeById(@PathVariable Integer id) {
        Optional<LigneCommande> ligneCommande = ligneCommandeRepository.findById(id);
        return ligneCommande.map(ResponseEntity::ok)
                            .orElse(ResponseEntity.notFound().build());
    }

    // --- Endpoint 3: Créer une nouvelle ligne de commande ---
    @PostMapping
    public ResponseEntity<LigneCommande> createLigneCommande(@RequestBody LigneCommande newLineCommande) {
        // Valider l'existence de la commande, du cocktail et de la taille
        if (newLineCommande.getCommande() == null || newLineCommande.getCommande().getIdCommande() == null ||
            newLineCommande.getCocktail() == null || newLineCommande.getCocktail().getIdCocktail() == null ||
            newLineCommande.getTaille() == null || newLineCommande.getTaille().getIdTaille() == null) {
            return ResponseEntity.badRequest().body(null); // IDs manquants
        }

        Optional<Commande> commandeOpt = commandeRepository.findById(newLineCommande.getCommande().getIdCommande());
        Optional<Cocktail> cocktailOpt = cocktailRepository.findById(newLineCommande.getCocktail().getIdCocktail());
        Optional<Taille> tailleOpt = tailleRepository.findById(newLineCommande.getTaille().getIdTaille());

        if (commandeOpt.isEmpty() || cocktailOpt.isEmpty() || tailleOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Commande, Cocktail ou Taille non trouvé
        }

        // Assigner les objets entité gérés par JPA
        newLineCommande.setCommande(commandeOpt.get());
        newLineCommande.setCocktail(cocktailOpt.get());
        newLineCommande.setTaille(tailleOpt.get());

        // Définir le statut initial de préparation si non fourni
        if (newLineCommande.getStatutCocktailPreparation() == null || newLineCommande.getStatutCocktailPreparation().isEmpty()) {
            newLineCommande.setStatutCocktailPreparation("En attente"); // Statut par défaut
        }

        LigneCommande savedLigneCommande = ligneCommandeRepository.save(newLineCommande);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLigneCommande);
    }

    // --- Endpoint 4: Mettre à jour une ligne de commande existante ---
    @PutMapping("/{id}")
    public ResponseEntity<LigneCommande> updateLigneCommande(@PathVariable Integer id, @RequestBody LigneCommande updatedLigneCommande) {
        Optional<LigneCommande> existingLigneCommandeOptional = ligneCommandeRepository.findById(id);

        if (existingLigneCommandeOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        LigneCommande existingLigneCommande = existingLigneCommandeOptional.get();

        // Mettez à jour les champs comme la quantité et le statut
        existingLigneCommande.setQuantite(updatedLigneCommande.getQuantite() != null ? updatedLigneCommande.getQuantite() : existingLigneCommande.getQuantite());
        existingLigneCommande.setStatutCocktailPreparation(updatedLigneCommande.getStatutCocktailPreparation() != null ? updatedLigneCommande.getStatutCocktailPreparation() : existingLigneCommande.getStatutCocktailPreparation());

        // La commande, le cocktail et la taille ne devraient généralement pas être modifiés après la création d'une ligne de commande,
        // mais si votre logique métier l'exige, vous feriez des validations similaires au POST ici.
        // Pour l'instant, nous supposons qu'ils restent inchangés ou sont gérés par un DELETE/POST.

        LigneCommande savedLigneCommande = ligneCommandeRepository.save(existingLigneCommande);
        return ResponseEntity.ok(savedLigneCommande);
    }

    // --- Endpoint 5: Supprimer une ligne de commande ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLigneCommande(@PathVariable Integer id) {
        if (!ligneCommandeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        ligneCommandeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}