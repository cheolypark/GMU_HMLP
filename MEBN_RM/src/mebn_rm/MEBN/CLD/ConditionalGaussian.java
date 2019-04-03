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
 
import edu.cmu.tetrad.data.DataSet;
import edu.cmu.tetrad.graph.EdgeListGraph;
import edu.cmu.tetrad.graph.Graph;
import edu.cmu.tetrad.graph.Node;
import edu.cmu.tetrad.sem.DagScorer;
import edu.cmu.tetrad.sem.Scorer;
import edu.cmu.tetrad.sem.SemIm; 
import edu.cmu.tetrad.util.TetradMatrix;   
import java.util.List; 
import mebn_rm.MEBN.CLD.LPD_Continuous;
import mebn_rm.MEBN.MNode.MNode; 
import mebn_rm.RDB.RDB; 
import mebn_rm.util.StringUtil;
import mebn_rm.util.Tetrad_Util; 
import util.TempMathFunctions;

/**
 * ConditionalGaussian is the class to perform functions related to local distribution of 
 * a continuous random variable in Gaussian distribution. The class performs calculation 
 * for a local distribution and construction of the script of the local distribution.
 * <p>
 * 
 * @author      Cheol Young Park
 * @version     0.0.1
 * @since       1.5
 */

public class ConditionalGaussian extends LPD_Continuous {
	TempMathFunctions tmath = new TempMathFunctions();
    
    public ConditionalGaussian() {
        super("", "ConditionalGaussian");
        parameterSize = 1;
        isSampling = false;
    } 
    
    public void calculateBestPara_op(String ipc, DataSet _dataSet_con, Graph continuousGraph) {
    	 
        System.out.println("////////////////////////////////" + mNode.name);
        System.out.println((Object)continuousGraph);
         
        if (_dataSet_con.getNumRows() == 0) {
            ipcScorers.put(ipc, null);
        } else if (!Tetrad_Util.hasVariance(_dataSet_con)) {
            ipcScorers.put(ipc, null);
        } else {
            DagScorer scorer = new DagScorer(_dataSet_con);
            double fml = scorer.score(continuousGraph);
//            System.out.println("FML (scorer) = " + fml);
//            System.out.println("BIC = " + scorer.getBicScore());
//            System.out.println("AIC = " + scorer.getAicScore());
//            System.out.println("DOF = " + scorer.getDof());
//            System.out.println("# free params = " + scorer.getNumFreeParams());
            SemIm im = scorer.getEstSem();
//            System.out.println("est sem = " + (Object)im);
            ipcScorers.put(ipc, scorer);
        }
    }
    
    public void calculateBestPara_op_default(DataSet _dataSet_con, Graph continuousGraph) {
    	DagScorer scorer = new DagScorer(_dataSet_con); 
        defaultScorer = scorer; 
    }
      
    public String getCLD_op(Object ob) {
        Scorer sc = (Scorer)ob;
        List<MNode> cp = mNode.getContinuousParents();
        String s = "";
        if (sc != null) {
            SemIm im = sc.getEstSem(); 
            double mean = im.getMean((Node)sc.getVariables().get(0));
            TetradMatrix implCovar = im.getImplCovar(false);
            double var = im.getVariance((Node)sc.getVariables().get(0), implCovar);
            for (MNode p : cp) {
                Node parent = sc.getDataSet().getVariable(p.columnName);
                Node child = sc.getDataSet().getVariable(mNode.name);
                double edgeCoef = im.getEdgeCoef(parent, child);
                if (edgeCoef == 0.0){
                	System.out.println( "The edgeCoef is zero.");
                	edgeCoef = tmath.getSmalNumber();
                }
                String edgeCoefs = tmath.safeDoubleAsString2(edgeCoef);
                s = String.valueOf(s) + edgeCoefs + " * " + "Sum(" + p.name + ")" + " + ";
                double meanParent = im.getMean(parent);
                double varParent = implCovar.get(sc.getVariables().indexOf((Object)parent), sc.getVariables().indexOf((Object)child));
                mean -= edgeCoef * meanParent;
                System.out.println(var -= edgeCoef * varParent);
            }
            String meanS = tmath.safeDoubleAsString2(mean);
            String varS = tmath.safeDoubleAsString2(Math.abs(var));
            s = cp.size() == 0 ? String.valueOf(s) + "NormalDist( " + meanS + ", " + varS + ")" : String.valueOf(s) + "NormalDist( " + meanS + ", " + varS + ")";
        } else {
            for (MNode p : cp) {
                s = String.valueOf(s) + "1.0 * " + p.name + " + ";
            }
            s = String.valueOf(s) + "NormalDist(0.0 ,  100000000);";
        }
        return s;
    }

