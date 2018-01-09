/*
 * HML Core
 * Copyright (C) 2017 Cheol Young Park
 * 
 * This file is part of HML Core.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mebn_ln.converter;

import java.util.ArrayList; 
import mebn_rm.MEBN.MFrag.MFrag;
import mebn_rm.MEBN.MNode.MNode;
import mebn_rm.MEBN.MTheory.MTheory;
import mebn_rm.util.Resource; 
import mebn_rm.util.TextFile;
import network.Network;
import network.Node;
import network.operator.OrderingNetwork;
import util.SortableValueMap;

/**
 * ConvertFromMTheoryToSBN is the class to convert from an MTheory to a BN. 
 * <p>
 * 
 * @author      Cheol Young Park
 * @version     0.0.1
 * @since       1.5
 */

public class ConvertFromMTheoryToSBN {
    public String save(MTheory m, String arg) {
        String strName = String.valueOf(m.name) + arg + "_ssbn.txt";
        String strFile = String.valueOf(Resource.getlearningOutputPath((String)m.name)) + strName;
        String ssbn_next = "";
        Network net = new Network("myNet");
        
        for (MFrag f : m.mfrags.keySet()) {
            for (MNode mn : f.getAllNodes()) {
                for (MNode pa : mn.parentMNodes) {
                    net.add(pa.name, mn.name);
                }
                
                for (MNode pa : mn.inputParentMNodes) {
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
                        String cps = mn.getILD();
                        ssbn_next = String.valueOf(ssbn_next) + cps;
                    }
                }
            }
        }
        new TextFile().save((String)strFile, (String)ssbn_next);
        return strFile;
    }
}

