/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  mebn_rm.MEBN.CLD.CLD
 *  mebn_rm.MEBN.MNode.MNode
 *  mebn_rm.data.ConditionalDataSet
 *  mebn_rm.util.SortableValueMap
 */
package mebn_ln.core;

import java.io.PrintStream;
import java.util.Set;
import mebn_ln.core.Learning_Common;
import mebn_rm.MEBN.CLD.CLD;
import mebn_rm.MEBN.MNode.MNode;
import mebn_rm.data.ConditionalDataSet;
import util.SortableValueMap; 

public class MNode_Learning
extends Learning_Common {
    public void run(MNode mn) {
        this.getCandidateMNodes(mn);
        System.out.println("******************* Begin MNode learning with the " + mn.name + " MNode *******************");
        System.out.println(mn.toString());
        this.run_operation(mn);
        System.out.println("******************* End MNode learning with the " + mn.name + " MNode *******************");
    }

    public void run_operation(MNode mn) {
        SortableValueMap<CLD, Double> cldCANs = mn.cldCANs;
        ConditionalDataSet CD = mn.CDs;
        for (CLD c2 : cldCANs.keySet()) {
            Double samplingSize = 100.0;
            if (this.isMC_Approach()) continue;
            c2.calculateBestPara(CD, null);
        }
        for (CLD l : cldCANs.keySet()) {
            Double cldSc = 0.0;
            if (this.isMC_Approach()) {
                cldSc = l.getAvgLogParaScore();
            } else if (this.isMC_Approach()) {
                cldSc = (Double)cldCANs.get((Object)l) + l.getlogParaScore();
            } else if (this.isML_Approach()) {
                cldSc = 0.0;
            }
            cldCANs.put(l, cldSc);
        }
        for (CLD c2 : cldCANs.keySet()) {
            System.out.println("Prior logP(L of " + c2.name + ":" + c2.lpd_type + "):  logCLDSC: " + cldCANs.get((Object)c2));
        }
    }

    public void getCandidateMNodes(MNode mn) {
    }
}

