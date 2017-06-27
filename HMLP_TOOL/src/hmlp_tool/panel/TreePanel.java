package hmlp_tool.panel; 
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import hmlp_tool.HMLP_Console; 
 

public class TreePanel extends MyPanel { 
    HMLP_Console console = null;   
	JTree tree; 
	public TreePanel(HMLP_Console con){
		super();
		console = con;  
		
		// Add Button
		JScrollPane treePane = createTreePane();
		treeFrame.add(treePane, BorderLayout.NORTH);    
	}
	  
	public JScrollPane createTreePane(){
		return null;
	}
	
	public JPanel createButtonPane(){
		return null;
	}

    public JButton createButton(Action a) 
    { 
   	 	JButton b = new JButton(); 
   	 
   	 	b.putClientProperty("displayActionText", Boolean.TRUE); 
   	 	b.setAction(a); 
   	 	
   	 	return b; 
    }  
    
    protected void expandTree(JTree tree, boolean expand) {
        TreeNode root = (TreeNode) tree.getModel().getRoot();
        expandAll(tree, new TreePath(root), expand);
    }

    private void expandAll(JTree tree, TreePath path, boolean expand) {
        TreeNode node = (TreeNode) path.getLastPathComponent();

        if (node.getChildCount() >= 0) {
            Enumeration enumeration = node.children();
            while (enumeration.hasMoreElements()) {
                TreeNode n = (TreeNode) enumeration.nextElement();
                TreePath p = path.pathByAddingChild(n);

                expandAll(tree, p, expand);
            }
        }

        if (expand) {
            tree.expandPath(path);
        } else {
            tree.collapsePath(path);
        }
    } 
}
