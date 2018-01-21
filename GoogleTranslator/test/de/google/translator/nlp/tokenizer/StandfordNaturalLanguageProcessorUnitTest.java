package de.google.translator.nlp.tokenizer;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public class StandfordNaturalLanguageProcessorUnitTest {

	
	//TODO Unit-Test this component with some SPIEGEL-Online Textes and some English Text
	
	private final StandfordNaturalLanguageProcessor nlpProcessor = new StandfordNaturalLanguageProcessor();
	
	@Test
	public void testSentenceSplittingGerman01() {
		
		
		List<String> singleSentences = nlpProcessor.determineSentences("Mit dem geplanten Interview-Aufbau muss man sich nicht lange aufhalten. Dazu ist Jones zu abgelenkt, von dem Teller Steak und Fritten vor ihr, von der sich zusehends leerenden Rotweinflasche, aber auch vom Tennismatch zwischen Rafael Nadal und Juan Martin del Potro auf dem Bildschirm. Zudem sprudelt die 69-Jährige viel zu sehr vor Einfällen, um allzu lange bei einem Thema zu verweilen.");
		assertEquals(3, singleSentences.size());
		assertEquals("Mit dem geplanten Interview-Aufbau muss man sich nicht lange aufhalten.", singleSentences.get(0));
		assertEquals("Dazu ist Jones zu abgelenkt, von dem Teller Steak und Fritten vor ihr, von der sich zusehends leerenden Rotweinflasche, aber auch vom Tennismatch zwischen Rafael Nadal und Juan Martin del Potro auf dem Bildschirm.", singleSentences.get(1));
		assertEquals("Zudem sprudelt die 69-Jährige viel zu sehr vor Einfällen, um allzu lange bei einem Thema zu verweilen.", singleSentences.get(2));
	}
}
