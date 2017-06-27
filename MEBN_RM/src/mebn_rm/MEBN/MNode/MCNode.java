/*
 * Decompiled with CFR 0_118.
 */
package mebn_rm.MEBN.MNode;

import java.util.List;
import mebn_rm.MEBN.MFrag.MFrag;
import mebn_rm.MEBN.MNode.MNode;
import mebn_rm.MEBN.MTheory.OVariable;

public class MCNode
extends MNode {
    public MCNode(MFrag f, String name, List<OVariable> ovs) {
        super(f, name, ovs, null);
        f.arrayResidentNodes.add(this);
    }

    public MCNode(MNode n) {
        super(n);
    }
}

