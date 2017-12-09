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

import com.mysql.jdbc.Driver;
import java.io.FileWriter;
import java.io.IOException; 
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet; 
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map; 
import mebn_rm.util.Resource;
import util.TempMathFunctions; 

public class MySQL_Interface extends TempMathFunctions {
    public static Connection connection;
    public String schema = "";
    public String root = "root";
    public String PW = "jesus"; 
    public String address = "localhost";

    public MySQL_Interface() { 
    }
    
    public void connect(String a, String r, String p) {
    	address = a;
        root = r;
        PW = p;
        
        try {
            DriverManager.registerDriver((java.sql.Driver)new Driver());
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + address + "/", root, PW);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection connectSchema(String s) {
    	try {
			connection.close();
		} catch (SQLException e1) { 
			e1.printStackTrace();
		}
    	
        try {
            DriverManager.registerDriver((java.sql.Driver)new Driver());
            schema = s;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + address  + "/" + s, root, PW);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Connected to a RDB [" + s + "]");
        return null;
    }

    public Connection createSchema(String s) {
        String str = "create schema  " + s;
        schema = s;
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(str);
            return connectSchema(s);
        }
        catch (SQLException e) {
            if (e.getErrorCode() == 1007) {
                return connectSchema(s);
            }
            e.printStackTrace();
            return null;
        }
    }

    public void createEntityTable(String s) {
        String primarykey = s;
        String sRun = "create table  " + s + " (" + primarykey + " char(45) not null, primary key(" + primarykey + "));";
        updateSQL(sRun);
    }

    public boolean createRelationTable(String s) {
        String sRun = "create table  " + s;
        return updateSQL(sRun);
    }

    public void createAttributeOnTable(String table, String attribute) {
        String sRun = "alter table  " + table + " add " + attribute + " char(45);";
        updateSQL(sRun);
    }

    public void createForeignKeyAttributeOnTable(String table, String attribute, String foreignTable, String foreignKey) {
        foreignTable = foreignTable.toLowerCase();
        String sRun = "alter table  " + table + " add COLUMN " + attribute + " char(45) NULL; ";
        updateSQL(sRun);
        sRun = "alter table  " + table + " add FOREIGN KEY (" + attribute + ") REFERENCES " + foreignTable + "(" + foreignKey + ");";
        updateSQL(sRun);
    }

    public void addValue(String table, String attribute, String value) {
        String sRun = "insert into " + table + " ( " + attribute + " ) values( " + value + " );";
        System.out.println(sRun);
        updateSQL(sRun);
    }

    public void updateValue(String table, String key, String value) {
        String sRun = "update " + table + " SET " + value + " where " + key;
        updateSQL(sRun);
    }

