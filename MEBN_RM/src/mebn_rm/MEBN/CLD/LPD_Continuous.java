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

import edu.cmu.tetrad.sem.Scorer;
import mebn_rm.MEBN.CLD.CLD; 

public class LPD_Continuous extends CLD {
	public Map<String, Scorer> ipcScorers = new HashMap<String, Scorer>();
    public Scorer defaultScorer = null;
    
    public LPD_Continuous() {
        super("", "LPD_Continuous");
    }

    public LPD_Continuous(String name, String type) {
        super(name, type);
    } 
}

