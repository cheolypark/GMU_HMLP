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
package network; 

import util.SortableValueMap;
 
/**
 * Network is the class to construct a network. 
 * <p>This contains the followings:
 * <ul>
 * <li>nodes: a list of nodes in the network
 * <li>edges: a list of edges in the network
 * </ul>
 * <p>
 * 
 * @author      Cheol Young Park
 * @version     0.0.1
 * @since       1.5
 */
 
public class Network { 
	public String name = "";
	public SortableValueMap<String, Node> nodes = new SortableValueMap<String, Node>();
	public SortableValueMap<String, Edge> edges = new SortableValueMap<String, Edge>();
	
	public Network(String n){
		name = n;
	}
		
	/**
	 * Used for printing out information for the network. 
	 */
	public void print(){
		Integer i = 1;
		for (String key : edges.keySet()){
			Edge e = edges.get(key);
			System.out.println(i + "["+e.startNode.name + "-" + e.name + "->" + e.endNode.name + "]");
			i++;
		}		
	}
	 
	/**
	 * Used for creating a node from a node name. 
	 * @param nodeName a node name
	 * @return Node a node.  
	 */
	private Node createNode(String nodeName){
		Node n = null;
		if (!nodes.containsKey(nodeName)){
			n = new Node(nodeName);
			nodes.put(nodeName, n);
		} else {
			n = nodes.get(nodeName);
		}
		return n;
	}
	
	/**
	 * Used for creating an edge from an edge name.  
	 * @param edgeName an edge name
	 * @return Edge an edge 
	 */
	private Edge createEdge(String edgeName){
		Edge e = null;
		if (!edges.containsKey(edgeName)){
			e = new Edge(edgeName);
			edges.put(edgeName, e);
		} else {
			e = edges.get(edgeName);
		}
		return e;
	}
	 
	
	/**
	 * Used for linking between two nodes. 
	 * @param node1	a starting node name  
	 * @param node2 an ending node name
	 */
	public void add(String node1,  String node2){
		String edge = "edge" + edges.size();
		add(node1, edge, node2);
	}
	
	/**
	 * Used for linking between two nodes via an edge.
	 * @param node1	a starting node name 
	 * @param edge	an edge name 
	 * @param node2 an ending node name
	 */
	public void add(String node1, String edge, String node2){
		Node n1 = createNode(node1);
		Node n2 = createNode(node2);
		Edge e = createEdge(edge);
		
		e.startNode = n1;
		e.endNode = n2;
		n1.outer.add(e);
		n2.inner.add(e);
	}
	  
	public static void main(String[] args) {
		// An example:
		//           C   D
		//            \ /
		//         I   E   H
		//          \ / \ / 
		//           F   G
		//
		Network net = new Network("myNet");
		net.add("I", "F");
		net.add("E", "F");
		net.add("E", "G");
		net.add("H", "G");
		net.add("D", "E");
		net.add("C", "E");
		net.print();
	}
}
