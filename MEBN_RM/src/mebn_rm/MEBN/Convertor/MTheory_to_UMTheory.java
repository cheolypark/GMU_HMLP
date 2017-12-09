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
package mebn_rm.MEBN.Convertor;
  
public class MTheory_to_UMTheory {
//    MultiEntityBayesianNetwork mebn = null;
//    public Map<ResidentNode, String> mapDefaultCPT = new HashMap<ResidentNode, String>();
//    MFrag refMFrag = null;
//    int nameCount = 0;
//    public Knowledge globalKnowledge = null;
//    MTheory mTheory = null;
//
//    public MTheory_to_UMTheory(MTheory m, Knowledge knowledge) {
//        try {
//            initDefaultMTheory(m, knowledge);
//        }
//        catch (TypeException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void initDefaultMTheory(MTheory m, Knowledge knowledge) throws TypeException {
//        mTheory = m;
//        loadMTheory("examples/default_MEBN/default.ubf");
//        mebn.setName(mTheory.name);
//        initEntityTypes();
//        initStateTypes();
//        initKnowledge(knowledge);
//    }
//
//    public void initEntityTypes() throws TypeException {
//        ArrayList<String> tables = mTheory.rdb.getEntityTables();
//        for (String t : tables) {
//            System.out.println("Create " + t.toUpperCase() + " Entity");
//            mebnCreateEntity(t.toUpperCase());
//        }
//    }
//
//    public void initStateTypes() {
//        for (String s : mTheory.rdb.mapDomainVaules.keySet()) {
//            List<String> list = mTheory.rdb.mapDomainVaules.get(s);
//            for (String v : list) {
//                mebn.getCategoricalStatesEntityContainer().createCategoricalEntity(v);
//            }
//        }
//        printCategoricalStateEntity();
//    }
//
//    public void initKnowledge(Knowledge knowledge) {
//        globalKnowledge = knowledge == null ? new Knowledge() : new Knowledge(knowledge);
//    }
//
//    public String generateNewName(String n) {
//        return String.valueOf(n) + nameCount++;
//    }
//
//    public String getNameRemovedType(String n, Type t) {
//        String s = n;
//        int last = n.lastIndexOf(t.toString());
//        if (last > 0) {
//            s = n.substring(0, last);
//        }
//        return s;
//    }
//
//    public Node getUNode(String n) {
//        for (MFrag mfrag : mebn.getDomainMFragList()) {
//            for (ResidentNode residentNode : mfrag.getResidentNodeList()) {
//                if (!residentNode.getName().equalsIgnoreCase(n)) continue;
//                return residentNode;
//            }
//        }
//        return null;
//    }
//
//    public ArrayList<String> getOVs(String n) {
//        for (Object o : mTheory.rdb.mapTableAndAttributes.keySet()) {
//            ArrayList a = (ArrayList)mTheory.rdb.mapTableAndAttributes.get(o);
//            if (!a.contains(n)) continue;
//            return (ArrayList)mTheory.rdb.mapTableAndKeys.get(o);
//        }
//        return new ArrayList<String>();
//    }
//
//    public OrdinaryVariable getOrdinaryVariableByType(Type t, MFrag mfrag) {
//        for (OrdinaryVariable test : mfrag.getOrdinaryVariableList()) {
//            if (test.getValueType() != t) continue;
//            return test;
//        }
//        return null;
//    }
//
//    public MFrag getUMFrag(String n) {
//        for (MFrag mfrag : mebn.getDomainMFragList()) {
//            for (ResidentNode residentNode : mfrag.getResidentNodeList()) {
//                if (!residentNode.getName().equalsIgnoreCase(n)) continue;
//                return mfrag;
//            }
//        }
//        return null;
//    }
//
//    public String getArugmentStrings(List<OrdinaryVariable> list) {
//        String str = " ";
//        for (OrdinaryVariable ov : list) {
//            str = String.valueOf(str) + ov.getName() + " ";
//        }
//        return str;
//    }
//
//    public String getArugmentStrings(ResidentNode rn) {
//        String str = "";
//        int i = 0;
//        for (OrdinaryVariable ov : rn.getOrdinaryVariableList()) {
//            Type t = ov.getValueType();
//            String name = ov.getName();
//            int last = name.lastIndexOf(t.toString());
//            if (last > 0) {
//                name = name.substring(0, last);
//            }
//            str = i == rn.getOrdinaryVariableList().size() - 1 ? String.valueOf(str) + name : String.valueOf(str) + name + ".";
//            ++i;
//        }
//        return str;
//    }
//
//    public boolean isNumber(String value) {
//        try {
//            Double.parseDouble(value);
//            return true;
//        }
//        catch (NumberFormatException e) {
//            return false;
//        }
//    }
//
//    public String changeNumberString(String value) {
//        String s = value;
//        if (isNumber(value)) {
//            s = "_" + value;
//        }
//        return s;
//    }
//
//    public BayesPm createInvertedBayesPm(BayesPm bayesPm) {
//        BayesPm oppsitePm = new BayesPm(bayesPm.getDag());
//        for (Edge e : bayesPm.getDag().getEdges()) {
//            oppsitePm.getDag().removeEdge(e);
//            oppsitePm.getDag().addEdge(new Edge(e.getNode2(), e.getNode1(), e.getEndpoint1(), e.getEndpoint2()));
//        }
//        return oppsitePm;
//    }
//
//    public EdgeListGraph resetCategoryInfo(Graph g) {
//        EdgeListGraph eg = new EdgeListGraph();
//        for (edu.cmu.tetrad.graph.Node n : g.getNodes()) {
//            ArrayList<String> newList = new ArrayList<String>();
//            List<String> list = mTheory.rdb.mapDomainVaules.get(n.getName());
//            Integer i = 0;
//            while (i < list.size()) {
//                newList.add(i.toString());
//                i = i + 1;
//            }
//            DiscreteVariable d = new DiscreteVariable(n.getName(), newList);
//            d.setNodeType(n.getNodeType());
//            eg.addNode((edu.cmu.tetrad.graph.Node)d);
//        }
//        for (Edge e : g.getEdges()) {
//            edu.cmu.tetrad.graph.Node d1 = eg.getNode(e.getNode1().getName());
//            edu.cmu.tetrad.graph.Node d2 = eg.getNode(e.getNode2().getName());
//            eg.addDirectedEdge(d1, d2);
//        }
//        return eg;
//    }
//
//    public void mebnCreateEntity(String t) throws TypeException {
//        mebn.getObjectEntityContainer().createObjectEntity(t);
//    }
//
//    public void printPotentialNet() {
//    }
//
//    public void saveMTheory(String strFile) {
//        UbfIO io = UbfIO.getInstance();
//        try {
//            io.saveMebn(new File(strFile), mebn);
//        }
//        catch (IOMebnException e) {
//            e.printStackTrace();
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void loadMTheory(String strUBF) {
//        UbfIO io = UbfIO.getInstance();
//        try {
//            mebn = io.loadMebn(new File(strUBF));
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void saveNet(String strFile, SingleEntityNetwork n) {
//        File file = new File("./examples/learning/output/" + strFile + ".xml");
//        try {
//            try {
//                XMLBIFIO.saveXML((FileWriter)new FileWriter(file), (SingleEntityNetwork)n);
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        catch (JAXBException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void printUnBBayesNet(ProbabilisticNetwork bn) {
//        for (Node node : bn.getNodes()) {
//            System.out.println(node.getDescription());
//            int i = 0;
//            while (i < node.getStatesSize()) {
//                System.out.println(String.valueOf(node.getStateAt(i)) + " : " + ((ProbabilisticNode)node).getMarginalAt(i));
//                ++i;
//            }
//        }
//    }
//
//    public void printMebn() {
//        System.out.println("*************Start**************");
//        for (MFrag mfrag : mebn.getDomainMFragList()) {
//            System.out.println("********************************");
//            System.out.println(" " + (Object)mfrag + " MFrag");
//            System.out.println("********************************");
//            for (OrdinaryVariable ordinaryVariable : mfrag.getOrdinaryVariableList()) {
//                System.out.println("Ordinary Variable: " + ordinaryVariable.getName());
//            }
//            for (ContextNode contextNode : mfrag.getContextNodeList()) {
//                ResidentNodePointer n = (ResidentNodePointer)contextNode.getFormulaTree().getNodeVariable();
//                System.out.println("Context Node: " + contextNode.getName() + "(" + getArugmentStrings(n.getOrdinaryVariableList()) + ")");
//            }
//            for (ResidentNode residentNode : mfrag.getResidentNodeList()) {
//                System.out.println("Resident Node: " + residentNode.getName() + "(" + getArugmentStrings(residentNode.getOrdinaryVariableList()) + ")");
//                residentNode.getTableFunction();
//                for (ResidentNode residentNodeFather : residentNode.getResidentNodeFatherList()) {
//                    System.out.println((Object)residentNodeFather + " -> " + (Object)residentNode);
//                }
//                for (InputNode inputNodeFather : residentNode.getParentInputNodesList()) {
//                    ResidentNode r = (ResidentNode)inputNodeFather.getInputInstanceOf();
//                    System.out.println(String.valueOf(inputNodeFather.getName()) + "(" + getArugmentStrings(r.getOrdinaryVariableList()) + ")" + " -> " + (Object)residentNode);
//                }
//            }
//        }
//        System.out.println("**************End***************");
//    }
//
//    public boolean CheckError(String s, String t) {
//        if (!s.equals(t)) {
//            System.err.println("Error at " + s);
//            return true;
//        }
//        return false;
//    }
//
//    public boolean CheckError(ArrayList<String> l) {
//        int i = 0;
//        if (CheckError(l.get(i++), "*************Start**************")) {
//            return true;
//        }
//        for (MFrag mfrag : mebn.getDomainMFragList()) {
//            if (CheckError(l.get(i++), "********************************")) {
//                return true;
//            }
//            if (CheckError(l.get(i++), " " + (Object)mfrag + " MFrag")) {
//                return true;
//            }
//            if (CheckError(l.get(i++), "********************************")) {
//                return true;
//            }
//            for (OrdinaryVariable ordinaryVariable : mfrag.getOrdinaryVariableList()) {
//                if (!CheckError(l.get(i++), "Ordinary Variable: " + ordinaryVariable.getName())) continue;
//                return true;
//            }
//            for (ContextNode contextNode : mfrag.getContextNodeList()) {
//                ResidentNodePointer n = (ResidentNodePointer)contextNode.getFormulaTree().getNodeVariable();
//                if (!CheckError(l.get(i++), "Context Node: " + contextNode.getName() + "(" + getArugmentStrings(n.getOrdinaryVariableList()) + ")")) continue;
//                return true;
//            }
//            for (ResidentNode residentNode : mfrag.getResidentNodeList()) {
//                if (CheckError(l.get(i++), "Resident Node: " + residentNode.getName() + "(" + getArugmentStrings(residentNode.getOrdinaryVariableList()) + ")")) {
//                    return true;
//                }
//                residentNode.getTableFunction();
//                for (ResidentNode residentNodeFather : residentNode.getResidentNodeFatherList()) {
//                    if (!CheckError(l.get(i++), (Object)residentNodeFather + " -> " + (Object)residentNode)) continue;
//                    return true;
//                }
//                for (InputNode inputNodeFather : residentNode.getParentInputNodesList()) {
//                    ResidentNode r = (ResidentNode)inputNodeFather.getInputInstanceOf();
//                    if (!CheckError(l.get(i++), String.valueOf(inputNodeFather.getName()) + "(" + getArugmentStrings(r.getOrdinaryVariableList()) + ")" + " -> " + (Object)residentNode)) continue;
//                    return true;
//                }
//            }
//        }
//        if (CheckError(l.get(i++), "**************End***************")) {
//            return true;
//        }
//        return false;
//    }
//
//    public void rearrangePositionsOfAllNodes() {
//        for (MFrag mfrag : mebn.getDomainMFragList()) {
//            int x1 = 50;
//            int y1 = 50;
//            int x2 = 0;
//            int y2 = 0;
//            for (OrdinaryVariable n222 : mfrag.getOrdinaryVariableList()) {
//                n222.setPosition((double)(x1 + x2 * 50), (double)(y1 + y2 * 50));
//                ++x2;
//            }
//            x2 = 0;
//            ++y2;
//            for (OrdinaryVariable n222 : mfrag.getContextNodeList()) {
//                n222.setPosition((double)(x1 + x2 * 50), (double)(y1 + y2 * 50));
//                ++x2;
//            }
//            x2 = 0;
//            ++y2;
//            for (OrdinaryVariable n222 : mfrag.getInputNodeList()) {
//                n222.setPosition((double)(x1 + x2 * 50), (double)(y1 + y2 * 50));
//                ++x2;
//            }
//            x2 = 0;
//            ++y2;
//            for (OrdinaryVariable n222 : mfrag.getResidentNodeList()) {
//                n222.setPosition((double)(x1 + x2 * 50), (double)(y1 + y2 * 50));
//                ++x2;
//            }
//        }
//    }
//
//    public void printListOfTypes() {
//        Set setType = mebn.getTypeContainer().getListOfTypes();
//        System.out.println(setType);
//    }
//
//    public void printCategoricalStateEntity() {
//        List list = mebn.getCategoricalStatesEntityContainer().getListEntity();
//        System.out.println(list);
//    }
//
//    public ContextNode addContextNode(String t, MFrag tarM) throws ArgumentOVariableAlreadySetException, OVDontIsOfTypeExpected {
//        ContextNode context = new ContextNode(generateNewName("context_" + t), tarM);
//        context.setDescription(t);
//        ResidentNode resident = refMFrag.getDomainResidentNodeByName(t.toUpperCase());
//        Argument arg = new Argument("resident", (IMultiEntityNode)context);
//        arg.setArgumentTerm((MultiEntityNode)resident);
//        ResidentNodePointer residentNodePointer = new ResidentNodePointer(resident, (Node)context);
//        NodeFormulaTree ftree = new NodeFormulaTree(resident.getName(), EnumType.OPERAND, EnumSubType.NODE, (Object)residentNodePointer);
//        context.setFormulaTree(ftree);
//        int index = 0;
//        for (OrdinaryVariable ov : resident.getOrdinaryVariableList()) {
//            residentNodePointer.addOrdinaryVariable(ov, index++);
//        }
//        tarM.addContextNode(context);
//        return context;
//    }
//
//    public ResidentNode addResidentNodeWithOV(String name, ArrayList<String> ovNames, MFrag tarM) throws ArgumentNodeAlreadySetException, OVariableAlreadyExistsInArgumentList {
//        ResidentNode rn = addResidentNode(name, tarM);
//        for (String key : ovNames) {
//            printListOfTypes();
//            String entityType = mTheory.rdb.mapKeysOrigins.get(key.toLowerCase());
//            Type type = mebn.getTypeContainer().getType(String.valueOf(entityType.toUpperCase()) + "_label");
//            OrdinaryVariable ov = addOrdinaryVariable(key, type, tarM);
//            rn.addArgument(ov, true);
//        }
//        return rn;
//    }
//
//    public ResidentNode addResidentNode(String name, MFrag tarM) {
//        ResidentNode rn = new ResidentNode(name, tarM);
//        rn.setDescription(name);
//        tarM.addResidentNode(rn);
//        return rn;
//    }
//
//    public OrdinaryVariable addOrdinaryVariable(String name, Type type, MFrag tarM) {
//        OrdinaryVariable ov = tarM.getOrdinaryVariableByName(name);
//        if (ov == null) {
//            ov = new OrdinaryVariable(name, type, tarM);
//            tarM.addOrdinaryVariable(ov);
//        }
//        return ov;
//    }
//
//    public unbbayes.prs.Edge addEdge(Node nP, Node nC, MFrag tarM) throws MEBNConstructionException, CycleFoundException, Exception {
//        unbbayes.prs.Edge uEdge = new unbbayes.prs.Edge(nP, nC);
//        tarM.addEdge(uEdge);
//        return uEdge;
//    }
//
//    public InputNode addInputNode(String inName, Node n, MFrag tarM) throws MEBNConstructionException, CycleFoundException, Exception {
//        InputNode i = null;
//        for (InputNode j : tarM.getInputNodeList()) {
//            ResidentNode r = (ResidentNode)j.getInputInstanceOf();
//            if (!r.getName().equalsIgnoreCase(inName)) continue;
//            i = j;
//            break;
//        }
//        if (i == null) {
//            i = new InputNode(generateNewName(String.valueOf(inName) + "_input"), tarM);
//            tarM.addInputNode(i);
//            ResidentNode r = (ResidentNode)getUNode(inName);
//            i.setInputInstanceOf(r);
//            ResidentNodePointer rp = i.getResidentNodePointer();
//            int index = 0;
//            while (index < r.getOrdinaryVariableList().size()) {
//                OrdinaryVariable ov = (OrdinaryVariable)r.getOrdinaryVariableList().get(index);
//                rp.addOrdinaryVariable(ov, index);
//                if (getOrdinaryVariableByType(ov.getValueType(), tarM) == null) {
//                    addOrdinaryVariable(ov.getName(), ov.getValueType(), tarM);
//                }
//                ++index;
//            }
//        }
//        unbbayes.prs.Edge uEdge = new unbbayes.prs.Edge((Node)i, n);
//        for (unbbayes.prs.Edge e : tarM.getEdges()) {
//            if (!uEdge.getOriginNode().getName().equalsIgnoreCase(e.getOriginNode().getName()) || !uEdge.getDestinationNode().getName().equalsIgnoreCase(e.getDestinationNode().getName())) continue;
//            return null;
//        }
//        tarM.addEdge(uEdge);
//        System.out.println("new input edge : " + uEdge.getOriginNode().getName() + " -> " + uEdge.getDestinationNode().getName());
//        return i;
//    }
}

