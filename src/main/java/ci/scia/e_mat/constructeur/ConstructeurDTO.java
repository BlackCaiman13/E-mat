package ci.scia.e_mat.constructeur;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ConstructeurDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String nomConstructeur;

    @NotNull
    @Size(max = 50)
    private String codeConstructeur;

}
