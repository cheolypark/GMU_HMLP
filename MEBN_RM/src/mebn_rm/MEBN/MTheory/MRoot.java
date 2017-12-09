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
package mebn_rm.MEBN.MTheory;

import java.util.ArrayList; 
import mebn_rm.MEBN.MTheory.MTheory;
import util.SortableValueMap; 

public class MRoot
implements Comparable<MRoot> {
    public SortableValueMap<MTheory, Double> mtheoryCANs = new SortableValueMap<MTheory, Double>();
    public String name = "ROOT";

    public void setMTheories(MTheory ... mtheories) {
        ArrayList<MTheory> arrayMTheories = new ArrayList<MTheory>();
        MTheory[] arrmTheory = mtheories;
        int n = arrmTheory.length;
        int n2 = 0;
        while (n2 < n) {
            MTheory l = arrmTheory[n2];
            arrayMTheories.add(l);
            ++n2;
        }
        for (MTheory l : mtheoryCANs.keySet()) {
            arrayMTheories.add(l);
        }
        Integer i = 1;
        mtheoryCANs.clear();
        for (MTheory l2 : arrayMTheories) {
            l2.name = String.valueOf(name) + "_MTheory_" + i;
            mtheoryCANs.put(l2, Math.log(1.0 / (double)arrayMTheories.size()));
            i = i + 1;
        }
    }

    public String toString() {
        String s = "[" + name + " :";
        for (MTheory m : mtheoryCANs.keySet()) {
            s = String.valueOf(s) + m.toString() + ", ";
        }
        s = s.substring(0, s.length() - 2);
        s = String.valueOf(s) + "]";
        return s;
    }

    public ArrayList<Double> getlogMFragScores() {
        ArrayList<Double> logSCs = new ArrayList<Double>();
        for (MTheory m : mtheoryCANs.keySet()) {
            Double logSC = mtheoryCANs.get(m);
            logSCs.add(logSC);
        }
        return logSCs;
    }
 
    public int compareTo(MRoot o) {
        return 0;
    }

    public MTheory getMTheory(String name) {
        for (MTheory m : mtheoryCANs.keySet()) {
            if (name.isEmpty()) {
                return m;
            }
            if (!m.name.equalsIgnoreCase(name)) continue;
            return m;
        }
        return null;
    }
}

