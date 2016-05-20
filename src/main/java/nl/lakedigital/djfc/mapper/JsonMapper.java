package nl.lakedigital.djfc.mapper;

import org.springframework.stereotype.Component;

@Component
public interface JsonMapper {
    boolean isVoorMij(Object object);

}
