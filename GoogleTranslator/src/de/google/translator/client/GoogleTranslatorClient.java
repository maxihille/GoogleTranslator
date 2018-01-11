/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.google.translator.client;

import java.util.Optional;

import javax.script.ScriptException;
import javax.ws.rs.core.Response;

/**
 *
 * @author maxi
 */
public interface GoogleTranslatorClient extends AutoCloseable {
	
    public Optional<Response> sendTranslateRequest(String sourceLocaleLanguage, String targetLocaleLanguage, String text) throws ScriptException;

}
