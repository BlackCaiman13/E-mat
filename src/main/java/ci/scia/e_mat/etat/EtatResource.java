package ci.scia.e_mat.etat;

import ci.scia.e_mat.util.ReferencedException;
import ci.scia.e_mat.util.ReferencedWarning;
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
@RequestMapping(value = "/api/etats", produces = MediaType.APPLICATION_JSON_VALUE)
public class EtatResource {

    private final EtatService etatService;

    public EtatResource(final EtatService etatService) {
        this.etatService = etatService;
    }

    @GetMapping
    public ResponseEntity<List<EtatDTO>> getAllEtats() {
        return ResponseEntity.ok(etatService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EtatDTO> getEtat(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(etatService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createEtat(@RequestBody @Valid final EtatDTO etatDTO) {
        final Long createdId = etatService.create(etatDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateEtat(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final EtatDTO etatDTO) {
        etatService.update(id, etatDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteEtat(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = etatService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        etatService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
