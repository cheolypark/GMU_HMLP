/**
 * 
 */
package util;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Cheol Young Park
 * This is based on Dr. Laskey's approach. 
 */
public class ROC2 {

	public class Recode{
		public String ID; 
		public Double TP = 0.0;
		public Double FP = 0.0;
		public Double TN = 0.0;
		public Double FN = 0.0;
		public Double TPR = 0.0;
		public Double FPR = 0.0;
		public Double value = 0.0;
		public String actualClass = "";	
		public Recode(){
		}
		public void set(Recode r){
			ID = r.ID; 
			TP = r.TP;
			FP = r.FP;
			TN = r.TN;
			FN = r.FN;
			TPR = r.TPR;
			FPR = r.FPR;
			value = r.value;
			actualClass = r.actualClass;
		}
	}
	
	
	public class byProbability implements java.util.Comparator {
		 	public int compare(Object boy, Object girl) {
		  	return Double.compare(((Recode)boy).value, ((Recode)girl).value);
		 }
	} 
	
	ArrayList<Recode> arryRecode = new ArrayList<Recode>(); 
	Double TruePositive; 
	Double FalsePositive;
	Double FalseNegative;
	Double TrueNegative;
	Double PositiveCount;
	Double NegativeCount;
	Double AUC;
	   
	public ROC2(){
		
	}
	
	public void Add(String id, Double val, String actC ){
		Recode r = new Recode();
		r.ID = id;
		r.value = val;
		r.actualClass = actC;
		
		arryRecode.add(r);
	}
	
	// return TP, FP, FN, TN
	public void calculateRoc(double threshold, String className, ArrayList<Recode> arryRec){
		for (Recode r : arryRec){
			String SOI = r.actualClass;  
			if (SOI.equalsIgnoreCase(className)){ // Actual is positive
	    		PositiveCount++;
	    		if (r.value >= threshold )		// Prediction is positive
	        		TruePositive++;
	        	else					// Prediction is negative
	        		FalseNegative++;
	    	}
	    	else{						// Actual is negative
	    		NegativeCount++;
	    		if (r.value >= threshold )		// Prediction is positive
	        		FalsePositive++;
	        	else
	        		TrueNegative++;		// Prediction is negative
	    	} 
		}		
		
		
	}
	
	@SuppressWarnings("unchecked")
	public void calculate(String className){ 
		AUC 			= 0.0; 
  		Collections.sort(arryRecode,  new byProbability()); 
		double threshold = 0.0; 
		Recode preR = new Recode();
		for (Recode r : arryRecode){
			threshold = (threshold + r.value)/2; 
			TruePositive 	= 0.0; 
			FalsePositive 	= 0.0;
			FalseNegative 	= 0.0;
			TrueNegative 	= 0.0;
			PositiveCount 	= 0.0;
			NegativeCount  	= 0.0; 
			calculateRoc(threshold, className, arryRecode);
			threshold = r.value;  
			r.TP = TruePositive;
			r.FP = FalsePositive; 
			r.TN = TrueNegative;
			r.FN = FalseNegative;
		  
			r.TPR = r.TP/(r.TP + r.FN);
			r.FPR = r.FP/(r.FP + r.TN);
		  
			if( preR.TPR != 0 )
				AUC += (r.TPR + preR.TPR)/2*(preR.FPR-r.FPR);
		 		
			preR.set(r);
		}
		
		print();
	}
	
	public void print(){
		System.out.println("/// ROC ///");
		String strC = "ID" + "	" + 
					 "value" + "	" + 
					 "actualClass" + "	" + 
					 "TP"  + "	" + 
					 "FP" + "	" + 
					 "TN" + "	" + 
					 "FN" + "	" +
					 "TPR" + "	" + 
					 "FPR" + "	" ;
		System.out.println(strC);
		
		
		for( Recode r : arryRecode ){
			String str = r.ID + "	" + 
						 r.value + "	" + 
						 r.actualClass + "	" + 
						 r.TP  + "	" + 
						 r.FP + "	" + 
						 r.TN + "	" + 
						 r.FN + "	" +
						 r.TPR + "	" + 
						 r.FPR + "	" ;
			System.out.println(str);
		}
		
		System.out.println("AUC = " + AUC);
		
		System.out.println("////////////////////////////////////////////////////" );
		System.out.println("			Predictive True		Predictive False" );
		System.out.println("Actual True	" + TruePositive  + "	" + FalseNegative );
		System.out.println("Actual False	" + FalsePositive + "	" + TrueNegative );
		System.out.println("count	" + PositiveCount + "	" + NegativeCount );
		System.out.println("////////////////////////// PreRoc Result ");
	} 
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ROC2 roc = new ROC2();
		roc.Add( "1", 0.6, "TRUE");
		roc.Add( "2", 0.7, "TRUE");
		roc.Add( "3", 0.043242, "TRUE");
		roc.Add( "4", 0.71, "TRUE");
		roc.Add( "5", 0.1, "TRUE");
		roc.Add( "6", 0.1, "FALSE");
		roc.Add( "7", 0.3, "FALSE");
		
		/*roc.Add( "1", 0.6, "TRUE");
		roc.Add( "2", 0.4, "FALSE");
		roc.Add( "3", 0.3, "TRUE");
		roc.Add( "4", 0.7, "TRUE");*/
		
		roc.calculate("TRUE");
		//roc.print();
	}

}
