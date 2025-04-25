package ci.scia.e_mat.utilisateur;

import ci.scia.e_mat.role.Role;
import ci.scia.e_mat.role.RoleRepository;
import ci.scia.e_mat.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurService implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;

    public UtilisateurService(final UtilisateurRepository utilisateurRepository,
                              final RoleRepository roleRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.roleRepository = roleRepository;
    }

    // Méthode pour charger un utilisateur par son nom d'utilisateur
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return utilisateurRepository.findByNomUtilisateur(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + username));
    }

    public List<UtilisateurDTO> findAll() {
        final List<Utilisateur> utilisateurs = utilisateurRepository.findAll(Sort.by("id"));
        return utilisateurs.stream()
                .map(utilisateur -> mapToDTO(utilisateur, new UtilisateurDTO()))
                .toList();
    }

    public UtilisateurDTO get(final Long id) {
        return utilisateurRepository.findById(id)
                .map(utilisateur -> mapToDTO(utilisateur, new UtilisateurDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final UtilisateurDTO utilisateurDTO) {
        final Utilisateur utilisateur = new Utilisateur();
        mapToEntity(utilisateurDTO, utilisateur);
        return utilisateurRepository.save(utilisateur).getId();
    }

    public void update(final Long id, final UtilisateurDTO utilisateurDTO) {
        final Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(utilisateurDTO, utilisateur);
        utilisateurRepository.save(utilisateur);
    }

    public void delete(final Long id) {
        utilisateurRepository.deleteById(id);
    }

    private UtilisateurDTO mapToDTO(final Utilisateur utilisateur,
                                    final UtilisateurDTO utilisateurDTO) {
        utilisateurDTO.setId(utilisateur.getId());
        utilisateurDTO.setNomUtilisateur(utilisateur.getNomUtilisateur());
        utilisateurDTO.setMdp(utilisateur.getMdp());
        utilisateurDTO.setRole(utilisateur.getRole() == null ? null : utilisateur.getRole().getId());
        return utilisateurDTO;
    }

    private Utilisateur mapToEntity(final UtilisateurDTO utilisateurDTO,
                                    final Utilisateur utilisateur) {
        utilisateur.setNomUtilisateur(utilisateurDTO.getNomUtilisateur());
        utilisateur.setMdp(utilisateurDTO.getMdp());
        final Role role = utilisateurDTO.getRole() == null ? null : roleRepository.findById(utilisateurDTO.getRole())
                .orElseThrow(() -> new NotFoundException("role not found"));
        utilisateur.setRole(role);
        return utilisateur;
    }
}
