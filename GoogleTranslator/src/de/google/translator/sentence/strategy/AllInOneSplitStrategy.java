package de.google.translator.sentence.strategy;

import java.util.Collections;
import java.util.List;

public class AllInOneSplitStrategy implements SentenceSplitStrategy {

	@Override
	public List<String> splitTextIntoSentences(String text) {
		return Collections.singletonList(text);
	}
}
