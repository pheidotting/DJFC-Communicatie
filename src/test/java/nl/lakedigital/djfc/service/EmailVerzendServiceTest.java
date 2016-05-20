package nl.lakedigital.djfc.service;

import nl.lakedigital.djfc.repository.CommunicatieProductRepository;
import nl.lakedigital.djfc.service.verzenden.EmailVerzendService;
import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(EasyMockRunner.class)
public class EmailVerzendServiceTest extends EasyMockSupport {
    @TestSubject
    private EmailVerzendService emailVerzendService=new EmailVerzendService();
    @Mock
    private CommunicatieProductRepository communicatieProductRepository;

    @Test
    public void test(){
//        String mailPop3Host = "localhost";
//         Integer smtpPort = 587;
//
//        ReflectionTestUtils.setField(emailVerzendService, "mailPop3Host", mailPop3Host);
//        ReflectionTestUtils.setField(emailVerzendService, "smtpPort", smtpPort);
//
//        UitgaandeEmail uitgaandeEmail=new UitgaandeEmail();
//        uitgaandeEmail.setEmailadres("a@b.c");uitgaandeEmail.setOnderwerp("onderwerp1");uitgaandeEmail.setTekst("tekst1");
//        List<UitgaandeEmail> uitgaandeEmails=new ArrayList<>();
//        uitgaandeEmails.add(uitgaandeEmail);
//        expect(
//        communicatieProductRepository.leesOnverzondenEmails()).andReturn(uitgaandeEmails);
//
//uitgaandeEmail.setDatumTijdVerzending(LocalDateTime.now());
//
//        communicatieProductRepository.opslaan(transform(uitgaandeEmails, new Function<UitgaandeEmail, CommunicatieProduct>() {
//            @Nullable
//            @Override
//            public CommunicatieProduct apply(@Nullable UitgaandeEmail uitgaandeEmail) {
//                return uitgaandeEmail;
//            }
//        }));
//
//        replayAll();
//
//        emailVerzendService.verstuurEmails();
//
//        verifyAll();
    }
}