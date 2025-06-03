package ci.scia.e_mat.fournisseur;

import ci.scia.e_mat.etat.Etat;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FournisseurRepository extends JpaRepository<Fournisseur, Long> {

    Fournisseur findFirstByEtat(Etat etat);

}
