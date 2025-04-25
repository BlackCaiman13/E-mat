package ci.scia.e_mat.utilisateur;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UtilisateurDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String nomUtilisateur;

    @NotNull
    @Size(max = 255)
    private String mdp;

    private Long role;

}
