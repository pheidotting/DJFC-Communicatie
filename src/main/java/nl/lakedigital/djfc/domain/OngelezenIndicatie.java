package nl.lakedigital.djfc.domain;

import org.hibernate.envers.Audited;

import javax.persistence.*;

@Table(name = "ONGELEZENINDICATIE")
@Entity
@Audited
public class OngelezenIndicatie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @JoinColumn(name="EMAIL")
    @OneToOne
    private IngaandeEmail ingaandeEmail;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IngaandeEmail getIngaandeEmail() {
        return ingaandeEmail;
    }

    public void setIngaandeEmail(IngaandeEmail ingaandeEmail) {
        this.ingaandeEmail = ingaandeEmail;
    }
}
