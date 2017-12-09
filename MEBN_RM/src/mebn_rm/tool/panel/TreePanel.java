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

import java.awt.BorderLayout; 
import java.util.Enumeration;  
import javax.swing.Action; 
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree; 
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath; 
import mebn_rm.tool.MEBN_RM_Console;
   
public class TreePanel extends MyPanel { 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1087404227165489568L;
	MEBN_RM_Console console = null;   
	JTree tree; 
	public TreePanel(MEBN_RM_Console con){
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
