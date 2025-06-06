package ci.scia.e_mat.status;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;


public interface StatusRepository extends JpaRepository<Status, Long> {

    Optional<Status> findByLibelleStatusIgnoreCase(String libelleStatus);
    boolean existsByLibelleStatusIgnoreCase(String libelleStatus);
    
    @Query("SELECT s FROM Status s JOIN FETCH s.materiels WHERE s.id = :id")
    Optional<Status> findByIdWithMateriels(@Param("id") Long id);
}
