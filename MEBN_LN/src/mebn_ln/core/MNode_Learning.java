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
import mebn_rm.MEBN.CLD.CLD;
import mebn_rm.MEBN.MNode.MNode; 
import util.SortableValueMap; 

/**
 * MNode_Learning is the class for MNode learning. 
 * <p>
 * 
 * @author      Cheol Young Park
 * @version     0.0.1
 * @since       1.5
 */

public class MNode_Learning extends Learning_Common {
    public void run(MNode mn) {
        getCandidateMNodes(mn);
        System.out.println("******************* Begin MNode learning with the " + mn.name + " MNode *******************");
        System.out.println(mn.toString());
        run_operation(mn);
        System.out.println("******************* End MNode learning with the " + mn.name + " MNode *******************");
    }

    public void run_operation(MNode mn) {
    	System.gc();
    	
        SortableValueMap<CLD, Double> cldCANs = mn.cldCANs; 
        for (CLD c2 : cldCANs.keySet()) { 
            if (isMC_Approach()) continue;
            
            if (mn.name.equalsIgnoreCase("land_state_Dry")) {
            	System.out.println(mn.name);
            }
            
            c2.calculateBestPara();
        }
        for (CLD l : cldCANs.keySet()) {
            Double cldSc = 0.0;
            if (isMC_Approach()) {
                cldSc = l.getAvgLogParaScore();
            } else if (isMC_Approach()) {
                cldSc = (Double)cldCANs.get((Object)l) + l.getlogParaScore();
            } else if (isML_Approach()) {
                cldSc = 0.0;
            }
            cldCANs.put(l, cldSc);
        }
        for (CLD c2 : cldCANs.keySet()) {
            System.out.println("Prior logP(L of " + c2.name + ":" + c2.cld_type + "):  logCLDSC: " + cldCANs.get((Object)c2));
        }
    }

    public void getCandidateMNodes(MNode mn) {
    }
}

