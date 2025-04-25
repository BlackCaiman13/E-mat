package ci.scia.e_mat.fournisseur;

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
@RequestMapping(value = "/api/fournisseurs", produces = MediaType.APPLICATION_JSON_VALUE)
public class FournisseurResource {

    private final FournisseurService fournisseurService;

    public FournisseurResource(final FournisseurService fournisseurService) {
        this.fournisseurService = fournisseurService;
    }

    @GetMapping
    public ResponseEntity<List<FournisseurDTO>> getAllFournisseurs() {
        return ResponseEntity.ok(fournisseurService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FournisseurDTO> getFournisseur(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(fournisseurService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createFournisseur(
            @RequestBody @Valid final FournisseurDTO fournisseurDTO) {
        final Long createdId = fournisseurService.create(fournisseurDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateFournisseur(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final FournisseurDTO fournisseurDTO) {
        fournisseurService.update(id, fournisseurDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteFournisseur(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = fournisseurService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        fournisseurService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
