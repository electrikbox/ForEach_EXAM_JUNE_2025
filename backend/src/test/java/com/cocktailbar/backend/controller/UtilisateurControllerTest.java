package com.cocktailbar.backend.controller;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cocktailbar.backend.model.Utilisateur;
import com.cocktailbar.backend.repository.UtilisateurRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "testuserBarmaker", roles = "Barmaker")
class UtilisateurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UtilisateurRepository utilisateurRepository;

    @Test
    @DisplayName("GET /utilisateurs doit retourner la liste des utilisateurs")
    void testGetAllUtilisateurs() throws Exception {
        Utilisateur u = new Utilisateur();
        u.setIdUtilisateur(1);
        u.setEmailUtilisateur("testuser");
        Mockito.when(utilisateurRepository.findAll()).thenReturn(List.of(u));

        mockMvc.perform(get("/utilisateurs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].emailUtilisateur").value("testuser"));
    }

    @Test
    @DisplayName("POST /utilisateurs doit créer un utilisateur")
    void testCreateUtilisateur() throws Exception {
        Utilisateur u = new Utilisateur();
        u.setIdUtilisateur(1);
        u.setEmailUtilisateur("testuser");
        Mockito.when(utilisateurRepository.save(any(Utilisateur.class))).thenReturn(u);

        mockMvc.perform(post("/utilisateurs")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"emailUtilisateur\":\"testuser\",\"motDePasse\":\"pass\",\"roleUtilisateur\":\"Client\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.emailUtilisateur").value("testuser"));
    }

    @Test
    @DisplayName("GET /utilisateurs/{id} doit retourner un utilisateur")
    void testGetUtilisateurById() throws Exception {
        Utilisateur u = new Utilisateur();
        u.setIdUtilisateur(1);
        u.setEmailUtilisateur("testuser");
        Mockito.when(utilisateurRepository.findById(1)).thenReturn(Optional.of(u));

        mockMvc.perform(get("/utilisateurs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.emailUtilisateur").value("testuser"));
    }

    @Test
    @DisplayName("DELETE /utilisateurs/{id} doit supprimer un utilisateur")
    void testDeleteUtilisateur() throws Exception {
        Mockito.when(utilisateurRepository.existsById(1)).thenReturn(true);

        mockMvc.perform(delete("/utilisateurs/1"))
                .andExpect(status().isNoContent());
    }
} 