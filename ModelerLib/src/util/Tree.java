package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Tree {

    private final static int ROOT = 0;

    public HashMap<String, Node> nodes;
    private TraversalStrategy traversalStrategy;
    private boolean duplicate = true;
    
    // Constructors
    public Tree() {
        this(TraversalStrategy.DEPTH_FIRST);
    }
    
    public Tree(boolean b) {
        this(TraversalStrategy.DEPTH_FIRST);
        duplicate = b;
    }

    public Tree(TraversalStrategy traversalStrategy) {
        this.nodes = new HashMap<String, Node>();
        this.traversalStrategy = traversalStrategy;
    }

    // Properties
    public HashMap<String, Node> getNodes() {
        return nodes;
    }

    public TraversalStrategy getTraversalStrategy() {
        return traversalStrategy;
    }

    public void setTraversalStrategy(TraversalStrategy traversalStrategy) {
        this.traversalStrategy = traversalStrategy;
    }

    // Public interface
    public Node addNode(String identifier) {
        return this.addNode(identifier, null);
    }

    public Node addNode(String identifier, String parent) {
    	if (!duplicate){
    		if (parent != null) {
    			Node node = nodes.get(identifier);
    			if (node != null){ 
    				nodes.get(parent).addChild(identifier);
    				node.setParent(parent);
    				return node;
                
                }
            }	
    	}
    	
        Node node = new Node(identifier);
        node.setParent(parent);
        nodes.put(identifier, node);
        node.setData3(1); // every node has a count number 1

        if (parent != null) {
            nodes.get(parent).addChild(identifier);
        }
        
        return node;
    }

    
    public Node find(String identifier, ArrayList<String> matchingData, int matchingDepth){
    	if (matchingData.size() == matchingDepth){
    		return nodes.get(identifier);
    	}
    	
    	Node node = nodes.get(identifier);
    	Node nodeRet = null; 
    	String mData = matchingData.get(matchingDepth);

    	//System.out.println(node.getData2() + " : " + node.getData1() +  "  " +  "  " + mData + "  " + matchingDepth );
    	
    	for (String c : node.getChildren()){
    		Node nodeChild = nodes.get(c);
    		if (mData.equalsIgnoreCase((String)nodeChild.getData1())){
    			nodeRet = find(c, matchingData, matchingDepth+1);
    		} else {
    			nodeRet = find(c, matchingData, matchingDepth);
    		}
    		
			if (nodeRet != null)
				return nodeRet;
    	}
    	    	
    	return nodeRet;
    }
    
    public Node find(String parentS, Node node, int sizeWindows){

    	for (String c : node.getChildren()){
    		Node nodeChild = nodes.get(c);
    		if (parentS.equalsIgnoreCase((String)nodeChild.getData1())){
    			return nodes.get(c);
    		} else {
    			find(parentS, nodeChild, sizeWindows);
    		}
    	}
    	
    	return null;
    }
    
    public void display(String identifier) {
        this.display(identifier, ROOT, 0);
    }
    
    public void display(String identifier, int displayType) {
        this.display(identifier, ROOT, displayType);
    }

    public void display(String identifier, int depth, int displayType) {
        ArrayList<String> children = nodes.get(identifier).getChildren();

        if (depth == ROOT) {
            System.out.println(nodes.get(identifier).getIdentifier());
        } else {
        	String tabs = String.format("%0" + depth + "d", 0).replace("0", "    "); // 4 spaces
       	 	String tabs2 = String.format("%0" + (depth+1) + "d", 0).replace("0", "    "); // 4 spaces
       	 	String s = tabs + nodes.get(identifier).getIdentifier();
       	 
        	if (displayType == 0){
        		System.out.println(s);   
        	} else 	if (displayType == 1){
        		if (nodes.get(identifier).getData3() != null)
                 	s += "[" + nodes.get(identifier).getData3() + "]";
                 
                 if (nodes.get(identifier).getData4() != null)
                	 s += " [" + nodes.get(identifier).getData4() + "]";
                 
                 System.out.println(s);
        	} else {
        		 System.out.println(s);
        		
                 if (nodes.get(identifier).getData1() != null)
                 	System.out.println(tabs2 + nodes.get(identifier).getData1());
                 
                 if (nodes.get(identifier).getData2() != null)
                 	System.out.println(tabs2 + nodes.get(identifier).getData2());
                 
                 if (nodes.get(identifier).getData3() != null)
                 	System.out.println(tabs2+tabs2 + "[" + nodes.get(identifier).getData3() + "]");
                 
                 if (nodes.get(identifier).getData4() != null)
                 	System.out.println(tabs2 + nodes.get(identifier).getData4());
        	}  
        }
        depth++;
        for (String child : children) {

            // Recursive call
            this.display(child, depth, displayType);
        }
    }

    public Iterator<Node> iterator(String identifier) {
        return this.iterator(identifier, traversalStrategy);
    }

    public Iterator<Node> iterator(String identifier, TraversalStrategy traversalStrategy) {
        return traversalStrategy == TraversalStrategy.BREADTH_FIRST ?
                new BreadthFirstTreeIterator(nodes, identifier) :
                new DepthFirstTreeIterator(nodes, identifier);
    }
}