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
package mebn_rm.MEBN.MFrag;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mebn_rm.MEBN.CLD.Categorical;
import mebn_rm.MEBN.CLD.ConditionalGaussian;
import mebn_rm.MEBN.MNode.MIsANode;
import mebn_rm.MEBN.MNode.MNode;
import mebn_rm.MEBN.MTheory.MTheory;
import mebn_rm.MEBN.MTheory.OVariable;
import mebn_rm.RDB.RDB;
import mebn_rm.util.StringUtil;
import util.ListMgr;
import util.SortableValueMap;

/**
 * MFrag is the class for a structure of MFrag.
 * <p>
 * MTheory1 [MFrag1, MFrag2, MFrag3]
 * <p>
 * 
 * @author Cheol Young Park
 * @version 0.0.1
 * @since 1.5
 */

public class MFrag implements Comparable<MFrag> {
	public String name;
	public MTheory mTheory = null;
	public List<MIsANode> arrayIsaContextNodes = new ArrayList<MIsANode>();
	public List<MNode> arrayContextNodes = new ArrayList<MNode>();
	public List<MNode> arrayResidentNodes = new ArrayList<MNode>();
	// public List<MNode> arrayInputNodes = new ArrayList<MNode>();
	// public List<MNode> arrayInputPrevNodes = new ArrayList<MNode>();
	public String joiningSQL;
	public String timedPrimaryKey;
	public int childWindowSizeForTimedMFrag = 0;
	public String table = null;
	public String cvsFile = null;
	public SortableValueMap<String, List<String>> mapCausality = new SortableValueMap<String, List<String>>();
	public SortableValueMap<String, List<String>> mapNonCorrelation = new SortableValueMap<String, List<String>>();
	public SortableValueMap<String, List<String>> mapCorrelation = new SortableValueMap<String, List<String>>();
	public LearningType learningType = LearningType.STRUCTURE_HYBRID_DISCRETIZED;
	public MFragType mFragType = MFragType.COMMON;
//	public List<MNode> globalMNodes = new ArrayList<MNode>();

	public MFrag(String mfragName) {
		name = mfragName;
	}

	public MFrag(MTheory m, String mfragName) {
		init(m, mfragName);
	}

	public MFrag(MTheory m, String mfragName, String contextSQL) {
		init(m, mfragName);
		joiningSQL = contextSQL;
	}

	public void init(MTheory m, String mfragName) {
		name = mfragName;
		mTheory = m;
		m.setMFrags(this);
	}

	public void setTableName(String o) {
		table = o;
	}

	public void setTimedMFrag(int childWindowSize, String mPK) {
		timedPrimaryKey = mPK;
		childWindowSizeForTimedMFrag = childWindowSize;
	}

	public String getTableName() {
		if (table != null) {
			return table;
		}

		return name;
	}

	public String getParentTableNames() {
		String s = "";

		for (MNode mn : arrayResidentNodes) {
			for (MNode p : mn.inputParentMNodes) {
				s += p.mFrag.getTableName() + ", ";
			}
		}

		s = s.substring(0, s.length() - 2);
		s = StringUtil.This().removeRedundantItem(s);

		return s;
	}

	public List<String> getRDBKeys() {
		List<String> ret = null;

		if (table != null) {
			ret = (List<String>) mTheory.rdb.mapTableAndKeys.get(table);
		} else {
			ret = (List<String>) mTheory.rdb.mapTableAndKeys.get(name);
		}

		return ret;
	}

	public void setCorrelation(String child, String... parents) {
		List listParents = null;
		if (mapCorrelation.containsKey(child)) {
			listParents = mapCorrelation.get(child);
		} else {
			listParents = new ArrayList();
			mapCorrelation.put(child, listParents);
		}
		int i = 0;
		while (i < parents.length) {
			listParents.add(parents[i]);
			++i;
		}
	}

	public void setNonCorrelation(String child, String... parents) {
		List listParents = null;
		if (mapNonCorrelation.containsKey(child)) {
			listParents = mapNonCorrelation.get(child);
		} else {
			listParents = new ArrayList();
			mapNonCorrelation.put(child, listParents);
		}
		int i = 0;
		while (i < parents.length) {
			listParents.add(parents[i]);
			++i;
		}
	}

	public void setCausality(String child, String... parents) {
		List listParents = null;
		if (mapCausality.containsKey(child)) {
			listParents = mapCausality.get(child);
		} else {
			listParents = new ArrayList();
			mapCausality.put(child, listParents);
		}
		int i = 0;
		while (i < parents.length) {
			listParents.add(parents[i]);
			++i;
		}
	}

	public void updateCausality() {
		for (MNode mn : arrayResidentNodes) {
			List<MNode> mns = mn.getAllParents();
			for (MNode p : mns) {
				setCausality(mn.name, p.name);
			}
		}
	}

