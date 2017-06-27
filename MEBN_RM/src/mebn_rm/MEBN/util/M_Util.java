/*
 * Decompiled with CFR 0_118.
 */
package mebn_rm.MEBN.util;

import java.util.ArrayList;
import java.util.List;
import mebn_rm.MEBN.MFrag.MFrag;
import mebn_rm.MEBN.MNode.MNode;

public class M_Util {
    public static final List<String> getMNodeNames(MFrag f) {
        ArrayList<String> arrayString = new ArrayList<String>();
        List<MNode> list = f.getMNodes();
        for (MNode s : list) {
            arrayString.add(s.toString());
        }
        return arrayString;
    }

    public static final List<String> getAllMNodeNames(MFrag f) {
        ArrayList<String> arrayString = new ArrayList<String>();
        List<MNode> list = f.getAllNodes();
        for (MNode s : list) {
            arrayString.add(s.toString());
        }
        return arrayString;
    }

    public static final List<String> getInputPrevNodes(MFrag f) {
        ArrayList<String> arrayString = new ArrayList<String>();
        List<MNode> list = f.getInputPrevNodes();
        for (MNode s : list) {
            arrayString.add(s.toString());
        }
        return arrayString;
    }
}

