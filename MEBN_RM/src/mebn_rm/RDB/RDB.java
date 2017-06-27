/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  au.com.bytecode.opencsv.CSVReader
 *  edu.cmu.tetrad.data.ColtDataSet
 *  edu.cmu.tetrad.data.ContinuousVariable
 *  edu.cmu.tetrad.data.DiscreteVariable
 *  edu.cmu.tetrad.graph.Node
 *  org.apache.commons.collections.MultiHashMap
 *  org.apache.commons.collections.MultiMap
 */
package mebn_rm.RDB;

import au.com.bytecode.opencsv.CSVReader;
import edu.cmu.tetrad.data.ColtDataSet;
import edu.cmu.tetrad.data.ContinuousVariable;
import edu.cmu.tetrad.data.DiscreteVariable;
import edu.cmu.tetrad.graph.Node;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException; 
import java.io.Reader; 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map; 
import java.util.concurrent.TimeUnit;
import mebn_rm.RDB.Bin;
import mebn_rm.RDB.MySQL_Interface;
import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.collections.MultiMap;

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
        this.mapTableAndKeys.clear();
        this.mapTableAndAttributes.clear();
        this.mapTableTypesAndTables.clear();
        this.mapChains.clear();
        this.mapChainsOfChains.clear();
        this.mapDomainVaules.clear();
        this.mapKeysOrigins.clear();
        this.mapAttributeTypes.clear();
        this.mapAttributeBins.clear();
        this.connectSchema(schema);
        ResultSet rs = this.getTables();
        Integer tableIndex = 1;
        try {
            rs.beforeFirst();
            while (rs.next()) {
                String tableName = rs.getString(3);
                Integer n = tableIndex;
                tableIndex = n + 1;
                this.initializeTableInformation(n, tableName);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        this.initializePrimaryKeys();
        this.initializeAttributes();
    }

    public void initializeChains() {
        List<String> tables = (List)this.mapTableTypesAndTables.get((Object)"RelationshipTable");
        if (tables != null) {
            for (String t : tables) {
                this.searchChains(t);
            }
        }
    }

    public void initializeTableInformation(Integer index, String table) {
        ResultSet attributes = this.getColumn(table);
        int sizeAttributes = this.sizeOfPrimaryKeys(table);
        if (sizeAttributes == 1) {
            this.addTableTypesAndTables("EntityTable", table);
            System.out.println(index + ". Found an entity table [" + table + "]");
        } else {
            this.addTableTypesAndTables("RelationshipTable", table);
            System.out.println(index + ". Found an relationship table [" + table + "]");
        }
    }

    public void initializePrimaryKeys() {
        List<String> tables = (List)this.mapTableTypesAndTables.get((Object)"EntityTable");
        for (String t2 : tables) {
            ArrayList<String> keys = this.getPrimaryKeysToArray(t2);
            for (String key : keys) {
                this.addTableAndKeys(t2, key, t2);
            }
        }
        tables = (List)this.mapTableTypesAndTables.get((Object)"RelationshipTable");
        if (tables != null) {
            for (String t2 : tables) {
                Map<String, String> importedColumns = this.getImportedColumn(t2);
                for (String importedColumn : importedColumns.keySet()) {
                    String fkTable = importedColumns.get(importedColumn);
                    this.addTableAndKeys(t2, importedColumn, fkTable);
                }
            }
        }
        for (Object k : this.mapChains.keySet()) {
            List<String> chains = (List)this.mapChainsOfChains.get(k);
            for (String t3 : chains) {
                Map<String, String> importedColumns = this.getImportedColumn(t3);
                for (String importedColumn : importedColumns.keySet()) {
                    String fkTable = importedColumns.get(importedColumn);
                    this.addTableAndKeys((String)k, importedColumn, fkTable);
                }
            }
        }
    }

    public void initializeAttributes() throws SQLException {
        String attrName;
        List Keys;
        ResultSet rs;
        String attrType;
        List<String> tables = (List)this.mapTableTypesAndTables.get((Object)"EntityTable");
        for (String t2 : tables) {
            rs = this.getColumn(t2);
            try {
                while (rs.next()) {
                    attrName = rs.getString("COLUMN_NAME");
                    attrType = rs.getString("TYPE_NAME");
                    Keys = (List)this.mapTableAndKeys.get((Object)t2);
                    if (Keys.contains(attrName)) continue;
                    this.addTableAndAttributes(t2, attrName, attrType);
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
            this.createValueDomain(t2);
        }
        tables = (List)this.mapTableTypesAndTables.get((Object)"RelationshipTable");
        if (tables == null) {
            return;
        }
        for (String t2 : tables) {
            rs = this.getColumn(t2);
            try {
                while (rs.next()) {
                    attrName = rs.getString("COLUMN_NAME");
                    attrType = rs.getString("TYPE_NAME");
                    Keys = (List)this.mapTableAndKeys.get((Object)t2);
                    if (Keys.contains(attrName)) continue;
                    this.addTableAndAttributes(t2, attrName, attrType);
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
            this.createValueDomain(t2);
        }
        this.printClassInfo();
    }

    public void createValueDomain(String t) throws SQLException {
        List<String> attrs = (List)this.mapTableAndAttributes.get((Object)t);
        if (attrs != null) {
            for (String attr : attrs) {
                String attrType = this.mapAttributeTypes.get(attr);
                if (this.isRealValueType(attrType)) {
                    this.createBins(attr, t);
                    continue;
                }
                ResultSet rs = this.getDomain(attr, t);
                if (rs.first()) {
                    while (rs.next()) {
                        String d = rs.getString(attr);
                        this.setDomainVaule(attr, d);
                    }
                    continue;
                }
                this.setDomainVaule(attr, "True");
                this.setDomainVaule(attr, "False");
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
        ResultSet rs = this.getDomain(attr, table);
        while (rs.next()) {
            String d = rs.getString(attr);
            Double cur = Double.valueOf(d);
            min = Math.min(cur, min);
            max = Math.max(cur, max);
        }
        Bin bin = new Bin(min, max);
        this.mapAttributeBins.put(attr, bin);
        for (String strBin : bin.getAllBins()) {
            this.setDomainVaule(attr, strBin);
        }
    }

    public void setDomainVaule(String attr, String d) {
        if (d == null || RDB.isNum(d)) {
            return;
        }
        List<String> domains = this.mapDomainVaules.get(attr);
        if (domains == null) {
            domains = new ArrayList<String>();
            this.mapDomainVaules.put(attr, domains);
        }
        if (!domains.contains(d)) {
            domains.add(d);
        }
    }

    public int getDomainIndex(String attr, String value) {
        List<String> domains = this.mapDomainVaules.get(attr);
        String attrType = this.mapAttributeTypes.get(attr);
        if (this.isRealValueType(attrType)) {
            String checkerNumOrBin = value.substring(0, 1);
            if (checkerNumOrBin.equals("_")) {
                return domains.indexOf(value);
            }
            Bin b = this.mapAttributeBins.get(attr);
            return b.getIndex(Double.valueOf(value));
        }
        return domains.indexOf(value);
    }

    public String getDomainValue(String attr, int index) {
        List<String> domains = this.mapDomainVaules.get(attr);
        return domains.get(index);
    }

    public void searchChains(String targetTable) {
        for (Object key : this.mapChains.keySet()) {
            if (targetTable.equalsIgnoreCase((String)key)) {
                return;
            }
            List tables = (List)this.mapChains.get(key);
            if (tables == null || !tables.contains(targetTable)) continue;
            return;
        }
        List<String> tables = (List)this.mapTableTypesAndTables.get((Object)"RelationshipTable");
        for (String t : tables) {
            boolean b;
            ArrayList<String> excepts;
            if (targetTable.equalsIgnoreCase(t) || !(b = this.IsConnectedOdd(t, targetTable, excepts = new ArrayList<String>()))) continue;
            ArrayList<String> chains = new ArrayList<String>();
            chains.add(targetTable);
            for (String connectedTable : excepts) {
                if (chains.contains(connectedTable)) continue;
                chains.add(connectedTable);
            }
            ArrayList<String> importedTables = this.getImportedTable(targetTable);
            for (String importedTable : importedTables) {
                if (chains.contains(importedTable)) continue;
                chains.add(importedTable);
            }
            String key2 = String.valueOf(targetTable) + "_" + t;
            this.addChains(key2, chains, targetTable, t);
            System.out.println("Chains: " + key2 + " " + chains);
        }
    }

    public boolean IsConnectedOdd(String t, String targetTable, List<String> excepts) {
        if (!excepts.contains(t)) {
            excepts.add(t);
        }
        boolean b = false;
        ArrayList<String> importedTables = this.getImportedTable(t);
        for (String importedTable : importedTables) {
            if (targetTable.equalsIgnoreCase(importedTable)) {
                return true;
            }
            if (excepts.contains(importedTable)) {
                return false;
            }
            b = this.IsConnectedEven(importedTable, targetTable, excepts);
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
        ArrayList<String> exportedTables = this.getExportedTable(t);
        for (String exportedTable : exportedTables) {
            if (targetTable.equalsIgnoreCase(exportedTable)) {
                return true;
            }
            if (excepts.contains(exportedTable)) {
                return false;
            }
            b = this.IsConnectedOdd(exportedTable, targetTable, excepts);
            if (!b) continue;
            return true;
        }
        return b;
    }

    public void addTableAndKeys(String t, String k, String ori) {
        ArrayList v = (ArrayList)this.mapTableAndKeys.get((Object)t);
        if (v == null) {
            this.mapTableAndKeys.put((Object)t, (Object)k);
        } else if (!v.contains(k)) {
            this.mapTableAndKeys.put((Object)t, (Object)k);
        }
        this.mapKeysOrigins.put(k, ori);
    }

    public void addTableAndAttributes(String table, String attribute, String attributeType) {
        this.mapTableAndAttributes.put((Object)table, (Object)attribute);
        this.mapAttributeTypes.put(attribute, attributeType);
    }

    public void addTableTypesAndTables(String t, String b) {
        this.mapTableTypesAndTables.put((Object)t, (Object)b);
    }

    public boolean isEntityTable(String b) {
        ArrayList tables = (ArrayList)this.mapTableTypesAndTables.get((Object)"EntityTable");
        if (tables.contains(b)) {
            return true;
        }
        return false;
    }

    public boolean isRelationshipTable(String b) {
        ArrayList tables = (ArrayList)this.mapTableTypesAndTables.get((Object)"RelationshipTable");
        if (tables.contains(b)) {
            return true;
        }
        return false;
    }

    public void addChains(String nkey, List<String> tables, String c1, String c2) {
        for (String s : tables) {
            this.mapChains.put((Object)nkey, (Object)s);
        }
        this.mapChainsOfChains.put((Object)nkey, (Object)c1);
        this.mapChainsOfChains.put((Object)nkey, (Object)c2);
    }

    public ArrayList<String> getEntityTables() {
        return (ArrayList)this.mapTableTypesAndTables.get((Object)"EntityTable");
    }

    public ArrayList<String> getRelationshipTables() {
        return (ArrayList)this.mapTableTypesAndTables.get((Object)"RelationshipTable");
    }

    public ArrayList<String> getEntityTablesHavingAttrs() {
        ArrayList<String> list = (ArrayList)this.mapTableTypesAndTables.get((Object)"EntityTable");
        ArrayList<String> re = new ArrayList<String>();
        for (String t : list) {
            ArrayList attrs = (ArrayList)this.mapTableAndAttributes.get((Object)t);
            if (attrs == null) continue;
            re.add(t);
        }
        return re;
    }

    public ColtDataSet getRowDataSet(ArrayList<String> attrs, String strTables) throws SQLException {
        String strAttrs = this.trimArrayString(attrs);
        ResultSet rs = this.get(strAttrs, strTables);
        return this.getDataSet(rs, attrs);
    }

    public ColtDataSet getNatualJoinedDataSet(ArrayList<String> importedTables, String strTables) throws SQLException {
        ResultSet rs = this.getNaturalJoin(importedTables, strTables);
        ArrayList<String> attrs = new ArrayList<String>();
        ArrayList<String> vars = null;
        vars = (ArrayList)this.mapTableAndAttributes.get((Object)strTables);
        if (vars != null) {
            for (String v : vars) {
                attrs.add(v);
            }
        }
        for (String importedTable : importedTables) {
            vars = (ArrayList)this.mapTableAndAttributes.get((Object)importedTable);
            if (vars == null) continue;
            for (String v : vars) {
                attrs.add(v);
            }
        }
        return this.getDataSet(rs, attrs);
    }

    public ColtDataSet getConditionedJoinedDataSet(List<String> importedTables, String strTable) throws SQLException {
        String strOnArg = "";
        Map<String, String> importedColumns = this.getImportedColumn(strTable);
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
            Map<String, String> exportedColumns = this.getExportedColumn(importedT);
            for (String exportedColumn : exportedColumns.keySet()) {
                strOnArg = String.valueOf(strOnArg) + " = " + importedT + "." + exportedColumn;
            }
        }
        ResultSet rs = this.getJoin(null, null, null, null, null);
        ArrayList<String> attrs = new ArrayList<String>();
        List<String> vars = null;
        vars = (List)this.mapTableAndAttributes.get((Object)strTable);
        if (vars != null) {
            for (String v : vars) {
                attrs.add(v);
            }
        }
        for (String importedTable : importedTables) {
            vars = (ArrayList)this.mapTableAndAttributes.get((Object)importedTable);
            if (vars == null) continue;
            for (String v : vars) {
                attrs.add(v);
            }
        }
        return this.getDataSet(rs, attrs);
    }

    public ColtDataSet getTetDataSetFromCSV(String fCSV) {
        List<Node> nodes = new ArrayList<Node>();
        ColtDataSet dataset = null;
        CSVReader reader = null;
        FileReader fr = null;
        try {
            fr = new FileReader(fCSV);
            reader = new CSVReader((Reader)fr);
        }
        catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        HashMap<Integer, String> mapHeader = new HashMap<Integer, String>();
        try {
            int i = 0;
            List<String[]> list = reader.readAll();
            for (String[] nextLine : list) {
                int j;
                int rowSize;
                if (i == 0) {
                    j = 0;
                    while (j < nextLine.length) {
                        String str = nextLine[j];
                        mapHeader.put(j, str);
                        ++j;
                    }
                    rowSize = list.size();
                    dataset = new ColtDataSet(rowSize - 1, nodes);
                } else {
                    if (i == 1) {
                        j = 0;
                        while (j < nextLine.length) {
                           
                            if (RDB.isNum(nextLine[j])) {
                            	ContinuousVariable gn;
                                gn = new ContinuousVariable((String)mapHeader.get(j));
                                nodes.add(gn);
                            } else {
                            	DiscreteVariable gn;
                                gn = new DiscreteVariable((String)mapHeader.get(j));
                                nodes.add(gn);
                            }
                            ++j;
                        }
                        rowSize = list.size();
                        dataset = new ColtDataSet(rowSize - 1, nodes);
                    }
                    j = 0;
                    while (j < nextLine.length) {
                        long timeTotal = System.nanoTime();
                        dataset.setObject(i - 1, j, (Object)nextLine[j]);
                        Long l = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - timeTotal);
                        ++j;
                    }
                }
                ++i;
            }
        }
        catch (IOException e) {
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
            importedColumns = this.getImportedColumn(strTable);
            for (String importedColumn : importedColumns.keySet()) {
                importedT = importedColumns.get(importedColumn);
                for (String strTableC : tablesAll) {
                    if (repeatedT.contains(strTableC) || !this.checkPreT(strTableC, importedColumn).booleanValue()) continue;
                    String strTableC_fillterd = this.checkPreT2(strTableC);
                    Map<String, String> importedColumnsC = this.getImportedColumn(strTableC_fillterd);
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
                importedColumns = this.getImportedColumn(strTableC);
                for (String importedColumn : importedColumns.keySet()) {
                    importedT = importedColumns.get(importedColumn);
                    if (importedT.equalsIgnoreCase("Time")) continue;
                    strOnArg = String.valueOf(strOnArg) + " && \n";
                    strOnArg = String.valueOf(strOnArg) + strTableC + "." + importedColumn + " = pre_" + strTableC + "." + importedColumn + "\n";
                }
            }
        }
        ResultSet rs = this.getJoin(curTable, tablesTarget, tablesAll, prevTables, strOnArg);
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
            importedColumns = this.getImportedColumn(strTable);
            for (String importedColumn : importedColumns.keySet()) {
                importedT = importedColumns.get(importedColumn);
                for (String strTableC : tablesAll) {
                    if (repeatedT.contains(strTableC) || !this.checkPreT(strTableC, importedColumn).booleanValue()) continue;
                    String strTableC_fillterd = this.checkPreT2(strTableC);
                    Map<String, String> importedColumnsC = this.getImportedColumn(strTableC_fillterd);
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
                importedColumns = this.getImportedColumn(strTableC);
                for (String importedColumn : importedColumns.keySet()) {
                    importedT = importedColumns.get(importedColumn);
                    if (importedT.equalsIgnoreCase("Time")) continue;
                    strOnArg = String.valueOf(strOnArg) + " && \n";
                    strOnArg = String.valueOf(strOnArg) + strTableC + "." + importedColumn + " = pre_" + strTableC + "." + importedColumn + "\n";
                }
            }
        }
        ResultSet rs = this.getJoin(curTable, tablesTarget, tablesAll, prevTables, strOnArg);
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
        List<String> listDomain = this.mapDomainVaules.get(curNode.getName());
        for (String domain : listDomain) {
            tempList.add(domain);
            this.createDefaultPriorDataset(nodes, nodeIndex, tempList, result);
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
        this.createDefaultPriorDataset(nodes, 0, tempList, result);
        dataset = new ColtDataSet(this.sizeOfResultSet(rs) + result.size(), nodes);
        for (ArrayList<String> dataRow : result) {
            int j = 0;
            for (String aData : dataRow) {
                dataset.setObject(i, j, (Object)this.getDomainIndex(nodes.get(j).getName(), aData));
                ++j;
            }
            ++i;
        }
        try {
            while (rs.next()) {
                int j = 0;
                for (String attr : attrs) {
                    dataset.setObject(i, j, (Object)this.getDomainIndex(attr, rs.getString(attr)));
                    ++j;
                }
                ++i;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return dataset;
    }

    public ColtDataSet getDataSet(ResultSet rs, List<String> attrs) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        ColtDataSet dataset = null;
        int i = 0;
        for (String s : attrs) {
            DiscreteVariable gn = new DiscreteVariable(s);
            nodes.add(gn);
        }
        dataset = new ColtDataSet(this.sizeOfResultSet(rs), nodes);
        try {
            while (rs.next()) {
                int j = 0;
                for (String attr : attrs) {
                    if (attr.equalsIgnoreCase("turnrate")) {
                        int a = 0;
                        ++a;
                        this.getDomainIndex(attr, "7.62954");
                    }
                    System.out.println(String.valueOf(attr) + " " + rs.getString(attr) + " " + " " + this.getDomainIndex(attr, rs.getString(attr)));
                    dataset.setObject(i, j, (Object)this.getDomainIndex(attr, rs.getString(attr)));
                    ++j;
                }
                ++i;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return dataset;
    }

    public void printClassInfo() {
        Object a;
        System.out.println("Tables' Types");
        for (Object o22222 : this.mapTableTypesAndTables.keySet()) {
            a = (ArrayList)this.mapTableTypesAndTables.get(o22222);
            System.out.println("\t" + o22222 + ":" + a);
        }
        System.out.println("Tables' Keys");
        for (Object o : this.mapTableAndKeys.keySet()) {
            a = (ArrayList)this.mapTableAndKeys.get(o);
            System.out.println("\t" + o + ":" + a);
        }
        System.out.println("Tables' Attributes");
        for (Object o2 : this.mapTableAndAttributes.keySet()) {
            String attrType = this.mapAttributeTypes.get(o2);
            ArrayList a2 = (ArrayList)this.mapTableAndAttributes.get(o2);
            System.out.println("\t" + o2 + ":" + a2 + "[" + attrType + "]");
        }
        System.out.println("Tables' Chains");
        for (Object o3 : this.mapChains.keySet()) {
            a = (ArrayList)this.mapChains.get(o3);
            System.out.println("\t" + o3 + ":" + a);
        }
        System.out.println("Tables' Chains of Chains");
        for (Object o4 : this.mapChainsOfChains.keySet()) {
            a = (ArrayList)this.mapChainsOfChains.get(o4);
            System.out.println("\t" + o4 + ":" + a);
        }
        System.out.println("Keys' Origins");
        for (Object o5 : this.mapKeysOrigins.keySet()) {
            a = this.mapKeysOrigins.get(o5);
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
        String strAttrs = this.trimArrayString(listAttrs);
        strAttrs = strAttrs.replace(",", "_");
        strAttrs = strAttrs.replace(" ", "");
        return strAttrs;
    }

    public String changeMapString(String str, ArrayList<String> listAttrs) {
        return String.valueOf(str) + "_" + this.changeArrayString(listAttrs);
    }
}

