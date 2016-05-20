package nl.lakedigital.djfc.repository;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.util.List;

import nl.lakedigital.djfc.domain.*;
import nl.lakedigital.djfc.inloggen.Sessie;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.*;
import org.joda.time.LocalDateTime;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-unittest.xml")
public class CommunicatieProductRepositoryTest  {
    @Inject
    private CommunicatieProductRepository communicatieProductRepository;

    @Before
    public void init() {
        Sessie.setIngelogdeGebruiker(46L);
    }
@Test
public void testOngelezenIndicatieEnExtraInformatie(){
    IngaandeEmail ingaandeEmail=new IngaandeEmail();
    ExtraInformatie extraInformatie=new ExtraInformatie();
    extraInformatie.setEmailadres("emailadres");extraInformatie.setNaamAfzender("naamAfzender");extraInformatie.setIngaandeEmail(ingaandeEmail);
    ingaandeEmail.setExtraInformatie(extraInformatie);

    communicatieProductRepository.opslaan(ingaandeEmail);

    IngaandeEmail opgehaald = (IngaandeEmail)communicatieProductRepository.lees(ingaandeEmail.getId());
    assertThat(opgehaald.getOngelezenIndicatie(),is(notNullValue()));
    ExtraInformatie eiOpgehaald = opgehaald.getExtraInformatie();
    assertThat(eiOpgehaald,is(notNullValue()));
    assertThat(eiOpgehaald.getEmailadres(),is("emailadres"));
    assertThat(eiOpgehaald.getNaamAfzender(),is("naamAfzender"));

    ingaandeEmail.setOngelezenIndicatie(null);

    communicatieProductRepository.opslaan(ingaandeEmail);

    assertThat(((IngaandeEmail)communicatieProductRepository.lees(ingaandeEmail.getId())).getOngelezenIndicatie(),is(nullValue()));

    ingaandeEmail.setExtraInformatie(null);
    communicatieProductRepository.opslaan(ingaandeEmail);

    assertThat(((IngaandeEmail)communicatieProductRepository.lees(ingaandeEmail.getId())).getExtraInformatie(),is(nullValue()));

    communicatieProductRepository.verwijder(ingaandeEmail);
}
    @Test
    public void testAntwoordOp(){
        SoortEntiteit soortEntiteit=SoortEntiteit.RELATIE;
        Long entiteitId = 5L;

        assertThat(communicatieProductRepository.alles(soortEntiteit,entiteitId).size(),is(0));

        IngaandeEmail mail1=new IngaandeEmail();
        mail1.setSoortEntiteit(soortEntiteit);mail1.setEntiteitId(entiteitId);
        communicatieProductRepository.opslaan(mail1);

        UitgaandeEmail mail2=new UitgaandeEmail();
        mail2.setAntwoordOp(mail1);
        mail2.setSoortEntiteit(soortEntiteit);mail2.setEntiteitId(entiteitId);
        communicatieProductRepository.opslaan(mail2);

        IngaandeEmail mail3=new IngaandeEmail();
        mail3.setAntwoordOp(mail2);
        mail3.setSoortEntiteit(soortEntiteit);mail3.setEntiteitId(entiteitId);
        communicatieProductRepository.opslaan(mail3);

        UitgaandeEmail mail4=new UitgaandeEmail();
        mail4.setAntwoordOp(mail3);
        mail4.setSoortEntiteit(soortEntiteit);mail4.setEntiteitId(entiteitId);
        communicatieProductRepository.opslaan(mail4);

        UitgaandeEmail opgehaald =(UitgaandeEmail) communicatieProductRepository.lees(mail4.getId());
        assertThat((IngaandeEmail)opgehaald.getAntwoordOp(),is(mail3));
        assertThat((UitgaandeEmail)opgehaald.getAntwoordOp().getAntwoordOp(),is(mail2));
        assertThat((IngaandeEmail)opgehaald.getAntwoordOp().getAntwoordOp().getAntwoordOp(),is(mail1));
        assertThat(communicatieProductRepository.alles(soortEntiteit,entiteitId).size(),is(4));

        communicatieProductRepository.verwijder(mail1);
        communicatieProductRepository.verwijder(mail2);
        communicatieProductRepository.verwijder(mail3);
        communicatieProductRepository.verwijder(mail4);

        assertThat(communicatieProductRepository.alles(soortEntiteit,entiteitId).size(),is(0));
    }

