/*
 * HML Core
 * Copyright (C) 2017 Cheol Young Park
 * 
 * This file is part of HML Core.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mebn_ln.math.stat;
 
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

