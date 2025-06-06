package ci.scia.e_mat.livraison;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ci.scia.e_mat.util.ReferencedException;
import ci.scia.e_mat.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;


@RestController
@RequestMapping(value = "/api/livraisons", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasRole('admin') or hasRole('exploitant')")
public class LivraisonResource {

    private final LivraisonService livraisonService;

    public LivraisonResource(final LivraisonService livraisonService) {
        this.livraisonService = livraisonService;
    }

    @GetMapping
    public ResponseEntity<List<LivraisonDTO>> getAllLivraisons() {
        return ResponseEntity.ok(livraisonService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivraisonDTO> getLivraison(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(livraisonService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createLivraison(
            @RequestBody @Valid final LivraisonDTO livraisonDTO) {
        final Long createdId = livraisonService.create(livraisonDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateLivraison(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final LivraisonDTO livraisonDTO) {
        livraisonService.update(id, livraisonDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteLivraison(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = livraisonService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        livraisonService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
