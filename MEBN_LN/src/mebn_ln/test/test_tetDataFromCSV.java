package mebn_ln.test;

import edu.cmu.tetrad.data.ColtDataSet;
import mebn_rm.RDB.RDB;

public class test_tetDataFromCSV {

	public test_tetDataFromCSV() {
	}
	
	public void run(String fCSV) {
		RDB rdb = new RDB();
		ColtDataSet cs = rdb.getTetDataSetFromCSV(fCSV); 
		System.out.println(cs);
	}

	public static void main(String[] args) {
		String file = "example/temp.csv";
		new test_tetDataFromCSV().run(file);
	}
}
