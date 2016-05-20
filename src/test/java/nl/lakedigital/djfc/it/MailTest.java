package nl.lakedigital.djfc.it;

import nl.lakedigital.djfc.service.ontvangen.LeesEmailService;
import org.junit.Test;

public class MailTest {
private    LeesEmailService leesEmailService=new LeesEmailService();
//    @Rule
//    public final GreenMailRule greenMail = new GreenMailRule(ServerSetupTest.SMTP_IMAP);
//
    @Test
    public void voeruit()throws Exception{
//        Session smtpSession = greenMail.getSmtp().createSession();
//
//        Message msg = new MimeMessage(smtpSession);
//        msg.setFrom(new InternetAddress("foo@example.com"));
//        msg.addRecipient(Message.RecipientType.TO,
//                new InternetAddress("bar@example.com"));
//        msg.setSubject("Email sent to GreenMail via plain JavaMail");
//        msg.setText("Fetch me via IMAP");
//        Transport.send(msg);
//
//         Create user, as connect verifies pwd
//        greenMail.setUser("bar@example.com", "bar@example.com", "secret-pwd");
//
//         Alternative 1: Create session and store or ...
//        Session imapSession = greenMail.getImap().createSession();
//        Store store = imapSession.getStore("imap");
//        store.connect("bar@example.com", "secret-pwd");
//        Folder inbox = store.getFolder("INBOX");
//        inbox.open(Folder.READ_ONLY);
//        Message msgReceived = inbox.getMessage(1);
//        assertEquals(msg.getSubject(), msgReceived.getSubject());




//        GreenMail greenMail = new GreenMail(ServerSetupTest.ALL);
//        greenMail.start();
//
//        Use random content to avoid potential residual lingering problems
//        final String subject = GreenMailUtil.random();
//        final String body = GreenMailUtil.random();
//        MimeMessage message = createMimeMessage(); // Construct message
//        GreenMailUser user = greenMail.setUser("wael@localhost.com", "waelc", "soooosecret");
//        user.deliver(message);
//        assertEquals(1, greenMail.getReceivedMessages().length);
//
//         --- Place your retrieve code here
//        greenMail.stop();


        //        SimpleSmtpServer server = SimpleSmtpServer.start();

//        try {
            // Submits an email using javamail to the email server listening on port 25
            // (method not shown here). Replace this with a call to your app logic.
            leesEmailService.leesMails();
//            sendMessage(25, "sender@here.com", "Test", "Test Body", "receiver@there.com");
//        } catch(Exception e) {
//            e.printStackTrace();
//            fail("Unexpected exception: "+e);
//        }
//
//        server.stop();
//
//        assertTrue(server.getReceivedEmailSize() == 1);
//        Iterator emailIter = server.getReceivedEmail();
//        SmtpMessage email = (SmtpMessage)emailIter.next();
//        assertTrue(email.getHeaderValue("Subject").equals("Test"));
//        assertTrue(email.getBody().equals("Test Body"));

}
}
