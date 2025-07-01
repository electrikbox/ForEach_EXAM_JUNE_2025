package com.cocktailbar.backend.DTO;

public class LoginUserDto {
    private String emailUtilisateur;
    private String motDePasse;

    public LoginUserDto() {}

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
} 