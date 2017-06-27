/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  edu.cmu.tetrad.bayes.BayesPm
 *  edu.cmu.tetrad.bayes.DirichletBayesIm
 *  edu.cmu.tetrad.bayes.DirichletEstimator
 *  edu.cmu.tetrad.data.DataSet
 *  edu.cmu.tetrad.graph.Dag
 *  edu.cmu.tetrad.graph.Graph
 */
package mebn_ln.bn_parameter_learning;

import edu.cmu.tetrad.bayes.BayesPm;
import edu.cmu.tetrad.bayes.DirichletBayesIm;
import edu.cmu.tetrad.bayes.DirichletEstimator;
import edu.cmu.tetrad.data.DataSet;
import edu.cmu.tetrad.graph.Dag;
import edu.cmu.tetrad.graph.Graph;

public class BNParameterLearning {
    public static void run(DataSet discretized, Graph graph2) {
        Dag dag = new Dag(graph2);
        BayesPm bayesPm = new BayesPm((Graph)dag);
        DirichletBayesIm prior = DirichletBayesIm.symmetricDirichletIm((BayesPm)bayesPm, (double)0.5);
        DirichletBayesIm bayesIm = DirichletEstimator.estimate((DirichletBayesIm)prior, (DataSet)discretized);
    }
}

