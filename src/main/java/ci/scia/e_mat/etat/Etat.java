package ci.scia.e_mat.etat;

import ci.scia.e_mat.constructeur.Constructeur;
import ci.scia.e_mat.employe.Employe;
import ci.scia.e_mat.fournisseur.Fournisseur;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "Etats")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Etat {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Long id;

    @Column(nullable = false, unique = true)
    private String libelleEtat;

    @OneToMany(mappedBy = "etat")
    private Set<Employe> employes;

    @OneToMany(mappedBy = "etat")
    private Set<Constructeur> constructeurs;

    @OneToMany(mappedBy = "etat")
    private Set<Fournisseur> fournisseurs;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
