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
import com.cocktailbar.backend.model.CocktailTaillePrix;
import com.cocktailbar.backend.model.CocktailTaillePrixId;
import com.cocktailbar.backend.model.Taille;
import com.cocktailbar.backend.repository.CocktailRepository;
import com.cocktailbar.backend.repository.CocktailTaillePrixRepository;
import com.cocktailbar.backend.repository.TailleRepository;

@WebMvcTest(CocktailTaillePrixController.class)
class CocktailTaillePrixControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CocktailTaillePrixRepository cocktailTaillePrixRepository;
    @MockitoBean
    private CocktailRepository cocktailRepository;
    @MockitoBean
    private TailleRepository tailleRepository;

    @Test
    @DisplayName("GET /api/cocktail-taille-prix doit retourner la liste des prix")
    void testGetAllCocktailTaillePrix() throws Exception {
        CocktailTaillePrix ctp = new CocktailTaillePrix();
        CocktailTaillePrixId id = new CocktailTaillePrixId();
        id.setIdCocktail(1);
        id.setIdTaille(1);
        ctp.setId(id);
        ctp.setPrix(new java.math.BigDecimal("5.0"));
        Mockito.when(cocktailTaillePrixRepository.findAll()).thenReturn(List.of(ctp));

        mockMvc.perform(get("/api/cocktail-taille-prix"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].prix").value(5.0));
    }

    @Test
    @DisplayName("GET /api/cocktail-taille-prix/{cocktailId}/{tailleId} doit retourner un prix existant")
    void testGetCocktailTaillePrixById_found() throws Exception {
        CocktailTaillePrix ctp = new CocktailTaillePrix();
        CocktailTaillePrixId id = new CocktailTaillePrixId();
        id.setIdCocktail(1);
        id.setIdTaille(1);
        ctp.setId(id);
        ctp.setPrix(new java.math.BigDecimal("5.0"));
        Mockito.when(cocktailTaillePrixRepository.findById(id)).thenReturn(Optional.of(ctp));

        mockMvc.perform(get("/api/cocktail-taille-prix/1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prix").value(5.0));
    }

    @Test
    @DisplayName("GET /api/cocktail-taille-prix/{cocktailId}/{tailleId} doit retourner 404 si non trouvé")
    void testGetCocktailTaillePrixById_notFound() throws Exception {
        CocktailTaillePrixId id = new CocktailTaillePrixId();
        id.setIdCocktail(99);
        id.setIdTaille(99);
        Mockito.when(cocktailTaillePrixRepository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/cocktail-taille-prix/99/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/cocktail-taille-prix doit créer un prix")
    void testCreateCocktailTaillePrix() throws Exception {
        Cocktail c = new Cocktail(); c.setIdCocktail(1);
        Taille t = new Taille(); t.setIdTaille(1);
        CocktailTaillePrix ctp = new CocktailTaillePrix();
        CocktailTaillePrixId id = new CocktailTaillePrixId();
        id.setIdCocktail(1);
        id.setIdTaille(1);
        ctp.setId(id);
        ctp.setCocktail(c);
        ctp.setTaille(t);
        ctp.setPrix(new java.math.BigDecimal("5.0"));
        Mockito.when(cocktailRepository.findById(1)).thenReturn(Optional.of(c));
        Mockito.when(tailleRepository.findById(1)).thenReturn(Optional.of(t));
        Mockito.when(cocktailTaillePrixRepository.save(Mockito.any(CocktailTaillePrix.class))).thenReturn(ctp);

        String json = "{" +
                "\"cocktail\":{\"idCocktail\":1}," +
                "\"taille\":{\"idTaille\":1}," +
                "\"prix\":5.0" +
                "}";

        mockMvc.perform(post("/api/cocktail-taille-prix")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.prix").value(5.0));
    }

    @Test
    @DisplayName("POST /api/cocktail-taille-prix doit retourner 400 si données manquantes ou entités non trouvées")
    void testCreateCocktailTaillePrix_badRequest() throws Exception {
        // IDs manquants
        String json = "{" +
                "\"cocktail\":{}," +
                "\"taille\":{}," +
                "\"prix\":5.0" +
                "}";
        mockMvc.perform(post("/api/cocktail-taille-prix")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());

        // Cocktail ou taille non trouvés
        Cocktail c = new Cocktail();
        c.setIdCocktail(1);
        Taille t = new Taille();
        t.setIdTaille(1);
        Mockito.when(cocktailRepository.findById(1)).thenReturn(Optional.empty());
        Mockito.when(tailleRepository.findById(1)).thenReturn(Optional.of(t));
        String json2 = "{" +
                "\"cocktail\":{\"idCocktail\":1}," +
                "\"taille\":{\"idTaille\":1}," +
                "\"prix\":5.0" +
                "}";
        mockMvc.perform(post("/api/cocktail-taille-prix")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json2))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/cocktail-taille-prix/{cocktailId}/{tailleId} doit mettre à jour un prix existant")
    void testUpdateCocktailTaillePrix() throws Exception {
        CocktailTaillePrixId id = new CocktailTaillePrixId();
        id.setIdCocktail(1);
        id.setIdTaille(1);
        CocktailTaillePrix ctp = new CocktailTaillePrix();
        ctp.setId(id);
        ctp.setPrix(new java.math.BigDecimal("5.0"));
        Mockito.when(cocktailTaillePrixRepository.findById(id)).thenReturn(Optional.of(ctp));
        Mockito.when(cocktailTaillePrixRepository.save(Mockito.any(CocktailTaillePrix.class))).thenReturn(ctp);

        String json = "{" +
                "\"prix\":6.0" +
                "}";

        mockMvc.perform(put("/api/cocktail-taille-prix/1/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prix").value(6.0)); // car le mock retourne toujours 5.0
    }

    @Test
    @DisplayName("PUT /api/cocktail-taille-prix/{cocktailId}/{tailleId} doit retourner 404 si non trouvé")
    void testUpdateCocktailTaillePrix_notFound() throws Exception {
        CocktailTaillePrixId id = new CocktailTaillePrixId();
        id.setIdCocktail(99);
        id.setIdTaille(99);
        Mockito.when(cocktailTaillePrixRepository.findById(id)).thenReturn(Optional.empty());

        String json = "{" +
                "\"prix\":6.0" +
                "}";

        mockMvc.perform(put("/api/cocktail-taille-prix/99/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/cocktail-taille-prix/{cocktailId}/{tailleId} doit supprimer un prix existant")
    void testDeleteCocktailTaillePrix() throws Exception {
        CocktailTaillePrixId id = new CocktailTaillePrixId();
        id.setIdCocktail(1);
        id.setIdTaille(1);
        Mockito.when(cocktailTaillePrixRepository.existsById(id)).thenReturn(true);

        mockMvc.perform(delete("/api/cocktail-taille-prix/1/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/cocktail-taille-prix/{cocktailId}/{tailleId} doit retourner 404 si non trouvé")
    void testDeleteCocktailTaillePrix_notFound() throws Exception {
        CocktailTaillePrixId id = new CocktailTaillePrixId();
        id.setIdCocktail(99);
        id.setIdTaille(99);
        Mockito.when(cocktailTaillePrixRepository.existsById(id)).thenReturn(false);

        mockMvc.perform(delete("/api/cocktail-taille-prix/99/99"))
                .andExpect(status().isNotFound());
    }
} 