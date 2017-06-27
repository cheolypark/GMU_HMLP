/*
 * Decompiled with CFR 0_118.
 */
package mebn_rm.MEBN.rv;

import java.util.ArrayList;
import java.util.List;
import mebn_rm.MEBN.CLD.Probability;

public class RV
extends Probability
implements Comparable<RV> {
    public String name;
    public String value;
    public Double prob;
    public List<RV> parents = new ArrayList<RV>();

    public RV(String n, String v, Double p) {
        this.name = n;
        this.value = v;
        this.prob = p;
    }

    public RV(String n, String v) {
        this.name = n;
        this.value = v;
    }

    public String toString() {
        String s = "[";
        s = String.valueOf(s) + this.name + " = ";
        s = String.valueOf(s) + this.value + " ";
        if (!this.parents.isEmpty()) {
            s = String.valueOf(s) + " | ";
            for (RV rv : this.parents) {
                s = String.valueOf(s) + rv.name + " = ";
                s = String.valueOf(s) + rv.value + " ";
                s = String.valueOf(s) + ", ";
            }
            s = s.substring(0, s.length() - 2);
        }
        s = String.valueOf(s) + "]: " + this.prob;
        return s;
    }

    public void addParent(RV rv) {
        this.parents.add(rv);
    }

    public /* varargs */ void addParents(RV ... rvs) {
        RV[] arrrV = rvs;
        int n = arrrV.length;
        int n2 = 0;
        while (n2 < n) {
            RV rv = arrrV[n2];
            this.parents.add(rv);
            ++n2;
        }
    }

    @Override
    public int compareTo(RV o) {
        return 0;
    }

    public String getParentValue(String parent) {
        if (!this.parents.isEmpty()) {
            for (RV rv : this.parents) {
                if (!parent.equalsIgnoreCase(rv.name)) continue;
                return rv.value;
            }
        }
        return null;
    }

    public ArrayList<String> getParents() {
        ArrayList<String> parentsString = new ArrayList<String>();
        if (!this.parents.isEmpty()) {
            for (RV rv : this.parents) {
                parentsString.add(rv.value);
            }
        }
        return parentsString;
    }
}

