package ci.scia.e_mat.status;

import ci.scia.e_mat.materiel.Materiel;
import ci.scia.e_mat.materiel.MaterielRepository;
import ci.scia.e_mat.util.NotFoundException;
import ci.scia.e_mat.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class StatusService {

    private final StatusRepository statusRepository;
    private final MaterielRepository materielRepository;

    public StatusService(final StatusRepository statusRepository,
            final MaterielRepository materielRepository) {
        this.statusRepository = statusRepository;
        this.materielRepository = materielRepository;
    }

    public List<StatusDTO> findAll() {
        final List<Status> statuses = statusRepository.findAll(Sort.by("id"));
        return statuses.stream()
                .map(status -> mapToDTO(status, new StatusDTO()))
                .toList();
    }

    public StatusDTO get(final Long id) {
        return statusRepository.findById(id)
                .map(status -> mapToDTO(status, new StatusDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final StatusDTO statusDTO) {
        final Status status = new Status();
        mapToEntity(statusDTO, status);
        return statusRepository.save(status).getId();
    }

    public void update(final Long id, final StatusDTO statusDTO) {
        final Status status = statusRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(statusDTO, status);
        statusRepository.save(status);
    }

    public void delete(final Long id) {
        statusRepository.deleteById(id);
    }

    private StatusDTO mapToDTO(final Status status, final StatusDTO statusDTO) {
        statusDTO.setId(status.getId());
        statusDTO.setLibelleStatus(status.getLibelleStatus());
        return statusDTO;
    }

    private Status mapToEntity(final StatusDTO statusDTO, final Status status) {
        status.setLibelleStatus(statusDTO.getLibelleStatus());
        return status;
    }

    public boolean libelleStatusExists(final String libelleStatus) {
        return statusRepository.existsByLibelleStatusIgnoreCase(libelleStatus);
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Status status = statusRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Materiel statusMateriel = materielRepository.findFirstByStatus(status);
        if (statusMateriel != null) {
            referencedWarning.setKey("status.materiel.status.referenced");
            referencedWarning.addParam(statusMateriel.getId());
            return referencedWarning;
        }
        return null;
    }

}
