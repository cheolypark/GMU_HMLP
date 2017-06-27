/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.apache.commons.collections.MultiMap
 */
package mebn_rm.MEBN.MTheory;
 
import java.util.ArrayList; 
import java.util.List; 
import mebn_rm.MEBN.CLD.CLD;
import mebn_rm.MEBN.CLD.Categorical;
import mebn_rm.MEBN.CLD.ConditionalGaussian;
import mebn_rm.MEBN.MFrag.MFrag;
import mebn_rm.MEBN.MNode.MCNode;
import mebn_rm.MEBN.MNode.MDNode;
import mebn_rm.MEBN.MNode.MIsANode;
import mebn_rm.MEBN.MNode.MNode;
import mebn_rm.MEBN.MTheory.OVariable;
import mebn_rm.RDB.RDB; 
import mebn_rm.util.StringUtil;
import util.SortableValueMap;
  
public class MTheory implements Comparable<MTheory> {
    public SortableValueMap<MFrag, Double> mfrags = new SortableValueMap<MFrag, Double>();
    public ArrayList<String> entities = new ArrayList();
    public String name;
    public RDB rdb = null;

    public MTheory(String n) {
        this.name = n;
    }

    public /* varargs */ void setMFrags(MFrag ... fs) {
        MFrag[] arrmFrag = fs;
        int n = arrmFrag.length;
        int n2 = 0;
        while (n2 < n) {
            MFrag f = arrmFrag[n2];
            f.mTheory = this;
            this.mfrags.put(f, 0.0);
            ++n2;
        }
    }

    public MFrag getMFrag(String s) {
        for (MFrag f : this.mfrags.keySet()) {
            if (!s.equalsIgnoreCase(f.name)) continue;
            return f;
        }
        return null;
    }

    public boolean removeMFrag(MFrag f) {
        this.mfrags.remove(f);
        return this.mfrags.isEmpty();
    }

    public MNode getMNode(String s) {
        String mFrag = new StringUtil().getLeft(s);
        String mNode = new StringUtil().getRight(s);
        MFrag f = this.getMFrag(mFrag);
        MNode childMNode = f.getMNode(mNode);
        return childMNode;
    }

    public void addParents(String c, List<String> ps) {
        String mFrag = new StringUtil().getLeft(c);
        String mNode = new StringUtil().getRight(c);
        String combParents = "";
        boolean bOtherMFrag = false;
        for (String p : ps) {
            String mFragP = new StringUtil().getLeft(p);
            String mNodeP = new StringUtil().getRight(p);
            if (!p.equalsIgnoreCase(ps.get(0))) {
                combParents = String.valueOf(combParents) + "_";
            }
            if (!mFrag.equalsIgnoreCase(mFragP) && !bOtherMFrag) {
                combParents = String.valueOf(combParents) + mNode + "_";
                bOtherMFrag = true;
            }
            combParents = String.valueOf(combParents) + mNodeP;
        }
        MFrag f = this.getMFrag(mFrag);
        MNode childMNode = f.getMNode(mNode);
        if (bOtherMFrag) {
            String sql = "SELECT\r\n";
            String sqlFrom = "";
            sql = String.valueOf(sql) + mFrag + "." + childMNode.getAttributeName() + " as " + mNode + ",\r\n";
            sqlFrom = String.valueOf(sqlFrom) + mFrag + ", ";
            MFrag newMFrag = new MFrag(this, combParents);
            this.setMFrags(newMFrag);
            MNode newChild = null;
            if (childMNode.isContinuous()) {
                newChild = new MCNode(childMNode);
            } else if (childMNode.isContinuous()) {
                newChild = new MDNode(childMNode);
            }
            newMFrag.setMNodes(newChild);
            List<String> keys = (List)this.rdb.mapTableAndKeys.get((Object)mFrag);
            for (String key : keys) {
                String origin = this.rdb.mapKeysOrigins.get(key);
                OVariable ov = new OVariable(mFrag, key, origin);
                newMFrag.arrayIsaContextNodes.add(new MIsANode(f, ov));
            }
            for (String p2 : ps) {
                String mFragP = new StringUtil().getLeft(p2);
                String mNodeP = new StringUtil().getRight(p2);
                MFrag fp = this.getMFrag(mFragP);
                MNode parentMNode = fp.getMNode(mNodeP);
                newChild.setInputParents(parentMNode);
                sql = String.valueOf(sql) + mFragP + "." + parentMNode.getAttributeName() + " as " + mNodeP + ",\r\n";
                sqlFrom = String.valueOf(sqlFrom) + mFragP + ", ";
                List<String> keys2 = (List)this.rdb.mapTableAndKeys.get((Object)mFragP);
                for (String key2 : keys2) {
                    String origin = this.rdb.mapKeysOrigins.get(key2);
                    OVariable ov = new OVariable(mFragP, key2, origin);
                    newMFrag.arrayIsaContextNodes.add(new MIsANode(f, ov));
                }
            }
            sql = sql.substring(0, sql.length() - 3);
            sql = String.valueOf(sql) + "\r\nFROM\r\n";
            sqlFrom = sqlFrom.substring(0, sqlFrom.length() - 2);
            sql = String.valueOf(sql) + (String)sqlFrom + "\r\n";
            System.out.println(sql);
            newMFrag.joiningSQL = sql;
            if (f.removeMNode(childMNode)) {
                this.removeMFrag(f);
            }
        } else {
            for (String p3 : ps) {
                String mFragP = new StringUtil().getLeft(p3);
                String mNodeP = new StringUtil().getRight(p3);
                MNode parentMNode = f.getMNode(mNodeP);
                childMNode.setParents(parentMNode);
            }
        }
    }

