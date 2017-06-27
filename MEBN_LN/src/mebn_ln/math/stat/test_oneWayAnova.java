/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.apache.commons.math3.stat.inference.TestUtils
 */
package mebn_ln.math.stat;

import java.io.PrintStream;
import java.util.ArrayList;
import org.apache.commons.math3.stat.inference.TestUtils;

public class test_oneWayAnova {
    public static void main(String[] args) {
        double[] classA = new double[]{93.0, 103.0, 95.0, 101.0, 91.0, 105.0, 96.0, 94.0, 101.0};
        double[] classB = new double[]{99.0, 92.0, 102.0, 100.0, 102.0, 89.0};
        double[] classC = new double[]{110.0, 115.0, 111.0, 117.0, 128.0, 117.0};
        ArrayList<double[]> classes = new ArrayList<double[]>();
        classes.add(classA);
        classes.add(classB);
        classes.add(classC);
        double fStatistic = TestUtils.oneWayAnovaFValue(classes);
        double pValue = TestUtils.oneWayAnovaPValue(classes);
        System.out.println("fStatistic: " + fStatistic);
        System.out.println("pValue: " + pValue);
        System.out.println(TestUtils.oneWayAnovaTest(classes, (double)0.01));
    }
}

