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
package mebn_ln.probabilistic_rule;
 
import mebn_rm.MEBN.MFrag.MFrag;
import mebn_rm.MEBN.MNode.MNode;
import mebn_rm.MEBN.MTheory.MTheory; 

public class ProbabilisticRules {
    public MTheory mTheory = null;

    public void run(MTheory m) {
        mTheory = m;
    }

    public void setParents(String child, String ... parents) {
        String mFrag = child.substring(0, child.indexOf("."));
        MFrag f = mTheory.getMFrag(mFrag); 
        MNode childMNode = mTheory.getMNode(child);
        String[] arrstring = parents;
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            MNode parMNode;
            String par = arrstring[n2];
            int index = par.indexOf(".");
            if (index >= 0) {
                parMNode = mTheory.getMNode(par);
                childMNode.setInputParents(new MNode[]{parMNode});
            } else {
                parMNode = f.getMNode(par);
                childMNode.setParents(new MNode[]{parMNode});
            }
            ++n2;
        }
    }
}

