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

import edu.cmu.tetrad.bayes.BayesIm;
import edu.cmu.tetrad.bayes.BayesPm;
import edu.cmu.tetrad.bayes.DirichletBayesIm;
import edu.cmu.tetrad.bayes.DirichletEstimator;
import edu.cmu.tetrad.data.DataSet;
import edu.cmu.tetrad.graph.Dag;
import edu.cmu.tetrad.graph.EdgeListGraph;
import edu.cmu.tetrad.graph.Graph;
import edu.cmu.tetrad.graph.Node; 
import java.util.ArrayList; 
import java.util.Iterator;
import java.util.List;  
import mebn_rm.MEBN.CLD.LPD_Discrete;
import mebn_rm.MEBN.MNode.MNode; 
import mebn_rm.RDB.RDB; 
import mebn_rm.util.StringUtil;
import mebn_rm.util.Tetrad_Util; 

/**
 * Categorical is the class to perform functions related to local distribution of 
 * a categorical random variable (e.g., true and false). The class performs calculation 
 * for a local distribution and construction of the script of the local distribution.
 * For calculation of the local distribution, Dirichlet distribution is used.   
 * 
 * <p>
 * 
 * @author      Cheol Young Park
 * @version     0.0.1
 * @since       1.5
 */

public class Categorical extends LPD_Discrete {
 
    public Categorical() {
        super("", "Dirichlet");
        parameterSize = 1;
        isSampling = false;
    }

    public Categorical(String name) {
        super(name, "Dirichlet");
        parameterSize = 1;
        isSampling = false;
    } 
 
    /* 
     * This method is used to return a list of categories.
     * 
     * @see mebn_rm.MEBN.CLD.CLD#getCategories()
     */
    public List<String> getCategories() {
        if (arrayCategories == null) {
            arrayCategories = new ArrayList<String>();
            BayesIm bayesIm = null;
            Iterator<String> iterator = ipcIMs.keySet().iterator();
            if (iterator.hasNext()) {
                String condition = iterator.next();
                bayesIm = ipcIMs.get(condition);
            }
            if (selectedData != null) {
                Node thisNode = selectedData.getVariable(mNode.name);
//                Node thisNode = selectedData.getVariable(mNode.columnName);
                int c = 0;
                while (c < bayesIm.getBayesPm().getNumCategories(thisNode)) {
                    arrayCategories.add(bayesIm.getBayesPm().getCategory(thisNode, c));
                    ++c;
                }
            }
        } else {
            return arrayCategories;
        }
        return arrayCategories;
    } 
    
    /*  
     * This method is used to calculate parameters.
     * TODO: the return of this method should be the score of the calculated parameters.   
     * 
     * @see mebn_rm.MEBN.CLD.CLD#calculateBestPara()
     */
    public Double calculateBestPara() { 
        EdgeListGraph hybridGraph = new EdgeListGraph();
        IPCs_Data = initIPCs((Graph)hybridGraph);
        
        if (IPCs_Data == null) {
            return 0.0;
        } 
        
        for (String ipc : IPCs_Data) {
            DataSet _dataSet_dis = null;
            _dataSet_dis = ipc.equalsIgnoreCase("") ? selectedData : Tetrad_Util.getSubsetdataFromIPC(ipc, selectedData);
            EdgeListGraph graph = new EdgeListGraph();
            Node child = _dataSet_dis.getVariable(mNode.name);
//            Node child = _dataSet_dis.getVariable(mNode.columnName);
            
            graph.addNode(child);
            Dag dag = new Dag((Graph)graph);
            BayesPm bayesPm = new BayesPm((Graph)dag);
            DirichletBayesIm prior = DirichletBayesIm.symmetricDirichletIm((BayesPm)bayesPm, (double)0.5);
            DirichletBayesIm bayesIm = DirichletEstimator.estimate((DirichletBayesIm)prior, (DataSet)_dataSet_dis);
            ipcIMs.put(ipc, (BayesIm)bayesIm);
        }
        
        // create default distribution
        if (mNode.cvsFile != null) {	// contains a cvs for a default data
        	System.out.println(mNode.cvsFile);
        	
            String strFile = mNode.cvsFile;
            defaultData = (DataSet)RDB.This().getTetDataSetFromCSV(strFile);
            if (defaultData.getNumRows() == 0) {
                return null;
            } 
              
            EdgeListGraph continuousGraph = new EdgeListGraph();
            Node child2 = defaultData.getVariable(mNode.name);
//            Node child2 = defaultData.getVariable(mNode.columnName);
             
            continuousGraph.addNode(child2);
            Dag dag = new Dag((Graph)continuousGraph);
            BayesPm bayesPm = new BayesPm((Graph)dag);
            DirichletBayesIm prior = DirichletBayesIm.symmetricDirichletIm((BayesPm)bayesPm, (double)0.5);
            DirichletBayesIm bayesIm = DirichletEstimator.estimate((DirichletBayesIm)prior, (DataSet)defaultData);
            ipcIMs_default = (BayesIm)bayesIm;
        }
        
        return 0.0;
    }
     
