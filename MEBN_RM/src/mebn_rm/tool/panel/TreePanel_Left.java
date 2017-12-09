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
package mebn_rm.tool.panel; 
 
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;  
import javax.swing.JScrollPane;
import javax.swing.JTree; 
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;   
import mebn_rm.MEBN.MFrag.MFrag;
import mebn_rm.MEBN.MNode.MNode; 
import mebn_rm.RDB.RDB;
import mebn_rm.tool.MEBN_RM_Console;
import mebn_rm.util.StringUtil; 
  
public class TreePanel_Left extends TreePanel {  

	/**
	 * 
	 */
	private static final long serialVersionUID = -4051838457750106644L;

	public String selectedObject = "";
	 
	DefaultMutableTreeNode root;
	 
	public TreePanel_Left(MEBN_RM_Console con){
		super(con); 
	}
	
	public void init(){
		if (console.wMode == MEBN_RM_Console.windowMode.SELECT_DB){
			changeName("Select DB"); 
			initTree_SELECT_DB(); 
		} else if (console.wMode == MEBN_RM_Console.windowMode.EDIT_DB){ 
			initTree_EDIT_DB();
		}  
		 
		this.invalidate();
		this.setVisible(true);
		this.repaint();
	}
	
	public void initTree_SELECT_DB() {
		RDB.This().connect("localhost", "root", "jesus");
		// Get Database schema from MySQL
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
	}	
	  
	public void initTree_ADD_PARENTS() {
		// Get Database schema from MySQL 
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		root.removeAllChildren();
		model.reload(root);
		
		root.setUserObject("MFrag list"); 
  		
		System.out.println("*After MEBN-RM *************************************************");
		System.out.println(console.mTheory.toString("MFrag", "MNode"));
		System.out.println("**************************************************");  
		 
		DefaultMutableTreeNode temp = null;
		DefaultMutableTreeNode temp2 = null; 
				
		for (MFrag m : console.mTheory.mfrags.keySet()){
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
 
						String strpar = node.getParent().toString();
						String strcur = node.toString(); 
						  
						selectedObject = strpar +"." + strcur;
						
						if (console.wMode == MEBN_RM_Console.windowMode.SELECT_DB){  
							console.insertTextOut("The database \"" + selectedObject +"\" was selected.");
						} else if (console.wMode == MEBN_RM_Console.windowMode.EDIT_DB){  
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