    public void addContexts(String mFrag, String sql) {
        MFrag f = this.getMFrag(mFrag);
    }

    public void updateContexts() {
        for (MFrag f : this.mfrags.keySet()) {
            ArrayList<MIsANode> removeList;
            ArrayList<String> listEntityType;
            OVariable ov;
            if (f.name.equalsIgnoreCase("HI_temperature_SII_temperature_HAI_energy")) {
                System.out.println("HI_temperature_SII_temperature_HAI_energy");
            }
            if (f.joiningSQL != null) {
                System.out.println(f.arrayContextNodes);
                listEntityType = new ArrayList<String>();
                removeList = new ArrayList<MIsANode>();
                for (MIsANode isa : f.arrayIsaContextNodes) {
                    ov = (OVariable)isa.ovs.get(0);
                    if (listEntityType.contains(ov.entityType)) {
                        removeList.add(isa);
                        continue;
                    }
                    listEntityType.add(ov.entityType);
                }
                if (removeList.size() > 0) {
                    f.arrayIsaContextNodes.removeAll(removeList);
                }
                String sql = "WHERE \r\n";
                MIsANode isamain = f.arrayIsaContextNodes.get(0);
                String curKey = String.valueOf(((OVariable)isamain.ovs.get((int)0)).originMFrag) + "." + ((OVariable)isamain.ovs.get((int)0)).originKey;
                for (MIsANode isa2 : removeList) {
                    String otherKey = String.valueOf(((OVariable)isa2.ovs.get((int)0)).originMFrag) + "." + ((OVariable)isa2.ovs.get((int)0)).originKey;
                    sql = String.valueOf(sql) + curKey + " = " + otherKey + " &&\r\n";
                }
                sql = sql.substring(0, sql.length() - 5);
                f.joiningSQL = String.valueOf(f.joiningSQL) + sql;
                continue;
            }
            if (f.joiningSQL != null) continue;
            System.out.println(f.arrayContextNodes);
            listEntityType = new ArrayList();
            removeList = new ArrayList();
            for (MIsANode isa : f.arrayIsaContextNodes) {
                ov = (OVariable)isa.ovs.get(0);
                if (listEntityType.contains(ov.entityType)) {
                    removeList.add(isa);
                    continue;
                }
                listEntityType.add(ov.entityType);
            }
            if (removeList.size() <= 0) continue;
            f.arrayIsaContextNodes.removeAll(removeList);
        }
    }

    public void addCLDType(String c, CLD cldType) {
        String mFrag = new StringUtil().getLeft(c);
        String mNode = new StringUtil().getRight(c);
        MFrag f = this.getMFrag(mFrag);
        MNode childMNode = f.getMNode(mNode);
        childMNode.setCLDs(cldType);
    }

    public void updateCLDs() {
        for (MFrag f : this.mfrags.keySet()) {
            for (MNode n : f.getMNodes()) {
                System.out.println(n);
                if (n.isContinuous()) {
                    n.setCLDs(new ConditionalGaussian());
                } else if (n.isDiscrete()) {
                    n.setCLDs(new Categorical());
                }
                System.out.println(n);
            }
        }
    }

    public String toString() {
        String s = "[M: " + this.name + "\r\n";
        for (MFrag m : this.mfrags.keySet()) {
            s = String.valueOf(s) + "\t" + m.toString() + "\r\n";
        }
        s = String.valueOf(s) + "]";
        return s;
    }

    public String toString(String ... inclusion) {
        ArrayList<String> inclusions = new ArrayList<String>();
        int i = 0;
        while (i < inclusion.length) {
            inclusions.add(inclusion[i]);
            ++i;
        }
        String s = "[M: " + this.name + "\r\n";
        for (MFrag m : this.mfrags.keySet()) {
            s = String.valueOf(s) + "\t" + m.toString(inclusions) + "\r\n";
        }
        s = String.valueOf(s) + "]";
        return s;
    }

    public Double getSumMFragLogScores() {
        Double logSCs = 0.0;
        for (MFrag f : this.mfrags.keySet()) {
            Double logSC = f.getSumMNodeLogScores();
            logSCs = logSCs + logSC;
            System.out.println(String.valueOf(f.toString()) + " : " + logSC);
        }
        return logSCs;
    }

    public Double getAvgLogMFragScore() {
        Double logSC = 0.0;
        for (MFrag f : this.mfrags.keySet()) {
            logSC = logSC + f.getAvglogMNodeScore();
            this.mfrags.put(f, f.getAvglogMNodeScore());
        }
        return logSC;
    }
 
    public int compareTo(MTheory o) {
        return 0;
    }
}

