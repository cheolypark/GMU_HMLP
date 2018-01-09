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
package util.gui; 

import java.awt.Component;
import java.awt.Dimension; 
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel; 
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder; 
 
/**
 * MyPanel is the class inherited from JPanel. This class contains common features of Panel.
 * <p>
 * 
 * @author      Cheol Young Park
 * @version     0.0.1
 * @since       1.5
 */

public class MyPanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5176532097285309146L;
	public Dimension VGAP5 = new Dimension(1,5); 
	public EmptyBorder border5 = new EmptyBorder(5,5,5,5);   
	public Border loweredBorder = new CompoundBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED), new EmptyBorder(5,5,5,5));
 
	public TitledBorder titledborder;
	public JPanel treeFrame;
	
	public MyPanel(){
		// Title area
		treeFrame = createVerticalPanel(false);
		add(treeFrame); 
		titledborder = new TitledBorder(null, "title", TitledBorder.LEFT, TitledBorder.TOP);
		treeFrame.setBorder(new CompoundBorder(titledborder, border5));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}
	 
	public void changeName(String n){
		setName(n);
		titledborder.setTitle(n);		
	}
	  
    public JPanel createHorizontalPanel(boolean threeD) {
    	
    	JPanel p = new JPanel();
    	p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
    	p.setAlignmentY(TOP_ALIGNMENT);
    	p.setAlignmentX(LEFT_ALIGNMENT);
    	
    	if (threeD) {
    		p.setBorder(loweredBorder);
    	}
    	
    	return p;
    }
   
    public JPanel createVerticalPanel(boolean threeD) 
    {
    	JPanel p = new JPanel();
    	p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
    	p.setAlignmentY(TOP_ALIGNMENT); 
    	if(threeD) 
    	{
    		p.setBorder(loweredBorder);
    	}
    	return p;
    } 
    
	public JPanel createPaneH(JComponent pane1, JComponent pane2 )    {
	   	 JPanel pane =  new JPanel();
	   	 pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS)); 
	   	 pane1.setAlignmentY(Component.LEFT_ALIGNMENT);
		 pane2.setAlignmentY(Component.LEFT_ALIGNMENT);
	  	 pane.add(pane1);
	   	 pane.add(pane2);  	 
	   	 return pane;
   }
	

    public JButton createButton(Action a) 
    { 
   	 	JButton b = new JButton(); 
   	 
   	 	b.putClientProperty("displayActionText", Boolean.TRUE); 
   	 	b.setAction(a); 
   	 	
   	 	return b; 
    }  
    
}
