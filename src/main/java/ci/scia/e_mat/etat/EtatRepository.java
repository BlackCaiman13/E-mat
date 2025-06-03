package ci.scia.e_mat.etat;

import org.springframework.data.jpa.repository.JpaRepository;


public interface EtatRepository extends JpaRepository<Etat, Long> {

    boolean existsByLibelleEtatIgnoreCase(String libelleEtat);

}
