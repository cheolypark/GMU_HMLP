/*
 * Decompiled with CFR 0_118.
 */
package mebn_rm.RDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Bin is the class for a bin or an interval information.
 * <p>
 * 
 * @author      Cheol Young Park
 * @version     0.0.1
 * @since       1.5
 */

public class Bin {
    public Double min = 0.0;
    public Double max = 0.0;
    public int size = 5;
    List<String> list = new ArrayList<String>();

    public Bin(Double n, Double x) {
        min = n;
        max = x;
        initBins();
    }

    private String toString(Double d) {
        String s = d.toString();
        s = s.replace('-', 'M');
        s = s.replace('.', 'P');
        return s;
    }

    private void initBins() {
        Double s = min;
        Double e = 0.0;
        int i = 0;
        while (i < size) {
            e = s + (max - min) / (double)size;
            String str = "_" + toString(s) + "_" + toString(e) + "_";
            list.add(str);
            s = e;
            ++i;
        }
    }

    public int getIndex(Double c) {
        Double s = min;
        Double e = 0.0;
        int i = 0;
        i = 0;
        while (i < size) {
            e = s + (max - min) / (double)size;
            if (s <= c && c <= e) {
                return i;
            }
            s = e;
            ++i;
        }
        return i - 1;
    }

    public List<String> getAllBins() {
        return list;
    }
}

