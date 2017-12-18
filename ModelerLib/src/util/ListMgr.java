package util;

import java.util.ArrayList;
import java.util.List;

public class ListMgr {

	public ListMgr() {
	}
	
	public String getListComma(List<String> list) {
		String str = "";
		for (String ob : list) {
			String s = ob.toString();
			str += s + ", ";
		}
		
		str =  str.substring(0, str.length()-2);
			
		return str;
	}
	
	public Double getAverage(List<Double> list) {
		Double ret = 0.0;
		for (Double d : list) {
			ret += d;
		} 
		return ret/list.size();
	}
		
	public static void main(String[] args) {
		ListMgr l = new ListMgr();
		List<String> keys = new ArrayList<String>();
		
		keys.add("A");
		keys.add("B");
		keys.add("C");
		System.out.println(l.getListComma(keys));
		
	}

}
