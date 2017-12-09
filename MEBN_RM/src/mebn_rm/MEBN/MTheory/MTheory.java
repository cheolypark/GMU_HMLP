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
package mebn_rm.MEBN.MTheory;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mebn_rm.MEBN.CLD.CLD;
import mebn_rm.MEBN.CLD.Categorical;
import mebn_rm.MEBN.CLD.ConditionalGaussian;
import mebn_rm.MEBN.MFrag.MFrag;
import mebn_rm.MEBN.MNode.MCNode;
import mebn_rm.MEBN.MNode.MDNode;
import mebn_rm.MEBN.MNode.MIsANode;
import mebn_rm.MEBN.MNode.MNode;
import mebn_rm.MEBN.MTheory.OVariable;
import mebn_rm.RDB.RDB; 
import mebn_rm.util.StringUtil;
import util.SortableValueMap;
  
public class MTheory implements Comparable<MTheory> {
    public SortableValueMap<MFrag, Double> mfrags = new SortableValueMap<MFrag, Double>();
    public ArrayList<String> entities = new ArrayList<String>();
    public String name;
    public RDB rdb = null;

    public MTheory(String n) {
        name = n;
    }

    public /* varargs */ void setMFrags(MFrag ... fs) {
        MFrag[] arrmFrag = fs;
        int n = arrmFrag.length;
        int n2 = 0;
        while (n2 < n) {
            MFrag f = arrmFrag[n2];
            f.mTheory = this;
            mfrags.put(f, 0.0);
            ++n2;
        }
    }

    public MFrag getMFrag(String s) {
        for (MFrag f : mfrags.keySet()) {
            if (!s.equalsIgnoreCase(f.name)) continue;
            return f;
        }
        return null;
    }

    public boolean removeMFrag(MFrag f) {
        mfrags.remove(f);
        return mfrags.isEmpty();
    }

    public MNode getMNode(String s) {
        String mFrag = new StringUtil().getLeft(s);
        String mNode = new StringUtil().getRight(s);
        MFrag f = getMFrag(mFrag);
        MNode childMNode = f.getMNode(mNode);
        return childMNode;
    }
 
