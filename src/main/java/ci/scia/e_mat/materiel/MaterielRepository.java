package ci.scia.e_mat.materiel;

import ci.scia.e_mat.constructeur.Constructeur;
import ci.scia.e_mat.fournisseur.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MaterielRepository extends JpaRepository<Materiel, Long> {

    Materiel findFirstByConstructeur(Constructeur constructeur);

    Materiel findFirstByFournisseur(Fournisseur fournisseur);

}
