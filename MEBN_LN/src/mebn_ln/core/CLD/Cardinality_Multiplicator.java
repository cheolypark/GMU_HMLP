/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  mebn_rm.MEBN.CLD.LPD_Discrete
 *  mebn_rm.MEBN.parameter.Parameter
 *  mebn_rm.data.ConditionalData
 *  mebn_rm.data.ConditionalDataSet
 */
package mebn_ln.core.CLD;

import java.util.ArrayList;
import mebn_rm.MEBN.CLD.LPD_Discrete;
import mebn_rm.MEBN.parameter.Parameter;
import mebn_rm.data.ConditionalData;
import mebn_rm.data.ConditionalDataSet;

public class Cardinality_Multiplicator
extends LPD_Discrete {
    public Cardinality_Multiplicator() {
        super("", "Cardinality_Multiplicator");
        this.parameterSize = 2;
    }

    public Cardinality_Multiplicator(String name) {
        super(name, "Cardinality_Multiplicator");
        this.parameterSize = 2;
    }

    public ArrayList<String> generateParent(int parentSize) {
        ArrayList<String> ps = new ArrayList<String>();
        int i = 0;
        while (i < parentSize) {
            ps.add(this.randomByUniform(new String[]{"T", "W"}));
            ++i;
        }
        return ps;
    }

    public ArrayList<Double> calculateDist(ConditionalData d, Parameter para) {
        ArrayList<Double> probs = new ArrayList<Double>();
        ArrayList parents = d.arrayParent;
        Double i1 = this.sizeOf(parents, "T").doubleValue();
        Double i2 = this.sizeOf(parents, "W").doubleValue();
        Double d1 = 0.0;
        Double d2 = 0.0;
        if (i1 > 0.0) {
            d1 = 1.0 - Math.pow((Double)para.parameters.get(0), i1);
            d2 = 1.0 - d1;
        } else {
            d1 = 0.5;
            d2 = 0.5;
        }
        if (d.Y.equalsIgnoreCase("H")) {
            probs.add(d1);
            probs.add(d2);
        } else {
            probs.add(d2);
            probs.add(d1);
        }
        return probs;
    }

    public ConditionalData distributionFunction(ArrayList<String> ps) {
        ConditionalData d = null;
        Double i1 = this.sizeOf(ps, "T").doubleValue();
        Double i2 = this.sizeOf(ps, "W").doubleValue();
        Double d1 = 1.0 - 1.0 / i1;
        Double d2 = 1.0 - d1;
        Double[] p = new Double[]{d1, d2};
        String c = this.random(p, new String[]{"H", "L"});
        d = this.CD.setY(c);
        return d;
    }
}

