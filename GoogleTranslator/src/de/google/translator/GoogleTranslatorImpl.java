/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.google.translator;

import de.google.translator.client.jersey.JerseyTranslatorClient;
import de.google.translator.client.response.GsonResponeParser;
import java.util.Locale;
import java.util.Optional;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author maxi
 */
public class GoogleTranslatorImpl implements GoogleTranslator {
    
    private final JerseyTranslatorClient translatorClient;

    public GoogleTranslatorImpl() {
        this.translatorClient = new JerseyTranslatorClient("https://translate.googleapis.com/translate_a/single", 
                            MediaType.APPLICATION_JSON_TYPE);
    }

    @Override
    public Optional<String> translate(Locale source, Locale target, String text) {
        
        Optional<Response> response = translatorClient.sendTranslateRequest(source, target, text);
        return translateBase(response);
    }

    @Override
    public Optional<String> translate(Locale target, String text) {

        Optional<Response> response = translatorClient.sendTranslateRequest(target, text);
        return translateBase(response);
    }
    
    private Optional<String> translateBase(Optional<Response> response) {
       
        if(!response.isPresent()) {
            return Optional.empty();
        }

        GsonResponeParser responseParser = new GsonResponeParser();
        return responseParser.determineTranslation(response.get());
    }
    
    @Override
    public void close() throws Exception {
        translatorClient.close();
    }
}
