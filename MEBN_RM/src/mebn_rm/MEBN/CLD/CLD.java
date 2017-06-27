/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  edu.cmu.tetrad.data.ColtDataSet
 *  edu.cmu.tetrad.data.DataSet
 *  edu.cmu.tetrad.graph.Graph
 *  edu.cmu.tetrad.graph.Node
 */
package mebn_rm.MEBN.CLD;

import edu.cmu.tetrad.data.ColtDataSet;
import edu.cmu.tetrad.data.DataSet;
import edu.cmu.tetrad.graph.Graph;
import edu.cmu.tetrad.graph.Node;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import mebn_rm.MEBN.CLD.Probability;
import mebn_rm.MEBN.MFrag.MFrag;
import mebn_rm.MEBN.MNode.MNode;
import mebn_rm.MEBN.MTheory.MTheory;
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
    public DataSet data = null;
    public List<String> arrayCategories = null;
    public String lpd_type = "";
    public Boolean isSampling = true;
    public SortableValueMap<Parameter, Double> paraCANs = new SortableValueMap();
    public Parameter bestParameter = null;
    public ConditionalDataSet CD = new ConditionalDataSet();

    public CLD(String n, String c) {
        this.name = n;
        this.lpd_type = c;
    }

    public String toString() {
        String s = "[L [";
        s = String.valueOf(s) + this.lpd_type + "]: ";
        s = String.valueOf(s) + this.name;
        s = String.valueOf(s) + "]\r\n";
        return s;
    }

    public String toString(List<String> inclusions) {
        String s = "";
        if (inclusions.contains("CLD")) {
            s = String.valueOf(s) + "[L [";
            s = String.valueOf(s) + this.lpd_type + "]: ";
            s = String.valueOf(s) + this.name;
            s = String.valueOf(s) + "]";
        }
        return s;
    }

    public int getParameterSize() {
        return this.parameterSize;
    }

    public String getCPS() {
        return "";
    }

    public String getCPS_op(Object ob) {
        return "";
    }

    public List<String> getCategories() {
        return null;
    }

    public void addCategories(String c) {
        if (this.arrayCategories == null) {
            this.arrayCategories = new ArrayList<String>();
            this.arrayCategories.add(c);
        } else {
            this.arrayCategories.add(c);
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
        for (Parameter p : this.paraCANs.keySet()) {
            Double logSC = this.paraCANs.get(p);
            logSCs.add(logSC);
            System.out.println(String.valueOf(p.toString()) + " Log Parameter scores : " + logSC);
        }
        return new Sum_for_Log().sum(logSCs);
    }

    public Double getAvgLogParaScore() {
        Double logSC = 0.0;
        for (Parameter p : this.paraCANs.keySet()) {
            logSC = logSC + this.paraCANs.get(p);
        }
        return logSC / (double)this.paraCANs.size();
    }

    public void resetParaCANsPrior() {
        for (Parameter para : this.paraCANs.keySet()) {
            this.paraCANs.put(para, Math.log(1.0 / (double)this.paraCANs.size()));
        }
    }

    public Double calculateBestPara(ConditionalDataSet CD, ConditionalDataSet prior_CD) {
        return null;
    }

    public void calculateBestPara_op(String ipc, DataSet _dataSet_con, Graph continuousGraph) {
    }

    public List<String> initIPCs(Graph hybridGraph) {
        if (this.mNode.name.equalsIgnoreCase("HI_temperature")) {
            System.out.println();
        }
        String strFile = String.valueOf(Resource.getCSVPath(this.mNode.mFrag.mTheory.name)) + this.mNode.mFrag.name + ".csv";
        this.data = RDB.This().getTetDataSetFromCSV(strFile);
        if (this.data.getNumRows() == 0) {
            return null;
        }
        System.out.println(this.data.getVariables());
        if (this.mNode.name.equalsIgnoreCase("HI_temperature")) {
            System.out.println();
        }
        Node child = this.data.getVariable(this.mNode.name);
        hybridGraph.addNode(child);
        List<String> parents = this.mNode.getAllParentNames();
        if (parents != null) {
            for (String pr : parents) {
                Node parent = this.data.getVariable(pr);
                hybridGraph.addNode(parent);
                hybridGraph.addDirectedEdge(parent, child);
            }
        }
        this.selectedData = (ColtDataSet)this.data.subsetColumns(hybridGraph.getNodes());
        return Tetrad_Util.getIPC(this.mNode, this.selectedData, this.mNode.name);
    }

    public void save(String filename) {
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            fos = new FileOutputStream(filename);
            out = new ObjectOutputStream(fos);
            out.writeObject(this.CD);
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
            this.CD = (ConditionalDataSet)in.readObject();
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

