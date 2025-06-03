package ci.scia.e_mat.status;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class StatusDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    @StatusLibelleStatusUnique
    private String libelleStatus;

}
