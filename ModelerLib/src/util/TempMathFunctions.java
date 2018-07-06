package util;
   
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import util.math.RBeta;
import util.math.RBinom;
import util.math.RExp;
import util.math.RGamma;
import util.math.RPois;
import util.math.RUnif;
 

/**
 * The Class TempMathFunctions.
 */
public class TempMathFunctions {

	
	/**
	 * Instantiates a new temp math functions.
	 */
	public TempMathFunctions()
	{
	}
	 
	// value = 60.00000001
	//			  12345678<-
	// return 8
	public static String findLocationMostSignificantDigit(double value) {
		value = value - Math.floor(value);
		
	    value = Math.abs(value);
	    if (value == 0) 
	    	return "#";
	    
	    String str = "#.";
	    
	    while (value < 1) {
	    	value *= 10;
	    	str += "0";
	    } 
	    
	    return str;
	}
	
	// value = 60.00000001
	// return 6
	public int getMostSignificantDigit(double value) {
	    value = Math.abs(value);
	    if (value == 0) 
	    	return 0;
	    
	    int count = 0;
	    while (value < 1) {
	    	value *= 10;
	    	count++;
	    }
	    
	    char firstChar = String.valueOf(value).charAt(0);
	    
	    return Integer.parseInt(firstChar + "");
	}
	
	// value = 60.00000001
	// return 1
	public int getMostSignificantDigitFromDecimalPoint(double value) {  
		return getMostSignificantDigit(value - Math.floor(value)); 
	}
	
	public static String safeDoubleAsString(Double value) {    
		String format = findLocationMostSignificantDigit(value); 
		DecimalFormat decimalFormat = new DecimalFormat(format);
		String numberAsString = decimalFormat.format(value);
		return numberAsString; 
	}
	
	public static String getSafeNumberString(String s) {
		if (isNum(s))
			return safeDoubleAsString(Double.valueOf(s));

		return s;
	}
	
	//e.g.,) data = "-.5E5" is changed to "-50000.0"
	
	public String safeDoubleAsString2(Double data) {   
		DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
		df.setMaximumFractionDigits(340); //340 = DecimalFormat.DOUBLE_FRACTION_DIGITS 
		return df.format(data);
	}
	
	public  String getSafeNumberString2(String data) {
		if (isNum(data)){
			return safeDoubleAsString2(Double.valueOf(data));
		}
		return data;
	}
	
	/**
	 * Checks if is number.
	 *
	 * @param s the s
	 * @return true, if is number
	 */
	public static boolean isNum(Object s) {
		if( s == null )
			return false;
		
		return isNum(s.toString());
	}
	
	/**
	 * Checks if is number.
	 *
	 * @param s the s
	 * @return true, if is number
	 */
	public static boolean isNum(String s) {  
		if( s == null )
			return false;
		
		try {
			Double.parseDouble(s);
		}
		catch (NumberFormatException nfe) {
			return false;
		}
		
		return true;
	}

	/**
	 * Gets the random on three.
	 *
	 * @param ex the exception 
	 * @return the random on three
	 */
	public int getRandomOnThree(int... ex){
		int r = 0;
		boolean c = true;
		while(c){
			c = false;
			r = getRandomOnThree();
			for( int i = 0; i < ex.length; i++ ){
				if( ex[i] == r ){
					c = true;
					break;
				}
			}
		}
		
		return r;
	}
	
	
	/**
	 * Gets the random on three.
	 *
	 * @return the random on three
	 */
	public int getRandomOnThree(){
		Double d = Math.random();
		if( d < 0.33333 )
			return 0;
		if ( 0.33333 < d && d <= 0.66666 )
			return 1;
		if ( 0.66666  < d )
			return 2;
		
		return 0;
	}
	
	/**
	 * Gets the distance.
	 *
	 * @param x1 the x1
	 * @param y1 the y1
	 * @param x2 the x2
	 * @param y2 the y2
	 * @return the distance
	 */
	public double getDistance(double x1, double y1, double x2, double y2) {
		return Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
	}
	
	/**
	 * Checks if is number with infinity.
	 *
	 * @param s the s
	 * @return true, if is number with infinity
	 */
	public static boolean isNumWithINF(String s) { 
		if( s.equals("PINF") || s.equals("NINF"))
			return true;
		
		return isNum(s);
	}
	
	/**
	 * Triangular.
	 *
	 * @param a the a value
	 * @param b the b value
	 * @param c the c value
	 * @return the double
	 */
	public double triangular(double a, double b, double c)
	{ 
		if( a < c && c < b ) 
		{
			double U = Math.random();
			if(U <= (c-a)/(b-a))
				return a + Math.sqrt((c-a)*(b-a)*U);
			else // if( (c-a)/(b-a) < U <= 1)
				return b - Math.sqrt((b-a)*(b-c)*(1-U));
		}
		else 
		{
			System.out.println("Invalid input !\n");
			return 0;
		}
	}

