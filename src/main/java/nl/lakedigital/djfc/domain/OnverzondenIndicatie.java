package nl.lakedigital.djfc.domain;

import org.hibernate.envers.Audited;

import javax.persistence.*;

@Audited
@Entity
@Table(name = "ONVERZONDENINDICATIE")
public class OnverzondenIndicatie {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "ID")
        private Long id;
        @OneToOne(optional = false)
        @JoinColumn(name="UITGAANDEEMAIL")
        private UitgaandeEmail uitgaandeEmail;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UitgaandeEmail getUitgaandeEmail() {
        return uitgaandeEmail;
    }

    public void setUitgaandeEmail(UitgaandeEmail uitgaandeEmail) {
        this.uitgaandeEmail = uitgaandeEmail;
    }
}
