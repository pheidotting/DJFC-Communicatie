package nl.lakedigital.djfc.web.controller;

import nl.lakedigital.djfc.commons.json.JsonCommunicatieProduct;
import nl.lakedigital.djfc.commons.json.OpslaanCommunicatieProduct;
import nl.lakedigital.djfc.domain.CommunicatieProduct;
import nl.lakedigital.djfc.domain.SoortEntiteit;
import nl.lakedigital.djfc.mapper.Mapper;
import nl.lakedigital.djfc.service.CommunicatieProductService;
import nl.lakedigital.djfc.service.ontvangen.LeesEnVerwerkEmailService;
import nl.lakedigital.djfc.service.verzenden.VerzendService;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/communicatieproduct")
public class CommunicatieProductController {
    private final static Logger LOGGER = LoggerFactory.getLogger(CommunicatieProductController.class);

    @Inject
    private CommunicatieProductService communicatieProductService;
    @Inject
    private Mapper mapper;
    @Inject
    private LeesEnVerwerkEmailService leesEnVerwerkEmailService;
    @Inject
    private VerzendService verzendService;

    @RequestMapping(method = RequestMethod.GET, value = "/alles/{soortentiteit}/{parentid}")
    @ResponseBody
    public List<JsonCommunicatieProduct> alles(@PathVariable("soortentiteit") String soortentiteit, @PathVariable("parentid") Long parentid) {
        List<JsonCommunicatieProduct> result = new ArrayList<>();

        for (CommunicatieProduct communicatieProduct : communicatieProductService.lijst(SoortEntiteit.valueOf(soortentiteit), parentid)) {
            result.add(mapper.map(communicatieProduct, JsonCommunicatieProduct.class));
        }

        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/nieuw")
    @ResponseBody
    public Long nieuwCommunicatieProduct(@RequestBody OpslaanCommunicatieProduct opslaanCommunicatieProduct) {
        LOGGER.debug("Opslaan {}", ReflectionToStringBuilder.toString(opslaanCommunicatieProduct, ToStringStyle.SHORT_PREFIX_STYLE));
        return communicatieProductService.maakCommunicatieProduct(opslaanCommunicatieProduct.getId(), CommunicatieProductService.SoortCommunicatieProduct.valueOf(opslaanCommunicatieProduct.getSoortCommunicatieProduct().name()),//
                opslaanCommunicatieProduct.getParentid(), opslaanCommunicatieProduct.getTekst(), opslaanCommunicatieProduct.getOnderwerp(), null, opslaanCommunicatieProduct.getMedewerker());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/versturen/{id}")
    @ResponseBody
    public void versturen(@PathVariable Long id) {

    }

    @RequestMapping(method = RequestMethod.GET, value = "/leesmails")
    @ResponseBody
    public void leesMails() {
        leesEnVerwerkEmailService.leesEnVerwerkEmails();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/verstuur")
    @ResponseBody
    public void verstuur() {
        verzendService.verzend();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/markeerOmTeVerzenden/{id}")
    @ResponseBody
    public void markeerOmTeVerzenden(@PathVariable("id") Long id) {
        communicatieProductService.markeerOmTeVerzenden(id);
    }


}
