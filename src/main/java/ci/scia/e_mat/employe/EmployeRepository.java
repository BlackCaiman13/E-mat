package ci.scia.e_mat.employe;

import ci.scia.e_mat.materiel.Materiel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EmployeRepository extends JpaRepository<Employe, Long> {

    Employe findFirstByMateriels(Materiel materiel);

    List<Employe> findAllByMateriels(Materiel materiel);

}
