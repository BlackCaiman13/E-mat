package ci.scia.e_mat.employe;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class EmployeDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String nomEmploye;

    @NotNull
    @Size(max = 255)
    private String prenomEmploye;

    private Long etat;

    private List<Long> materiels;

}
