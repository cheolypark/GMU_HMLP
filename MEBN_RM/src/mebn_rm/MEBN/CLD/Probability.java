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
package mebn_rm.MEBN.CLD;

import java.util.ArrayList;

public class Probability {
    public String random(Double[] p, String ... s) {
        double r = Math.random();
        return random_operator(0, r, 0.0, p, s);
    }

    public String random_operator(int index, double r, double e, Double[] p, String ... s) {
        if (index == s.length) {
            return "";
        }
        double b = e;
        if (b < r && r <= (e += p[index].doubleValue())) {
            return s[index];
        }
        return random_operator(++index, r, e, p, s);
    }

    public String randomByUniform(ArrayList<String> s) {
        double r = Math.random();
        return randomByUniform_operator(0, r, s);
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
        return randomByUniform_operator(++index, r, s);
    }

    public String randomByUniform(String ... s) {
        double r = Math.random();
        return randomByUniform_operator(0, r, s);
    }

    public String randomByUniform_operator(int index, double r, String ... s) {
        if (index == s.length) {
            return "";
        }
        double b = ((double)index + 0.0) / (double)s.length;
        double e = ((double)index + 1.0) / (double)s.length;
        if (b < r && r <= e) {
            return s[index];
        }
        return randomByUniform_operator(++index, r, s);
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

