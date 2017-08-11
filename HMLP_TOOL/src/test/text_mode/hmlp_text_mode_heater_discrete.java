package test.text_mode;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mebn_ln.core.MTheory_Learning;
import mebn_rm.MEBN.CLD.ConditionalGaussian;
import mebn_rm.MEBN.MTheory.MRoot;
import mebn_rm.MEBN.MTheory.MTheory;
import mebn_rm.RDB.RDB;
import mebn_rm.core.RM_To_MEBN;
 

public class hmlp_text_mode_heater_discrete {

	public hmlp_text_mode_heater_discrete() {
	}

	public void run() {
		//1. Get Database schema from MySQL
		RDB.This().connect("root", "jesus");
		List<String> list = RDB.This().getSchemas();
		
		System.out.println(list);
		
		String database = "test_heater_for_experiment_discrete"; 
		
		//2. initialize RDB
		try {
			RDB.This().init(database);
		} catch (SQLException e) { 
			e.printStackTrace();
		}
		
		//3. MEBN-RM 
		MTheory	mTheory =  new RM_To_MEBN(RDB.This()).run(); 
		
		//4. Add parents
		String childMNode = "";
		List<String> parentMNodes = null; 
				
		childMNode = "heateractuator_item.HAI_energy";
		parentMNodes = new ArrayList<String>();
		parentMNodes.add("slabinput_item.SII_temperature");
		parentMNodes.add("slabinput_item.SII_grade");	// Discrete
		parentMNodes.add("slabinput_item.SII_volume");	// Discrete		
		
		mTheory.addParents(childMNode, parentMNodes);
		
//		System.out.println(mTheory.toString("MFrag", "MNode"));
		
		childMNode = "heater_item.HI_temperature";
		parentMNodes = new ArrayList<String>();
		parentMNodes.add("slabinput_item.SII_temperature");
		parentMNodes.add("HAI_energy_SII_temperature_SII_grade_SII_volume.HAI_energy");
		mTheory.addParents(childMNode, parentMNodes);
		  
//		System.out.println(mTheory.toString("MFrag", "MNode"));
		
		//5. Add contexts 
//		String targetMFrag = "heater_item";
//		mTheory.addContexts(targetMFrag, null);
		
		/*
		 * 	 SELECT 
		 *  	heater_item.temperature as HI_temperature,
		 *  	slabinput_item.temperature as SII_temperature,
		 *  	heateractuator_item.energy as HAI_energy
		 *   FROM heater_item, slabinput_item, heateractuator_item 
		 *   WHERE 
		 *  	heater_item.TimeID = slabinput_item.TimeID &&
		 *  	heater_item.TimeID = heateractuator_item.TimeID 
		 */
		mTheory.updateContexts();
		
// 		System.out.println(mTheory.toString("MFrag", "MNode")); 
		
		//6. Add CLD type  
//		mTheory.addCLDType("HI_temperature_SII_temperature_HAI_energy.HI_temperature", new ConditionalGaussian()); 
// 		mTheory.addCLDType("slabinput_item.SII_temperature", new ConditionalGaussian());
//		mTheory.addCLDType("HAI_energy_SII_temperature.HAI_energy", new ConditionalGaussian());
		mTheory.updateCLDs(); 
		
//		System.out.println(mTheory.toString("MFrag", "MNode")); 
		
		//7. Learn MEBN  
		MRoot mroot = new MRoot();
		mroot.setMTheories(mTheory);  
		new MTheory_Learning().run(mroot);  
		 
 		System.out.println(mTheory.toString("MFrag", "MNode", "CLD" ));  
		 
	}
	
	public static void main(String[] args) {
		hmlp_text_mode_heater_discrete h = new hmlp_text_mode_heater_discrete();
		h.run();
	} 
}
