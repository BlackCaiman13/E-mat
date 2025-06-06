package ci.scia.e_mat.dashboard;

import java.util.Map;

public class FournisseurStatDTO {
    private Long id;
    private String nomFournisseur;
    private String codeFournisseur;
    private int totalMateriels;
    private double tauxPanne;
    private Map<String, Integer> repartitionStatus;
    private Map<String, Integer> evolutionLivraisons;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFournisseur() {
        return nomFournisseur;
    }

    public void setNomFournisseur(String nomFournisseur) {
        this.nomFournisseur = nomFournisseur;
    }

    public String getCodeFournisseur() {
        return codeFournisseur;
    }

    public void setCodeFournisseur(String codeFournisseur) {
        this.codeFournisseur = codeFournisseur;
    }

    public int getTotalMateriels() {
        return totalMateriels;
    }

    public void setTotalMateriels(int totalMateriels) {
        this.totalMateriels = totalMateriels;
    }

    public double getTauxPanne() {
        return tauxPanne;
    }

    public void setTauxPanne(double tauxPanne) {
        this.tauxPanne = tauxPanne;
    }

    public Map<String, Integer> getRepartitionStatus() {
        return repartitionStatus;
    }

    public void setRepartitionStatus(Map<String, Integer> repartitionStatus) {
        this.repartitionStatus = repartitionStatus;
    }

    public Map<String, Integer> getEvolutionLivraisons() {
        return evolutionLivraisons;
    }

    public void setEvolutionLivraisons(Map<String, Integer> evolutionLivraisons) {
        this.evolutionLivraisons = evolutionLivraisons;
    }
}
