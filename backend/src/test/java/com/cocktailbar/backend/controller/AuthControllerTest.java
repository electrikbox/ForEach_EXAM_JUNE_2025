package com.cocktailbar.backend.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cocktailbar.backend.DTO.LoginResponse;
import com.cocktailbar.backend.TestUtils;
import com.cocktailbar.backend.model.Utilisateur;
import com.cocktailbar.backend.repository.UtilisateurRepository;
import com.cocktailbar.backend.service.AuthService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private UtilisateurRepository utilisateurRepository;

    @Test
    @DisplayName("POST /auth/register - Succès")
    void testRegister_Success() throws Exception {
        Utilisateur newUser = TestUtils.createTestUtilisateur("test@test.com", "ROLE_Client");
        Mockito.when(authService.register(any())).thenReturn(newUser);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(newUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.emailUtilisateur").value("test@test.com"))
                .andExpect(jsonPath("$.roleUtilisateur").value("ROLE_Client"));
    }

    @Test
    @DisplayName("POST /auth/register - Email déjà utilisé")
    void testRegister_EmailAlreadyExists() throws Exception {
        Mockito.when(authService.register(any())).thenThrow(new RuntimeException("Email déjà utilisé"));

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(TestUtils.createTestUtilisateur("test@test.com", "ROLE_Client"))))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("POST /auth/login - Succès")
    void testLogin_Success() throws Exception {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken("test.jwt.token");
        loginResponse.setExpiresIn(3600000L);
        Mockito.when(authService.login(any())).thenReturn(loginResponse);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"emailUtilisateur\":\"test@test.com\",\"motDePasse\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("token"))
                .andExpect(jsonPath("$.token").value("test.jwt.token"));
    }

    @Test
    @DisplayName("POST /auth/login - Identifiants invalides")
    void testLogin_InvalidCredentials() throws Exception {
        Mockito.when(authService.login(any())).thenThrow(new RuntimeException("Identifiants invalides"));

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"emailUtilisateur\":\"test@test.com\",\"motDePasse\":\"wrongpassword\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("GET /auth/me - Utilisateur authentifié")
    @WithMockUser(username = "test@test.com", roles = {"USER"})
    void testGetMe_Authenticated() throws Exception {
        Utilisateur user = TestUtils.createTestUtilisateur("test@test.com", "ROLE_USER");
        Mockito.when(utilisateurRepository.findByEmailUtilisateur("test@test.com")).thenReturn(java.util.Optional.of(user));

        mockMvc.perform(get("/auth/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("test@test.com"))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"));
    }

    @Test
    @DisplayName("GET /auth/me - Non authentifié")
    void testGetMe_Unauthenticated() throws Exception {
        mockMvc.perform(get("/auth/me"))
                .andExpect(status().isForbidden());
    }
} 