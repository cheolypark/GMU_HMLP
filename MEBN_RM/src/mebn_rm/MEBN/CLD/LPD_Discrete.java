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
package mebn_rm.MEBN.CLD; 
import java.util.HashMap;
import java.util.Map;

import edu.cmu.tetrad.bayes.BayesIm;
import mebn_rm.MEBN.CLD.CLD; 


/**
 * LPD_Discrete is the class to perform functions related to a discrete random variable.
 * <p>
 * 
 * @author      Cheol Young Park
 * @version     0.0.1
 * @since       1.5
 */

public class LPD_Discrete extends CLD {
    Map<String, BayesIm> ipcIMs = new HashMap<String, BayesIm>();
    BayesIm ipcIMs_default = null;
    
    public LPD_Discrete() {
        super("", "LPD_Discrete");
    }

    public LPD_Discrete(String name, String type) {
        super(name, type);
    } 
}

