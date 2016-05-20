package nl.lakedigital.djfc.service.verzenden;

import nl.lakedigital.djfc.domain.CommunicatieProduct;

public abstract class AbstractVerzendService {
    public abstract void verzend(CommunicatieProduct communicatieProduct);
    public abstract boolean isVoorMij(CommunicatieProduct communicatieProduct);
}