    public void addParents(String c, List<String> ps) {
        String mFrag = new StringUtil().getLeft(c);
        String mNode = new StringUtil().getRight(c);
        String combParents = "";
        boolean bOtherMFrag = false;
        
        for (String p : ps) {
            String mFragP = new StringUtil().getLeft(p); 
            if (!mFrag.equalsIgnoreCase(mFragP) && !bOtherMFrag) { 
                bOtherMFrag = true;
            } 
        }
        
        combParents = mNode;
        
        MFrag f = getMFrag(mFrag);
        MNode childMNode = f.getMNode(mNode);
        if (bOtherMFrag) {
            String sql = "SELECT\r\n";
            String sqlFrom = "";
            sql = sql + f.getTableName() + "." + childMNode.getAttributeName() + " as " + mNode + ",\r\n";
            sqlFrom = sqlFrom + f.getTableName() + ", ";
            
            MFrag newMFrag = new MFrag(this, combParents);
            newMFrag.setTableName(f.getTableName());
            
            MNode newChild = null;
            if (childMNode.isContinuous()) {
                newChild = new MCNode(childMNode);
            } else if (childMNode.isDiscrete()) {
                newChild = new MDNode(childMNode);
            }
            newMFrag.setMNodes(newChild);
            
            List<String> keys = newMFrag.getRDBKeys();
            
            // Add Isa Context Nodes for the new MFrag from the current MFrag
            for (String key : keys) {
                String originEntity = rdb.mapKeysOrigins.get(key);
                OVariable ov = new OVariable(f.getTableName(), key, originEntity);
                newMFrag.arrayIsaContextNodes.add(new MIsANode(f, ov));
            }
            
            if (newMFrag.name.equalsIgnoreCase("HI_temperature_SII_temperature_HAI_energy")){
            	System.out.println("");
            }

            // Add parent nodes
            for (String p2 : ps) {
            	            	
                String mFragP = new StringUtil().getLeft(p2);
                String mNodeP = new StringUtil().getRight(p2);
                
                if(mNodeP.equalsIgnoreCase("RPS_SET_ROLLGAP")){
                	System.out.println("d");
                }
                	
                	
                MFrag fp = getMFrag(mFragP);
                MNode parentMNode = fp.getMNode(mNodeP);
                
                sql = sql + fp.getTableName() + "." + parentMNode.getAttributeName() + " as " + mNodeP + ",\r\n";
                sqlFrom = sqlFrom + fp.getTableName() + ", "; 
                
                List<String> keys2 = fp.getRDBKeys();
                List<OVariable> listOV = new ArrayList<OVariable>(); 
                
                // Add Isa Context Nodes for the new MFrag from the parent MFrags
                for (String key2 : keys2) {
                    String originEntity = rdb.mapKeysOrigins.get(key2);
                    OVariable ov = new OVariable(fp.getTableName(), key2, originEntity);
                    newMFrag.arrayIsaContextNodes.add(new MIsANode(f, ov));
                    listOV.add(ov);
                }
                
                // Create a new parent input MNode from a current parent MNode
                // Both have different OVs
                MNode ip = null;
                if (parentMNode.isContinuous()) {
                	ip = new MCNode(fp, parentMNode, listOV);
                } else if (parentMNode.isDiscrete()) {
                	ip = new MDNode(fp, parentMNode, listOV);
                }
                
                newChild.setInputParents(ip);
            }
             
            sql = sql.substring(0, sql.length() - 3);
            sql = sql + "\r\nFROM\r\n";
            sqlFrom = sqlFrom.substring(0, sqlFrom.length() - 2);
            sqlFrom = new StringUtil().removeRedundantItem(sqlFrom);
            sql = sql + (String)sqlFrom + "\r\n";
            System.out.println(sql);
            newMFrag.joiningSQL = sql;
            
            if (f.removeMNode(childMNode)) {
                removeMFrag(f);
            }
        } else {
            for (String p3 : ps) {
                String mFragP = new StringUtil().getLeft(p3);
                String mNodeP = new StringUtil().getRight(p3);
                MNode parentMNode = f.getMNode(mNodeP);
                childMNode.setParents(parentMNode);
            }
        }
    }

    public void addContexts(String mFrag, String sql) {
        MFrag f = getMFrag(mFrag);
    }

    public void updateParentNodesOVs(MFrag f, List<MIsANode> removeList, Map<String, String> surviveMap) {
    	for (MNode n : f.arrayResidentNodes) {
    		for (MNode ip : n.inputParentMNodes) {
    			for (MIsANode isa : removeList) {
    				OVariable ov = (OVariable)isa.ovs.get(0);
    				for (OVariable ov_ip: ip.ovs) {
    					if (ov.entityType.equalsIgnoreCase(ov_ip.entityType)) { 	
    						String ovName = surviveMap.get(ov_ip.entityType);
    						ov_ip.name = ovName; 
    					}
    				}
    			}
    		}    		
    	}    	
    }
    
