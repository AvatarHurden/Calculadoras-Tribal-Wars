package database;

import java.math.BigDecimal;
import java.math.BigInteger;
//sqrt-function copied of BigFunctionsClass
//From The Java Programmers Guide To numerical Computing (Ronald Mak, 2003)
public class BigOperation {
    /**
     * Compute the square root of x to a given scale, x >= 0.
     * Use Newton's algorithm.
     * @param x the value of x
     * @param scale the desired scale of the result
     * @return the result value
     */
    public static BigDecimal sqrt(BigDecimal x, int scale) {
        // Check that x >= 0.
        if (x.signum() < 0) {
            throw new IllegalArgumentException("x < 0");
        }
        
        // If x == 0, return 0.
        if (x.signum() == 0) {
        	return BigDecimal.ZERO;
        }
 
        // n = x*(10^(2*scale))
        BigInteger n = x.movePointRight(scale << 1).toBigInteger();
 
        // The first approximation is the upper half of n.
        int bits = (n.bitLength() + 1) >> 1;
        BigInteger ix = n.shiftRight(bits);
        BigInteger ixPrev;
 
        // Loop until the approximations converge
        // (two successive approximations are equal after rounding).
        do {
            ixPrev = ix;
 
            // x = (x + n/x)/2
            ix = ix.add(n.divide(ix)).shiftRight(1);
 
            Thread.yield();
        } while (ix.compareTo(ixPrev) != 0);
 
        return new BigDecimal(ix, scale);
    }

    /**
     * Formats <code>number</code> to have, at most, <code>digitsToDisplay</code> digits. 
     * 
     * Useful to reduce the precision of the <code>BigDecimal</code> or to display the number on a GUI.
     * Uses <code>BigDecimal.ROUND_HALF_EVEN</code> as a rounding mode.
     * 
     * @param number <code>BigDecimal</code> to be formated
     * @param digits number of digits the new <code>BigDecimal</code> will have
     * @return <code>BigDecimal</code> with <code>digits</code> digits
     */
	public static BigDecimal format(BigDecimal number, int digits){
		
		try {
		
			if (number.signum() == 0) return BigDecimal.ZERO;
		
			double numDigits;
			numDigits = Math.log10(number.doubleValue());
			int decimalsDisplayed = digits - (int)numDigits;
		
			number = number.setScale(decimalsDisplayed, BigDecimal.ROUND_HALF_EVEN);
			number = number.stripTrailingZeros();
		
			try {
				number.intValueExact();
				number = number.setScale(0, BigDecimal.ROUND_HALF_EVEN);
			} catch (Exception e) {}
		
			return number;
		
		} catch (NullPointerException e) {
			
			return BigDecimal.ZERO;
		}
	}

	/**
     * Raises <code>n1</code> to <code>n2</code> power. 
     * 
     * Can raise to negative and fractional powers using aproximation.
     * 
     * @param n1 <code>BigDecimal</code> to be raised
     * @param n2 <code>BigDecimal</code> power
     * @return result in <code>BigDecimal</code> 
     */
	public static BigDecimal pow(BigDecimal n1, BigDecimal n2){

		BigDecimal result = null;
		int signOf2 = n2.signum();
		
	    try {
	        // Perform X^(A+B)=X^A*X^B (B = remainder)
	        double dn1 = n1.doubleValue();
	        
	        n2 = n2.multiply(new BigDecimal(signOf2)); // n2 is now positive
	        
	        BigDecimal remainderOf2 = n2.remainder(BigDecimal.ONE);
	        BigDecimal n2IntPart = n2.subtract(remainderOf2);
	        
	        // Calculate big part of the power using context -
	        // bigger range and performance but lower accuracy
	        BigDecimal intPow = n1.pow(n2IntPart.intValueExact());
	        
	        BigDecimal doublePow =
	            new BigDecimal(Math.pow(dn1, remainderOf2.doubleValue()));
	        
	        result = intPow.multiply(doublePow);
	    } catch (Exception e) {}
	    
	    // Fix negative power
	    if (signOf2 == -1)
	        result = BigDecimal.ONE.divide(result,30,BigDecimal.ROUND_HALF_EVEN);
	    
	    return result;
		
	}
}