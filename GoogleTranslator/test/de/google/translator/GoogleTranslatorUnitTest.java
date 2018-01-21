/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.google.translator;

import static org.junit.Assert.assertEquals;

import java.util.Locale;

import javax.script.ScriptException;

import org.junit.Test;

/**
 *
 * @author maxi
 */
public class GoogleTranslatorUnitTest {
    
	
	private final GoogleTranslatorImpl googleTranslator = new GoogleTranslatorImpl();
	
    @Test
    public void testTranslateGermanToEnglishOneSentence() throws ScriptException {
    	assertEquals("What will happen to him now?", googleTranslator.translate(Locale.GERMAN, Locale.ENGLISH,"Was wird jetzt aus ihm?").get());
    	assertEquals("I am Max Mustermann.", googleTranslator.translate(Locale.GERMAN, Locale.ENGLISH,"Ich bin Max Mustermann.").get());
    	assertEquals("The decline of the department store chain has long been apparent.", googleTranslator.translate(Locale.GERMAN, Locale.ENGLISH,"Der Niedergang der Kaufhauskette zeichnet sich seit Langem ab.").get());
    	assertEquals("The business is now in the sixth generation in family ownership - with twelve tribes with around 1,800 relatives a complicated undertaking, which the Brenninkmeijers regard as a precious achievement.", googleTranslator.translate(Locale.GERMAN, Locale.ENGLISH,"Das Geschäft hält sich inzwischen in sechster Generation in Familienbesitz - bei zwölf Stämmen mit rund 1800 Angehörigen ein kompliziertes Unterfangen, was die Brenninkmeijers als kostbare Leistung betrachten.").get());
    }
    
    @Test
    public void testTranslateGermanToEnglishOneSentenceNoSourceLanguage() throws ScriptException {
    	assertEquals(googleTranslator.translate(Locale.GERMAN, Locale.ENGLISH,"Was wird jetzt aus ihm?").get(), googleTranslator.translate(Locale.ENGLISH,"Was wird jetzt aus ihm?").get());
    	assertEquals(googleTranslator.translate(Locale.GERMAN, Locale.ENGLISH,"Ich bin Max Mustermann.").get(), googleTranslator.translate(Locale.ENGLISH,"Ich bin Max Mustermann.").get());
    	assertEquals(googleTranslator.translate(Locale.GERMAN, Locale.ENGLISH,"Der Niedergang der Kaufhauskette zeichnet sich seit Langem ab.").get(), googleTranslator.translate(Locale.ENGLISH,"Der Niedergang der Kaufhauskette zeichnet sich seit Langem ab.").get());
    	assertEquals(googleTranslator.translate(Locale.GERMAN, Locale.ENGLISH,"Das Geschäft hält sich inzwischen in sechster Generation in Familienbesitz - bei zwölf Stämmen mit rund 1800 Angehörigen ein kompliziertes Unterfangen, was die Brenninkmeijers als kostbare Leistung betrachten.").get(), googleTranslator.translate(Locale.GERMAN, Locale.ENGLISH,"Das Geschäft hält sich inzwischen in sechster Generation in Familienbesitz - bei zwölf Stämmen mit rund 1800 Angehörigen ein kompliziertes Unterfangen, was die Brenninkmeijers als kostbare Leistung betrachten.").get());
    }
    
    //TODO Unit-Tests: English to German, French, Spain, Italy, Russian, Greece
    
}
