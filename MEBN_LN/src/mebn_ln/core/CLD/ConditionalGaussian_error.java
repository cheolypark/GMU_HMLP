/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  edu.cmu.tetrad.data.DataSet
 *  edu.cmu.tetrad.graph.EdgeListGraph
 *  edu.cmu.tetrad.graph.Graph
 *  edu.cmu.tetrad.graph.Node
 *  edu.cmu.tetrad.sem.DagScorer
 *  edu.cmu.tetrad.sem.Scorer
 *  edu.cmu.tetrad.sem.SemIm
 *  edu.cmu.tetrad.util.TetradMatrix
 *  mebn_rm.MEBN.CLD.LPD_Continuous
 *  mebn_rm.MEBN.MNode.MNode
 *  mebn_rm.util.TempMathFunctions
 *  mebn_rm.util.Tetrad_Util
 */
package mebn_ln.core.CLD;

import edu.cmu.tetrad.data.DataSet;
import edu.cmu.tetrad.graph.EdgeListGraph;
import edu.cmu.tetrad.graph.Graph;
import edu.cmu.tetrad.graph.Node;
import edu.cmu.tetrad.sem.DagScorer;
import edu.cmu.tetrad.sem.Scorer;
import edu.cmu.tetrad.sem.SemIm;
import edu.cmu.tetrad.util.TetradMatrix;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import mebn_rm.MEBN.CLD.LPD_Continuous;
import mebn_rm.MEBN.MNode.MNode; 
import mebn_rm.util.Tetrad_Util;
import util.TempMathFunctions;

public class ConditionalGaussian_error
extends LPD_Continuous {
    public ConditionalGaussian_error() {
        super("", "ConditionalGaussian_error");
        this.parameterSize = 1;
        this.isSampling = false;
    }

    public ConditionalGaussian_error(String name) {
        super(name, "ConditionalGaussian_error");
        this.parameterSize = 1;
        this.isSampling = false;
    }

    public void calculateBestPara_op(String ipc, DataSet _dataSet_con, Graph continuousGraph) {
        EdgeListGraph graph = new EdgeListGraph();
        DataSet data = Tetrad_Util.getDifferenceData((String)this.mNode.name, (DataSet)_dataSet_con, (Graph)graph);
        DagScorer scorer = new DagScorer(data);
        if (this.mNode.name.equalsIgnoreCase("AltitudeReport")) {
            System.out.println();
        }
        if (_dataSet_con.getNumRows() == 0) {
            this.ipcScorers.put(ipc, null);
        } else if (!Tetrad_Util.hasVariance((DataSet)_dataSet_con)) {
            this.ipcScorers.put(ipc, null);
        } else {
            double fml = scorer.score((Graph)graph);
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

    public String getCPS_op(Object ob) {
        Scorer sc = (Scorer)ob;
        List<MNode> cp = this.mNode.getContinuousParents();
        String s = "";
        if (this.mNode.name.equalsIgnoreCase("AltitudeReport")) {
            System.out.println();
        }
        if (sc != null) {
            SemIm im = sc.getEstSem();
            System.out.println(im.toString());
            double mean = im.getMean((Node)sc.getVariables().get(0));
            TetradMatrix implCovar = im.getImplCovar(false);
            double var = im.getVariance((Node)sc.getVariables().get(0), implCovar);
            for (MNode p : cp) {
                Node parent = sc.getDataSet().getVariable(p.name);
                Node child = sc.getDataSet().getVariable(this.mNode.name);
                s = String.valueOf(s) + " 1.0 * " + p.name + " + ";
            }
            String meanS = TempMathFunctions.safeDoubleAsString((double)mean);
            String varS = TempMathFunctions.safeDoubleAsString((double)var);
            s = String.valueOf(s) + "NormalDist( 0.0 , " + varS + ");";
        } else {
            for (MNode p : cp) {
                s = String.valueOf(s) + "1.0 * " + p.name + " + ";
            }
            s = String.valueOf(s) + "NormalDist(0 ,  0.1);";
        }
        return s;
    }
}

