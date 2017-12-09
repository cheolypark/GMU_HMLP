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
package mebn_rm.util;

import edu.cmu.tetrad.data.ColtDataSet;
import edu.cmu.tetrad.data.ContinuousVariable;
import edu.cmu.tetrad.data.DataSet;
import edu.cmu.tetrad.data.DiscreteVariable;
import edu.cmu.tetrad.graph.Edge;
import edu.cmu.tetrad.graph.EdgeListGraph;
import edu.cmu.tetrad.graph.Graph;
import edu.cmu.tetrad.graph.Node;
import edu.cmu.tetrad.util.TetradMatrix; 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map; 
import mebn_rm.MEBN.MNode.MNode;

public final class Tetrad_Util {
    static List<Node> getContinuousNodes(DataSet data) {
        ArrayList<Node> list = new ArrayList<Node>();
        for (Node n : data.getVariables()) {
            if (n instanceof ContinuousVariable) {
                list.add(n);
                continue;
            } 
        }
        return list;
    }

    static List<Node> getDiscreteNodes(DataSet data, String exceptNode) {
        ArrayList<Node> list = new ArrayList<Node>();
        for (Node n : data.getVariables()) {
            if (n instanceof ContinuousVariable || !(n instanceof DiscreteVariable) || n.getName().equalsIgnoreCase(exceptNode)) continue;
            list.add(n);
        }
        return list;
    }

    public static List<String> getIPC(DataSet data) {
        return Tetrad_Util.getIPC(null, data, "");
    }

    public static List<String> getIPC(MNode mNode, DataSet data, String exceptNode) {
        ArrayList<String> IPCs = new ArrayList<String>();
        List<Node> list = Tetrad_Util.getDiscreteNodes(data, exceptNode);
        Tetrad_Util.getIPC_op(0, mNode, list, IPCs, "");
        for (Node n : list) {
            System.out.println((Object)n);
        }
        return IPCs;
    }

    static void getIPC_op(int index, MNode mNode, List<Node> list, List<String> IPCs, String s) {
        if (index >= list.size()) {
            if (!s.isEmpty()) {
                s = s.substring(0, s.length() - 4);
            }
            IPCs.add(s);
            return;
        }
        DiscreteVariable n = (DiscreteVariable)list.get(index); 
        ++index;
        for (String cat : n.getCategories()) {
            String ns = String.valueOf(s) + n.getName() + " == " + cat + " && ";
            Tetrad_Util.getIPC_op(index, mNode, list, IPCs, ns);
        }
    }

    public static void getIPC_op_from_MNode(int index, MNode mNode, List<String> IPCs, String s) {
        if (index >= mNode.parentMNodes.size()) {
            if (!s.isEmpty()) {
                s = s.substring(0, s.length() - 4);
            }
            IPCs.add(s);
            return;
        }
        MNode pm = mNode.parentMNodes.get(index);
        ++index;
        for (String cat : pm.getCategories()) {
            String ns = String.valueOf(s) + pm.name + " == " + cat + " && ";
            Tetrad_Util.getIPC_op_from_MNode(index, mNode, IPCs, ns);
        }
    }

