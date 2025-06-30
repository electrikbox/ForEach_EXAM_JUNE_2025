package com.cocktailbar.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Taille")
@Data
public class Taille {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_taille")
    private Integer idTaille;

    @Column(name = "nom_taille", nullable = false, unique = true)
    private String nomTaille; // 'S', 'M', 'L'
}