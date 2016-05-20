package nl.lakedigital.djfc.inloggen;

import javax.servlet.http.HttpServletRequest;

public final class Sessie {
    private Sessie() {
    }

    // Zodat Hibernate Envers deze uit kan lezen..
    private static Long ingelogdeGebruiker;

    public static Long getIngelogdeGebruiker() {
        return ingelogdeGebruiker;
    }

    public static void setIngelogdeGebruiker(Long ingelogdeGebruiker) {
        Sessie.ingelogdeGebruiker = ingelogdeGebruiker;
    }

    public static void setIngelogdeGebruiker(HttpServletRequest httpServletRequest) {
        String ingelogdeGebruiker = httpServletRequest.getHeader("ingelogdeGebruiker");
        if (ingelogdeGebruiker != null) {
            Sessie.ingelogdeGebruiker = Long.valueOf(ingelogdeGebruiker);
        }
    }
}
