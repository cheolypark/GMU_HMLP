/*
 * Decompiled with CFR 0_118.
 */
package mebn_rm.data;

import java.io.Serializable;
import java.util.ArrayList;
import mebn_rm.data.ConditionalDataSet;

public class DataSet
implements Serializable {
    private static final long serialVersionUID = 8223089159543946185L;
    private ArrayList<ConditionalDataSet> arrayData = new ArrayList();

    public /* varargs */ DataSet(ConditionalDataSet ... cds) {
        ConditionalDataSet[] arrconditionalDataSet = cds;
        int n = arrconditionalDataSet.length;
        int n2 = 0;
        while (n2 < n) {
            ConditionalDataSet cd = arrconditionalDataSet[n2];
            this.add(cd);
            ++n2;
        }
    }

    public void add(ConditionalDataSet d) {
        this.arrayData.add(d);
    }

    public ConditionalDataSet get(int index) {
        return this.arrayData.get(index);
    }

    public String toString() {
        String s = "";
        Integer i = 1;
        for (ConditionalDataSet d : this.arrayData) {
            s = String.valueOf(s) + i + ":" + d.toString() + "\n";
            i = i + 1;
        }
        return s;
    }
}

