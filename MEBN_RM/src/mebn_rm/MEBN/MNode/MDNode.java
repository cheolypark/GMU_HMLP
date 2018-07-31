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

import java.util.List;
import mebn_rm.MEBN.MFrag.MFrag;
import mebn_rm.MEBN.MNode.MNode;
import mebn_rm.MEBN.MTheory.OVariable;

/**
 * MDNode is the class for a structure of discrete MNode.
 * <p>
 * 
 * @author      Cheol Young Park
 * @version     0.0.1
 * @since       1.5
 */

public class MDNode extends MNode {
	public MDNode(MFrag f, String name) {
        super(f, name); 
    }
	  
    public MDNode(MFrag f, String name, List<OVariable> ovs) {
        super(f, name, ovs, null); 
    }
    
    public MDNode(MFrag f, MNode mn, List<OVariable> ovs) {
        super(f, mn.name, ovs, null);
        setAttributeName(mn.getAttributeName());
    }

    public MDNode(MNode n) {
        super(n); 
    }
}

