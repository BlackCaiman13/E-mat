package ci.scia.e_mat.materiel;

import ci.scia.e_mat.constructeur.Constructeur;
import ci.scia.e_mat.constructeur.ConstructeurRepository;
import ci.scia.e_mat.employe.Employe;
import ci.scia.e_mat.employe.EmployeRepository;
import ci.scia.e_mat.fournisseur.Fournisseur;
import ci.scia.e_mat.fournisseur.FournisseurRepository;
import ci.scia.e_mat.livraison.Livraison;
import ci.scia.e_mat.livraison.LivraisonRepository;
import ci.scia.e_mat.status.Status;
import ci.scia.e_mat.status.StatusRepository;
import ci.scia.e_mat.type.Type;
import ci.scia.e_mat.type.TypeRepository;
import ci.scia.e_mat.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class MaterielService {

    private final MaterielRepository materielRepository;
    private final ConstructeurRepository constructeurRepository;
    private final FournisseurRepository fournisseurRepository;
    private final TypeRepository typeRepository;
    private final StatusRepository statusRepository;
    private final EmployeRepository employeRepository;
    private final LivraisonRepository livraisonRepository;
    private static final Logger log = LoggerFactory.getLogger(MaterielService.class);


    public MaterielService(final MaterielRepository materielRepository,
            final ConstructeurRepository constructeurRepository,
            final FournisseurRepository fournisseurRepository, final TypeRepository typeRepository,
            final StatusRepository statusRepository, final EmployeRepository employeRepository,
            final LivraisonRepository livraisonRepository) {
        this.materielRepository = materielRepository;
        this.constructeurRepository = constructeurRepository;
        this.fournisseurRepository = fournisseurRepository;
        this.typeRepository = typeRepository;
        this.statusRepository = statusRepository;
        this.employeRepository = employeRepository;
        this.livraisonRepository = livraisonRepository;
    }

    public List<MaterielDTO> findAll() {
        final List<Materiel> materiels = materielRepository.findAll(Sort.by("id"));
        return materiels.stream()
                .map(materiel -> mapToDTO(materiel, new MaterielDTO()))
                .toList();
    }

    public MaterielDTO get(final Long id) {
        return materielRepository.findById(id)
                .map(materiel -> mapToDTO(materiel, new MaterielDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final MaterielDTO materielDTO) {
        final Materiel materiel = new Materiel();
        mapToEntity(materielDTO, materiel);
        materiel.setStatus(statusRepository.findByLibelleStatusIgnoreCase("Neuf")
                .orElseThrow(() -> new NotFoundException("Status 'Neuf' not found")));
        materiel.setEmployes(null); // Initialement, le matériel n'est pas attribué
        return materielRepository.save(materiel).getId();
    }

    public void update(final Long id, final MaterielDTO materielDTO) {
        final Materiel materiel = materielRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(materielDTO, materiel);
        materielRepository.save(materiel);
    }

    public void delete(final Long id) {
        materielRepository.deleteById(id);
    }

    private MaterielDTO mapToDTO(final Materiel materiel, final MaterielDTO materielDTO) {
        materielDTO.setId(materiel.getId());
        materielDTO.setNature(materiel.getNature());
        materielDTO.setModel(materiel.getModel());
        materielDTO.setConstructeur(materiel.getConstructeur() == null ? null : materiel.getConstructeur().getId());
        materielDTO.setFournisseur(materiel.getFournisseur() == null ? null : materiel.getFournisseur().getId());
        materielDTO.setType(materiel.getType() == null ? null : materiel.getType().getId());
        materielDTO.setStatus(materiel.getStatus() == null ? null : materiel.getStatus().getId());
        materielDTO.setEmploye(materiel.getEmployes() == null ? null : materiel.getEmployes().getId());
        materielDTO.setLivraison(materiel.getLivraisons() == null ? null : materiel.getLivraisons().getId());
        return materielDTO;
    }

    private Materiel mapToEntity(final MaterielDTO materielDTO, final Materiel materiel) {
        materiel.setNature(materielDTO.getNature());
        materiel.setModel(materielDTO.getModel());
        final Constructeur constructeur = materielDTO.getConstructeur() == null ? null : constructeurRepository.findById(materielDTO.getConstructeur())
                .orElseThrow(() -> new NotFoundException("constructeur not found"));
        materiel.setConstructeur(constructeur);
        final Fournisseur fournisseur = materielDTO.getFournisseur() == null ? null : fournisseurRepository.findById(materielDTO.getFournisseur())
                .orElseThrow(() -> new NotFoundException("fournisseur not found"));
        materiel.setFournisseur(fournisseur);
        final Type type = materielDTO.getType() == null ? null : typeRepository.findById(materielDTO.getType())
                .orElseThrow(() -> new NotFoundException("type not found"));
        materiel.setType(type);
        final Status status = materielDTO.getStatus() == null ? null : statusRepository.findById(materielDTO.getStatus())
                .orElseThrow(() -> new NotFoundException("status not found"));
        materiel.setStatus(status);
        final Employe employe = materielDTO.getEmploye() == null ? null : employeRepository.findById(materielDTO.getEmploye())
                .orElseThrow(() -> new NotFoundException("employe not found"));
        materiel.setEmployes(employe);
        final Livraison livraison = materielDTO.getLivraison() == null ? null : livraisonRepository.findById(materielDTO.getLivraison())
                .orElseThrow(() -> new NotFoundException("livraison not found"));
        materiel.setLivraisons(livraison);
        return materiel;
    }

    @Transactional
    public void attribuerMateriel(final Long materielId, final Long employeId) {
        final Materiel materiel = materielRepository.findById(materielId)
                .orElseThrow(() -> new NotFoundException("Matériel non trouvé"));

        if (materiel.getStatus() == null || !materiel.getStatus().getLibelleStatus().equals("Neuf")) {
        log.info(materiel.getModel());
        log.info(materiel.getStatus().getLibelleStatus());
        throw new IllegalStateException("Seuls les matériels neufs peuvent être attribués");
    }

        final Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new NotFoundException("Employé non trouvé"));

        materiel.setEmployes(employe);
        Status status = statusRepository.findByLibelleStatusIgnoreCase("Usagé")
                .orElseThrow(() -> new NotFoundException("Statut 'Attribué' non trouvé"));
        materiel.setStatus(status);
        materielRepository.save(materiel);
    }

    public void revoquerAttribution(final Long materielId) {
        final Materiel materiel = materielRepository.findById(materielId)
                .orElseThrow(() -> new NotFoundException("Matériel non trouvé"));

        if (materiel.getEmployes() == null) {
            throw new IllegalStateException("Ce matériel n'est pas attribué");
        }

        materiel.setEmployes(null);
        Status status = statusRepository.findByLibelleStatusIgnoreCase("Neuf")
                .orElseThrow(() -> new NotFoundException("Statut 'Attribué' non trouvé"));
        materiel.setStatus(status);
        materielRepository.save(materiel);
    }

    public void changerEtat(final Long materielId, final Long statusId) {
        final Materiel materiel = materielRepository.findById(materielId)
                .orElseThrow(() -> new NotFoundException("Matériel non trouvé"));

        final Status newStatus = statusRepository.findById(statusId)
                .orElseThrow(() -> new NotFoundException("Status non trouvé"));

        // Si le nouveau statut est "en panne", on révoque l'attribution
        if (newStatus.getLibelleStatus().equals("en panne") && materiel.getEmployes() != null) {
            materiel.setEmployes(null);
        }

        materiel.setStatus(newStatus);
        materielRepository.save(materiel);
    }

}
