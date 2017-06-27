package network;

import java.util.ArrayList;
 

public class Node {
	public String name = "";
	public ArrayList<Edge> inner = new ArrayList<Edge>();	
	public ArrayList<Edge> outer = new ArrayList<Edge>();
	
	protected Node(String n){
		name = n;
	}
	
	public String getName(){
		return name;
	}
	
	
}
