package ci.scia.e_mat.fournisseur;

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
public class FournisseurService {

    private final FournisseurRepository fournisseurRepository;
    private final EtatRepository etatRepository;
    private final MaterielRepository materielRepository;

    public FournisseurService(final FournisseurRepository fournisseurRepository,
            final EtatRepository etatRepository, final MaterielRepository materielRepository) {
        this.fournisseurRepository = fournisseurRepository;
        this.etatRepository = etatRepository;
        this.materielRepository = materielRepository;
    }

    public List<FournisseurDTO> findAll() {
        final List<Fournisseur> fournisseurs = fournisseurRepository.findAll(Sort.by("id"));
        return fournisseurs.stream()
                .map(fournisseur -> mapToDTO(fournisseur, new FournisseurDTO()))
                .toList();
    }

    public FournisseurDTO get(final Long id) {
        return fournisseurRepository.findById(id)
                .map(fournisseur -> mapToDTO(fournisseur, new FournisseurDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final FournisseurDTO fournisseurDTO) {
        final Fournisseur fournisseur = new Fournisseur();
        mapToEntity(fournisseurDTO, fournisseur);
        return fournisseurRepository.save(fournisseur).getId();
    }

    public void update(final Long id, final FournisseurDTO fournisseurDTO) {
        final Fournisseur fournisseur = fournisseurRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(fournisseurDTO, fournisseur);
        fournisseurRepository.save(fournisseur);
    }

    public void delete(final Long id) {
        fournisseurRepository.deleteById(id);
    }

    private FournisseurDTO mapToDTO(final Fournisseur fournisseur,
            final FournisseurDTO fournisseurDTO) {
        fournisseurDTO.setId(fournisseur.getId());
        fournisseurDTO.setNomFournisseur(fournisseur.getNomFournisseur());
        fournisseurDTO.setCodeFournisseur(fournisseur.getCodeFournisseur());
        fournisseurDTO.setEtat(fournisseur.getEtat() == null ? null : fournisseur.getEtat().getId());
        return fournisseurDTO;
    }

    private Fournisseur mapToEntity(final FournisseurDTO fournisseurDTO,
            final Fournisseur fournisseur) {
        fournisseur.setNomFournisseur(fournisseurDTO.getNomFournisseur());
        fournisseur.setCodeFournisseur(fournisseurDTO.getCodeFournisseur());
        final Etat etat = fournisseurDTO.getEtat() == null ? null : etatRepository.findById(fournisseurDTO.getEtat())
                .orElseThrow(() -> new NotFoundException("etat not found"));
        fournisseur.setEtat(etat);
        return fournisseur;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Fournisseur fournisseur = fournisseurRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Materiel fournisseurMateriel = materielRepository.findFirstByFournisseur(fournisseur);
        if (fournisseurMateriel != null) {
            referencedWarning.setKey("fournisseur.materiel.fournisseur.referenced");
            referencedWarning.addParam(fournisseurMateriel.getId());
            return referencedWarning;
        }
        return null;
    }

}
