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
package hmlp_tool;

import java.awt.BorderLayout; 
import java.awt.Frame; 
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup; 
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel; 
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane; 
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
 
import hmlp_tool.panel.TreePanel_Container;
import hmlp_tool.panel.TreePanel_Left;
import hmlp_tool.panel.TreePanel_Right;
import mebn_ln.core.MTheory_Learning;
import mebn_rm.MEBN.MTheory.MRoot;
import mebn_rm.MEBN.MTheory.MTheory;
import mebn_rm.RDB.RDB;
import mebn_rm.core.RM_To_MEBN; 

public class HMLP_Console extends GeneralDialog {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
  
	// Sharing data
	public String selectedDB = "";
	public MTheory mTheory = null; 
	
	public enum windowMode {
		CONNECT_DB, SELECT_DB, EDIT_DB, ADD_PARENTS, JOIN_RELATIONS, ADD_CLD, LEARNING, EVALUATION
	};

	public windowMode wMode = windowMode.SELECT_DB;
	public HMLP_TextPane textInputArea;
	static public HMLP_TextPane textOutputArea;
	 
	JTextPane textIn = null;
	JTextPane textOut = null;
	JPanel btns = null;
	TreePanel_Container treeContainer = null; 
	TreePanel_Left leftTree;
	TreePanel_Right rightTree;
	JPanel center = null; 
	
	Action setLoad;
	Action setSave;
	Action setbtn1;
	Action setbtn2;
	Action setbtn3;
	Action setbtn4;
	Action setbtn5;
	Action setbtn6;	
	Action setbtn7;
	Action setExit;
	
	FileChooserDialog ufd = new FileChooserDialog();
	
	static int widthFrame = 1000;
	static int heightFrame = 800;

	static HMLP_Console CPSLD = null;
 
	static public HMLP_Console This() {
		if (CPSLD == null) {
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(false);

			HMLP_Console d = new HMLP_Console(frame, "");
			d.init(windowMode.SELECT_DB);
			d.setVisible(true);
		}

		return CPSLD; 
	}

	public HMLP_Console(Frame owner, String str) {
		super(owner, str, widthFrame, heightFrame);
		CPSLD = this;  
		
		textIn = createTextIn();
		textOut = createTextOut();
 		//btns = createButtonPane();
 		center = createPaneV(textOut, textIn);
 		
 		leftTree = new TreePanel_Left(this); 
 		rightTree = new TreePanel_Right(this, leftTree); 
 		treeContainer = new TreePanel_Container(this, leftTree, rightTree);
 		    
		getContentPane().add(treeContainer, BorderLayout.WEST); 
		getContentPane().add(center, BorderLayout.CENTER); 
	}
	  
	public void init(windowMode mode) {
		wMode = mode;
		
		if (wMode == windowMode.CONNECT_DB) { 
			treeContainer.setVisible(false);
			showConnectDB();
		} else if (wMode == windowMode.SELECT_DB) { 
		} else if (wMode == windowMode.EDIT_DB) { 
		} else if (wMode == windowMode.ADD_PARENTS) { 
			if (mTheory == null)
				mTheory =  new RM_To_MEBN(RDB.This()).run();
		} else if (wMode == windowMode.JOIN_RELATIONS) { 
		} else if (wMode == windowMode.ADD_CLD) { 
			mTheory.updateContexts();
			showMTheory();
		} else if (wMode == windowMode.LEARNING) { 
			mTheory.updateCLDs();
			MRoot mroot = new MRoot();
			mroot.setMTheories(mTheory);  
			new MTheory_Learning().run(mroot);  
			showMTheory_CLD();
		} else if (wMode == windowMode.EVALUATION) { 
		}  
 		   
 		treeContainer.init();	
 		
		this.invalidate();
		this.setVisible(true);
		this.repaint();
	} 
	
    public void keyUpdated (KeyEvent e) {
    	if (e.getKeyCode() == KeyEvent.VK_ENTER) {
    		leftTree.username = textInputArea.getConntents("Username: ");
    		leftTree.password = textInputArea.getConntents("Password: ");
    		
    		if (leftTree.username.isEmpty()) {
    			leftTree.username = "root";
    			leftTree.password = "jesus";
    		}
    		
    		textOutputArea.clear();
    		textInputArea.clear();
    		
    		init(windowMode.SELECT_DB);
    	}  
    }
	
