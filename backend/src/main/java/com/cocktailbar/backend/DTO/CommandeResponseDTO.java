package com.cocktailbar.backend.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.cocktailbar.backend.model.Commande;

import lombok.Data;

@Data
public class CommandeResponseDTO {
    private Integer idCommande;
    private LocalDateTime dateCommande;
    private String statutCommande;
    private List<LigneCommandeDTO> lignes;
    private BigDecimal total;

    @Data
    public static class LigneCommandeDTO {
        private Integer idLigneCommande;
        private String nomCocktail;
        private String nomTaille;
        private int quantite;
        private BigDecimal prix;
    }

    public static CommandeResponseDTO fromCommande(Commande commande) {
        CommandeResponseDTO dto = new CommandeResponseDTO();
        dto.setIdCommande(commande.getIdCommande());
        dto.setDateCommande(commande.getDateCommande());
        dto.setStatutCommande(commande.getStatutCommande());
        
        List<LigneCommandeDTO> lignesDTO = commande.getLignes().stream()
            .map(ligne -> {
                LigneCommandeDTO ligneDTO = new LigneCommandeDTO();
                ligneDTO.setIdLigneCommande(ligne.getIdLigneCommande());
                ligneDTO.setNomCocktail(ligne.getCocktail().getNomCocktail());
                ligneDTO.setNomTaille(ligne.getTaille().getNomTaille());
                ligneDTO.setQuantite(ligne.getQuantite());
                ligneDTO.setPrix(ligne.getCocktail().getTaillesPrix().stream()
                    .filter(tp -> tp.getTaille().getIdTaille().equals(ligne.getTaille().getIdTaille()))
                    .findFirst()
                    .map(tp -> tp.getPrix())
                    .orElse(BigDecimal.ZERO));
                return ligneDTO;
            })
            .collect(Collectors.toList());
        
        dto.setLignes(lignesDTO);
        dto.setTotal(lignesDTO.stream()
            .map(ligne -> ligne.getPrix().multiply(BigDecimal.valueOf(ligne.getQuantite())))
            .reduce(BigDecimal.ZERO, BigDecimal::add));
        
        return dto;
    }
} 