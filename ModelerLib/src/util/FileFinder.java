/**
 * 
 */
package util;
import java.io.File;
import java.io.FilenameFilter; 
/**
 * http://stackoverflow.com/questions/3008043/list-all-files-from-directories-and-subdirectories-in-java
 * http://stackoverflow.com/questions/5751335/using-file-listfiles-with-filenameextensionfilter
 *
 */
public class FileFinder { 
	static int spc_count=-1;

	/* [Printing]
	 * [DIR] 
		 [DIR] $Recycle.Bin
		  [DIR] S-1-5-20
		   [FILE] desktop.ini
		  [DIR] S-1-5-21-1439636022-3700646530-3399222471-1001
		   [FILE] $I0HYD6M.xlsx
		   [FILE] $I0Z8YTP.doc
		   [FILE] $I1BAPLX.lnk
		   [FILE] $I1H4RPL.docx
	 */
	public static void Process(File aFile) {
	    spc_count++;
	    String spcs = "";
	    for (int i = 0; i < spc_count; i++)
	    	spcs += " ";
	    if(aFile.isFile())
	    	System.out.println(spcs + "[FILE] " + aFile.getName());
	    else if (aFile.isDirectory()) {
	    	System.out.println(spcs + "[DIR] " + aFile.getName());
	    	File[] listOfFiles = aFile.listFiles();
	    	if(listOfFiles!=null) {
	        for (int i = 0; i < listOfFiles.length; i++)
	        	Process(listOfFiles[i]);
	      	} else {
	      		System.out.println(spcs + " [ACCESS DENIED]");
	      	}
	    }
	    spc_count--;
	}

	/* Get files in same format
	 * [In: C:\, doc, Out: $I0Z8YTP.doc]
	 * [DIR] 
	 * 		$I0Z8YTP.doc 
	 */  
	public static File[] GetFilesInSameFormat(String strDir, final String strFormat) {
	    File dir = new File(strDir);
 		File[] files = dir.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.toLowerCase().endsWith("."+strFormat);
		    }
		});
		
		return files;
	}
	
	  
	/**
	 * @param args
	 */
	public static void main(String[] args) { 
	    File[] f = GetFilesInSameFormat("c://logs", "txt"); 
	    System.out.println(f);
	}
}
