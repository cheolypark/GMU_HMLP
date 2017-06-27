/*
 * Decompiled with CFR 0_118.
 */
package mebn_rm.MEBN.MNode;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EmptyStackException;
import java.util.HashMap;
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
import util.Node;
import util.SortableValueMap;
import util.Tree;
import util.math.Sum_for_Log; 

public class MNode extends Tree implements Comparable<MNode> {
    public String name = "";
    public String pret = "";
    public SortableValueMap<CLD, Double> cldCANs = new SortableValueMap();
    public CLD bestCLD = null;
    public Boolean isContinuous = false;
    public MFrag mFrag = null;
    public List<OVariable> ovs = new ArrayList<OVariable>();
    public List<MNode> parentMNodes = new ArrayList<MNode>();
    public List<MNode> inputParentMNodes = new ArrayList<MNode>();
    public ConditionalDataSet CDs = null;
    static List<MNode> globalMNodes = new ArrayList<MNode>();
    private Integer count = 0;
    public SortableValueMap<String, ResidentNode> residentNodes = new SortableValueMap();
    public ResidentNode childResidentNode = null;

    public MNode(String n) {
        super(false);
        this.name = n;
    }

    public MNode(MNode n) {
        super(false);
        this.name = n.name;
        this.ovs = n.ovs;
    }

    public /* varargs */ MNode(MFrag f, String n, MNode ... parenetmnodes) {
        super(false);
        this.name = n;
        this.mFrag = f;
        this.setParents(parenetmnodes);
    }

    public /* varargs */ MNode(MFrag f, String n, List<OVariable> o, MNode ... parenetmnodes) {
        super(false);
        this.name = n;
        this.mFrag = f;
        this.ovs = new ArrayList<OVariable>(o);
        this.setParents(parenetmnodes);
    }

    public /* varargs */ MNode(MFrag f, String n, List<OVariable> o, boolean bContinuous, MNode ... parenetmnodes) {
        super(false);
        this.name = n;
        this.mFrag = f;
        this.ovs = new ArrayList<OVariable>(o);
        this.isContinuous = bContinuous;
        this.setParents(parenetmnodes);
    }

    public String toString() {
        ArrayList<String> inclusions = new ArrayList<String>();
        inclusions.add("MNode");
        inclusions.add("Parents");
        inclusions.add("CLD");
        inclusions.add("Graph");
        return this.toString(inclusions);
    }

