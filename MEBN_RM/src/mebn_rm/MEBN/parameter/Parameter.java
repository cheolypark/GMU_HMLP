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
package mebn_rm.MEBN.parameter;

import java.util.ArrayList;
import mebn_rm.MEBN.rv.RV;
import util.SortableValueMap; 

public class Parameter
implements Comparable<Parameter> {
    public ArrayList<Double> parameters = new ArrayList();
    public boolean bTrueParameter = false;
    SortableValueMap<RV, Double> rvMap = new SortableValueMap();

    public Parameter() {
    }

    public /* varargs */ Parameter(boolean b, Double ... ds) {
        bTrueParameter = b;
        Double[] arrdouble = ds;
        int n = arrdouble.length;
        int n2 = 0;
        while (n2 < n) {
            Double d = arrdouble[n2];
            parameters.add(d);
            ++n2;
        }
    }

    public /* varargs */ Parameter(boolean b, RV ... rvs) {
        bTrueParameter = b;
        RV[] arrrV = rvs;
        int n = arrrV.length;
        int n2 = 0;
        while (n2 < n) {
            RV rv = arrrV[n2];
            rvMap.put(rv, 0.0);
            ++n2;
        }
    }

    public String toString() {
        String s = String.valueOf(bTrueParameter) + " [";
        for (Double d : parameters) {
            s = String.valueOf(s) + d + ", ";
        }
        s = s.substring(0, s.length() - 2);
        s = String.valueOf(s) + "]";
        return s;
    }
 
    public int compareTo(Parameter o) {
        for (Double d1 : o.parameters) {
            for (Double d2 : parameters) {
                if (d1 <= d2) continue;
                return -1;
            }
        }
        return 1;
    }
}

