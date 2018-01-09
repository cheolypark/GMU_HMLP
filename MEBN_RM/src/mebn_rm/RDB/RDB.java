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
package mebn_rm.RDB;
 
import edu.cmu.tetrad.data.ColtDataSet;
import edu.cmu.tetrad.data.ContinuousVariable;
import edu.cmu.tetrad.data.DiscreteVariable;
import edu.cmu.tetrad.graph.Node;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;  
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;  
import mebn_rm.RDB.Bin;
import mebn_rm.RDB.MySQL_Interface;
import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
 
/**
 * RDB is the class for a relational database. This class stores several information
 * of the relational database such as Keys, Attributes, and Domain of Attributes. 
 * <p>
 * 
 * @author      Cheol Young Park
 * @version     0.0.1
 * @since       1.5
 */

public class RDB extends MySQL_Interface {
	
    public MultiMap mapTableAndKeys = new MultiHashMap();
    public MultiMap mapTableAndAttributes = new MultiHashMap();
    public MultiMap mapTableTypesAndTables = new MultiHashMap();
    public MultiMap mapChains = new MultiHashMap();
    public MultiMap mapChainsOfChains = new MultiHashMap();
    public Map<String, List<String>> mapDomainVaules = new HashMap<String, List<String>>();
    public Map<String, String> mapKeysOrigins = new HashMap<String, String>();
    public Map<String, String> mapAttributeTypes = new HashMap<String, String>();
    public Map<String, Bin> mapAttributeBins = new HashMap<String, Bin>();
    public int sizeOfBins = 5;
    public static RDB database = new RDB();
    
    public static RDB This() {
        return database;
    }

    public void init(String schema) throws SQLException {
        mapTableAndKeys.clear();
        mapTableAndAttributes.clear();
        mapTableTypesAndTables.clear();
        mapChains.clear();
        mapChainsOfChains.clear();
        mapDomainVaules.clear();
        mapKeysOrigins.clear();
        mapAttributeTypes.clear();
        mapAttributeBins.clear();
        connectSchema(schema);
        ResultSet rs = getTables();
        Integer tableIndex = 1;
        try { 
            while (rs.next()) {
                String tableName = rs.getString(3);
                Integer n = tableIndex;
                tableIndex = n + 1;
                initializeTableInformation(n, tableName);
            }
            rs.close();
        }
        
        catch (SQLException e) {
            e.printStackTrace();
        }
        initializePrimaryKeys();
        initializeAttributes();
    }

    public void initializeChains() {
        List<String> tables = (List)mapTableTypesAndTables.get((Object)"RelationshipTable");
        if (tables != null) {
            for (String t : tables) {
                searchChains(t);
            }
        }
    }

    public void initializeTableInformation(Integer index, String table) {
        ResultSet attributes = getColumn(table);
        int sizeAttributes = sizeOfPrimaryKeys(table);
        if (sizeAttributes == 1) {
            addTableTypesAndTables("EntityTable", table);
            System.out.println(index + ". Found an entity table [" + table + "]");
        } else {
            addTableTypesAndTables("RelationshipTable", table);
            System.out.println(index + ". Found an relationship table [" + table + "]");
        }
    }

    public void initializePrimaryKeys() {
        List<String> tables = (List)mapTableTypesAndTables.get((Object)"EntityTable");
        for (String t2 : tables) {
            ArrayList<String> keys = getPrimaryKeysToArray(t2);
            Map<String, String> importedColumns = getImportedColumn(t2);
            // This is pure entity table
            // e.g.,)
            // [Time] and no foreign key 
            //   t1
            //   t2
            //  ...
            if (importedColumns.isEmpty()) {
	            for (String key : keys) {
	                addTableAndKeys(t2, key, t2);
	            }
            } else {
            	// This is not pure entity table with a foreign key  
                // e.g.,)
                // [Time] -> [Passing Time] 
                //   t1
                //   t2
                //  ...
            	
            	for (String importedColumn : importedColumns.keySet()) {
                    String fkTable = importedColumns.get(importedColumn);
                    addTableAndKeys(t2, importedColumn, fkTable);
                }
            }
        }
        tables = (List)mapTableTypesAndTables.get((Object)"RelationshipTable");
        if (tables != null) {
            for (String t2 : tables) {
                Map<String, String> importedColumns = getImportedColumn(t2);
                for (String importedColumn : importedColumns.keySet()) {
                    String fkTable = importedColumns.get(importedColumn);
                    addTableAndKeys(t2, importedColumn, fkTable);
                }
            }
        }
        for (Object k : mapChains.keySet()) {
            List<String> chains = (List)mapChainsOfChains.get(k);
            for (String t3 : chains) {
                Map<String, String> importedColumns = getImportedColumn(t3);
                for (String importedColumn : importedColumns.keySet()) {
                    String fkTable = importedColumns.get(importedColumn);
                    addTableAndKeys((String)k, importedColumn, fkTable);
                }
            }
        }
    }

