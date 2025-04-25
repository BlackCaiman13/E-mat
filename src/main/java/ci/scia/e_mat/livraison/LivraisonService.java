package ci.scia.e_mat.livraison;

import ci.scia.e_mat.fournisseur.Fournisseur;
import ci.scia.e_mat.fournisseur.FournisseurRepository;
import ci.scia.e_mat.materiel.Materiel;
import ci.scia.e_mat.materiel.MaterielRepository;
import ci.scia.e_mat.util.NotFoundException;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class LivraisonService {

    private final LivraisonRepository livraisonRepository;
    private final FournisseurRepository fournisseurRepository;
    private final MaterielRepository materielRepository;

    public LivraisonService(final LivraisonRepository livraisonRepository,
            final FournisseurRepository fournisseurRepository,
            final MaterielRepository materielRepository) {
        this.livraisonRepository = livraisonRepository;
        this.fournisseurRepository = fournisseurRepository;
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
        livraisonDTO.setDate(livraison.getDate());
        livraisonDTO.setFournisseur(livraison.getFournisseur() == null ? null : livraison.getFournisseur().getId());
        livraisonDTO.setMateriels(livraison.getMateriels().stream()
                .map(materiel -> materiel.getId())
                .toList());
        return livraisonDTO;
    }

    private Livraison mapToEntity(final LivraisonDTO livraisonDTO, final Livraison livraison) {
        livraison.setDate(livraisonDTO.getDate());
        final Fournisseur fournisseur = livraisonDTO.getFournisseur() == null ? null : fournisseurRepository.findById(livraisonDTO.getFournisseur())
                .orElseThrow(() -> new NotFoundException("fournisseur not found"));
        livraison.setFournisseur(fournisseur);
        final List<Materiel> materiels = materielRepository.findAllById(
                livraisonDTO.getMateriels() == null ? List.of() : livraisonDTO.getMateriels());
        if (materiels.size() != (livraisonDTO.getMateriels() == null ? 0 : livraisonDTO.getMateriels().size())) {
            throw new NotFoundException("one of materiels not found");
        }
        livraison.setMateriels(new HashSet<>(materiels));
        return livraison;
    }

}
