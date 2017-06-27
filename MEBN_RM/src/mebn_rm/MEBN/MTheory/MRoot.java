/*
 * Decompiled with CFR 0_118.
 */
package mebn_rm.MEBN.MTheory;

import java.util.ArrayList;
import java.util.Set;
import mebn_rm.MEBN.MTheory.MTheory;
import util.SortableValueMap; 

public class MRoot
implements Comparable<MRoot> {
    public SortableValueMap<MTheory, Double> mtheoryCANs = new SortableValueMap();
    public String name = "ROOT";

    public /* varargs */ void setMTheories(MTheory ... mtheories) {
        ArrayList<MTheory> arrayMTheories = new ArrayList<MTheory>();
        MTheory[] arrmTheory = mtheories;
        int n = arrmTheory.length;
        int n2 = 0;
        while (n2 < n) {
            MTheory l = arrmTheory[n2];
            arrayMTheories.add(l);
            ++n2;
        }
        for (MTheory l : this.mtheoryCANs.keySet()) {
            arrayMTheories.add(l);
        }
        Integer i = 1;
        this.mtheoryCANs.clear();
        for (MTheory l2 : arrayMTheories) {
            l2.name = String.valueOf(this.name) + "_MTheory_" + i;
            this.mtheoryCANs.put(l2, Math.log(1.0 / (double)arrayMTheories.size()));
            i = i + 1;
        }
    }

    public String toString() {
        String s = "[" + this.name + " :";
        for (MTheory m : this.mtheoryCANs.keySet()) {
            s = String.valueOf(s) + m.toString() + ", ";
        }
        s = s.substring(0, s.length() - 2);
        s = String.valueOf(s) + "]";
        return s;
    }

    public ArrayList<Double> getlogMFragScores() {
        ArrayList<Double> logSCs = new ArrayList<Double>();
        for (MTheory m : this.mtheoryCANs.keySet()) {
            Double logSC = this.mtheoryCANs.get(m);
            logSCs.add(logSC);
        }
        return logSCs;
    }

    @Override
    public int compareTo(MRoot o) {
        return 0;
    }

    public MTheory getMTheory(String name) {
        for (MTheory m : this.mtheoryCANs.keySet()) {
            if (name.isEmpty()) {
                return m;
            }
            if (!m.name.equalsIgnoreCase(name)) continue;
            return m;
        }
        return null;
    }
}

