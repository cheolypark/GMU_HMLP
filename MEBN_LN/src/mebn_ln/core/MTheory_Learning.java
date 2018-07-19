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
package mebn_ln.core;
 
import mebn_ln.core.Learning_Common;
import mebn_ln.core.MFrag_Learning;
import mebn_rm.MEBN.MFrag.MFrag;
import mebn_rm.MEBN.MTheory.MRoot;
import mebn_rm.MEBN.MTheory.MTheory;
import util.SortableValueMap; 

/**
 * MTheory_Learning is the class for MTheory learning. 
 * <p>
 * 
 * @author      Cheol Young Park
 * @version     0.0.1
 * @since       1.5
 */

public class MTheory_Learning extends Learning_Common {
    MFrag_Learning MFrag_learning = new MFrag_Learning();
    public static boolean structure_learning = true;

    public void run(MRoot mRoot) {
        getCandidateMTheories(mRoot.mtheoryCANs);
        System.out.println("******************* Begin MTheory learning with an MRoot *******************");
 
        run_operation(mRoot.mtheoryCANs);
    }

    public void run_operation(SortableValueMap<MTheory, Double> mtheoryCANs) {
        for (MTheory m : mtheoryCANs.keySet()) {
            for (MFrag f : m.mfrags.keySet()) {
                if (f.mFragType == MFrag.MFragType.COMMON)  { // For MFragType.REFERENCE, we don't perform machine learning. 
                	MFrag_learning.run((MFrag)f);
                }
            }
        }
        
        for (MTheory m : mtheoryCANs.keySet()) {
            Object f;
            if (!isMC_Approach()) continue;
            f = m.getAvgLogMFragScore();
        }
        
        for (MTheory m : mtheoryCANs.keySet()) {
            System.out.println("6 >>>>>>> " + m.name + " logMTheorySC : " + mtheoryCANs.get((Object)m));
        }
    }

    public void getCandidateMTheories(SortableValueMap<MTheory, Double> mtheoryCANs) {
    }
}

