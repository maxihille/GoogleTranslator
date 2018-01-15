/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.google.translator.token;

import de.google.translator.token.seed.GoogleTranslatorSeedFinder;
import de.google.translator.util.JavascriptOperators;

import java.util.ArrayList;
import java.util.List;
import javax.script.ScriptException;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author maxi
 */
public class GoogleTranslatTokenator {
    
    private final GoogleTranslatorSeedFinder seedFinder = new GoogleTranslatorSeedFinder();
    
    public String determineToken(String text) throws ScriptException {
        
        String seed =  seedFinder.determineCurrentSeed();
        
        List<Integer> charCodes = computeCharCodes(text);
        
        
        String[] seedParts = seed.split("\\.");
        
        long oldValueFromSeed = StringUtils.isEmpty(seed) && seed.contains(".") ? 0l : Long.parseLong(seedParts[0]);
        long numericalSeedBeforePoint = oldValueFromSeed;
        
        for(int index = 0; index<charCodes.size(); index++) {
            
            numericalSeedBeforePoint += charCodes.get(index);
            numericalSeedBeforePoint = compute(String.valueOf(numericalSeedBeforePoint), "+-a^+6");
        }
        
        numericalSeedBeforePoint = compute(String.valueOf(numericalSeedBeforePoint), "+-3^+b+-f");
        long oldSeedDecimalPlace = StringUtils.isEmpty(seed) && seed.contains(".") ? 0l : Long.parseLong(seedParts[1]);
        numericalSeedBeforePoint = JavascriptOperators.bitwiseXor(numericalSeedBeforePoint, oldSeedDecimalPlace) ;

        //This does not correspond to the JS part: 0 > a && (a = (a & 2147483647) + 2147483648);
        if(numericalSeedBeforePoint < 0) {
            
        	numericalSeedBeforePoint = JavascriptOperators.add(JavascriptOperators.bitwiseAnd(numericalSeedBeforePoint, 2147483647l), 2147483648l);
        }
        
        numericalSeedBeforePoint %= 1000000;
        
        long newSeedDecimalPlace = JavascriptOperators.bitwiseXor(numericalSeedBeforePoint, oldValueFromSeed);
        return Long.toString(numericalSeedBeforePoint).concat(".").concat(Long.toString(newSeedDecimalPlace));
    }

    private List<Integer> computeCharCodes(String text) {
        
    	//TODO here goes something wrong, the first index which differs is the 10th
    	//"ä" makes trouble
    	
        List<Integer> charCodes = new ArrayList<>();
        
        for(int fIndex = 0, gIndex = 0; gIndex < text.length(); gIndex++) {
            
            int charCode = text.codePointAt(gIndex);
            
            if(charCode < 128) {
                charCodes.add(fIndex++, charCode);
                
            } else {
                
                if(charCode < 2048) {
                    charCodes.add(fIndex++, charCode >> 6 |192);
               
                } else {
                   
                    if( (55296 == (charCode & 64512)) && (gIndex + 1 < text.length())
                            && (56320 == (text.codePointAt(gIndex + 1) & 64512))) {
                        charCode = 65536 + ((charCode & 1023) << 10) + (text.codePointAt(++gIndex) & 1023);
                        charCodes.add(fIndex++,  charCode >> 18 | 240);
                        charCodes.add(fIndex++,  charCode >> 12 & 63 | 128);
                    } else {
                        charCodes.add(fIndex++,  charCode >> 12 | 224);
                        charCodes.add(fIndex++,  charCode >> 6 & 63 | 128);
                    }
                }
                
                charCodes.add(fIndex++,  charCode & 63 | 128);
            }
        }
        
        return charCodes;
    }
    
    /**
     * 
     * @param numberAsText, normaly a Number like "4123231232"
     * @param computeString, "+-a^+6" or "+-3^+b+-f"
     * @return 
     * @throws ScriptException 
     */
    private long compute(String numberAsText, String computeString) throws ScriptException {

        long number = Long.parseLong(numberAsText);

        for(int index=0; index < computeString.length() - 2; index = index + 3) {
            
            char thirdChar = computeString.charAt(index + 2);
            
            //Determine charCode
            long charCode;
            
            if(thirdChar >= 'a') {
                charCode = Character.codePointAt("" + thirdChar, 0) - 87;
                
            } else {
                charCode = Character.getNumericValue(thirdChar);
            }
            
            if(computeString.charAt(index + 1) == '+') {
            	charCode = JavascriptOperators.zeroFillRightShift(number, charCode);
            } else {
            	
                //Cause int and long primitive type
                // ITS Problematic in JAVA because long is has more upper zeros so shift 3 from the right will create a bigger number
            	// Solution: Use Nashorn JS-Engine
            	
            	charCode = JavascriptOperators.leftShift(number, charCode);
            }
            
            if(computeString.charAt(index) == '+' ) {
            	number = JavascriptOperators.bitwiseAnd(JavascriptOperators.add(number, charCode), 4294967295l);
            } else {
            	number = JavascriptOperators.bitwiseXor(number, charCode);
            }

        }
        return number;
    }
}
