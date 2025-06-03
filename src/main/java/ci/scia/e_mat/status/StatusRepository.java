package ci.scia.e_mat.status;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface StatusRepository extends JpaRepository<Status, Long> {

    boolean existsByLibelleStatusIgnoreCase(String libelleStatus);
    Optional<Status> findByLibelleStatusIgnoreCase(String libelleStatus);

}