    public String getCLD_default_op(Object ob) {
		Scorer sc = (Scorer) ob;
		String s = "";
		if (sc != null) {
			SemIm im = sc.getEstSem();
			double mean = im.getMean((Node) sc.getVariables().get(0));
			TetradMatrix implCovar = im.getImplCovar(false);
			double var = im.getVariance((Node) sc.getVariables().get(0), implCovar);
			String meanS = tmath.safeDoubleAsString2(mean);
			String varS = tmath.safeDoubleAsString2(Math.abs(var));
			s = String.valueOf(s) + "NormalDist( " + meanS + ", " + varS + ")";
		} else {
			s = String.valueOf(s) + "NormalDist(0.0 ,  100000000);";
		}
		return s;
	}
      
    public Double calculateBestPara() {
        EdgeListGraph hybridGraph = new EdgeListGraph();
        IPCs_Data = initIPCs((Graph)hybridGraph);
        if (IPCs_Data.size() == 0) {
            IPCs_Data.add("null");
        } 
        
        for (String ipc : IPCs_Data) {
//            System.out.println(ipc);
            DataSet _dataSet_con = null;
            _dataSet_con = ipc.equalsIgnoreCase("") ? selectedData : Tetrad_Util.getSubsetdataFromIPC(ipc, selectedData);
            EdgeListGraph continuousGraph = new EdgeListGraph();
            Node child2 = _dataSet_con.getVariable(mNode.getColumnName());
            continuousGraph.addNode(child2);
            List<MNode> listPar = mNode.getAllParents();
            for (MNode mn : listPar) {
                if (!mn.isContinuous()) continue;
                Node parent = data.getVariable(mn.getColumnName());
                continuousGraph.addNode(parent);
                continuousGraph.addDirectedEdge(parent, child2);
            }
            calculateBestPara_op(ipc, _dataSet_con, (Graph)continuousGraph);
        }
        
        // create default distribution
        if (mNode.cvsFile != null) {	// contains a cvs for a default data
        	System.out.println(mNode.cvsFile);
        	
            String strFile = mNode.cvsFile;
            defaultData = (DataSet)RDB.This().getTetDataSetFromCSV(strFile);
            
            if (defaultData == null || defaultData.getNumRows() == 0) {
                return null;
            } 
              
            EdgeListGraph continuousGraph = new EdgeListGraph();
            Node child2 = defaultData.getVariable(mNode.getColumnName());
            continuousGraph.addNode(child2);
            calculateBestPara_op_default(defaultData, continuousGraph);
        }
        
        return null;
    }
 
