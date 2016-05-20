package nl.lakedigital.djfc.service.verzenden;

import nl.lakedigital.djfc.domain.CommunicatieProduct;
import nl.lakedigital.djfc.repository.CommunicatieProductRepository;
import nl.lakedigital.djfc.service.CommunicatieProductService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.*;
import java.util.function.Predicate;

@Service
public class VerzendService {
    @Inject
    private List<AbstractVerzendService> verzendServiceList;
    @Inject
    private CommunicatieProductService communicatieProductService;

    public void verzend(){
        List<CommunicatieProduct> communicatieProducten=communicatieProductService.leesOnverzondenCommunicatieProducten();

        for(final CommunicatieProduct communicatieProduct:communicatieProducten){
            verzendServiceList.stream().filter(new Predicate<AbstractVerzendService>() {
                @Override
                public boolean test(AbstractVerzendService abstractVerzendService) {
                    return abstractVerzendService.isVoorMij(communicatieProduct);
                }
            }).findFirst().get().verzend(communicatieProduct);
        }

        communicatieProductService.opslaan(communicatieProducten);
    }
}
