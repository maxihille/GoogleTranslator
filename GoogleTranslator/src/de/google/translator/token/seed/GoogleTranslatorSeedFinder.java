/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.google.translator.token.seed;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.google.translator.token.GoogleTranslatTokenator;

/**
 *
 * @author maxi
 */
public class GoogleTranslatorSeedFinder {

	// TODO Unit-Tests for Regexp
	private final ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
	private final Client client = ClientBuilder.newClient();
	private final Pattern tkkRegexPattern = Pattern.compile("TKK=((.*?)\\(\\)\\)'\\);)");

	public String determineCurrentSeed() {

		String htmlContent = fetchHtmlPageContent();
		Matcher matches = tkkRegexPattern.matcher(htmlContent);

		if (matches.find()) {

			String javascriptExpression = matches.group(1);

			return evalJavascriptExpression(javascriptExpression);

		}

		return "";
	}

	private String evalJavascriptExpression(String javascriptExpression) {

		try {

			String googleToken = String.class.cast(engine.eval(javascriptExpression));
			return googleToken;

		} catch (ScriptException ex) {
			Logger.getLogger(GoogleTranslatTokenator.class.getName()).log(Level.SEVERE, null, ex);
		}

		return "";
	}

	private String fetchHtmlPageContent() {

		WebTarget webTarget = client.target("https://translate.google.com");
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.TEXT_HTML_TYPE);
		Response response = invocationBuilder.get();

		if (response.getStatus() != 200) {
			// TODO throw error
		}

		String htmlContent = response.readEntity(String.class);
		return htmlContent;
	}
}
