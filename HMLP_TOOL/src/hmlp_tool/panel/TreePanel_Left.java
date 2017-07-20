package hmlp_tool.panel; 
 
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List; 
 
import javax.swing.JScrollPane;
import javax.swing.JTree; 
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import hmlp_tool.HMLP_Console;
import hmlp_tool.HMLP_Console.windowMode;
import mebn_rm.MEBN.MFrag.MFrag;
import mebn_rm.MEBN.MNode.MNode; 
import mebn_rm.RDB.RDB; 
import mebn_rm.util.StringUtil;
  
public class TreePanel_Left extends TreePanel {  

	public String selectedObject = "";
	public String username = "root";
	public String password = "jesus"; 
	 
	DefaultMutableTreeNode root;
	 
	public TreePanel_Left(HMLP_Console con){
		super(con); 
	}
	
	public void init(){
		
		if (console.wMode == HMLP_Console.windowMode.CONNECT_DB){   
			setVisible(false);
			return;
		} else if (console.wMode == HMLP_Console.windowMode.SELECT_DB){
			changeName("Select DB"); 
			initTree_SELECT_DB(); 
		} else if (console.wMode == HMLP_Console.windowMode.EDIT_DB){
			changeName("Edit DB"); 
			initTree_EDIT_DB();
		} else if (console.wMode == HMLP_Console.windowMode.ADD_PARENTS){
			changeName("Child RVs"); 
			initTree_ADD_PARENTS(); 
			console.showMTheory();
		} else if (console.wMode == windowMode.JOIN_RELATIONS) {			
		} else if (console.wMode == HMLP_Console.windowMode.ADD_CLD){
			changeName("Add CLD");
		} else if (console.wMode == windowMode.LEARNING) { 
		} else if (console.wMode == windowMode.EVALUATION) { 
		}
		 
		this.invalidate();
		this.setVisible(true);
		this.repaint();
	}
	
	public void initTree_SELECT_DB() {
		// Get Database schema from MySQL
		RDB.This().connect(username, password);
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

						String type = (String) node.getUserObject();
						String strpar = node.getParent().toString();
						String strcur = node.toString(); 
						 
						String selectedObjectParent = strpar;
						selectedObject = strpar +"." + strcur;
						
						if (console.wMode == HMLP_Console.windowMode.SELECT_DB){  
							console.insertTextOut("The database \"" + selectedObject +"\" was selected.");
						} else if (console.wMode == HMLP_Console.windowMode.EDIT_DB){ 
						} else if (console.wMode == HMLP_Console.windowMode.ADD_PARENTS){ 
							console.insertTextOut("The node \"" + selectedObject +"\" was selected.");
						} else if (console.wMode == windowMode.JOIN_RELATIONS) {
							
						} else if (console.wMode == HMLP_Console.windowMode.ADD_CLD){ 
							console.insertTextOut("The node \"" + selectedObject +"\" was selected.");
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
}
