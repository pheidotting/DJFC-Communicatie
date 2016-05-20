package nl.lakedigital.djfc.service;

import nl.lakedigital.djfc.service.ontvangen.LeesEmailService;
import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(EasyMockRunner.class)
public class LeesEmailServiceTest extends EasyMockSupport {
    @TestSubject
    private LeesEmailService leesEmailService = new LeesEmailService();

    @Test
    public void test() throws Exception {
//        String mailPop3Host = "localhost";
//        String mailStoreType = "pop3";
//        String mailUser = "hendrik@unknown.com";
//        String mailPassword = "";
//
//        ReflectionTestUtils.setField(leesEmailService, "mailPop3Host", mailPop3Host);
//        ReflectionTestUtils.setField(leesEmailService, "mailStoreType", mailStoreType);
//        ReflectionTestUtils.setField(leesEmailService, "mailUser", mailUser);
//        ReflectionTestUtils.setField(leesEmailService, "mailPassword", mailPassword);
//
//        final MockMailbox mb = MockMailbox.get("hendrik@unknown.com");
//        final MailboxFolder mf = mb.getInbox();
//
//        final MimeMessage msg1 = new MimeMessage((Session) null);
//        msg1.setSubject("Onderwerp 1");
//        msg1.setFrom("Henkie Penkie <afzender1@afzender1.nl>");
//        msg1.setText("Tekst 1");
//        msg1.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress("ontvanger1@ontvanger1.nl"));
//        mf.add(msg1); // 11
//
//        final MimeMessage msg2 = new MimeMessage((Session) null);
//        msg2.setSubject("Onderwerp 2");
//        msg2.setFrom("Wilson Fisk <afzender2@afzender2.nl>");
//        msg2.setText("Tekst 2");
//        msg2.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress("ontvanger2@ontvanger2.nl"));
//        mf.add(msg2); // 12
//
//        Map<CommunicatieProduct, String> result = leesEmailService.leesMails();
//
//        assertThat(result.size(), is(2));
//        IngaandeEmail cp1 = null;
//        IngaandeEmail cp2 = null;
//
//        for (CommunicatieProduct cp : result.keySet()) {
//            if (cp1 == null) {
//                cp1 = (IngaandeEmail) cp;
//            } else {
//                cp2 = (IngaandeEmail) cp;
//            }
//        }
//
//        assertThat(result.get(cp1), is("Henkie Penkie"));
//        assertThat(cp1.getTekst(), is("Tekst 1"));
//        assertThat(cp1.getOnderwerp(), is("Onderwerp 1"));
//        assertThat(cp1.getEmailadres(), is("afzender1@afzender1.nl"));
//
//        assertThat(result.get(cp2), is("Wilson Fisk"));
//        assertThat(cp2.getTekst(), is("Tekst 2"));
//        assertThat(cp2.getOnderwerp(), is("Onderwerp 2"));
//        assertThat(cp2.getEmailadres(), is("afzender2@afzender2.nl"));
    }

}