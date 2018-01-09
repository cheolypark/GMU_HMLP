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

/**
 * Deterministic_Conditional_Gaussian is the class for the special type of CLD.
 * The Deterministic-Conditional Gaussian distribution is a Conditional 
 * Gaussian distribution whose regression intercept is zero, regression 
 * coefficient is one, and variance is a very small value.  
 * <p>
 * 
 * @author      Cheol Young Park
 * @version     0.0.1
 * @since       1.5
 */

public class Deterministic_Conditional_Gaussian extends LPD_Continuous {
    public Deterministic_Conditional_Gaussian() {
        super("", "Deterministic_Conditional_Gaussian");
        parameterSize = 1;
        isSampling = false;
    }

    public Deterministic_Conditional_Gaussian(String name) {
        super(name, "Deterministic_Conditional_Gaussian");
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

