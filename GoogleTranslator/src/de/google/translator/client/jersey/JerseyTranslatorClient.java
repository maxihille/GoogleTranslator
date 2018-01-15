/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.google.translator.client.jersey;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.script.ScriptException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.uri.UriComponent;

import de.google.translator.client.GoogleTranslatorClient;
import de.google.translator.token.GoogleTranslatTokenator;

/**
 *
 * @author maxi
 */
public class JerseyTranslatorClient implements GoogleTranslatorClient {

	// UNIT-Test for ArrayList
	// TODO Improve Performance: only once webTarget to get more performance ?! perhaps singleton or MapReduce for complete text an single threads 
	// TODO Ping to check if API is alive
	// TODO UNIT-Tests
	// TODO UTIL-Klasse für Locale Options
	// TODO Maven Javadoc
	// TODO UTIL-Klasse für GET-Parameter
	// TODO logging
	// TODO Splitten in SLätze...zusammenhängende Kontexte um die nicht max-length
	// der API zu verletzen

	private final Client client = ClientBuilder.newClient();

	private final String webResourceUri;

	private final MediaType mimeType;

	public JerseyTranslatorClient(String webResourceUri, MediaType mimeType) {
		this.webResourceUri = webResourceUri;
		this.mimeType = mimeType;
	}

	@Override
	public Optional<Response> sendTranslateRequest(String sourceLocaleLanguage, String targetLocaleLanguage,
			String text) throws ScriptException {
		return sendTranslateRequestBase(sourceLocaleLanguage, targetLocaleLanguage, text);
	}

	private Optional<Response> sendTranslateRequestBase(String sourceLanguage, String targetLanguage, String text)
			throws ScriptException {

		WebTarget webTarget;
		try {
			webTarget = createWebTarget(sourceLanguage, targetLanguage, text);

		} catch (UnsupportedEncodingException ex) {

			Logger.getLogger(JerseyTranslatorClient.class.getName()).log(Level.SEVERE, null, ex);
			return Optional.empty();
		}

		Invocation.Builder invocationBuilder = webTarget.request(mimeType);
		Response response = invocationBuilder.get();

		if (response.getStatus() != 200) {
			return Optional.empty();
		}

		return Optional.of(response);
	}

	private WebTarget createWebTarget(String sourceLanguage, String targetLanguage, String text)
			throws UnsupportedEncodingException, ScriptException {

		// TODO Disable/Enable Logging
		// Logger logger = Logger.getLogger(getClass().getName());
		// Feature feature = new LoggingFeature(logger, Level.INFO, null, null);
		// client.register(feature);

		WebTarget webTarget = client.target(webResourceUri);
		
		webTarget = webTarget.queryParam("client", "t").queryParam("sl", sourceLanguage)
				.queryParam("tl", targetLanguage).queryParam("hl", sourceLanguage)

				.queryParam("dt", "at").queryParam("dt", "bd").queryParam("dt", "ex").queryParam("dt", "ld")
				.queryParam("dt", "md").queryParam("dt", "qca").queryParam("dt", "rw").queryParam("dt", "rm")
				.queryParam("dt", "ss").queryParam("dt", "t")

				.queryParam("ie", "UTF-8").queryParam("oe", "UTF-8")

				.queryParam("source", "bh")

				.queryParam("ssel", 0).queryParam("tsel", 0).queryParam("kc", 1)
				.queryParam("tk", new GoogleTranslatTokenator().determineToken(text))
				.queryParam("q", UriComponent.encode(text, UriComponent.Type.QUERY_PARAM_SPACE_ENCODED));
		return webTarget;
	}

	@Override
	public void close() {
		client.close();
	}

}
