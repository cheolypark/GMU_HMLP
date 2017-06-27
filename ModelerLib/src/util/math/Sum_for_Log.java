package util.math;
 
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

public class Sum_for_Log {
	Double log_sum = 0.0; 
		
	public Sum_for_Log() {
	} 
	
	public Double sum(List<Double> listLogs) {
		listLogs = new doubleQuickSort().sortToHigher(listLogs);
		
		Double firstLog = listLogs.get(0);
		
		Double forOtherLogs = 1.0;
		for (int i = 1; i < listLogs.size(); i++){
			Double log = listLogs.get(i) - firstLog;
			forOtherLogs += Math.exp(log);
		}
		
		if (forOtherLogs.isInfinite()){
			System.out.println("forOtherLogs.isInfinite() for " + listLogs);
			System.out.println("between items, differences are too big to compute the exp function");
			throw new EmptyStackException();
		}
		
		log_sum = firstLog + Math.log(forOtherLogs);
		return log_sum; 
	}
	 

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Double a = -800.0;//-910.2382173135611;
		Double b = -106.23143895129729;
		Double c = -4.489673842047605;
		
		//[-910.2382173135611, -408.0909870962033, -106.23143895129729, -4.489673842047605]
				
		List<Double> listLogs = new ArrayList<Double>();
		listLogs.add(a);
		listLogs.add(b);
		listLogs.add(c);
		
		Sum_for_Log m = new Sum_for_Log();
		System.out.println(m.sum(listLogs));
		
		/*List<Double> listLogs =  new ArrayList<Double>();
		listLogs.add(Math.log(0.999999995));
		listLogs.add(Math.log(0.00000000499999997499988));
		listLogs.add(Math.log(0.00000000000002499999987));
		
		Normalizer_for_Log m = new Normalizer_for_Log(listLogs);
		System.out.println(m.fraction(Math.log(0.999999995)));
		System.out.println(m.fraction(Math.log(0.00000000499999997499988)));
		System.out.println(m.fraction(Math.log(0.00000000000002499999987)));*/

	}

}
