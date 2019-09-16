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

import network.Edge;
import network.Network;
import network.Node;

/**
 * SearchingPath is the class to search a depth of a network. 	
 * <p>
 * 
 * @author      Cheol Young Park
 * @version     0.0.1
 * @since       1.5
 */
 
public class SearchingPath {
	public SearchingPath(){
		
	}
	 
	/**
	 * Used for returning a list of root nodes in a network.  
	 * @param net	a network
	 * @return		a list of nodes which are root nodes in the network 
	 */
	public ArrayList<Node> findRootNodes(Network net){
		//1. Find top nodes 
		//          [C] [D]     
		//            \ /
		//        [I]  E  [H]	
		//          \ / \ / 
		//           F   G 		
		ArrayList<Node> topNodes = new ArrayList<Node>(); 
		for (String key : net.nodes.keySet()){
			Node n = net.nodes.get(key);
			if (n.inner.isEmpty()){//if non-inner nodes meaning no parent
				topNodes.add(n);
			}
		}
				
		return topNodes;
	} 
	
	/**
	 * Used for finding a depth which is a longest one. 
	 * To do this, root nodes are found and for each root node,
	 * a longest depth are searched. 
	 * @param net	a network
	 * @return	a depth
	 */
	public Integer findLongestDepth(Network net){
		//1. Find top nodes 
		//          [C] [D]     
		//            \ /
		//        [I]  E  [H]	
		//          \ / \ / 
		//           F   G 		
		ArrayList<Node> topNodes = findRootNodes(net); 
		
		//2. Search a top node having longest path  
		//          [C] [D]  		C->E->G : 3, I->F : 2     
		//            \ /			C->E->F : 3, H->G : 2
		//         I   E   H		D->E->G : 3
		//          \ / \ /			D->E->F : 3
		//           F   G 			Thus, C or D can be.
		Integer max = -1;
		Node nodeLongest = null;
		for (Node n : topNodes){
			Integer r = searchAllPath(net, n, 0);
			if( max < r ){
				max = r;
				nodeLongest = n;
			}
		}		
		
		return max;
	}
	
	/**
	 * Used for finding a depth which is a longest one. 
	 * @param net 		a network
	 * @param node		a current node in the search operation
	 * @param number	a current number of depth
	 * @return			a longest depth
	 */
	private Integer searchAllPath(Network net, Node node, int number){
		number++;
		if (node.outer.isEmpty()){
			return number;
		}
		Integer ret = -1;
		for (Edge e: node.outer){
			ret = Math.max(ret, searchAllPath(net, e.endNode, number));
		} 
		
		return ret;
	}
}
