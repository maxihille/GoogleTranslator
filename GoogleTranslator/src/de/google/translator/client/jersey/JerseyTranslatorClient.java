/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.google.translator.client.jersey;

import de.google.translator.client.GoogleTranslatorClient;
import de.google.translator.token.GoogleTranslatTokenator;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author maxi
 */
public class JerseyTranslatorClient implements GoogleTranslatorClient {
    
    
    //UNIT-Test for ArrayList
    //TODO Ping to check if API is alive
    //TODO UNIT-Tests
    //TODO UTIL-Klasse für Locale Options
    //TODO Maven Javadoc
    //TODO UTIL-Klasse für GET-Parameter
    //TODO logging
    //TODO Splitten in SLätze...zusammenhängende Kontexte um die nicht max-length der API zu verletzen
        

    private final Client client = ClientBuilder.newClient();
    
    private final String webResourceUri;
    
    private final MediaType mimeType;
    
    
    public JerseyTranslatorClient(String webResourceUri, MediaType mimeType) {
        this.webResourceUri = webResourceUri;
        this.mimeType = mimeType;
    }
    
    @Override
    public Optional<Response> sendTranslateRequest(Locale target, String text) {
        return sendTranslateRequestBase("auto", target.getLanguage(), text);
    }
    
    @Override
    public Optional<Response> sendTranslateRequest(Locale source, Locale target, String text) {
        
        return sendTranslateRequestBase(source.getLanguage(), target.getLanguage(), text);
    }

    private Optional<Response> sendTranslateRequestBase(String sourceLanguage, String targetLanguage, String text) {
       
        WebTarget webTarget;
        try {
            webTarget = createWebTarget(sourceLanguage, targetLanguage, text);
            
        } catch (UnsupportedEncodingException ex) {
            
            Logger.getLogger(JerseyTranslatorClient.class.getName()).log(Level.SEVERE, null, ex);
            return Optional.empty();
        }

        Invocation.Builder invocationBuilder =  webTarget.request(mimeType);
        Response response = invocationBuilder.get();

        if (response.getStatus() != 200) {
            return Optional.empty();
        }

        return Optional.of(response);
    }



    private WebTarget createWebTarget(String sourceLanguage, String targetLanguage, String text) throws UnsupportedEncodingException {

        
        WebTarget webTarget = client.target(webResourceUri);
        webTarget = webTarget.queryParam("client", "t") 
                .queryParam("sl", sourceLanguage)
                .queryParam("tl", targetLanguage)
                .queryParam("hl", targetLanguage)
                
                .queryParam("dt", "at")
                .queryParam("dt", "bd")
                .queryParam("dt", "ex")
                .queryParam("dt", "ld")
                .queryParam("dt", "md")
                .queryParam("dt", "qca")
                .queryParam("dt", "rw")
                .queryParam("dt", "rm")
                .queryParam("dt", "ss")
                .queryParam("dt", "t")
                
                .queryParam("ie", "UTF-8")
                .queryParam("oe", "UTF-8")
                
                .queryParam("source", "bh")
//                .queryParam("otf", 1)
                .queryParam("ssel", 0)
                .queryParam("tsel", 0)
                .queryParam("kc", 1)
                .queryParam("tk", new GoogleTranslatTokenator().determineToken(text))
                
                .queryParam("q", URLEncoder.encode(text, "UTF-8"));
        return webTarget;
    }

    @Override
    public void close() {
        client.close();
    }
}
