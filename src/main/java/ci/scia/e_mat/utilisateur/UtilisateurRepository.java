package ci.scia.e_mat.utilisateur;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    // Méthode pour trouver un utilisateur par son nom d'utilisateur
    Optional<Utilisateur> findByNomUtilisateur(String nomUtilisateur);

   
}
