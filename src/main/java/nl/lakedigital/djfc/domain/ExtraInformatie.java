package nl.lakedigital.djfc.domain;

import org.hibernate.envers.Audited;

import javax.persistence.*;

@Table(name = "EXTRAINFORMATIE")
@Entity
@Audited
public class ExtraInformatie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @JoinColumn(name="EMAIL")
    @OneToOne
    private IngaandeEmail ingaandeEmail;
    @Column(name="EMAILADRES")
    private String emailadres;
    @Column(name="NAAMAFZENDER")
    private String naamAfzender;

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

    public String getEmailadres() {
        return emailadres;
    }

    public void setEmailadres(String emailadres) {
        this.emailadres = emailadres;
    }

    public String getNaamAfzender() {
        return naamAfzender;
    }

    public void setNaamAfzender(String naamAfzender) {
        this.naamAfzender = naamAfzender;
    }
}
