package com.cocktailbar.backend.DTO;

import lombok.Data;

@Data
public class LigneCommandeRequestDTO {
    private Integer cocktailId;
    private Integer tailleId;
    private Integer quantite;
} 