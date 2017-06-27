package util.math;

import java.math.BigDecimal;


public class KL_Divergence { 
	public double distence = 0.0; 
	

	public KL_Divergence() {
	} 
	
	//Kullback-Leibler divergence for two univariate gaussian
	public double get(double cov1, double cov2,
					  double mean1, double mean2,  
					  double prior1, double prior2){ 
		
		double cov = 0.0; 
		double prior = 0.0; 
		double lambda = 0.0;
		
		prior = Math.abs(prior1) + Math.abs(prior2); 
		lambda = Math.abs(prior1)/prior;
		 
		cov = lambda * cov1 + (1 - lambda) * cov2 + lambda * (1 - lambda) * (mean1 - mean2)*(mean1-mean2);
 
	    distence = 0.5 * (prior * Math.log(cov) - prior1 * Math.log(cov1) - prior2 * Math.log(cov2));
	    distence = Math.abs(distence);
			 
	    return distence; 
	} 
	
	//Kullback-Leibler divergence for two discrete distributions
	public double get(double[] p1, double[] p2) { 
	    double klDiv = 0.0;

	    for (int i = 0; i < p1.length; ++i) {
	        if (p1[i] == 0) { continue; }
	        if (p2[i] == 0) { continue; }   
	    	klDiv += p1[i] * Math.log( p1[i] / p2[i] );
	    }

		return klDiv / Math.log(2);  
	}
	
	
	public double get(double p1, double p2) { 
        if (p1 == 0 || p2 == 0) return 0;   
        System.out.println(p1 * Math.log( p1 / p2 ));
		return p1 * Math.log( p1 / p2 );  
	}
	
	public java.math.BigDecimal get(java.math.BigDecimal p1, java.math.BigDecimal p2) { 
        if (p1.doubleValue() == 0 || p2.doubleValue() == 0) return new java.math.BigDecimal("0.0");  
        System.out.println(p1.divide(p2, 10, BigDecimal.ROUND_HALF_EVEN));
        System.out.println(new BigDecimalUtils().ln(p1.divide(p2, 10, BigDecimal.ROUND_HALF_EVEN), 10));
		return p1.multiply(new BigDecimalUtils().ln(p1.divide(p2, 10, BigDecimal.ROUND_HALF_EVEN), 10));  
	}
	
	
}
