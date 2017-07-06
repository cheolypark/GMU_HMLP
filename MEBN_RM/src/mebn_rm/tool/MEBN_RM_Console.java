package mebn_rm.tool;

import java.awt.BorderLayout; 
import java.awt.Frame; 
import java.awt.event.ActionEvent; 
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
 
import mebn_rm.MEBN.MTheory.MTheory;
import mebn_rm.RDB.RDB;
import mebn_rm.core.RM_To_MEBN;
import mebn_rm.tool.panel.TreePanel_Container;
import mebn_rm.tool.panel.TreePanel_Left; 

public class MEBN_RM_Console extends GeneralDialog {
	/**
	*    
	*/
	private static final long serialVersionUID = 1L;
  
	// Sharing data
	public String selectedDB = "";
	public MTheory mTheory = null; 
	
	public enum windowMode {
		SELECT_DB, EDIT_DB
	};

	public windowMode wMode = windowMode.SELECT_DB;
	public MEBN_RM_TextPane textInputArea;
	static public MEBN_RM_TextPane textOutputArea;
	 
	JTextPane textIn = null;
	JTextPane textOut = null;
	JPanel btns = null;
	TreePanel_Container treeContainer = null; 
	JPanel center = null; 
	
	Action setLoad;
	Action setSave;
	Action setbtn1;
	Action setbtn2; 
	Action setExit;
	
	FileChooserDialog ufd = new FileChooserDialog();
	
	static int widthFrame = 1000;
	static int heightFrame = 800;

	static MEBN_RM_Console CPSLD = null;
 
	static public MEBN_RM_Console This() {
		if (CPSLD == null) {
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(false);

			MEBN_RM_Console d = new MEBN_RM_Console(frame, "");
			d.init(windowMode.SELECT_DB);
			d.setVisible(true);
		}

		return CPSLD; 
	}

	public MEBN_RM_Console(Frame owner, String str) {
		super(owner, str, widthFrame, heightFrame);
		CPSLD = this;  
		
		textIn = createTextIn();
		textOut = createTextOut();
 		//btns = createButtonPane();
 		center = createPaneV(textOut, textIn);
 		
 		TreePanel_Left leftTree = new TreePanel_Left(this);  
 		treeContainer = new TreePanel_Container(this, leftTree);
 		    
		getContentPane().add(treeContainer, BorderLayout.WEST); 
		getContentPane().add(center, BorderLayout.CENTER); 
	}
	

	public void init(windowMode mode) {
		wMode = mode;
		
		treeContainer.init();	
		
 		if (wMode == windowMode.SELECT_DB) { 
		} else if (wMode == windowMode.EDIT_DB) {  
			mTheory = new RM_To_MEBN(RDB.This()).run();
			showMTheory();
		} 
 		 		
		this.invalidate();
		this.setVisible(true);
		this.repaint();
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
		textInputArea = new MEBN_RM_TextPane();
		textInputArea.setEditable(true);
		textPane.add(new JScrollPane(textInputArea));
		return textPane;
	}

	public JTextPane createTextOut() {
		JTextPane textPane = new JTextPane();
		textPane.setLayout(new BoxLayout(textPane, BoxLayout.X_AXIS));
		textOutputArea = new MEBN_RM_TextPane();
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
	
	// Buttons [Start, Cancel] 
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
		MEBN_RM_Console d = new MEBN_RM_Console(null, "MEBN-RM Tool V.1");
		d.init(windowMode.SELECT_DB);

		// frame.getContentPane().add(d);
		// d.insertTextOut("1. test");
		// d.insertTextOut("========", 3);
		d.setVisible(true);
	}
}
