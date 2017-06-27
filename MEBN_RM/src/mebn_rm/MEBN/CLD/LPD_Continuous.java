/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  edu.cmu.tetrad.data.DataSet
 *  edu.cmu.tetrad.graph.EdgeListGraph
 *  edu.cmu.tetrad.graph.Graph
 *  edu.cmu.tetrad.graph.Node
 *  edu.cmu.tetrad.sem.Scorer
 */
package mebn_rm.MEBN.CLD;

import edu.cmu.tetrad.data.DataSet;
import edu.cmu.tetrad.graph.EdgeListGraph;
import edu.cmu.tetrad.graph.Graph;
import edu.cmu.tetrad.graph.Node;
import edu.cmu.tetrad.sem.Scorer;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import mebn_rm.MEBN.CLD.CLD;
import mebn_rm.MEBN.MNode.MNode;
import mebn_rm.data.ConditionalDataSet;
import mebn_rm.util.Tetrad_Util;

public class LPD_Continuous
extends CLD {
    public Map<String, Scorer> ipcScorers = new HashMap<String, Scorer>();

    public LPD_Continuous() {
        super("", "LPD_Continuous");
    }

    public LPD_Continuous(String name, String type) {
        super(name, type);
    }

    @Override
    public Double calculateBestPara(ConditionalDataSet CD, ConditionalDataSet prior_CD) {
        EdgeListGraph hybridGraph = new EdgeListGraph();
        this.IPCs = this.initIPCs((Graph)hybridGraph);
        if (this.IPCs.size() == 0) {
            this.IPCs.add("null");
        }
        if (this.mNode.name.equalsIgnoreCase("HI_temperature")) {
            System.out.println(this.mNode.name);
        }
        for (String ipc : this.IPCs) {
            System.out.println(ipc);
            DataSet _dataSet_con = null;
            _dataSet_con = ipc.equalsIgnoreCase("") ? this.selectedData : Tetrad_Util.getSubsetdataFromIPC(ipc, this.selectedData);
            EdgeListGraph continuousGraph = new EdgeListGraph();
            Node child2 = _dataSet_con.getVariable(this.mNode.name);
            continuousGraph.addNode(child2);
            List<MNode> listPar = this.mNode.getAllParents();
            for (MNode mn : listPar) {
                if (!mn.isContinuous()) continue;
                Node parent = this.data.getVariable(mn.name);
                continuousGraph.addNode(parent);
                continuousGraph.addDirectedEdge(parent, child2);
            }
            this.calculateBestPara_op(ipc, _dataSet_con, (Graph)continuousGraph);
        }
        return null;
    }

    @Override
    public String getCPS() {
        List<MNode> dp = this.mNode.getDiscreteParents();
        List<MNode> cp = this.mNode.getContinuousParents();
        String s = "{ defineState(Continuous); \n";
        s = String.valueOf(s) + "p( " + this.mNode.name;
        if (dp.size() > 0 || cp.size() > 0) {
            s = String.valueOf(s) + " | ";
        }
        for (MNode p2 : dp) {
            s = String.valueOf(s) + p2.name + " , ";
        }
        for (MNode p2 : cp) {
            s = String.valueOf(s) + p2.name + " , ";
        }
        if (dp.size() > 0 || cp.size() > 0) {
            s = s.substring(0, s.length() - 2);
        }
        s = String.valueOf(s) + " ) = \n";
        if (this.mNode.name.equalsIgnoreCase("TemperatureReport")) {
            System.out.println();
        }
        int i = 0;
        for (String k : this.ipcScorers.keySet()) {
            Scorer sc = this.ipcScorers.get(k);
            if (!k.isEmpty()) {
                String conditions = k;
                s = i == 0 ? String.valueOf(s) + "if( " + conditions + " ) {" : String.valueOf(s) + "else if( " + conditions + " ) {";
            }
            s = String.valueOf(s) + this.getCPS_op((Object)sc);
            ++i;
            if (k.isEmpty()) continue;
            s = String.valueOf(s) + "\n}";
        }
        return s;
    }

    @Override
    public String toString(List<String> inclusions) {
        String s = "";
        if (inclusions.contains("CLD")) {
            s = String.valueOf(s) + "[L: ";
            int i = 0;
            for (String k : this.ipcScorers.keySet()) {
                Scorer sc = this.ipcScorers.get(k);
                s = String.valueOf(s) + this.getCPS_op((Object)sc);
                ++i;
                if (k.isEmpty()) continue;
                s = String.valueOf(s) + "\n}";
            }
            s = String.valueOf(s) + "]\r\n";
        }
        return s;
    }
}

