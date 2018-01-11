/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.google.translator;

import java.util.Locale;

import javax.script.ScriptException;

import org.junit.Test;

/**
 *
 * @author maxi
 */
public class GoogleTranslatorUnitTest {
    
//    Die Google Cloud Translation API kann dynamisch Text in Tausenden von Sprachpaaren übersetzen. Mit der Cloud Translation API können Websites und Programme den Übersetzungsdienst programmatisch integrieren."
    
    @Test
    public void testTranslatePositiveFrench() throws ScriptException {
        
        GoogleTranslatorImpl test = new GoogleTranslatorImpl();
        
//        https://translate.google.com/translate_a/single?client=t&sl=de&tl=en&hl=de&dt=at&dt=bd&dt=ex&dt=ld&dt=md&dt=qca&dt=rw&dt=rm&dt=ss&dt=t&ie=UTF-8&oe=UTF-8&source=bh&ssel=0&tsel=0&kc=1&tk=493408.126452&q=Hallo.%20Ich%20bin%20Max%20Mustermann%20und%20bin%2025%20Jahre%20alt.%20Ich%20freue%20mich.

//        System.out.println(test.translate(Locale.GERMAN, Locale.ENGLISH,"Hallo. Ich bin Max Mustermann und bin 25 Jahre alt. Ich freue mich."));
        
        System.out.println(test.translate(Locale.GERMAN, Locale.ENGLISH,"Was wird jetzt aus ihm?"));


    }
}
