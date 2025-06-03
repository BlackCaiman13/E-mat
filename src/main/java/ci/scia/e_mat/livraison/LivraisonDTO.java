package ci.scia.e_mat.livraison;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LivraisonDTO {

    private Long id;

    @Schema(type = "string", example = "2024-06-10T18:30:00")
    private LocalDateTime dateHeure;

}
