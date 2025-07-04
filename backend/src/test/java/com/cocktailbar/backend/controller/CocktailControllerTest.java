package com.cocktailbar.backend.controller;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cocktailbar.backend.model.Categorie;
import com.cocktailbar.backend.model.Cocktail;
import com.cocktailbar.backend.repository.CategorieRepository;
import com.cocktailbar.backend.repository.CocktailRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CocktailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CocktailRepository cocktailRepository;

    @MockitoBean
    private CategorieRepository categorieRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "Barmaker")
    public void testCreateCocktail_Success() throws Exception {
        Cocktail newCocktail = new Cocktail();
        newCocktail.setNomCocktail("Nouveau Mojito");
        
        Categorie categorie = new Categorie();
        categorie.setIdCategorie(1);
        newCocktail.setCategorie(categorie);

        when(categorieRepository.findById(1)).thenReturn(Optional.of(categorie));
        when(cocktailRepository.save(any(Cocktail.class))).thenReturn(newCocktail);

        mockMvc.perform(post("/api/cocktails")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(newCocktail)))
               .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "Client")
    public void testCreateCocktail_Unauthorized() throws Exception {
        Cocktail newCocktail = new Cocktail();
        newCocktail.setNomCocktail("Nouveau Mojito");

        mockMvc.perform(post("/api/cocktails")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(newCocktail)))
               .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "Barmaker")
    public void testUpdateCocktail_Success() throws Exception {
        Cocktail existingCocktail = new Cocktail();
        existingCocktail.setIdCocktail(1);
        existingCocktail.setNomCocktail("Mojito Original");

        Cocktail updatedCocktail = new Cocktail();
        updatedCocktail.setIdCocktail(1);
        updatedCocktail.setNomCocktail("Mojito Modifié");

        when(cocktailRepository.findById(1)).thenReturn(Optional.of(existingCocktail));
        when(cocktailRepository.save(any(Cocktail.class))).thenReturn(updatedCocktail);

        mockMvc.perform(put("/api/cocktails/1")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(updatedCocktail)))
               .andExpect(status().isOk());
    }
} 