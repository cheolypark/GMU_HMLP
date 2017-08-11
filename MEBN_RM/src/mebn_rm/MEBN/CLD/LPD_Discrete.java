/*
 * Decompiled with CFR 0_118.
 */
package mebn_rm.MEBN.CLD; 
import java.util.HashMap;
import java.util.Map;

import edu.cmu.tetrad.bayes.BayesIm;
import mebn_rm.MEBN.CLD.CLD; 

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

