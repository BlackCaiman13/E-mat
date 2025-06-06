package ci.scia.e_mat.materiel;

import ci.scia.e_mat.constructeur.Constructeur;
import ci.scia.e_mat.employe.Employe;
import ci.scia.e_mat.fournisseur.Fournisseur;
import ci.scia.e_mat.livraison.Livraison;
import ci.scia.e_mat.status.Status;
import ci.scia.e_mat.type.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterielRepository extends JpaRepository<Materiel, Long> {

    Materiel findFirstByConstructeur(Constructeur constructeur);

    Materiel findFirstByFournisseur(Fournisseur fournisseur);

    Materiel findFirstByType(Type type);

    Materiel findFirstByStatus(Status status);

    Materiel findFirstByEmployes(Employe employe);

    Materiel findFirstByLivraisons(Livraison livraison);

    List<Materiel> findTop10ByOrderByLastUpdatedDesc();

    // Statistiques par fournisseur
    long countByFournisseur_Id(Long fournisseurId);
    
    long countByFournisseur_IdAndStatus(Long fournisseurId, Status status);
    
    List<Materiel> findByFournisseur_IdAndDateCreatedBetween(Long fournisseurId, OffsetDateTime start, OffsetDateTime end);

    List<Materiel> findByFournisseur(Fournisseur fournisseur);
    
    @Query("SELECT COUNT(m) FROM Materiel m WHERE m.status = :status")
    long countByStatus(@Param("status") Status status);
    
    @Query("SELECT COUNT(m) FROM Materiel m WHERE m.status = :status AND m.dateCreated BETWEEN :start AND :end")
    long countByStatusAndDateCreatedBetween(
        @Param("status") Status status, 
        @Param("start") OffsetDateTime start, 
        @Param("end") OffsetDateTime end
    );
}
