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
package mebn_rm.util;
 
import java.util.Map; 

import util.SortableValueMap; 

public class ROC_FromSensitivityAndSpecificity {
    Double sens = 0.0;
    Double spec = 0.0;
    Double FPR = 0.0;
    Double FNR = 0.0;
    Double xUnit = 0.0;
    Double auc = 0.0;
    SortableValueMap<Double, Double> roc = null;

    public ROC_FromSensitivityAndSpecificity(Double Sensitivity, Double Specificity, Double u) {
        this.sens = Sensitivity;
        this.spec = Specificity;
        this.FPR = 1.0 - this.spec;
        this.FNR = 1.0 - this.sens;
        this.xUnit = u;
    }

    public Map<Double, Double> run() {
        this.roc = new SortableValueMap();
        Double pre = 0.0;
        Double i = 0.0;
        while (i <= 1.0 + this.xUnit) {
            Double p = this.sens * i + this.FPR * (1.0 - i);
            Double vp = this.sens * i / p;
            this.roc.put(i, vp);
            if (i != 0.0) {
                this.auc = this.auc + this.xUnit * (vp + pre);
            }
            pre = vp;
            i = i + this.xUnit;
        }
        this.auc = this.auc / 2.0;
        this.roc.sortByKey();
        return this.roc;
    }

    public Double Auc() {
        return this.auc;
    }

    public void print() {
        System.out.println("////////////////////////////////////////////////////");
        System.out.println("/// ROC ///");
        System.out.println("Sensitivity\t" + this.sens + "\tSpecificity\t" + this.spec);
        System.out.println("false positive rate\t" + this.FPR + "\tfalse negative rate\t" + this.FNR);
        System.out.println("X\tY");
        for (Double x : this.roc.keySet()) {
            Double y = this.roc.get(x);
            System.out.println(x + "\t" + y);
        }
        System.out.println();
        System.out.println("AUC = " + this.auc);
    }

    public static void main(String[] args) {
        ROC_FromSensitivityAndSpecificity roc = new ROC_FromSensitivityAndSpecificity(0.956521739, 0.590977444, 0.05);
        roc.run();
        roc.print();
        ROC_FromSensitivityAndSpecificity roc2 = new ROC_FromSensitivityAndSpecificity(1.0, 0.594066353, 0.05);
        roc2.run();
        roc2.print();
        ROC_FromSensitivityAndSpecificity roc3 = new ROC_FromSensitivityAndSpecificity(0.909090909, 0.58500401, 0.05);
        roc3.run();
        roc3.print();
        ROC_FromSensitivityAndSpecificity roc4 = new ROC_FromSensitivityAndSpecificity(0.3333333333333333, 0.5, 0.05);
        roc4.run();
        roc4.print();
    }
}

