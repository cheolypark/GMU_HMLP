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
package mebn_rm.data;

import java.io.Serializable;
import java.util.ArrayList; 
import mebn_rm.MEBN.rv.RV;
import mebn_rm.data.ConditionalData;

public class ConditionalDataSet
implements Serializable {
    private static final long serialVersionUID = -1372300619947556069L;
    public ArrayList<ConditionalData> dataArray = new ArrayList<ConditionalData>();
    public ArrayList<RV> arrayRV = new ArrayList<RV>();

    public RV get(RV r) {
        for (RV d : arrayRV) {
            if (!d.name.equalsIgnoreCase(r.name) || !d.parents.toString().equalsIgnoreCase(r.parents.toString())) continue;
            return d;
        }
        return null;
    }

    public void addConditionalData(RV d) {
        arrayRV.add(d);
    }

    public void addConditionalData(String c, String ... ps) {
        RV cRV = new RV("Y", c);
        int i = 0;
        String[] arrstring = ps;
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String p = arrstring[n2];
            cRV.addParent(new RV("X" + i, p));
            ++n2;
        }
        arrayRV.add(cRV);
    }

    public void addConditionalData(ConditionalData d) {
        dataArray.add(d);
    }

    public ConditionalData setY(String y) {
        ConditionalData d = new ConditionalData();
        dataArray.add(d);
        d.Y = y;
        return d;
    }
 
    public String toString() {
        String s = "";
        Integer i = 1;
        for (RV d : arrayRV) {
            s = String.valueOf(s) + i + ":" + d.toString() + "\n";
            i = i + 1;
        }
        return s;
    }
}

