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
package mebn_rm.MEBN.MNode;
 
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList; 
import java.util.EmptyStackException; 
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import mebn_rm.MEBN.CLD.CLD;
import mebn_rm.MEBN.MFrag.MFrag;
import mebn_rm.MEBN.MNode.MCNode;
import mebn_rm.MEBN.MNode.MDNode;
import mebn_rm.MEBN.MTheory.OVariable;
import mebn_rm.RDB.RDB;
import mebn_rm.data.ConditionalDataSet;
import mebn_rm.util.StringUtil;
import util.Node;
import util.SortableValueMap;
import util.Tree;
import util.math.Sum_for_Log; 

public class MNode extends Tree implements Comparable<MNode> {
    public String name = "";
    public String pret = "";
    public SortableValueMap<CLD, Double> cldCANs = new SortableValueMap<CLD, Double>();
    public CLD bestCLD = null; 
    public MFrag mFrag = null;
    public String attribute = null;
    public String cvsFile = null;
    public List<OVariable> ovs = new ArrayList<OVariable>();
    public List<MNode> parentMNodes = new ArrayList<MNode>();
    public List<MNode> inputParentMNodes = new ArrayList<MNode>();
    public ConditionalDataSet CDs = null;
    static List<MNode> globalMNodes = new ArrayList<MNode>();
    private Integer count = 0;
    public SortableValueMap<String, ResidentNode> residentNodes = new SortableValueMap<String, ResidentNode>();
    public ResidentNode childResidentNode = null;

    public MNode(String n) {
        super(false);
        name = n;
    }

    public MNode(MNode n) {
        super(false);
        name = n.name;
        ovs = n.ovs;
        attribute = n.attribute;
    }

    public MNode(MFrag f, String n, MNode ... parenetmnodes) {
        super(false);
        name = n;
        mFrag = f;
        setParents(parenetmnodes);
    }

    public /* varargs */ MNode(MFrag f, String n, List<OVariable> o, MNode ... parenetmnodes) {
        super(false); 
        name = n; 
        mFrag = f;
        ovs = new ArrayList<OVariable>(o);
        setParents(parenetmnodes);
    }
 
    public String toString() {
        ArrayList<String> inclusions = new ArrayList<String>();
        inclusions.add("MNode");
        inclusions.add("Parents");
        inclusions.add("CLD");
        inclusions.add("Graph");
        return toString(inclusions);
    }

    public String toStringOVswithBracket() {
    	List<OVariable> ovs1 = ovs;
    	String s = "";
    	
    	if (ovs1.size() > 0) {
            s = String.valueOf(s) + "(";
            s += toStringOVs();
            s = String.valueOf(s) + ")";
        }
    	
    	return s;
    }
    
    public String toStringOVs() {
    	List<OVariable> ovs1 = ovs;
    	String s = "";
    	
    	if (ovs1.size() > 0) {
            for (OVariable o : ovs1) {
                if (ovs1.get(0) != o) {
                    s = String.valueOf(s) + ", ";
                }
                s = String.valueOf(s) + o.name;
            }
        }
    	
    	return s;
    }
    
    public String toString(List<String> inclusions) {
        String s = "";
        if (inclusions.contains("MNode")) {
            s = String.valueOf(s) + "[R: " + name;
            s += toStringOVswithBracket();
//            if (ovs.size() > 0) {
//                s = String.valueOf(s) + "(";
//                for (OVariable o : ovs) {
//                    if (ovs.get(0) != o) {
//                        s = String.valueOf(s) + ", ";
//                    }
//                    s = String.valueOf(s) + o.name;
//                }
//                s = String.valueOf(s) + ")";
//            }
            if (parentMNodes.size() > 0) {
                s = String.valueOf(s) + "\r\n";
                s = String.valueOf(s) + "\t\t\t";
                s = String.valueOf(s) + "[RP: ";
                for (MNode mn : parentMNodes) {
                    s = String.valueOf(s) + mn.name;
                    s += toStringOVswithBracket();
                    s += ", ";
                }
                s = s.substring(0, s.length() - 2);
                s = String.valueOf(s) + "]"; 
            }
            if (inputParentMNodes.size() > 0) {
                s = String.valueOf(s) + "\r\n";
                s = String.valueOf(s) + "\t\t\t";
                s = String.valueOf(s) + "[IP: ";
                for (MNode mn : inputParentMNodes) {
                    s = String.valueOf(s) + mn.name;
                    s += toStringOVswithBracket();
                    s += ", ";
                }
                s = s.substring(0, s.length() - 2);
                s = String.valueOf(s) + "]";
            }
            if (inclusions.contains("CLD")) {
                for (CLD l : cldCANs.keySet()) {
                    s = String.valueOf(s) + "\r\n";
                    s = String.valueOf(s) + "\t\t\t";
                    s = String.valueOf(s) + l.toString(inclusions);
                }
            } else {
                s = String.valueOf(s) + "]\r\n";
            }
            s = String.valueOf(s) + "\t\t";
            s = String.valueOf(s) + "]\r\n";
        }
        return s;
    }

