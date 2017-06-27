/*
 * Decompiled with CFR 0_118.
 */
package mebn_rm.MEBN.CLD;

import java.util.ArrayList;

public class Probability {
    public /* varargs */ String random(Double[] p, String ... s) {
        double r = Math.random();
        return this.random_operator(0, r, 0.0, p, s);
    }

    public /* varargs */ String random_operator(int index, double r, double e, Double[] p, String ... s) {
        if (index == s.length) {
            return "";
        }
        double b = e;
        if (b < r && r <= (e += p[index].doubleValue())) {
            return s[index];
        }
        return this.random_operator(++index, r, e, p, s);
    }

    public String randomByUniform(ArrayList<String> s) {
        double r = Math.random();
        return this.randomByUniform_operator(0, r, s);
    }

    public String randomByUniform_operator(int index, double r, ArrayList<String> s) {
        if (index == s.size()) {
            return "";
        }
        double b = ((double)index + 0.0) / (double)s.size();
        double e = ((double)index + 1.0) / (double)s.size();
        if (b < r && r <= e) {
            return s.get(index);
        }
        return this.randomByUniform_operator(++index, r, s);
    }

    public /* varargs */ String randomByUniform(String ... s) {
        double r = Math.random();
        return this.randomByUniform_operator(0, r, s);
    }

    public /* varargs */ String randomByUniform_operator(int index, double r, String ... s) {
        if (index == s.length) {
            return "";
        }
        double b = ((double)index + 0.0) / (double)s.length;
        double e = ((double)index + 1.0) / (double)s.length;
        if (b < r && r <= e) {
            return s[index];
        }
        return this.randomByUniform_operator(++index, r, s);
    }

    public Integer sizeOf(ArrayList<String> ps, String cmp) {
        Integer i = 0;
        for (String s : ps) {
            if (!s.equalsIgnoreCase(cmp)) continue;
            i = i + 1;
        }
        return i;
    }
}