    @Test
    public void testOpslaanIngaandeEmail() throws Exception {
        SoortEntiteit soortEntiteit=SoortEntiteit.RELATIE;
        Long entiteitId = 3L;

        Email email =new IngaandeEmail();
        email.setEntiteitId(entiteitId);
        email.setSoortEntiteit(soortEntiteit);

        voerTestUit(email, soortEntiteit,entiteitId);
    }
    @Test
    public void testOpslaanUitgaandeEmail() throws Exception {
        SoortEntiteit soortEntiteit=SoortEntiteit.RELATIE;
        Long entiteitId = 3L;

        UitgaandeEmail email =new UitgaandeEmail();
        email.setEntiteitId(entiteitId);
        email.setSoortEntiteit(soortEntiteit);
        email.setEmailadres("hetemailadres");

        voerTestUit(email, soortEntiteit,entiteitId);
    }
    @Test
    public void testOpslaanUitgaandeBrief() throws Exception {
        SoortEntiteit soortEntiteit=SoortEntiteit.RELATIE;
        Long entiteitId = 3L;

        UitgaandeBrief brief =new UitgaandeBrief();
        brief.setEntiteitId(entiteitId);
        brief.setSoortEntiteit(soortEntiteit);
        brief.setTekst("fjooaisjfisajfowjofijweojifow");

        voerTestUit(brief, soortEntiteit,entiteitId);
    }
    @Test
    public void testOpslaanIngaandeBrief() throws Exception {
        SoortEntiteit soortEntiteit=SoortEntiteit.RELATIE;
        Long entiteitId = 3L;

        Brief brief =new IngaandeBrief();
        brief.setEntiteitId(entiteitId);
        brief.setSoortEntiteit(soortEntiteit);
        brief.setTekst("fjooaisjfisajfowjofijweojifow");

        voerTestUit(brief, soortEntiteit,entiteitId);
    }

    private void voerTestUit(CommunicatieProduct communicatieProduct,SoortEntiteit soortEntiteit,Long entiteitId){
        communicatieProductRepository.opslaan(communicatieProduct);

        List<CommunicatieProduct> lijst =
                communicatieProductRepository.alles(soortEntiteit,entiteitId);
        assertThat(lijst.size(),is(1));
        assertThat(lijst.get(0).getDatumTijdCreatie(),is(notNullValue()));

        lijst =
                communicatieProductRepository.alles(soortEntiteit,entiteitId);
        assertThat(lijst.size(),is(1));
        assertThat(lijst.get(0).getDatumTijdCreatie(),is(notNullValue()));

        communicatieProductRepository.verwijder(communicatieProduct);

        lijst =
                communicatieProductRepository.alles(soortEntiteit,entiteitId);
        assertThat(lijst.size(),is(0));
    }

    @Test
    public void leesOnverzondenEmails(){
        SoortEntiteit soortEntiteit=SoortEntiteit.RELATIE;
        Long entiteitId = 3L;

        assertThat(communicatieProductRepository.alles(soortEntiteit,entiteitId).size(),is(0));

        UitgaandeEmail uitgaandeEmailOnverzonden=new UitgaandeEmail();
        uitgaandeEmailOnverzonden.setSoortEntiteit(soortEntiteit);uitgaandeEmailOnverzonden.setEntiteitId(entiteitId);
        UitgaandeEmail uitgaandeEmailVerzonden = new UitgaandeEmail();
        uitgaandeEmailVerzonden.setSoortEntiteit(soortEntiteit);uitgaandeEmailVerzonden.setEntiteitId(entiteitId);
        UitgaandeBrief uitgaandeBriefOnverzonden=new UitgaandeBrief();
        uitgaandeBriefOnverzonden.setSoortEntiteit(soortEntiteit);uitgaandeBriefOnverzonden.setEntiteitId(entiteitId);
        IngaandeEmail ingaandeEmailOnverzonden=new IngaandeEmail();
        ingaandeEmailOnverzonden.setSoortEntiteit(soortEntiteit);ingaandeEmailOnverzonden.setEntiteitId(entiteitId);

        uitgaandeEmailVerzonden.setDatumTijdVerzending(LocalDateTime.now());

        communicatieProductRepository.opslaan(uitgaandeEmailOnverzonden);
        communicatieProductRepository.opslaan(uitgaandeEmailVerzonden);
        communicatieProductRepository.opslaan(uitgaandeBriefOnverzonden);
        communicatieProductRepository.opslaan(ingaandeEmailOnverzonden);

        assertThat(communicatieProductRepository.alles(soortEntiteit,entiteitId).size(),is(4));

        List<UitgaandeEmail> onverzondenMails = communicatieProductRepository.leesOnverzondenEmails();
        assertThat(onverzondenMails.size(),is(1));
        assertThat(onverzondenMails.get(0),is(uitgaandeEmailOnverzonden));

        communicatieProductRepository.verwijder(uitgaandeEmailOnverzonden);
        communicatieProductRepository.verwijder(uitgaandeEmailVerzonden);
        communicatieProductRepository.verwijder(uitgaandeBriefOnverzonden);
        communicatieProductRepository.verwijder(ingaandeEmailOnverzonden);

        assertThat(communicatieProductRepository.alles(soortEntiteit,entiteitId).size(),is(0));
    }
}