    public void updateContexts() {
        for (MFrag f : mfrags.keySet()) {
            ArrayList<MIsANode> removeList;
            Map<String, String> surviveMap;
            OVariable ov; 
            
            if (f.name.equalsIgnoreCase("HI_temperature_SII_temperature_HAI_energy")) {
                System.out.println("HI_temperature_SII_temperature_HAI_energy");
            }
            
            if (f.joiningSQL != null) {
                System.out.println(f.arrayContextNodes);
                
                // remove redundant OVs, if they are same OVs
	            // e.g.) IsA(t1, TIME), IsA(t2, TIME)
	            // => IsA(t1, TIME)
	            // Also, resident nodes using these OVs changes to not having redundant OVs
                surviveMap = new HashMap<String, String>();
                removeList = new ArrayList<MIsANode>();
                
                for (MIsANode isa : f.arrayIsaContextNodes) {
                    ov = (OVariable)isa.ovs.get(0);
                    if (surviveMap.containsKey(ov.entityType)) {
                        removeList.add(isa);
                        continue;
                    }
                    surviveMap.put(ov.entityType, ov.name);
                }
                
                updateParentNodesOVs(f, removeList, surviveMap);
                
                if (removeList.size() > 0) {
                    f.arrayIsaContextNodes.removeAll(removeList);
                }
                
                // Change SQL script
                String sql = "WHERE \r\n";
                MIsANode isamain = f.arrayIsaContextNodes.get(0);
                String curKey = ((OVariable)isamain.ovs.get((int)0)).originMFrag + "." + ((OVariable)isamain.ovs.get((int)0)).originKey;
                 	
                for (MIsANode isa2 : removeList) {
                    String otherKey = ((OVariable)isa2.ovs.get((int)0)).originMFrag + "." + ((OVariable)isa2.ovs.get((int)0)).originKey;
                    sql = sql + curKey + " = " + otherKey + " &&\r\n";
                }
                
                sql = sql.substring(0, sql.length() - 5);
                f.joiningSQL = f.joiningSQL + sql;
                 
                
            } else if (f.joiningSQL == null) {
	            System.out.println(f.arrayContextNodes);
	            
	            // remove redundant OVs, if they are same OVs
	            // e.g.) IsA(t1, TIME), IsA(t2, TIME)
	            // => IsA(t1, TIME)
	            // Also, resident nodes using these OVs changes to not having redundant OVs
                surviveMap = new HashMap<String, String>();
                removeList = new ArrayList<MIsANode>();
                
                for (MIsANode isa : f.arrayIsaContextNodes) {
                    ov = (OVariable)isa.ovs.get(0);
                    if (surviveMap.containsKey(ov.entityType)) {
                        removeList.add(isa);
                        continue;
                    }
                    surviveMap.put(ov.entityType, ov.name);
                }
                
                updateParentNodesOVs(f, removeList, surviveMap);
                
                if (removeList.size() > 0) {
                    f.arrayIsaContextNodes.removeAll(removeList);
                }
            }
        }
    }

    public void addCLDType(String c, CLD cldType) {
        String mFrag = new StringUtil().getLeft(c);
        String mNode = new StringUtil().getRight(c);
        MFrag f = getMFrag(mFrag);
        MNode childMNode = f.getMNode(mNode);
        childMNode.setCLDs(cldType);
    }

    public void updateCLDs() {
        for (MFrag f : mfrags.keySet()) {
            for (MNode n : f.getMNodes()) { 
                if (n.isContinuous()) {
                    n.setCLDs(new ConditionalGaussian());
                } else if (n.isDiscrete()) {
                    n.setCLDs(new Categorical());
                } 
            }
        }
    }

    public String toString() {
        String s = "[M: " + name + "\r\n";
        for (MFrag m : mfrags.keySet()) {
            s = s + "\t" + m.toString() + "\r\n";
        }
        s = s + "]";
        return s;
    }

    public String toString(String ... inclusion) {
        ArrayList<String> inclusions = new ArrayList<String>();
        int i = 0;
        while (i < inclusion.length) {
            inclusions.add(inclusion[i]);
            ++i;
        }
        String s = "[M: " + name + "\r\n";
        for (MFrag m : mfrags.keySet()) {
            s = s + "\t" + m.toString(inclusions) + "\r\n";
        }
        s = s + "]";
        return s;
    }

    public Double getSumMFragLogScores() {
        Double logSCs = 0.0;
        for (MFrag f : mfrags.keySet()) {
            Double logSC = f.getSumMNodeLogScores();
            logSCs = logSCs + logSC;
            System.out.println(f.toString() + " : " + logSC);
        }
        return logSCs;
    }

    public Double getAvgLogMFragScore() {
        Double logSC = 0.0;
        for (MFrag f : mfrags.keySet()) {
            logSC = logSC + f.getAvglogMNodeScore();
            mfrags.put(f, f.getAvglogMNodeScore());
        }
        return logSC;
    }
 
    public int compareTo(MTheory o) {
        return 0;
    }
}

