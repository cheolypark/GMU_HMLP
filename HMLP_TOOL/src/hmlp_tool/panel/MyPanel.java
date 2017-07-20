package hmlp_tool.panel;  
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
 

public class MyPanel extends JPanel {
    Dimension VGAP5 = new Dimension(1,5); 
    EmptyBorder border5 = new EmptyBorder(5,5,5,5);  
	
	Border loweredBorder = new CompoundBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED), 
					  new EmptyBorder(5,5,5,5));
 
	TitledBorder titledborder;
	JPanel treeFrame;
	public MyPanel(){
		// Title area
		treeFrame = createVerticalPanel(false);
		add(treeFrame);
		titledborder = new TitledBorder(null, "title", TitledBorder.LEFT, TitledBorder.TOP);
		treeFrame.setBorder(new CompoundBorder(titledborder, border5));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		treeFrame.setVisible(false);
	}
	
	public void setVisible(boolean b) {
		super.setVisible(b);
		treeFrame.setVisible(b);
	}
	 
	public void changeName(String n){
		setName(n);
		titledborder.setTitle(n);		
	}
	  
    public JPanel createHorizontalPanel(boolean threeD) 
    {
    	JPanel p = new JPanel();
    	p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
    	p.setAlignmentY(TOP_ALIGNMENT);
    	p.setAlignmentX(LEFT_ALIGNMENT);
    	if(threeD) 
    	{
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
