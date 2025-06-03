package ci.scia.e_mat.type;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import ci.scia.e_mat.materiel.Materiel;
import ci.scia.e_mat.materiel.MaterielRepository;
import ci.scia.e_mat.util.NotFoundException;
import ci.scia.e_mat.util.ReferencedWarning;


@Service
public class TypeService {

    private final TypeRepository typeRepository;
    private final MaterielRepository materielRepository;

    public TypeService(final TypeRepository typeRepository,
            final MaterielRepository materielRepository) {
        this.typeRepository = typeRepository;
        this.materielRepository = materielRepository;
    }

    public List<TypeDTO> findAll() {
        final List<Type> types = typeRepository.findAll(Sort.by("id"));
        return types.stream()
                .map(type -> mapToDTO(type, new TypeDTO()))
                .toList();
    }

    public TypeDTO get(final Long id) {
        return typeRepository.findById(id)
                .map(type -> mapToDTO(type, new TypeDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final TypeDTO typeDTO) {
        final Type type = new Type();
        mapToEntity(typeDTO, type);
        return typeRepository.save(type).getId();
    }

    public void update(final Long id, final TypeDTO typeDTO) {
        final Type type = typeRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(typeDTO, type);
        typeRepository.save(type);
    }

    public void delete(final Long id) {
        typeRepository.deleteById(id);
    }

    private TypeDTO mapToDTO(final Type type, final TypeDTO typeDTO) {
        typeDTO.setId(type.getId());
        typeDTO.setLibelleType(type.getLibelleType());
        return typeDTO;
    }

    private Type mapToEntity(final TypeDTO typeDTO, final Type type) {
        type.setLibelleType(typeDTO.getLibelleType());
        return type;
    }

    public boolean libelleTypeExists(final String libelleType) {
        return typeRepository.existsByLibelleTypeIgnoreCase(libelleType);
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Type type = typeRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Materiel typeMateriel = materielRepository.findFirstByType(type);
        if (typeMateriel != null) {
            referencedWarning.setKey("type.materiel.type.referenced");
            referencedWarning.addParam(typeMateriel.getId());
            return referencedWarning;
        }
        return null;
    }

}
