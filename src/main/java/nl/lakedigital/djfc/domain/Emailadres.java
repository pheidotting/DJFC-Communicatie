package nl.lakedigital.djfc.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Audited
@Entity
@Table(name = "EMAILADRES")
public class Emailadres {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "EMAILADRES")
    private String emailadres;
    @OneToOne(optional = false)
    @JoinColumn(name="UITGAANDEEMAIL")
    private UitgaandeEmail uitgaandeEmail;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmailadres() {
        return emailadres;
    }

    public void setEmailadres(String emailadres) {
        this.emailadres = emailadres;
    }

    public UitgaandeEmail getUitgaandeEmail() {
        return uitgaandeEmail;
    }

    public void setUitgaandeEmail(UitgaandeEmail uitgaandeEmail) {
        this.uitgaandeEmail = uitgaandeEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Emailadres)) {
            return false;
        }

        Emailadres that = (Emailadres) o;

        return new EqualsBuilder().append(getId(), that.getId()).append(getEmailadres(), that.getEmailadres()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getId()).append(getEmailadres()).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("emailadres", emailadres).toString();
    }
}
