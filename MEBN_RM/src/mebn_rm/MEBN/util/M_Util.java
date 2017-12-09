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
package mebn_rm.MEBN.util;

import java.util.ArrayList;
import java.util.List;
import mebn_rm.MEBN.MFrag.MFrag;
import mebn_rm.MEBN.MNode.MNode;

public class M_Util {
    public static final List<String> getMNodeNames(MFrag f) {
        ArrayList<String> arrayString = new ArrayList<String>();
        List<MNode> list = f.getMNodes();
        for (MNode s : list) {
            arrayString.add(s.toString());
        }
        return arrayString;
    }

    public static final List<String> getAllMNodeNames(MFrag f) {
        ArrayList<String> arrayString = new ArrayList<String>();
        List<MNode> list = f.getAllNodes();
        for (MNode s : list) {
            arrayString.add(s.toString());
        }
        return arrayString;
    }

    public static final List<String> getInputPrevNodes(MFrag f) {
        ArrayList<String> arrayString = new ArrayList<String>();
        List<MNode> list = f.getInputPrevNodes();
        for (MNode s : list) {
            arrayString.add(s.toString());
        }
        return arrayString;
    }
}