    public boolean updateSQL(String s) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(s);
        }
        catch (SQLException e) {
            if (e.getErrorCode() == 1007) {
                connectSchema(s);
            } else if (e.getErrorCode() == 1050) {
                System.out.println("Already there is the table");
            } else if (e.getErrorCode() == 1060) {
                System.out.println("Already there is the attribute");
            } else if (e.getErrorCode() == 1062) {
                System.out.println("Duplicate key");
            } else {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }

    public ResultSet querySQL(String s) {
        System.out.println(s);
        Statement statement = null;
        try { 
        	connection.close();
        	connection = DriverManager.getConnection("jdbc:mysql://" + address + "/" + schema, root, PW);
        	statement = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, 
        										   java.sql.ResultSet.CONCUR_READ_ONLY);
        	statement.setFetchSize(Integer.MIN_VALUE);
        	return statement.executeQuery(s); 
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet getSufficientStatistics(String strAttrs, String strTables) {
        String sRun = "SELECT " + strAttrs + ", count(*) FROM " + strTables + " GROUP BY " + strAttrs + ";";
        return querySQL(sRun);
    }
 
    public ArrayList<String> getPrimaryKeysToArray(String table) {
        ArrayList<String> primarykeys = new ArrayList<String>();
        ResultSet rs = null; 
        try {
            DatabaseMetaData meta = connection.getMetaData();
            rs = meta.getPrimaryKeys(null, null, table);
            while (rs.next()) {
                primarykeys.add(rs.getString("COLUMN_NAME"));
            }
            rs.close(); 
            return primarykeys;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return primarykeys;
        }
    }

    public ResultSet getTables() {
        ResultSet rs = null;
        String[] types = new String[]{"TABLE"};
        try {
            DatabaseMetaData meta = connection.getMetaData();
            rs = meta.getTables(null, null, "%", types); 
            return rs;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet getColumn(String table) {
        try {
            DatabaseMetaData metadata = connection.getMetaData();
            ResultSet rs = metadata.getColumns(null, null, table, null);
            return rs;
        }
        catch (SQLException metadata) {
            return null;
        }
    }

    public ResultSet get(String sRun) {
        return querySQL(sRun);
    }

    public ResultSet get(String strAttrs, String strTables) {
        String sRun = "SELECT " + strAttrs + " FROM " + strTables + ";";
        return querySQL(sRun);
    }

    public ResultSet getWithOutNull(String strAttrs, String strNullAttr, String strTables) {
        String sRun = "SELECT " + strAttrs + " FROM " + strTables + " WHERE " + strNullAttr + " IS NOT NULL;";
        return querySQL(sRun);
    }

    public ResultSet getNaturalJoin(ArrayList<String> importedTable, String strTables) {
        String sRun = "SELECT distinct * FROM " + strTables;
        for (String s : importedTable) {
            sRun = String.valueOf(sRun) + " natural JOIN " + s;
        }
        sRun = String.valueOf(sRun) + ";";
        return querySQL(sRun);
    }

    public String rename(String s) {
        String ret = s;
        Integer index = ret.indexOf("_pret");
        if (index < 0) {
            return ret;
        }
        ret = ret.substring(0, ret.length() - ret.indexOf("_pret"));
        return ret;
    }

    public ResultSet getJoin(String curTable, List<String> tableTarget, List<String> tableAll, List<String> prevTables, String strOnArg) {
        String sRun = "SELECT ";
        String sOrderBy = " ORDER BY ";
        int j = 0;
        Map<String, String> importedColumns = getImportedColumn(curTable);
        for (String key : importedColumns.keySet()) {
            String s = String.valueOf(curTable) + "." + key;
            sRun = String.valueOf(sRun) + s + ", ";
            sOrderBy = String.valueOf(sOrderBy) + s + ", ";
        }
        sOrderBy = sOrderBy.substring(0, sOrderBy.length() - 2);
        for (String s222 : tableTarget) {
            if (j > 0) {
                sRun = String.valueOf(sRun) + ", \n";
            }
            sRun = String.valueOf(sRun) + s222;
            sRun = String.valueOf(sRun) + "." + rename(s222);
            sRun = String.valueOf(sRun) + " AS '" + s222 + "'";
            ++j;
        }
        for (String s222 : prevTables) {
            if (j > 0) {
                sRun = String.valueOf(sRun) + ", \n";
            }
            sRun = String.valueOf(sRun) + s222;
            sRun = String.valueOf(sRun) + "." + rename(s222);
            sRun = String.valueOf(sRun) + " AS 'pre_" + s222 + "'";
            ++j;
        }
        sRun = String.valueOf(sRun) + " FROM ";
        j = 0;
        for (String s222 : tableAll) {
            if (j > 0) {
                sRun = String.valueOf(sRun) + " JOIN ";
            }
            sRun = String.valueOf(sRun) + rename(s222);
            sRun = String.valueOf(sRun) + " " + s222;
            ++j;
        }
        for (String s222 : prevTables) {
            if (j > 0) {
                sRun = String.valueOf(sRun) + " JOIN ";
            }
            sRun = String.valueOf(sRun) + rename(s222);
            sRun = String.valueOf(sRun) + " pre_" + s222;
            ++j;
        }
        if (!strOnArg.isEmpty()) {
            sRun = String.valueOf(sRun) + " \n";
            sRun = String.valueOf(sRun) + "on \n";
            sRun = String.valueOf(sRun) + strOnArg;
        }
        sRun = String.valueOf(sRun) + sOrderBy + ";";
        return querySQL(sRun);
    }

    public ResultSet getDomain(String strAttrs, String strTables) {
        String sRun = "SELECT " + strAttrs + " FROM " + strTables + " GROUP BY " + strAttrs + ";";
        return querySQL(sRun);
    }

    public ArrayList<String> getImportedTable(String strTable) {
        ArrayList<String> ImportedTables = new ArrayList<String>();
        ResultSet rs = null;
        ResultSet rstable = getTables();
        try {
            DatabaseMetaData metadata = connection.getMetaData();
            String strC = connection.getCatalog();
            while (rstable.next()) {
                String tableIterated = rstable.getString(3);
                rs = metadata.getExportedKeys(strC, null, tableIterated);
                while (rs.next()) {
                    String fkTableName = rs.getString("FKTABLE_NAME"); 
                    if (!fkTableName.equalsIgnoreCase(strTable) || strTable.equalsIgnoreCase(tableIterated) || ImportedTables.contains(tableIterated)) continue;
                    	ImportedTables.add(tableIterated);
                }
            }
            
            rstable.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return ImportedTables;
    }

    public Map<String, String> getImportedColumn(String strTable) {
        HashMap<String, String> ImportedTables = new HashMap<String, String>();
        try {
            DatabaseMetaData metadata = connection.getMetaData();
            String strC = connection.getCatalog();
            ResultSet rs = metadata.getImportedKeys(strC, null, strTable);
            while (rs.next()) {
                String fkColumnName = rs.getString("FKCOLUMN_NAME"); 
                String pkTableName = rs.getString("PKTABLE_NAME"); 
                if (ImportedTables.containsKey(fkColumnName)) continue;
                ImportedTables.put(fkColumnName, pkTableName);
            }
            
            rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return ImportedTables;
    }

    public ArrayList<String> getExportedTable(String strTable) {
        ArrayList<String> exportedTables = new ArrayList<String>();
        try {
            DatabaseMetaData metadata = connection.getMetaData();
            String strC = connection.getCatalog();
            ResultSet rs = metadata.getExportedKeys(strC, null, strTable);
            while (rs.next()) {
                String fkTableName = rs.getString("FKTABLE_NAME");
                if (exportedTables.contains(fkTableName)) continue;
                exportedTables.add(fkTableName);
            }
            
            rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return exportedTables;
    }

    public Map<String, String> getExportedColumn(String strTable) {
        HashMap<String, String> exportedTables = new HashMap<String, String>();
        try {
            DatabaseMetaData metadata = connection.getMetaData();
            String strC = connection.getCatalog();
            ResultSet rs = metadata.getExportedKeys(strC, null, strTable);
            while (rs.next()) { 
                String fkTableName = rs.getString("FKTABLE_NAME"); 
                String pkColumnName = rs.getString("PKCOLUMN_NAME");
                if (exportedTables.containsKey(pkColumnName)) continue;
                exportedTables.put(pkColumnName, fkTableName);
            }
            rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return exportedTables;
    }

    public void deleteAllRows(String table) {
        String sRun = "DELETE FROM " + table + ";";
        updateSQL(sRun);
    }

    public void deleteSchema(String s) {
        String sRun = "DROP SCHEMA " + s + ";";
        updateSQL(sRun);
    }

    public int sizeOfPrimaryKeys(String table) {
        int i = 0;
        ResultSet rs = null;
        try {
            DatabaseMetaData meta = connection.getMetaData();
            rs = meta.getPrimaryKeys(null, null, table);
            while (rs.next()) {
                ++i;
            }
//            rs.beforeFirst();
            rs.close();
            return i;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return i;
        }
    }

    public int sizeOfResultSet(ResultSet rs) {
        int i = 0;
        try {
            while (rs.next()) {
            	++i;
            } 
            rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    public void exportingMySQLTabletoFlatFile() {
        try {
            Statement stmt = connection.createStatement();
            String filename = "c:\\\\temp\\\\outfile.txt";
            String tablename = "mysql_2_table";
            stmt.executeUpdate("SELECT * INTO OUTFILE \"" + filename + "\" FROM " + tablename);
        }
        catch (SQLException stmt) {
            // empty catch block
        }
    }

    public void loadingFlatFiletoMySQLTable() {
        try {
            Statement stmt = connection.createStatement();
            String filename = "c:\\\\temp\\\\infile.txt";
            String tablename = "mysql_2_table";
            stmt.executeUpdate("LOAD DATA INFILE \"" + filename + "\" INTO TABLE " + tablename);
            stmt.executeUpdate("LOAD DATA INFILE \"" + filename + "\" INTO TABLE " + tablename + " FIELDS TERMINATED BY ','");
            stmt.executeUpdate("LOAD DATA INFILE \"" + filename + "\" INTO TABLE " + tablename + " LINES TERMINATED BY '\\r\\n'");
        }
        catch (SQLException stmt) {
            // empty catch block
        }
    }

    public String toExcel(String fileName, String strMTheory, ResultSet res) throws SQLException, IOException {
        int colunmCount = MySQL_Interface.getColumnCount(res);
        String strFile = String.valueOf(Resource.getCSVPath(strMTheory)) + fileName + ".csv";
        FileWriter fw = new FileWriter(strFile);
        int i = 1;
        while (i <= colunmCount) {
            fw.append(res.getMetaData().getColumnName(i));
            fw.append(",");
            ++i;
        }
        fw.append(System.getProperty("line.separator"));
        while (res.next()) {
            i = 1;
            while (i <= colunmCount) {
                String data;
                if (res.getObject(i) != null) {
                    data = res.getObject(i).toString();
                    fw.append(data);
                    if (i < colunmCount) {
                        fw.append(",");
                    }
                } else {
                    data = "null";
                    fw.append(data);
                    if (i < colunmCount) {
                        fw.append(",");
                    }
                }
                ++i;
            }
            fw.append(System.getProperty("line.separator"));
        }
        fw.flush();
        fw.close();
        res.close();
        return strFile;
    } 

    public static int getColumnCount(ResultSet res) throws SQLException {
        return res.getMetaData().getColumnCount();
    }

    public List<String> getSchemas() {
        ArrayList<String> listSchemas = new ArrayList<String>();
        try {
            ResultSet rs = connection.getMetaData().getCatalogs();
            while (rs.next()) {
                String schema = rs.getString("TABLE_CAT");
                if (schema.equalsIgnoreCase("information_schema") || schema.equalsIgnoreCase("mysql") || schema.equalsIgnoreCase("performance_schema")) continue;
                listSchemas.add(schema);
            }
            rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return listSchemas;
    }
}

