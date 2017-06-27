/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  mebn_rm.MEBN.MTheory.MRoot
 *  mebn_rm.MEBN.MTheory.MTheory
 *  mebn_rm.RDB.RDB
 *  mebn_rm.core.RM_To_MEBN
 */
package mebn_ln.test.steel_plate_KEIT;

import java.io.PrintStream;
import java.sql.SQLException;
import mebn_ln.converter.ConvertFromMTheoryToSBN;
import mebn_ln.core.MTheory_Learning;
import mebn_ln.test.steel_plate_KEIT.Rules_steel_plates_KEIT;
import mebn_rm.MEBN.MTheory.MRoot;
import mebn_rm.MEBN.MTheory.MTheory;
import mebn_rm.RDB.RDB;
import mebn_rm.core.RM_To_MEBN;

public class Learning_test_for_steel_plate_KEIT {
    public void run() throws SQLException {
        MRoot mroot = new MRoot();
        String schema = "steel_plate";
        RDB.This().init(schema);
        MTheory m1 = new RM_To_MEBN(RDB.This()).run();
        System.out.println("*After MEBN-RM *************************************************");
        System.out.println(m1.toString(new String[]{"MFrag", "MNode"}));
        System.out.println("**************************************************");
        Rules_steel_plates_KEIT.setRules(m1);
        System.out.println("*After expert knowledge *************************************************");
        System.out.println(m1.toString(new String[]{"MFrag", "MNode"}));
        System.out.println("**************************************************");
        mroot.setMTheories(new MTheory[]{m1});
        new MTheory_Learning().run(mroot);
        System.out.println("**************************************************");
        System.out.println("Learning Completed!");
        System.out.println("**************************************************");
        String file = ConvertFromMTheoryToSBN.save(m1);
        System.out.println(file);
    }

    public static void main(String[] args) throws SQLException {
        Learning_test_for_steel_plate_KEIT t = new Learning_test_for_steel_plate_KEIT();
        t.run();
    }
}

