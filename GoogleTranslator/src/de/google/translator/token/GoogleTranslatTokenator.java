/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.google.translator.token;

import de.google.translator.token.seed.GoogleTranslatorSeedFinder;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author maxi
 */
public class GoogleTranslatTokenator {
    
    private final GoogleTranslatorSeedFinder seedFinder = new GoogleTranslatorSeedFinder();
    
    public String determineToken(String text) {
        
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
        
        numericalSeedBeforePoint ^= StringUtils.isEmpty(seed) && seed.contains(".") ? 0l : Long.parseLong(seedParts[1]);


        if(numericalSeedBeforePoint > 0) {
            
            numericalSeedBeforePoint = (numericalSeedBeforePoint & 2147483647l) + 2147483648l;
        }
        
        numericalSeedBeforePoint %= 1000000;
        
        
        return Long.toString(numericalSeedBeforePoint).concat(".").concat(Long.toString(numericalSeedBeforePoint ^ oldValueFromSeed ));
    }

    private List<Integer> computeCharCodes(String text) {
        
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
                    
                    charCodes.add(fIndex++,  charCode >> 6 & 63 | 128);
                }
            }
        }
        
        return charCodes;
    }
    
    /**
     * 
     * @param numberAsText, normaly a Number like "4123231232"
     * @param computeString, "+-a^+6" or "+-3^+b+-f"
     * @return 
     */
    private long compute(String numberAsText, String computeString) {

        long number = Long.parseLong(numberAsText);

        //TODO Integer.parsing to Long.parsing.
        //Parameter Text shold be long 
        
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
                charCode = number >>> charCode;
            } else {
                //!!!!! THis seems to be wrong: Difference between JAVA:1258990779 << 3 and JS:1258990779 << 3 
                //Cause int and long primitive type
                // ITS Problematic in JAVA because long is has more upper zeros so shift 3 from the right will create a bigger number
                
                charCode = number << charCode; 
//                charCode = Math.toIntExact(number) << charCode; 

            }
            
            number = computeString.charAt(index) == '+' ? number + charCode & 4294967295l : number ^ charCode;
        }
        
        return number;
    }
    
    
    public static void main(String...args) {
        
        GoogleTranslatTokenator test = new GoogleTranslatTokenator();
        
//        System.out.println(1258990779 ^ 4294967295l); 
//        System.out.println(1258990779l ^ 4294967295l);
//        
//        System.out.println(1258990779 & 4294967295l); 
//        System.out.println(1258990779l & 4294967295l); 
//        
//        System.out.println(1258990779 >> 3); 
//        System.out.println(1258990779l >> 3); 
//
        
        
        System.out.println(1258990779 << 3); //JS 1481991640
        System.out.println(1258990779l << 3); //JS 1481991640
        
        System.out.println(Math.toIntExact(1258990779l) << 3); //JS 1481991640
        
        System.out.println(test.determineToken("Hallo. Ich bin Max Mustermann und bin 25 Jahre alt. Ich freue mich."));
    }
    
            
}
