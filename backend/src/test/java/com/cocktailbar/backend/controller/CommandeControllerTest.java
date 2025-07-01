package com.cocktailbar.backend.controller;

import java.time.LocalDateTime;
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

import com.cocktailbar.backend.model.Commande;
import com.cocktailbar.backend.model.Utilisateur;
import com.cocktailbar.backend.repository.CommandeRepository;
import com.cocktailbar.backend.repository.UtilisateurRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "testuserBarmaker", roles = "Barmaker")
class CommandeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommandeRepository commandeRepository;

    @MockitoBean
    private UtilisateurRepository utilisateurRepository;

    @Test
    @DisplayName("GET /api/commandes doit retourner la liste des commandes")
    void testGetAllCommandes() throws Exception {
        Commande c = new Commande();
        c.setIdCommande(1);
        Mockito.when(commandeRepository.findAll()).thenReturn(List.of(c));

        mockMvc.perform(get("/api/commandes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idCommande").value(1));
    }

    @Test
    @DisplayName("GET /api/commandes/{id} doit retourner une commande existante")
    void testGetCommandeById_found() throws Exception {
        Commande c = new Commande();
        c.setIdCommande(1);
        Mockito.when(commandeRepository.findById(1)).thenReturn(Optional.of(c));

        mockMvc.perform(get("/api/commandes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCommande").value(1));
    }

    @Test
    @DisplayName("GET /api/commandes/{id} doit retourner 404 si la commande n'existe pas")
    void testGetCommandeById_notFound() throws Exception {
        Mockito.when(commandeRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/commandes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/commandes doit créer une commande")
    void testCreateCommande() throws Exception {
        Utilisateur u = new Utilisateur();
        u.setIdUtilisateur(1);
        Commande c = new Commande();
        c.setIdCommande(1);
        c.setUtilisateur(u);
        c.setDateCommande(LocalDateTime.now());
        c.setStatutCommande("Commandée");
        Mockito.when(utilisateurRepository.findById(1)).thenReturn(Optional.of(u));
        Mockito.when(commandeRepository.save(Mockito.any(Commande.class))).thenReturn(c);

        String json = "{" +
                "\"utilisateur\":{\"idUtilisateur\":1}" +
                "}";

        mockMvc.perform(post("/api/commandes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCommande").value(1))
                .andExpect(jsonPath("$.statutCommande").value("Commandée"));
    }

    @Test
    @DisplayName("POST /api/commandes doit retourner 400 si utilisateur manquant ou inexistant")
    void testCreateCommande_badRequest() throws Exception {
        // Utilisateur manquant
        String json = "{" +
                "\"utilisateur\":null" +
                "}";
        mockMvc.perform(post("/api/commandes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());

        // Utilisateur inexistant
        String json2 = "{" +
                "\"utilisateur\":{\"idUtilisateur\":99}" +
                "}";
        Mockito.when(utilisateurRepository.findById(99)).thenReturn(Optional.empty());
        mockMvc.perform(post("/api/commandes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json2))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/commandes/{id} doit mettre à jour une commande existante")
    void testUpdateCommande() throws Exception {
        Utilisateur u = new Utilisateur();
        u.setIdUtilisateur(1);
        Commande c = new Commande();
        c.setIdCommande(1);
        c.setUtilisateur(u);
        c.setDateCommande(LocalDateTime.now());
        c.setStatutCommande("Commandée");
        Mockito.when(commandeRepository.findById(1)).thenReturn(Optional.of(c));
        Mockito.when(utilisateurRepository.findById(1)).thenReturn(Optional.of(u));
        Mockito.when(commandeRepository.save(Mockito.any(Commande.class))).thenReturn(c);

        String json = "{" +
                "\"statutCommande\":\"Prête\"," +
                "\"utilisateur\":{\"idUtilisateur\":1}" +
                "}";

        mockMvc.perform(put("/api/commandes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCommande").value(1));
    }

    @Test
    @DisplayName("PUT /api/commandes/{id} doit retourner 404 si la commande n'existe pas")
    void testUpdateCommande_notFound() throws Exception {
        Mockito.when(commandeRepository.findById(99)).thenReturn(Optional.empty());

        String json = "{" +
                "\"statutCommande\":\"Prête\"" +
                "}";

        mockMvc.perform(put("/api/commandes/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/commandes/{id} doit supprimer une commande existante")
    void testDeleteCommande() throws Exception {
        Mockito.when(commandeRepository.existsById(1)).thenReturn(true);

        mockMvc.perform(delete("/api/commandes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/commandes/{id} doit retourner 404 si la commande n'existe pas")
    void testDeleteCommande_notFound() throws Exception {
        Mockito.when(commandeRepository.existsById(99)).thenReturn(false);

        mockMvc.perform(delete("/api/commandes/99"))
                .andExpect(status().isNotFound());
    }
} 