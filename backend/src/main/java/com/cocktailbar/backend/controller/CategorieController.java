package com.cocktailbar.backend.controller;

import java.util.List;
import java.util.Optional;

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

import com.cocktailbar.backend.model.Categorie;
import com.cocktailbar.backend.repository.CategorieRepository;

@RestController
@RequestMapping("/api/categories")
public class CategorieController {

    private final CategorieRepository categorieRepository;

    public CategorieController(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

    @GetMapping
    public List<Categorie> getAllCategories() {
        return categorieRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categorie> getCategorieById(@PathVariable Integer id) {
        Optional<Categorie> categorie = categorieRepository.findById(id);
        return categorie.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('Barmaker')")
    public ResponseEntity<Categorie> createCategorie(@RequestBody Categorie newCategorie) {
        // Optionnel: Ajouter une validation pour s'assurer que le nom n'est pas vide
        if (newCategorie.getNomCategorie() == null || newCategorie.getNomCategorie().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        Categorie savedCategorie = categorieRepository.save(newCategorie);
        return ResponseEntity.ok(savedCategorie);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('Barmaker')")
    public ResponseEntity<Categorie> updateCategorie(@PathVariable Integer id, @RequestBody Categorie updatedCategorie) {
        Optional<Categorie> existingCategorieOptional = categorieRepository.findById(id);

        if (existingCategorieOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Categorie existingCategorie = existingCategorieOptional.get();
        // Mettez à jour uniquement les champs modifiables
        if (updatedCategorie.getNomCategorie() != null && !updatedCategorie.getNomCategorie().trim().isEmpty()) {
            existingCategorie.setNomCategorie(updatedCategorie.getNomCategorie());
        }
        
        Categorie savedCategorie = categorieRepository.save(existingCategorie);
        return ResponseEntity.ok(savedCategorie);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('Barmaker')")
    public ResponseEntity<Void> deleteCategorie(@PathVariable Integer id) {
        if (!categorieRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        categorieRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}