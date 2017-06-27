/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  mebn_rm.MEBN.CLD.CLD
 *  mebn_rm.MEBN.CLD.Categorical
 *  mebn_rm.MEBN.CLD.ConditionalGaussian
 *  mebn_rm.MEBN.MFrag.MFrag
 *  mebn_rm.MEBN.MNode.MNode
 *  mebn_rm.MEBN.MTheory.MTheory
 */
package mebn_ln.test.steel_plate_KEIT;

import java.io.PrintStream;
import java.util.List;
import mebn_ln.probabilistic_rule.ProbabilisticRules;
import mebn_rm.MEBN.CLD.CLD;
import mebn_rm.MEBN.CLD.Categorical;
import mebn_rm.MEBN.CLD.ConditionalGaussian;
import mebn_rm.MEBN.MFrag.MFrag;
import mebn_rm.MEBN.MNode.MNode;
import mebn_rm.MEBN.MTheory.MTheory;

public class Rules_steel_plates_KEIT
extends ProbabilisticRules {
    public static MTheory setRules(MTheory m) {
        MFrag tablea_MFrag = m.getMFrag("tablea");
        tablea_MFrag.setCausality("X_Minimum", new String[]{"Types_of_Steel_Plates_Faults"});
        tablea_MFrag.setCausality("X_Maximum", new String[]{"Types_of_Steel_Plates_Faults"});
        tablea_MFrag.setCausality("Y_Minimum", new String[]{"Types_of_Steel_Plates_Faults"});
        tablea_MFrag.setCausality("Y_Maximum", new String[]{"Types_of_Steel_Plates_Faults"});
        tablea_MFrag.setCausality("Pixels_Areas", new String[]{"Types_of_Steel_Plates_Faults"});
        tablea_MFrag.setCausality("X_Perimeter", new String[]{"Types_of_Steel_Plates_Faults"});
        tablea_MFrag.setCausality("Y_Perimeter", new String[]{"Types_of_Steel_Plates_Faults"});
        tablea_MFrag.setCausality("Sum_of_Luminosity", new String[]{"Types_of_Steel_Plates_Faults"});
        tablea_MFrag.setCausality("Minimum_of_Luminosity", new String[]{"Types_of_Steel_Plates_Faults"});
        tablea_MFrag.setCausality("Maximum_of_Luminosity", new String[]{"Types_of_Steel_Plates_Faults"});
        tablea_MFrag.setCausality("Length_of_Conveyer", new String[]{"Types_of_Steel_Plates_Faults"});
        tablea_MFrag.setCausality("TypeOfSteel", new String[]{"Types_of_Steel_Plates_Faults"});
        tablea_MFrag.setCausality("Steel_Plate_Thickness", new String[]{"Types_of_Steel_Plates_Faults"});
        tablea_MFrag.setCausality("Edges_Index", new String[]{"Types_of_Steel_Plates_Faults"});
        tablea_MFrag.setCausality("Empty_Index", new String[]{"Types_of_Steel_Plates_Faults"});
        tablea_MFrag.setCausality("Square_Index", new String[]{"Types_of_Steel_Plates_Faults"});
        tablea_MFrag.setCausality("Outside_X_Index", new String[]{"Types_of_Steel_Plates_Faults"});
        tablea_MFrag.setCausality("Edges_X_Index", new String[]{"Types_of_Steel_Plates_Faults"});
        tablea_MFrag.setCausality("Edges_Y_Index", new String[]{"Types_of_Steel_Plates_Faults"});
        tablea_MFrag.setCausality("Outside_Global_Index", new String[]{"Types_of_Steel_Plates_Faults"});
        tablea_MFrag.setCausality("LogOfAreas", new String[]{"Types_of_Steel_Plates_Faults"});
        tablea_MFrag.setCausality("Log_X_Index", new String[]{"Types_of_Steel_Plates_Faults"});
        tablea_MFrag.setCausality("Log_Y_Index", new String[]{"Types_of_Steel_Plates_Faults"});
        tablea_MFrag.setCausality("Orientation_Index", new String[]{"Types_of_Steel_Plates_Faults"});
        tablea_MFrag.setCausality("Luminosity_Index", new String[]{"Types_of_Steel_Plates_Faults"});
        tablea_MFrag.setCausality("SigmoidOfAreas", new String[]{"Types_of_Steel_Plates_Faults"});
        tablea_MFrag.setCausality("SigmoidOfAreas", new String[]{"Types_of_Steel_Plates_Faults"});
        tablea_MFrag.setNonCorrelation("TypeOfSteel", new String[]{"X_Minimum", "X_Maximum", "Y_Minimum", "Y_Maximum", "Pixels_Areas", "X_Perimeter", "Y_Perimeter", "Sum_of_Luminosity", "Minimum_of_Luminosity", "Maximum_of_Luminosity", "Length_of_Conveyer", "Steel_Plate_Thickness", "Edges_Index", "Empty_Index", "Square_Index", "Outside_X_Index", "Edges_X_Index", "Edges_Y_Index", "Outside_Global_Index", "LogOfAreas", "Log_X_Index", "Log_Y_Index", "Orientation_Index", "Luminosity_Index", "SigmoidOfAreas"});
        for (MNode n : tablea_MFrag.getMNodes()) {
            System.out.println((Object)n);
            if (n.isContinuous()) {
                n.setCLDs(new CLD[]{new ConditionalGaussian()});
            } else if (n.isDiscrete()) {
                n.setCLDs(new CLD[]{new Categorical()});
            }
            System.out.println((Object)n);
        }
        return m;
    }
}

