package util.math;


public class GMM_Operator { 
	public double distence = 0.0; 
	
	public double cov = 0.0;
	public double mean = 0.0; 
	public double prior = 0.0;
	public double precision = 0.0;
	public double info_state = 0.0;
	public double weight = 0.0;
	public double retio = 0.0;
	
	public GMM_Operator() {
	}
	
	public void Merging(double cov1, double cov2,
						double mean1, double mean2,  
						double prior1, double prior2){ 

		double lambda = 0.0;  
		prior = Math.abs(prior1) + Math.abs(prior2); 
		lambda = Math.abs(prior1)/prior;
		
		mean  = lambda * mean1 + (1 - lambda) * mean2;  
		cov   = lambda * cov1 + (1 - lambda) * cov2 + lambda * (1 - lambda) * (mean1 - mean2)*(mean1-mean2);
	}
	
	public void Merging(double cov1, double cov2,
			double mean1, double mean2,  
			double prior1, double prior2,
			double precision1, double precision2,
			double info_state1, double info_state2,
			double weight1, double weight2,
			double retio1, double retio2){ 

		Merging(cov1, cov2,
				mean1, mean2,  
				prior1, prior2);
				
		precision = 1/cov;
		info_state = mean/cov;
		weight = weight1 + weight2;
		retio = retio1*(weight1/weight) + retio2*(weight2/weight);
	}
		
	public double KL_Distance(double cov1, double cov2,
						   	  double mean1, double mean2,  
						      double prior1, double prior2){ 
		
		Merging(cov1, cov2, mean1, mean2, prior1, prior2, 0, 0, 0, 0, 0, 0, 0, 0);		
	    distence = 0.5 * (prior * Math.log(cov) - prior1 * Math.log(cov1) - prior2 * Math.log(cov2));
	    distence = Math.abs(distence);
			 
	    return distence; 
	} 
}
