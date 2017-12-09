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
import edu.cmu.tetrad.graph.Graph;
import java.util.List; 
import mebn_rm.MEBN.CLD.LPD_Continuous;
import mebn_rm.MEBN.MNode.MNode;

public class ConditionalGaussian_Empty
extends LPD_Continuous {
    public ConditionalGaussian_Empty() {
        super("", "ConditionalGaussian_Empty");
        parameterSize = 1;
        isSampling = false;
    }

    public ConditionalGaussian_Empty(String name) {
        super(name, "ConditionalGaussian_Empty");
        parameterSize = 1;
        isSampling = false;
    }

    public void calculateBestPara_op(String ipc, DataSet _dataSet_con, Graph continuousGraph) {
        ipcScorers.put(ipc, null);
    }

    public String getILD_op(Object ob) {
        List<MNode> cp = mNode.getContinuousParents();
        String s = "";
        for (MNode p : cp) {
            s = String.valueOf(s) + "1.0 * " + p.name + " + ";
        }
        s = String.valueOf(s) + "NormalDist(0 ,  0.0000001);";
        return s;
    }
}

