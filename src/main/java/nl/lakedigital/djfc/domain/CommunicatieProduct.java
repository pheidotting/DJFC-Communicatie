package nl.lakedigital.djfc.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.joda.time.LocalDateTime;

import javax.persistence.*;

@Table(name = "COMMUNICATIEPRODUCT")
@Entity
@Audited
@NamedQueries({//
        @NamedQuery(name = "CommunicatieProduct.zoekBijEntiteit", query = "select a from CommunicatieProduct a where a.soortEntiteit = :soortEntiteit and a.entiteitId = :entiteitId"),//
        @NamedQuery(name = "CommunicatieProduct.zoekOnverzondenEmails", query = "select a from UitgaandeEmail a where a.datumTijdVerzending is null and a.onverzondenIndicatie is not null"),//
        @NamedQuery(name = "CommunicatieProduct.zoekOnverzondenBrieven", query = "select a from UitgaandeBrief a where a.datumTijdVerzending is null and a.briefDocument is not null")//
})
public abstract class CommunicatieProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "DATUMTIJDCREATIE")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime datumTijdCreatie;
    @Column(name = "DATUMTIJDVERZENDING")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime datumTijdVerzending;
    @Column(name = "SOORTENTITEIT")
    @Enumerated(EnumType.STRING)
    private SoortEntiteit soortEntiteit;
    @Column(name = "ENTITEITID")
    private Long entiteitId;
    @Column(name = "TEKST")
    private String tekst;
    @JoinColumn(name="ANTWOORDOP")
    @OneToOne
    private CommunicatieProduct antwoordOp;
    @Column(name="MEDEWERKER")
    private Long medewerker;
    @Column(name="ONDERWERP")
    private String onderwerp;

    public String getOnderwerp() {
        return onderwerp;
    }

    public void setOnderwerp(String onderwerp) {
        this.onderwerp = onderwerp;
    }

    public CommunicatieProduct(){
        setDatumTijdCreatie(LocalDateTime.now());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDatumTijdCreatie() {
        return datumTijdCreatie;
    }

    public void setDatumTijdCreatie(LocalDateTime datumTijdCreatie) {
        this.datumTijdCreatie = datumTijdCreatie;
    }

    public LocalDateTime getDatumTijdVerzending() {
        return datumTijdVerzending;
    }

    public void setDatumTijdVerzending(LocalDateTime datumTijdVerzending) {
        this.datumTijdVerzending = datumTijdVerzending;
    }

    public SoortEntiteit getSoortEntiteit() {
        return soortEntiteit;
    }

    public void setSoortEntiteit(SoortEntiteit soortEntiteit) {
        this.soortEntiteit = soortEntiteit;
    }

    public Long getEntiteitId() {
        return entiteitId;
    }

    public void setEntiteitId(Long entiteitId) {
        this.entiteitId = entiteitId;
    }

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }

    public CommunicatieProduct getAntwoordOp() {
        return antwoordOp;
    }

    public void setAntwoordOp(CommunicatieProduct antwoordOp) {
        this.antwoordOp = antwoordOp;
    }

    public Long getMedewerker() {
        return medewerker;
    }

    public void setMedewerker(Long medewerker) {
        this.medewerker = medewerker;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof CommunicatieProduct)) {
            return false;
        }

        CommunicatieProduct that = (CommunicatieProduct) o;

        return new EqualsBuilder().append(getId(), that.getId()).append(getDatumTijdVerzending(), that.getDatumTijdVerzending()).append(getSoortEntiteit(), that.getSoortEntiteit()).append(getEntiteitId(), that.getEntiteitId()).append(getTekst(), that.getTekst()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getId()).append(getDatumTijdVerzending()).append(getSoortEntiteit()).append(getEntiteitId()).append(getTekst()).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("datumTijdCreatie", datumTijdCreatie).append("datumTijdVerzending", datumTijdVerzending).append("soortEntiteit", soortEntiteit).append("entiteitId", entiteitId).append("tekst", tekst).toString();
    }
}