	public void showConnectDB() { 
		insertLast("**********************************************************************************************************");
		insertLast("To connect a DB, insert a username and a password for the DB.");
		
		textInputArea.insertTextOut("Username: ", 1);
		textInputArea.insertTextOut("Password: ", 2);
		 
	} 
	  
	public void showMTheory() { 
		insertLast("**********************************************************************************************************");
		insertLast(mTheory.toString("MFrag", "MNode"));
	} 
	
	public void showMTheory_CLD() { 
		insertLast("**********************************************************************************************************");
		insertLast(mTheory.toString("MFrag", "MNode", "CLD"));
	} 
	
	public void setScript(String str) {
		textInputArea.setText(str);
	}

	public JTextPane createTextIn() {
		JTextPane textPane = new JTextPane();
		textPane.setLayout(new BoxLayout(textPane, BoxLayout.X_AXIS));
		textInputArea = new HMLP_TextPane(this);
		textInputArea.setEditable(true);
		textPane.add(new JScrollPane(textInputArea));
		return textPane;
	}

	public JTextPane createTextOut() {
		JTextPane textPane = new JTextPane();
		textPane.setLayout(new BoxLayout(textPane, BoxLayout.X_AXIS));
		textOutputArea = new HMLP_TextPane(this);
		textOutputArea.setEditable(false);
		textPane.add(new JScrollPane(textOutputArea));
		return textPane;
	}

	public void insertTextOut(String str) {
		textOutputArea.insertTextOut(str);
	}

	public void insertTextOut(String str, int i) {
		textOutputArea.insertTextOut(str, i);
	}

	public void insertLast(String str) {
		textOutputArea.insertLast(str);
	}

	public void clearTextOut() {
		textOutputArea.clear();
	}

	public JPanel createButtonPane() {
		ButtonGroup group = new ButtonGroup();

		// Text Button Panel
		JPanel ButtonPanel = new JPanel();
		ButtonPanel.setLayout(new BoxLayout(ButtonPanel, BoxLayout.X_AXIS));
		ButtonPanel.setBorder(border5);
 
		// Buttons
		JPanel p2 = createHorizontalPanel(false);
		ButtonPanel.add(p2);
		p2.setBorder(
				new CompoundBorder(new TitledBorder(null, "File", TitledBorder.LEFT, TitledBorder.TOP), border5));

		// Add Button
		JButton btn;
		btn = createLoadButton();
		p2.add(btn);
		group.add(btn);
		p2.add(Box.createRigidArea(VGAP5));

		btn = createSaveButton();
		p2.add(btn);
		group.add(btn);
		p2.add(Box.createRigidArea(VGAP5));
	 
		// Add Button
		JButton btn2; 

		// Window area
		JPanel p4 = createHorizontalPanel(false);
		ButtonPanel.add(p4);
		p4.setBorder(
				new CompoundBorder(new TitledBorder(null, "Menu", TitledBorder.LEFT, TitledBorder.TOP), border5));
		
		btn2 = createButton1(); p4.add(btn2); group.add(btn2); p4.add(Box.createRigidArea(VGAP5));
		btn2 = createButton2(); p4.add(btn2); group.add(btn2); p4.add(Box.createRigidArea(VGAP5));
		btn2 = createButton3(); p4.add(btn2); group.add(btn2); p4.add(Box.createRigidArea(VGAP5));
		btn2 = createButton4(); p4.add(btn2); group.add(btn2); p4.add(Box.createRigidArea(VGAP5));
		btn2 = createButton5(); p4.add(btn2); group.add(btn2); p4.add(Box.createRigidArea(VGAP5));
		btn2 = createButton6(); p4.add(btn2); group.add(btn2); p4.add(Box.createRigidArea(VGAP5));
		btn2 = createButton7(); p4.add(btn2); group.add(btn2); p4.add(Box.createRigidArea(VGAP5));
		btn2 = createExitButton(); p4.add(btn2); group.add(btn2); p4.add(Box.createRigidArea(VGAP5));

		return ButtonPanel;
	}
  
