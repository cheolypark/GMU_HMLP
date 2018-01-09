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
package mebn_rm.MEBN.rv;

import java.util.ArrayList;
import java.util.List;
import mebn_rm.MEBN.CLD.Probability;

/**
 * RV is the class for a random variable.
 * <p>
 * 
 * @author      Cheol Young Park
 * @version     0.0.1
 * @since       1.5
 */

public class RV extends Probability implements Comparable<RV> {
    public String name;
    public String value;
    public Double prob;
    public List<RV> parents = new ArrayList<RV>();

    public RV(String n, String v, Double p) {
        name = n;
        value = v;
        prob = p;
    }

    public RV(String n, String v) {
        name = n;
        value = v;
    }

    public String toString() {
        String s = "[";
        s = String.valueOf(s) + name + " = ";
        s = String.valueOf(s) + value + " ";
        if (!parents.isEmpty()) {
            s = String.valueOf(s) + " | ";
            for (RV rv : parents) {
                s = String.valueOf(s) + rv.name + " = ";
                s = String.valueOf(s) + rv.value + " ";
                s = String.valueOf(s) + ", ";
            }
            s = s.substring(0, s.length() - 2);
        }
        s = String.valueOf(s) + "]: " + prob;
        return s;
    }

    public void addParent(RV rv) {
        parents.add(rv);
    }

    public void addParents(RV ... rvs) {
        RV[] arrrV = rvs;
        int n = arrrV.length;
        int n2 = 0;
        while (n2 < n) {
            RV rv = arrrV[n2];
            parents.add(rv);
            ++n2;
        }
    } 
    
    public int compareTo(RV o) {
        return 0;
    }

    public String getParentValue(String parent) {
        if (!parents.isEmpty()) {
            for (RV rv : parents) {
                if (!parent.equalsIgnoreCase(rv.name)) continue;
                return rv.value;
            }
        }
        return null;
    }

    public ArrayList<String> getParents() {
        ArrayList<String> parentsString = new ArrayList<String>();
        if (!parents.isEmpty()) {
            for (RV rv : parents) {
                parentsString.add(rv.value);
            }
        }
        return parentsString;
    }
}

