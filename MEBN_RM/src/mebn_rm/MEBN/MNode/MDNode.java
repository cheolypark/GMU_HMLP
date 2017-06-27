/*
 * Decompiled with CFR 0_118.
 */
package mebn_rm.MEBN.MNode;

import java.util.List;
import mebn_rm.MEBN.MFrag.MFrag;
import mebn_rm.MEBN.MNode.MNode;
import mebn_rm.MEBN.MTheory.OVariable;

public class MDNode
extends MNode {
    public MDNode(MFrag f, String name, List<OVariable> ovs) {
        super(f, name, ovs, null);
        f.arrayResidentNodes.add(this);
    }

    public MDNode(MNode n) {
        super(n);
    }
}

