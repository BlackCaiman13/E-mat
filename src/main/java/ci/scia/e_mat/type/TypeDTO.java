package ci.scia.e_mat.type;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TypeDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    @TypeLibelleTypeUnique
    private String libelleType;

}
