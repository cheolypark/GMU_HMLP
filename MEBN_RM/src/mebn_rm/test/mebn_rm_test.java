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
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

