package nl.lakedigital.djfc.service;


import com.lowagie.text.DocumentException;
import nl.lakedigital.djfc.client.oga.BijlageClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.inject.Inject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class HtmlToPdfConversieService {
    private StringBuffer stringBufferOfData;
    private final static Logger LOGGER = LoggerFactory.getLogger(HtmlToPdfConversieService.class);

    @Inject
    private BijlageClient bijlageClient;

    public String maakAan(String input) {
        stringBufferOfData = new StringBuffer();

        String root = bijlageClient.getUploadPad();
        String bestandsnaam = bijlageClient.genereerBestandsnaam();
        String outputPDF = root + File.separator+ bestandsnaam +".pdf";

        OutputStream os = null;
        try {
            os = new FileOutputStream(new File(outputPDF));
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(input);
            renderer.layout();
            renderer.createPDF(os, false);
            renderer.layout();
            renderer.writeNextDocument();
            renderer.finishPDF();
            os.close();
            os = null;
        } catch (IOException | DocumentException e) {
            LOGGER.error("{}", e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    LOGGER.error("{}", e);
                }
            }
        }

        return bestandsnaam;
    }
}