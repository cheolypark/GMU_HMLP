/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  au.com.bytecode.opencsv.CSVReader
 *  edu.cmu.tetrad.data.ColtDataSet
 *  edu.cmu.tetrad.data.ContinuousVariable
 *  edu.cmu.tetrad.data.DiscreteVariable
 *  edu.cmu.tetrad.graph.Node
 *  mebn_rm.util.TempMathFunctions
 */
package mebn_ln.converter;

import au.com.bytecode.opencsv.CSVReader;
import edu.cmu.tetrad.data.ColtDataSet;
import edu.cmu.tetrad.data.ContinuousVariable;
import edu.cmu.tetrad.data.DiscreteVariable;
import edu.cmu.tetrad.graph.Node;
import util.TempMathFunctions;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List; 

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

