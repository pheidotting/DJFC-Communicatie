package nl.lakedigital.djfc.service;

import nl.lakedigital.djfc.client.dejonge.RelatieClient;
import nl.lakedigital.djfc.client.oga.AdresClient;
import nl.lakedigital.djfc.commons.json.JsonAdres;
import nl.lakedigital.djfc.commons.json.JsonRelatie;
import nl.lakedigital.djfc.domain.*;
import nl.lakedigital.djfc.repository.CommunicatieProductRepository;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommunicatieProductService {
    private final static Logger LOGGER = LoggerFactory.getLogger(CommunicatieProductService.class);

    public enum SoortCommunicatieProduct {EMAIL, BRIEF}

    @Inject
    private CommunicatieProductRepository communicatieProductRepository;
    @Inject
    private MaakBriefService maakBriefService;

    private RelatieClient relatieClient = new RelatieClient();
    private AdresClient adresClient = new AdresClient();

    public void opslaan(List<CommunicatieProduct> communicatieProducts) {
        communicatieProductRepository.opslaan(communicatieProducts);
    }
        public List<CommunicatieProduct> lijst(SoortEntiteit soortEntiteit,Long entiteitId){
        LOGGER.debug("Ophalen lijst bij {} met id {}",soortEntiteit,entiteitId);
        return communicatieProductRepository.alles(soortEntiteit,entiteitId);
    }

    public Long maakCommunicatieProduct(Long id, SoortCommunicatieProduct soortCommunicatieProduct, Long relatieId, String tekst, String onderwerp, Long antwoordOpId, Long medewerker) {
        JsonRelatie relatie = relatieClient.lees(relatieId);

        if (soortCommunicatieProduct == SoortCommunicatieProduct.EMAIL && relatie.getEmailadres() == null) {
            throw new IllegalStateException();
        }

        List<JsonAdres> adressen = adresClient.lijst(SoortEntiteit.RELATIE.name(), relatieId);
        if (soortCommunicatieProduct == SoortCommunicatieProduct.BRIEF && adressen.isEmpty()) {
            throw new IllegalStateException();
        }
        if (soortCommunicatieProduct == null) {
            if (relatie.getIdentificatie() != null) {
                soortCommunicatieProduct = SoortCommunicatieProduct.EMAIL;
            } else if (!adressen.isEmpty()) {
                soortCommunicatieProduct = SoortCommunicatieProduct.BRIEF;
            } else {
                throw new IllegalStateException();
            }
        }

        CommunicatieProduct communicatieProduct = maakCommunicatieProduct(soortCommunicatieProduct, id);
        communicatieProduct.setSoortEntiteit(SoortEntiteit.RELATIE);
        communicatieProduct.setEntiteitId(relatieId);

        communicatieProduct.setTekst(tekst);
             communicatieProduct.setOnderwerp(onderwerp);

        if (antwoordOpId != null) {
            CommunicatieProduct antwoordOp = communicatieProductRepository.lees(antwoordOpId);
            communicatieProduct.setAntwoordOp(antwoordOp);
        }

        communicatieProduct.setMedewerker(medewerker);

        communicatieProductRepository.opslaan(communicatieProduct);

        return communicatieProduct.getId();
    }

    private CommunicatieProduct maakCommunicatieProduct(SoortCommunicatieProduct soortCommunicatieProduct, Long id) {
        if(id !=null){
            return communicatieProductRepository.lees(id);
        }
        if (soortCommunicatieProduct == SoortCommunicatieProduct.BRIEF) {
            return new UitgaandeBrief();
        }
        return new UitgaandeEmail();
    }

    public void markeerOmTeVerzenden(Long id){
        CommunicatieProduct communicatieProduct=communicatieProductRepository.lees(id);

        LOGGER.debug("markeerOmTeVerzenden {}", ReflectionToStringBuilder.toString(communicatieProduct, ToStringStyle.SHORT_PREFIX_STYLE));

        if(communicatieProduct.getDatumTijdVerzending()==null){

        if(communicatieProduct instanceof UitgaandeBrief){
maakBriefService.verzend((UitgaandeBrief)communicatieProduct);
        }else{
            OnverzondenIndicatie onverzondenIndicatie=new OnverzondenIndicatie();
            onverzondenIndicatie.setUitgaandeEmail((UitgaandeEmail)communicatieProduct);
            ((UitgaandeEmail)communicatieProduct).setOnverzondenIndicatie(onverzondenIndicatie);

            JsonRelatie relatie = relatieClient.lees(communicatieProduct.getEntiteitId());

            Emailadres emailadres=new Emailadres();
            emailadres.setUitgaandeEmail((UitgaandeEmail)communicatieProduct);
            emailadres.setEmailadres(relatie.getEmailadres());
            ((UitgaandeEmail)communicatieProduct).setEmailadres(emailadres);
        }

        communicatieProductRepository.opslaan(communicatieProduct);
    }
    }

    public List<CommunicatieProduct> leesOnverzondenCommunicatieProducten(){
        List<CommunicatieProduct> ret = new ArrayList<>();

        ret.addAll(communicatieProductRepository.leesOnverzondenBrieven());
        ret.addAll(communicatieProductRepository.leesOnverzondenEmails());

        return ret;
    }
}
