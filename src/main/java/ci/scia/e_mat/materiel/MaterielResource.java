package ci.scia.e_mat.materiel;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/materiels", produces = MediaType.APPLICATION_JSON_VALUE)
public class MaterielResource {

    private final MaterielService materielService;

    public MaterielResource(final MaterielService materielService) {
        this.materielService = materielService;
    }

    @GetMapping
    public ResponseEntity<List<MaterielDTO>> getAllMateriels() {
        return ResponseEntity.ok(materielService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterielDTO> getMateriel(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(materielService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createMateriel(@RequestBody @Valid final MaterielDTO materielDTO) {
        final Long createdId = materielService.create(materielDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateMateriel(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final MaterielDTO materielDTO) {
        materielService.update(id, materielDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteMateriel(@PathVariable(name = "id") final Long id) {
        materielService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
