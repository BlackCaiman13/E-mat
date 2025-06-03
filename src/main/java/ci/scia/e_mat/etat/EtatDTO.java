package ci.scia.e_mat.etat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class EtatDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    @EtatLibelleEtatUnique
    private String libelleEtat;

}
