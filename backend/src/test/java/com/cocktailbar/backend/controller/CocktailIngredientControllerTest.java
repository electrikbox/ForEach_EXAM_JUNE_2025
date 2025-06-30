package com.cocktailbar.backend.controller;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cocktailbar.backend.model.Cocktail;
import com.cocktailbar.backend.model.CocktailIngredient;
import com.cocktailbar.backend.model.CocktailIngredientId;
import com.cocktailbar.backend.model.Ingredient;
import com.cocktailbar.backend.repository.CocktailIngredientRepository;
import com.cocktailbar.backend.repository.CocktailRepository;
import com.cocktailbar.backend.repository.IngredientRepository;

@WebMvcTest(CocktailIngredientController.class)
class CocktailIngredientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CocktailIngredientRepository cocktailIngredientRepository;
    @MockitoBean
    private CocktailRepository cocktailRepository;
    @MockitoBean
    private IngredientRepository ingredientRepository;

    @Test
    @DisplayName("GET /api/cocktail-ingredients doit retourner la liste des liens")
    void testGetAllCocktailIngredients() throws Exception {
        CocktailIngredient ci = new CocktailIngredient();
        CocktailIngredientId id = new CocktailIngredientId();
        id.setIdCocktail(1); id.setIdIngredient(1);
        ci.setId(id);
        Mockito.when(cocktailIngredientRepository.findAll()).thenReturn(List.of(ci));

        mockMvc.perform(get("/api/cocktail-ingredients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id.idCocktail").value(1))
                .andExpect(jsonPath("$[0].id.idIngredient").value(1));
    }

    @Test
    @DisplayName("GET /api/cocktail-ingredients/{cocktailId}/{ingredientId} doit retourner un lien existant")
    void testGetCocktailIngredientById_found() throws Exception {
        CocktailIngredient ci = new CocktailIngredient();
        CocktailIngredientId id = new CocktailIngredientId();
        id.setIdCocktail(1); id.setIdIngredient(1);
        ci.setId(id);
        Mockito.when(cocktailIngredientRepository.findById(id)).thenReturn(Optional.of(ci));

        mockMvc.perform(get("/api/cocktail-ingredients/1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id.idCocktail").value(1))
                .andExpect(jsonPath("$.id.idIngredient").value(1));
    }

    @Test
    @DisplayName("GET /api/cocktail-ingredients/{cocktailId}/{ingredientId} doit retourner 404 si non trouvé")
    void testGetCocktailIngredientById_notFound() throws Exception {
        CocktailIngredientId id = new CocktailIngredientId();
        id.setIdCocktail(99); id.setIdIngredient(99);
        Mockito.when(cocktailIngredientRepository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/cocktail-ingredients/99/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/cocktail-ingredients/cocktail/{cocktailId} doit retourner les ingrédients d'un cocktail")
    void testGetIngredientsForCocktail() throws Exception {
        CocktailIngredient ci = new CocktailIngredient();
        Cocktail c = new Cocktail(); c.setIdCocktail(1);
        ci.setCocktail(c);
        Mockito.when(cocktailIngredientRepository.findAll()).thenReturn(List.of(ci));

        mockMvc.perform(get("/api/cocktail-ingredients/cocktail/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/cocktail-ingredients doit créer un lien")
    void testCreateCocktailIngredient() throws Exception {
        Cocktail c = new Cocktail(); c.setIdCocktail(1);
        Ingredient i = new Ingredient(); i.setIdIngredient(1);
        CocktailIngredient ci = new CocktailIngredient();
        CocktailIngredientId id = new CocktailIngredientId();
        id.setIdCocktail(1); id.setIdIngredient(1);
        ci.setId(id); ci.setCocktail(c); ci.setIngredient(i);
        Mockito.when(cocktailRepository.findById(1)).thenReturn(Optional.of(c));
        Mockito.when(ingredientRepository.findById(1)).thenReturn(Optional.of(i));
        Mockito.when(cocktailIngredientRepository.save(Mockito.any(CocktailIngredient.class))).thenReturn(ci);

        String json = "{" +
                "\"cocktail\":{\"idCocktail\":1}," +
                "\"ingredient\":{\"idIngredient\":1}" +
                "}";

        mockMvc.perform(post("/api/cocktail-ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id.idCocktail").value(1))
                .andExpect(jsonPath("$.id.idIngredient").value(1));
    }

    @Test
    @DisplayName("POST /api/cocktail-ingredients doit retourner 400 si données manquantes ou entités non trouvées")
    void testCreateCocktailIngredient_badRequest() throws Exception {
        // IDs manquants
        String json = "{" +
                "\"cocktail\":{}," +
                "\"ingredient\":{}" +
                "}";
        mockMvc.perform(post("/api/cocktail-ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());

        // Cocktail ou ingrédient non trouvés
        Cocktail c = new Cocktail(); c.setIdCocktail(1);
        Ingredient i = new Ingredient(); i.setIdIngredient(1);
        Mockito.when(cocktailRepository.findById(1)).thenReturn(Optional.empty());
        Mockito.when(ingredientRepository.findById(1)).thenReturn(Optional.of(i));
        String json2 = "{" +
                "\"cocktail\":{\"idCocktail\":1}," +
                "\"ingredient\":{\"idIngredient\":1}" +
                "}";
        mockMvc.perform(post("/api/cocktail-ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json2))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/cocktail-ingredients/{cocktailId}/{ingredientId} doit mettre à jour un lien existant")
    void testUpdateCocktailIngredient() throws Exception {
        CocktailIngredientId id = new CocktailIngredientId();
        id.setIdCocktail(1); id.setIdIngredient(1);
        CocktailIngredient ci = new CocktailIngredient();
        ci.setId(id); ci.setQuantite(2.0); ci.setUnite("cl");
        Mockito.when(cocktailIngredientRepository.findById(id)).thenReturn(Optional.of(ci));
        Mockito.when(cocktailIngredientRepository.save(Mockito.any(CocktailIngredient.class))).thenReturn(ci);

        String json = "{" +
                "\"quantite\":3.0," +
                "\"unite\":\"ml\"" +
                "}";

        mockMvc.perform(put("/api/cocktail-ingredients/1/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id.idCocktail").value(1));
    }

    @Test
    @DisplayName("PUT /api/cocktail-ingredients/{cocktailId}/{ingredientId} doit retourner 404 si non trouvé")
    void testUpdateCocktailIngredient_notFound() throws Exception {
        CocktailIngredientId id = new CocktailIngredientId();
        id.setIdCocktail(99); id.setIdIngredient(99);
        Mockito.when(cocktailIngredientRepository.findById(id)).thenReturn(Optional.empty());

        String json = "{" +
                "\"quantite\":3.0," +
                "\"unite\":\"ml\"" +
                "}";

        mockMvc.perform(put("/api/cocktail-ingredients/99/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/cocktail-ingredients/{cocktailId}/{ingredientId} doit supprimer un lien existant")
    void testDeleteCocktailIngredient() throws Exception {
        CocktailIngredientId id = new CocktailIngredientId();
        id.setIdCocktail(1); id.setIdIngredient(1);
        Mockito.when(cocktailIngredientRepository.existsById(id)).thenReturn(true);

        mockMvc.perform(delete("/api/cocktail-ingredients/1/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/cocktail-ingredients/{cocktailId}/{ingredientId} doit retourner 404 si non trouvé")
    void testDeleteCocktailIngredient_notFound() throws Exception {
        CocktailIngredientId id = new CocktailIngredientId();
        id.setIdCocktail(99); id.setIdIngredient(99);
        Mockito.when(cocktailIngredientRepository.existsById(id)).thenReturn(false);

        mockMvc.perform(delete("/api/cocktail-ingredients/99/99"))
                .andExpect(status().isNotFound());
    }
} 