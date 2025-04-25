package ci.scia.e_mat.fournisseur;

import ci.scia.e_mat.etat.Etat;
import ci.scia.e_mat.livraison.Livraison;
import ci.scia.e_mat.materiel.Materiel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "Fournisseurs")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Fournisseur {

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

    @Column(nullable = false)
    private String nomFournisseur;

    @Column(nullable = false, length = 50)
    private String codeFournisseur;

    @OneToMany(mappedBy = "fournisseur")
    private Set<Materiel> materiels;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etat_id")
    private Etat etat;

    @OneToMany(mappedBy = "fournisseur")
    private Set<Livraison> livraisons;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
