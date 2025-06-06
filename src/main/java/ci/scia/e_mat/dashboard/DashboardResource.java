package ci.scia.e_mat.dashboard;

import ci.scia.e_mat.materiel.Materiel;
import ci.scia.e_mat.materiel.MaterielDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



@RestController
@RequestMapping(value = "/api/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasRole('admin') or hasRole('exploitant') or hasRole('directeur')")
public class DashboardResource {

    private final DashboardService dashboardService;

    public DashboardResource(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/fournisseurs-stats")
    public ResponseEntity<List<FournisseurStatDTO>> getFournisseursStats() {
        return ResponseEntity.ok(dashboardService.getFournisseursStats());
    }

    @GetMapping("/evolution-mensuelle")
    public ResponseEntity<EvolutionMensuelleDTO> getEvolutionMensuelle() {
        return ResponseEntity.ok(dashboardService.getEvolutionMensuelle());
    }

    @GetMapping("/statistiques")
    public ResponseEntity<Map<String, Integer>> getStatistiquesGenerales() {
        Map<String, Integer> stats = Map.of(
            "totalMaterielsEnPanne", dashboardService.getTotalMaterielsEnPanne(),
            "totalMaterielsNeufs", dashboardService.getTotalMaterielsNeufs(),
            "totalMaterielsEnService", dashboardService.getTotalMaterielsEnService()
        );
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/dernieres-activites")
    public ResponseEntity<List<MaterielDTO>> getDernieresActivites() {
        List<Materiel> materiels = dashboardService.getDernieresActivites();
        List<MaterielDTO> materielDTOs = materiels.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(materielDTOs);
    }

    private MaterielDTO mapToDTO(Materiel materiel) {
        MaterielDTO dto = new MaterielDTO();
        dto.setId(materiel.getId());
        dto.setNature(materiel.getNature());
        dto.setModel(materiel.getModel());
        dto.setConstructeur(materiel.getConstructeur() != null ? materiel.getConstructeur().getId() : null);
        dto.setFournisseur(materiel.getFournisseur() != null ? materiel.getFournisseur().getId() : null);
        dto.setType(materiel.getType() != null ? materiel.getType().getId() : null);
        dto.setStatus(materiel.getStatus() != null ? materiel.getStatus().getId() : null);
        dto.setEmploye(materiel.getEmployes() != null ? materiel.getEmployes().getId() : null);
        dto.setLivraison(materiel.getLivraisons() != null ? materiel.getLivraisons().getId() : null);
        return dto;
    }
}
