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

import com.cocktailbar.backend.model.Categorie;
import com.cocktailbar.backend.repository.CategorieRepository;

@WebMvcTest(CategorieController.class)
class CategorieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategorieRepository categorieRepository;

    @Test
    @DisplayName("GET /api/categories doit retourner la liste des catégories")
    void testGetAllCategories() throws Exception {
        Categorie c = new Categorie();
        c.setIdCategorie(1);
        c.setNomCategorie("Soft");
        Mockito.when(categorieRepository.findAll()).thenReturn(List.of(c));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomCategorie").value("Soft"));
    }

    @Test
    @DisplayName("GET /api/categories/{id} doit retourner une catégorie existante")
    void testGetCategorieById_found() throws Exception {
        Categorie c = new Categorie();
        c.setIdCategorie(1);
        c.setNomCategorie("Soft");
        Mockito.when(categorieRepository.findById(1)).thenReturn(Optional.of(c));

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomCategorie").value("Soft"));
    }

    @Test
    @DisplayName("GET /api/categories/{id} doit retourner 404 si la catégorie n'existe pas")
    void testGetCategorieById_notFound() throws Exception {
        Mockito.when(categorieRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/categories/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/categories doit créer une catégorie")
    void testCreateCategorie() throws Exception {
        Categorie c = new Categorie();
        c.setIdCategorie(1);
        c.setNomCategorie("Soft");
        Mockito.when(categorieRepository.save(Mockito.any(Categorie.class))).thenReturn(c);

        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nomCategorie\":\"Soft\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomCategorie").value("Soft"));
    }

    @Test
    @DisplayName("POST /api/categories doit retourner 400 si le nom est vide")
    void testCreateCategorie_badRequest() throws Exception {
        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nomCategorie\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/categories/{id} doit mettre à jour une catégorie existante")
    void testUpdateCategorie() throws Exception {
        Categorie c = new Categorie();
        c.setIdCategorie(1);
        c.setNomCategorie("Soft");
        Mockito.when(categorieRepository.findById(1)).thenReturn(Optional.of(c));
        Mockito.when(categorieRepository.save(Mockito.any(Categorie.class))).thenReturn(c);

        mockMvc.perform(put("/api/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nomCategorie\":\"Jus\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomCategorie").value("Jus")); // car le mock retourne toujours "Soft"
    }

    @Test
    @DisplayName("PUT /api/categories/{id} doit retourner 404 si la catégorie n'existe pas")
    void testUpdateCategorie_notFound() throws Exception {
        Mockito.when(categorieRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/categories/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nomCategorie\":\"Jus\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/categories/{id} doit supprimer une catégorie existante")
    void testDeleteCategorie() throws Exception {
        Mockito.when(categorieRepository.existsById(1)).thenReturn(true);

        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/categories/{id} doit retourner 404 si la catégorie n'existe pas")
    void testDeleteCategorie_notFound() throws Exception {
        Mockito.when(categorieRepository.existsById(99)).thenReturn(false);

        mockMvc.perform(delete("/api/categories/99"))
                .andExpect(status().isNotFound());
    }
} 