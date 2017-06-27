/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  edu.cmu.tetrad.data.Knowledge
 */
package mebn_rm.test;

import edu.cmu.tetrad.data.Knowledge;
import java.io.PrintStream;
import java.sql.SQLException;
import mebn_rm.MEBN.Convertor.MTheory_to_UMTheory;
import mebn_rm.MEBN.MTheory.MTheory;
import mebn_rm.RDB.RDB;
import mebn_rm.core.RM_To_MEBN;
import mebn_rm.util.TextFile;

public class MTheory_to_UMTheory_test {
    public MTheory_to_UMTheory_test(String schema) throws SQLException {
        RDB.This().init(schema);
        MTheory m1 = new RM_To_MEBN(RDB.This()).run();
        String mTheory = m1.toString("MFrag", "MNode");
        System.out.println(mTheory);
        new mebn_rm.util.TextFile();
        TextFile.save(".//Examples//MTheory//" + schema + ".txt", mTheory);
//        MTheory_to_UMTheory converter = new MTheory_to_UMTheory(m1, null);
//        converter.saveMTheory(schema);
    }

    public static void main(String[] args) {
        try {
            new mebn_rm.test.MTheory_to_UMTheory_test("Prognos_NewWorldModel");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

