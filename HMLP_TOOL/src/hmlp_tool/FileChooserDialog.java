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
import java.awt.FileDialog;
import java.awt.Frame; 
 
public class FileChooserDialog {
	
	 public String loadFile(Frame f, String title, String defDir, String fileType) {
		 FileDialog fd = new FileDialog(f, title, FileDialog.LOAD);
		 fd.setFile(fileType);
		 fd.setDirectory(defDir);
		 fd.setLocation(50, 50);
		 fd.show();
		 return fd.getDirectory()+fd.getFile();
	 }

	 public String saveFile(Frame f, String title, String defDir, String fileType) {
		 FileDialog fd = new FileDialog(f, title, FileDialog.SAVE);
		 fd.setFile(fileType);
		 fd.setDirectory(defDir);
		 fd.setLocation(50, 50);
		 fd.show();
		 return fd.getDirectory()+fd.getFile();
	 }

	 public static void main(String s[]) {
		 FileChooserDialog ufd = new FileChooserDialog();
		 System.out.println ("Loading : " + ufd.loadFile(new Frame(), "Open...", ".\\", "*.java"));
		 System.out.println ("Saving : "  + ufd.saveFile(new Frame(), "Save...", ".\\", "*.java"));
		 System.exit(0);
    } 
}
