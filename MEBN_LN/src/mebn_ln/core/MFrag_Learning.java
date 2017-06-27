/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  mebn_rm.MEBN.MFrag.MFrag
 *  mebn_rm.MEBN.MFrag.MFrag$LearningType
 *  mebn_rm.MEBN.MNode.MNode
 */
package mebn_ln.core;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import mebn_ln.bn_structure_learning.BNStructureLearning;
import mebn_ln.core.Learning_Common;
import mebn_ln.core.MNode_Learning;
import mebn_ln.core.MTheory_Learning;
import mebn_rm.MEBN.MFrag.MFrag;
import mebn_rm.MEBN.MNode.MNode;

public class MFrag_Learning
extends Learning_Common {
    MNode_Learning MNode_learning = new MNode_Learning();
    MFrag mFrag = null;

    public void run(MFrag f) {
        this.mFrag = f;
        this.getCandidateMGraphs(f, 5.0);
        System.out.println("******************* Begin MFrag learning with the " + f.name + " MFrag *******************");
        System.out.println(f.toString());
        f.initSelectedDataset(-1);
        this.run_operation(f);
        System.out.println("******************* End MFrag learning with the " + f.name + " MFrag *******************");
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void run_operation(MFrag f) {
        if (f.name.equalsIgnoreCase("HI_temperature_SII_temperature_HAI_energy")) {
            System.out.println("");
        }
        if (f.learningType == MFrag.LearningType.PARAMETER || f.learningType == MFrag.LearningType.BAYES) {
            for (MNode n : f.arrayResidentNodes) {
                this.MNode_learning.run(n);
            }
            return;
        }
        if (f.learningType != MFrag.LearningType.STRUCTURE_HYBRID_DISCRETIZED) return;
        
//        This is a structuring learning 
//        
//        try {
//            if (MTheory_Learning.structure_learning) {
//                BNStructureLearning.run(this.mFrag.cvsFile, f);
//            }
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
        for (MNode n : f.arrayResidentNodes) {
            this.MNode_learning.run(n);
        }
    }

    public void getCandidateMGraphs(MFrag mfrag, Double size) {
    }
}

