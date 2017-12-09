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
package mebn_rm.util;

public class ProbabilityMath {
    public static double normalCdf(double x) {
        boolean qdirct;
        double dumnor;
        double derf = 0.0;
        double derfc = 0.0;
        double[] xnum1 = new double[]{242.66795523053176, 21.979261618294153, 6.996383488619135, -0.035609843701815386};
        double[] xden1 = new double[]{215.0588758698612, 91.1649054045149, 15.082797630407788, 1.0};
        double[] xnum2 = new double[]{300.4592610201616, 451.9189537118729, 339.3208167343437, 152.9892850469404, 43.162227222056735, 7.2117582508830935, 0.564195517478974, -1.368648573827167E-7};
        double[] xden2 = new double[]{300.4592609569833, 790.9509253278981, 931.3540948506096, 638.9802644656312, 277.58544474398764, 77.00015293522948, 12.782727319629423, 1.0};
        double[] xnum3 = new double[]{-0.002996107077035422, -0.04947309106232507, -0.22695659353968692, -0.2786613086096478, -0.02231924597341847};
        double[] xden3 = new double[]{0.010620923052846792, 0.19130892610782985, 1.051675107067932, 1.9873320181713525, 1.0};
        double pim12 = 0.5641895835477563;
        double sqrt2 = 1.4142135623730951;
        if (Math.abs(x) < 1.0E-30) {
            dumnor = 0.5;
        } else if (x < -38.0) {
            dumnor = 0.0;
        } else if (x < -15.0) {
            dumnor = Math.exp(ProbabilityMath.dlanor(x));
        } else if (x > 6.0) {
            dumnor = 1.0;
        }
        double z = Math.abs(x / sqrt2);
        double z2 = z * z;
        double zm2 = 1.0 / z2;
        if (z < 0.5) {
            derf = z * ProbabilityMath.devlpl(xnum1, 4, z2) / ProbabilityMath.devlpl(xden1, 4, z2);
            qdirct = true;
        } else if (z < 4.0) {
            derfc = Math.exp(- z2) * ProbabilityMath.devlpl(xnum2, 8, z) / ProbabilityMath.devlpl(xden2, 8, z);
            qdirct = false;
        } else {
            derfc = Math.exp(- z2) / z * (pim12 + zm2 * ProbabilityMath.devlpl(xnum3, 5, zm2) / ProbabilityMath.devlpl(xden3, 5, zm2));
            qdirct = false;
        }
        if (x >= 0.0) {
            if (!qdirct) {
                derf = 1.0 - derfc;
            }
            dumnor = (1.0 + derf) / 2.0;
        } else {
            if (qdirct) {
                derfc = 1.0 - derf;
            }
            dumnor = derfc / 2.0;
        }
        return dumnor;
    }

    static double devlpl(double[] A, int n, double x) {
        double term = A[n - 1];
        int i = n - 2;
        while (i >= 0) {
            term = A[i] + term * x;
            --i;
        }
        return term;
    }

    static double dlanor(double x) {
        double dlsqpi = 0.9189385332046728;
        double[] coef = new double[]{-1.0, 3.0, -15.0, 105.0, -945.0, 10395.0, -135135.0, 2027025.0, -3.4459425E7, 6.54729075E8, -1.3749310575E10, 3.16234143225E11};
        double xx = Math.abs(x);
        double approx = - dlsqpi - 0.5 * xx * xx - Math.log(xx);
        double xx2 = xx * xx;
        double correc = ProbabilityMath.devlpl(coef, 12, 1.0 / xx2) / xx2;
        correc = ProbabilityMath.dln1px(correc);
        double result = approx + correc;
        return result;
    }

    static double dln1px(double a) {
        double p1 = -1.29418923021993;
        double p2 = 0.405303492862024;
        double p3 = -0.0178874546012214;
        double q1 = -1.62752256355323;
        double q2 = 0.747811014037616;
        double q3 = -0.0845104217945565;
        if (Math.abs(a) <= 0.375) {
            double t = a / (a + 2.0);
            double t2 = t * t;
            double w = (((p3 * t2 + p2) * t2 + p1) * t2 + 1.0) / (((q3 * t2 + q2) * t2 + q1) * t2 + 1.0);
            double result = 2.0 * t * w;
            return result;
        }
        double x = 1.0 + a;
        return Math.log(x);
    }

