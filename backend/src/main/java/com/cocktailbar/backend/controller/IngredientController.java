package com.cocktailbar.backend.controller;

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

import com.cocktailbar.backend.model.Ingredient;
import com.cocktailbar.backend.repository.IngredientRepository;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {

    @Autowired
    private IngredientRepository ingredientRepository;

    // --- Endpoint 1: Récupérer tous les ingrédients ---
    @GetMapping
    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    // --- Endpoint 2: Récupérer un ingrédient par son ID ---
    @GetMapping("/{id}")
    public ResponseEntity<Ingredient> getIngredientById(@PathVariable Integer id) {
        Optional<Ingredient> ingredient = ingredientRepository.findById(id);
        return ingredient.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }

    // --- Endpoint 3: Créer un nouvel ingrédient ---
    @PostMapping
    public ResponseEntity<Ingredient> createIngredient(@RequestBody Ingredient newIngredient) {
        if (newIngredient.getNomIngredient() == null || newIngredient.getNomIngredient().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        Ingredient savedIngredient = ingredientRepository.save(newIngredient);
        return ResponseEntity.ok(savedIngredient);
    }

    // --- Endpoint 4: Mettre à jour un ingrédient existant ---
    @PutMapping("/{id}")
    public ResponseEntity<Ingredient> updateIngredient(@PathVariable Integer id, @RequestBody Ingredient updatedIngredient) {
        Optional<Ingredient> existingIngredientOptional = ingredientRepository.findById(id);

        if (existingIngredientOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Ingredient existingIngredient = existingIngredientOptional.get();
        if (updatedIngredient.getNomIngredient() != null && !updatedIngredient.getNomIngredient().trim().isEmpty()) {
            existingIngredient.setNomIngredient(updatedIngredient.getNomIngredient());
        }
        
        Ingredient savedIngredient = ingredientRepository.save(existingIngredient);
        return ResponseEntity.ok(savedIngredient);
    }

    // --- Endpoint 5: Supprimer un ingrédient ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Integer id) {
        if (!ingredientRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        ingredientRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}