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
import com.cocktailbar.backend.model.Cocktail;
import com.cocktailbar.backend.model.Utilisateur;
import com.cocktailbar.backend.repository.CategorieRepository;
import com.cocktailbar.backend.repository.CocktailRepository;
import com.cocktailbar.backend.repository.UtilisateurRepository;

@WebMvcTest(CocktailController.class)
class CocktailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CocktailRepository cocktailRepository;
    @MockitoBean
    private CategorieRepository categorieRepository;
    @MockitoBean
    private UtilisateurRepository utilisateurRepository;

    @Test
    @DisplayName("GET /api/cocktails doit retourner la liste des cocktails")
    void testGetAllCocktails() throws Exception {
        Cocktail c = new Cocktail();
        c.setIdCocktail(1);
        Mockito.when(cocktailRepository.findAll()).thenReturn(List.of(c));

        mockMvc.perform(get("/api/cocktails"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idCocktail").value(1));
    }

    @Test
    @DisplayName("GET /api/cocktails/{id} doit retourner un cocktail existant")
    void testGetCocktailById_found() throws Exception {
        Cocktail c = new Cocktail();
        c.setIdCocktail(1);
        Mockito.when(cocktailRepository.findById(1)).thenReturn(Optional.of(c));

        mockMvc.perform(get("/api/cocktails/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCocktail").value(1));
    }

    @Test
    @DisplayName("GET /api/cocktails/{id} doit retourner 404 si le cocktail n'existe pas")
    void testGetCocktailById_notFound() throws Exception {
        Mockito.when(cocktailRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/cocktails/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/cocktails doit créer un cocktail")
    void testCreateCocktail() throws Exception {
        Categorie cat = new Categorie(); cat.setIdCategorie(1);
        Utilisateur u = new Utilisateur(); u.setIdUtilisateur(1);
        Cocktail c = new Cocktail();
        c.setIdCocktail(1); c.setCategorie(cat); c.setCreateur(u);
        Mockito.when(categorieRepository.findById(1)).thenReturn(Optional.of(cat));
        Mockito.when(utilisateurRepository.findById(1)).thenReturn(Optional.of(u));
        Mockito.when(cocktailRepository.save(Mockito.any(Cocktail.class))).thenReturn(c);

        String json = "{" +
                "\"nomCocktail\":\"Mojito\"," +
                "\"categorie\":{\"idCategorie\":1}," +
                "\"createur\":{\"idUtilisateur\":1}" +
                "}";

        mockMvc.perform(post("/api/cocktails")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCocktail").value(1));
    }

    @Test
    @DisplayName("POST /api/cocktails doit retourner 400 si catégorie ou créateur inexistant")
    void testCreateCocktail_badRequest() throws Exception {
        // Catégorie inexistante
        Mockito.when(categorieRepository.findById(99)).thenReturn(Optional.empty());
        String json = "{" +
                "\"nomCocktail\":\"Mojito\"," +
                "\"categorie\":{\"idCategorie\":99}," +
                "\"createur\":{\"idUtilisateur\":1}" +
                "}";
        mockMvc.perform(post("/api/cocktails")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());

        // Créateur inexistant
        Categorie cat = new Categorie(); cat.setIdCategorie(1);
        Mockito.when(categorieRepository.findById(1)).thenReturn(Optional.of(cat));
        Mockito.when(utilisateurRepository.findById(99)).thenReturn(Optional.empty());
        String json2 = "{" +
                "\"nomCocktail\":\"Mojito\"," +
                "\"categorie\":{\"idCategorie\":1}," +
                "\"createur\":{\"idUtilisateur\":99}" +
                "}";
        mockMvc.perform(post("/api/cocktails")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json2))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/cocktails/{id} doit mettre à jour un cocktail existant")
    void testUpdateCocktail() throws Exception {
        Categorie cat = new Categorie(); cat.setIdCategorie(1);
        Utilisateur u = new Utilisateur(); u.setIdUtilisateur(1);
        Cocktail c = new Cocktail();
        c.setIdCocktail(1); c.setCategorie(cat); c.setCreateur(u);
        Mockito.when(cocktailRepository.findById(1)).thenReturn(Optional.of(c));
        Mockito.when(categorieRepository.findById(1)).thenReturn(Optional.of(cat));
        Mockito.when(utilisateurRepository.findById(1)).thenReturn(Optional.of(u));
        Mockito.when(cocktailRepository.save(Mockito.any(Cocktail.class))).thenReturn(c);

        String json = "{" +
                "\"nomCocktail\":\"Mojito\"," +
                "\"descriptionCocktail\":\"Menthe\"," +
                "\"categorie\":{\"idCategorie\":1}," +
                "\"createur\":{\"idUtilisateur\":1}" +
                "}";

        mockMvc.perform(put("/api/cocktails/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCocktail").value(1));
    }

    @Test
    @DisplayName("PUT /api/cocktails/{id} doit retourner 404 si le cocktail n'existe pas")
    void testUpdateCocktail_notFound() throws Exception {
        Mockito.when(cocktailRepository.findById(99)).thenReturn(Optional.empty());

        String json = "{" +
                "\"nomCocktail\":\"Mojito\"" +
                "}";

        mockMvc.perform(put("/api/cocktails/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/cocktails/{id} doit supprimer un cocktail existant")
    void testDeleteCocktail() throws Exception {
        Mockito.when(cocktailRepository.existsById(1)).thenReturn(true);

        mockMvc.perform(delete("/api/cocktails/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/cocktails/{id} doit retourner 404 si le cocktail n'existe pas")
    void testDeleteCocktail_notFound() throws Exception {
        Mockito.when(cocktailRepository.existsById(99)).thenReturn(false);

        mockMvc.perform(delete("/api/cocktails/99"))
                .andExpect(status().isNotFound());
    }
} 