    static Map<String, String> getIPCmap(String ipc) {
        String[] list;
        HashMap<String, String> map = new HashMap<String, String>();
        String[] arrstring = list = ipc.split(" && ");
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String s = arrstring[n2];
            String[] ss = s.split(" == ");
            map.put(ss[0], ss[1]);
            ++n2;
        }
        return map;
    }

    public static boolean hasVariance(DataSet data) { 
        double pred = data.getDouble(0, 0);
        int i = 1;
        while (i < data.getNumRows()) {
            double d = data.getDouble(i, 0);
            if (pred != d) {
                return true;
            }
            pred = d;
            ++i;
        }
        return false;
    }

    static Boolean containValues(int row, DataSet srcData, Map<String, String> map) {
        for (String k : map.keySet()) {
            String v = map.get(k);
            DiscreteVariable n = (DiscreteVariable)srcData.getVariable(k);
            int index = n.getIndex(v);
            double srcIndex = srcData.getDouble(row, srcData.getColumn((Node)n));
            if (srcIndex == (double)index) continue;
            return false;
        }
        return true;
    }

    static Boolean containVariables(int column, DataSet srcData, Map<String, String> map) {
        for (String k : map.keySet()) {
            DiscreteVariable n = (DiscreteVariable)srcData.getVariable(k);
            double cr = srcData.getColumn((Node)n);
            if (cr != (double)column) continue;
            return false;
        }
        return true;
    }

    public static DataSet getDifferenceData(String curNode, DataSet srcData, Graph graph) {
        TetradMatrix data = new TetradMatrix(srcData.getDoubleData().rows(), 1);
        int curNodeColumn = srcData.getColumn(srcData.getVariable(curNode));
        int i = 0;
        while (i < srcData.getDoubleData().rows()) {
            Double curD = srcData.getDouble(i, curNodeColumn);
            int j = 0;
            while (j < srcData.getNumColumns()) {
                if (j != curNodeColumn) {
                    Double d = srcData.getDouble(i, j);
                    curD = curD - d;
                }
                ++j;
            }
            data.set(i, 0, curD.doubleValue());
            ++i;
        }
        ArrayList<Node> variables = new ArrayList<Node>();
        variables.add(srcData.getVariable(curNode));
        ColtDataSet _dataSet = ColtDataSet.makeData(variables, (TetradMatrix)data);
        Node child = _dataSet.getVariable(curNode);
        graph.addNode(child);
        return _dataSet;
    }

    public static DataSet getSubsetdataFromIPC(String ipc, DataSet srcData) {
        Map<String, String> map = Tetrad_Util.getIPCmap(ipc);
        ArrayList<Integer> rowList = new ArrayList<Integer>();
        int i = 0;
        while (i < srcData.getDoubleData().rows()) {
            if (Tetrad_Util.containValues(i, srcData, map).booleanValue()) {
                rowList.add(i);
            }
            ++i;
        }
        int[] rows = new int[rowList.size()];
        int i2 = 0;
        while (i2 < rows.length) {
            rows[i2] = (Integer)rowList.get(i2);
            ++i2;
        }
        ArrayList<Node> variables = new ArrayList<Node>();
        ArrayList<Integer> columnList = new ArrayList<Integer>();
        int j = 0;
        while (j < srcData.getDoubleData().columns()) {
            if (Tetrad_Util.containVariables(j, srcData, map).booleanValue()) {
                columnList.add(j);
                variables.add((Node)srcData.getVariables().get(j));
            }
            ++j;
        }
        int[] columns = new int[columnList.size()];
        int j2 = 0;
        while (j2 < columns.length) {
            columns[j2] = (Integer)columnList.get(j2);
            ++j2;
        }
        TetradMatrix _data = srcData.getDoubleData().getSelection(rows, columns).copy();
        ColtDataSet _dataSet = ColtDataSet.makeData(variables, (TetradMatrix)_data);
        return _dataSet;
    }

    static Graph getContinuousGraph(Graph g) {
        EdgeListGraph conGraph = new EdgeListGraph(g);
        for (Edge e : conGraph.getEdges()) {
            Node n1 = e.getNode1();
            Node n2 = e.getNode2();
            if (!(n1 instanceof DiscreteVariable) && !(n2 instanceof DiscreteVariable)) continue;
            conGraph.removeEdge(e);
        }
        for (Node n : conGraph.getNodes()) {
            if (n instanceof ContinuousVariable || !(n instanceof DiscreteVariable)) continue;
            conGraph.removeNode(n);
        }
        return conGraph;
    }

    static Graph getIandParentNodes(Node my, Graph trueGraph) {
        EdgeListGraph conGraph = new EdgeListGraph();
        conGraph.addNode(my);
        for (Edge e : trueGraph.getEdges()) {
            Node n1 = e.getNode1();
            Node n2 = e.getNode2();
            if (n2 != my) continue;
            conGraph.addNode(n1);
            conGraph.addDirectedEdge(n1, my);
        }
        return conGraph;
    }
}

