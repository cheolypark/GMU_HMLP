/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  edu.cmu.tetrad.data.DataSet
 *  edu.cmu.tetrad.graph.Graph
 *  edu.cmu.tetrad.graph.Node
 *  edu.cmu.tetrad.sem.DagScorer
 *  edu.cmu.tetrad.sem.Scorer
 *  edu.cmu.tetrad.sem.SemIm
 *  edu.cmu.tetrad.util.StatUtils
 *  edu.cmu.tetrad.util.TetradMatrix
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mebn_rm.MEBN.CLD.LPD_Continuous;
import mebn_rm.MEBN.MNode.MNode; 
import mebn_rm.RDB.RDB;
import mebn_rm.data.ConditionalDataSet;
import mebn_rm.util.StringUtil;
import mebn_rm.util.Tetrad_Util; 
import util.TempMathFunctions;

public class ConditionalGaussian extends LPD_Continuous {

    
    public ConditionalGaussian() {
        super("", "ConditionalGaussian");
        this.parameterSize = 1;
        this.isSampling = false;
    } 
    
    public void calculateBestPara_op(String ipc, DataSet _dataSet_con, Graph continuousGraph) {
    	 
        System.out.println("////////////////////////////////" + this.mNode.name);
        System.out.println((Object)continuousGraph);
        
        if (this.mNode.name.equalsIgnoreCase("QR_RESULT_COL_1")) {
            System.out.println(this.mNode.name);
        }
        
        
        if (_dataSet_con.getNumRows() == 0) {
            this.ipcScorers.put(ipc, null);
        } else if (!Tetrad_Util.hasVariance(_dataSet_con)) {
            this.ipcScorers.put(ipc, null);
        } else {
            DagScorer scorer = new DagScorer(_dataSet_con);
            double fml = scorer.score(continuousGraph);
            System.out.println("FML (scorer) = " + fml);
            System.out.println("BIC = " + scorer.getBicScore());
            System.out.println("AIC = " + scorer.getAicScore());
            System.out.println("DOF = " + scorer.getDof());
            System.out.println("# free params = " + scorer.getNumFreeParams());
            SemIm im = scorer.getEstSem();
            System.out.println("est sem = " + (Object)im);
            this.ipcScorers.put(ipc, scorer);
        }
    }
    
    public void calculateBestPara_op_default(DataSet _dataSet_con, Graph continuousGraph) {
    	DagScorer scorer = new DagScorer(_dataSet_con); 
        defaultScorer = scorer; 
    }
      
