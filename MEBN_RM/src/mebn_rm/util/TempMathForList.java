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

