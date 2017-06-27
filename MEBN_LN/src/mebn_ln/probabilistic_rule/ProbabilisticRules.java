/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  mebn_rm.MEBN.MFrag.MFrag
 *  mebn_rm.MEBN.MNode.MNode
 *  mebn_rm.MEBN.MTheory.MTheory
 *  mebn_rm.RDB.RDB
 */
package mebn_ln.probabilistic_rule;

import java.util.ArrayList;
import mebn_rm.MEBN.MFrag.MFrag;
import mebn_rm.MEBN.MNode.MNode;
import mebn_rm.MEBN.MTheory.MTheory;
import mebn_rm.RDB.RDB;

public class ProbabilisticRules {
    public MTheory mTheory = null;

    public void run(MTheory m) {
        this.mTheory = m;
    }

    public /* varargs */ void setParents(String child, String ... parents) {
        String mFrag = child.substring(0, child.indexOf("."));
        MFrag f = this.mTheory.getMFrag(mFrag);
        ArrayList dd = RDB.This().getEntityTables();
        MNode childMNode = this.mTheory.getMNode(child);
        String[] arrstring = parents;
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            MNode parMNode;
            String par = arrstring[n2];
            int index = par.indexOf(".");
            if (index >= 0) {
                parMNode = this.mTheory.getMNode(par);
                childMNode.setInputParents(new MNode[]{parMNode});
            } else {
                parMNode = f.getMNode(par);
                childMNode.setParents(new MNode[]{parMNode});
            }
            ++n2;
        }
    }
}

