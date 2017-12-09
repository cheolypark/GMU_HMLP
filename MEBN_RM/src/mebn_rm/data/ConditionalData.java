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

public class ConditionalData
implements Serializable {
    private static final long serialVersionUID = 2088114453116034965L;
    public ArrayList<String> arrayParent = new ArrayList<String>();
    public String Y;

    public ConditionalData() {
    }

    public ConditionalData(String ... rvs) {
        boolean b = true;
        String[] arrstring = rvs;
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String rv = arrstring[n2];
            if (b) {
                Y = rv;
                b = false;
            } else {
                arrayParent.add(rv);
            }
            ++n2;
        }
    }

    public void setY(String y) {
        Y = y;
    }

    public Integer getParentSize() {
        return arrayParent.size();
    }

    public String toString() {
        return String.valueOf(Y) + "|" + arrayParent.toString();
    }

    public boolean hasEmptyParents() {
        if (arrayParent.size() > 0) {
            return false;
        }
        return true;
    }

    public boolean equal(ConditionalData cd) {
        if (!Y.equalsIgnoreCase(cd.Y)) {
            return false;
        }
        if (arrayParent.size() != cd.arrayParent.size()) {
            return false;
        }
        int count = 0;
        for (String p1 : arrayParent) {
            for (String p2 : cd.arrayParent) {
                if (!p1.equalsIgnoreCase(p2)) continue;
                ++count;
            }
        }
        if (arrayParent.size() != count) {
            return false;
        }
        return true;
    }
}

