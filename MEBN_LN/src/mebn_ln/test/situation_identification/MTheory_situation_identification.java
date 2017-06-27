/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  mebn_rm.MEBN.CLD.CLD
 *  mebn_rm.MEBN.CLD.Categorical
 *  mebn_rm.MEBN.MFrag.MFrag
 *  mebn_rm.MEBN.MNode.MNode
 *  mebn_rm.MEBN.MTheory.MTheory
 */
package mebn_ln.test.situation_identification;

import mebn_rm.MEBN.CLD.CLD;
import mebn_rm.MEBN.CLD.Categorical;
import mebn_rm.MEBN.MFrag.MFrag;
import mebn_rm.MEBN.MNode.MNode;
import mebn_rm.MEBN.MTheory.MTheory;

public class MTheory_situation_identification {
    public static final MTheory get(MTheory m) {
        MTheory_situation_identification.object_MFrag(m);
        return m;
    }

    public static final void object_MFrag(MTheory m) {
        MNode vehicletype = m.getMFrag("vehicletype").getMNode("vehicletype");
        MNode speed = m.getMFrag("speed").getMNode("speed");
        MFrag f_ROO = new MFrag(m, "speed_vehicletype", "speed_vehicletype.sql");
        speed.setInputParents(new MNode[]{vehicletype});
        f_ROO.setMNodes(new MNode[]{speed});
        f_ROO.resetContextNodes();
    }

    public static final MFrag situation_MFrag(MTheory m) {
        MFrag f_Situation = new MFrag(m, "RegionSituation", "location");
        MNode mn_danger_level = new MNode("danger_level");
        mn_danger_level.setCLDs(new CLD[]{new Categorical()});
        MNode mn_vehicletype = m.getMFrag("Vehicle").getMNode("vehicletype");
        mn_danger_level.setInputParents(new MNode[]{mn_vehicletype});
        f_Situation.setMNodes(new MNode[]{mn_danger_level});
        return f_Situation;
    }
}

