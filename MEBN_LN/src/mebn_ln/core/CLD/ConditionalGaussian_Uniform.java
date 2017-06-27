/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  edu.cmu.tetrad.data.DataSet
 *  edu.cmu.tetrad.graph.Graph
 *  mebn_rm.MEBN.CLD.LPD_Continuous
 *  mebn_rm.MEBN.MNode.MNode
 */
package mebn_ln.core.CLD;

import edu.cmu.tetrad.data.DataSet;
import edu.cmu.tetrad.graph.Graph;
import java.util.List;
import java.util.Map;
import mebn_rm.MEBN.CLD.LPD_Continuous;
import mebn_rm.MEBN.MNode.MNode;

public class ConditionalGaussian_Uniform
extends LPD_Continuous {
    public ConditionalGaussian_Uniform() {
        super("", "ConditionalGaussian_Uniform");
        this.parameterSize = 1;
        this.isSampling = false;
    }

    public ConditionalGaussian_Uniform(String name) {
        super(name, "ConditionalGaussian_Uniform");
        this.parameterSize = 1;
        this.isSampling = false;
    }

    public void calculateBestPara_op(String ipc, DataSet _dataSet_con, Graph continuousGraph) {
        this.ipcScorers.put(ipc, null);
    }

    public String getCPS_op(Object ob) {
        List<MNode> cp = this.mNode.getContinuousParents();
        String s = "";
        for (MNode p : cp) {
            s = String.valueOf(s) + "1.0 * " + p.name + " + ";
        }
        s = String.valueOf(s) + "NormalDist(0 ,  100000000);";
        return s;
    }
}