	// Button [Load, Save]
	public JButton createLoadButton() {
		setLoad = new AbstractAction("Load") {
			public void actionPerformed(ActionEvent e) {
				String strFile;
				strFile = ufd.loadFile(new Frame(), "Open...", ".\\", "*.sbn");
				if (strFile != null) {
					StringBuffer contents = new StringBuffer();
					BufferedReader reader = null;
					try {
						File file = new File(strFile);
						reader = new BufferedReader(new FileReader(file));
						if (file == null || reader == null)
							return;
						String text = null;

						// repeat until all lines is read
						while ((text = reader.readLine()) != null) {
							contents.append(text).append(System.getProperty("line.separator"));
						}
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e2) {
						e2.printStackTrace();
					} finally {
						try {
							if (reader != null)
								reader.close();
						} catch (IOException e3) {
							e3.printStackTrace();
						}
					}
 
				}
			}
		};
		return createButton(setLoad);
	}

	public JButton createSaveButton() {
		setSave = new AbstractAction("Save") {
			public void actionPerformed(ActionEvent e) {
				String strFile;
				strFile = ufd.loadFile(new Frame(), "Save...", ".\\", "*.sbn");
				if (strFile != null) {
					File file = new File(strFile);
					Writer writer = null;

					try {
						writer = new BufferedWriter(new FileWriter(file));
						writer.write(textInputArea.getText());
						JOptionPane.showMessageDialog(null, "The SBN file was saved!");
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e2) {
						e2.printStackTrace();
					} finally {
						try {
							if (writer != null)
								writer.close();
						} catch (IOException e3) {
							e3.printStackTrace();
						}
					}

				}
			}
		};
		return createButton(setSave);
	} 
	 
	public JButton createExitButton() {
		setExit = new AbstractAction("Exit") {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		};
		return createButton(setExit);
	}
	
	public JButton createButton1() {
		setbtn1 = new AbstractAction("Select DB") {
			public void actionPerformed(ActionEvent e) {
				init(windowMode.SELECT_DB);
			}
		};
		return createButton(setbtn1);
	}
	
	public JButton createButton2() {
		setbtn2 = new AbstractAction("Create World Model") {
			public void actionPerformed(ActionEvent e) {
				init(windowMode.EDIT_DB);
			}
		};
		return createButton(setbtn2);
	}
	
	public JButton createButton3() {
		setbtn3 = new AbstractAction("Select Parents") {
			public void actionPerformed(ActionEvent e) {
				init(windowMode.ADD_PARENTS);
			}
		};
		return createButton(setbtn3);
	}
	  	
	public JButton createButton4() {
		setbtn4 = new AbstractAction("Select Context") {
			public void actionPerformed(ActionEvent e) {
				init(windowMode.JOIN_RELATIONS);
			}
		};
		return createButton(setbtn4);
	}
	
	public JButton createButton5() {
		setbtn5 = new AbstractAction("Select CLD") {
			public void actionPerformed(ActionEvent e) {
				init(windowMode.ADD_CLD);
			}
		};
		return createButton(setbtn5);
	}
	
	public JButton createButton6() {
		setbtn6 = new AbstractAction("Learning") {
			public void actionPerformed(ActionEvent e) {
				init(windowMode.LEARNING);
			}
		};
		return createButton(setbtn6);
	}
	
	public JButton createButton7() {
		setbtn7 = new AbstractAction("Evaluation") {
			public void actionPerformed(ActionEvent e) {
				init(windowMode.EVALUATION);
			}
		};
		return createButton(setbtn7);
	}

	/**
	 * TextArea Pane
	 */
	class TextOutArea extends JTextArea {
		public TextOutArea() {
			super(null, 0, 0);
			setEditable(false);
			setText("");
		}

		public float getAlignmentX() {
			return LEFT_ALIGNMENT;
		}

		public float getAlignmentY() {
			return TOP_ALIGNMENT;
		}
	}

	public static void main(String[] args) {
		HMLP_Console d = new HMLP_Console(null, "HML Tool V.1");
		d.init(windowMode.CONNECT_DB);

		// frame.getContentPane().add(d);
		// d.insertTextOut("1. test");
		// d.insertTextOut("========", 3);
		d.setVisible(true);
	}
}
