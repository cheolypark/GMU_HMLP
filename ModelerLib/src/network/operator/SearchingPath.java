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
