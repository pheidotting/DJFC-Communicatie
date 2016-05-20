package nl.lakedigital.djfc.service;

import nl.lakedigital.djfc.client.dejonge.KantoorClient;
import nl.lakedigital.djfc.client.dejonge.MedewerkerClient;
import nl.lakedigital.djfc.client.dejonge.RelatieClient;
import nl.lakedigital.djfc.client.oga.AdresClient;
import nl.lakedigital.djfc.client.oga.BijlageClient;
import nl.lakedigital.djfc.commons.json.*;
import nl.lakedigital.djfc.domain.BriefDocument;
import nl.lakedigital.djfc.domain.UitgaandeBrief;
import nl.lakedigital.djfc.repository.CommunicatieProductRepository;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static com.google.common.collect.Lists.newArrayList;

@Service
public class MaakBriefService {
    private final static Logger LOGGER = LoggerFactory.getLogger(MaakBriefService.class);

    @Inject
    private CommunicatieProductRepository communicatieProductRepository;
    @Inject
    private TemplateEngine templateEngine;
    @Inject
    private HtmlToPdfConversieService htmlToPdfConversieService;

    private KantoorClient kantoorClient = new KantoorClient();
    private MedewerkerClient medewerkerClient = new MedewerkerClient();
    private RelatieClient relatieClient = new RelatieClient();
    private AdresClient adresClient = new AdresClient();
    private BijlageClient bijlageClient = new BijlageClient();

    public void verzend(UitgaandeBrief uitgaandeBrief) {
            Context context = new Context();

            JsonMedewerker medewerker = medewerkerClient.lees(uitgaandeBrief.getMedewerker());
            JsonKantoor kantoor = kantoorClient.lees(medewerker.getKantoor());
            JsonRelatie relatie = relatieClient.lees(uitgaandeBrief.getEntiteitId());

            JsonAdres afzenderAdres = adresClient.lijst("KANTOOR", kantoor.getId()).get(0);
            Optional<JsonAdres> geadresseerdeAdresOptional = adresClient.lijst("RELATIE", relatie.getId()).stream().filter(new Predicate<JsonAdres>() {
                @Override
                public boolean test(JsonAdres jsonAdres) {
                    return "POSTADRES".equals(jsonAdres.getSoortAdres());
                }
            }).findFirst();
            if (!geadresseerdeAdresOptional.isPresent()) {
                geadresseerdeAdresOptional = adresClient.lijst("RELATIE", relatie.getId()).stream().filter(new Predicate<JsonAdres>() {
                    @Override
                    public boolean test(JsonAdres jsonAdres) {
                        return "WOONADRES".equals(jsonAdres.getSoortAdres());
                    }
                }).findFirst();
            }

            JsonAdres geadresseerdeAdres = geadresseerdeAdresOptional.get();

            context.setVariable("afzenderAdres", afzenderAdres);
            context.setVariable("kantoor", kantoor);
            context.setVariable("geadresseerdeAdres", geadresseerdeAdres);
            context.setVariable("relatie", relatie);
            context.setVariable("uitgaandeBrief", uitgaandeBrief);

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(relatie.getVoornaam());
            stringBuilder.append(" ");
            if (relatie.getTussenvoegsel() != null) {
                stringBuilder.append(relatie.getTussenvoegsel());
                stringBuilder.append(" ");
            }
            stringBuilder.append(relatie.getAchternaam());
            context.setVariable("relatieNaam", stringBuilder.toString());

            String a = templateEngine.process("standaard-brief", context).replace("\r", "<br />");
            LOGGER.debug(a);

            JsonBijlage bijlage = new JsonBijlage();
            bijlage.setBestandsNaam(htmlToPdfConversieService.maakAan(a));
            bijlage.setSoortEntiteit("COMMUNICATIEPRODUCT");
            bijlage.setEntiteitId(uitgaandeBrief.getId());
            bijlage.setOmschrijvingOfBestandsNaam(uitgaandeBrief.getOnderwerp());
            bijlageClient.opslaan(newArrayList(bijlage));

            BriefDocument briefDocument = new BriefDocument();
            briefDocument.setBriefDocument(bijlage.getBestandsNaam());
            briefDocument.setUitgaandeBrief(uitgaandeBrief);
            uitgaandeBrief.setBriefDocument(briefDocument);
    }}
