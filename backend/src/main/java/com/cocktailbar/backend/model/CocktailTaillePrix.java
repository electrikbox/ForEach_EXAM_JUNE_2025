package com.cocktailbar.backend.model;

import java.math.BigDecimal;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Cocktail_Taille_Prix")
@Data
public class CocktailTaillePrix {

    @EmbeddedId
    private CocktailTaillePrixId id;

    @ManyToOne
    @MapsId("idCocktail")
    @JoinColumn(name = "id_cocktail")
    private Cocktail cocktail;

    @ManyToOne
    @MapsId("idTaille")
    @JoinColumn(name = "id_taille")
    private Taille taille;

    private BigDecimal prix;
}