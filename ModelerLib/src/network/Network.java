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
 
public class Network { 
	public String name = "";
	public SortableValueMap<String, Node> nodes = new SortableValueMap<String, Node>();
	public SortableValueMap<String, Edge> edges = new SortableValueMap<String, Edge>();
	
	public Network(String n){
		name = n;
	}
	
	public void print(){
		Integer i = 1;
		for (String key : edges.keySet()){
			Edge e = edges.get(key);
			System.out.println(i + "["+e.startNode.name + "-" + e.name + "->" + e.endNode.name + "]");
			i++;
		}		
	}
	
	private Node createNode(String node){
		Node n = null;
		if (!nodes.containsKey(node)){
			n = new Node(node);
			nodes.put(node, n);
		} else {
			n = nodes.get(node);
		}
		return n;
	}
	
	private Edge createEdge(String edge){
		Edge e = null;
		if (!edges.containsKey(edge)){
			e = new Edge(edge);
			edges.put(edge, e);
		} else {
			e = edges.get(edge);
		}
		return e;
	}
	 
	public void add(String node1,  String node2){
		String edge = "edge" + edges.size();
		add(node1, edge, node2);
	}
	
	public void add(String node1, String edge, String node2){
		Node n1 = createNode(node1);
		Node n2 = createNode(node2);
		Edge e = createEdge(edge);
		
		e.startNode = n1;
		e.endNode = n2;
		n1.outer.add(e);
		n2.inner.add(e);
	}
	 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
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
