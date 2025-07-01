package com.cocktailbar.backend.DTO;

public class RegisterUserDto {
    private String nomUtilisateur;
    private String motDePasse;
    private String roleUtilisateur;

    public RegisterUserDto() {}

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }
    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
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