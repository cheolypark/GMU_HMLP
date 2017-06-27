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
import edu.cmu.tetrad.graph.Graph;
import edu.cmu.tetrad.graph.Node;
import edu.cmu.tetrad.sem.DagScorer;
import edu.cmu.tetrad.sem.Scorer;
import edu.cmu.tetrad.sem.SemIm;
import edu.cmu.tetrad.util.StatUtils;
import edu.cmu.tetrad.util.TetradMatrix;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import mebn_rm.MEBN.CLD.LPD_Continuous;
import mebn_rm.MEBN.MNode.MNode;
import mebn_rm.MEBN.rv.RV;
import mebn_rm.data.ConditionalDataSet; 
import mebn_rm.util.Tetrad_Util;
import util.SortableValueMap;
import util.TempMathFunctions;

public class ConditionalGaussian
extends LPD_Continuous {
    public ConditionalGaussian() {
        super("", "ConditionalGaussian");
        this.parameterSize = 1;
        this.isSampling = false;
    }

    public ConditionalGaussian(String name) {
        super(name, "ConditionalGaussian");
        this.parameterSize = 1;
        this.isSampling = false;
    }

    public ArrayList<String> generateParent(int parentSize) {
        ArrayList<String> parents = new ArrayList<String>();
        int i = 0;
        while (i < parentSize) {
            parents.add(this.randomByUniform("T", "W"));
            ++i;
        }
        return parents;
    }

    @Override
    public void calculateBestPara_op(String ipc, DataSet _dataSet_con, Graph continuousGraph) {
        System.out.println("////////////////////////////////" + this.mNode.name);
        System.out.println((Object)continuousGraph);
        if (this.mNode.name.equalsIgnoreCase("LongitudeReport")) {
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

    @Override
    public String getCPS_op(Object ob) {
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
                Node parent = sc.getDataSet().getVariable(p.name);
                Node child = sc.getDataSet().getVariable(this.mNode.name);
                double edgeCoef = im.getEdgeCoef(parent, child);
                String edgeCoefs = TempMathFunctions.safeDoubleAsString(edgeCoef);
                s = String.valueOf(s) + edgeCoefs + " * " + "Sum(" + p.name + ")" + " + ";
                double meanParent = im.getMean(parent);
                double varParent = implCovar.get(sc.getVariables().indexOf((Object)parent), sc.getVariables().indexOf((Object)child));
                mean -= edgeCoef * meanParent;
                System.out.println(var -= edgeCoef * varParent);
            }
            String meanS = TempMathFunctions.safeDoubleAsString(mean);
            String varS = TempMathFunctions.safeDoubleAsString(Math.abs(var));
            System.out.println("mean: " + meanS + " var: " + varS + " his own var: " + im.getVariance((Node)sc.getVariables().get(0), implCovar));
            s = cp.size() == 0 ? String.valueOf(s) + "NormalDist( " + meanS + ", " + varS + ");" : String.valueOf(s) + "NormalDist( " + meanS + ", " + varS + ");";
        } else {
            for (MNode p : cp) {
                s = String.valueOf(s) + "1.0 * " + p.name + " + ";
            }
            s = String.valueOf(s) + "NormalDist(0.0 ,  100000000);";
        }
        return s;
    }

    public Double calculateDist2(ConditionalDataSet CD, ConditionalDataSet prior_CD) {
        SortableValueMap<String, Double> NN = new SortableValueMap<String, Double>();
        double logSC = 0.0;
        Double alpha = 0.0;
        for (RV r2222 : prior_CD.arrayRV) {
            alpha = alpha + r2222.prob;
            logSC -= Math.log(StatUtils.gamma((double)r2222.prob));
        }
        logSC += Math.log(StatUtils.gamma((double)alpha));
        logSC -= Math.log(StatUtils.gamma((double)(alpha + (double)CD.arrayRV.size())));
        for (RV r2222 : CD.arrayRV) {
            RV rv = prior_CD.get(r2222);
            if (rv != null) {
                rv.prob = rv.prob + 1.0;
                continue;
            }
            System.out.println("NULL");
        }
        for (RV r2222 : prior_CD.arrayRV) {
            logSC += Math.log(StatUtils.gamma((double)r2222.prob));
        }
        for (RV r2222 : prior_CD.arrayRV) {
            String nn = r2222.parents.toString();
            if (!NN.containsKey(nn)) {
                NN.put(nn, r2222.prob);
                continue;
            }
            Double d = (Double)NN.get(nn);
            NN.put(nn, d + r2222.prob);
        }
        for (RV r2222 : prior_CD.arrayRV) {
            Double d = (Double)NN.get(r2222.parents.toString());
            r2222.prob = r2222.prob / d;
        }
        return logSC;
    }
}