    public String getCLD_op(Object ob) {
        Scorer sc = (Scorer)ob;
        List<MNode> cp = this.mNode.getContinuousParents();
        String s = "";
        if (sc != null) {
            SemIm im = sc.getEstSem(); 
            double mean = im.getMean((Node)sc.getVariables().get(0));
            TetradMatrix implCovar = im.getImplCovar(false);
            double var = im.getVariance((Node)sc.getVariables().get(0), implCovar);
            for (MNode p : cp) {
                Node parent = sc.getDataSet().getVariable(p.name);
                Node child = sc.getDataSet().getVariable(this.mNode.name);
                double edgeCoef = im.getEdgeCoef(parent, child);
                String edgeCoefs = TempMathFunctions.safeDoubleAsString2(edgeCoef);
                s = String.valueOf(s) + edgeCoefs + " * " + "Sum(" + p.name + ")" + " + ";
                double meanParent = im.getMean(parent);
                double varParent = implCovar.get(sc.getVariables().indexOf((Object)parent), sc.getVariables().indexOf((Object)child));
                mean -= edgeCoef * meanParent;
                System.out.println(var -= edgeCoef * varParent);
            }
            String meanS = TempMathFunctions.safeDoubleAsString2(mean);
            String varS = TempMathFunctions.safeDoubleAsString2(Math.abs(var));
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
   	 Scorer sc = (Scorer)ob;
        String s = "";
        if (sc != null) {
            SemIm im = sc.getEstSem(); 
            double mean = im.getMean((Node)sc.getVariables().get(0));
            TetradMatrix implCovar = im.getImplCovar(false);
            double var = im.getVariance((Node)sc.getVariables().get(0), implCovar);              
            String meanS = TempMathFunctions.safeDoubleAsString2(mean);
            String varS = TempMathFunctions.safeDoubleAsString2(Math.abs(var));
            s = String.valueOf(s) + "NormalDist( " + meanS + ", " + varS + ")";
        } else {
            s = String.valueOf(s) + "NormalDist(0.0 ,  100000000);";
        }
        return s;
    }
      
    public Double calculateBestPara(ConditionalDataSet CD, ConditionalDataSet prior_CD) {
        EdgeListGraph hybridGraph = new EdgeListGraph();
        IPCs = initIPCs((Graph)hybridGraph);
        if (IPCs.size() == 0) {
            IPCs.add("null");
        } 
        
        for (String ipc : IPCs) {
            System.out.println(ipc);
            DataSet _dataSet_con = null;
            _dataSet_con = ipc.equalsIgnoreCase("") ? selectedData : Tetrad_Util.getSubsetdataFromIPC(ipc, selectedData);
            EdgeListGraph continuousGraph = new EdgeListGraph();
            Node child2 = _dataSet_con.getVariable(mNode.name);
            continuousGraph.addNode(child2);
            List<MNode> listPar = mNode.getAllParents();
            for (MNode mn : listPar) {
                if (!mn.isContinuous()) continue;
                Node parent = data.getVariable(mn.name);
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
            if (defaultData.getNumRows() == 0) {
                return null;
            } 
              
            EdgeListGraph continuousGraph = new EdgeListGraph();
            Node child2 = defaultData.getVariable(mNode.name);
            continuousGraph.addNode(child2);
            calculateBestPara_op_default(defaultData, continuousGraph);
        }
        
        return null;
    }
 
    public String getILD() {
        List<MNode> dp = mNode.getDiscreteParents();
        List<MNode> cp = mNode.getContinuousParents();
        
        String s = "{ defineState(Continuous); \n";
        s = s + "p( " + mNode.name;
        
        if (dp.size() > 0 || cp.size() > 0) {
            s = s + " | ";
        }
        
        for (MNode p2 : dp) {
            s = s + p2.name + " , ";
        }
        
        for (MNode p2 : cp) {
            s = s + p2.name + " , ";
        }
        
        if (dp.size() > 0 || cp.size() > 0) {
            s = s.substring(0, s.length() - 2);
        }
        
        s = s + " ) = \n";
          
        int i = 0;
        
        if (this.name.equalsIgnoreCase("QR_RESULT_COL_1_cld_1"))
        {
        	System.out.println("QR_RESULT_COL_1");
        }
//        	 
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
     
//    Scorer sc = (Scorer)ob;
//    List<MNode> cp = this.mNode.getContinuousParents();
//    String s = "";
//    if (sc != null) {
    
    public String getILD_op(Object ob) {
//    	if (this.mNode.name.equalsIgnoreCase("QR_RESULT_COL_1")) {
//            System.out.println();
//        }
    	
    	Scorer sc = (Scorer)ob;
        List<MNode> cp = this.mNode.getContinuousParents();
        String s = "";
          
        if (sc != null) {
        	SemIm im = sc.getEstSem();
            System.out.println(im.toString());
            double mean = im.getMean((Node)sc.getVariables().get(0));
            TetradMatrix implCovar = im.getImplCovar(false);
            double var = im.getVariance((Node)sc.getVariables().get(0), implCovar);
            for (MNode p : cp) {
//                  Node parent = sc.getDataSet().getVariable(p.name);
//                  Node child = sc.getDataSet().getVariable(this.mNode.name);
//                  s = String.valueOf(s) + " 1.0 * " + p.name + " + ";
            	  
            	Node parent = sc.getDataSet().getVariable(p.name);
                Node child = sc.getDataSet().getVariable(this.mNode.name);
                double edgeCoef = im.getEdgeCoef(parent, child);
                String edgeCoefs = TempMathFunctions.safeDoubleAsString2(edgeCoef);
                s = String.valueOf(s) + edgeCoefs + " * " +  p.name + " + ";
                double meanParent = im.getMean(parent);
                double varParent = implCovar.get(sc.getVariables().indexOf((Object)parent), sc.getVariables().indexOf((Object)child));
                mean -= edgeCoef * meanParent;
                System.out.println(var -= edgeCoef * varParent);
            }
              
            String meanS = TempMathFunctions.safeDoubleAsString2(mean);
            String varS = TempMathFunctions.safeDoubleAsString2(Math.abs(var));
            s = cp.size() == 0 ? String.valueOf(s) + "NormalDist( " + meanS + ", " + varS + ");" : String.valueOf(s) + "NormalDist( " + meanS + ", " + varS + ");";

//              String meanS = TempMathFunctions.safeDoubleAsString((double)mean);
//              String varS = TempMathFunctions.safeDoubleAsString((double)var);
//              s = String.valueOf(s) + "NormalDist( "+" meanS" + " , " + varS + ");";
        } else {
        	for (MNode p : cp) {
        		s = String.valueOf(s) + "1.0 * " + p.name + " + ";
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
            // In the future, we will release here.
//            s = s + "else[\n			";
//            s = s + getCLD_default_op((Object)this.defaultScorer);
//            s = s + "]\n";
//            
            s = s + "]\r\n";
            
        }
        return s;
    }
}

