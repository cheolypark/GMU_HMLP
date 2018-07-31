package test.learner;

import mebn_ln.core.MFrag_Learning;
import mebn_rm.MEBN.MFrag.MFrag;  

public class test_learner1 {

	public test_learner1() { 
	}
	
	public void run() {  
		MFrag f = new MFrag(null, "MyMFrag");
		f.cvsFile = "";
		f.script
		new MFrag_Learning().run(f);
		
	}

	public static void main(String[] args) {
		new test_learner1().run();

	}

}