    public String getILD() {
        List<MNode> dp = mNode.getDiscreteParents();
        List<MNode> cp = mNode.getContinuousParents();
        
        String s = "{\n";
        s = s + "defineState(Continuous); \n";
        s = s + "p( " + mNode.getColumnName();
        
        if (dp.size() > 0 || cp.size() > 0) {
            s = s + " | ";
        }
        
        for (MNode p2 : dp) {
            s = s + p2.getColumnName() + " , ";
        }
        
        for (MNode p2 : cp) {
            s = s + p2.getColumnName() + " , ";
        }
        
        if (dp.size() > 0 || cp.size() > 0) {
            s = s.substring(0, s.length() - 2);
        }
        
        s = s + " ) = \n";
          
        int i = 0;
 
        for (String k : ipcScorers.keySet()) {
            Scorer sc = ipcScorers.get(k);
            if (!k.isEmpty()) {
                String conditions = k;
                s = i == 0 ? s + "if( " + conditions + " ) {" : s + "else if( " + conditions + " ) {";
            }
            s = s + getILD_op((Object)sc);
            ++i;
            if (k.isEmpty()) continue;
            s = s + "\n}";
        }
        
        return s;
    }
      
    
    public String getILD_op(Object ob) { 
    	Scorer sc = (Scorer)ob;
        List<MNode> cp = mNode.getContinuousParents();
        String s = "";
         
        if (sc != null) {
        	SemIm im = sc.getEstSem();
//            System.out.println(im.toString());
            double mean = im.getMean((Node)sc.getVariables().get(0));
            TetradMatrix implCovar = im.getImplCovar(false);
            double var = im.getVariance((Node)sc.getVariables().get(0), implCovar);
            for (MNode p : cp) { 
            	
            	Node parent = sc.getDataSet().getVariable(p.getColumnName());
                Node child = sc.getDataSet().getVariable(mNode.name);
                double edgeCoef = im.getEdgeCoef(parent, child);
                if (edgeCoef == 0.0){ 
                	edgeCoef = tmath.getSmalNumber();
                }
                String edgeCoefs = tmath.safeDoubleAsString2(edgeCoef);
                s = String.valueOf(s) + edgeCoefs + " * " +  p.getColumnName() + " + ";
                double meanParent = im.getMean(parent);
                double varParent = implCovar.get(sc.getVariables().indexOf((Object)parent), sc.getVariables().indexOf((Object)child));
                mean -= edgeCoef * meanParent;
                System.out.println(var -= edgeCoef * varParent);
            }
              
            String meanS = tmath.safeDoubleAsString2(mean);
            String varS = tmath.safeDoubleAsString2(Math.abs(var));
            s = cp.size() == 0 ? String.valueOf(s) + "NormalDist( " + meanS + ", " + varS + ");" : String.valueOf(s) + "NormalDist( " + meanS + ", " + varS + ");";
 
        } else {
        	for (MNode p : cp) {
        		s = String.valueOf(s) + "1.0 * " + p.getColumnName() + " + ";
        	}
        	s = String.valueOf(s) + "NormalDist(0 ,  0.1);";
        }
        
        return s;
    }
 
    // 	if some obj have (VehicleType = Tracked ) [
    //    10 * CARDINALITY(obj) + NormalDist(10, 5)
    //  ] else [
    //    NormalDist(10, 5)
    //	]
    //
    public String toString(List<String> inclusions) {
        String s = "";
        if (inclusions.contains("CLD")) {
        	
        	// get ovs statement
        	String ovs = "";
        	List<MNode> parents = mNode.getDiscreteParents(); 
        	for (MNode mn : parents){
        		ovs += mn.toStringOVs();
        		ovs += ",";
        	}
        	ovs = new StringUtil().removeRedundantItem(ovs);
        	ovs = ovs.replace(",", ".");        	
        	ovs = ovs.replace(" ", "");
        	        	
            s = s + "[L: ";
            int i = 0;
            for (String k : ipcScorers.keySet()) {
                Scorer sc = ipcScorers.get(k); 
                
                k = k.replace("&&", "&");
                k = k.replace("==", "=");
                
                if (parents.size() > 0) {
	                s = i == 0 ? s + "if some " + ovs + " have ( " + k + " ) [" 
	                		   : s + "else if some " + ovs + " have ( " + k + " ) [";
                }
                
                s = s + getCLD_op((Object)sc);
                ++i;
                if (k.isEmpty()) continue;
                s = s + "]\n			";
            }
            
            // * Add default distribution */
            // For the continuous case, we didn't define yet a default distribution grammar, 
            // so we commented out here. 
            List<MNode> cparents = mNode.getContinuousParents();
            if (cparents.size() > 0) {
                s = s + "else[\n			";
	            s = s + getCLD_default_op((Object)defaultScorer);
	            s = s + "]\n";
	        }
//            
            s = s + "]\r\n";
            
        }
        return s;
    }
}

