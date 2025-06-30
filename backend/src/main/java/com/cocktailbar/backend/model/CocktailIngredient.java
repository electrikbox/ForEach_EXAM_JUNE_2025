package com.cocktailbar.backend.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Cocktail_Ingredient")
@Data
public class CocktailIngredient {

    @EmbeddedId
    private CocktailIngredientId id;

    @ManyToOne
    @MapsId("idCocktail")
    @JoinColumn(name = "id_cocktail")
    private Cocktail cocktail;

    @ManyToOne
    @MapsId("idIngredient")
    @JoinColumn(name = "id_ingredient")
    private Ingredient ingredient;

    private Double quantite;

    private String unite;
}