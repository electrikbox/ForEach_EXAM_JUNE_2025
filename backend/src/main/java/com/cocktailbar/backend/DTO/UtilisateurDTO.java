package com.cocktailbar.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour les utilisateurs
 * Exclut le mot de passe pour des raisons de sécurité
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurDTO {

    private Integer idUtilisateur;
    private String nomUtilisateur;
    private String roleUtilisateur;
    
    public UtilisateurDTO(String nomUtilisateur, String roleUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
        this.roleUtilisateur = roleUtilisateur;
    }
}
