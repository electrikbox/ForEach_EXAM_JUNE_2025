package com.cocktailbar.backend.controller;

import java.util.List;
import java.util.Optional;

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

    private final TailleRepository tailleRepository;

    public TailleController(TailleRepository tailleRepository) {
        this.tailleRepository = tailleRepository;
    }

    @GetMapping
    public List<Taille> getAllTailles() {
        return tailleRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Taille> getTailleById(@PathVariable Integer id) {
        Optional<Taille> taille = tailleRepository.findById(id);
        return taille.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
}