	/**
	 * Uniform.
	 *
	 * @param a the a value
	 * @param b the b value
	 * @return the double
	 */
	public double uniform(double a, double b) 
	{
		return RUnif.Get(a, b);
	}

	/**
	 * Exponential.
	 *
	 * @param m the value
	 * @return the double
	 */
	public double exponential(double m) 
	{
		return RExp.Get(m);
	}

	/**
	 * Normal.
	 *
	 * @param m the mean
	 * @param s the standard variance
	 * @return the double
	 */
	public static double normal(double m, double s) 
	{
		return m + (Math.sqrt (-2.0 * Math.log (Math.random())) * 
				Math.cos (Math.PI * (2.0 * Math.random() - 1.0))) * s;
	}
	
	public double normalV(double m, double v) 
	{
		return normal (m, Math.sqrt (v));
	}

	// Input : "A", "B", 0.99, 0.01
	public String  getRandomResult(Object... s){
		ArrayList<String> listS = new ArrayList<String>();
		ArrayList<Double> listP = new ArrayList<Double>();
		
		for (int i = 0; i < s.length; i++){
			if (s[i] instanceof String ){
				listS.add((String)s[i]);
			} else if (s[i] instanceof Double ){
				listP.add((Double)s[i]);
			}
		}
		
		Double[] D = new Double[listP.size()];
		for (int i = 0; i < listP.size(); i++){
			D[i] = listP.get(i);
		}
		
		return getRandomResult(listS, D);		
	}
	 

	public static String getRandomResult(ArrayList<String> listStates, Double... ex){
		Double r = Math.random();
		Double p = 0.0;
		Double n = ex[0];
		for( int i = 0; i < ex.length; i++ ){
			if (p < r && r <= n){
				return listStates.get(i);
			}
			
			p += ex[i];
			n += ex[i+1];
		}
	
		return "";
	}
	
	public int getRandom(int size){
		Double r = Math.random();
		Double bin = 1/(double)size; 
		Double p = 0.0;
		Double n = bin;
		
		for( int i = 0; i < size; i++ ){
			if (p < r && r <= n){
				return i;
			}
			
			p += bin;
			n += bin;
		}
	
		return -1;
	}
	
	/**
	 * Poison.
	 *
	 * @param m the value
	 * @return the double
	 */
	public double poisson(double m) 
	{
		return RPois.Get(m);
	}
	
	/**
	 * Gamma.
	 *
	 * @param a the a value
	 * @param b the b value
	 * @return the double
	 */
	public double gamma(double a, double b ) 
	{
		return RGamma.Get(a, b);
	} 
	
	/**
	 * Beta.
	 *
	 * @param a the a value
	 * @param b the b value
	 * @return the double
	 */
	public double beta(double a, double b) 
	{
		return RBeta.Get(a, b);
	}
	
	/**
	 * Binomial.
	 *
	 * @param n the value
	 * @param prob the probability
	 * @return the double
	 */
	public double binomial(int n, double prob) 
	{
		return RBinom.GetByProb(n, prob);
	}
	
	/**
	 * Normal pdf.
	 *
	 * @param m the mean
	 * @param s the std
	 * @param x the probability
	 * @return the double
	 */
	public double normalPDF(double m, double s, double x) 
	{
		double num =  Math.exp( - (x - m) * (x - m) / (2 * s * s) );
        double den = s * Math.sqrt(2 * Math.PI);
        return num / den;
	}
	
	public double getSmalNumber() { 
        return 0.000000000001; 
	}
	
	public double getBigNumber() {
		return 1000000000000.0;
	}
 
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws Exception the exception
	 */
	public static void main(String args[]) throws Exception {
		
		TempMathFunctions math = new TempMathFunctions();
	//	System.out.print("math.gamma(0.4) = " + math.gamma( 2,1) + " \n" );
//		double d= math.normal(0, 10); //-20.26318822783283
//		
//		for( int i = 0 ; i < 100; i ++){
//			//System.out.print( math.exponential(2) + " \n" );
//			System.out.print( RExp.Get(2) + " \n" );
//		}
		
	//	System.out.print("math.binomial(10, 0.5) = " + math.binomial(10, 0.5) + " \n" );
	//	System.out.print("Test = " + (int)Math.round((Math.random()*10)) + " \n" );
		
		double d1 = 60.00000001;
		int e1 = new TempMathFunctions().getMostSignificantDigitFromDecimalPoint(d1);
		System.out.println(e1);
		
		double d2 = .01234;
		String e2 = new TempMathFunctions().safeDoubleAsString(d2);
		System.out.println(e2);
		
		
//		
	} 
}
