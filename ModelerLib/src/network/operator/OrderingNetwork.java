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
package network.operator;

import java.util.ArrayList;
import java.util.List;
import network.Edge;
import network.Network;
import network.Node;
import util.SortableValueMap;

/**
 * OrderingNetwork is the class to construct an ordered network. The ordered
 * network is a network containing nodes which are in a certain level. In terms
 * of tree structure, a root can be in a first level and the node linked to the
 * root can be in a second level. In this class, these levels are constructed
 * given a network. e.g., ) [C] [D] level 1 {C, D, I, H} \ / [I] [E] [H] level 2
 * {E}, because C and D are in levels \ / \ / F G
 * <p>
 * 
 * @author Cheol Young Park
 * @version 0.0.1
 * @since 1.5
 */

public class OrderingNetwork extends SearchingPath {
	public SortableValueMap<Integer, ArrayList<Node>> levels = new SortableValueMap<Integer, ArrayList<Node>>();

	public OrderingNetwork() {
	}

	/**
	 * Used for returning a list of levels in a network.
	 * 
	 * @return a list of levels
	 */
	public List<Integer> getKeys() {
		ArrayList<Integer> keySet = new ArrayList<Integer>();
		for (Integer key : levels.keySet()) {
			keySet.add(key);
		}
		return keySet;
	}

	/**
	 * Used for returning a list of reversed levels in a network.
	 * 
	 * @return a list of levels
	 */
	public List<Integer> getReversedKeys() {
		List<Integer> keySet = getKeys();
		ArrayList<Integer> keyReversedSet = new ArrayList<Integer>();
		int i = keySet.size() - 1;
		while (i >= 0) {
			keyReversedSet.add(keySet.get(i));
			--i;
		}
		return keyReversedSet;
	}

	/**
	 * Used for returning a map of levels for a network.
	 * 
	 * @param net
	 *            a network
	 * @return a map with a key for level and a list for nodes
	 */
	public SortableValueMap<Integer, ArrayList<Node>> run(Network net) {
		Node aNode = null;
		if (net.nodes.size() == 0) {
			return null;
		}

		// add all possible root nodes into level 1
		// [C] [D] level 1 {C, D, I, H}
		// \ /
		// [I] E [H]
		// \ / \ /
		// F G
		ArrayList<Node> rootNodes = findRootNodes(net);
		levels.put(1, rootNodes);

		// search All Node For Levels with rules
		// rule 1. if a child have a parent which is not in the levels, stop.
		// rule 2. if a child has all parents which are in the levels, set this child in
		// the next level of the parent and keep doing.
		// [C] [D] level 1 {C, D, I, H}
		// \ /
		// [I] [E] [H] level 2 {E}, because C and D are in levels
		// \ / \ /
		// F G

		for (Node root : rootNodes) {
			searchAllNodeForLevels(net, root, 1);
		}

		levels.sortByKey();
		print();
		return levels;
	}

	/**
	 * Used for returning a level index given a node in a network.
	 * 
	 * @param curNode
	 *            a node
	 * @return a level index
	 */
	private Integer getLevelID(Node curNode) {
		for (Integer key : levels.keySet()) {
			for (Node n : levels.get(key)) {
				if (n.name.equalsIgnoreCase(curNode.name))
					return key;
			}
		}
		return -1;
	}

	/**
	 * Used for returning a parent level given a node in a network.
	 * 
	 * @param node
	 *            a node in a network
	 * @return a parent level, while -1 means there is no parent.
	 */
	private Integer getParentsLevel(Node node) {
		Integer ret = -1;
		for (Edge e : node.inner) {
			Integer lvl = getLevelID(e.startNode);
			if (lvl == -1)
				return -1;
			ret = Math.max(ret, lvl);
		}

		return ret;
	}

	/**
	 * Used for checking levels of nodes in a network.
	 * 
	 * @param net
	 *            a network
	 * @param curNode
	 *            a current node in searching
	 * @param level
	 *            a current level
	 */
	private void searchAllNodeForLevels(Network net, Node curNode, Integer level) {

		// children
		for (Edge e : curNode.outer) {
			if (!contains(e.endNode)) { // e.endNode is not in levels
				Integer lvl = getParentsLevel(e.endNode);
				if (lvl != -1) {
					addToLevel(e.endNode, lvl + 1);
					searchAllNodeForLevels(net, e.endNode, (level + 1));
				}
			}
		}
	}

	/**
	 * Used for printing out information for levels of nodes in a network.
	 */
	public void print() {
		System.out.println("===== Ordered Network =====");
		for (Integer key : levels.keySet()) {
			String s = "Level " + key + " [ ";
			for (Node n : levels.get(key)) {
				s += n.name + " ";
			}

			s += "]";
			System.out.println(s);
		}
	}

