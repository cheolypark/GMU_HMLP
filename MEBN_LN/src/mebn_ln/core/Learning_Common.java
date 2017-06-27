/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  mebn_rm.MEBN.parameter.Parameter
 *  mebn_rm.data.ConditionalDataSet
 *  mebn_rm.util.TempMathFunctions
 *  mebn_rm.util.math.Sum_for_Log
 */
package mebn_ln.core;

import mebn_rm.MEBN.parameter.Parameter;
import mebn_rm.data.ConditionalDataSet;
import util.TempMathFunctions;
import util.math.Sum_for_Log; 

public class Learning_Common extends TempMathFunctions {
    public Sum_for_Log logsum = new Sum_for_Log();
    public String typeLearning = "ML_Approach";

    public boolean isMC_Approach() {
        if (this.typeLearning.equalsIgnoreCase("MC_Approach")) {
            return true;
        }
        return false;
    }

    public boolean isML_Approach() {
        if (this.typeLearning.equalsIgnoreCase("ML_Approach")) {
            return true;
        }
        return false;
    }

    public Double calculate(ConditionalDataSet CD, Parameter para) {
        Double logTotal = 0.0;
        return logTotal;
    }
}

