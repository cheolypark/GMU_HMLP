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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException; 
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream; 
import java.util.ArrayList;
import java.util.List; 
import mebn_rm.MEBN.CLD.Probability; 
import mebn_rm.MEBN.MNode.MNode; 
import mebn_rm.MEBN.parameter.Parameter;
import mebn_rm.RDB.RDB;
import mebn_rm.data.ConditionalDataSet;
import mebn_rm.util.Resource; 
import mebn_rm.util.Tetrad_Util;
import util.SortableValueMap;
import util.math.Sum_for_Log; 

public class CLD
extends Probability
implements Comparable<CLD> {
    public MNode mNode = null;
    public String name;
    public int parameterSize;
    public List<String> IPCs = null;
    public DataSet selectedData = null;
    public DataSet defaultData = null;
    public DataSet data = null;
    public List<String> arrayCategories = null;
    public String cld_type = "";
    public Boolean isSampling = true;
    public SortableValueMap<Parameter, Double> paraCANs = new SortableValueMap();
    public Parameter bestParameter = null;
    public ConditionalDataSet CD = new ConditionalDataSet();

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

    public int getParameterSize() {
        return parameterSize;
    }

    public String getILD() {
        return "";
    }

    public String getILD_op(Object ob) {
        return "";
    }
    
    public String getCLD_op(Object ob) {
        return "";
    }
    
    public String getCLD_default_op(Object ob) {
        return "";
    }

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
 
    public Integer sizeOf(ArrayList<String> ps, String cmp) {
        Integer i = 0;
        for (String s : ps) {
            if (!s.equalsIgnoreCase(cmp)) continue;
            i = i + 1;
        }
        return i;
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

    public Double calculateBestPara(ConditionalDataSet CD, ConditionalDataSet prior_CD) {
        return null;
    }

    public void calculateBestPara_op(String ipc, DataSet _dataSet_con, Graph graph) {
    }
    
    public void calculateBestPara_op_default(DataSet _dataSet_con, Graph graph) {
    }
     

    public List<String> initIPCs(Graph hybridGraph) {  
    	
        if (name.equalsIgnoreCase("QR_RESULT_COL_1_cld_1"))
        {
        	System.out.println("QR_RESULT_COL_1");
        }
        
        String strFile = String.valueOf(Resource.getCSVPath(mNode.mFrag.mTheory.name)) + mNode.mFrag.name + ".csv";
        data = RDB.This().getTetDataSetFromCSV(strFile);
        if (data.getNumRows() == 0) {
            return null;
        } 
        
        Node child = data.getVariable(mNode.name);
        hybridGraph.addNode(child);
        List<String> parents = mNode.getAllParentNames();
        if (parents != null) {
            for (String pr : parents) {
                Node parent = data.getVariable(pr);
                hybridGraph.addNode(parent);
                hybridGraph.addDirectedEdge(parent, child);
            }
        }
        selectedData = (ColtDataSet)data.subsetColumns(hybridGraph.getNodes());
        return Tetrad_Util.getIPC(mNode, selectedData, mNode.name);
    }

    public void save(String filename) {
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            fos = new FileOutputStream(filename);
            out = new ObjectOutputStream(fos);
            out.writeObject(CD);
            out.close();
            fos.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void load(String filename) {
        FileInputStream fis = null;
        ObjectInputStream in = null;
        try {
            fis = new FileInputStream(filename);
            in = new ObjectInputStream(fis);
            CD = (ConditionalDataSet)in.readObject();
            in.close();
            fis.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}