    public String toString(List<String> inclusions) {
        String s = "";
        if (inclusions.contains("MNode")) {
            s = String.valueOf(s) + "[R: " + this.name;
            if (this.ovs.size() > 0) {
                s = String.valueOf(s) + "(";
                for (OVariable o : this.ovs) {
                    if (this.ovs.get(0) != o) {
                        s = String.valueOf(s) + ", ";
                    }
                    s = String.valueOf(s) + o.name;
                }
                s = String.valueOf(s) + ")";
            }
            if (this.parentMNodes.size() > 0) {
                s = String.valueOf(s) + "\r\n";
                s = String.valueOf(s) + "\t\t\t";
                s = String.valueOf(s) + "[RP: ";
                for (MNode mn : this.parentMNodes) {
                    s = String.valueOf(s) + mn.name + ", ";
                }
                s = s.substring(0, s.length() - 2);
                s = String.valueOf(s) + "]";
                s = String.valueOf(s) + "\r\n";
            }
            if (this.inputParentMNodes.size() > 0) {
                s = String.valueOf(s) + "\r\n";
                s = String.valueOf(s) + "\t\t\t";
                s = String.valueOf(s) + "[IP: ";
                for (MNode mn : this.inputParentMNodes) {
                    s = String.valueOf(s) + mn.name + ", ";
                }
                s = s.substring(0, s.length() - 2);
                s = String.valueOf(s) + "]";
            }
            if (inclusions.contains("CLD")) {
                for (CLD l : this.cldCANs.keySet()) {
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
        for (CLD l : this.cldCANs.keySet()) {
            arrayCLDs.add(l);
        }
        Integer i = 1;
        this.cldCANs.clear();
        for (CLD l2 : arrayCLDs) {
            l2.name = String.valueOf(this.name) + "_cld_" + i;
            this.cldCANs.put(l2, Math.log(1.0 / (double)arrayCLDs.size()));
            i = i + 1;
        }
    }

    public CLD getCLD(String s) {
        for (CLD l : this.cldCANs.keySet()) {
            if (s.equalsIgnoreCase(l.name)) {
                return l;
            }
            if (!s.isEmpty()) continue;
            return l;
        }
        return null;
    }

    public CLD getFirstCandidateCLD() {
        Iterator<CLD> iterator = this.cldCANs.keySet().iterator();
        if (iterator.hasNext()) {
            CLD c = iterator.next();
            return c;
        }
        return null;
    }

    public Double getAvglogCLDScore() {
        Double logSC = 0.0;
        for (CLD cld : this.cldCANs.keySet()) {
            logSC = logSC + this.cldCANs.get(cld);
        }
        return logSC / (double)this.cldCANs.size();
    }

    public ArrayList<Double> getlogCLDScores() {
        ArrayList<Double> logSCs = new ArrayList<Double>();
        for (CLD cld : this.cldCANs.keySet()) {
            Double logSC = this.cldCANs.get(cld);
            logSCs.add(logSC);
        }
        return logSCs;
    }

    public Double getlogCLDScore() {
        ArrayList<Double> logSCs = new ArrayList<Double>();
        for (CLD cld : this.cldCANs.keySet()) {
            Double logSC = this.cldCANs.get(cld);
            logSCs.add(logSC);
        }
        return new Sum_for_Log().sum(logSCs);
    }

    public String getAttributeName() {
        String s = this.name;
        String right = s.replaceFirst(this.mFrag.name+"_", "");
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
            if (this.getParentNode(mnode.name) == null) {
                this.parentMNodes.add(mnode);
            }
            ++n2;
        }
    }

    public MNode getParentNode(String s) {
        for (MNode mn : this.parentMNodes) {
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
            this.inputParentMNodes.add(mnode);
            ++n2;
        }
    }

    public boolean checkSameKeys(ResultSet rs, ArrayList<String> curKeys) {
        String keyVal;
        boolean bCheck = true;
        Map<String, String> importedColumns = RDB.This().getImportedColumn(this.name);
        for (String key2 : importedColumns.keySet()) {
            try {
                keyVal = rs.getString(String.valueOf(this.name) + "." + key2);
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
                    keyVal = rs.getString(String.valueOf(this.name) + "." + key2);
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
        List<MNode> dm = this.getDiscreteParents();
        List<MNode> cm = this.getContinuousParents();
        dm.addAll(cm);
        return dm;
    }

    public List<String> getAllParentNames() {
        List<MNode> dm = this.getDiscreteParents();
        List<MNode> cm = this.getContinuousParents();
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
                if (!this.parentMNodes.contains(p) || !p.isDiscrete()) continue;
                l.add(p);
            }
        } else {
            for (MNode p : this.parentMNodes) {
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
                if (!this.parentMNodes.contains(p) || !p.isContinuous()) continue;
                l.add(p);
            }
        } else {
            for (MNode p2 : this.parentMNodes) {
                if (!p2.isContinuous()) continue;
                l.add(p2);
            }
            for (MNode p2 : this.inputParentMNodes) {
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
        this.CDs = d;
    }
 
    public int compareTo(MNode o) {
        return 0;
    }

    public void resetCLDCANsPrior() {
        for (CLD e : this.cldCANs.keySet()) {
            this.cldCANs.put(e, Math.log(1.0 / (double)this.cldCANs.size()));
        }
    }

    public /* varargs */ void addParentResidentNode(String rn, String ovl, String ... sList) {
        ResidentNode resident = new ResidentNode(rn, ovl, sList);
        this.residentNodes.put(rn, resident);
    }

    public /* varargs */ void addChildResidentNode(String rn, String ovl, String ... sList) {
        this.childResidentNode = new ResidentNode(rn, ovl, sList);
        this.residentNodes.put(rn, this.childResidentNode);
    }

    public void initCLDModel() {
        String node = "!R";
        this.addNode(node);
        Set<String> keySet = this.residentNodes.keySet();
        String[] keys = keySet.toArray(new String[keySet.size()]);
        this.init(node, keys, 0);
    }

    private void init(String nodeParent, String[] keys, int cur) {
        if (cur >= keys.length) {
            return;
        }
        String key = keys[cur++];
        ResidentNode rn = this.residentNodes.get(key);
        for (String state : rn.states) {
            this.count = this.count + 1;
            String newStr = String.valueOf(state) + this.count;
            Node node = this.addNode(newStr, nodeParent);
            node.setData1(state);
            node.setData2(key);
            this.init(newStr, keys, cur);
        }
    }

    public void countingMarkovData(String data, int sizeWindows) {
        this.countingMarkovData_iter(data, sizeWindows);
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
        Node node = this.find("!R", parents, 0);
        this.addCount(node);
        index = data.indexOf("\t");
        if (index == -1) {
            return false;
        }
        data = data.substring(index + 1, data.length());
        this.countingMarkovData_iter(data, sizeWindows);
        return true;
    }

    public CLD getBestCLD() {
        Iterator<CLD> iterator = this.cldCANs.keySet().iterator();
        if (iterator.hasNext()) {
            CLD c = iterator.next();
            return c;
        }
        return null;
    }

    public String getCPS() {
        if (this.getBestCLD() == null) {
            return "";
        }
        String s = "defineNode(" + this.name + " , Desc); \n";
        s = String.valueOf(s) + this.getBestCLD().getCPS();
        s = String.valueOf(s) + "}\n";
        globalMNodes.add(this);
        return s;
    }

    public List<String> getCategories() {
        return this.getBestCLD().getCategories();
    }

    public void updateCPT() {
        this.updateCPT2("!R", 0);
    }

    public void updateCPT2(String identifier, int depth) {
        ArrayList<String> children = ((Node)this.nodes.get(identifier)).getChildren();
        if (depth >= this.residentNodes.size() - 1) {
            Integer c;
            Integer t = 0;
            for (String child2 : children) {
                c = (Integer)((Node)this.nodes.get(child2)).getData3();
                t = t + c;
            }
            for (String child2 : children) {
                c = (Integer)((Node)this.nodes.get(child2)).getData3();
                Double p = Double.valueOf(c.doubleValue()) / Double.valueOf(t.doubleValue());
                ((Node)this.nodes.get(child2)).setData4(p);
            }
            DecimalFormat formatter = new DecimalFormat("#0.0000000000");
            Double total = 0.0;
            String lastChild = "";
            for (String child3 : children) {
                Double p = (Double)((Node)this.nodes.get(child3)).getData4();
                p = Double.valueOf(formatter.format(p));
                ((Node)this.nodes.get(child3)).setData4(p);
                total = total + p;
                lastChild = child3;
            }
            Double gap = 1.0 - total;
            Double p = (Double)((Node)this.nodes.get(lastChild)).getData4();
            p = p + gap;
            ((Node)this.nodes.get(lastChild)).setData4(p);
            return;
        }
        ++depth;
        for (String child : children) {
            this.updateCPT2(child, depth);
        }
    }

    public String generateContinuousCLDscript() {
        String sDefault = this.generateContinuousDefault();
        return this.generateContinuousCLDscript("!R", 0, sDefault);
    }

    public String generateContinuousDefault() {
        String s = "";
        if (this.childResidentNode == null) {
            return s;
        }
        s = String.valueOf(s) + " else [ NormalDist(0, 10) ]\n";
        return s;
    }

    public String generateContinuousCLDscript(String identifier, int depth, String sDefault) {
        String s = "";
        Node node = this.getNodes().get(identifier);
        ArrayList<String> children = node.getChildren();
        String tabs = "";
        String tabs2 = "";
        if (depth == this.residentNodes.size()) {
            String eq = (String)node.getData4();
            s = String.valueOf(s) + tabs2 + eq;
        } else if (depth != 0) {
            tabs = String.format("%0" + depth + "d", 0).replace("0", "    ");
            tabs2 = String.format("%0" + (depth + 1) + "d", 0).replace("0", "    ");
            ResidentNode rn = this.residentNodes.get(node.getData2());
            s = String.valueOf(tabs) + "if any " + rn.ordinaryVariableLabel + " have ( " + rn.name + " = " + node.getData1() + " ) [";
        }
        ++depth;
        int i = 0;
        for (String child : children) {
            s = String.valueOf(s) + "\n";
            String str = this.generateContinuousCLDscript(child, depth, sDefault);
            if (depth == this.residentNodes.size()) {
                if (this.childResidentNode != null && i < this.childResidentNode.states.size() - 1) {
                    str = String.valueOf(str) + ",";
                }
            } else if (i != 0) {
                s = String.valueOf(s) + tabs + "] else \n";
            }
            s = String.valueOf(s) + str;
            ++i;
        }
        if (children.size() > 0 && depth != this.residentNodes.size()) {
            s = String.valueOf(s) + tabs + "] \n";
            s = String.valueOf(s) + sDefault;
        }
        return s;
    }

    public String generateCLDscript() {
        String sDefault = this.generateDefault();
        return this.generateCLDscript("!R", 0, sDefault);
    }

    public String generateDefault() {
        String s = "";
        if (this.childResidentNode == null) {
            return s;
        }
        ArrayList<String> states = this.childResidentNode.states;
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
        Node node = this.getNodes().get(identifier);
        ArrayList<String> children = node.getChildren();
        String tabs = "";
        String tabs2 = "";
        if (depth == this.residentNodes.size()) {
            tabs2 = String.format("%0" + (depth + 1) + "d", 0).replace("0", "    ");
            Double p = (Double)node.getData4();
            BigDecimal myNumber = new BigDecimal(p.toString());
            s = String.valueOf(s) + tabs2 + node.getData1() + " = " + myNumber;
        } else if (depth != 0) {
            tabs = String.format("%0" + depth + "d", 0).replace("0", "    ");
            tabs2 = String.format("%0" + (depth + 1) + "d", 0).replace("0", "    ");
            ResidentNode rn = this.residentNodes.get(node.getData2());
            s = String.valueOf(tabs) + "if any " + rn.ordinaryVariableLabel + " have ( " + rn.name + " = " + node.getData1() + " ) [";
        }
        ++depth;
        int i = 0;
        for (String child : children) {
            s = String.valueOf(s) + "\n";
            String str = this.generateCLDscript(child, depth, sDefault);
            if (depth == this.residentNodes.size()) {
                if (this.childResidentNode != null && i < this.childResidentNode.states.size() - 1) {
                    str = String.valueOf(str) + ",";
                }
            } else if (i != 0) {
                s = String.valueOf(s) + tabs + "] else \n";
            }
            s = String.valueOf(s) + str;
            ++i;
        }
        if (children.size() > 0 && depth != this.residentNodes.size()) {
            s = String.valueOf(s) + tabs + "] \n";
            s = String.valueOf(s) + sDefault;
        }
        return s;
    }

    public class ResidentNode {
        String name;
        String ordinaryVariableLabel;
        public ArrayList<String> states;

        public /* varargs */ ResidentNode(String rn, String ovl, String ... sList) {
            this.name = "";
            this.ordinaryVariableLabel = "";
            this.states = new ArrayList();
            this.name = rn;
            this.ordinaryVariableLabel = ovl;
            int i = 0;
            while (i < sList.length) {
                this.states.add(sList[i]);
                ++i;
            }
        }
    }

}

