package nl.lakedigital.djfc.service;

import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.*;
import org.junit.*;
import org.junit.runner.RunWith;
@Ignore
@RunWith(EasyMockRunner.class)
public class HtmlToPdfConversieServiceTest extends EasyMockSupport {
    @TestSubject
    private HtmlToPdfConversieService htmlToPdfConversieService=new HtmlToPdfConversieService();

    @Test
    public void testGenereerPdf() throws Exception {
//        htmlToPdfConversieService.maakAan("/Users/patrickheidotting/Documents/test.html");
        htmlToPdfConversieService.maakAan(moi());
    }

    private String moi(){
        return "<!DOCTYPE HTML>\n" +
                "<html>\n" +
                "    <head>\n" +
                "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=US-ASCII\" />\n" +
                "            <title>De Jonge</title>\n" +
                "            <!-- JQuery -->\n" +
                "            <link rel=\"stylesheet\" href=\"jquery/css/ui-lightness/jquery-ui-1.10.3.custom.css\" />\n" +
                "            <script type=\"text/javascript\" src=\"jquery/js/jquery-1.9.1.js\"></script>\n" +
                "            <script type=\"text/javascript\" src=\"jquery/js/jquery-ui-1.10.3.custom.min.js\"></script>\n" +
                "            \n" +
                "            <!-- Require JS -->\n" +
                "            <script src=\"commons/3rdparty/require.js\" data-main=\"commons/app.js\" type=\"text/javascript\"></script>\n" +
                "            \n" +
                "            <!-- Twitter Bootstrap -->\n" +
                "            <link rel=\"stylesheet\" href=\"bootstrap/css/bootstrap.min.css\" />\n" +
                "                <script src=\"bootstrap/js/bootstrap.min.js\"></script>\n" +
                "                \n" +
                "                <!-- Eigen CSS -->\n" +
                "                <link rel=\"stylesheet\" href=\"lakedigital.css\" />\n" +
                "                    \n" +
                "                    <script>\n" +
                "                        var refreshIntervalId = 0;\n" +
                "                        </script>\n" +
                "    </head>\n" +
                "    <body id=\"body\">\n" +
                "        <img id=\"homeKnop\" src=\"images/home.png\" alt=\"Naar de lijst met Relaties\" style=\"cursor:pointer; display:none;float:left;\" onclick=\"document.location.hash='#dashboard'\" />\n" +
                "        <div id=\"ingelogdeGebruiker\"></div>\n" +
                "        <div class=\"uitloggen\" id=\"uitloggen\">Uitloggen</div>\n" +
                "        <div id=\"menu\"></div>\n" +
                "        <div class=\"alert alert-success\" style=\"display:none;\" id=\"alertSucces\"></div>\n" +
                "        <div class=\"alert alert-danger\" style=\"display:none;\" id=\"alertDanger\"></div>\n" +
                "        <div id=\"content\">DIAS</div>\n" +
                "    </body>\n" +
                "</html>";
    }
}