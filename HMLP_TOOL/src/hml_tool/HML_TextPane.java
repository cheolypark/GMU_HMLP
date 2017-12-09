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
package hml_tool;
 
import java.awt.Color; 
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList; 
import javax.swing.JTextPane; 
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
  
  
public class HML_TextPane extends JTextPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4441923677825230112L;
	DefaultStyledDocument doc;
	SimpleAttributeSet attrs = new SimpleAttributeSet(); 
    int offs = 0; 
    int lastLine = 0; 
    ArrayList<String> listString;  
    GeneralDialog parent;
    
	public HML_TextPane(GeneralDialog p) {
		super(); 
		
		// Make one of each kind of document.
		doc = new DefaultStyledDocument();
        setDocument(doc);
        parent = p;
        
        class KeyStrokeListener implements KeyListener{  
            public void keyPressed(KeyEvent e){  
                switch(e.getKeyCode()){  
                     case KeyEvent.VK_ENTER:
                    	 parent.keyUpdated(e); 
                     break;  
                 }  
                 
             }  
             public void keyTyped(KeyEvent e){}  
             public void keyReleased(KeyEvent e){ 
             }  
         }  
  
         KeyStrokeListener keyboardListener = new KeyStrokeListener();  
         addKeyListener(keyboardListener);  
          
         setDocument(doc);
         
         listString = new ArrayList<String>();
	}
	
	public void insert(String str){
        StyleConstants.setForeground( attrs, Color.BLACK );
        if( str.charAt(0) != ' ' ){
        	str = " " + str;
        }
        	
        String str2 = str.replaceAll(";", ";\n");
        
        try {
			doc.insertString(offs, str2, attrs);
			offs += str2.length();
		} catch (BadLocationException e) { 
			e.printStackTrace();
		}
	}
	    
	public void insertText(String text, int caretOffset) {
        String selText = getSelectedText();
        if( selText != null && selText.length() > 0 ) {
            int start = getSelectionStart();
            try {
                getDocument().remove(start, getSelectionEnd() - start);
                getDocument().insertString(start, text, null);
                setCaretPosition(start + caretOffset);
            }
            catch (BadLocationException ex) { 
            }
        }
        else {
            try {
                int pos = getCaretPosition();
                getDocument().insertString(pos, text, null);
                setCaretPosition(pos + caretOffset);
            }
            catch (BadLocationException ex) { 
            }
        } 
        requestFocus();
	}
	
	  public void insertTextOut(String str){
		  if( listString.size() == 0 )
			  listString.add(0, str);
		  else
			  listString.set(0, str);
		  printText();
	  }
	  
	  public void insertTextOut(String str, int p){
		  if( listString.size() <= p )
			  listString.add(str);
		  else
			  listString.set(p, str);
		  
		  lastLine = p;
		  printText();
	  }
	  
	  public void insertLast(String str){
		  lastLine++;
		  insertTextOut(str, lastLine); 
	  }
	   	  
	  public void clear(){
		  listString.clear();
		  printText();
	  }
	  
	  public void printText(){
		  String strResult = "";
		  for( String str: listString ){
			  strResult += str + "\n";
		  }
		  	  
		  setText(strResult);		  
	      requestFocus();
	  }
	  
	  public String getConntents(String key){
		  String selText = "";
		  try {
			  selText = getDocument().getText(0, getDocument().getLength());
		
		  } catch (BadLocationException e) {
			  e.printStackTrace();
		  }
		  		    
		  int i = selText.indexOf(key);
		  int i2 = selText.indexOf("\n", i);
		  
		  String strResult = selText.substring(i+key.length(), i2);
		  
	      return strResult;
	  } 
}
