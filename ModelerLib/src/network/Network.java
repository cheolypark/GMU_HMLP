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
