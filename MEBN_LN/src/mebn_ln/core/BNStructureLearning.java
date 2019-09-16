/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  edu.cmu.tetrad.data.ColtDataSet
 *  edu.cmu.tetrad.data.ContinuousVariable
 *  edu.cmu.tetrad.data.DataSet
 *  edu.cmu.tetrad.data.DiscreteVariable
 *  edu.cmu.tetrad.data.Discretizer
 *  edu.cmu.tetrad.data.IKnowledge
 *  edu.cmu.tetrad.data.Knowledge
 *  edu.cmu.tetrad.graph.Edge
 *  edu.cmu.tetrad.graph.Graph
 *  edu.cmu.tetrad.graph.Node
 *  edu.cmu.tetrad.search.BDeScore
 *  edu.cmu.tetrad.search.Fges
 *  edu.cmu.tetrad.search.PatternToDag
 *  edu.cmu.tetrad.search.Score
 *  mebn_rm.MEBN.MFrag.MFrag
 *  mebn_rm.util.SortableValueMap
 */
package mebn_ln.core;

import edu.cmu.tetrad.data.ColtDataSet;
import edu.cmu.tetrad.data.ContinuousVariable;
import edu.cmu.tetrad.data.DataSet;
import edu.cmu.tetrad.data.DiscreteVariable;
import edu.cmu.tetrad.data.Discretizer;
import edu.cmu.tetrad.data.IKnowledge;
import edu.cmu.tetrad.data.Knowledge;
import edu.cmu.tetrad.graph.Edge;
import edu.cmu.tetrad.graph.Graph;
import edu.cmu.tetrad.graph.Node;
import edu.cmu.tetrad.search.BDeScore;
import edu.cmu.tetrad.search.Fges;
import edu.cmu.tetrad.search.PatternToDag;
import edu.cmu.tetrad.search.Score;
import java.io.IOException; 
import java.util.ArrayList;
import java.util.List; 
import mebn_ln.converter.ConverterColtDataSet;
import mebn_rm.MEBN.MFrag.MFrag; 

public class BNStructureLearning {
    public static void run(String fCSV, MFrag f) throws IOException {
        List<String> listParents;
        ArrayList<Node> nodes = new ArrayList<Node>();
        ColtDataSet dataset = ConverterColtDataSet.getTetDataSetFromCSV(fCSV, nodes);
        Discretizer discretizer = new Discretizer((DataSet)dataset);
        discretizer.setVariablesCopied(true);
        for (Node n : nodes) {
            if (n instanceof ContinuousVariable) {
                discretizer.equalIntervals(n, 3);
                continue;
            }
            if (!(n instanceof DiscreteVariable)) continue;
            discretizer.notDiscretized(n);
        }
        DataSet discretized = discretizer.discretize();
        Knowledge k = new Knowledge();
        for (String child2 : f.mapCausality.keySet()) {
            listParents = (List)f.mapCausality.get((Object)child2);
            for (String parent : listParents) {
                k.setRequired(parent, child2);
            }
        }
        for (String child2 : f.mapNonCorrelation.keySet()) {
            listParents = (List)f.mapNonCorrelation.get((Object)child2);
            for (String parent : listParents) {
                k.setForbidden(parent, child2);
            }
        } 
        
        BDeScore score = new BDeScore(discretized);
        score.setSamplePrior(1.0);
        score.setStructurePrior(10.0);
        Fges ges = new Fges((Score)score);
        ges.setKnowledge((IKnowledge)k);
        ges.setVerbose(false);
        ges.setNumPatternsToStore(5);
        ges.setFaithfulnessAssumed(false);
        Graph graph_learned = ges.search();
        PatternToDag search = new PatternToDag(graph_learned);
        Graph graph_learned2 = search.patternToDagMeek();
        f.mapCausality.clear();
        f.mapCorrelation.clear();
        f.mapNonCorrelation.clear();
        for (Edge e : graph_learned2.getEdges()) {
            Node n1 = e.getNode1();
            Node n2 = e.getNode2();
            f.setCausality(n2.getName(), new String[]{n1.getName()});
        } 
    }
}

