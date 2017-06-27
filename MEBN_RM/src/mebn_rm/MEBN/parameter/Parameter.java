/*
 * Decompiled with CFR 0_118.
 */
package mebn_rm.MEBN.parameter;

import java.util.ArrayList;
import mebn_rm.MEBN.rv.RV;
import util.SortableValueMap; 

public class Parameter
implements Comparable<Parameter> {
    public ArrayList<Double> parameters = new ArrayList();
    public boolean bTrueParameter = false;
    SortableValueMap<RV, Double> rvMap = new SortableValueMap();

    public Parameter() {
    }

    public /* varargs */ Parameter(boolean b, Double ... ds) {
        this.bTrueParameter = b;
        Double[] arrdouble = ds;
        int n = arrdouble.length;
        int n2 = 0;
        while (n2 < n) {
            Double d = arrdouble[n2];
            this.parameters.add(d);
            ++n2;
        }
    }

    public /* varargs */ Parameter(boolean b, RV ... rvs) {
        this.bTrueParameter = b;
        RV[] arrrV = rvs;
        int n = arrrV.length;
        int n2 = 0;
        while (n2 < n) {
            RV rv = arrrV[n2];
            this.rvMap.put(rv, 0.0);
            ++n2;
        }
    }

    public String toString() {
        String s = String.valueOf(this.bTrueParameter) + " [";
        for (Double d : this.parameters) {
            s = String.valueOf(s) + d + ", ";
        }
        s = s.substring(0, s.length() - 2);
        s = String.valueOf(s) + "]";
        return s;
    }
 
    public int compareTo(Parameter o) {
        for (Double d1 : o.parameters) {
            for (Double d2 : this.parameters) {
                if (d1 <= d2) continue;
                return -1;
            }
        }
        return 1;
    }
}

