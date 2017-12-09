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
package mebn_ln.core.CLD;

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
import mebn_rm.util.Tetrad_Util;
import util.TempMathFunctions;

public class ConditionalGaussian_error
extends LPD_Continuous {
    public ConditionalGaussian_error() {
        super("", "ConditionalGaussian_error");
        parameterSize = 1;
        isSampling = false;
    }

    public ConditionalGaussian_error(String name) {
        super(name, "ConditionalGaussian_error");
        parameterSize = 1;
        isSampling = false;
    }

    public void calculateBestPara_op(String ipc, DataSet _dataSet_con, Graph continuousGraph) {
        EdgeListGraph graph = new EdgeListGraph();
        DataSet data = Tetrad_Util.getDifferenceData((String)mNode.name, (DataSet)_dataSet_con, (Graph)graph);
        DagScorer scorer = new DagScorer(data);
        if (mNode.name.equalsIgnoreCase("AltitudeReport")) {
            System.out.println();
        }
        if (_dataSet_con.getNumRows() == 0) {
            ipcScorers.put(ipc, null);
        } else if (!Tetrad_Util.hasVariance((DataSet)_dataSet_con)) {
            ipcScorers.put(ipc, null);
        } else {
            double fml = scorer.score((Graph)graph);
            System.out.println("FML (scorer) = " + fml);
            System.out.println("BIC = " + scorer.getBicScore());
            System.out.println("AIC = " + scorer.getAicScore());
            System.out.println("DOF = " + scorer.getDof());
            System.out.println("# free params = " + scorer.getNumFreeParams());
            SemIm im = scorer.getEstSem();
            System.out.println("est sem = " + (Object)im);
            ipcScorers.put(ipc, scorer);
        }
    }

    public String getILD_op(Object ob) {
        Scorer sc = (Scorer)ob;
        List<MNode> cp = mNode.getContinuousParents();
        String s = "";
        if (mNode.name.equalsIgnoreCase("AltitudeReport")) {
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
                Node child = sc.getDataSet().getVariable(mNode.name);
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

