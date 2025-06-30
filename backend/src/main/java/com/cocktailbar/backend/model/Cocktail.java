package com.cocktailbar.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Cocktail")
@Data
public class Cocktail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cocktail")
    private Integer idCocktail;

    @Column(name = "nom_cocktail", nullable = false)
    private String nomCocktail;

    @Column(name = "description_cocktail")
    private String descriptionCocktail;

    @ManyToOne
    @JoinColumn(name = "id_categorie", referencedColumnName = "id_categorie")
    private Categorie categorie; // Relation Many-to-One avec Categorie

    @ManyToOne
    @JoinColumn(name = "id_createur_utilisateur", referencedColumnName = "id_utilisateur")
    private Utilisateur createur; // Relation Many-to-One avec Utilisateur (le barmaker)
}