package nl.lakedigital.djfc.repository;

import nl.lakedigital.djfc.domain.CommunicatieProduct;
import nl.lakedigital.djfc.domain.SoortEntiteit;
import nl.lakedigital.djfc.domain.UitgaandeBrief;
import nl.lakedigital.djfc.domain.UitgaandeEmail;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommunicatieProductRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommunicatieProductRepository.class);

    @Autowired
    private SessionFactory sessionFactory;

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    protected Transaction getTransaction() {
        Transaction transaction = getSession().getTransaction();
        if (transaction.getStatus() != TransactionStatus.ACTIVE) {
            transaction.begin();
        }

        return transaction;
    }

    public List<UitgaandeEmail> leesOnverzondenEmails(){
        getTransaction();

        Query query = getSession().getNamedQuery( "CommunicatieProduct.zoekOnverzondenEmails");

        List<UitgaandeEmail> lijst = query.list();

        getTransaction().commit();

        return lijst;
    }
    public List<UitgaandeBrief> leesOnverzondenBrieven(){
        getTransaction();

        Query query = getSession().getNamedQuery( "CommunicatieProduct.zoekOnverzondenBrieven");

        List<UitgaandeBrief> lijst = query.list();

        getTransaction().commit();

        return lijst;
    }

    public void verwijder(List<CommunicatieProduct> communicatieProducts) {
        if (getTransaction().getStatus() != TransactionStatus.ACTIVE) {
            getTransaction().begin();
        }

        for (CommunicatieProduct communicatieProduct : communicatieProducts) {
            getSession().delete(communicatieProduct);
        }

        getTransaction().commit();
    }

    public void opslaan(List<CommunicatieProduct> communicatieProducts) {
        if (getTransaction().getStatus() != TransactionStatus.ACTIVE) {
            getTransaction().begin();
        }

        for (CommunicatieProduct communicatieProduct : communicatieProducts) {
            LOGGER.info("Opslaan {}", ReflectionToStringBuilder.toString(communicatieProduct, ToStringStyle.SHORT_PREFIX_STYLE));
            if (communicatieProduct.getId() == null) {
                getSession().save(communicatieProduct);
            } else {
                getSession().merge(communicatieProduct);
            }
        }

        getTransaction().commit();
        getSession().close();
    }
    public void opslaan(CommunicatieProduct communicatieProduct) {
        if (getTransaction().getStatus() != TransactionStatus.ACTIVE) {
            getTransaction().begin();
        }

            LOGGER.info("Opslaan {}", ReflectionToStringBuilder.toString(communicatieProduct, ToStringStyle.SHORT_PREFIX_STYLE));
            if (communicatieProduct.getId() == null) {
                getSession().save(communicatieProduct);
            } else {
                getSession().merge(communicatieProduct);
            }

        getTransaction().commit();
        getSession().close();
    }

    public CommunicatieProduct lees(Long id) {
        getTransaction().begin();

        CommunicatieProduct t = getSession().get(CommunicatieProduct.class, id);

        getTransaction().commit();

        return t;
    }

    public List<CommunicatieProduct> alles(SoortEntiteit soortEntiteit, Long entiteitId) {
        LOGGER.debug("Ophalen lijst bij {} met id {}",soortEntiteit,entiteitId);

        getTransaction().begin();

        Query query = getSession().getNamedQuery( "CommunicatieProduct.zoekBijEntiteit");
        query.setParameter("soortEntiteit", soortEntiteit);
        query.setParameter("entiteitId", entiteitId);

        List<CommunicatieProduct> lijst = query.list();

        getTransaction().commit();

        LOGGER.debug("Size {}",lijst.size());

        return lijst;
    }

    public void verwijder(CommunicatieProduct communicatieProduct){
        getTransaction().begin();

        getSession().delete(communicatieProduct);

        getTransaction().commit();
    }}
