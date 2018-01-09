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
import util.ListMgr;
import util.SortableValueMap;
  
/**
 * MTheory is the class for a structure of MTheory.
 * <p>
 * 
 * @author      Cheol Young Park
 * @version     0.0.1
 * @since       1.5
 */

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
//                String originEntity = rdb.mapKeysOrigins.get(key);
            	String originEntity = rdb.getOriginFromKey(newMFrag.table, key);
                OVariable ov = new OVariable(f.getTableName(), key, originEntity);
//                newMFrag.arrayIsaContextNodes.add(new MIsANode(f, ov));
        		new MIsANode(newMFrag, ov);
            }

            // Add parent nodes
            List<MFrag> parentMFrags = new ArrayList<MFrag>();
            for (String p2 : ps) {
            	            	
                String mFragP = new StringUtil().getLeft(p2);
                String mNodeP = new StringUtil().getRight(p2);
                
//                System.out.println(mFragP + ":" + mNodeP);
                
                MFrag fp = getMFrag(mFragP);
                MNode parentMNode = fp.getMNode(mNodeP);
                if (!parentMFrags.contains(fp)) {
                	parentMFrags.add(fp);
                }

                // create a joining sql for child and parent nodes
                if (!fp.isTimedMFrag()){
	                sql += fp.getTableName() + "." + parentMNode.getAttributeName() + " as " + mNodeP + ",\r\n";
//	                sqlFrom += fp.getTableName() + ", ";
                } else { // is a TimedMFrag 
                	sql += fp.getTableName() + "." + mNodeP + " as " + mNodeP + ",\r\n";
//	                sqlFrom += " ( " +  fp.joiningSQL + " ) " + fp.getTableName();  
//	                sqlFrom += ", ";
                }
                
                List<String> keys2 = fp.getRDBKeys();
                List<OVariable> listOV = new ArrayList<OVariable>(); 
                
                // Add Isa Context Nodes for the new MFrag from the parent MFrags
                for (String key2 : keys2) {
                    String originEntity = rdb.getOriginFromKey(fp.getTableName(), key2);
                    OVariable ov = new OVariable(fp.getTableName(), key2, originEntity);
//                    newMFrag.arrayIsaContextNodes.add(new MIsANode(f, ov));
                    new MIsANode(newMFrag, ov);
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
//            sqlFrom = sqlFrom.substring(0, sqlFrom.length() - 2);
//            System.out.println(sqlFrom);
//            sqlFrom = new StringUtil().removeRedundantItem(sqlFrom);
            
            for (MFrag fp : parentMFrags){
	            if (fp.joiningSQL == null){
	                sqlFrom += fp.getTableName() + ", ";
	            } else { // is a TimedMFrag 
	                sqlFrom += " ( " +  fp.joiningSQL + " ) " + fp.getTableName();  
	                sqlFrom += ", ";
	            }
            }
            
            sqlFrom = sqlFrom.substring(0, sqlFrom.length() - 2);
            sql = sql + (String)sqlFrom + "\r\n";
            
            // set a where clause
            
//            System.out.println(sql);
             
            if (c.equalsIgnoreCase("quality_result.QR_RESULT_COL_1")){
            	System.out.println();
            }
            
            newMFrag.joiningSQL = sql;
            
            // If there is no more node, then delete this MFrag 
            if (f.removeMNode(childMNode)) {
                removeMFrag(f);
            }
        } else { // If added parents are in the same MFrag
        	
            for (String p3 : ps) {
                String mFragP = new StringUtil().getLeft(p3);
                String mNodeP = new StringUtil().getRight(p3);
                MNode parentMNode = f.getMNode(mNodeP);
                childMNode.setParents(parentMNode);
            }
        }
    }
    
    // If this MFrag is a timed MFrag, a special timed table is created and it is used for joining 
    /*
     * 	select 
     * 		SLAB_NO, 
	 *  	sum(A) as A,
	 * 		sum(B) as B,
	 * 		sum(C) as C,  
	 *      sum(A1) as A1,
	 *     	sum(B1) as B1,
	 *      sum(C1) as C1 
	 *	from 
	 *		( select
	 * 			t2.*,
	 *			case when mod(row_num, 3) = 1 then RM_PRE_WK end as A, 
	 *			case when mod(row_num, 3) = 2 then RM_PRE_WK end as B,
	 *			case when mod(row_num, 3) = 0 then RM_PRE_WK end as C, 
	 *
	 *          case when mod(row_num, 3) = 1 then RM_ACT_WK end as A1, 
	 *        	case when mod(row_num, 3) = 2 then RM_ACT_WK end as B1,
	 *          case when mod(row_num, 3) = 0 then RM_ACT_WK end as C1, 
	 *          
	 *			case when row_num / 3 > 1     then FLOOR ((row_num -1) / 3) end as flag_for_windows  
	 * 		from (
	 *	 		select t.*, 
	 *		  		( case SLAB_NO when @curtype then @curRow := @curRow + 1 
	 *											 else @curRow := 1 and @curType := SLAB_NO end
	 *		  		) + 1 as row_num
	 *			from rm_pass t,
	 *		  		(select @curRow := 0, @curType := '') r
	 *			order BY SLAB_NO asc  
	 *	  		) t2
	 *		) t1 
 	 *		group by flag_for_windows, SLAB_NO 
     */ 
    public void createTimedTable(MFrag f) { 
	    if (!f.isTimedMFrag()) {
	    	return;
	    }
	    
	    int cw = f.childWindowSizeForTimedMFrag;
	    String sqlTimed = "";
	    sqlTimed += "select \r\n";
	    
	    ListMgr l = new ListMgr();
	    List<String> keyList = f.getKeysExceptX(f.timedPrimaryKey);
	    String keys = l.getListComma(keyList);
	    // To do: this version works only a single keys (e.g., [SLAB_NO]),
	    // but this version doens't work for a composite keys (e.g., [SLAB_NO, MACHINE_NO]).
	    // So, change the sqlTimed.
	    
	    sqlTimed += keys + ", \r\n";
	    
	    for (MNode n : f.arrayResidentNodes){
	    	for (int k = 1; k < (cw+1); k++) {
	    		sqlTimed += "sum(" + n.name +"_"+ k + " ) as " + n.name +"_"+ k + ", \r\n";
	    	}
	    } 
	    sqlTimed =  sqlTimed.substring(0, sqlTimed.length()-4);
 
	    sqlTimed += "\r\n from \r\n";
	    sqlTimed += "	( select \r\n";
	    sqlTimed += "	t2.*, \r\n";
	    
	    for (MNode n : f.arrayResidentNodes){
	    	int i = 0; 
	    	for (int k = 1; k < (cw+1); k++) {
	    		i++;
		    	if (i == cw) {
		    		i = 0;
		    	}
	    		sqlTimed += "case when mod(row_num, " + cw + " ) = " + i + " then " + n.attribute + " end as " + n.name +"_"+ k + ", \r\n";
	    	}
	    }
	    
	    sqlTimed += "case when row_num / " + cw + " > 1 then FLOOR ((row_num -1) / " + cw + " ) end as flag_for_windows \r\n";
	    sqlTimed += "from ( \r\n";
	    sqlTimed += "select t.*, \r\n";
	    sqlTimed += "( case " + keys + " when @curtype then @curRow := @curRow + 1 \r\n";
	    sqlTimed += "else @curRow := 1 and @curType := " + keys + " end \r\n";
	    sqlTimed += ") + 1 as row_num \r\n";
	    sqlTimed += "from " + f.name + " t, \r\n";
	    sqlTimed += "(select @curRow := 0, @curType := '') r \r\n";
	    sqlTimed += "order BY " + keys + " asc \r\n";
	    sqlTimed += ") t2 \r\n";
	    sqlTimed += ") t1 \r\n";
	    sqlTimed += "group by flag_for_windows, " + keys + " \r\n"; 
//	    System.out.println(sqlTimed);
	    f.joiningSQL = sqlTimed;
	  
    }
    public void setChildWindowSize(String timedPK, int childWindowSize) {
    	String mFrag = new StringUtil().getLeft(timedPK);
        String mPK = new StringUtil().getRight(timedPK);
        MFrag f = getMFrag(mFrag);
         
        if (childWindowSize <= 1 ){
    		return;
    	}  
        
        f.setTimedMFrag(childWindowSize, mPK);  
        
        createTimedTable(f);
         
        List<MNode> newList = new ArrayList<MNode>(f.arrayResidentNodes);
        
        for (MNode mn : newList) { 
        	// create new MNodes according to childWindowSize
        	for (int i = 1; i < childWindowSize; i++) {
        		MNode newMN = null;
                if (mn.isContinuous()) {
                	newMN = new MCNode(f, mn.name + "_" + (i+1), mn.ovs);
                } else if (mn.isDiscrete()) {
                	newMN = new MDNode(f, mn.name + "_" + (i+1), mn.ovs);
                }
                
                newMN.setAttributeName(mn.attribute);
                
                f.setMNode(newMN);
        	}
        	
        	mn.name += "_" + 1;
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
            OVariable ov2;
            
            if (f.name.equalsIgnoreCase("fm_pass")) {
            	  System.out.println(f.joiningSQL);
            } 
            
            if (f.joiningSQL != null) { 
            	// Step 1. Add "where" clause to the SQL script of this MFrag
                // e.g.,)
                // quality_result_SLAB_NO  quality_result
                // rm_pass_PASS_NO  rm_pass
                // rm_pass_SLAB_NO  rm_pass
                // rm_pass_PASS_NO  rm_pass
                // rm_pass_SLAB_NO  rm_pass
                // -> where quality_result.SLAB_NO == rm_pass.SLAB_NO
                // If there are tables and they have same keys, then create the "where" clause 
                //
                List<String> dubList = new ArrayList<String>(); 
                String sql = "WHERE \r\n";
                for (int i = 0; i < f.arrayIsaContextNodes.size(); i++) {
                	MIsANode isa = f.arrayIsaContextNodes.get(i);
                	ov = (OVariable)isa.ovs.get(0);
                	MFrag orgF = getMFrag(ov.originMFrag);
                	
                	// timedPrimaryKey will be skipped.
         			if (orgF != null &&orgF.timedPrimaryKey != null && orgF.timedPrimaryKey.equalsIgnoreCase(ov.originKey)){
        				continue;
        			}
        			
                	for (int j = 0; j < i+1; j++) {
                		MIsANode isa2 = f.arrayIsaContextNodes.get(j);
                    	ov2 = (OVariable)isa2.ovs.get(0);
                    	if (!ov.originMFrag.equalsIgnoreCase(ov2.originMFrag)){
                    		if (ov.originKey.equalsIgnoreCase(ov2.originKey)){
//                    		if (ov.entityType.equalsIgnoreCase(ov2.entityType)){
//	                    		System.out.println(ov + ":" + ov.originMFrag + "   <->   " + ov2 + ":" + ov2.originMFrag);
	                    		String curKey = ov.originMFrag + "." + ov.originKey;
	                    		String otherKey = ov2.originMFrag + "." + ov2.originKey;
	                    		String wh = curKey + " = " + otherKey;
	                    		
	                    		if(wh.equalsIgnoreCase("fm_pass.PASS_NO = rm_pass.PASS_NO")){
	                    			System.out.println();
	                    		}
	                    		
	                    		if (!dubList.contains(wh)) {
		                    		dubList.add(wh);
		                    		sql += wh + " &&\r\n";
	                    		}
                			}
                    	}
                    }
                } 
                
                if (dubList.size() > 0) {
		            sql = sql.substring(0, sql.length() - 5);
		            f.joiningSQL = f.joiningSQL + sql;
                }
                // Step 2. remove redundant OVs, if they are same OVs
	            // e.g.) IsA(t1, TIME), IsA(t2, TIME)
	            // => IsA(t1, TIME)
	            // Also, resident nodes using these OVs changes to not having redundant OVs
                surviveMap = new HashMap<String, String>();
                removeList = new ArrayList<MIsANode>();
                
                for (MIsANode isa : f.arrayIsaContextNodes) {
                    ov = (OVariable)isa.ovs.get(0);
//                    System.out.println(ov.name + "  " + ov.originMFrag);
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
                
            } else if (f.joiningSQL == null) {
//	            System.out.println(f.arrayContextNodes);
	            
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

