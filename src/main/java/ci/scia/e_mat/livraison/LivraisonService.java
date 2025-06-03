package ci.scia.e_mat.livraison;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import ci.scia.e_mat.materiel.Materiel;
import ci.scia.e_mat.materiel.MaterielRepository;
import ci.scia.e_mat.util.NotFoundException;
import ci.scia.e_mat.util.ReferencedWarning;


@Service
public class LivraisonService {

    private final LivraisonRepository livraisonRepository;
    private final MaterielRepository materielRepository;

    public LivraisonService(final LivraisonRepository livraisonRepository,
            final MaterielRepository materielRepository) {
        this.livraisonRepository = livraisonRepository;
        this.materielRepository = materielRepository;
    }

    public List<LivraisonDTO> findAll() {
        final List<Livraison> livraisons = livraisonRepository.findAll(Sort.by("id"));
        return livraisons.stream()
                .map(livraison -> mapToDTO(livraison, new LivraisonDTO()))
                .toList();
    }

    public LivraisonDTO get(final Long id) {
        return livraisonRepository.findById(id)
                .map(livraison -> mapToDTO(livraison, new LivraisonDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final LivraisonDTO livraisonDTO) {
        final Livraison livraison = new Livraison();
        mapToEntity(livraisonDTO, livraison);
        return livraisonRepository.save(livraison).getId();
    }

    public void update(final Long id, final LivraisonDTO livraisonDTO) {
        final Livraison livraison = livraisonRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(livraisonDTO, livraison);
        livraisonRepository.save(livraison);
    }

    public void delete(final Long id) {
        livraisonRepository.deleteById(id);
    }

    private LivraisonDTO mapToDTO(final Livraison livraison, final LivraisonDTO livraisonDTO) {
        livraisonDTO.setId(livraison.getId());
        livraisonDTO.setDateHeure(livraison.getDate());
        return livraisonDTO;
    }

    private Livraison mapToEntity(final LivraisonDTO livraisonDTO, final Livraison livraison) {
        livraison.setDate(livraisonDTO.getDateHeure());
        return livraison;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Livraison livraison = livraisonRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Materiel livraisonsMateriel = materielRepository.findFirstByLivraisons(livraison);
        if (livraisonsMateriel != null) {
            referencedWarning.setKey("livraison.materiel.livraisons.referenced");
            referencedWarning.addParam(livraisonsMateriel.getId());
            return referencedWarning;
        }
        return null;
    }

}
