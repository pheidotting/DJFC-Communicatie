package nl.lakedigital.djfc.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Audited
@Entity
@Table(name = "COMMUNICATIEPRODUCT")
public class UitgaandeEmail extends Email {
    @OneToOne(mappedBy = "uitgaandeEmail",cascade = CascadeType.ALL, orphanRemoval = true)
    private Emailadres emailadres;

    @OneToOne(mappedBy = "uitgaandeEmail",cascade = CascadeType.ALL, orphanRemoval = true)
    private OnverzondenIndicatie onverzondenIndicatie;

    public Emailadres getEmailadres() {
        return emailadres;
    }

    public void setEmailadres(String emailadres) {
        if (this.emailadres == null) {
            this.emailadres = new Emailadres();
            this.emailadres.setUitgaandeEmail(this);
        }
        this.emailadres.setEmailadres(emailadres);
    }

    public void setEmailadres(Emailadres emailadres) {
        this.emailadres = emailadres;
    }

    public OnverzondenIndicatie getOnverzondenIndicatie() {
        return onverzondenIndicatie;
    }

    public void setOnverzondenIndicatie(OnverzondenIndicatie onverzondenIndicatie) {
        this.onverzondenIndicatie = onverzondenIndicatie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof UitgaandeEmail)) {
            return false;
        }

        UitgaandeEmail that = (UitgaandeEmail) o;

        return new EqualsBuilder().appendSuper(super.equals(o)).append(getEmailadres(), that.getEmailadres()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(getEmailadres()).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("emailadres", emailadres).toString();
    }
}
