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
import mebn_rm.data.ConditionalDataSet;

public class DataSet
implements Serializable {
    private static final long serialVersionUID = 8223089159543946185L;
    private ArrayList<ConditionalDataSet> arrayData = new ArrayList<ConditionalDataSet>();

    public DataSet(ConditionalDataSet ... cds) {
        ConditionalDataSet[] arrconditionalDataSet = cds;
        int n = arrconditionalDataSet.length;
        int n2 = 0;
        while (n2 < n) {
            ConditionalDataSet cd = arrconditionalDataSet[n2];
            add(cd);
            ++n2;
        }
    }

    public void add(ConditionalDataSet d) {
        arrayData.add(d);
    }

    public ConditionalDataSet get(int index) {
        return arrayData.get(index);
    }

    public String toString() {
        String s = "";
        Integer i = 1;
        for (ConditionalDataSet d : arrayData) {
            s = String.valueOf(s) + i + ":" + d.toString() + "\n";
            i = i + 1;
        }
        return s;
    }
}

