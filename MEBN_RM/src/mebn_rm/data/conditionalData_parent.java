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

public class conditionalData_parent
implements Serializable {
    private static final long serialVersionUID = -1808310324002374246L;
    private ArrayList<String> arrayParent = new ArrayList<String>();

    public void add(ArrayList<String> s) {
        arrayParent.addAll(s);
    }

    public ArrayList<String> getArray() {
        return arrayParent;
    }

    public String toString() {
        return arrayParent.toString();
    }

    public Boolean equal(conditionalData_parent cdp) {
        int i = 0;
        while (i < arrayParent.size()) {
            if (!arrayParent.get(i).equalsIgnoreCase(cdp.arrayParent.get(i))) {
                return false;
            }
            ++i;
        }
        return true;
    }
}

