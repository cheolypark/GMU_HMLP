/*
 * Decompiled with CFR 0_118.
 */
package mebn_rm.RDB;

import java.util.ArrayList;

public class Bin {
    public Double min = 0.0;
    public Double max = 0.0;
    public int size = 5;
    ArrayList<String> list = new ArrayList();

    public Bin(Double n, Double x) {
        this.min = n;
        this.max = x;
        this.initBins();
    }

    private String toString(Double d) {
        String s = d.toString();
        s = s.replace('-', 'M');
        s = s.replace('.', 'P');
        return s;
    }

    private void initBins() {
        Double s = this.min;
        Double e = 0.0;
        int i = 0;
        while (i < this.size) {
            e = s + (this.max - this.min) / (double)this.size;
            String str = "_" + this.toString(s) + "_" + this.toString(e) + "_";
            this.list.add(str);
            s = e;
            ++i;
        }
    }

    public int getIndex(Double c) {
        Double s = this.min;
        Double e = 0.0;
        int i = 0;
        i = 0;
        while (i < this.size) {
            e = s + (this.max - this.min) / (double)this.size;
            if (s <= c && c <= e) {
                return i;
            }
            s = e;
            ++i;
        }
        return i - 1;
    }

    public ArrayList<String> getAllBins() {
        return this.list;
    }
}

