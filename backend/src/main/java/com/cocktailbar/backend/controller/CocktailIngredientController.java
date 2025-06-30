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
import com.cocktailbar.backend.model.CocktailIngredient;
import com.cocktailbar.backend.model.CocktailIngredientId;
import com.cocktailbar.backend.model.Ingredient;
import com.cocktailbar.backend.repository.CocktailIngredientRepository;
import com.cocktailbar.backend.repository.CocktailRepository;
import com.cocktailbar.backend.repository.IngredientRepository;

@RestController
@RequestMapping("/api/cocktail-ingredients")
public class CocktailIngredientController {

    @Autowired
    private CocktailIngredientRepository cocktailIngredientRepository;

    @Autowired
    private CocktailRepository cocktailRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    // --- Endpoint 1: Récupérer tous les liens Cocktail-Ingrédient ---
    @GetMapping
    public List<CocktailIngredient> getAllCocktailIngredients() {
        return cocktailIngredientRepository.findAll();
    }

    // --- Endpoint 2: Récupérer un lien Cocktail-Ingrédient par ID composé ---
    // Note: Accéder par ID composé dans l'URL est plus complexe. Souvent, on préfère
    //       récupérer tous les ingrédients pour un cocktail donné (voir le prochain endpoint).
    @GetMapping("/{cocktailId}/{ingredientId}")
    public ResponseEntity<CocktailIngredient> getCocktailIngredientById(
            @PathVariable Integer cocktailId,
            @PathVariable Integer ingredientId) {

        CocktailIngredientId id = new CocktailIngredientId();
        id.setIdCocktail(cocktailId);
        id.setIdIngredient(ingredientId);

        Optional<CocktailIngredient> ci = cocktailIngredientRepository.findById(id);
        return ci.map(ResponseEntity::ok)
                 .orElse(ResponseEntity.notFound().build());
    }

    // --- Endpoint 3: Récupérer tous les ingrédients pour un cocktail spécifique ---
    @GetMapping("/cocktail/{cocktailId}")
    public List<CocktailIngredient> getIngredientsForCocktail(@PathVariable Integer cocktailId) {
        // Cette méthode nécessiterait une méthode personnalisée dans CocktailIngredientRepository
        // ou une requête JPQL/Hibernate pour filtrer par id_cocktail.
        // Pour l'instant, on peut faire un filtre après avoir récupéré tout (moins efficace pour de grandes données)
        // ou ajouter une méthode findByCocktailId dans le repository.
        // Exemple simple (moins optimal):
        return cocktailIngredientRepository.findAll().stream()
                .filter(ci -> ci.getCocktail().getIdCocktail().equals(cocktailId))
                .toList();
        // Une meilleure approche serait d'ajouter :
        // List<CocktailIngredient> findByCocktail_IdCocktail(Integer idCocktail);
        // dans CocktailIngredientRepository.
    }


    // --- Endpoint 4: Ajouter un ingrédient à un cocktail ---
    @PostMapping
    public ResponseEntity<CocktailIngredient> createCocktailIngredient(@RequestBody CocktailIngredient newCocktailIngredient) {
        // Valider l'existence du cocktail et de l'ingrédient
        if (newCocktailIngredient.getCocktail() == null || newCocktailIngredient.getCocktail().getIdCocktail() == null ||
            newCocktailIngredient.getIngredient() == null || newCocktailIngredient.getIngredient().getIdIngredient() == null) {
            return ResponseEntity.badRequest().body(null); // IDs manquants
        }

        Optional<Cocktail> cocktailOpt = cocktailRepository.findById(newCocktailIngredient.getCocktail().getIdCocktail());
        Optional<Ingredient> ingredientOpt = ingredientRepository.findById(newCocktailIngredient.getIngredient().getIdIngredient());

        if (cocktailOpt.isEmpty() || ingredientOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Cocktail ou ingrédient non trouvé
        }

        // Créer l'ID composé
        CocktailIngredientId id = new CocktailIngredientId();
        id.setIdCocktail(cocktailOpt.get().getIdCocktail());
        id.setIdIngredient(ingredientOpt.get().getIdIngredient());
        newCocktailIngredient.setId(id); // Assigner l'ID composé

        // Assigner les objets entité gérés par JPA
        newCocktailIngredient.setCocktail(cocktailOpt.get());
        newCocktailIngredient.setIngredient(ingredientOpt.get());

        // Sauvegarder
        CocktailIngredient savedCi = cocktailIngredientRepository.save(newCocktailIngredient);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCi);
    }

    // --- Endpoint 5: Mettre à jour la quantité/unité d'un ingrédient pour un cocktail ---
    @PutMapping("/{cocktailId}/{ingredientId}")
    public ResponseEntity<CocktailIngredient> updateCocktailIngredient(
            @PathVariable Integer cocktailId,
            @PathVariable Integer ingredientId,
            @RequestBody CocktailIngredient updatedCocktailIngredient) {

        CocktailIngredientId id = new CocktailIngredientId();
        id.setIdCocktail(cocktailId);
        id.setIdIngredient(ingredientId);

        Optional<CocktailIngredient> existingCiOptional = cocktailIngredientRepository.findById(id);

        if (existingCiOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        CocktailIngredient existingCi = existingCiOptional.get();
        existingCi.setQuantite(updatedCocktailIngredient.getQuantite());
        existingCi.setUnite(updatedCocktailIngredient.getUnite());

        CocktailIngredient savedCi = cocktailIngredientRepository.save(existingCi);
        return ResponseEntity.ok(savedCi);
    }

    // --- Endpoint 6: Supprimer un ingrédient d'un cocktail ---
    @DeleteMapping("/{cocktailId}/{ingredientId}")
    public ResponseEntity<Void> deleteCocktailIngredient(
            @PathVariable Integer cocktailId,
            @PathVariable Integer ingredientId) {

        CocktailIngredientId id = new CocktailIngredientId();
        id.setIdCocktail(cocktailId);
        id.setIdIngredient(ingredientId);

        if (!cocktailIngredientRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        cocktailIngredientRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}