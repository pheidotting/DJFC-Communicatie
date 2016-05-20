package nl.lakedigital.djfc.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Audited
@Entity
@Table(name = "BRIEFDOCUMENT")
public class BriefDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "DOCUMENT")
    private String briefDocument;
    @OneToOne(optional = false)
    @JoinColumn(name="UITGAANDEBRIEF")
    private UitgaandeBrief uitgaandeBrief;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBriefDocument() {
        return briefDocument;
    }

    public void setBriefDocument(String briefDocument) {
        this.briefDocument = briefDocument;
    }

    public UitgaandeBrief getUitgaandeBrief() {
        return uitgaandeBrief;
    }

    public void setUitgaandeBrief(UitgaandeBrief uitgaandeBrief) {
        this.uitgaandeBrief = uitgaandeBrief;
    }
}
