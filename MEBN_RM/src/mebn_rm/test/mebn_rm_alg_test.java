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
package mebn_rm.test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mebn_rm.MEBN.MFrag.MFrag;
import mebn_rm.MEBN.MTheory.MTheory;
import mebn_rm.RDB.RDB;
import mebn_rm.core.RM_To_MEBN;
import mebn_rm.util.ExcelCSV;
import mebn_rm.util.TextFile;

/**
 * mebn_rm_test is the test class to test MEBN-RM mapping algorithm with 30 relational databases.
 * Motl, J., & Schulte, O. (2015). The CTU prague relational learning repository. arXiv preprint arXiv:1511.03086.  
 * <p>
 * 
 * @author      Cheol Young Park
 * @version     0.0.1
 * @since       1.5
 */
   
public class mebn_rm_alg_test {
	String file_mapping_time = ".//Examples//TestResult//mapping_results";
	
	void initCSVFiles() {
    	ExcelCSV.createCSV(file_mapping_time);
        String[] heads = new String[]{"Schema", "ERS", "RRS", "Relation", "Attribute", "PK", "Entity", "MFrag", "Resident", "ISA", "Time"};
        ExcelCSV.writeCSV(file_mapping_time, heads);
	}

	void saveMappingTimeInfo(String schema, RDB rdb, MTheory m, String time) {
		List etables = (List)rdb.mapTableTypesAndTables.get((Object)"EntityTable");
		List rtables = (List)rdb.mapTableTypesAndTables.get((Object)"RelationshipTable");

		// Get the number of attributes
		Integer numOfAttr = 0;
		Integer numOfPK = 0;
		for (Object key : rdb.mapTableAndAttributes.keySet()) {
			List list = (List)rdb.mapTableAndAttributes.get(key);
			numOfAttr += list.size(); 
		}
		
		for (Object key : rdb.mapTableAndKeys.keySet()) {
			List list = (List)rdb.mapTableAndKeys.get(key);
			numOfAttr += list.size();
			numOfPK += list.size();
		}
		 
		// Get the number of resident nodes
		Integer numOfRegNodes = 0;
		Integer numOfIsACont = 0;  
		for (MFrag f : m.mfrags.keySet()) {
			numOfRegNodes += f.arrayResidentNodes.size();
			numOfIsACont += f.arrayIsaContextNodes.size();
        }
	
		String[] heads = new String[11];
		heads[0] = schema;
		heads[1] = String.valueOf(etables.size());
		heads[2] = String.valueOf((rtables == null) ? 0 : rtables.size());
		heads[3] = String.valueOf((rtables == null) ? etables.size() : rtables.size()+etables.size());
		heads[4] = numOfAttr.toString();
		heads[5] = numOfPK.toString(); 
		heads[6] = String.valueOf(m.entities.size()); 
		heads[7] = String.valueOf(m.mfrags.size());
		heads[8] = numOfRegNodes.toString();
		heads[9] = numOfIsACont.toString();
		heads[10] = time;

        ExcelCSV.writeCSV(file_mapping_time, heads);
	}
		 
    public mebn_rm_alg_test(List<String> schemas) throws SQLException {
    	// initialize CSV files
    	initCSVFiles();
    	
    	// Step 1: connect relational databases
//    	String address = "localhost:3306";
//    	String root = "root";
//    	String PW = "jesus";
    	
        String address = "relational.fit.cvut.cz:3306";
    	String root = "guest";
        String PW = "relational";
        
    	RDB.This().connect(address, root, PW);
    	
    	// Step 2: for each database schema, perform MEBN-RM
    	for (String schema : schemas) {
    		Long time1 = System.nanoTime(); 
    		
	        RDB.This().init(schema, false); 
	        
	        MTheory m = new RM_To_MEBN(RDB.This()).run();
	        String mTheory = m.toString("MFrag", "MNode");
	        System.out.println("///////////////////////////////////////////////////////////////");
	        System.out.println("The schema, " + schema + ", is used to create an MTheory");
	        System.out.println(mTheory);
	         
	        new mebn_rm.util.TextFile();
	        new TextFile().save(".//Examples//MTheory//" + schema + ".txt", mTheory);
	        
	        // Save mapping time information
	        Double seconds = (double)(System.nanoTime()-time1) / 1000000000.0;
	        saveMappingTimeInfo(schema, RDB.This(), m, seconds.toString()); 
    	} 
    }

    public static void main(String[] args) {
    	List<String> schemas = new ArrayList<String>();
     	schemas.add("stats");
    	schemas.add("financial");
    	schemas.add("imdb_MovieLens"); 
     	schemas.add("legalActs");
        schemas.add("SAT");
        schemas.add("Dunur");
        schemas.add("Elti");
        schemas.add("Same_gen");
        schemas.add("Bupa");
        schemas.add("Pima");
        schemas.add("Facebook");
//    	schemas.add("VisualGenome"); // The table IMG_OBJ is not in entity relation normalization.
    // 	schemas.add("WebKP");// The table contents is not in entity relation normalization.
    //	schemas.add("Student_loan"); // The table enrolled is not in entity relation normalization.
    //	schemas.add("ccs"); // The table yearmonth is not in entity relation normalization.
    //	schemas.add("imdb_full"); The table countries is not in entity relation normalization.
    //	schemas.add("nations"); The table relation is not in entity relation normalization.
    //	schemas.add("Accidents"); no primary key
    //	schemas.add("geneea"); no primary key
    //	schemas.add("Mesh");//The table mesh_test_neg is not in entity relation normalization.
    //	schemas.add("UTube");//The table utube_attributes is not in entity relation normalization.
    //	schemas.add("PTE");The table pte_alcohol is not in entity relation normalization.
    //	schemas.add("Airline");no primary key
    //	schemas.add("Seznam"); The table probehnuto_mimo_penezenku is not in entity relation normalization.
    //	schemas.add("NCAA"); the table regular_season_compact_results is not in entity relation normalization.
    	   	
        try {
        	new mebn_rm.test.mebn_rm_alg_test(schemas); 
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

