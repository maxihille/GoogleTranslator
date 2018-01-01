/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.google.translator;

import java.util.Locale;
import java.util.Optional;

/**
 *
 * @author maxi
 */
public interface GoogleTranslator extends AutoCloseable {
    
    public Optional<String> translate(Locale source, Locale target, String text);
    
    public Optional<String> translate(Locale target, String text);
}

