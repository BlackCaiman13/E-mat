package ci.scia.e_mat.status;

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
@RequestMapping(value = "/api/statuses", produces = MediaType.APPLICATION_JSON_VALUE)
public class StatusResource {

    private final StatusService statusService;

    public StatusResource(final StatusService statusService) {
        this.statusService = statusService;
    }


    @PreAuthorize("hasRole('admin') or hasRole('exploitant') or hasRole('directeur')")
    @GetMapping
    public ResponseEntity<List<StatusDTO>> getAllStatuses() {
        return ResponseEntity.ok(statusService.findAll());
    }

    @PreAuthorize("hasRole('admin') or hasRole('exploitant') or hasRole('directeur')")
    @GetMapping("/{id}")
    public ResponseEntity<StatusDTO> getStatus(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(statusService.get(id));
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createStatus(@RequestBody @Valid final StatusDTO statusDTO) {
        final Long createdId = statusService.create(statusDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('admin')")
    @PutMapping("/{id}")
    public ResponseEntity<Long> updateStatus(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final StatusDTO statusDTO) {
        statusService.update(id, statusDTO);
        return ResponseEntity.ok(id);
    }

    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteStatus(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = statusService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        statusService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
