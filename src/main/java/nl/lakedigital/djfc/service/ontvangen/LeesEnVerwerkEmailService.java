package nl.lakedigital.djfc.service.ontvangen;

import nl.lakedigital.djfc.client.dejonge.RelatieClient;
import nl.lakedigital.djfc.commons.json.JsonRelatie;
import nl.lakedigital.djfc.domain.CommunicatieProduct;
import nl.lakedigital.djfc.domain.IngaandeEmail;
import nl.lakedigital.djfc.domain.SoortEntiteit;
import nl.lakedigital.djfc.service.CommunicatieProductService;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class LeesEnVerwerkEmailService {
    private final static Logger LOGGER = LoggerFactory.getLogger(LeesEnVerwerkEmailService.class);
    @Inject
    private LeesEmailService leesEmailService;
    @Inject
    private CommunicatieProductService communicatieProductService;

    private RelatieClient relatieClient = new RelatieClient();

    public void leesEnVerwerkEmails() {
        List<CommunicatieProduct> mails = leesEmailService.leesMails();

        LOGGER.debug("Opgehaald {} emails",mails.size());

        List<CommunicatieProduct> communicatieProducten = koppelAanRelatieViaEmailadresEnNaam(mails);

        communicatieProductService.opslaan(communicatieProducten);

    }
    private List<CommunicatieProduct> koppelAanRelatieViaEmailadresEnNaam(List<CommunicatieProduct> mails){
        List<CommunicatieProduct> communicatieProducten = new ArrayList<>();
        for (CommunicatieProduct communicatieProduct : mails) {
            LOGGER.debug("Onderzoeken {}", ReflectionToStringBuilder.toString(communicatieProduct, ToStringStyle.SHORT_PREFIX_STYLE));

            if (communicatieProduct instanceof IngaandeEmail) {
                IngaandeEmail email = (IngaandeEmail) communicatieProduct;
                LOGGER.debug("{} opzoeken",email);
                JsonRelatie relatie = relatieClient.zoekOpEmailadres(email.getExtraInformatie().getEmailadres());

                if (relatie != null) {
                    communicatieProduct.setSoortEntiteit(SoortEntiteit.RELATIE);
                    communicatieProduct.setEntiteitId(relatie.getId());
                    ((IngaandeEmail) communicatieProduct).setExtraInformatie(null);
                }else{
                    try {
                        List<JsonRelatie> gevondenRelaties = relatieClient.zoekOpNaam(URLEncoder.encode(email.getExtraInformatie().getNaamAfzender(), "UTF-8"));
                    if (gevondenRelaties.size() == 1) {
                        communicatieProduct.setSoortEntiteit(SoortEntiteit.RELATIE);
                        communicatieProduct.setEntiteitId(gevondenRelaties.get(0).getId());
                        ((IngaandeEmail) communicatieProduct).setExtraInformatie(null);
                    }
                    }catch(UnsupportedEncodingException e){

                    }
                }
                communicatieProducten.add(communicatieProduct);
            }
        }return communicatieProducten;
    }
}
