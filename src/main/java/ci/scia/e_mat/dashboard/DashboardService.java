package ci.scia.e_mat.dashboard;

import ci.scia.e_mat.fournisseur.Fournisseur;
import ci.scia.e_mat.fournisseur.FournisseurRepository;
import ci.scia.e_mat.livraison.Livraison;
import ci.scia.e_mat.livraison.LivraisonRepository;
import ci.scia.e_mat.materiel.Materiel;
import ci.scia.e_mat.materiel.MaterielDTO;
import ci.scia.e_mat.materiel.MaterielRepository;
import ci.scia.e_mat.status.Status;
import ci.scia.e_mat.status.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DashboardService {
    private final MaterielRepository materielRepository;
    private final StatusRepository statusRepository;
    private final FournisseurRepository fournisseurRepository;
    private final LivraisonRepository livraisonRepository;

    private List<FournisseurStatDTO> calculateFournisseurStats(Status statusNeuf, Status statusEnService, Status statusEnPanne) {
        List<FournisseurStatDTO> stats = new ArrayList<>();
        List<Fournisseur> fournisseurs = fournisseurRepository.findAll();

        for (Fournisseur fournisseur : fournisseurs) {
            FournisseurStatDTO stat = new FournisseurStatDTO();
            stat.setId(fournisseur.getId());
            stat.setNomFournisseur(fournisseur.getNomFournisseur());
            stat.setCodeFournisseur(fournisseur.getCodeFournisseur());

            // Calculer le total des matériels
            long totalMaterielsLong = materielRepository.countByFournisseur_Id(fournisseur.getId());
            stat.setTotalMateriels((int) totalMaterielsLong); // Conversion explicite

            // Calculer le taux de panne
            if (totalMaterielsLong > 0) {
                long materielsPanne = materielRepository.countByFournisseur_IdAndStatus(fournisseur.getId(), statusEnPanne);
                stat.setTauxPanne((double) materielsPanne / totalMaterielsLong * 100);
            }

            // Répartition des statuts
            Map<String, Integer> repartition = new HashMap<>();
            repartition.put("Neuf", (int) materielRepository.countByFournisseur_IdAndStatus(fournisseur.getId(), statusNeuf));
            repartition.put("Usagé", (int) materielRepository.countByFournisseur_IdAndStatus(fournisseur.getId(), statusEnService));
            repartition.put("En Panne", (int) materielRepository.countByFournisseur_IdAndStatus(fournisseur.getId(), statusEnPanne));
            stat.setRepartitionStatus(repartition);

            // Évolution des livraisons sur les 12 derniers mois
            Map<String, Integer> evolutionLivraisons = new HashMap<>();
            YearMonth currentMonth = YearMonth.now();
            
            for (int i = 11; i >= 0; i--) {
                YearMonth month = currentMonth.minusMonths(i);
                String monthKey = month.toString();
                LocalDateTime startOfMonth = month.atDay(1).atStartOfDay();
                LocalDateTime endOfMonth = month.atEndOfMonth().atTime(23, 59, 59);
                OffsetDateTime start = startOfMonth.atOffset(ZoneOffset.UTC);
                OffsetDateTime end = endOfMonth.atOffset(ZoneOffset.UTC);
                
                long countLong = materielRepository
                    .findByFournisseur_IdAndDateCreatedBetween(fournisseur.getId(), start, end)
                    .stream()
                    .count();
                evolutionLivraisons.put(monthKey, (int) countLong); // Conversion explicite
            }
            stat.setEvolutionLivraisons(evolutionLivraisons);

            stats.add(stat);
        }

        return stats;
    }

    public DashboardDTO getDashboardData() {
        DashboardDTO dashboard = new DashboardDTO();
        
        // Récupérer les status
        Status statusNeuf = statusRepository.findByLibelleStatusIgnoreCase("Neuf")
                .orElseThrow(() -> new RuntimeException("Status 'Neuf' non trouvé"));
        Status statusEnService = statusRepository.findByLibelleStatusIgnoreCase("Usagé")
                .orElseThrow(() -> new RuntimeException("Status 'Usagé' non trouvé"));
        Status statusEnPanne = statusRepository.findByLibelleStatusIgnoreCase("En panne")
                .orElseThrow(() -> new RuntimeException("Status 'En panne' non trouvé"));

        // Compter les matériels par status
        dashboard.setTotalMaterielsEnPanne(materielRepository.countByStatus(statusEnPanne));
        dashboard.setTotalMaterielsNeufs(materielRepository.countByStatus(statusNeuf));
        dashboard.setTotalMaterielsEnService(materielRepository.countByStatus(statusEnService));

        // Répartition par status pour le graphique circulaire
        Map<String, Long> repartition = new HashMap<>();
        repartition.put("Neuf", dashboard.getTotalMaterielsNeufs());
        repartition.put("Usagé", dashboard.getTotalMaterielsEnService());
        repartition.put("En Panne", dashboard.getTotalMaterielsEnPanne());
        dashboard.setRepartitionParStatus(repartition);

        // Évolution sur les 6 derniers mois
        List<Map<String, Object>> evolution = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (int i = 5; i >= 0; i--) {
            LocalDateTime monthStart = now.minusMonths(i)
                .withDayOfMonth(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
            LocalDateTime monthEnd = monthStart.with(TemporalAdjusters.firstDayOfNextMonth());
            OffsetDateTime start = monthStart.atOffset(ZoneOffset.UTC);
            OffsetDateTime end = monthEnd.atOffset(ZoneOffset.UTC);
            
            Map<String, Object> monthData = new HashMap<>();
            monthData.put("mois", monthStart.getMonth().toString());
            monthData.put("neuf", materielRepository.countByStatusAndDateCreatedBetween(statusNeuf, start, end));
            monthData.put("Usagé", materielRepository.countByStatusAndDateCreatedBetween(statusEnService, start, end));
            monthData.put("enPanne", materielRepository.countByStatusAndDateCreatedBetween(statusEnPanne, start, end));
            
            evolution.add(monthData);
        }
        dashboard.setEvolutionParMois(evolution);

        // Dernières activités (10 derniers matériels modifiés) avec conversion en DTO
        List<MaterielDTO> dernieresActivites = materielRepository.findTop10ByOrderByLastUpdatedDesc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        dashboard.setDernieresActivites(dernieresActivites);

        // Ajouter les statistiques des fournisseurs
        dashboard.setStatsFournisseurs(calculateFournisseurStats(statusNeuf, statusEnService, statusEnPanne));

        return dashboard;
    }

    private MaterielDTO convertToDTO(Materiel materiel) {
        MaterielDTO dto = new MaterielDTO();
        dto.setId(materiel.getId());
        dto.setNature(materiel.getNature());
        dto.setModel(materiel.getModel());
        dto.setStatus(materiel.getStatus() != null ? materiel.getStatus().getId() : null);
        dto.setConstructeur(materiel.getConstructeur() != null ? materiel.getConstructeur().getId() : null);
        dto.setFournisseur(materiel.getFournisseur() != null ? materiel.getFournisseur().getId() : null);
        dto.setType(materiel.getType() != null ? materiel.getType().getId() : null);
        dto.setEmploye(materiel.getEmployes() != null ? materiel.getEmployes().getId() : null);
        return dto;
    }

    public List<FournisseurStatDTO> getFournisseursStats() {
        List<Fournisseur> fournisseurs = fournisseurRepository.findAll();
        return fournisseurs.stream()
                .map(this::calculateFournisseurStats)
                .collect(Collectors.toList());
    }

    private FournisseurStatDTO calculateFournisseurStats(Fournisseur fournisseur) {
        FournisseurStatDTO dto = new FournisseurStatDTO();
        dto.setId(fournisseur.getId());
        dto.setNomFournisseur(fournisseur.getNomFournisseur());
        dto.setCodeFournisseur(fournisseur.getCodeFournisseur());

        List<Materiel> materiels = materielRepository.findByFournisseur(fournisseur);
        dto.setTotalMateriels(materiels.size());

        // Calculer le taux de panne
        long materielsPannes = materiels.stream()
                .filter(m -> m.getStatus() != null && 
                        m.getStatus().getLibelleStatus().equalsIgnoreCase("En panne"))
                .count();
        dto.setTauxPanne(materiels.isEmpty() ? 0 : (double) materielsPannes / materiels.size() * 100);

        // Calculer la répartition par statut
        Map<String, Integer> repartitionStatus = new HashMap<>();
        materiels.stream()
                .filter(m -> m.getStatus() != null)
                .forEach(m -> {
                    String status = m.getStatus().getLibelleStatus();
                    repartitionStatus.merge(status, 1, Integer::sum);
                });
        dto.setRepartitionStatus(repartitionStatus);

        // Calculer l'évolution des livraisons sur les 12 derniers mois
        Map<String, Integer> evolutionLivraisons = new LinkedHashMap<>();
        YearMonth currentMonth = YearMonth.now();
        
        for (int i = 11; i >= 0; i--) {
            YearMonth month = currentMonth.minusMonths(i);
            String monthKey = month.toString();
            int count = (int) materiels.stream()
                    .filter(m -> m.getLivraisons() != null && 
                            YearMonth.from(m.getLivraisons().getDate()).equals(month))
                    .count();
            evolutionLivraisons.put(monthKey, count);
        }
        dto.setEvolutionLivraisons(evolutionLivraisons);

        return dto;
    }

    public EvolutionMensuelleDTO getEvolutionMensuelle() {
        EvolutionMensuelleDTO dto = new EvolutionMensuelleDTO();
        Map<String, Map<String, Integer>> evolution = new LinkedHashMap<>();
        
        List<Status> allStatus = statusRepository.findAll();
        YearMonth currentMonth = YearMonth.now();

        for (int i = 11; i >= 0; i--) {
            YearMonth month = currentMonth.minusMonths(i);
            String monthKey = month.toString();
            Map<String, Integer> statusCount = new LinkedHashMap<>();
            
            for (Status status : allStatus) {
                LocalDateTime startOfMonth = month.atDay(1).atStartOfDay();
                LocalDateTime endOfMonth = month.atEndOfMonth().atTime(23, 59, 59);
                OffsetDateTime start = startOfMonth.atOffset(ZoneOffset.UTC);
                OffsetDateTime end = endOfMonth.atOffset(ZoneOffset.UTC);
                
                long count = materielRepository.countByStatusAndDateCreatedBetween(status, start, end);
                statusCount.put(status.getLibelleStatus(), (int) count);
            }
            evolution.put(monthKey, statusCount);
        }
        
        dto.setEvolution(evolution);
        return dto;
    }

    public int getTotalMaterielsEnPanne() {
        Status status = statusRepository.findByLibelleStatusIgnoreCase("En panne")
                .orElseThrow(() -> new RuntimeException("Status 'En panne' non trouvé"));
        return (int) materielRepository.countByStatus(status);
    }

    public int getTotalMaterielsNeufs() {
        Status status = statusRepository.findByLibelleStatusIgnoreCase("Neuf")
                .orElseThrow(() -> new RuntimeException("Status 'Neuf' non trouvé"));
        return (int) materielRepository.countByStatus(status);
    }

    public int getTotalMaterielsEnService() {
        Status status = statusRepository.findByLibelleStatusIgnoreCase("Usagé")
                .orElseThrow(() -> new RuntimeException("Status 'En service' non trouvé"));
        return (int) materielRepository.countByStatus(status);
    }
    
    public List<Materiel> getDernieresActivites() {
        return materielRepository.findTop10ByOrderByLastUpdatedDesc();
    }
}