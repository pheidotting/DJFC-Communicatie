package nl.lakedigital.djfc.service.verzenden;

import nl.lakedigital.djfc.client.dejonge.KantoorClient;
import nl.lakedigital.djfc.client.dejonge.MedewerkerClient;
import nl.lakedigital.djfc.client.dejonge.RelatieClient;
import nl.lakedigital.djfc.client.oga.AdresClient;
import nl.lakedigital.djfc.client.oga.BijlageClient;
import nl.lakedigital.djfc.commons.json.*;
import nl.lakedigital.djfc.domain.BriefDocument;
import nl.lakedigital.djfc.domain.CommunicatieProduct;
import nl.lakedigital.djfc.domain.UitgaandeBrief;
import nl.lakedigital.djfc.repository.CommunicatieProductRepository;
import nl.lakedigital.djfc.service.HtmlToPdfConversieService;
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
public class BriefVerzendService extends AbstractVerzendService{
    private final static Logger LOGGER = LoggerFactory.getLogger(BriefVerzendService.class);

    @Inject
    private CommunicatieProductRepository communicatieProductRepository;

    @Override
    public boolean isVoorMij(CommunicatieProduct communicatieProduct) {
        return communicatieProduct instanceof UitgaandeBrief;
    }

    @Override
    public void verzend(CommunicatieProduct communicatieProduct) {
        List<UitgaandeBrief> uitgaandeBriefs = communicatieProductRepository.leesOnverzondenBrieven();

        for (UitgaandeBrief uitgaandeBrief : uitgaandeBriefs) {
            uitgaandeBrief.setDatumTijdVerzending(LocalDateTime.now());

            communicatieProductRepository.opslaan(uitgaandeBrief);

        }
    }
}
