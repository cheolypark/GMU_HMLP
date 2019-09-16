/**
 * 
 */
package util;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Cheol Young Park
 *
 */
public class ROC {

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
	   
	public ROC(){
		
	}
	
	public void Add(String id, Double val, String actC ){
		Recode r = new Recode();
		r.ID = id;
		r.value = val;
		r.actualClass = actC;
		
		arryRecode.add(r);
	}
	
	@SuppressWarnings("unchecked")
	public void calculate(String className){
		TruePositive 	= 0.0; 
		FalsePositive 	= 0.0;
		FalseNegative 	= 0.0;
		TrueNegative 	= 0.0;
		PositiveCount 	= 0.0;
		NegativeCount  	= 0.0;
		AUC 			= 0.0; 
	
		for (Recode r : arryRecode){
			String SOI = r.actualClass;  
			if (SOI.equalsIgnoreCase(className)){ // Actual is positive
	    		PositiveCount++;
	    		if (r.value >= 0.5 )		// Prediction is positive
	        		TruePositive++;
	        	else					// Prediction is negative
	        		FalseNegative++;
	    	}
	    	else{						// Actual is negative
	    		NegativeCount++;
	    		if (r.value >= 0.5 )		// Prediction is positive
	        		FalsePositive++;
	        	else
	        		TrueNegative++;		// Prediction is negative
	    	} 
		}
		
		Collections.sort(arryRecode, new byProbability()); 
		boolean b = true;
		Recode preR = new Recode();
		for (Recode r : arryRecode){
			if (b){
				r.TP = PositiveCount;
				r.FP = NegativeCount;
				r.TN = 0.0;
				r.FN = 0.0;
				r.TPR = 0.0;
				r.FPR = 0.0; 
				r.TPR = r.TP/(r.TP + r.FN);
				r.FPR = r.FP/(r.FP + r.TN);
				b = false;
			} else {
				if (preR.actualClass.equalsIgnoreCase(className)){
					r.TP = preR.TP - 1;
					r.FP = preR.FP;
				} else {
					r.TP = preR.TP;
					r.FP = preR.FP - 1;
				} 
				
				r.TN = NegativeCount - r.FP;
				r.FN = PositiveCount - r.TP;
			  
				r.TPR = r.TP/(r.TP + r.FN);
				r.FPR = r.FP/(r.FP + r.TN);
			  
				AUC += (r.TPR + preR.TPR)/2*(preR.FPR-r.FPR);
			}
			
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
		System.out.println("		Predictive True		Predictive False" );
		System.out.println("Actual True		" + TruePositive  + "		" + FalseNegative );
		System.out.println("Actual False		" + FalsePositive + "		" + TrueNegative );
		System.out.println("///count////		" + PositiveCount + "		" + NegativeCount );
		System.out.println("////////////////////////////////////////////////////" );
		System.out.println("Accuracy		" + ((TruePositive+TrueNegative)/(PositiveCount+NegativeCount)));
		System.out.println("Precision 		" + ((TruePositive)/(TruePositive+FalseNegative)));
		System.out.println("F1 			" + ((2*TruePositive)/(2*TruePositive+FalsePositive+FalseNegative)));
	} 
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ROC roc = new ROC();
		 
		/*	roc.Add( "1", 0.6, "FALSE");
		roc.Add( "2", 0.7, "FALSE");
		roc.Add( "3", 0.043242, "TRUE");
		roc.Add( "4", 0.31, "TRUE");
		roc.Add( "5", 0.1, "FALSE");
		roc.Add( "6", 0.1, "FALSE");
		roc.Add( "7", 0.3, "FALSE");
		roc.Add( "8", 0.45, "FALSE");
		roc.Add( "9", 0.8, "TRUE");
		roc.Add( "10", 0.72, "TRUE");
		roc.Add( "11", 0.2, "FALSE");
		roc.Add( "12", 0.22, "FALSE");
		roc.Add( "13", 0.043242, "TRUE");
		roc.Add( "14", 0.71, "TRUE");
		roc.Add( "15", 0.1, "FALSE");
		roc.Add( "16", 0.1, "FALSE");
		roc.Add( "17", 0.3, "FALSE");
		roc.Add( "18", 0.45, "FALSE");
		roc.Add( "19", 0.8, "FALSE");
		roc.Add( "20", 0.72, "FALSE");
		*/
		
		roc.Add( "1", 0.6, "FALSE");
		roc.Add( "2", 0.7, "FALSE");
		roc.Add( "3", 0.043242, "TRUE");
		roc.Add( "4", 0.71, "TRUE");
		roc.Add( "5", 0.1, "FALSE");
		roc.Add( "6", 0.1, "FALSE");
		roc.Add( "7", 0.3, "FALSE");
		roc.Add( "8", 0.45, "FALSE");
		roc.Add( "9", 0.8, "TRUE");
		roc.Add( "10", 0.72, "TRUE");
		roc.Add( "11", 0.6, "FALSE");
		roc.Add( "12", 0.7, "FALSE");
		roc.Add( "13", 0.043242, "TRUE");
		roc.Add( "14", 0.71, "TRUE");
		roc.Add( "15", 0.1, "FALSE");
		roc.Add( "16", 0.1, "FALSE");
		roc.Add( "17", 0.3, "FALSE");
		roc.Add( "18", 0.45, "FALSE");
		roc.Add( "19", 0.8, "FALSE");
		roc.Add( "20", 0.72, "FALSE");
		
		
		/*roc.Add( "1", 0.6, "TRUE");
		roc.Add( "2", 0.4, "FALSE");
		roc.Add( "3", 0.3, "TRUE");
		roc.Add( "4", 0.7, "TRUE");*/
		
		roc.calculate("TRUE"); 
	}

}
