/*
 * HML Core
 * Copyright (C) 2017 Cheol Young Park
 * 
 * This file is part of HML Core.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package test.text_mode;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mebn_ln.core.MTheory_Learning; 
import mebn_rm.MEBN.MTheory.MRoot;
import mebn_rm.MEBN.MTheory.MTheory;
import mebn_rm.RDB.RDB;
import mebn_rm.core.RM_To_MEBN;
   
public class text_mode_continuous_heater_model {

	public text_mode_continuous_heater_model() {
	}

	public void run() {
		//1. Get Database schema from MySQL
		RDB.This().connect("root", "jesus");
		List<String> list = RDB.This().getSchemas();
		
		System.out.println(list);
		 
		String database = "test_heater";
		
		//2. initialize RDB
		try {
			RDB.This().init(database);
		} catch (SQLException e) { 
			e.printStackTrace();
		}
		
		//3. Perform MEBN-RM 
		MTheory	mTheory =  new RM_To_MEBN(RDB.This()).run(); 
		
		//4. Add parents
		String childMNode = "";
		List<String> parentMNodes = null; 
				
		childMNode = "heateractuator_item.HAI_energy";
		parentMNodes = new ArrayList<String>();
		parentMNodes.add("slabinput_item.SII_temperature");
		mTheory.addParents(childMNode, parentMNodes);
		
		System.out.println(mTheory.toString("MFrag", "MNode"));
		
		childMNode = "heater_item.HI_temperature";
		parentMNodes = new ArrayList<String>();
		parentMNodes.add("slabinput_item.SII_temperature");
		parentMNodes.add("HAI_energy_SII_temperature.HAI_energy");
		mTheory.addParents(childMNode, parentMNodes);
		  
		System.out.println(mTheory.toString("MFrag", "MNode"));
		
		//5. Add contexts  
		mTheory.updateContexts();
		
 		System.out.println(mTheory.toString("MFrag", "MNode")); 
		
		//6. Add CLD type   
		mTheory.updateCLDs();  
		
		//7. Learn MEBN  
		MRoot mroot = new MRoot();
		mroot.setMTheories(mTheory);  
		new MTheory_Learning().run(mroot);  
		 
 		System.out.println(mTheory.toString("MFrag", "MNode", "CLD" ));  
	}
	
	public static void main(String[] args) {
		text_mode_continuous_heater_model h = new text_mode_continuous_heater_model();
		h.run();
	} 
}
