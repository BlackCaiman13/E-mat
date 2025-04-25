package ci.scia.e_mat.fournisseur;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FournisseurDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String nomFournisseur;

    @NotNull
    @Size(max = 50)
    private String codeFournisseur;

    private Long etat;

}
