package ci.scia.e_mat.materiel;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MaterielDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String nature;

    @NotNull
    @Size(max = 255)
    private String model;

    private Long constructeur;

    private Long fournisseur;

    private Long type;

    private Long status;

}
