/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.google.translator.token;

import de.google.translator.token.seed.GoogleTranslatorSeedFinder;
import de.google.translator.util.JavascriptOperators;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
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
     * @throws ScriptException 
     */
    private long compute(String numberAsText, String computeString) throws ScriptException {

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
    
    
    public static void main(String...args) throws ScriptException {
        
        GoogleTranslatTokenator test = new GoogleTranslatTokenator();
        
        
        //JS
    	//3293726314 & 4294967295
        //-1001240982
        //JSBinary 
        //3293726314	-> 11000100010100100100011001101010
        //4294967295	-> 11111111111111111111111111111111	
        //1001240982	-> 11000100010100100100011001101010
        
//        System.out.println(Long.toBinaryString(3293726314l));
//        System.out.println(Long.toBinaryString(4294967295l));
//        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
//        System.out.println(Long.toBinaryString(Long.MAX_VALUE << 1 | 1));
//        System.out.println(Long.toBinaryString(Long.MIN_VALUE + Long.MAX_VALUE));
//
//        
//        System.out.println(Long.toBinaryString(3293726314l & 4294967295l));
//        //Get highest bit an fill with bits from the right to match 63 bits at all
//        
//        BitSet longBitSet =  BitSet.valueOf(new long[] {3293726314l & 4294967295l});
//        System.out.println(longBitSet.nextSetBit(0));
//        System.out.println(longBitSet.nextSetBit(1));
//        System.out.println(longBitSet.nextSetBit(12));
////        longBitSet.set(62);
////        longBitSet.set(61);
//        
//        ScriptEngine jsEngine = new ScriptEngineManager().getEngineByName("nashorn");
//        Number result = Number.class.cast(jsEngine.eval("3293726314 & 4294967295"));
//        System.out.println(result.longValue());
//        
//        
//        System.out.println(Long.toBinaryString(longBitSet.toLongArray()[0])+ " das");
//        
//        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
//        Object tmp = engine.eval("1 + 1;");
//        System.out.println("QUARK" + tmp);
//        
//        System.out.println(Long.toBinaryString(3293726314l & (Long.MAX_VALUE << 1 | 1)));
//        System.out.println(Long.toBinaryString(-1001240982l >> 2) + " P");
//        
//        
//        System.out.println(binaryStringToLong("1111111111111111111111111111111111000100010100100100011001101010"));
//        System.out.println(binaryStringToLong("1111111111111111111111111111111100000000000000000000000000000000"));
//        
//        System.out.println(Integer.toBinaryString(-1001240982));


        System.out.println(test.determineToken("Hallo. Ich bin Max Mustermann und bin 25 Jahre alt. Ich freue mich."));
    }
    
    public static int binaryStringToInteger(String binaryString) {
    	
    	if(!binaryString.matches("^[01]{32,32}")) {
    		throw new IllegalArgumentException("The expected format of the string is: ^[01]{32,32} ");
    	}
    	
    	return Integer.parseInt(binaryString, 2);
    }
    
    public static long binaryStringToLong(String binaryString) {
    	
    	if(!binaryString.matches("^[01]{64,64}")) {
    		throw new IllegalArgumentException("The expected format of the string is: ^[01]{64,64} ");
    	}
    	return Long.parseUnsignedLong(binaryString, 2);
    }
            
}
