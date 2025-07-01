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

import com.cocktailbar.backend.model.Cocktail;
import com.cocktailbar.backend.model.Commande;
import com.cocktailbar.backend.model.LigneCommande;
import com.cocktailbar.backend.model.Taille;
import com.cocktailbar.backend.repository.CocktailRepository;
import com.cocktailbar.backend.repository.CommandeRepository;
import com.cocktailbar.backend.repository.LigneCommandeRepository;
import com.cocktailbar.backend.repository.TailleRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "testuserBarmaker", roles = "Barmaker")
class LigneCommandeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LigneCommandeRepository ligneCommandeRepository;

    @MockitoBean
    private CommandeRepository commandeRepository;

    @MockitoBean
    private CocktailRepository cocktailRepository;

    @MockitoBean
    private TailleRepository tailleRepository;

    @Test
    @DisplayName("GET /api/ligne-commandes doit retourner la liste des lignes de commande")
    void testGetAllLigneCommandes() throws Exception {
        LigneCommande l = new LigneCommande();
        l.setIdLigneCommande(1);
        Mockito.when(ligneCommandeRepository.findAll()).thenReturn(List.of(l));

        mockMvc.perform(get("/api/ligne-commandes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idLigneCommande").value(1));
    }

    @Test
    @DisplayName("GET /api/ligne-commandes/{id} doit retourner une ligne de commande existante")
    void testGetLigneCommandeById_found() throws Exception {
        LigneCommande l = new LigneCommande();
        l.setIdLigneCommande(1);
        Mockito.when(ligneCommandeRepository.findById(1)).thenReturn(Optional.of(l));

        mockMvc.perform(get("/api/ligne-commandes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idLigneCommande").value(1));
    }

    @Test
    @DisplayName("GET /api/ligne-commandes/{id} doit retourner 404 si la ligne n'existe pas")
    void testGetLigneCommandeById_notFound() throws Exception {
        Mockito.when(ligneCommandeRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/ligne-commandes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/ligne-commandes doit créer une ligne de commande")
    void testCreateLigneCommande() throws Exception {
        LigneCommande l = new LigneCommande();
        l.setIdLigneCommande(1);
        Commande commande = new Commande(); commande.setIdCommande(1);
        Cocktail cocktail = new Cocktail(); cocktail.setIdCocktail(1);
        Taille taille = new Taille(); taille.setIdTaille(1);
        l.setCommande(commande); l.setCocktail(cocktail); l.setTaille(taille);
        l.setStatutCocktailPreparation("En attente");
        Mockito.when(commandeRepository.findById(1)).thenReturn(Optional.of(commande));
        Mockito.when(cocktailRepository.findById(1)).thenReturn(Optional.of(cocktail));
        Mockito.when(tailleRepository.findById(1)).thenReturn(Optional.of(taille));
        Mockito.when(ligneCommandeRepository.save(Mockito.any(LigneCommande.class))).thenReturn(l);

        String json = "{" +
                "\"commande\":{\"idCommande\":1}," +
                "\"cocktail\":{\"idCocktail\":1}," +
                "\"taille\":{\"idTaille\":1}," +
                "\"quantite\":2" +
                "}";

        mockMvc.perform(post("/api/ligne-commandes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idLigneCommande").value(1))
                .andExpect(jsonPath("$.statutCocktailPreparation").value("En attente"));
    }

    @Test
    @DisplayName("POST /api/ligne-commandes doit retourner 400 si données manquantes")
    void testCreateLigneCommande_badRequest() throws Exception {
        String json = "{" +
                "\"commande\":{}," +
                "\"cocktail\":{}," +
                "\"taille\":{}," +
                "\"quantite\":2" +
                "}";
        mockMvc.perform(post("/api/ligne-commandes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/ligne-commandes/{id} doit mettre à jour une ligne de commande existante")
    void testUpdateLigneCommande() throws Exception {
        LigneCommande l = new LigneCommande();
        l.setIdLigneCommande(1);
        l.setQuantite(2);
        l.setStatutCocktailPreparation("En attente");
        Mockito.when(ligneCommandeRepository.findById(1)).thenReturn(Optional.of(l));
        Mockito.when(ligneCommandeRepository.save(Mockito.any(LigneCommande.class))).thenReturn(l);

        String json = "{" +
                "\"quantite\":3," +
                "\"statutCocktailPreparation\":\"Prêt\"" +
                "}";

        mockMvc.perform(put("/api/ligne-commandes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idLigneCommande").value(1));
    }

    @Test
    @DisplayName("PUT /api/ligne-commandes/{id} doit retourner 404 si la ligne n'existe pas")
    void testUpdateLigneCommande_notFound() throws Exception {
        Mockito.when(ligneCommandeRepository.findById(99)).thenReturn(Optional.empty());

        String json = "{" +
                "\"quantite\":3," +
                "\"statutCocktailPreparation\":\"Prêt\"" +
                "}";

        mockMvc.perform(put("/api/ligne-commandes/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/ligne-commandes/{id} doit supprimer une ligne existante")
    void testDeleteLigneCommande() throws Exception {
        Mockito.when(ligneCommandeRepository.existsById(1)).thenReturn(true);

        mockMvc.perform(delete("/api/ligne-commandes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/ligne-commandes/{id} doit retourner 404 si la ligne n'existe pas")
    void testDeleteLigneCommande_notFound() throws Exception {
        Mockito.when(ligneCommandeRepository.existsById(99)).thenReturn(false);

        mockMvc.perform(delete("/api/ligne-commandes/99"))
                .andExpect(status().isNotFound());
    }
} 