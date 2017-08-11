/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  edu.cmu.tetrad.data.DataSet
 *  edu.cmu.tetrad.graph.EdgeListGraph
 *  edu.cmu.tetrad.graph.Graph
 *  edu.cmu.tetrad.graph.Node
 *  edu.cmu.tetrad.sem.Scorer
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

