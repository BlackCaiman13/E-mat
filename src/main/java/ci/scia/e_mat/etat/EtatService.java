package ci.scia.e_mat.etat;

import ci.scia.e_mat.employe.Employe;
import ci.scia.e_mat.employe.EmployeRepository;
import ci.scia.e_mat.fournisseur.Fournisseur;
import ci.scia.e_mat.fournisseur.FournisseurRepository;
import ci.scia.e_mat.util.NotFoundException;
import ci.scia.e_mat.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class EtatService {

    private final EtatRepository etatRepository;
    private final EmployeRepository employeRepository;
    private final FournisseurRepository fournisseurRepository;

    public EtatService(final EtatRepository etatRepository,
            final EmployeRepository employeRepository,
            final FournisseurRepository fournisseurRepository) {
        this.etatRepository = etatRepository;
        this.employeRepository = employeRepository;
        this.fournisseurRepository = fournisseurRepository;
    }

    public List<EtatDTO> findAll() {
        final List<Etat> etats = etatRepository.findAll(Sort.by("id"));
        return etats.stream()
                .map(etat -> mapToDTO(etat, new EtatDTO()))
                .toList();
    }

    public EtatDTO get(final Long id) {
        return etatRepository.findById(id)
                .map(etat -> mapToDTO(etat, new EtatDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final EtatDTO etatDTO) {
        final Etat etat = new Etat();
        mapToEntity(etatDTO, etat);
        return etatRepository.save(etat).getId();
    }

    public void update(final Long id, final EtatDTO etatDTO) {
        final Etat etat = etatRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(etatDTO, etat);
        etatRepository.save(etat);
    }

    public void delete(final Long id) {
        etatRepository.deleteById(id);
    }

    private EtatDTO mapToDTO(final Etat etat, final EtatDTO etatDTO) {
        etatDTO.setId(etat.getId());
        etatDTO.setLibelleEtat(etat.getLibelleEtat());
        return etatDTO;
    }

    private Etat mapToEntity(final EtatDTO etatDTO, final Etat etat) {
        etat.setLibelleEtat(etatDTO.getLibelleEtat());
        return etat;
    }

    public boolean libelleEtatExists(final String libelleEtat) {
        return etatRepository.existsByLibelleEtatIgnoreCase(libelleEtat);
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Etat etat = etatRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Employe etatEmploye = employeRepository.findFirstByEtat(etat);
        if (etatEmploye != null) {
            referencedWarning.setKey("etat.employe.etat.referenced");
            referencedWarning.addParam(etatEmploye.getId());
            return referencedWarning;
        }
        final Fournisseur etatFournisseur = fournisseurRepository.findFirstByEtat(etat);
        if (etatFournisseur != null) {
            referencedWarning.setKey("etat.fournisseur.etat.referenced");
            referencedWarning.addParam(etatFournisseur.getId());
            return referencedWarning;
        }
        return null;
    }

}
