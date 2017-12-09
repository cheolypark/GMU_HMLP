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

public class OrderingNetwork extends SearchingPath{
	public SortableValueMap<Integer, ArrayList<Node>> levels = new SortableValueMap<Integer, ArrayList<Node>>();
	
	public OrderingNetwork() {
	}
	
	 public List<Integer> getKeys() {
        ArrayList<Integer> keySet = new ArrayList<Integer>();
        for (Integer key : this.levels.keySet()) {
            keySet.add(key);
        }
        return keySet;
    }

    public List<Integer> getReversedKeys() {
        List<Integer> keySet = this.getKeys();
        ArrayList<Integer> keyReversedSet = new ArrayList<Integer>();
        int i = keySet.size() - 1;
        while (i >= 0) {
            keyReversedSet.add(keySet.get(i));
            --i;
        }
        return keyReversedSet;
    }

    
	public SortableValueMap<Integer, ArrayList<Node>> run(Network net) {
		Node aNode= null;
		if (net.nodes.size() == 0){
			return null;
		}
		
		//add all possible root nodes into level 1
		//          [C] [D]      level 1 {C, D, I, H}
		//            \ /
		//        [I]  E  [H]	
		//          \ / \ / 
		//           F   G 	
		ArrayList<Node> rootNodes = findRootNodes(net); 
		levels.put(1, rootNodes); 
		 
		// search All Node For Levels with rules
		// rule 1. if a child have a parent which is not in the levels, stop.
		// rule 2. if a child has all parents which are in the levels, set this child in the next level of the parent and keep doing.
		//          [C] [D]      level 1 {C, D, I, H}
		//            \ /
		//        [I] [E] [H]	 level 2 {E}, because C and D are in levels 	
		//          \ / \ / 
		//           F   G 
		 
		for (Node root: rootNodes){
			searchAllNodeForLevels(net, root, 1);
		}
		
		levels.sortByKey();
		print();		
		return levels; 
	}
	 
	private Integer getLevelID(Node curNode){
		for (Integer key : levels.keySet()){ 
			for (Node n : levels.get(key)){ 
				if (n.name.equalsIgnoreCase(curNode.name))
					return key;
			}				  
		}
		return -1;
	}
	
	//returning -1 means rule 1.
	//returning non -1 means rule 2.
	private Integer getParentsLevel(Node curNode){
		Integer ret = -1;
		for (Edge e : curNode.inner){
			Integer lvl = getLevelID(e.startNode);
			if (lvl == -1)
				return -1;
			ret = Math.max(ret, lvl);
		}
		
		return ret;
	}
	
	private void searchAllNodeForLevels(Network net, Node curNode, Integer level){
		
		//children 
		for (Edge e : curNode.outer){
			if (!contains(e.endNode)){ // e.endNode is not in levels  
				Integer lvl = getParentsLevel(e.endNode);
				if (lvl != -1 ){
					addToLevel(e.endNode, lvl+1);
					searchAllNodeForLevels(net, e.endNode, (level+1));
				}
			} 
		}
	}
	
	private void searchAllNodeForLevels_temp_function(Network net, Node curNode, Integer level){
		addToLevel(curNode, level);

		// go up
		for (Edge e : curNode.inner){
			if (!contains(e.startNode))
				searchAllNodeForLevels(net, e.startNode, (level-1));
		}

		// go down
		for (Edge e : curNode.outer){
			if (!contains(e.endNode))
				searchAllNodeForLevels(net, e.endNode, (level+1));
		}
	}
	
	public void print(){
		System.out.println("===== Ordered Network =====");
		for (Integer key : levels.keySet()){
			String s = "Level " + key + " [ ";
			for (Node n : levels.get(key)){
				s += n.name + " ";
			}
				
			s += "]";
			System.out.println(s);
		}
	}
	
	public boolean contains(Node curNode){
		for (Integer key : levels.keySet()){
			ArrayList<Node> list = levels.get(key);
			if (list.contains(curNode))
				return true;
		}
		return false;
	}
	
	private void addToLevel(Node curNode, Integer level){
		ArrayList<Node> list = null;
		if (!levels.containsKey(level)){
			list = new ArrayList<Node>();
			levels.put(level, list);
		} else {
			list = levels.get(level);
		}
		list.add(curNode);
	}
	 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//				           N
		//				          /
		//           C    -D-    M
		//            \ /     \ / 
		//         I   E   H   O
		//          \ / \ / \
		//           F   G   K
		//					  \
		//					   L
		Network net = new Network("myNet");
		/*net.add("I", "F");
		net.add("E", "F");
		net.add("E", "G");
		net.add("H", "G");
		net.add("D", "E");
		net.add("C", "E");
		net.add("H", "K");
		net.add("K", "L");
		net.add("D", "O");
		net.add("M", "O");
		net.add("N", "M");*/
	

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
		
		OrderingNetwork on = new OrderingNetwork();
		on.run(net);

	}

}