    public /* varargs */ void setCLDs(CLD ... clds) {
        ArrayList<CLD> arrayCLDs = new ArrayList<CLD>();
        CLD[] arrcLD = clds;
        int n = arrcLD.length;
        int n2 = 0;
        while (n2 < n) {
            CLD l = arrcLD[n2];
            l.mNode = this;
            arrayCLDs.add(l);
            ++n2;
        }
        for (CLD l : cldCANs.keySet()) {
            arrayCLDs.add(l);
        }
        Integer i = 1;
        cldCANs.clear();
        for (CLD l2 : arrayCLDs) {
            l2.name = String.valueOf(name) + "_cld_" + i;
            cldCANs.put(l2, Math.log(1.0 / (double)arrayCLDs.size()));
            i = i + 1;
        }
    }

    public CLD getCLD(String s) {
        for (CLD l : cldCANs.keySet()) {
            if (s.equalsIgnoreCase(l.name)) {
                return l;
            }
            if (!s.isEmpty()) continue;
            return l;
        }
        return null;
    }

    public CLD getFirstCandidateCLD() {
        Iterator<CLD> iterator = cldCANs.keySet().iterator();
        if (iterator.hasNext()) {
            CLD c = iterator.next();
            return c;
        }
        return null;
    }

    public Double getAvglogCLDScore() {
        Double logSC = 0.0;
        for (CLD cld : cldCANs.keySet()) {
            logSC = logSC + cldCANs.get(cld);
        }
        return logSC / (double)cldCANs.size();
    }

    public ArrayList<Double> getlogCLDScores() {
        ArrayList<Double> logSCs = new ArrayList<Double>();
        for (CLD cld : cldCANs.keySet()) {
            Double logSC = cldCANs.get(cld);
            logSCs.add(logSC);
        }
        return logSCs;
    }

    public Double getlogCLDScore() {
        ArrayList<Double> logSCs = new ArrayList<Double>();
        for (CLD cld : cldCANs.keySet()) {
            Double logSC = cldCANs.get(cld);
            logSCs.add(logSC);
        }
        return new Sum_for_Log().sum(logSCs);
    }

    public void setAttributeName(String s) {
    	attribute = s;
    }
    
    public String getAttributeName() {
        String s = name; 
        String prefix = new StringUtil().createAbbreviation(mFrag.name);
        String right = s.replaceFirst(prefix+"_", "");
        
        if (attribute != null){
        	return attribute;
        }
        
        return right;
    }

    public /* varargs */ void setParents(MNode ... mnodes) {
        if (mnodes == null) {
            return;
        }
        MNode[] arrmNode = mnodes;
        int n = arrmNode.length;
        int n2 = 0;
        while (n2 < n) {
            MNode mnode = arrmNode[n2];
            if (this == mnode) {
                System.out.println("*** A node can't have a parent which is the node ***");
                throw new EmptyStackException();
            }
            if (getParentNode(mnode.name) == null) {
                parentMNodes.add(mnode);
            }
            ++n2;
        }
    }

    public MNode getParentNode(String s) {
        for (MNode mn : parentMNodes) {
            if (!mn.name.equalsIgnoreCase(s)) continue;
            return mn;
        }
        return null;
    }

    public /* varargs */ void setInputParents(MNode ... mnodes) {
        MNode[] arrmNode = mnodes;
        int n = arrmNode.length;
        int n2 = 0;
        while (n2 < n) {
            MNode mnode = arrmNode[n2];
            if (this == mnode) {
                System.out.println("*** A node can't have a parent which is the node ***");
                throw new EmptyStackException();
            }
            if (mnode == null) {
                System.out.println("*** the node is null ***");
                throw new EmptyStackException();
            }
            inputParentMNodes.add(mnode);
            ++n2;
        }
    }
    
