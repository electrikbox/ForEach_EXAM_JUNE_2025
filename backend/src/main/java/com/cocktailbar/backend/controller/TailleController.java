package com.cocktailbar.backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cocktailbar.backend.model.Taille;
import com.cocktailbar.backend.repository.TailleRepository;

@RestController
@RequestMapping("/api/tailles")
public class TailleController {

    @Autowired
    private TailleRepository tailleRepository;

    // --- Endpoint 1: Récupérer toutes les tailles ---
    @GetMapping
    public List<Taille> getAllTailles() {
        return tailleRepository.findAll();
    }

    // --- Endpoint 2: Récupérer une taille par son ID ---
    @GetMapping("/{id}")
    public ResponseEntity<Taille> getTailleById(@PathVariable Integer id) {
        Optional<Taille> taille = tailleRepository.findById(id);
        return taille.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
}