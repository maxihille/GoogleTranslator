package de.google.translator;

import de.google.translator.client.jersey.JerseyTranslatorClient;
import de.google.translator.client.response.GsonResponeParser;
import de.google.translator.sentence.strategy.AllInOneSplitStrategy;
import de.google.translator.sentence.strategy.SentenceSplitStrategy;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.script.ScriptException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author maxi
 */
public class GoogleTranslatorImpl implements GoogleTranslator {
    
    private final JerseyTranslatorClient translatorClient = new JerseyTranslatorClient("https://translate.google.com/translate_a/single", 
            MediaType.APPLICATION_JSON_TYPE);
    
    private final SentenceSplitStrategy sentenceSplitStrategy;
    
    public GoogleTranslatorImpl() {
        this.sentenceSplitStrategy = new AllInOneSplitStrategy();
    }
    
    public GoogleTranslatorImpl(SentenceSplitStrategy sentenceSplitStrategy) {
        this.sentenceSplitStrategy = sentenceSplitStrategy;
    }

    @Override
    public Optional<String> translate(Locale source, Locale target, String text) {
    	Optional<String> translation = doTranslation(source.getLanguage(), target.getLanguage(), text);
    	return translation;
    }
    
    @Override
    public Optional<String> translate(Locale target, String text) {
    	Optional<String> translation = doTranslation("auto", target.getLanguage(), text);
    	return translation;
    }

    /**
     * 
     * @param source
     * @param target
     * @param text
     * @return
     */
	private Optional<String> doTranslation(String sourceLanguage, String targetLanguage, String text) {
		
		List<String> sentences = sentenceSplitStrategy.splitTextIntoSentences(text);
    	
    	List<String> translatedSentences = sentences.stream().<Optional<Response>> map(sentence -> {
    		return determineTranslationResponse(sourceLanguage, targetLanguage, text);
		}).map(response -> parseTranslationResponse(response)).map(Optional::get).collect(Collectors.toList());
    	
    	Optional<String> translation = Optional.of(translatedSentences.stream().collect(Collectors.joining()));
		return translation;
	}

	/**
	 * 
	 * @param source
	 * @param target
	 * @param text
	 * @return
	 */
	private Optional<Response> determineTranslationResponse(String sourceLanguage, String targetLanguage, String text) {
		try {
			return translatorClient.sendTranslateRequest(sourceLanguage, targetLanguage, text);

		} catch (ScriptException e) {
			//TODO Log
			return Optional.empty();
		}
	}

	/**
	 * 
	 * @param response
	 * @return
	 */
    private Optional<String> parseTranslationResponse(Optional<Response> response) {
       
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
