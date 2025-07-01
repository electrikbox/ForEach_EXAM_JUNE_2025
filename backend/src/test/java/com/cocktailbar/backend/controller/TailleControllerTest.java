package com.cocktailbar.backend.controller;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cocktailbar.backend.model.Taille;
import com.cocktailbar.backend.repository.TailleRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "testuserBarmaker", roles = "Barmaker")
class TailleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TailleRepository tailleRepository;

    @Test
    @DisplayName("GET /api/tailles doit retourner la liste des tailles")
    void testGetAllTailles() throws Exception {
        Taille t = new Taille();
        t.setIdTaille(1);
        t.setNomTaille("S");
        Mockito.when(tailleRepository.findAll()).thenReturn(List.of(t));

        mockMvc.perform(get("/api/tailles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomTaille").value("S"));
    }

    @Test
    @DisplayName("GET /api/tailles/{id} doit retourner une taille existante")
    void testGetTailleById_found() throws Exception {
        Taille t = new Taille();
        t.setIdTaille(1);
        t.setNomTaille("S");
        Mockito.when(tailleRepository.findById(1)).thenReturn(Optional.of(t));

        mockMvc.perform(get("/api/tailles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomTaille").value("S"));
    }

    @Test
    @DisplayName("GET /api/tailles/{id} doit retourner 404 si la taille n'existe pas")
    void testGetTailleById_notFound() throws Exception {
        Mockito.when(tailleRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/tailles/99"))
                .andExpect(status().isNotFound());
    }
} 