	public void updateCLDs() {
		for (MNode n : getMNodes()) {
			if (n.isContinuous()) {
				n.setCLDs(new ConditionalGaussian());
			} else if (n.isDiscrete()) {
				n.setCLDs(new Categorical());
			}
		}
	}

	public MNode getMNode(String s) {
		for (MNode mn : arrayResidentNodes) {
			if (!s.equalsIgnoreCase(mn.name))
				continue;
			return mn;
		}
		return null;
	}

	public MIsANode getIsaMNode(String s) {
		for (MIsANode mn : arrayIsaContextNodes) {
			if (!s.equalsIgnoreCase(mn.toString()))
				continue;
			return mn;
		}
		return null;
	}

	public MNode getContextMNode(String s) {
		for (MNode mn : arrayContextNodes) {
			if (!s.equalsIgnoreCase(mn.toString()))
				continue;
			return mn;
		}
		return null;
	}

	// public MNode getInputMNode(String s) {
	// for (MNode mn : arrayInputNodes) {
	// if (!s.equalsIgnoreCase(mn.toString())) continue;
	// return mn;
	// }
	// return null;
	// }

	public void setMNodes(MNode... mns) {
		MNode[] arrmNode = mns;
		int n = arrmNode.length;
		int n2 = 0;
		while (n2 < n) {
			MNode l = arrmNode[n2];
			l.mFrag = this;
			setMNode(l);
			++n2;
		}
	}

	public void setMNode(MNode mn) {
		for (MNode n : arrayResidentNodes) {
			if (!n.name.equalsIgnoreCase(mn.name))
				continue;
			return;
		}
		arrayResidentNodes.add(mn);
	}

	public boolean removeMNode(MNode mn) {
		arrayResidentNodes.remove(mn);
		return arrayResidentNodes.isEmpty();
	}

	public List<MNode> getAllNodes() {
		List<MNode> array = getMNodes();
		
		for (MNode n : arrayContextNodes) {
			array.add(n);
		}
		
		return array;
	}

	public List<MNode> getMNodes() {
		ArrayList<MNode> array = new ArrayList<MNode>();
		for (MNode n : arrayResidentNodes) {
			array.add(n);
			
			for (MNode p : n.inputParentMNodes) {
				if (!array.contains(p)) {
					array.add(p);
				}
			}
		}
		 
		return array;
	}

	public List<OVariable> getOVsInMNodes() {
		List<OVariable> allOvs = new ArrayList<OVariable>();
		for (MNode n : arrayResidentNodes) {
			for (OVariable ov : n.ovs) {
				if (!allOvs.contains(ov)) {
					allOvs.add(ov);
				}
			}

			for (MNode input : n.inputParentMNodes) {
				for (OVariable ov : input.ovs) {
					if (!allOvs.contains(ov)) {
						allOvs.add(ov);
					}
				}
			}
		}

		return allOvs;
	}

	public void removeAllUnnecessaryIsANodes() {
		List<OVariable> allOvs = getOVsInMNodes();

		// k, 2, k, 1, 4 i = 0, last = 4 [check for i, if i==k then switch i and last,
		// and i++ and last--]
		// 4, 2, k, 1, k i = 1, last = 3 [check for i, if i==k then switch i and last,
		// and i++ and last--]
		// 4, 2, k, 1, k i = 2, last = 3 [check for i, if i==k then switch i and last,
		// and i++ and last--]
		// 4, 2, 1, k, k i = 2, last = 3 [check for i, if i==k then switch i and last,
		// and i++ and last--]
		//
		int last = arrayIsaContextNodes.size() - 1;
		for (int i = 0; i < arrayIsaContextNodes.size(); i++) {
			MIsANode isa = arrayIsaContextNodes.get(i);

			if (!allOvs.contains(isa.ovs.get(0))) {
				System.out.println(isa + " " + last + " " + i);

				MIsANode isaLast = arrayIsaContextNodes.get(last);
				arrayIsaContextNodes.set(last, isa);
				arrayIsaContextNodes.set(i, isaLast);
				last--;

				if (!allOvs.contains(arrayIsaContextNodes.get(i))) {
					i--;
				}

			} else {
				System.out.println("exsiting " + isa);
			}

			if (last == i)
				break;
		}

		// Remove all unnecessary nodes
		for (int i = (arrayIsaContextNodes.size() - 1); last < i; i--) {
			System.out.println("del" + last + " " + i);
			arrayIsaContextNodes.remove(i);
		}
	}

	/*
	 * [C: IsA(rm_pass_PASS_NO, PASS)] [C: IsA(rm_pass_SLAB_NO, PRODUCT)] X =
	 * "PASS_NO", then [SLAB_NO] is returned.
	 */
	public List<String> getKeysExceptX(String X) {
		List<String> ret = new ArrayList<String>();

		// get keys except the timedPK
		for (MIsANode isaNode : arrayIsaContextNodes) {
			OVariable ov = isaNode.ovs.get(0);
			if (!X.equalsIgnoreCase(ov.originKey)) {
				ret.add(ov.originKey);
			}
		}
		return ret;
	}

