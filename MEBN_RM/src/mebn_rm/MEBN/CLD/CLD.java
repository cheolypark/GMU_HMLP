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
package mebn_rm.MEBN.CLD;

import edu.cmu.tetrad.data.ColtDataSet;
import edu.cmu.tetrad.data.DataSet;
import edu.cmu.tetrad.graph.Graph;
import edu.cmu.tetrad.graph.Node; 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mebn_rm.MEBN.CLD.Probability; 
import mebn_rm.MEBN.MNode.MNode; 
import mebn_rm.MEBN.parameter.Parameter;
import mebn_rm.RDB.RDB; 
import mebn_rm.util.Resource; 
import mebn_rm.util.Tetrad_Util;
import util.SortableValueMap;
import util.math.Sum_for_Log; 

/**
 * CLD is the class to perform functions related to class local distribution (CLD). 
 * The class performs calculation for a CLD and construction of the script of the CLD.
 * <p>
 * 
 * @author      Cheol Young Park
 * @version     0.0.1
 * @since       1.5
 */

public class CLD extends Probability implements Comparable<CLD> {
    public MNode mNode = null;
    public String name;
    public int parameterSize;
    public List<String> IPCs_Data = null;	//	IPC corresponding to columns in csv data (This can not have a redundant name) 
    public Map<String, String> IPCs_MEBN = new HashMap<String, String>();	//	IPC corresponding to CLD in a resident node (This can have a redundant name) 
    public DataSet selectedData = null;
    public DataSet defaultData = null;
    public DataSet data = null;
    public List<String> arrayCategories = null;
    public String cld_type = "";
    public Boolean isSampling = true;
    public SortableValueMap<Parameter, Double> paraCANs = new SortableValueMap<Parameter, Double>();
    public Parameter bestParameter = null; 

    public CLD(String n, String c) {
        name = n;
        cld_type = c;
    }
 
    public String toString() {
        String s = "[L [";
        s = String.valueOf(s) + cld_type + "]: ";
        s = String.valueOf(s) + name;
        s = String.valueOf(s) + "]\r\n";
        return s;
    }

    /**
     * Used for returning a string of this CLD. 
     * The string will contains specific information according to inclusions.  
     * @param inclusions		a list of inclusions
     * @return					a string representing CLD
     */
    public String toString(List<String> inclusions) {
        String s = "";
        if (inclusions.contains("CLD")) {
        	
        	if (cld_type.equalsIgnoreCase("Dirichlet")) {
        		System.out.println("");
        	}
        	
            s = String.valueOf(s) + "[L [";
            s = String.valueOf(s) + cld_type + "]: ";
            s = String.valueOf(s) + name;
            s = String.valueOf(s) + "]";
        }
        return s;
    }
 
    /**
     * This method is used to return the script of local distribution of a script BN.
     * 
     * @return 		the script of local distribution of a script BN
     */
    public String getILD() {
        return "";
    }
 
    /**
     * This method is used to return a list of categories.
     * 
     * @return		a list of categories
     */
    public List<String> getCategories() {
        return null;
    }

    public void addCategories(String c) {
        if (arrayCategories == null) {
            arrayCategories = new ArrayList<String>();
            arrayCategories.add(c);
        } else {
            arrayCategories.add(c);
        }
    }
   
    public Double calculateSimilarity() {
        return null;
    }
 
    public int compareTo(CLD o) {
        return 0;
    }

    public Double getlogParaScore() {
        ArrayList<Double> logSCs = new ArrayList<Double>();
        for (Parameter p : paraCANs.keySet()) {
            Double logSC = paraCANs.get(p);
            logSCs.add(logSC);
            System.out.println(String.valueOf(p.toString()) + " Log Parameter scores : " + logSC);
        }
        return new Sum_for_Log().sum(logSCs);
    }

    public Double getAvgLogParaScore() {
        Double logSC = 0.0;
        for (Parameter p : paraCANs.keySet()) {
            logSC = logSC + paraCANs.get(p);
        }
        return logSC / (double)paraCANs.size();
    }

    public void resetParaCANsPrior() {
        for (Parameter para : paraCANs.keySet()) {
            paraCANs.put(para, Math.log(1.0 / (double)paraCANs.size()));
        }
    }

    /**
     * This method is used to calculate parameters.
     * TODO: the return of this method should be the score of the calculated parameters.   
     * @return		the score of the calculated parameters
     */
    public Double calculateBestPara() {
        return null;
    }
 
    /**
     * Used for generating influencing parent conditions (IPC).
     * @param hybridGraph		a network containing discrete and continuous nodes  
     * @return					a list of IPCs
     */
    public List<String> initIPCs(Graph hybridGraph) {  
    	
        if (name.equalsIgnoreCase("land_state_Dry_cld_1"))
        {
        	System.out.println("land_state_Dry_cld_1");
        }
        
        String strFile = "";
        
        if (mNode.mFrag.cvsFile != null && !mNode.mFrag.cvsFile.isEmpty()) {
        	strFile = mNode.mFrag.cvsFile; 
        } else {
        	strFile = String.valueOf(Resource.getCSVPath(mNode.mFrag.mTheory.name)) + mNode.mFrag.name + ".csv";
        }
        
        data = RDB.This().getTetDataSetFromCSV(strFile);
        if (data.getNumRows() == 0) {
            return null;
        } 
        
		Node child = data.getVariable(mNode.getColumnName());
        hybridGraph.addNode(child);
        List<String> parents = mNode.getAllParentColumnNames(); 
        
        if (parents != null) {
            for (String pr : parents) {
                Node parent = data.getVariable(pr);
                hybridGraph.addNode(parent);
                hybridGraph.addDirectedEdge(parent, child);
            }
        }
        selectedData = (ColtDataSet)data.subsetColumns(hybridGraph.getNodes());
         
        return Tetrad_Util.getIPC(mNode, selectedData, mNode.name, IPCs_MEBN);
    } 
}