    public boolean checkContainedNodeByName(MNode source, List<MNode> mNodes) {
    	for (MNode m : mNodes) {
    		if (source.name.equalsIgnoreCase(m.name))
    			return true;
    	}
    	
    	return false;
    }

    public boolean checkSameKeys(ResultSet rs, ArrayList<String> curKeys) {
        String keyVal;
        boolean bCheck = true;
        Map<String, String> importedColumns = RDB.This().getImportedColumn(name);
        for (String key2 : importedColumns.keySet()) {
            try {
                keyVal = rs.getString(String.valueOf(name) + "." + key2);
                if (curKeys.contains(keyVal)) continue;
                bCheck = false;
                break;
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (!bCheck) {
            curKeys.clear();
            for (String key2 : importedColumns.keySet()) {
                try {
                    keyVal = rs.getString(String.valueOf(name) + "." + key2);
                    curKeys.add(keyVal);
                    continue;
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return bCheck;
    }

    public boolean isContinuous() {
        if (this instanceof MCNode) {
            return true;
        }
        return false;
    }

    public boolean isDiscrete() {
        if (this instanceof MDNode) {
            return true;
        }
        return false;
    }

    public List<MNode> getAllParents() {
        List<MNode> dm = getDiscreteParents();
        List<MNode> cm = getContinuousParents();
        dm.addAll(cm);
        return dm;
    }

    public List<String> getAllParentNames() {
        List<MNode> dm = getDiscreteParents();
        List<MNode> cm = getContinuousParents();
        dm.addAll(cm);
        ArrayList<String> ret = new ArrayList<String>();
        for (MNode n : dm) {
            ret.add(n.name);
        }
        return ret;
    }

    public List<MNode> getDiscreteParents() {
        ArrayList<MNode> l = new ArrayList<MNode>();
        if (!globalMNodes.isEmpty()) {
            for (MNode p : globalMNodes) {
                if (checkContainedNodeByName(p, parentMNodes) && p.isDiscrete()) {
                	l.add(p);	
                }
                                
                if (checkContainedNodeByName(p, inputParentMNodes) && p.isDiscrete()) {
                	l.add(p);
                }
            }
        } else {
            for (MNode p : parentMNodes) {
                if (!p.isDiscrete()) continue;
                	l.add(p);
            }
            
            for (MNode p : inputParentMNodes) {
                if (!p.isDiscrete()) continue;
                	l.add(p);
            }
        }
        return l;
    }

    public List<MNode> getContinuousParents() {
        ArrayList<MNode> l = new ArrayList<MNode>();
        if (!globalMNodes.isEmpty()) {
            for (MNode p : globalMNodes) {
                if (checkContainedNodeByName(p, parentMNodes) && p.isContinuous()) {
                	l.add(p);
                }
                
                if (checkContainedNodeByName(p, inputParentMNodes) && p.isContinuous()) {
                	l.add(p);	
                }                
            }
        } else {
            for (MNode p2 : parentMNodes) {
                if (!p2.isContinuous()) continue;
                l.add(p2);
            }
            for (MNode p2 : inputParentMNodes) {
                if (!p2.isContinuous()) continue;
                l.add(p2);
            }
        }
        return l;
    }

    public int checkPredecessorData(List<MNode> arrayContextNodes) {
        int i = 0;
        for (MNode con : arrayContextNodes) {
            if (!con.name.equalsIgnoreCase("predecessor")) continue;
            ++i;
        }
        return i;
    }

    public void setDataSet(ConditionalDataSet d) {
        CDs = d;
    }
 
    public int compareTo(MNode o) {
        return 0;
    }

    public void resetCLDCANsPrior() {
        for (CLD e : cldCANs.keySet()) {
            cldCANs.put(e, Math.log(1.0 / (double)cldCANs.size()));
        }
    }

    public /* varargs */ void addParentResidentNode(String rn, String ovl, String ... sList) {
        ResidentNode resident = new ResidentNode(rn, ovl, sList);
        residentNodes.put(rn, resident);
    }

    public /* varargs */ void addChildResidentNode(String rn, String ovl, String ... sList) {
        childResidentNode = new ResidentNode(rn, ovl, sList);
        residentNodes.put(rn, childResidentNode);
    }

    public void initCLDModel() {
        String node = "!R";
        addNode(node);
        Set<String> keySet = residentNodes.keySet();
        String[] keys = keySet.toArray(new String[keySet.size()]);
        init(node, keys, 0);
    }

    private void init(String nodeParent, String[] keys, int cur) {
        if (cur >= keys.length) {
            return;
        }
        String key = keys[cur++];
        ResidentNode rn = residentNodes.get(key);
        for (String state : rn.states) {
            count = count + 1;
            String newStr = String.valueOf(state) + count;
            Node node = addNode(newStr, nodeParent);
            node.setData1(state);
            node.setData2(key);
            init(newStr, keys, cur);
        }
    }

    public void countingMarkovData(String data, int sizeWindows) {
        countingMarkovData_iter(data, sizeWindows);
    }

    private void addCount(Node node) {
        Integer i = (Integer)node.getData3();
        if (i == null) {
            node.setData3(1);
        } else {
            i = i + 1;
            node.setData3(i);
        }
    }

    private boolean countingMarkovData_iter(String data, int sizeWindows) {
        int index;
        ArrayList<String> parents = new ArrayList<String>();
        String dataCopy = data;
        int i = 0;
        while (i < sizeWindows) {
            index = dataCopy.indexOf("\t");
            if (index == -1) {
                return false;
            }
            String parentS = dataCopy.substring(0, index);
            dataCopy = dataCopy.substring(index + 1, dataCopy.length());
            parents.add(parentS);
            ++i;
        }
        Node node = find("!R", parents, 0);
        addCount(node);
        index = data.indexOf("\t");
        if (index == -1) {
            return false;
        }
        data = data.substring(index + 1, data.length());
        countingMarkovData_iter(data, sizeWindows);
        return true;
    }

    public CLD getBestCLD() {
        Iterator<CLD> iterator = cldCANs.keySet().iterator();
        if (iterator.hasNext()) {
            CLD c = iterator.next();
            return c;
        }
        return null;
    }

    public String getILD() {
        if (getBestCLD() == null) {
            return "";
        }
        String s = "defineNode(" + name + " , Desc); \n";
        s = String.valueOf(s) + getBestCLD().getILD();
        s = String.valueOf(s) + "}\n";
        globalMNodes.add(this);
        return s;
    }

    public List<String> getCategories() {
        return getBestCLD().getCategories();
    }

    public void updateCPT() {
        updateCPT2("!R", 0);
    }

    public void updateCPT2(String identifier, int depth) {
        ArrayList<String> children = ((Node)nodes.get(identifier)).getChildren();
        if (depth >= residentNodes.size() - 1) {
            Integer c;
            Integer t = 0;
            for (String child2 : children) {
                c = (Integer)((Node)nodes.get(child2)).getData3();
                t = t + c;
            }
            for (String child2 : children) {
                c = (Integer)((Node)nodes.get(child2)).getData3();
                Double p = Double.valueOf(c.doubleValue()) / Double.valueOf(t.doubleValue());
                ((Node)nodes.get(child2)).setData4(p);
            }
            DecimalFormat formatter = new DecimalFormat("#0.0000000000");
            Double total = 0.0;
            String lastChild = "";
            for (String child3 : children) {
                Double p = (Double)((Node)nodes.get(child3)).getData4();
                p = Double.valueOf(formatter.format(p));
                ((Node)nodes.get(child3)).setData4(p);
                total = total + p;
                lastChild = child3;
            }
            Double gap = 1.0 - total;
            Double p = (Double)((Node)nodes.get(lastChild)).getData4();
            p = p + gap;
            ((Node)nodes.get(lastChild)).setData4(p);
            return;
        }
        ++depth;
        for (String child : children) {
            updateCPT2(child, depth);
        }
    }

    public String generateContinuousCLDscript() {
        String sDefault = generateContinuousDefault();
        return generateContinuousCLDscript("!R", 0, sDefault);
    }

    public String generateContinuousDefault() {
        String s = "";
        if (childResidentNode == null) {
            return s;
        }
        s = String.valueOf(s) + " else [ NormalDist(0, 10) ]\n";
        return s;
    }

    public String generateContinuousCLDscript(String identifier, int depth, String sDefault) {
        String s = "";
        Node node = getNodes().get(identifier);
        ArrayList<String> children = node.getChildren();
        String tabs = "";
        String tabs2 = "";
        if (depth == residentNodes.size()) {
            String eq = (String)node.getData4();
            s = String.valueOf(s) + tabs2 + eq;
        } else if (depth != 0) {
            tabs = String.format("%0" + depth + "d", 0).replace("0", "    ");
            tabs2 = String.format("%0" + (depth + 1) + "d", 0).replace("0", "    ");
            ResidentNode rn = residentNodes.get(node.getData2());
            s = String.valueOf(tabs) + "if any " + rn.ordinaryVariableLabel + " have ( " + rn.name + " = " + node.getData1() + " ) [";
        }
        ++depth;
        int i = 0;
        for (String child : children) {
            s = String.valueOf(s) + "\n";
            String str = generateContinuousCLDscript(child, depth, sDefault);
            if (depth == residentNodes.size()) {
                if (childResidentNode != null && i < childResidentNode.states.size() - 1) {
                    str = String.valueOf(str) + ",";
                }
            } else if (i != 0) {
                s = String.valueOf(s) + tabs + "] else \n";
            }
            s = String.valueOf(s) + str;
            ++i;
        }
        if (children.size() > 0 && depth != residentNodes.size()) {
            s = String.valueOf(s) + tabs + "] \n";
            s = String.valueOf(s) + sDefault;
        }
        return s;
    }

    public String generateCLDscript() {
        String sDefault = generateDefault();
        return generateCLDscript("!R", 0, sDefault);
    }

    public String generateDefault() {
        String s = "";
        if (childResidentNode == null) {
            return s;
        }
        ArrayList<String> states = childResidentNode.states;
        s = String.valueOf(s) + " else [ \n";
        Integer total = states.size();
        Double d = 1.0 / total.doubleValue();
        int j = 0;
        for (String state : states) {
            s = j < states.size() - 1 ? String.valueOf(s) + state + " = " + d + ", \n" : String.valueOf(s) + state + " = " + d + "\n";
            ++j;
        }
        return String.valueOf(s) + "]";
    }

    public String generateCLDscript(String identifier, int depth, String sDefault) {
        String s = "";
        Node node = getNodes().get(identifier);
        ArrayList<String> children = node.getChildren();
        String tabs = "";
        String tabs2 = "";
        if (depth == residentNodes.size()) {
            tabs2 = String.format("%0" + (depth + 1) + "d", 0).replace("0", "    ");
            Double p = (Double)node.getData4();
            BigDecimal myNumber = new BigDecimal(p.toString());
            s = String.valueOf(s) + tabs2 + node.getData1() + " = " + myNumber;
        } else if (depth != 0) {
            tabs = String.format("%0" + depth + "d", 0).replace("0", "    ");
            tabs2 = String.format("%0" + (depth + 1) + "d", 0).replace("0", "    ");
            ResidentNode rn = residentNodes.get(node.getData2());
            s = String.valueOf(tabs) + "if any " + rn.ordinaryVariableLabel + " have ( " + rn.name + " = " + node.getData1() + " ) [";
        }
        ++depth;
        int i = 0;
        for (String child : children) {
            s = String.valueOf(s) + "\n";
            String str = generateCLDscript(child, depth, sDefault);
            if (depth == residentNodes.size()) {
                if (childResidentNode != null && i < childResidentNode.states.size() - 1) {
                    str = String.valueOf(str) + ",";
                }
            } else if (i != 0) {
                s = String.valueOf(s) + tabs + "] else \n";
            }
            s = String.valueOf(s) + str;
            ++i;
        }
        if (children.size() > 0 && depth != residentNodes.size()) {
            s = String.valueOf(s) + tabs + "] \n";
            s = String.valueOf(s) + sDefault;
        }
        return s;
    }

    public class ResidentNode {
        String name;
        String ordinaryVariableLabel;
        public ArrayList<String> states;

        public  ResidentNode(String rn, String ovl, String ... sList) {
            name = "";
            ordinaryVariableLabel = "";
            states = new ArrayList();
            name = rn;
            ordinaryVariableLabel = ovl;
            int i = 0;
            while (i < sList.length) {
                states.add(sList[i]);
                ++i;
            }
        }
    }

}

