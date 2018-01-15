package de.google.translator.sentence.strategy;

import java.util.List;

public interface SentenceSplitStrategy {

	public List<String> splitTextIntoSentences(String text);
}
