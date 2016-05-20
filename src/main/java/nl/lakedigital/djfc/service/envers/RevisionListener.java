package nl.lakedigital.djfc.service.envers;


import nl.lakedigital.djfc.inloggen.Sessie;

public class RevisionListener implements org.hibernate.envers.RevisionListener {
    @Override
    public void newRevision(Object revisionEntity) {
        RevEntity revEntity = (RevEntity) revisionEntity;

        revEntity.setUserid(Sessie.getIngelogdeGebruiker());
    }

}
