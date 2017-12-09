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

import mebn_rm.util.ProbabilityMath;

public class NormalDistribution {
    private double mean;
    private double variance;

    public String toString() {
        String s = "mean:" + this.mean + " variance:" + this.variance;
        return s;
    }

    public NormalDistribution() {
        this.mean = 0.0;
        this.variance = 1.0;
    }

    public NormalDistribution(double mean, double variance) {
        this.mean = mean;
        this.variance = variance;
    }

    public double getCDFUpperBound(double probability) {
        double z = ProbabilityMath.inverseNormal(probability);
        double x = Math.sqrt(this.variance) * z + this.mean;
        return x;
    }

    public double getCDF(double x) {
        double z = (x - this.mean) / Math.sqrt(this.variance);
        double probability = ProbabilityMath.normalCdf(z);
        return probability;
    }

    public double getProbability(double ini, double end) {
        return this.getCDF(end) - this.getCDF(ini);
    }

    public double getProbability(double x) {
        return ProbabilityMath.getNormalPDF(x, this.mean, this.variance);
    }

    public double getMean() {
        return this.mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getVariance() {
        return this.variance;
    }

    public void setVariance(double variance) {
        this.variance = variance;
    }

    public static void main(String[] args) {
        NormalDistribution nd = new NormalDistribution(700.0, 300.0);
        nd.getCDFUpperBound(0.6);
    }
}

