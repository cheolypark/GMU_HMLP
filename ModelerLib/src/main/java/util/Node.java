package util;

import java.util.ArrayList;

public class Node {

    private String identifier;
    private String parent;
    private Object data1;
    private Object data2;
    private Object data3;
    private Object data4;
    private ArrayList<String> children;

    // Constructor
    public Node(String identifier) {
        this.identifier = identifier;
        children = new ArrayList<String>();
    }

    // Properties
    public String getIdentifier() {
        return identifier;
    }
    
    public String getParent() {
        return parent;
    }

    public ArrayList<String> getChildren() {
        return children;
    }

    // Public interface
    public void addChild(String identifier) {
    	if (!children.contains(identifier))
    		children.add(identifier);
    }
    
    public void setParent(String s) {
        parent = s;
    }
    
    public void setData1(Object d){
    	data1 = d;
    }
    
    public void setData2(Object d){
    	data2 = d;
    }
    
    public void setData3(Object d){
    	data3 = d;
    }
    
    public void setData4(Object d){
    	data4 = d;
    }
    
    public Object getData1(){
    	return data1;
    }
    
    public Object getData2(){
    	return data2;
    }
    
    public Object getData3(){
    	return data3;
    }
    
    public Object getData4(){
    	return data4;
    }
}