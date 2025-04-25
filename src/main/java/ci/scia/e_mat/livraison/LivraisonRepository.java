package ci.scia.e_mat.livraison;

import ci.scia.e_mat.fournisseur.Fournisseur;
import ci.scia.e_mat.materiel.Materiel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LivraisonRepository extends JpaRepository<Livraison, Long> {

    Livraison findFirstByFournisseur(Fournisseur fournisseur);

    Livraison findFirstByMateriels(Materiel materiel);

    List<Livraison> findAllByMateriels(Materiel materiel);

}
