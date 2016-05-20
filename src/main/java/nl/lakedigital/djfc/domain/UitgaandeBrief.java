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

public class UitgaandeBrief extends Brief {
    @OneToOne(mappedBy = "uitgaandeBrief",cascade = CascadeType.ALL, orphanRemoval = true)
    private BriefDocument briefDocument;

    public BriefDocument getBriefDocument() {
        return briefDocument;
    }

    public void setBriefDocument(BriefDocument briefDocument) {
        this.briefDocument = briefDocument;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof UitgaandeBrief)) {
            return false;
        }

        UitgaandeBrief that = (UitgaandeBrief) o;

        return new EqualsBuilder().appendSuper(super.equals(o)).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).toString();
    }
}
