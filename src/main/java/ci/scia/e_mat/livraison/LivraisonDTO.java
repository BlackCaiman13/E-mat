package ci.scia.e_mat.livraison;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LivraisonDTO {

    private Long id;

    @Schema(type = "string", example = "18:30")
    private LocalTime date;

    private Long fournisseur;

    private List<Long> materiels;

}