    public static double inverseNormal(double p) {
        double z;
        double sign = 1.0;
        double[] xnum = new double[]{-0.322232431088, -1.0, -0.342242088547, -0.0204231210245, -4.53642210148E-5};
        double[] xden = new double[]{0.099348462606, 0.588581570495, 0.531103462366, 0.10353775285, 0.0038560700634};
        if (p < 1.0E-20) {
            double invnor = -10.0;
            return invnor;
        }
        if (p > 1.0) {
            double invnor = 10.0;
            return invnor;
        }
        if (p <= 0.5) {
            sign = -1.0;
            z = p;
        } else {
            z = 1.0 - p;
        }
        double y = Math.sqrt(-2.0 * Math.log(z));
        double invnor = y + ProbabilityMath.devlpl(xnum, 5, y) / ProbabilityMath.devlpl(xden, 5, y);
        invnor = sign * invnor;
        return invnor;
    }

    public static double factorial(int n) {
        int nx = 1;
        double x = -999.0;
        if (n < 0) {
            throw new IllegalArgumentException("n must be nonnegative");
        }
        if (n < 12) {
            int j = 1;
            while (j <= n) {
                nx *= j;
                ++j;
            }
            x = nx;
        }
        if (n >= 12) {
            x = Math.exp(ProbabilityMath.loggamma((double)n + 1.0));
        }
        return x;
    }

    public static double loggamma(double a) {
        double dlngam;
        double c0 = 0.0833333333333333;
        double c1 = -0.00277777777760991;
        double c2 = 7.9365066682539E-4;
        double c3 = -5.9520293135187E-4;
        double c4 = 8.37308034031215E-4;
        double c5 = -0.00165322962780713;
        double d = 0.418938533204673;
        if (a <= 0.8) {
            dlngam = ProbabilityMath.gamln1(a) - Math.log(a);
        } else if (a <= 2.25) {
            double t = a - 0.5 - 0.5;
            dlngam = ProbabilityMath.gamln1(t);
        } else if (a < 10.0) {
            int n = (int)(a - 1.25);
            double t = a;
            double w = 1.0;
            int i = 1;
            while (i <= n) {
                w = (t -= 1.0) * w;
                ++i;
            }
            dlngam = ProbabilityMath.gamln1(t - 1.0) + Math.log(w);
        } else {
            double t = 1.0 / a * (1.0 / a);
            double w = (((((c5 * t + c4) * t + c3) * t + c2) * t + c1) * t + c0) / a;
            dlngam = d + w + (a - 0.5) * (Math.log(a) - 1.0);
        }
        return dlngam;
    }

    static double gamln1(double a) {
        double result;
        double p0 = 0.577215664901533;
        double p1 = 0.844203922187225;
        double p2 = -0.168860593646662;
        double p3 = -0.780427615533591;
        double p4 = -0.402055799310489;
        double p5 = -0.0673562214325671;
        double p6 = -0.00271935708322958;
        double q1 = 2.88743195473681;
        double q2 = 3.12755088914843;
        double q3 = 1.56875193295039;
        double q4 = 0.361951990101499;
        double q5 = 0.0325038868253937;
        double q6 = 6.67465618796164E-4;
        double r0 = 0.422784335098467;
        double r1 = 0.848044614534529;
        double r2 = 0.565221050691933;
        double r3 = 0.156513060486551;
        double r4 = 0.017050248402265;
        double r5 = 4.97958207639485E-4;
        double s1 = 1.24313399877507;
        double s2 = 0.548042109832463;
        double s3 = 0.10155218743983;
        double s4 = 0.00713309612391;
        double s5 = 1.16165475989616E-4;
        if (a < 0.6) {
            double w = ((((((p6 * a + p5) * a + p4) * a + p3) * a + p2) * a + p1) * a + p0) / ((((((q6 * a + q5) * a + q4) * a + q3) * a + q2) * a + q1) * a + 1.0);
            result = (- a) * w;
        } else {
            double x = a - 0.5 - 0.5;
            double w = (((((r5 * x + r4) * x + r3) * x + r2) * x + r1) * x + r0) / (((((s5 * x + s4) * x + s3) * x + s2) * x + s1) * x + 1.0);
            result = x * w;
        }
        return result;
    }

    public static double combin(int N, int k) {
        double result = Math.exp(ProbabilityMath.loggamma(N + 1) - ProbabilityMath.loggamma(k + 1) - ProbabilityMath.loggamma(N - k + 1));
        return result;
    }

    public static double getNormalPDF(double x, double mean, double variance) {
        double pow = Math.pow(x - mean, 2.0) / (2.0 * variance);
        double e = Math.pow(2.718281828459045, pow);
        return e / Math.sqrt(6.283185307179586 * variance);
    }

    public static double getNormalPDF(double x) {
        return ProbabilityMath.getNormalPDF(x, 0.0, 1.0);
    }
}

