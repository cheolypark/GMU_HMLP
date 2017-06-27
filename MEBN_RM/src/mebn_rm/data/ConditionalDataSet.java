/*
 * Decompiled with CFR 0_118.
 */
package mebn_rm.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mebn_rm.MEBN.rv.RV;
import mebn_rm.data.ConditionalData;

public class ConditionalDataSet
implements Serializable {
    private static final long serialVersionUID = -1372300619947556069L;
    public ArrayList<ConditionalData> dataArray = new ArrayList();
    public ArrayList<RV> arrayRV = new ArrayList();

    public RV get(RV r) {
        for (RV d : this.arrayRV) {
            if (!d.name.equalsIgnoreCase(r.name) || !d.parents.toString().equalsIgnoreCase(r.parents.toString())) continue;
            return d;
        }
        return null;
    }

    public void addConditionalData(RV d) {
        this.arrayRV.add(d);
    }

    public /* varargs */ void addConditionalData(String c, String ... ps) {
        RV cRV = new RV("Y", c);
        int i = 0;
        String[] arrstring = ps;
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String p = arrstring[n2];
            cRV.addParent(new RV("X" + i, p));
            ++n2;
        }
        this.arrayRV.add(cRV);
    }

    public void addConditionalData(ConditionalData d) {
        this.dataArray.add(d);
    }

    public ConditionalData setY(String y) {
        ConditionalData d = new ConditionalData();
        this.dataArray.add(d);
        d.Y = y;
        return d;
    }
 
    public String toString() {
        String s = "";
        Integer i = 1;
        for (RV d : this.arrayRV) {
            s = String.valueOf(s) + i + ":" + d.toString() + "\n";
            i = i + 1;
        }
        return s;
    }
}

