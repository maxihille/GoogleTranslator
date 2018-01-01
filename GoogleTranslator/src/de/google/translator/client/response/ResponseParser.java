/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.google.translator.client.response;

import java.util.Optional;
import javax.ws.rs.core.Response;

/**
 *
 * @author maxi
 */
public interface ResponseParser {
    
    public Optional<String> determineTranslation(Response response);

}
