package ci.scia.e_mat.employe;

import ci.scia.e_mat.util.ReferencedException;
import ci.scia.e_mat.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
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


@RestController
@RequestMapping(value = "/api/employes", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasRole('admin') or hasRole('exploitant')")  // Seul l'admin et l'exploitant peuvent accéder aux endpoints des employés
public class EmployeResource {

    private final EmployeService employeService;

    public EmployeResource(final EmployeService employeService) {
        this.employeService = employeService;
    }

    @GetMapping
    public ResponseEntity<List<EmployeDTO>> getAllEmployes() {
        return ResponseEntity.ok(employeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeDTO> getEmploye(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(employeService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createEmploye(@RequestBody @Valid final EmployeDTO employeDTO) {
        final Long createdId = employeService.create(employeDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateEmploye(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final EmployeDTO employeDTO) {
        employeService.update(id, employeDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteEmploye(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = employeService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        employeService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