	public boolean isTimedMFrag() {
		if (childWindowSizeForTimedMFrag == 0)
			return false;
		return true;
	}

	// public List<MNode> getInputPrevNodes() {
	// ArrayList<MNode> array = new ArrayList<MNode>();
	// for (MNode s : arrayInputPrevNodes) {
	// array.add(s);
	// }
	// return array;
	// }

	public Double getSumMNodeLogScores() {
		Double logSCs = 0.0;
		for (MNode mn : arrayResidentNodes) {
			Double logSC = mn.getlogCLDScore();
			logSCs = logSCs + logSC;
		}
		return logSCs;
	}

	public Double getAvglogMNodeScore() {
		Double logSC = 0.0;
		for (MNode mn : arrayResidentNodes) {
			logSC = logSC + mn.getAvglogCLDScore();
		}
		return logSC;
	}

	public void initSelectedDataset(int size) {
		System.out.println("init Selected Dataset for the MFrag : " + name);
		ResultSet rs = null;

		if (joiningSQL == null) {
			if (this.mFragType == MFragType.COMMON) { // For MFragType.REFERENCE, we don't make a data set.
				String attrs = "";
				String strTables = name;

				for (MNode mn : getMNodes()) {
					attrs += mn.getAttributeName() + " as " + mn.name + ", ";
				}

				if (!attrs.isEmpty()) {
					attrs = attrs.substring(0, attrs.length() - 2);
					rs = RDB.This().get(attrs, strTables);
				}

				cvsFile = RDBToCVS(name, mTheory.name, rs);

				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else {
			String sql = joiningSQL;
			if (isTimedMFrag()) {
				List<String> keyList = getKeysExceptX(timedPrimaryKey);
				String keys = new ListMgr().getListComma(keyList);
				sql = sql.replaceFirst(keys + ",", "");
			}

			rs = RDB.This().get(sql);
			cvsFile = RDBToCVS(name, mTheory.name, rs);
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// create default cvs files for each node
			// To do: for the default local distribution, develop the following code.
			// for (MNode mn : getMNodes()) {
			// String s = "SELECT " + mn.getAttributeName() + " as " + mn.name + "\n";
			// s += "FROM " + "\n";
			// s += getTableName() + "\n";
			// s += "where not exists (" + "\n";
			// s += "SELECT *" + "\n";
			// s += "FROM " + getParentTableNames() + "\n";
			// String afterwhere = joiningSQL.substring(joiningSQL.indexOf("WHERE"),
			// joiningSQL.length());
			// s += afterwhere + "\n";
			// s += ") " + "\n";
			//
			// rs = RDB.This().get(s);
			// mn.cvsFile = RDBToCVS(mn.name+"_default", mTheory.name, rs);
			// try {
			// rs.close();
			// } catch (SQLException e) {
			// e.printStackTrace();
			// }
			// }
		}
	}

	public String RDBToCVS(String file, String folder, ResultSet rs) {
		if (rs != null) {
			try {
				try {
					return RDB.This().toExcel(file, folder, rs);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return "";
	}

	public String toString() {
		ArrayList<String> inclusions = new ArrayList<String>();
		inclusions.add("MFrag");
		inclusions.add("MNode");
		inclusions.add("CLD");
		return toString(inclusions);
	}

	public String toString(List<String> inclusions) {
		String s = "";
		if (inclusions.contains("MFrag")) {
			String context;
			s += "[F: " + name + "\r\n";
			for (MIsANode c2 : arrayIsaContextNodes) {
				context = "[C: " + c2.toString() + "]" + "\r\n";
				s += "\t\t" + context;
			}
			for (MNode c : arrayContextNodes) {
				context = c.toString(inclusions);
				context = context.replace("R:", "C:");
				s += "\t\t" + context;
			}
			for (MNode r : arrayResidentNodes) {
				context = r.toString(inclusions);
				s += "\t\t" + context;
			}
		}
		if (inclusions.contains("MFrag")) {
			s += "\t]";
		}
		return s;
	}

	public int compareTo(MFrag o) {
		return 0;
	}

	public void resetContextNodes() {
		for (MNode mn : arrayResidentNodes) {
			// System.out.println(mn.ovs);
			for (MNode rp : mn.parentMNodes) {
				// System.out.println(rp.ovs);
			}
			for (MNode ip : mn.inputParentMNodes) {
				// System.out.println(ip.ovs);
			}
		}
	}

	public static enum LearningType {
		BAYES, PARAMETER, STRUCTURE, STRUCTURE_HYBRID_DISCRETIZED
	}

	public static enum MFragType {
		COMMON, REFERENCE
	}
}
