package ci.scia.e_mat.employe;

import ci.scia.e_mat.etat.Etat;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EmployeRepository extends JpaRepository<Employe, Long> {

    Employe findFirstByEtat(Etat etat);

}
