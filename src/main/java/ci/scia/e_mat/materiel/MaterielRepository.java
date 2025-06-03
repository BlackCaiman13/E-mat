package ci.scia.e_mat.materiel;

import ci.scia.e_mat.constructeur.Constructeur;
import ci.scia.e_mat.employe.Employe;
import ci.scia.e_mat.fournisseur.Fournisseur;
import ci.scia.e_mat.livraison.Livraison;
import ci.scia.e_mat.status.Status;
import ci.scia.e_mat.type.Type;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MaterielRepository extends JpaRepository<Materiel, Long> {

    Materiel findFirstByConstructeur(Constructeur constructeur);

    Materiel findFirstByFournisseur(Fournisseur fournisseur);

    Materiel findFirstByType(Type type);

    Materiel findFirstByStatus(Status status);

    Materiel findFirstByEmployes(Employe employe);

    Materiel findFirstByLivraisons(Livraison livraison);

}
