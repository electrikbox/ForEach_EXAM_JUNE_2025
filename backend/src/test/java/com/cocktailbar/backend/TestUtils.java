package com.cocktailbar.backend;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.cocktailbar.backend.DTO.LoginResponse;
import com.cocktailbar.backend.DTO.LoginUserDto;
import com.cocktailbar.backend.model.Categorie;
import com.cocktailbar.backend.model.Cocktail;
import com.cocktailbar.backend.model.Utilisateur;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String asJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Utilisateur createTestUtilisateur(String email, String role) {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmailUtilisateur(email);
        utilisateur.setRoleUtilisateur(role);
        utilisateur.setMotDePasse("password");
        return utilisateur;
    }

    public static Cocktail createTestCocktail(String nomCocktail, Categorie categorie, Utilisateur createur) {
        Cocktail cocktail = new Cocktail();
        cocktail.setNomCocktail(nomCocktail);
        cocktail.setDescriptionCocktail("Description de " + nomCocktail);
        cocktail.setCategorie(categorie);
        cocktail.setCreateur(createur);
        return cocktail;
    }

    public static Categorie createTestCategorie(String nomCategorie) {
        Categorie categorie = new Categorie();
        categorie.setNomCategorie(nomCategorie);
        return categorie;
    }

    public static String getTestAuthToken(MockMvc mockMvc, String email, String password) throws Exception {
        LoginUserDto loginDto = new LoginUserDto();
        loginDto.setEmailUtilisateur(email);
        loginDto.setMotDePasse(password);

        ResultActions result = mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(loginDto)));

        String responseContent = result.andReturn().getResponse().getContentAsString();
        LoginResponse loginResponse = objectMapper.readValue(responseContent, LoginResponse.class);
        return loginResponse.getToken();
    }
} 