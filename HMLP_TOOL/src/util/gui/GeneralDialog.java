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

import javax.swing.*;
import javax.swing.border.*; 
import java.awt.*;
import java.awt.event.ActionEvent; 
import java.awt.event.KeyEvent; 

/**
 * GeneralDialog is the class to create a common JFrame window.
 * <p>
 * 
 * @author      Cheol Young Park
 * @version     0.0.1
 * @since       1.5
 */ 

public class GeneralDialog extends JFrame {

	private static final long serialVersionUID = -6372075014284878800L;
	
	private int PREFERRED_WIDTH = 680;
	private int PREFERRED_HEIGHT = 600;

	public Dimension VGAP5 = new Dimension(1,5); 
    public EmptyBorder border5 = new EmptyBorder(5,5,5,5); 
    
	Border loweredBorder = new CompoundBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED), new EmptyBorder(5,5,5,5));
  
	public GeneralDialog(Frame owner, String str, int width, int height) { 
		super(str);
      	setSize(width, height); 
	} 
	
	public GeneralDialog(Frame owner, String str) {
		super(str);     
      	setSize(PREFERRED_WIDTH, PREFERRED_HEIGHT); 
	} 
   
    protected JRootPane createRootPane() { 
	    JRootPane rootPane = new JRootPane();
	    KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");
	    Action actionListener = new AbstractAction() { 
	      public void actionPerformed(ActionEvent actionEvent) { 
	    	  exit();
	      } 
	    } ;
	    InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
	    inputMap.put(stroke, "ESCAPE");
	    rootPane.getActionMap().put("ESCAPE", actionListener);

	    return rootPane;
	} 
    
    public void exit(){ 
    	setVisible(false);
    	getParent().setVisible(false);
    	((Window) getParent()).dispose();
    }
    
    public JButton createButton(Action a) { 
   	 	JButton b = new JButton(); 
   	 	b.putClientProperty("displayActionText", Boolean.TRUE); 
   	 	b.setAction(a); 
   	 	return b; 
    }  
 
    public JRadioButton createRadioButton(Action a) { 
   	 	JRadioButton b = new JRadioButton(); 
   	 	b.putClientProperty("displayActionText", Boolean.TRUE); 
   	 	b.setAction(a); 
   	 	return b; 
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
    	p.setAlignmentX(LEFT_ALIGNMENT);
    	if(threeD) 
    	{
    		p.setBorder(loweredBorder);
    	}
    	return p;
    }

    public static JPanel createPaneV(JComponent pane1, JComponent pane2){
		 JPanel pane =  new JPanel();
		 pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS)); 
		 pane1.setAlignmentX(Component.LEFT_ALIGNMENT);
		 pane2.setAlignmentX(Component.LEFT_ALIGNMENT);
		 pane1.setPreferredSize(new Dimension(1000, 600));
		 pane2.setPreferredSize(new Dimension(1000, 200));
		 pane.add(pane1);
		 pane.add(pane2);
		 return pane;
    }
    
    public static JPanel createPaneH(JComponent pane1, JComponent pane2){
	   	 JPanel pane =  new JPanel();
	   	 pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS)); 
	   	 pane1.setAlignmentY(Component.LEFT_ALIGNMENT);
		 pane2.setAlignmentY(Component.LEFT_ALIGNMENT);
	  	 pane.add(pane1);
	   	 pane.add(pane2);  	 
	   	 return pane;
    }
      
    public void keyUpdated (KeyEvent e){
    }
    
    public static void main(String[] args){  
   	 	JFrame frame = new JFrame();
   	 	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
   	 	frame.setSize(500, 300);
     	frame.setVisible(false); 
     	GeneralDialog d = new GeneralDialog(frame,""); 
     	d.setVisible(true);
    } 
}