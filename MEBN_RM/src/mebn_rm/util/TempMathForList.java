/*
 * Decompiled with CFR 0_118.
 */
package mebn_rm.util;

import java.util.ArrayList;
import java.util.List;

import util.TempMathFunctions; 

public class TempMathForList {
    public static double sum(List<Double> a) {
        if (a.size() > 0) {
            double sum = 0.0;
            for (Double i : a) {
                sum += i.doubleValue();
            }
            return sum;
        }
        return 0.0;
    }

    public static List<Double> getDoubleList(List<String> a) {
        ArrayList<Double> d = new ArrayList<Double>();
        for (String i : a) {
            if (!TempMathFunctions.isNum(i)) continue;
            d.add(Double.valueOf(i));
        }
        return d;
    }

    public static double meanFromString(List<String> a) {
        return TempMathForList.mean(TempMathForList.getDoubleList(a));
    }

    public static double mean(List<Double> a) {
        double sum = TempMathForList.sum(a);
        double mean = 0.0;
        mean = sum / ((double)a.size() * 1.0);
        return mean;
    }

    public static double median(List<Double> a) {
        int middle = a.size() / 2;
        if (a.size() % 2 == 1) {
            return a.get(middle);
        }
        return (a.get(middle - 1) + a.get(middle)) / 2.0;
    }

    public static double sdFromString(List<String> a) {
        return TempMathForList.sd(TempMathForList.getDoubleList(a));
    }

    public static double sd(List<Double> a) {
        double sum = 0.0;
        double mean = TempMathForList.mean(a);
        for (Double i : a) {
            sum += Math.pow(i - mean, 2.0);
        }
        return Math.sqrt(sum / (double)(a.size() - 1));
    }
}

