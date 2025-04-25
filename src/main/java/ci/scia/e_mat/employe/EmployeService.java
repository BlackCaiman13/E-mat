package ci.scia.e_mat.employe;

import ci.scia.e_mat.etat.Etat;
import ci.scia.e_mat.etat.EtatRepository;
import ci.scia.e_mat.materiel.Materiel;
import ci.scia.e_mat.materiel.MaterielRepository;
import ci.scia.e_mat.util.NotFoundException;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class EmployeService {

    private final EmployeRepository employeRepository;
    private final EtatRepository etatRepository;
    private final MaterielRepository materielRepository;

    public EmployeService(final EmployeRepository employeRepository,
            final EtatRepository etatRepository, final MaterielRepository materielRepository) {
        this.employeRepository = employeRepository;
        this.etatRepository = etatRepository;
        this.materielRepository = materielRepository;
    }

    public List<EmployeDTO> findAll() {
        final List<Employe> employes = employeRepository.findAll(Sort.by("id"));
        return employes.stream()
                .map(employe -> mapToDTO(employe, new EmployeDTO()))
                .toList();
    }

    public EmployeDTO get(final Long id) {
        return employeRepository.findById(id)
                .map(employe -> mapToDTO(employe, new EmployeDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final EmployeDTO employeDTO) {
        final Employe employe = new Employe();
        mapToEntity(employeDTO, employe);
        return employeRepository.save(employe).getId();
    }

    public void update(final Long id, final EmployeDTO employeDTO) {
        final Employe employe = employeRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(employeDTO, employe);
        employeRepository.save(employe);
    }

    public void delete(final Long id) {
        employeRepository.deleteById(id);
    }

    private EmployeDTO mapToDTO(final Employe employe, final EmployeDTO employeDTO) {
        employeDTO.setId(employe.getId());
        employeDTO.setNomEmploye(employe.getNomEmploye());
        employeDTO.setPrenomEmploye(employe.getPrenomEmploye());
        employeDTO.setEtat(employe.getEtat() == null ? null : employe.getEtat().getId());
        employeDTO.setMateriels(employe.getMateriels().stream()
                .map(materiel -> materiel.getId())
                .toList());
        return employeDTO;
    }

    private Employe mapToEntity(final EmployeDTO employeDTO, final Employe employe) {
        employe.setNomEmploye(employeDTO.getNomEmploye());
        employe.setPrenomEmploye(employeDTO.getPrenomEmploye());
        final Etat etat = employeDTO.getEtat() == null ? null : etatRepository.findById(employeDTO.getEtat())
                .orElseThrow(() -> new NotFoundException("etat not found"));
        employe.setEtat(etat);
        final List<Materiel> materiels = materielRepository.findAllById(
                employeDTO.getMateriels() == null ? List.of() : employeDTO.getMateriels());
        if (materiels.size() != (employeDTO.getMateriels() == null ? 0 : employeDTO.getMateriels().size())) {
            throw new NotFoundException("one of materiels not found");
        }
        employe.setMateriels(new HashSet<>(materiels));
        return employe;
    }

}
