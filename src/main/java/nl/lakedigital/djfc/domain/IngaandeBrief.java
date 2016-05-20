package nl.lakedigital.djfc.domain;

import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Table;

@Audited
@Entity
@Table(name = "COMMUNICATIEPRODUCT")
public  class IngaandeBrief extends Brief{
}