	/**
	 * Used for checking whether a node is in levels.
	 * 
	 * @param node
	 *            a node for checking
	 * @return True, if the node is in the levels.
	 */
	public boolean contains(Node node) {
		for (Integer key : levels.keySet()) {
			ArrayList<Node> list = levels.get(key);
			if (list.contains(node))
				return true;
		}
		return false;
	}

	/**
	 * Used for adding a node to a level.
	 * 
	 * @param node
	 *            a node for adding
	 * @param level
	 *            a level to which the node is added
	 */
	private void addToLevel(Node node, Integer level) {
		ArrayList<Node> list = null;
		if (!levels.containsKey(level)) {
			list = new ArrayList<Node>();
			levels.put(level, list);
		} else {
			list = levels.get(level);
		}
		list.add(node);
	}

	public void test1() {
		// N
		// /
		// C -D- M
		// \ / \ /
		// I E H O
		// \ / \ / \
		// F G K
		// \
		// L
		Network net = new Network("myNet");
		/*
		 * net.add("I", "F"); net.add("E", "F"); net.add("E", "G"); net.add("H", "G");
		 * net.add("D", "E"); net.add("C", "E"); net.add("H", "K"); net.add("K", "L");
		 * net.add("D", "O"); net.add("M", "O"); net.add("N", "M");
		 */

		net.add("X__g1_t2", "XReport_SG__g1_s1_t2");

		net.add("SecurityGuardType__s1", "XReport_SG__g1_s1_t2");

		net.add("Distance__g1_s1_t2", "XReport_SG__g1_s1_t2");

		net.add("Mission__g1_t2", "Mission__g1_t3");

		net.add("TargetType__g1", "Mission__g1_t3");

		net.add("Situation__t3", "Mission__g1_t3");

		net.add("Activity__g1_t2", "Activity_1Pre__g1_t2");

		net.add("Activity__g1_t1", "Activity_2Pre__g1_t1");

		net.add("Velocity_X__g1_t0", "X__g1_t1");

		net.add("X__g1_t0", "X__g1_t1");

		net.add("IsFirst__t1", "X__g1_t1");

		net.add("Velocity_X__g1_t1", "X__g1_t2");

		net.add("X__g1_t1", "X__g1_t2");

		net.add("IsFirst__t2", "X__g1_t2");

		net.add("Mission__g1_t1", "Mission__g1_t2");

		net.add("TargetType__g1", "Mission__g1_t2");

		net.add("Situation__t2", "Mission__g1_t2");

		net.add("Situation__t2", "Situation__t3");

		net.add("TargetType__g1", "Activity__g1_t2");

		net.add("Mission__g1_t2", "Activity__g1_t2");

		net.add("TargetType__g1", "Activity__g1_t3");

		net.add("Mission__g1_t3", "Activity__g1_t3");

		net.add("Activity_1Pre__g1_t2", "Activity__g1_t3");

		net.add("Activity_2Pre__g1_t1", "Activity__g1_t3");

		net.add("X__g1_t0", "XReport_SG__g1_s1_t0");

		net.add("SecurityGuardType__s1", "XReport_SG__g1_s1_t0");

		net.add("Distance__g1_s1_t0", "XReport_SG__g1_s1_t0");

		net.add("X__g1_t1", "XReport_SG__g1_s1_t1");

		net.add("SecurityGuardType__s1", "XReport_SG__g1_s1_t1");

		net.add("Distance__g1_s1_t1", "XReport_SG__g1_s1_t1");

		net.add("Activity_1Pre__g1_t1", "Activity__g1_t2");

		net.add("Activity_2Pre__g1_t0", "Activity__g1_t2");

		net.add("Activity__g1_t1", "Velocity_X__g1_t1");

		net.add("Velocity_X__g1_t0", "Velocity_X__g1_t1");

		net.add("IsFirst__t1", "Velocity_X__g1_t1");

		net.add("TargetType__g1", "Velocity_X__g1_t1");

		net.add("RegionType__g1_t1", "Velocity_X__g1_t1");

		net.add("Mission__g1_t0", "Mission__g1_t1");

		net.add("TargetType__g1", "Mission__g1_t1");

		net.add("Situation__t1", "Mission__g1_t1");

		net.add("Situation__t1", "Situation__t2");

		net.add("Activity__g1_t1", "Activity_1Pre__g1_t1");

		net.add("Activity__g1_t0", "Activity_2Pre__g1_t0");

		net.add("Region__g1_t1", "RegionType__g1_t1");

		net.add("TargetType__g1", "RegionType__g1_t1");

		net.add("Situation__t0", "Situation__t1");

		net.add("Region__g1_t0", "Region__g1_t1");

		net.print();

		run(net);

	}
	
	public void test2() {
		// I  E  C
		//  \ | /
		//    C
	
		Network net = new Network("myNet");
		net.add("I", "C"); 
		net.add("E", "C"); 
		net.add("C", "C"); 
		net.print(); 
		run(net); 
	}

	public static void main(String[] args) {
		OrderingNetwork on = new OrderingNetwork();
		on.test2();
	}
}
