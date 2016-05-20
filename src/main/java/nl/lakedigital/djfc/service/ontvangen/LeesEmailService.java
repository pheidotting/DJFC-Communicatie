package nl.lakedigital.djfc.service.ontvangen;

import com.google.common.base.Function;
import com.sun.mail.imap.IMAPFolder;
import nl.lakedigital.djfc.client.oga.BijlageClient;
import nl.lakedigital.djfc.commons.json.JsonBijlage;
import nl.lakedigital.djfc.domain.CommunicatieProduct;
import nl.lakedigital.djfc.domain.IngaandeEmail;
import nl.lakedigital.djfc.repository.CommunicatieProductRepository;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.mail.util.MimeMessageParser;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.activation.DataSource;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;

@Service
public class LeesEmailService {
    private final static Logger LOGGER = LoggerFactory.getLogger(LeesEmailService.class);

    private String mailPop3Host = "89.18.180.239";
    private String mailStoreType = "pop3";
    private String mailUser = "test@heidotting.nl";
    private String mailPassword = "test";

    @Inject
    private CommunicatieProductRepository communicatieProductRepository;
    private BijlageClient bijlageClient=new BijlageClient();

    public List<CommunicatieProduct> leesMails() {
        List<CommunicatieProduct> lijst = new ArrayList<>();

        IMAPFolder emailFolder = null;
        Store store = null;
        String subject = null;
        Flags.Flag flag = null;

        try {
            LOGGER.debug("Verbinding maken met {}", mailPop3Host);
            Properties props = System.getProperties();
            props.setProperty("mail.store.protocol", "imap");

            Session session = Session.getDefaultInstance(props, null);

            store = session.getStore("imap");
            store.connect(mailPop3Host, mailUser, mailPassword);

            emailFolder = (IMAPFolder) store.getFolder("Inbox"); // This doesn't work for other email account
            emailFolder.open(Folder.READ_WRITE);

            Message[] messages = emailFolder.getMessages();
            LOGGER.info("Inlezen {} mails", messages.length);
            for (int i = 0; i < messages.length; i++) {
                List<JsonBijlage> bijlages = new ArrayList<>();

                Message message = messages[i];

                final IngaandeEmail ingaandeEmail = new IngaandeEmail();
                ingaandeEmail.setOnderwerp(message.getSubject());

                final MimeMessageParser mmp = new MimeMessageParser((MimeMessage) message);
                mmp.parse();

                final List<?> attachments = mmp.getAttachmentList();

                LOGGER.debug("moi {}", attachments.size());

                if (!attachments.isEmpty()) {

                    for (DataSource data : mmp.getAttachmentList()) {
                        String bestandsnaam = bijlageClient.genereerBestandsnaam();
                        String uploadPad = bijlageClient.getUploadPad();
                        LOGGER.debug("bestandsnaam {}",bestandsnaam);
                        LOGGER.debug("uploadPad {}",uploadPad);

                        if (data.getName() != null && !"".equals(data.getName())) {
                            File postacert = new File(uploadPad + File.separator+bestandsnaam);
                            FileOutputStream output = new FileOutputStream(postacert);
                            IOUtils.copy(data.getInputStream(), output);
                            IOUtils.closeQuietly(output);
                        }

                        JsonBijlage bijlage = new JsonBijlage();
                        bijlage.setBestandsNaam(bestandsnaam);
                        bijlage.setSoortEntiteit("COMMUNICATIEPRODUCT");
                        bijlage.setOmschrijvingOfBestandsNaam(data.getName());

                        bijlages.add(bijlage);
                    }
                }

                if (message.getContent() instanceof javax.mail.internet.MimeMultipart) {
                    Multipart multipart = (Multipart) message.getContent();
                    String result = "";
                    for (int j = 0; j < multipart.getCount(); j++) {
                        LOGGER.debug("j {}", j);
                        BodyPart bodyPart = multipart.getBodyPart(j);
                        if (bodyPart.isMimeType("text/plain")) {
                            result = result + "\n" + bodyPart.getContent();
                            break;  //without break same text appears twice in my tests
                        } else if (bodyPart.isMimeType("text/html")) {
                            String html = (String) bodyPart.getContent();
                            result = result + "\n" + Jsoup.parse(html).text();

                        }
                    }

                    result = result.trim();
                    if (result.startsWith(">")) {
                        result = result.substring(1).trim();
                    }

                    ingaandeEmail.setTekst(result);
                } else {
                    ingaandeEmail.setTekst(message.getContent().toString());
                }

                String emailadres = stripEmailAdres(message.getFrom()[0].toString());
                ingaandeEmail.getExtraInformatie().setEmailadres(emailadres);

                String naam = message.getFrom()[0].toString().replace(emailadres, "").replace("<", "").replace(">", "").trim();
                ingaandeEmail.getExtraInformatie().setNaamAfzender(naam);

                String afzenderNaam = message.getFrom()[0].toString().replace(ingaandeEmail.getExtraInformatie().getEmailadres(), "").replace("<>", "").trim();
                LOGGER.debug("Afzender {}", afzenderNaam);

                communicatieProductRepository.opslaan(ingaandeEmail);
                if(!bijlages.isEmpty()){
                bijlageClient.opslaan(newArrayList(transform(bijlages, new Function<JsonBijlage, JsonBijlage>() {
                    @Nullable
                    @Override
                    public JsonBijlage apply(@Nullable JsonBijlage jsonBijlage) {
                         jsonBijlage.setEntiteitId(ingaandeEmail.getId());

                        return jsonBijlage;
                    }
                })));}

                message.setFlag(Flags.Flag.DELETED, true);

                LOGGER.trace("==============================");
                LOGGER.trace("Email #" + (i + 1));
                LOGGER.trace("Subject: " + message.getSubject());
                for (int j = 0; j < message.getFrom().length; j++) {
                    LOGGER.trace("From: " + message.getFrom()[j]);
                }
                LOGGER.trace("Text: " + message.getContent().toString());

                LOGGER.trace(ReflectionToStringBuilder.toString(ingaandeEmail, ToStringStyle.SHORT_PREFIX_STYLE));

                lijst.add(ingaandeEmail);
            }

            emailFolder.close(true);
            store.close();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lijst;
    }

    private String stripEmailAdres(String mailadres) {
        return mailadres.substring(mailadres.indexOf("<") + 1).replace(">", "");
    }
}