    public void initializeAttributes() throws SQLException {
        String attrName;
        List Keys;
        ResultSet rs;
        String attrType;
        List<String> tables = (List)mapTableTypesAndTables.get((Object)"EntityTable");
        for (String t2 : tables) {
            rs = getColumn(t2);
            try {
                while (rs.next()) {
                    attrName = rs.getString("COLUMN_NAME");
                    attrType = rs.getString("TYPE_NAME");
                    Keys = (List)mapTableAndKeys.get((Object)t2);
                    if (Keys.contains(attrName)) continue;
                    addTableAndAttributes(t2, attrName, attrType);
                }
                rs.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
            createValueDomain(t2);
        }
        tables = (List)mapTableTypesAndTables.get((Object)"RelationshipTable");
        if (tables == null) {
            return;
        }
        for (String t2 : tables) {
            rs = getColumn(t2);
            try {
                while (rs.next()) {
                    attrName = rs.getString("COLUMN_NAME");
                    attrType = rs.getString("TYPE_NAME");
                    Keys = (List)mapTableAndKeys.get((Object)t2);
                    if (Keys.contains(attrName)) continue;
                    addTableAndAttributes(t2, attrName, attrType);
                }
                rs.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
            createValueDomain(t2);
        }
        printClassInfo();
    }

    public void createValueDomain(String t) throws SQLException {
        List<String> attrs = (List)mapTableAndAttributes.get((Object)t);
        if (attrs != null) {
            for (String attr : attrs) {
                String attrType = mapAttributeTypes.get(attr);
                
                // 1. If continuous variables
                if (isRealValueType(attrType)) {
                    createBins(attr, t);
                    continue;
                }
                
                // 2. If discrete variables, add domain values (i.g., states) 
                ResultSet rs = getDomain(attr, t);
                boolean b = false;
                while (rs.next()) {
                    String d = rs.getString(attr);
                    setDomainVaule(attr, d);
                    b = true;
                }
                
                rs.close();
                
                if (b) continue;
                 
                // 3. If all of the above are not                 
                setDomainVaule(attr, "True");
                setDomainVaule(attr, "False");
            }
        }
    }

    public boolean isRealValueType(String s) {
        if (s.equalsIgnoreCase("float") || s.equalsIgnoreCase("doulbe")) {
            return true;
        }
        return false;
    }

    public void createBins(String attr, String table) throws SQLException {
        Double min = Double.MAX_VALUE;
        Double max = Double.MIN_VALUE;
        ResultSet rs = getDomain(attr, table);
        while (rs.next()) {
            String d = rs.getString(attr);
            Double cur = Double.valueOf(d);
            min = Math.min(cur, min);
            max = Math.max(cur, max);
        }
        rs.close();
        Bin bin = new Bin(min, max);
        mapAttributeBins.put(attr, bin);
        for (String strBin : bin.getAllBins()) {
            setDomainVaule(attr, strBin);
        }
    }

    public void setDomainVaule(String attr, String d) { 
        if (d == null || RDB.isNum(d)) {
            return;
        }
        List<String> domains = mapDomainVaules.get(attr);
        if (domains == null) {
            domains = new ArrayList<String>();
            mapDomainVaules.put(attr, domains);
        }
        if (!domains.contains(d)) {
            domains.add(d);
        }
    }

    public int getDomainIndex(String attr, String value) {
        List<String> domains = mapDomainVaules.get(attr);
        String attrType = mapAttributeTypes.get(attr);
        if (isRealValueType(attrType)) {
            String checkerNumOrBin = value.substring(0, 1);
            if (checkerNumOrBin.equals("_")) {
                return domains.indexOf(value);
            }
            Bin b = mapAttributeBins.get(attr);
            return b.getIndex(Double.valueOf(value));
        }
        return domains.indexOf(value);
    }

    public String getDomainValue(String attr, int index) {
        List<String> domains = mapDomainVaules.get(attr);
        return domains.get(index);
    }

    public void searchChains(String targetTable) {
        for (Object key : mapChains.keySet()) {
            if (targetTable.equalsIgnoreCase((String)key)) {
                return;
            }
            List tables = (List)mapChains.get(key);
            if (tables == null || !tables.contains(targetTable)) continue;
            return;
        }
        List<String> tables = (List)mapTableTypesAndTables.get((Object)"RelationshipTable");
        for (String t : tables) {
            boolean b;
            ArrayList<String> excepts;
            if (targetTable.equalsIgnoreCase(t) || !(b = IsConnectedOdd(t, targetTable, excepts = new ArrayList<String>()))) continue;
            ArrayList<String> chains = new ArrayList<String>();
            chains.add(targetTable);
            for (String connectedTable : excepts) {
                if (chains.contains(connectedTable)) continue;
                chains.add(connectedTable);
            }
            ArrayList<String> importedTables = getImportedTable(targetTable);
            for (String importedTable : importedTables) {
                if (chains.contains(importedTable)) continue;
                chains.add(importedTable);
            }
            String key2 = String.valueOf(targetTable) + "_" + t;
            addChains(key2, chains, targetTable, t);
            System.out.println("Chains: " + key2 + " " + chains);
        }
    }

    public boolean IsConnectedOdd(String t, String targetTable, List<String> excepts) {
        if (!excepts.contains(t)) {
            excepts.add(t);
        }
        boolean b = false;
        ArrayList<String> importedTables = getImportedTable(t);
        for (String importedTable : importedTables) {
            if (targetTable.equalsIgnoreCase(importedTable)) {
                return true;
            }
            if (excepts.contains(importedTable)) {
                return false;
            }
            b = IsConnectedEven(importedTable, targetTable, excepts);
            if (!b) continue;
            return true;
        }
        return b;
    }

    public boolean IsConnectedEven(String t, String targetTable, List<String> excepts) {
        if (!excepts.contains(t)) {
            excepts.add(t);
        }
        boolean b = false;
        ArrayList<String> exportedTables = getExportedTable(t);
        for (String exportedTable : exportedTables) {
            if (targetTable.equalsIgnoreCase(exportedTable)) {
                return true;
            }
            if (excepts.contains(exportedTable)) {
                return false;
            }
            b = IsConnectedOdd(exportedTable, targetTable, excepts);
            if (!b) continue;
            return true;
        }
        return b;
    }

    public void addTableAndKeys(String t, String k, String ori) {
        ArrayList v = (ArrayList)mapTableAndKeys.get((Object)t);
        if (v == null) {
            mapTableAndKeys.put((Object)t, (Object)k);
        } else if (!v.contains(k)) {
            mapTableAndKeys.put((Object)t, (Object)k);
        }
        mapKeysOrigins.put(t + "." + k, ori);
//        mapKeysOrigins.put(k, ori);
    }
    
    public String getOriginFromKey(String table, String k) {
    	String ori = "";
    	ori = mapKeysOrigins.get(table+"."+k);
    	return ori; 
    }

    public void addTableAndAttributes(String table, String attribute, String attributeType) {
        mapTableAndAttributes.put((Object)table, (Object)attribute);
        mapAttributeTypes.put(attribute, attributeType);
    }

    public void addTableTypesAndTables(String t, String b) {
        mapTableTypesAndTables.put((Object)t, (Object)b);
    }

    public boolean isEntityTable(String b) {
        ArrayList tables = (ArrayList)mapTableTypesAndTables.get((Object)"EntityTable");
        if (tables.contains(b)) {
            return true;
        }
        return false;
    }

    public boolean isRelationshipTable(String b) {
        ArrayList tables = (ArrayList)mapTableTypesAndTables.get((Object)"RelationshipTable");
        if (tables.contains(b)) {
            return true;
        }
        return false;
    }

    public void addChains(String nkey, List<String> tables, String c1, String c2) {
        for (String s : tables) {
            mapChains.put((Object)nkey, (Object)s);
        }
        mapChainsOfChains.put((Object)nkey, (Object)c1);
        mapChainsOfChains.put((Object)nkey, (Object)c2);
    }

    public ArrayList<String> getEntityTables() {
        return (ArrayList)mapTableTypesAndTables.get((Object)"EntityTable");
    }

    public ArrayList<String> getRelationshipTables() {
        return (ArrayList)mapTableTypesAndTables.get((Object)"RelationshipTable");
    }

    public ArrayList<String> getEntityTablesHavingAttrs() {
        ArrayList<String> list = (ArrayList)mapTableTypesAndTables.get((Object)"EntityTable");
        ArrayList<String> re = new ArrayList<String>();
        for (String t : list) {
            ArrayList attrs = (ArrayList)mapTableAndAttributes.get((Object)t);
            if (attrs == null) continue;
            re.add(t);
        }
        return re;
    }

    public ColtDataSet getRowDataSet(ArrayList<String> attrs, String strTables) throws SQLException {
        String strAttrs = trimArrayString(attrs);
        ResultSet rs = get(strAttrs, strTables);
        int sizeRow = sizeOfResultSet(rs);
        rs = get(strAttrs, strTables);
        
        return getDataSet(sizeRow, rs, attrs);
    }

    public ColtDataSet getNatualJoinedDataSet(ArrayList<String> importedTables, String strTables) throws SQLException {
        ResultSet rs = getNaturalJoin(importedTables, strTables);
        int sizeRow = sizeOfResultSet(rs);
        rs = getNaturalJoin(importedTables, strTables);
        
        ArrayList<String> attrs = new ArrayList<String>();
        ArrayList<String> vars = null;
        vars = (ArrayList)mapTableAndAttributes.get((Object)strTables);
        if (vars != null) {
            for (String v : vars) {
                attrs.add(v);
            }
        }
        for (String importedTable : importedTables) {
            vars = (ArrayList)mapTableAndAttributes.get((Object)importedTable);
            if (vars == null) continue;
            for (String v : vars) {
                attrs.add(v);
            }
        }
        return getDataSet(sizeRow, rs, attrs);
    }

    public ColtDataSet getConditionedJoinedDataSet(List<String> importedTables, String strTable) throws SQLException {
        String strOnArg = "";
        Map<String, String> importedColumns = getImportedColumn(strTable);
        if (importedColumns.size() > 0) {
            strOnArg = String.valueOf(strOnArg) + " on ";
        }
        int j = 0;
        for (String importedColumn : importedColumns.keySet()) {
            String importedT = importedColumns.get(importedColumn);
            if (j != 0) {
                strOnArg = String.valueOf(strOnArg) + " && ";
            }
            ++j;
            strOnArg = String.valueOf(strOnArg) + strTable + "." + importedColumn;
            Map<String, String> exportedColumns = getExportedColumn(importedT);
            for (String exportedColumn : exportedColumns.keySet()) {
                strOnArg = String.valueOf(strOnArg) + " = " + importedT + "." + exportedColumn;
            }
        }
        
        ResultSet rs = getJoin(null, null, null, null, null);
        int sizeRow = sizeOfResultSet(rs);
        rs = getJoin(null, null, null, null, null);
        		
        ArrayList<String> attrs = new ArrayList<String>();
        List<String> vars = null;
        vars = (List)mapTableAndAttributes.get((Object)strTable);
        if (vars != null) {
            for (String v : vars) {
                attrs.add(v);
            }
        }
        for (String importedTable : importedTables) {
            vars = (ArrayList)mapTableAndAttributes.get((Object)importedTable);
            if (vars == null) continue;
            for (String v : vars) {
                attrs.add(v);
            }
        }
        return getDataSet(sizeRow, rs, attrs);
    }

    public ColtDataSet getTetDataSetFromCSV(String fCSV) {
        List<Node> nodes = new ArrayList<Node>();
        ColtDataSet dataset = null;
                
        System.gc();
          
		try {
			Iterable<CSVRecord> records;
			
			boolean b = false;
			boolean b2 = false;
			CSVParser Parser;
			
			records = CSVFormat.RFC4180.parse(new FileReader(fCSV));

			int rowSize = 0;
			for (CSVRecord record : records) {
				rowSize++;
			}
			
			records = CSVFormat.RFC4180.parse(new FileReader(fCSV));
			
			HashMap<Integer, String> mapHeader = new HashMap<Integer, String>();
			int i = 0; 
			for (CSVRecord record : records) {
				// Headers
				if (!b) {
					b = true;
					for (int j = 0; j < record.size(); j++) {
						String column = record.get(j);
						if (!column.isEmpty()) {
//							System.out.println(column);
							mapHeader.put(j, column);
						}
					}
					 
				} 				
				// Data 
				else {
					if (!b2) {
						b2 = true;
						for (int j = 0; j < record.size(); j++) {
							String column = record.get(j);
							if (!column.isEmpty()) {
							    if (RDB.isNum(column)) {
		                        	ContinuousVariable gn;
		                            gn = new ContinuousVariable((String)mapHeader.get(j));
		                            nodes.add(gn);
		                        } else {
		                        	DiscreteVariable gn;
		                            gn = new DiscreteVariable((String)mapHeader.get(j));
		                            nodes.add(gn);
		                        }
							}								
						}
						
	                    dataset = new ColtDataSet(rowSize - 1, nodes);
					}
					
					
					for (int j = 0; j < record.size(); j++) {
						String column = record.get(j);
						if (!column.isEmpty()) {
//					        long timeTotal = System.nanoTime();
					        dataset.setObject(i - 1, j, (Object)column);
//		                    Long l = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - timeTotal);
						}
					}
				}
				
				i++;
			} 
		} catch (FileNotFoundException e) { 
			e.printStackTrace();
		} catch (IOException e) { 
			e.printStackTrace();
		}
          
        return dataset;
    }
     
    public String checkPreT2(String strTableC) {
        String ret = strTableC;
        Integer index = ret.indexOf("_pret");
        if (index < 0) {
            return strTableC;
        }
        ret = ret.substring(0, ret.length() - ret.indexOf("_pret"));
        return ret;
    }

    public Boolean checkPreT(String strTableC, String importedColumn) {
        String ret = strTableC;
        Integer index = ret.indexOf("_pret");
        if (index < 0) {
            if (importedColumn.equalsIgnoreCase("pret")) {
                return false;
            }
            return true;
        }
        ret = ret.substring(0, ret.length() - ret.indexOf("_pret"));
        if (importedColumn.equalsIgnoreCase("pret")) {
            return true;
        }
        return false;
    }

    public ResultSet getInnerJoinedDataSet(String curTable, List<String> tablesTarget, List<String> tablesAll, List<String> prevTables) throws SQLException {
        Map<String, String> importedColumns;
        String importedT;
        String strOnArg = "";
        int j = 0;
        ArrayList<String> repeatedT = new ArrayList<String>();
        for (String strTable : tablesAll) {
            repeatedT.add(strTable);
            importedColumns = getImportedColumn(strTable);
            for (String importedColumn : importedColumns.keySet()) {
                importedT = importedColumns.get(importedColumn);
                for (String strTableC : tablesAll) {
                    if (repeatedT.contains(strTableC) || !checkPreT(strTableC, importedColumn).booleanValue()) continue;
                    String strTableC_fillterd = checkPreT2(strTableC);
                    Map<String, String> importedColumnsC = getImportedColumn(strTableC_fillterd);
                    for (String importedColumnC : importedColumnsC.keySet()) {
                        String importedTC = importedColumnsC.get(importedColumnC);
                        System.out.println(String.valueOf(strTable) + "." + importedColumn + "[" + importedT + "]");
                        System.out.println(String.valueOf(strTableC_fillterd) + "." + importedColumnC + "[" + importedTC + "]");
                        if (!importedT.equals(importedTC)) continue;
                        if (j != 0) {
                            strOnArg = String.valueOf(strOnArg) + " && \n";
                        }
                        ++j;
                        strOnArg = String.valueOf(strOnArg) + strTable + "." + importedColumn;
                        strOnArg = String.valueOf(strOnArg) + " = " + strTableC + "." + importedColumnC;
                        System.out.println(strOnArg);
                    }
                }
            }
        }
        if (!prevTables.isEmpty()) {
            for (String strTableC : prevTables) {
                strOnArg = String.valueOf(strOnArg) + " && \n";
                strOnArg = String.valueOf(strOnArg) + "predecessor.pret = pre_" + strTableC + ".t" + "\n";
                importedColumns = getImportedColumn(strTableC);
                for (String importedColumn : importedColumns.keySet()) {
                    importedT = importedColumns.get(importedColumn);
                    if (importedT.equalsIgnoreCase("Time")) continue;
                    strOnArg = String.valueOf(strOnArg) + " && \n";
                    strOnArg = String.valueOf(strOnArg) + strTableC + "." + importedColumn + " = pre_" + strTableC + "." + importedColumn + "\n";
                }
            }
        }
        ResultSet rs = getJoin(curTable, tablesTarget, tablesAll, prevTables, strOnArg);
        return rs;
    }

    public ResultSet getInnerJoinedDataSet2(String curTable, List<String> tablesTarget, List<String> tablesAll, List<String> prevTables) throws SQLException {
        Map<String, String> importedColumns;
        String importedT;
        String strOnArg = "";
        int j = 0;
        ArrayList<String> repeatedT = new ArrayList<String>();
        for (String strTable : tablesAll) {
            repeatedT.add(strTable);
            importedColumns = getImportedColumn(strTable);
            for (String importedColumn : importedColumns.keySet()) {
                importedT = importedColumns.get(importedColumn);
                for (String strTableC : tablesAll) {
                    if (repeatedT.contains(strTableC) || !checkPreT(strTableC, importedColumn).booleanValue()) continue;
                    String strTableC_fillterd = checkPreT2(strTableC);
                    Map<String, String> importedColumnsC = getImportedColumn(strTableC_fillterd);
                    for (String importedColumnC : importedColumnsC.keySet()) {
                        String importedTC = importedColumnsC.get(importedColumnC);
                        System.out.println(String.valueOf(strTable) + "." + importedColumn + "[" + importedT + "]");
                        System.out.println(String.valueOf(strTableC_fillterd) + "." + importedColumnC + "[" + importedTC + "]");
                        if (!importedT.equals(importedTC)) continue;
                        if (j != 0) {
                            strOnArg = String.valueOf(strOnArg) + " && \n";
                        }
                        ++j;
                        strOnArg = String.valueOf(strOnArg) + strTable + "." + importedColumn;
                        strOnArg = String.valueOf(strOnArg) + " = " + strTableC + "." + importedColumnC;
                        System.out.println(strOnArg);
                    }
                }
            }
        }
        if (!prevTables.isEmpty()) {
            for (String strTableC : prevTables) {
                strOnArg = String.valueOf(strOnArg) + " && \n";
                strOnArg = String.valueOf(strOnArg) + "predecessor.pret = pre_" + strTableC + ".t" + "\n";
                importedColumns = getImportedColumn(strTableC);
                for (String importedColumn : importedColumns.keySet()) {
                    importedT = importedColumns.get(importedColumn);
                    if (importedT.equalsIgnoreCase("Time")) continue;
                    strOnArg = String.valueOf(strOnArg) + " && \n";
                    strOnArg = String.valueOf(strOnArg) + strTableC + "." + importedColumn + " = pre_" + strTableC + "." + importedColumn + "\n";
                }
            }
        }
        ResultSet rs = getJoin(curTable, tablesTarget, tablesAll, prevTables, strOnArg);
        return rs;
    }

    public void createDefaultPriorDataset(List<Node> nodes, int nodeIndex, ArrayList<String> tempList, ArrayList<ArrayList<String>> result) {
        if (nodes.size() == nodeIndex) {
            ArrayList<String> l = new ArrayList<String>();
            for (String domain : tempList) {
                l.add(domain);
            }
            result.add(l);
            return;
        }
        Node curNode = nodes.get(nodeIndex++);
        List<String> listDomain = mapDomainVaules.get(curNode.getName());
        for (String domain : listDomain) {
            tempList.add(domain);
            createDefaultPriorDataset(nodes, nodeIndex, tempList, result);
            tempList.remove(nodeIndex - 1);
        }
    }

    public ColtDataSet getDataSetWithDefaultPrior(ResultSet rs, List<String> attrs) throws SQLException {
        ArrayList<Node> nodes = new ArrayList<Node>();
        ColtDataSet dataset = null;
        int i = 0;
        for (String s : attrs) {
            DiscreteVariable gn = new DiscreteVariable(s);
            nodes.add((Node)gn);
        }
        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        ArrayList<String> tempList = new ArrayList<String>();
        createDefaultPriorDataset(nodes, 0, tempList, result);
        dataset = new ColtDataSet(sizeOfResultSet(rs) + result.size(), nodes);
        for (ArrayList<String> dataRow : result) {
            int j = 0;
            for (String aData : dataRow) {
                dataset.setObject(i, j, (Object)getDomainIndex(nodes.get(j).getName(), aData));
                ++j;
            }
            ++i;
        }
        try {
            while (rs.next()) {
                int j = 0;
                for (String attr : attrs) {
                    dataset.setObject(i, j, (Object)getDomainIndex(attr, rs.getString(attr)));
                    ++j;
                }
                ++i;
            }
            
            rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return dataset;
    }

    public ColtDataSet getDataSet(int sizeRow, ResultSet rs, List<String> attrs) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        ColtDataSet dataset = null;
        int i = 0;
        for (String s : attrs) {
            DiscreteVariable gn = new DiscreteVariable(s);
            nodes.add(gn);
        }
        dataset = new ColtDataSet(sizeRow, nodes);
        try {
            while (rs.next()) {
                int j = 0;
                for (String attr : attrs) {
                    System.out.println(String.valueOf(attr) + " " + rs.getString(attr) + " " + " " + getDomainIndex(attr, rs.getString(attr)));
                    dataset.setObject(i, j, (Object)getDomainIndex(attr, rs.getString(attr)));
                    ++j;
                }
                ++i;
            }
            
            rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return dataset;
    }

    public void printClassInfo() {
        Object a;
        System.out.println("Tables' Types");
        for (Object o22222 : mapTableTypesAndTables.keySet()) {
            a = (ArrayList)mapTableTypesAndTables.get(o22222);
            System.out.println("\t" + o22222 + ":" + a);
        }
        System.out.println("Tables' Keys");
        for (Object o : mapTableAndKeys.keySet()) {
            a = (ArrayList)mapTableAndKeys.get(o);
            System.out.println("\t" + o + ":" + a);
        }
        System.out.println("Tables' Attributes");
        for (Object o2 : mapTableAndAttributes.keySet()) {
            String attrType = mapAttributeTypes.get(o2);
            ArrayList a2 = (ArrayList)mapTableAndAttributes.get(o2);
            System.out.println("\t" + o2 + ":" + a2 + "[" + attrType + "]");
        }
        System.out.println("Tables' Chains");
        for (Object o3 : mapChains.keySet()) {
            a = (ArrayList)mapChains.get(o3);
            System.out.println("\t" + o3 + ":" + a);
        }
        System.out.println("Tables' Chains of Chains");
        for (Object o4 : mapChainsOfChains.keySet()) {
            a = (ArrayList)mapChainsOfChains.get(o4);
            System.out.println("\t" + o4 + ":" + a);
        }
        System.out.println("Keys' Origins");
        for (Object o5 : mapKeysOrigins.keySet()) {
            a = mapKeysOrigins.get(o5);
            System.out.println("\t" + o5 + ":" + (String)a);
        }
    }

    public String trimArrayString(ArrayList<String> listAttrs) {
        String strAttrs = listAttrs.toString();
        strAttrs = strAttrs.replace("[", "");
        strAttrs = strAttrs.replace("]", "");
        return strAttrs;
    }

    public String orString(String s) {
        return s.replace(",", " or ");
    }

    public String changeArrayString(ArrayList<String> listAttrs) {
        String strAttrs = trimArrayString(listAttrs);
        strAttrs = strAttrs.replace(",", "_");
        strAttrs = strAttrs.replace(" ", "");
        return strAttrs;
    }

    public String changeMapString(String str, ArrayList<String> listAttrs) {
        return String.valueOf(str) + "_" + changeArrayString(listAttrs);
    }
}

