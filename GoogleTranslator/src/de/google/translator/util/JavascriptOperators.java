package de.google.translator.util;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;


public class JavascriptOperators {

	//TODO Unit-Test
	
	//Own Project / Library / Module ??
	
    private static final ScriptEngine jsEngine = new ScriptEngineManager().getEngineByName("nashorn");


    /**
     * Returns a one in each bit position for which the corresponding bits of both operands are ones.
     * @return
     * @throws ScriptException 
     */
    public static long bitwiseAnd(long operandOne, long operandTwo) throws ScriptException {
    	
    	return executeJavascriptOperation(operandOne, operandTwo, "&");
    }
    
    /**
     * Executes Addition.
     * 
     * @param operandOne
     * @param operandTwo
     * @return
     * @throws ScriptException
     */
    public static long add(long operandOne, long operandTwo) throws ScriptException {
    	
    	return executeJavascriptOperation(operandOne, operandTwo, "+");
    }
    
    /**
     * Returns a one in each bit position for which the corresponding bits of either but not both operands are ones.
     * @param operandOne
     * @param operandTwo
     * @return
     * @throws ScriptException
     */
    public static long bitwiseXor(long operandOne, long operandTwo) throws ScriptException {
    	
    	return executeJavascriptOperation(operandOne, operandTwo, "^");
    }
    
    
    /**
     * Shifts a in binary representation b (< 32) bits to the left, shifting in zeroes from the right.
     * @param operandOne
     * @param operandTwo
     * @return
     * @throws ScriptException
     */
    public static long leftShift(long operandOne, long operandTwo) throws ScriptException {
    	
        return executeJavascriptOperation(operandOne, operandTwo, "<<");
    }
    
	
    /**
     * Shifts a in binary representation b (< 32) bits to the right, discarding bits shifted off, and shifting in zeroes from the left.
     * @param operandOne
     * @param operandTwo
     * @return
     * @throws ScriptException
     */
    public static long zeroFillRightShift(long operandOne, long operandTwo) throws ScriptException {
        
        return executeJavascriptOperation(operandOne, operandTwo, ">>>");
    }
    
    private static long executeJavascriptOperation(long operandOne, long operandTwo, String operator) throws ScriptException {
    	
    	String jsStatement = removeLongLiteral(operandOne) + operator + removeLongLiteral(operandTwo);
        Number result = evalJavascriptStatement(jsStatement);
        
        return result.longValue();
    }
    
    
    private static Number evalJavascriptStatement(String jsStatement) throws ScriptException {
    	
    	Object result = jsEngine.eval(jsStatement);
    	
    	if(!(result instanceof Number)) {
    		throw new IllegalArgumentException("Passed JS-Statement does not delive a Number.");
    	}
    	
    	return Number.class.cast(result);
    }
    

	private static String removeLongLiteral(long longNumber) {
    	
		return String.valueOf(longNumber).replace("l", "");
    }
}
