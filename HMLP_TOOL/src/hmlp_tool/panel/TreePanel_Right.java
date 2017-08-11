package hmlp_tool.panel;  
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;  

import javax.swing.Action; 
import javax.swing.JScrollPane;
import javax.swing.JTree; 
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import hmlp_tool.HMLP_Console;
import hmlp_tool.HMLP_Console.windowMode;
import mebn_rm.MEBN.MFrag.MFrag;
import mebn_rm.MEBN.MNode.MNode;
import mebn_rm.MEBN.MTheory.MTheory; 
import mebn_rm.RDB.RDB; 
  
public class TreePanel_Right extends TreePanel {   
	Action setRun3; 
	TreePanel_Left leftPanel = null;  
	DefaultMutableTreeNode root;
	List<String> selectedObjects = new ArrayList<String>();
	
	public TreePanel_Right(HMLP_Console con, TreePanel_Left left){
		super(con); 
		leftPanel = left;
	}  
	
	public void init(){
		if (console.wMode == HMLP_Console.windowMode.CONNECT_DB){   
			setVisible(false);
			return;
		} else if (console.wMode == HMLP_Console.windowMode.SELECT_DB){   
			setVisible(false);
			return;
		} else if (console.wMode == HMLP_Console.windowMode.EDIT_DB){ 
			setVisible(false);
			return;
		} else if (console.wMode == HMLP_Console.windowMode.ADD_PARENTS){
			changeName("Parent RVs");  
			initTree_MEBN();
		} else if (console.wMode == windowMode.JOIN_RELATIONS) {
			changeName("Conditions");  
			initTree_Conditions();
		} else if (console.wMode == HMLP_Console.windowMode.ADD_CLD){
			changeName("Class Local Distribution");  
			initTree_Select_CLD();
		} else if (console.wMode == windowMode.LEARNING) { 
		} else if (console.wMode == windowMode.EVALUATION) { 
		}
		 
		this.invalidate();
		this.setVisible(true);
		this.repaint();
	}
	  
	public void initTree_MEBN() {
		// Get Database schema from MySQL 
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		root.removeAllChildren();
		model.reload(root);
		
		root.setUserObject("MFrag list");
		MTheory m1 =  this.console.mTheory;
  		
		System.out.println("*After MEBN-RM *************************************************");
		System.out.println(m1.toString("MFrag", "MNode"));
		System.out.println("**************************************************");  
		 
		DefaultMutableTreeNode temp = null;
		DefaultMutableTreeNode temp2 = null; 
				
		for (MFrag m : m1.mfrags.keySet()){
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
	
	public void initTree_Conditions() {
		// Get Database schema from MySQL 
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		root.removeAllChildren();
		model.reload(root);
		
		root.setUserObject("Where Conditions");
		 
		DefaultMutableTreeNode temp = null;
				
		temp = new DefaultMutableTreeNode("default"); root.add(temp); 
//		temp = new DefaultMutableTreeNode("c1 = d1"); root.add(temp);		
		 		 
		model.nodeChanged(root);
		
		expandTree(tree, true);
	}	
	
	public void initTree_Select_CLD() {
		// Get Database schema from MySQL 
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		root.removeAllChildren();
		model.reload(root);
		
		root.setUserObject("CLD Types");
		 
		DefaultMutableTreeNode temp = null;
				
		temp = new DefaultMutableTreeNode("default"); root.add(temp); 
//		temp = new DefaultMutableTreeNode("Continuous"); root.add(temp);
//		temp = new DefaultMutableTreeNode("Sensor_CLD"); root.add(temp);		
		 		 
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

						TreePath[] threePaths = tree.getSelectionPaths();
						
						selectedObjects.clear();
						
						for (TreePath tp: threePaths){
							DefaultMutableTreeNode eachNode = (DefaultMutableTreeNode) tp.getLastPathComponent();
							String strpar = eachNode.getParent().toString();
							String str = strpar + "." + eachNode.toString();
							selectedObjects.add(str);
						}
						
//						String type = (String) node.getUserObject();
//						String str2 = node.toString();
//						String strpar = node.getParent().toString();
						
//						selectedObject = str2;
						
						if (console.wMode == HMLP_Console.windowMode.SELECT_DB){  
							console.insertTextOut("The database \"" + selectedObjects +"\" was selected.");
						} else if (console.wMode == HMLP_Console.windowMode.EDIT_DB){ 
						} else if (console.wMode == HMLP_Console.windowMode.ADD_PARENTS){ 
							console.insertTextOut("The node \"" + selectedObjects +"\" was selected."); 
						} else if (console.wMode == windowMode.JOIN_RELATIONS) {
							
						} else if (console.wMode == HMLP_Console.windowMode.ADD_CLD){ 
							console.insertTextOut("The CLD \"" + selectedObjects +"\" was selected.");
						} else if (console.wMode == windowMode.LEARNING) { 
						} else if (console.wMode == windowMode.EVALUATION) { 
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
	 
	public DefaultMutableTreeNode getTreeNode() {
		// Get Database schema from MySQL 
		List<String> list = RDB.This().getSchemas();
		
		DefaultMutableTreeNode t = new DefaultMutableTreeNode("Database list");
		DefaultMutableTreeNode temp = null;
		
		for (String schema : list){
			temp = new DefaultMutableTreeNode(schema);
			t.add(temp);
		} 
		
		return t;
	}  
}
