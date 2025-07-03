package com.cocktailbar.backend.DTO;

import java.util.List;

import lombok.Data;

@Data
public class CommandeRequestDTO {
    private Integer utilisateurId;
    private List<LigneCommandeRequestDTO> lignes;
} 