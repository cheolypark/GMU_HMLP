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
package mebn_ln.core.CLD;

import edu.cmu.tetrad.graph.EdgeListGraph;
import edu.cmu.tetrad.graph.Graph; 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map; 
import java.util.concurrent.TimeUnit;
import mebn_rm.MEBN.CLD.CLD;
import mebn_rm.MEBN.CLD.Categorical;
import mebn_rm.MEBN.CLD.LPD_Discrete;
import mebn_rm.MEBN.MNode.MNode;
import mebn_rm.data.ConditionalDataSet;
import mebn_rm.util.ExcelCSV;
import mebn_rm.util.Tetrad_Util;
import mebn_rm.util.TextFile;

public class Inverse_Cardinality_Average
extends LPD_Discrete {
    Map<String, String> ipcProbs = new HashMap<String, String>();

    public Inverse_Cardinality_Average() {
        super("", "Inverse_Cardinality_Average");
        parameterSize = 2;
    }

    public Inverse_Cardinality_Average(String name) {
        super(name, "Inverse_Cardinality_Average");
        parameterSize = 2;
    }

    public List<String> getCategories() {
        ArrayList<String> re = new ArrayList<String>();
        re.add("High");
        re.add("Low");
        return re;
    }

    public String getILD() {
        List<MNode> discreteParents = mNode.getDiscreteParents();
        String s = "{ defineState(Discrete, ";
        for (String state : mNode.getCategories()) {
            s = String.valueOf(s) + state + ", ";
        }
        s = s.substring(0, s.length() - 2);
        s = String.valueOf(s) + " ); \n";
        s = String.valueOf(s) + "p( " + mNode.name;
        if (discreteParents.size() > 0) {
            s = String.valueOf(s) + " | ";
            for (MNode p : discreteParents) {
                s = String.valueOf(s) + p.name + " , ";
            }
            s = s.substring(0, s.length() - 2);
        }
        s = String.valueOf(s) + " ) = \n";
        boolean b = true;
        for (String condition : ipcProbs.keySet()) {
            String probs = ipcProbs.get(condition);
            if (!condition.isEmpty()) {
                if (b) {
                    s = String.valueOf(s) + "if( " + condition + " ) {";
                    b = false;
                } else {
                    s = String.valueOf(s) + "\nelse if( " + condition + " ) {";
                }
            } else {
                s = String.valueOf(s) + "{ ";
            }
            s = String.valueOf(s) + probs;
            s = String.valueOf(s) + " }";
        }
        s = String.valueOf(s) + "\n";
        return s;
    }

    public Double calculateBestPara(ConditionalDataSet CD, ConditionalDataSet prior_CD) {
        EdgeListGraph hybridGraph = new EdgeListGraph();
        IPCs = initIPCs((Graph)hybridGraph);
        if (IPCs == null) {
            return 0.0;
        }
        for (String ipc : IPCs) {
            String probs = "";
            probs = ipc.contains("Mission == Attack && TargetType == ThreateningGroundTarget") ? "High : 0.00033535; Low : 0.99966465;" : "High : 0.0; Low : 1.0;";
            ipcProbs.put(ipc, probs);
        }
        return 0.0;
    }

    public static void main(String[] args) {
        int numTarget = 0;
        int nextTime = 0;
        String csvFile = "examples\\learning\\core_heraldSSBN_DangerLevel_IPC_Num";
        ExcelCSV.createCSV((String)csvFile);
        String[] stockArr = new String[]{"NumTarget", "Time", "MEM", "Size"};
        ExcelCSV.writeCSV((String)csvFile, (String[])stockArr);
        int t = 1;
        while (t < 2) {
            nextTime = t;
            int k = 1;
            while (k < 11) {
                numTarget = k;
                System.gc();
                Runtime rt = Runtime.getRuntime();
                Inverse_Cardinality_Average icv = new Inverse_Cardinality_Average();
                icv.mNode = new MNode("DangerLevel_ci1");
                icv.mNode.setCLDs(new CLD[]{new Categorical()});
                icv.mNode.getBestCLD().addCategories("High");
                icv.mNode.getBestCLD().addCategories("Low");
                String sensor = "s1";
                String ci = "ci1";
                int i = 1;
                while (i < numTarget + 1) {
                    String target = "g" + i;
                    String time = "t" + nextTime;
                    String time2 = "t" + nextTime;
                    String ovs1 = "_" + target + "_" + sensor + "_" + time;
                    String ovs2 = "_" + target + "_" + sensor + "_" + time2;
                    String ovs3 = "_" + target + "_" + time;
                    String ovs4 = "_" + target + "_" + time2;
                    String ovs5 = "_" + target;
                    String ovs6 = "_" + ci;
                    String parent = "TargetType" + ovs5;
                    MNode pa = new MNode(parent);
                    pa.setCLDs(new CLD[]{new Categorical()});
                    pa.getBestCLD().addCategories("Others");
                    pa.getBestCLD().addCategories("ThreateningAirTarget");
                    pa.getBestCLD().addCategories("ThreateningGroundTarget");
                    icv.mNode.setParents(new MNode[]{pa});
                    parent = "Mission" + ovs4;
                    pa = new MNode(parent);
                    pa.setCLDs(new CLD[]{new Categorical()});
                    pa.getBestCLD().addCategories("Attack");
                    pa.getBestCLD().addCategories("Others");
                    icv.mNode.setParents(new MNode[]{pa});
                    ++i;
                }
                long timeTotal = System.nanoTime();
                ArrayList<String> IPCs = new ArrayList();
                Tetrad_Util.getIPC_op_from_MNode((int)0, (MNode)icv.mNode, IPCs, (String)"");
                System.out.println(IPCs);
                for (String ipc : IPCs) {
                    String probs = "";
                    probs = ipc.contains("TargetType_g1 == ThreateningAirTarget && Mission_g1_t1 == Attack") ? "High : 0.00033535; Low : 0.99966465;" : "High : 0.0; Low : 1.0;";
                    icv.ipcProbs.put(ipc, probs);
                }
                String ssbn = "defineNode(DangerLevel_ci1 , Desc); \n";
                ssbn = String.valueOf(ssbn) + icv.getILD();
                ssbn = String.valueOf(ssbn) + "}";
                new TextFile().save((String)("examples\\learning\\core_heraldSSBN_DangerLevel_" + numTarget + "_" + nextTime + ".txt"), (String)ssbn);
                Long t1 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - timeTotal);
                Long usedMB = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
                String[] stockArr2 = new String[]{String.valueOf(numTarget), t1.toString(), usedMB.toString(), String.valueOf(IPCs.size())};
                ExcelCSV.writeCSV((String)csvFile, (String[])stockArr2);
                System.out.println(ssbn);
                ++k;
            }
            ++t;
        }
    }
}

