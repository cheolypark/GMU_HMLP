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

import java.util.ArrayList;
 

/**
 * Node is the class for a network.
 * <p>This contains the followings:
 * <ul>
 * <li>inner: a list of incoming edges
 * <li>outer: a list of outgoing edges
 * </ul>
 * <p> 
 * 
 * @author      Cheol Young Park
 * @version     0.0.1
 * @since       1.5
 */

public class Node {
	public String name = "";
	public ArrayList<Edge> inner = new ArrayList<Edge>();	
	public ArrayList<Edge> outer = new ArrayList<Edge>();
	
	protected Node(String n){
		name = n;
	}
	
	/**
 	 * Used for returning this node name. 
	 * @return String a node name.  
	 * @see #Network()
	 */
	public String getName(){
		return name;
	} 
}
