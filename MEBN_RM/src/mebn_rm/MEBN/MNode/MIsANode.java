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
package mebn_rm.MEBN.MNode;
 
import mebn_rm.MEBN.MFrag.MFrag;
import mebn_rm.MEBN.MNode.MNode;
import mebn_rm.MEBN.MTheory.OVariable;

public class MIsANode
extends MNode {
    public MIsANode(MFrag f, OVariable o) {
        super(f, "IsA", null);
        ovs.add(o);
        f.arrayIsaContextNodes.add(this);
    }
 
    public String toString() {
        String s = name;
        OVariable ov = (OVariable)ovs.get(0);
        s = String.valueOf(s) + "(" + ov.name + ", " + ov.entityType + ")";
        return s;
    }
}

