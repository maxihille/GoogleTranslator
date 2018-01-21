package de.google.translator.nlp.tokenizer;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.WordToSentenceProcessor;

/**
 * 
 * @author maxi
 *
 */
public class StandfordNaturalLanguageProcessor {

	private final CoreLabelTokenFactory coreLabelTokenFactory =  new CoreLabelTokenFactory();
	
	
	/**
	 * 
	 * @param text
	 * @return
	 */
	public List<String> determineSentences(String text) {
		
		List<CoreLabel> tokens = tokenizeText(text);
		
		// Split sentences from tokens
		List<List<CoreLabel>> sentences = new WordToSentenceProcessor<CoreLabel>().process(tokens);
		
		return buildRecognizedSentences(text, sentences);
	}

	private List<CoreLabel> tokenizeText(String paragraph) {
		
		List<CoreLabel> tokens = new ArrayList<CoreLabel>();
		
		PTBTokenizer<CoreLabel> tokenizer = new PTBTokenizer<CoreLabel>(new StringReader(paragraph), coreLabelTokenFactory, "");
		while (tokenizer.hasNext()) {
		    tokens.add(tokenizer.next());
		}
		return tokens;
	}
	
	private List<String> buildRecognizedSentences(String text, List<List<CoreLabel>> sentences) {
		List<String> recognizedSentences = sentences.stream().map(sentence -> {
			
			int begin = sentence.get(0).beginPosition();
			int end = sentence.get(sentence.size() - 1).endPosition();
			
			
			return text.substring(begin, end);
			
		}).collect(Collectors.toList());
		return recognizedSentences;
	}
	
}
