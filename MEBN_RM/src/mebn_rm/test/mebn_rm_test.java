/*
 * Decompiled with CFR 0_118.
 */
package mebn_rm.test;

import java.sql.SQLException;
import mebn_rm.MEBN.MTheory.MTheory;
import mebn_rm.RDB.RDB;
import mebn_rm.core.RM_To_MEBN;
import mebn_rm.util.TextFile;

public class mebn_rm_test {
    public mebn_rm_test(String schema) throws SQLException {
        RDB.This().init(schema);
        MTheory m1 = new RM_To_MEBN(RDB.This()).run();
        String mTheory = m1.toString("MFrag", "MNode");
        System.out.println(mTheory);
        new mebn_rm.util.TextFile();
        TextFile.save(".//Examples//MTheory//" + schema + ".txt", mTheory);
    }

    public static void main(String[] args) {
        try {
        	new mebn_rm.test.mebn_rm_test("msaw_process_logical");
//            new mebn_rm.test.mebn_rm_test("Prognos_NewWorldModel");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

