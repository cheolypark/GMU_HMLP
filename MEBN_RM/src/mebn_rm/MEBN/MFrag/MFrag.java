/*
 * Decompiled with CFR 0_118.
 */
package mebn_rm.MEBN.MFrag;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mebn_rm.MEBN.MNode.MIsANode;
import mebn_rm.MEBN.MNode.MNode;
import mebn_rm.MEBN.MTheory.MTheory;
import mebn_rm.MEBN.MTheory.OVariable;
import mebn_rm.RDB.RDB;
import util.SortableValueMap; 

public class MFrag
implements Comparable<MFrag> {
    public String name;
    public MTheory mTheory = null;
    public List<MIsANode> arrayIsaContextNodes = new ArrayList<MIsANode>();
    public List<MNode> arrayContextNodes = new ArrayList<MNode>();
    public List<MNode> arrayResidentNodes = new ArrayList<MNode>();
    public List<MNode> arrayInputNodes = new ArrayList<MNode>();
    public List<MNode> arrayInputPrevNodes = new ArrayList<MNode>();
    public String joiningSQL;
    public String cvsFile;
    public SortableValueMap<String, List<String>> mapCausality = new SortableValueMap();
    public SortableValueMap<String, List<String>> mapNonCorrelation = new SortableValueMap();
    public SortableValueMap<String, List<String>> mapCorrelation = new SortableValueMap();
    public LearningType learningType = LearningType.STRUCTURE_HYBRID_DISCRETIZED;
    public MFragType mFragType = MFragType.COMMON;

    public MFrag(MTheory m, String mfragName) {
        this.init(m, mfragName);
    }

    public MFrag(MTheory m, String mfragName, String contextSQL) {
        this.init(m, mfragName);
        this.joiningSQL = contextSQL;
    }

    public void init(MTheory m, String mfragName) {
        this.name = mfragName;
        this.mTheory = m;
        m.setMFrags(this);
    }

    public /* varargs */ void setCorrelation(String child, String ... parents) {
        List listParents = null;
        if (this.mapCorrelation.containsKey(child)) {
            listParents = this.mapCorrelation.get(child);
        } else {
            listParents = new ArrayList();
            this.mapCorrelation.put(child, listParents);
        }
        int i = 0;
        while (i < parents.length) {
            listParents.add(parents[i]);
            ++i;
        }
    }

    public /* varargs */ void setNonCorrelation(String child, String ... parents) {
        List listParents = null;
        if (this.mapNonCorrelation.containsKey(child)) {
            listParents = this.mapNonCorrelation.get(child);
        } else {
            listParents = new ArrayList();
            this.mapNonCorrelation.put(child, listParents);
        }
        int i = 0;
        while (i < parents.length) {
            listParents.add(parents[i]);
            ++i;
        }
    }

    public /* varargs */ void setCausality(String child, String ... parents) {
        List listParents = null;
        if (this.mapCausality.containsKey(child)) {
            listParents = this.mapCausality.get(child);
        } else {
            listParents = new ArrayList();
            this.mapCausality.put(child, listParents);
        }
        int i = 0;
        while (i < parents.length) {
            listParents.add(parents[i]);
            ++i;
        }
    }

    public void updateCausality() {
        for (MNode mn : this.arrayResidentNodes) {
            List<MNode> mns = mn.getAllParents();
            for (MNode p : mns) {
                this.setCausality(mn.name, p.name);
            }
        }
    }

    public MNode getMNode(String s) {
        for (MNode mn : this.arrayResidentNodes) {
            if (!s.equalsIgnoreCase(mn.name)) continue;
            return mn;
        }
        return null;
    }

    public /* varargs */ void setMNodes(MNode ... mns) {
        MNode[] arrmNode = mns;
        int n = arrmNode.length;
        int n2 = 0;
        while (n2 < n) {
            MNode l = arrmNode[n2];
            l.mFrag = this;
            this.setMNode(l);
            ++n2;
        }
    }

    public void setMNode(MNode mn) {
        for (MNode n : this.arrayResidentNodes) {
            if (!n.name.equalsIgnoreCase(mn.name)) continue;
            return;
        }
        this.arrayResidentNodes.add(mn);
    }

    public boolean removeMNode(MNode mn) {
        this.arrayResidentNodes.remove(mn);
        return this.arrayResidentNodes.isEmpty();
    }

    public List<MNode> getAllNodes() {
        ArrayList<MNode> array = new ArrayList<MNode>();
        for (MNode s22 : this.arrayContextNodes) {
            array.add(s22);
        }
        for (MNode s22 : this.arrayResidentNodes) {
            array.add(s22);
        }
        this.arrayInputPrevNodes.clear();
        for (MNode s22 : this.arrayInputNodes) {
            if (!array.contains(s22)) {
                array.add(s22);
                continue;
            }
            this.arrayInputPrevNodes.add(s22);
        }
        return array;
    }

    public List<MNode> getMNodes() {
        ArrayList<MNode> array = new ArrayList<MNode>();
        for (MNode s2 : this.arrayResidentNodes) {
            array.add(s2);
        }
        for (MNode s2 : this.arrayInputNodes) {
            if (array.contains(s2)) continue;
            array.add(s2);
        }
        return array;
    }

    public List<MNode> getInputPrevNodes() {
        ArrayList<MNode> array = new ArrayList<MNode>();
        for (MNode s : this.arrayInputPrevNodes) {
            array.add(s);
        }
        return array;
    }

    public Double getSumMNodeLogScores() {
        Double logSCs = 0.0;
        for (MNode mn : this.arrayResidentNodes) {
            Double logSC = mn.getlogCLDScore();
            logSCs = logSCs + logSC;
        }
        return logSCs;
    }

    public Double getAvglogMNodeScore() {
        Double logSC = 0.0;
        for (MNode mn : this.arrayResidentNodes) {
            logSC = logSC + mn.getAvglogCLDScore();
        }
        return logSC;
    }

    public void initSelectedDataset(int size) {
        System.out.println("init Selected Dataset for the MFrag : " + this.name);
        ResultSet rs = null;
        if (this.joiningSQL == null) {
            String attrs = "";
            String strTables = this.name;
            for (MNode mn : this.getMNodes()) {
                attrs = String.valueOf(attrs) + mn.getAttributeName() + " as " + mn.name + ", ";
            }
            if (!attrs.isEmpty()) {
                attrs = attrs.substring(0, attrs.length() - 2);
                rs = RDB.This().get(attrs, strTables);
            }
        } else {
            rs = RDB.This().get(this.joiningSQL);
        }
        if (rs != null) {
            try {
                rs.beforeFirst();
                try {
                    this.cvsFile = RDB.This().toExcel(this.name, this.mTheory.name, rs);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            catch (SQLException e1) {
                e1.printStackTrace();
            }
            System.out.println("MFrag : " + this.name);
        }
    }

    public String toString() {
        ArrayList<String> inclusions = new ArrayList<String>();
        inclusions.add("MFrag");
        inclusions.add("MNode");
        inclusions.add("CLD");
        return this.toString(inclusions);
    }

    public String toString(List<String> inclusions) {
        String s = "";
        if (inclusions.contains("MFrag")) {
            String context;
            s = String.valueOf(s) + "[F: " + this.name + "\r\n";
            for (MIsANode c2 : this.arrayIsaContextNodes) {
                context = "[C: " + c2.toString() + "]" + "\r\n";
                s = String.valueOf(s) + "\t\t" + context;
            }
            for (MNode c : this.arrayContextNodes) {
                context = c.toString(inclusions);
                context = context.replace("R:", "C:");
                s = String.valueOf(s) + "\t\t" + context;
            }
            for (MNode r : this.arrayResidentNodes) {
                context = r.toString(inclusions);
                s = String.valueOf(s) + "\t\t" + context;
            }
        }
        if (inclusions.contains("MFrag")) {
            s = String.valueOf(s) + "\t]";
        }
        return s;
    }

    @Override
    public int compareTo(MFrag o) {
        return 0;
    }

    public void resetContextNodes() {
        for (MNode mn : this.arrayResidentNodes) {
            System.out.println(mn.ovs);
            for (MNode rp : mn.parentMNodes) {
                System.out.println(rp.ovs);
            }
            for (MNode ip : mn.inputParentMNodes) {
                System.out.println(ip.ovs);
            }
        }
    }

    public static enum LearningType {
        BAYES,
        PARAMETER,
        STRUCTURE,
        STRUCTURE_HYBRID_DISCRETIZED  
    }

    public static enum MFragType {
        COMMON,
        REFERENCE 
    }

}

