package com.cocktailbar.backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.cocktailbar.backend.DTO.CocktailDetailsDTO;
import com.cocktailbar.backend.model.Categorie;
import com.cocktailbar.backend.model.Cocktail;
import com.cocktailbar.backend.model.CocktailIngredient;
import com.cocktailbar.backend.model.CocktailTaillePrix;
import com.cocktailbar.backend.model.Utilisateur;
import com.cocktailbar.backend.repository.CategorieRepository;
import com.cocktailbar.backend.repository.CocktailIngredientRepository;
import com.cocktailbar.backend.repository.CocktailRepository;
import com.cocktailbar.backend.repository.CocktailTaillePrixRepository;
import com.cocktailbar.backend.repository.UtilisateurRepository;

@RestController
@RequestMapping("/api/cocktails")
public class CocktailController {

    @Autowired
    private CocktailRepository cocktailRepository;

    @Autowired
    private CategorieRepository categorieRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private CocktailIngredientRepository cocktailIngredientRepository;

    @Autowired
    private CocktailTaillePrixRepository cocktailTaillePrixRepository;

    // --- Endpoint 1: Récupérer tous les cocktails ---
    @GetMapping
    public List<Cocktail> getAllCocktails() {
        return cocktailRepository.findAll();
    }

    // --- Endpoint 2: Récupérer un cocktail par son ID (détaillé) ---
    @GetMapping("/{id}")
    public ResponseEntity<CocktailDetailsDTO> getCocktailById(@PathVariable Integer id) {
        Optional<Cocktail> cocktailOpt = cocktailRepository.findById(id);
        if (cocktailOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Cocktail cocktail = cocktailOpt.get();
        List<CocktailIngredient> ingredients = cocktailIngredientRepository.findByCocktail_IdCocktail(id);
        List<CocktailTaillePrix> taillesPrix = cocktailTaillePrixRepository.findByCocktail_IdCocktail(id);
        CocktailDetailsDTO dto = CocktailDetailsDTO.fromEntities(cocktail, ingredients, taillesPrix);
        return ResponseEntity.ok(dto);
    }

    // --- Endpoint 3: Créer un nouveau cocktail ---
    @PostMapping
    @PreAuthorize("hasRole('Barmaker')")
    public ResponseEntity<Cocktail> createCocktail(@RequestBody Cocktail newCocktail) {
        // Logique pour s'assurer que la catégorie et l'utilisateur existent
        if (newCocktail.getCategorie() != null && newCocktail.getCategorie().getIdCategorie() != null) {
            Optional<Categorie> categorie = categorieRepository.findById(newCocktail.getCategorie().getIdCategorie());
            if (categorie.isEmpty()) {
                return ResponseEntity.badRequest().build(); // Erreur si la catégorie n'existe pas
            }
            newCocktail.setCategorie(categorie.get());
        }

        if (newCocktail.getCreateur() != null && newCocktail.getCreateur().getIdUtilisateur() != null) {
            Optional<Utilisateur> createur = utilisateurRepository.findById(newCocktail.getCreateur().getIdUtilisateur());
            if (createur.isEmpty()) {
                return ResponseEntity.badRequest().build(); // Erreur si l'utilisateur n'existe pas
            }
            newCocktail.setCreateur(createur.get());
        }

        Cocktail savedCocktail = cocktailRepository.save(newCocktail);
        return ResponseEntity.ok(savedCocktail);
    }
    
    // --- Endpoint 4: Mettre à jour un cocktail existant ---
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('Barmaker')")
    public ResponseEntity<Cocktail> updateCocktail(@PathVariable Integer id, @RequestBody Cocktail updatedCocktail) {
        Optional<Cocktail> existingCocktailOptional = cocktailRepository.findById(id);

        if (existingCocktailOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Cocktail existingCocktail = existingCocktailOptional.get();
        
        // Mettez à jour les champs
        existingCocktail.setNomCocktail(updatedCocktail.getNomCocktail());
        existingCocktail.setDescriptionCocktail(updatedCocktail.getDescriptionCocktail());

        // Gère la mise à jour de la catégorie
        if (updatedCocktail.getCategorie() != null && updatedCocktail.getCategorie().getIdCategorie() != null) {
            Optional<Categorie> categorie = categorieRepository.findById(updatedCocktail.getCategorie().getIdCategorie());
            if (categorie.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            existingCocktail.setCategorie(categorie.get());
        }

        // Gère la mise à jour du créateur
        if (updatedCocktail.getCreateur() != null && updatedCocktail.getCreateur().getIdUtilisateur() != null) {
            Optional<Utilisateur> createur = utilisateurRepository.findById(updatedCocktail.getCreateur().getIdUtilisateur());
            if (createur.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            existingCocktail.setCreateur(createur.get());
        }
        
        Cocktail savedCocktail = cocktailRepository.save(existingCocktail);
        return ResponseEntity.ok(savedCocktail);
    }

    // --- Endpoint 5: Supprimer un cocktail ---
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('Barmaker')")
    public ResponseEntity<Void> deleteCocktail(@PathVariable Integer id) {
        if (!cocktailRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        cocktailRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}