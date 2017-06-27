/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  mebn_rm.MEBN.MFrag.MFrag
 *  mebn_rm.MEBN.MNode.MNode
 *  mebn_rm.MEBN.MTheory.MTheory
 *  mebn_rm.util.Resource
 *  mebn_rm.util.SortableValueMap
 *  mebn_rm.util.TextFile
 *  network.Network
 *  network.Node
 *  network.operator.OrderingNetwork
 *  util.SortableValueMap
 */
package mebn_ln.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import mebn_rm.MEBN.MFrag.MFrag;
import mebn_rm.MEBN.MNode.MNode;
import mebn_rm.MEBN.MTheory.MTheory;
import mebn_rm.util.Resource; 
import mebn_rm.util.TextFile;
import network.Network;
import network.Node;
import network.operator.OrderingNetwork;
import util.SortableValueMap;

public class ConvertFromMTheoryToSBN {
    public static String save(MTheory m) {
        String strName = String.valueOf(m.name) + "_ssbn.txt";
        String strFile = String.valueOf(Resource.getlearningOutputPath((String)m.name)) + strName;
        String ssbn_next = "";
        Network net = new Network("myNet");
        for (MFrag f : m.mfrags.keySet()) {
            for (MNode mn : f.getAllNodes()) {
                for (MNode pa : mn.parentMNodes) {
                    net.add(pa.name, mn.name);
                }
            }
        }
        net.print();
        OrderingNetwork on = new OrderingNetwork();
        SortableValueMap<Integer, ArrayList<Node>> nodesInLevels = on.run(net);
        for (Integer i : nodesInLevels.keySet()) {
            ArrayList<Node> listNode = (ArrayList)nodesInLevels.get((Object)i);
            for (Node n : listNode) {
                for (MFrag f2 : m.mfrags.keySet()) {
                    for (MNode mn : f2.getAllNodes()) {
                        if (!mn.name.equalsIgnoreCase(n.name)) continue;
                        String cps = mn.getCPS();
                        ssbn_next = String.valueOf(ssbn_next) + cps;
                    }
                }
            }
        }
        TextFile.save((String)strFile, (String)ssbn_next);
        return strFile;
    }
}

