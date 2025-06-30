package com.cocktailbar.backend.model;

import jakarta.persistence.Embeddable;
import lombok.Data;
import java.io.Serializable;

@Embeddable
@Data
public class CocktailIngredientId implements Serializable {

    private Integer idCocktail;
    private Integer idIngredient;
}