package nl.lakedigital.djfc.service.verzenden;

import nl.lakedigital.djfc.client.dejonge.KantoorClient;
import nl.lakedigital.djfc.client.dejonge.MedewerkerClient;
import nl.lakedigital.djfc.client.dejonge.RelatieClient;
import nl.lakedigital.djfc.client.oga.BijlageClient;
import nl.lakedigital.djfc.commons.json.JsonBijlage;
import nl.lakedigital.djfc.commons.json.JsonKantoor;
import nl.lakedigital.djfc.commons.json.JsonMedewerker;
import nl.lakedigital.djfc.commons.json.JsonRelatie;
import nl.lakedigital.djfc.domain.CommunicatieProduct;
import nl.lakedigital.djfc.domain.UitgaandeEmail;
import nl.lakedigital.djfc.repository.CommunicatieProductRepository;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Service
public class EmailVerzendService extends AbstractVerzendService {
    private final static Logger LOGGER = LoggerFactory.getLogger(EmailVerzendService.class);

    private String mailHost = "localhost";
    private Integer smtpPort = 2170;

    private MedewerkerClient medewerkerClient = new MedewerkerClient(8080);
    private KantoorClient kantoorClient = new KantoorClient(8080);
    private RelatieClient relatieClient = new RelatieClient(8080);

    @Inject
    private CommunicatieProductRepository communicatieProductRepository;
    private BijlageClient bijlageClient = new BijlageClient(8081);

    @Override
    public boolean isVoorMij(CommunicatieProduct communicatieProduct) {
        return communicatieProduct instanceof UitgaandeEmail;
    }

    public void verzend(CommunicatieProduct communicatieProduct) {
        try {
            LOGGER.debug("Verbinding maken met {}", mailHost);

            Properties properties = new Properties();
            properties.put("mail.smtp.host", mailHost);
            properties.put("mail.smtp.port", smtpPort);
            LOGGER.debug("smtp port {}", smtpPort);
            Session emailSession = Session.getDefaultInstance(properties, null);

            UitgaandeEmail uitgaandeEmail = (UitgaandeEmail) communicatieProduct;

            JsonMedewerker medewerker = medewerkerClient.lees(uitgaandeEmail.getMedewerker());
            JsonKantoor kantoor = kantoorClient.lees(medewerker.getKantoor());

            StringBuilder afzender = maakNaam(medewerker.getVoornaam(), medewerker.getTussenvoegsel(), medewerker.getAchternaam());
            afzender.append(" (");
            afzender.append(kantoor.getNaam());
            afzender.append(") <");
            afzender.append(kantoor.getEmailadres());
            afzender.append(">");

            Message msg = new MimeMessage(emailSession);

            // -- Set the FROM and TO fields --
            msg.setFrom(new InternetAddress(afzender.toString()));

            JsonRelatie relatie = relatieClient.lees(uitgaandeEmail.getEntiteitId());

            StringBuilder ontvanger = maakNaam(relatie.getVoornaam(), relatie.getTussenvoegsel(), relatie.getAchternaam());
            ontvanger.append(" <");
            ontvanger.append(relatie.getEmailadres());
            ontvanger.append(">");

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(ontvanger.toString(), false));
            msg.setSubject(uitgaandeEmail.getOnderwerp());
            msg.setSentDate(new Date());

            BodyPart messageBodyPart = new MimeBodyPart();

            //evt bijlages bijzoeken
            List<JsonBijlage> bijlages = bijlageClient.lijst("COMMUNICATIEPRODUCT", uitgaandeEmail.getId());

            // Create a multipar message
            Multipart multipart = new MimeMultipart();
            //                 Now set the actual message
            messageBodyPart.setText(uitgaandeEmail.getTekst() + "\n");
            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            for (JsonBijlage bijlage : bijlages) {
                // Part two is attachment
                messageBodyPart = new MimeBodyPart();
                String filename = bijlage.getBestandsNaam();
                DataSource source = new FileDataSource(bijlageClient.getUploadPad() + File.separator + bijlage.getS3Identificatie());
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(bijlage.getBestandsNaam());
                multipart.addBodyPart(messageBodyPart);
            }

            // Send the complete message parts
            msg.setContent(multipart);

            Transport transport = emailSession.getTransport("smtp");
            transport.connect(mailHost, smtpPort, null, null);
            //Zeker weten dat de mail niet al verstuurd is door een andere Thread
            if (communicatieProductRepository.lees(uitgaandeEmail.getId()).getDatumTijdVerzending() == null) {
                transport.sendMessage(msg, msg.getAllRecipients());
            }
            transport.close();

            try {
                Thread.sleep(1 + (int) (Math.random() * 10));
            } catch (InterruptedException ie) {
                LOGGER.trace("Error bij Sleep {}", ie.getStackTrace());
            }

            uitgaandeEmail.setDatumTijdVerzending(LocalDateTime.now());
            uitgaandeEmail.setOnverzondenIndicatie(null);
        } catch (NoSuchProviderException e) {
            LOGGER.error("{}", e);
        } catch (MessagingException e) {
            LOGGER.error("{}", e);
        }

    }

    private StringBuilder maakNaam(String voornaam, String tussenvoegsel, String achternaam) {
        StringBuilder naam = new StringBuilder();
        naam.append(voornaam);
        naam.append(" ");
        if (tussenvoegsel != null && !"".equals(tussenvoegsel)) {
            naam.append(tussenvoegsel);
            naam.append(" ");
        }
        naam.append(achternaam);

        return naam;

    }
}
