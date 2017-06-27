/*
 * Decompiled with CFR 0_118.
 */
package mebn_rm.MEBN.MNode;

import java.util.List;
import mebn_rm.MEBN.MFrag.MFrag;
import mebn_rm.MEBN.MNode.MNode;
import mebn_rm.MEBN.MTheory.OVariable;

public class MIsANode
extends MNode {
    public MIsANode(MFrag f, OVariable o) {
        super(f, "IsA", null);
        this.ovs.add(o);
        f.arrayIsaContextNodes.add(this);
    }
 
    public String toString() {
        String s = this.name;
        OVariable ov = (OVariable)this.ovs.get(0);
        s = String.valueOf(s) + "(" + ov.name + ", " + ov.entityType + ")";
        return s;
    }
}

