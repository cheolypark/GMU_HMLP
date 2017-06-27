/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.apache.commons.math3.stat.inference.GTest
 *  org.apache.commons.math3.stat.inference.TestUtils
 */
package mebn_ln.math.stat;

import java.io.PrintStream;
import org.apache.commons.math3.stat.inference.GTest;
import org.apache.commons.math3.stat.inference.TestUtils;

public class test_GTests {
    public static void main(String[] args) {
        double[] expected = new double[]{0.54, 0.4, 0.05, 0.01};
        long[] observed = new long[]{70, 79, 3, 4};
        System.out.println(TestUtils.g((double[])expected, (long[])observed));
        TestUtils.gTest((double[])expected, (long[])observed);
        double alpha = 0.2;
        TestUtils.gTest((double[])expected, (long[])observed, (double)alpha);
        long[] obs1 = new long[]{268, 199, 42};
        long[] obs2 = new long[]{807, 759, 184};
        System.out.println(TestUtils.gDataSetsComparison((long[])obs1, (long[])obs2));
        System.out.println(TestUtils.gTestDataSetsComparison((long[])obs1, (long[])obs2));
        new GTest().rootLogLikelihoodRatio(5, 1995, 0, 100000);
    }
}

