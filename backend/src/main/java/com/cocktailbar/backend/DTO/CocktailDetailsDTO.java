package com.cocktailbar.backend.DTO;

import java.util.List;
import java.util.stream.Collectors;

import com.cocktailbar.backend.model.Categorie;
import com.cocktailbar.backend.model.Cocktail;
import com.cocktailbar.backend.model.CocktailIngredient;
import com.cocktailbar.backend.model.CocktailTaillePrix;
import com.cocktailbar.backend.model.Ingredient;
import com.cocktailbar.backend.model.Taille;

import lombok.Data;

@Data
public class CocktailDetailsDTO {
    private CocktailDTO cocktail;
    private List<IngredientDTO> ingredients;
    private List<TaillePrixDTO> taillesPrix;

    @Data
    public static class CocktailDTO {
        private Integer idCocktail;
        private String nomCocktail;
        private String descriptionCocktail;
        private CategorieDTO categorie;
        public CocktailDTO(Cocktail c) {
            this.idCocktail = c.getIdCocktail();
            this.nomCocktail = c.getNomCocktail();
            this.descriptionCocktail = c.getDescriptionCocktail();
            if (c.getCategorie() != null) {
                this.categorie = new CategorieDTO(c.getCategorie());
            }
        }
    }

    @Data
    public static class CategorieDTO {
        private Integer idCategorie;
        private String nomCategorie;
        public CategorieDTO(Categorie cat) {
            this.idCategorie = cat.getIdCategorie();
            this.nomCategorie = cat.getNomCategorie();
        }
    }

    @Data
    public static class IngredientDTO {
        private Integer idIngredient;
        private String nomIngredient;
        private Double quantite;
        private String unite;
        public IngredientDTO(CocktailIngredient ci) {
            Ingredient i = ci.getIngredient();
            this.idIngredient = i.getIdIngredient();
            this.nomIngredient = i.getNomIngredient();
            this.quantite = ci.getQuantite();
            this.unite = ci.getUnite();
        }
    }

    @Data
    public static class TaillePrixDTO {
        private Integer idTaille;
        private String nomTaille;
        private Double prix;
        public TaillePrixDTO(CocktailTaillePrix ctp) {
            Taille t = ctp.getTaille();
            this.idTaille = t.getIdTaille();
            this.nomTaille = t.getNomTaille();
            this.prix = ctp.getPrix().doubleValue();
        }
    }

    public static CocktailDetailsDTO fromEntities(Cocktail cocktail, List<CocktailIngredient> ingredients, List<CocktailTaillePrix> taillesPrix) {
        CocktailDetailsDTO dto = new CocktailDetailsDTO();
        dto.cocktail = new CocktailDTO(cocktail);
        dto.ingredients = ingredients.stream().map(IngredientDTO::new).collect(Collectors.toList());
        dto.taillesPrix = taillesPrix.stream().map(TaillePrixDTO::new).collect(Collectors.toList());
        return dto;
    }
} 