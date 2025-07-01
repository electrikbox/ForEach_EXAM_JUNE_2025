package com.cocktailbar.backend.controller;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cocktailbar.backend.model.Ingredient;
import com.cocktailbar.backend.repository.IngredientRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "testuserBarmaker", roles = "Barmaker")
class IngredientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IngredientRepository ingredientRepository;

    @Test
    @DisplayName("GET /api/ingredients doit retourner la liste des ingrédients")
    void testGetAllIngredients() throws Exception {
        Ingredient i = new Ingredient();
        i.setIdIngredient(1);
        i.setNomIngredient("Citron");
        Mockito.when(ingredientRepository.findAll()).thenReturn(List.of(i));

        mockMvc.perform(get("/api/ingredients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomIngredient").value("Citron"));
    }

    @Test
    @DisplayName("GET /api/ingredients/{id} doit retourner un ingrédient existant")
    void testGetIngredientById_found() throws Exception {
        Ingredient i = new Ingredient();
        i.setIdIngredient(1);
        i.setNomIngredient("Citron");
        Mockito.when(ingredientRepository.findById(1)).thenReturn(Optional.of(i));

        mockMvc.perform(get("/api/ingredients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomIngredient").value("Citron"));
    }

    @Test
    @DisplayName("GET /api/ingredients/{id} doit retourner 404 si l'ingrédient n'existe pas")
    void testGetIngredientById_notFound() throws Exception {
        Mockito.when(ingredientRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/ingredients/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/ingredients doit créer un ingrédient")
    void testCreateIngredient() throws Exception {
        Ingredient i = new Ingredient();
        i.setIdIngredient(1);
        i.setNomIngredient("Citron");
        Mockito.when(ingredientRepository.save(Mockito.any(Ingredient.class))).thenReturn(i);

        mockMvc.perform(post("/api/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nomIngredient\":\"Citron\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomIngredient").value("Citron"));
    }

    @Test
    @DisplayName("POST /api/ingredients doit retourner 400 si le nom est vide")
    void testCreateIngredient_badRequest() throws Exception {
        mockMvc.perform(post("/api/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nomIngredient\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/ingredients/{id} doit mettre à jour un ingrédient existant")
    void testUpdateIngredient() throws Exception {
        Ingredient i = new Ingredient();
        i.setIdIngredient(1);
        i.setNomIngredient("Citron");
        Mockito.when(ingredientRepository.findById(1)).thenReturn(Optional.of(i));
        Mockito.when(ingredientRepository.save(Mockito.any(Ingredient.class))).thenReturn(i);

        mockMvc.perform(put("/api/ingredients/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nomIngredient\":\"Lime\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomIngredient").value("Lime"));
    }

    @Test
    @DisplayName("PUT /api/ingredients/{id} doit retourner 404 si l'ingrédient n'existe pas")
    void testUpdateIngredient_notFound() throws Exception {
        Mockito.when(ingredientRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/ingredients/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nomIngredient\":\"Lime\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/ingredients/{id} doit supprimer un ingrédient existant")
    void testDeleteIngredient() throws Exception {
        Mockito.when(ingredientRepository.existsById(1)).thenReturn(true);

        mockMvc.perform(delete("/api/ingredients/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/ingredients/{id} doit retourner 404 si l'ingrédient n'existe pas")
    void testDeleteIngredient_notFound() throws Exception {
        Mockito.when(ingredientRepository.existsById(99)).thenReturn(false);

        mockMvc.perform(delete("/api/ingredients/99"))
                .andExpect(status().isNotFound());
    }
} 