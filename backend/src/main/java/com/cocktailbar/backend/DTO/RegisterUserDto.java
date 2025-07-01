package com.cocktailbar.backend.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterUserDto {
    private String emailUtilisateur;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String motDePasse;
    private String roleUtilisateur;

    public RegisterUserDto() {}

    public String getEmailUtilisateur() {
        return emailUtilisateur;
    }
    public void setEmailUtilisateur(String emailUtilisateur) {
        this.emailUtilisateur = emailUtilisateur;
    }
    public String getMotDePasse() {
        return motDePasse;
    }
    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
    public String getRoleUtilisateur() {
        return roleUtilisateur;
    }
    public void setRoleUtilisateur(String roleUtilisateur) {
        this.roleUtilisateur = roleUtilisateur;
    }
} 