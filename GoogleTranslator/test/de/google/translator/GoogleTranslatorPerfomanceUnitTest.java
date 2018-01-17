package de.google.translator;

import static org.junit.Assert.assertEquals;

import java.util.Locale;

import org.junit.Test;

public class GoogleTranslatorPerfomanceUnitTest {

	private static final int TRANSLATION_LOOPS = 10;
	
	private final GoogleTranslator googleTranslator = new GoogleTranslatorImpl();
	
	/**
	 * Should take about 18-22 secs, but this test is used to measure improvements
	 */
	@Test
	public void testPerformanceMultipleTranslationCalls() {
		
		for(int counter=0; counter<TRANSLATION_LOOPS; counter++) {
			assertEquals("In addition, 20,000 new jobs are to be created in existing locations, and Apple also wants to open a new campus for its employees.", googleTranslator.translate(Locale.ENGLISH, "Zudem sollen 20.000 neue Jobs an bereits existierenden Standorten entstehen, ferner wolle Apple einen neuen Campus für seine Mitarbeiterinnen und Mitarbeiter eröffnen.").get());
			assertEquals("One of the objectives of the recently enacted tax reform by President Donald Trump is to redirect these funds back to the United States.", googleTranslator.translate(Locale.ENGLISH, "Ziel der jüngst in Kraft getretenen Steuerreform von Präsident Donald Trump ist es unter anderem, diese Gelder wieder in die Vereinigten Staaten umzuleiten.").get());
		}
	}
	
	//TODO UNit-Test with one long text to be splitted into single parts
}
