package ci.scia.e_mat.dashboard;

import lombok.Data;

import java.util.Map;

@Data
public class EvolutionMensuelleDTO {
    private String mois;
    private int neuf;
    private int enService;
    private int enPanne;
    private Map<String, Map<String, Integer>> evolution;

    public String getMois() {
        return mois;
    }

    public void setMois(String mois) {
        this.mois = mois;
    }

    public int getNeuf() {
        return neuf;
    }

    public void setNeuf(int neuf) {
        this.neuf = neuf;
    }

    public int getEnService() {
        return enService;
    }

    public void setEnService(int enService) {
        this.enService = enService;
    }

    public int getEnPanne() {
        return enPanne;
    }

    public void setEnPanne(int enPanne) {
        this.enPanne = enPanne;
    }
}
