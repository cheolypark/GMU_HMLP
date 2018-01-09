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
package hml_tool; 
 
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List; 
 
import javax.swing.JScrollPane;
import javax.swing.JTree; 
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import hml_tool.HML_Console.windowMode;
import mebn_rm.MEBN.MFrag.MFrag;
import mebn_rm.MEBN.MNode.MNode; 
import mebn_rm.RDB.RDB; 
import mebn_rm.util.StringUtil;
import util.gui.TreePanel;
  

/**
 * TreePanel_Left is the class for a left tree panel. 
 * <p>
 * 
 * @author      Cheol Young Park
 * @version     0.0.1
 * @since       1.5
 */

public class TreePanel_Left extends TreePanel {   	
	private static final long serialVersionUID = -1341074971312856857L;
	public String selectedObject = "";
	public String username = "root";
	public String password = "jesus"; 
	public String address = "localhost";
	 
	DefaultMutableTreeNode root;
	 
	public TreePanel_Left(HML_Console con){
		super(con); 
	}
	
	public void init(){
		
		if (((HML_Console)console).wMode == HML_Console.windowMode.CONNECT_DB){   
			setVisible(false);
			return;
		} else if (((HML_Console)console).wMode == HML_Console.windowMode.SELECT_DB){
			changeName("Select DB"); 
			initTree_SELECT_DB(); 
		} else if (((HML_Console)console).wMode == HML_Console.windowMode.EDIT_DB){
			changeName("Edit DB"); 
			initTree_EDIT_DB();
		} else if (((HML_Console)console).wMode == HML_Console.windowMode.ADD_PARENTS){
			changeName("Child RVs"); 
			initTree_ADD_PARENTS(); 
			((HML_Console)console).showMTheory();
		} else if (((HML_Console)console).wMode == windowMode.JOIN_RELATIONS) {			
		} else if (((HML_Console)console).wMode == HML_Console.windowMode.ADD_CLD){
			changeName("Add CLD");
		} else if (((HML_Console)console).wMode == windowMode.LEARNING) { 
		} else if (((HML_Console)console).wMode == windowMode.EVALUATION) { 
		}
		 
		this.invalidate();
		this.setVisible(true);
		this.repaint();
	}
	
	public void initTree_SELECT_DB() {
		// Get Database schema from MySQL
		RDB.This().connect(address, username, password);
		List<String> list = RDB.This().getSchemas();
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		
		root.setUserObject("Database");
		DefaultMutableTreeNode temp = null;
		
		for (String schema : list){
			temp = new DefaultMutableTreeNode(schema);
			root.add(temp);
		} 
		 
		model.nodeChanged(root);
		
		expandTree(tree, true);
	}
	 
	public void initTree_EDIT_DB() {
		String database = new StringUtil().getRight(selectedObject); 
		
		// Step 1. initialize RDB
		try {
			RDB.This().init(database);
		} catch (SQLException e) { 
			e.printStackTrace();
		}
		
		// Get Database schema from MySQL 
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		root.removeAllChildren();
		model.reload(root);
		
		root.setUserObject("Table list");
		DefaultMutableTreeNode temp = null;
		DefaultMutableTreeNode temp2 = null;
		List<String> attrs = null;
				
		// Get Database schema from MySQL
		List<String> list = RDB.This().getEntityTables();
		
		for (String t : list){
			temp = new DefaultMutableTreeNode(t);
			root.add(temp); 
			
			attrs =  (List<String>)RDB.This().mapTableAndKeys.get(t);
			if (attrs != null){
				for (String attr: attrs) {
					temp2 = new DefaultMutableTreeNode("PK:"+attr);
					temp.add(temp2); 
				}
			} 
			
			attrs =  (List<String>)RDB.This().mapTableAndAttributes.get(t);
			if (attrs != null){
				for (String attr: attrs) {
					temp2 = new DefaultMutableTreeNode(attr);
					temp.add(temp2); 
				}
			} 
		}
		
		list = RDB.This().getRelationshipTables();
		
		if (list != null) {
			for (String t : list){
				temp = new DefaultMutableTreeNode(t);
				root.add(temp);
				 
				attrs =  (List)RDB.This().mapTableAndKeys.get(t);
				if (attrs != null){
					for (String attr: attrs) {
						temp2 = new DefaultMutableTreeNode("PK:"+attr);
						temp.add(temp2); 
					}
				} 
				
				attrs =  (List)RDB.This().mapTableAndAttributes.get(t);
				if (attrs != null){
					for (String attr: attrs) {
						temp2 = new DefaultMutableTreeNode(attr);
						temp.add(temp2); 
					}
				} 
			}
		}
		 
 		model.nodeChanged(root);
 
 		expandTree(tree, true); 
	}	
	  
	public void initTree_ADD_PARENTS() {
		// Get Database schema from MySQL 
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		root.removeAllChildren();
		model.reload(root);
		
		root.setUserObject("MFrag list"); 
  		
		System.out.println("*After MEBN-RM *************************************************");
		System.out.println(((HML_Console)console).mTheory.toString("MFrag", "MNode"));
		System.out.println("**************************************************");  
		 
		DefaultMutableTreeNode temp = null;
		DefaultMutableTreeNode temp2 = null; 
				
		for (MFrag m : ((HML_Console)console).mTheory.mfrags.keySet()){
			temp = new DefaultMutableTreeNode(m.name);
			root.add(temp); 
			
			for (MNode r: m.arrayResidentNodes) {
				temp2 = new DefaultMutableTreeNode(r.name);
				temp.add(temp2); 
			} 
		}
		  
		model.nodeChanged(root);
		
		expandTree(tree, true);
	}	
	 
	public JScrollPane createTreePane() { 
		// new tree
		root = new DefaultMutableTreeNode("Root");
		tree = new JTree(root);
		tree.setEditable(false);

		// click tree
		tree.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
				if (selPath != null) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.getLastPathComponent();
					if (node.isLeaf()) {

						String type = (String) node.getUserObject();
						String strpar = node.getParent().toString();
						String strcur = node.toString(); 
						  
						selectedObject = strpar +"." + strcur;
						
						if (((HML_Console)console).wMode == HML_Console.windowMode.SELECT_DB){  
							((HML_Console)console).insertTextOut("The database \"" + selectedObject +"\" was selected.");
						} else if (((HML_Console)console).wMode == HML_Console.windowMode.EDIT_DB){ 
						} else if (((HML_Console)console).wMode == HML_Console.windowMode.ADD_PARENTS){ 
							((HML_Console)console).insertTextOut("The node \"" + selectedObject +"\" was selected.");
						} else if (((HML_Console)console).wMode == windowMode.JOIN_RELATIONS) {
							
						} else if (((HML_Console)console).wMode == HML_Console.windowMode.ADD_CLD){ 
							((HML_Console)console).insertTextOut("The node \"" + selectedObject +"\" was selected.");
						} else if (((HML_Console)console).wMode == windowMode.LEARNING) { 
						} else if (((HML_Console)console).wMode == windowMode.EVALUATION) { 
						} 
					}
				}
			}
		});

		// Expand all
		for (int i = 0; i < tree.getRowCount(); i++)
			tree.expandRow(i);

		return new JScrollPane(tree);
	}   
}
