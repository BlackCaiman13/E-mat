package ci.scia.e_mat.materiel;

import ci.scia.e_mat.constructeur.Constructeur;
import ci.scia.e_mat.constructeur.ConstructeurRepository;
import ci.scia.e_mat.employe.EmployeRepository;
import ci.scia.e_mat.fournisseur.Fournisseur;
import ci.scia.e_mat.fournisseur.FournisseurRepository;
import ci.scia.e_mat.livraison.LivraisonRepository;
import ci.scia.e_mat.status.Status;
import ci.scia.e_mat.status.StatusRepository;
import ci.scia.e_mat.type.Type;
import ci.scia.e_mat.type.TypeRepository;
import ci.scia.e_mat.util.NotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class MaterielService {

    private final MaterielRepository materielRepository;
    private final ConstructeurRepository constructeurRepository;
    private final FournisseurRepository fournisseurRepository;
    private final TypeRepository typeRepository;
    private final StatusRepository statusRepository;
    private final EmployeRepository employeRepository;
    private final LivraisonRepository livraisonRepository;

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
        return materielRepository.save(materiel).getId();
    }

    public void update(final Long id, final MaterielDTO materielDTO) {
        final Materiel materiel = materielRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(materielDTO, materiel);
        materielRepository.save(materiel);
    }

    public void delete(final Long id) {
        final Materiel materiel = materielRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        employeRepository.findAllByMateriels(materiel)
                .forEach(employe -> employe.getMateriels().remove(materiel));
        livraisonRepository.findAllByMateriels(materiel)
                .forEach(livraison -> livraison.getMateriels().remove(materiel));
        materielRepository.delete(materiel);
    }

    private MaterielDTO mapToDTO(final Materiel materiel, final MaterielDTO materielDTO) {
        materielDTO.setId(materiel.getId());
        materielDTO.setNature(materiel.getNature());
        materielDTO.setModel(materiel.getModel());
        materielDTO.setConstructeur(materiel.getConstructeur() == null ? null : materiel.getConstructeur().getId());
        materielDTO.setFournisseur(materiel.getFournisseur() == null ? null : materiel.getFournisseur().getId());
        materielDTO.setType(materiel.getType() == null ? null : materiel.getType().getId());
        materielDTO.setStatus(materiel.getStatus() == null ? null : materiel.getStatus().getId());
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
        return materiel;
    }

}
