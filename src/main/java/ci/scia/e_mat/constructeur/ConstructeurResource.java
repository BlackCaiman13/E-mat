package ci.scia.e_mat.constructeur;

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
@RequestMapping(value = "/api/constructeurs", produces = MediaType.APPLICATION_JSON_VALUE)
public class ConstructeurResource {

    private final ConstructeurService constructeurService;

    public ConstructeurResource(final ConstructeurService constructeurService) {
        this.constructeurService = constructeurService;
    }

    @GetMapping
    public ResponseEntity<List<ConstructeurDTO>> getAllConstructeurs() {
        return ResponseEntity.ok(constructeurService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConstructeurDTO> getConstructeur(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(constructeurService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createConstructeur(
            @RequestBody @Valid final ConstructeurDTO constructeurDTO) {
        final Long createdId = constructeurService.create(constructeurDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateConstructeur(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final ConstructeurDTO constructeurDTO) {
        constructeurService.update(id, constructeurDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteConstructeur(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = constructeurService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        constructeurService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
