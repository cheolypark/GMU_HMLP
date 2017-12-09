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
package mebn_ln.converter;
 
import edu.cmu.tetrad.data.ColtDataSet;
import edu.cmu.tetrad.data.ContinuousVariable;
import edu.cmu.tetrad.data.DiscreteVariable;
import edu.cmu.tetrad.graph.Node;
import util.TempMathFunctions;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException; 
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader; 

public class ConverterColtDataSet {
    public static ColtDataSet getTetDataSetFromCSV(String fCSV) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        return ConverterColtDataSet.getTetDataSetFromCSV(fCSV, nodes);
    }

    public static ColtDataSet getTetDataSetFromCSV(String fCSV, List<Node> nodes) {
        ColtDataSet dataset = null;
        CSVReader reader = null;
        try {
            reader = new CSVReader((Reader)new FileReader(fCSV));
        }
        catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        ArrayList<String> colName = new ArrayList<String>();
        try {
            int row = 0;
            List<String[]> list = reader.readAll();
            for (String[] nextLine : list) {
                int col;
                String str;
                if (row == 0) {
                    col = 0;
                    while (col < nextLine.length) {
                        str = nextLine[col];
                        colName.add(str);
                        ++col;
                    }
                } else if (row == 1) {
                    col = 0;
                    while (col < nextLine.length) {
                        
                        str = nextLine[col];
                        if (TempMathFunctions.isNum((String)str)) {
                        	ContinuousVariable gn;
                            gn = new ContinuousVariable((String)colName.get(col));
                            nodes.add((Node)gn);
                        } else {
                        	DiscreteVariable gn;
                            gn = new DiscreteVariable((String)colName.get(col));
                            nodes.add((Node)gn);
                        }
                        ++col;
                    }
                    int rowSize = list.size();
                    dataset = new ColtDataSet(rowSize - 1, nodes);
                    int col2 = 0;
                    while (col2 < nextLine.length) {
                        dataset.setObject(row - 1, col2, (Object)nextLine[col2]);
                        ++col2;
                    }
                } else {
                    col = 0;
                    while (col < nextLine.length) {
                        dataset.setObject(row - 1, col, (Object)nextLine[col]);
                        ++col;
                    }
                }
                System.out.println(row);
                ++row;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return dataset;
    }
}

