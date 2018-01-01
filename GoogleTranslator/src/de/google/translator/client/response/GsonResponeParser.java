/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.google.translator.client.response;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Optional;
import javax.ws.rs.core.Response;

/**
 *
 * @author maxi
 */
public class GsonResponeParser implements ResponseParser {
    
    private final Gson gson = new Gson();
  
    @Override
    public Optional<String> determineTranslation(Response response) {
       
        String responseString = response.readEntity(String.class);
        ArrayList responseList = gson.fromJson(responseString, ArrayList.class);
        
        String translation = determineTranslationFormArrayList(responseList);
        
        return Optional.of(cleanTranslation(translation));
    }
    
    private String determineTranslationFormArrayList(ArrayList responseList) {
        
        Object stringObj =  ArrayList.class.cast(ArrayList.class.cast(responseList.get(0)).get(0)).get(0);
        return String.class.cast(stringObj);
    }
    
    private String cleanTranslation(String translation) {
        
        return translation.trim().replace("+", "").replaceAll(" +", " ");
    }
}
