package com.cocktailbar.backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cocktailbar.backend.model.Cocktail;
import com.cocktailbar.backend.model.CocktailTaillePrix;
import com.cocktailbar.backend.model.CocktailTaillePrixId;
import com.cocktailbar.backend.model.Taille;
import com.cocktailbar.backend.repository.CocktailRepository;
import com.cocktailbar.backend.repository.CocktailTaillePrixRepository;
import com.cocktailbar.backend.repository.TailleRepository;

@RestController
@RequestMapping("/api/cocktail-taille-prix")
public class CocktailTaillePrixController {

    @Autowired
    private CocktailTaillePrixRepository cocktailTaillePrixRepository;

    @Autowired
    private CocktailRepository cocktailRepository;

    @Autowired
    private TailleRepository tailleRepository;

    // --- Endpoint 1: Récupérer tous les prix par taille de cocktail ---
    @GetMapping
    public List<CocktailTaillePrix> getAllCocktailTaillePrix() {
        return cocktailTaillePrixRepository.findAll();
    }

    // --- Endpoint 2: Récupérer un prix par taille de cocktail par ID composé ---
    @GetMapping("/{cocktailId}/{tailleId}")
    public ResponseEntity<CocktailTaillePrix> getCocktailTaillePrixById(
            @PathVariable Integer cocktailId,
            @PathVariable Integer tailleId) {

        CocktailTaillePrixId id = new CocktailTaillePrixId();
        id.setIdCocktail(cocktailId);
        id.setIdTaille(tailleId);

        Optional<CocktailTaillePrix> ctp = cocktailTaillePrixRepository.findById(id);
        return ctp.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    // --- Endpoint 3: Ajouter un prix pour une taille de cocktail ---
    @PostMapping
    @PreAuthorize("hasRole('Barmaker')")
    public ResponseEntity<CocktailTaillePrix> createCocktailTaillePrix(@RequestBody CocktailTaillePrix newCocktailTaillePrix) {
        // Valider l'existence du cocktail et de la taille
        if (newCocktailTaillePrix.getCocktail() == null || newCocktailTaillePrix.getCocktail().getIdCocktail() == null ||
            newCocktailTaillePrix.getTaille() == null || newCocktailTaillePrix.getTaille().getIdTaille() == null) {
            return ResponseEntity.badRequest().body(null); // IDs manquants
        }

        Optional<Cocktail> cocktailOpt = cocktailRepository.findById(newCocktailTaillePrix.getCocktail().getIdCocktail());
        Optional<Taille> tailleOpt = tailleRepository.findById(newCocktailTaillePrix.getTaille().getIdTaille());

        if (cocktailOpt.isEmpty() || tailleOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Cocktail ou taille non trouvée
        }

        // Créer l'ID composé
        CocktailTaillePrixId id = new CocktailTaillePrixId();
        id.setIdCocktail(cocktailOpt.get().getIdCocktail());
        id.setIdTaille(tailleOpt.get().getIdTaille());
        newCocktailTaillePrix.setId(id); // Assigner l'ID composé

        // Assigner les objets entité gérés par JPA
        newCocktailTaillePrix.setCocktail(cocktailOpt.get());
        newCocktailTaillePrix.setTaille(tailleOpt.get());

        // Sauvegarder
        CocktailTaillePrix savedCtp = cocktailTaillePrixRepository.save(newCocktailTaillePrix);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCtp);
    }

    // --- Endpoint 4: Mettre à jour le prix d'un cocktail pour une taille donnée ---
    @PutMapping("/{cocktailId}/{tailleId}")
    @PreAuthorize("hasRole('Barmaker')")
    public ResponseEntity<CocktailTaillePrix> updateCocktailTaillePrix(
            @PathVariable Integer cocktailId,
            @PathVariable Integer tailleId,
            @RequestBody CocktailTaillePrix updatedCocktailTaillePrix) {

        CocktailTaillePrixId id = new CocktailTaillePrixId();
        id.setIdCocktail(cocktailId);
        id.setIdTaille(tailleId);

        Optional<CocktailTaillePrix> existingCtpOptional = cocktailTaillePrixRepository.findById(id);

        if (existingCtpOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        CocktailTaillePrix existingCtp = existingCtpOptional.get();
        existingCtp.setPrix(updatedCocktailTaillePrix.getPrix()); // Mettre à jour le prix

        CocktailTaillePrix savedCtp = cocktailTaillePrixRepository.save(existingCtp);
        return ResponseEntity.ok(savedCtp);
    }

    // --- Endpoint 5: Supprimer une entrée de prix par taille pour un cocktail ---
    @DeleteMapping("/{cocktailId}/{tailleId}")
    @PreAuthorize("hasRole('Barmaker')")
    public ResponseEntity<Void> deleteCocktailTaillePrix(
            @PathVariable Integer cocktailId,
            @PathVariable Integer tailleId) {

        CocktailTaillePrixId id = new CocktailTaillePrixId();
        id.setIdCocktail(cocktailId);
        id.setIdTaille(tailleId);

        if (!cocktailTaillePrixRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        cocktailTaillePrixRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}