    /*
     * This method is used to return the script of local distribution of MEBN.
     * e.g.,) 
     *   
     * if any rgn have ( TerrainType = Road ) [
     * 	Tracked = .2,
     * 	Wheeled = .8
     * ] else if any rgn have ( TerrainType = OffRoad ) [
     * 	Tracked = .8,
     * 	Wheeled = .2
     * ] else [
     * 	Tracked = .5,
     * 	Wheeled = .5
     * ]
     * 
     * @see mebn_rm.MEBN.CLD.CLD#toString(java.util.List)
     */
    public String toString(List<String> inclusions) {
    	String s = "";
        if (inclusions.contains("CLD") && selectedData != null) { 
        	Node thisNode = selectedData.getVariable(mNode.name);
        	
        	// get ovs statement
        	String ovs = "";
        	List<MNode> parents = mNode.getDiscreteParents(); 
        	for (MNode mn : parents){
        		ovs += mn.toStringOVs();
        		ovs += ",";
        	}
        	ovs = StringUtil.This().removeRedundantItem(ovs);
        	ovs = ovs.replace(",", ".");      
        	ovs = ovs.replace(" ", "");
        	
        	s = s + "[L: ";
        	 
        	boolean b = true; 
        	
        	for (String k : ipcIMs.keySet()) {
        		BayesIm bayesIm = ipcIMs.get(k);
        		String conditions = IPCs_MEBN.get(k);
        		conditions = conditions.replace("&&", "&");
        		conditions = conditions.replace("==", "=");
        		 
        		if (!k.isEmpty()) {
        			if (b) {
        				s += "if any " + ovs + " have ( " + conditions + " ) ";
        				b = false;
        			} else {
        				s += "else if any " + ovs + " have ( " + conditions + " ) ";
        			}
        		}   
        		
        		s += "[";
        		
        		int j = 0;
        		while (j < bayesIm.getNumColumns(0)) {
        			Double p = bayesIm.getProbability(0, 0, j);
        			//String prob = TempMathFunctions.safeDoubleAsString(p);
	    			String prob = p.toString();
        			String state2 = bayesIm.getBayesPm().getCategory(thisNode, j);
        			s += state2 + " = " + prob;
        			s += ", ";
        			++j;
        		}
        		
        		s = s.substring(0, s.length() - 2);
        		
        		s = s + "]\n			";
        	}
        	  
            // add default distribution 
        	if (parents.size() > 0) {
        		s = s + "else[ ";
        		
	            if (ipcIMs_default != null) {
	            	
		            BayesIm bayesIm = ipcIMs_default;
		            int j = 0;
		    		while (j < bayesIm.getNumColumns(0)) {
		    			Double p = bayesIm.getProbability(0, 0, j);
//		    			String prob = TempMathFunctions.safeDoubleAsString(p);
		    			String prob = p.toString();
		    			String state2 = bayesIm.getBayesPm().getCategory(thisNode, j);
		    			s += state2 + " = " + prob;
		    			s += ", ";
		    			++j;
		    		}
		    		s = s.substring(0, s.length() - 2);
		            	    		
	            } else {	// no default distribution
	            	for (String k : ipcIMs.keySet()) {
	            		BayesIm bayesIm = ipcIMs.get(k);
	            		int j = 0;
	            		while (j < bayesIm.getNumColumns(0)) {
	            			Double p = bayesIm.getProbability(0, 0, j);
	            			Double size = Double.valueOf(bayesIm.getNumColumns(0));
	            			Double prob = 1/size;
	            			String state2 = bayesIm.getBayesPm().getCategory(thisNode, j);
	            			s += state2 + " = " + prob;
	            			s += ", ";
	            			++j;
	            		}
	            		s = s.substring(0, s.length() - 2);
	            		break;
	            	}
	            }
	            
	            s = s + "]"; 
        	}
        	
        	s = s + "]\r\n";
        }
        
        return s;
    }
    
    /* 
     * This method is used to return the script of local distribution of a script BN.
     * 
     * @see mebn_rm.MEBN.CLD.CLD#getILD()
     */
    public String getILD() {
        List<MNode> discreteParents = mNode.getDiscreteParents();
        String s = "{ defineState(Discrete, ";
        Node thisNode = selectedData.getVariable(mNode.name);
        for (String state : mNode.getCategories()) {
            s = String.valueOf(s) + state + ", ";
        }
        s = s.substring(0, s.length() - 2);
        s = String.valueOf(s) + " ); \n";
        s = String.valueOf(s) + "p( " + mNode.name;
        if (discreteParents.size() > 0) {
            s = String.valueOf(s) + " | ";
            for (MNode p : discreteParents) {
                s = String.valueOf(s) + p.name + " , ";
            }
            s = s.substring(0, s.length() - 2);
        }
        s = String.valueOf(s) + " ) = \n";
        boolean b = true;
        for (String condition : ipcIMs.keySet()) {
            BayesIm bayesIm = ipcIMs.get(condition);
            if (!condition.isEmpty()) {
                if (b) {
                    s = String.valueOf(s) + "if( " + condition + " ) {";
                    b = false;
                } else {
                    s = String.valueOf(s) + "\nelse if( " + condition + " ) {";
                }
            } else {
                s = String.valueOf(s) + "{ ";
            }
            int k = 0;
            while (k < bayesIm.getNumColumns(0)) {
                Double p = bayesIm.getProbability(0, 0, k);
                //String prob = TempMathFunctions.safeDoubleAsString(p);
                String prob = p.toString();
                String state2 = bayesIm.getBayesPm().getCategory(thisNode, k);
                s = String.valueOf(s) + state2 + " : " + prob;
                s = String.valueOf(s) + "; ";
                ++k;
            }
            s = String.valueOf(s) + " }";
        }
        s = String.valueOf(s) + "\n";
        return s;
    }
}

