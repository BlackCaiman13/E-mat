package ci.scia.e_mat.dashboard;

import lombok.Data;
import java.util.Map;
import java.util.List;
import ci.scia.e_mat.materiel.MaterielDTO;

@Data
public class DashboardDTO {
    private long totalMaterielsEnPanne;
    private long totalMaterielsNeufs;
    private long totalMaterielsEnService;
    private List<Map<String, Object>> evolutionParMois;
    private Map<String, Long> repartitionParStatus;
    private List<MaterielDTO> dernieresActivites;
    private List<FournisseurStatDTO> statsFournisseurs;
}
