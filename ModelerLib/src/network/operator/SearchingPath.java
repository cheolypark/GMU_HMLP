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

public class SearchingPath {
	public SearchingPath(){
		
	}
	
	public ArrayList<Node>  findRootNodes(Network net){
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
	
	private Integer searchAllPath(Network net, Node curNode, int number){
		number++;
		if (curNode.outer.isEmpty()){
			return number;
		}
		Integer ret = -1;
		for (Edge e: curNode.outer){
			ret = Math.max(ret, searchAllPath(net, e.endNode, number));
		} 
		
		return ret;
	}
}
