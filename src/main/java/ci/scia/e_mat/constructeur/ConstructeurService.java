package ci.scia.e_mat.constructeur;

import ci.scia.e_mat.etat.Etat;
import ci.scia.e_mat.etat.EtatRepository;
import ci.scia.e_mat.materiel.Materiel;
import ci.scia.e_mat.materiel.MaterielRepository;
import ci.scia.e_mat.util.NotFoundException;
import ci.scia.e_mat.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ConstructeurService {

    private final ConstructeurRepository constructeurRepository;
    private final EtatRepository etatRepository;
    private final MaterielRepository materielRepository;

    public ConstructeurService(final ConstructeurRepository constructeurRepository,
            final EtatRepository etatRepository, final MaterielRepository materielRepository) {
        this.constructeurRepository = constructeurRepository;
        this.etatRepository = etatRepository;
        this.materielRepository = materielRepository;
    }

    public List<ConstructeurDTO> findAll() {
        final List<Constructeur> constructeurs = constructeurRepository.findAll(Sort.by("id"));
        return constructeurs.stream()
                .map(constructeur -> mapToDTO(constructeur, new ConstructeurDTO()))
                .toList();
    }

    public ConstructeurDTO get(final Long id) {
        return constructeurRepository.findById(id)
                .map(constructeur -> mapToDTO(constructeur, new ConstructeurDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ConstructeurDTO constructeurDTO) {
        final Constructeur constructeur = new Constructeur();
        mapToEntity(constructeurDTO, constructeur);
        return constructeurRepository.save(constructeur).getId();
    }

    public void update(final Long id, final ConstructeurDTO constructeurDTO) {
        final Constructeur constructeur = constructeurRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(constructeurDTO, constructeur);
        constructeurRepository.save(constructeur);
    }

    public void delete(final Long id) {
        constructeurRepository.deleteById(id);
    }

    private ConstructeurDTO mapToDTO(final Constructeur constructeur,
            final ConstructeurDTO constructeurDTO) {
        constructeurDTO.setId(constructeur.getId());
        constructeurDTO.setNomConstructeur(constructeur.getNomConstructeur());
        constructeurDTO.setCodeConstructeur(constructeur.getCodeConstructeur());
        constructeurDTO.setEtat(constructeur.getEtat() == null ? null : constructeur.getEtat().getId());
        return constructeurDTO;
    }

    private Constructeur mapToEntity(final ConstructeurDTO constructeurDTO,
            final Constructeur constructeur) {
        constructeur.setNomConstructeur(constructeurDTO.getNomConstructeur());
        constructeur.setCodeConstructeur(constructeurDTO.getCodeConstructeur());
        final Etat etat = constructeurDTO.getEtat() == null ? null : etatRepository.findById(constructeurDTO.getEtat())
                .orElseThrow(() -> new NotFoundException("etat not found"));
        constructeur.setEtat(etat);
        return constructeur;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Constructeur constructeur = constructeurRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Materiel constructeurMateriel = materielRepository.findFirstByConstructeur(constructeur);
        if (constructeurMateriel != null) {
            referencedWarning.setKey("constructeur.materiel.constructeur.referenced");
            referencedWarning.addParam(constructeurMateriel.getId());
            return referencedWarning;
        }
        return null;
    }

}
