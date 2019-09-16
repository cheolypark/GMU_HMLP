package util;

//import java.net.InetAddress;
//import java.net.NetworkInterface;
//import java.net.SocketException;
//import java.net.UnknownHostException;
import java.sql.SQLException;
 
  
public class SystemInformation {

	public SystemInformation() { 
	} 
	
	public static void main(String[] args) throws SQLException {  
    	SystemInformation f = new SystemInformation();
    	System.out.println(f.testOperatingSystem());
    	System.out.println(new MemoryUtils().getMemoryInfo()); 
    }
	
	public String testOperatingSystem() {
		String ret = "";
//		InetAddress ip;
		try {
//	        ip = InetAddress.getLocalHost();
//		        System.out.println("Current host name : " + ip.getHostName());
//		        System.out.println("Current IP address : " + ip.getHostAddress());
	        String nameOS= System.getProperty("os.name"); 
	        ret += nameOS + "	";
	        System.out.println("Operating system Name=>"+ nameOS);
	        String osType= System.getProperty("os.arch");
	        ret += osType + "	";
	        System.out.println("Operating system type =>"+ osType);
	        String osVersion= System.getProperty("os.version");
	        ret += "version: " + osVersion + "	";
	        System.out.println("Operating system version =>"+ osVersion);
	         
//		        System.out.println(System.getenv("PROCESSOR_IDENTIFIER"));
//		        System.out.println(System.getenv("PROCESSOR_ARCHITECTURE"));
//		        System.out.println(System.getenv("PROCESSOR_ARCHITEW6432"));
//		        System.out.println(System.getenv("NUMBER_OF_PROCESSORS"));
	        /* Total number of processors or cores available to the JVM */
	        System.out.println("Available processors (cores): " + 
	        Runtime.getRuntime().availableProcessors());
	        ret += "cores: " + Runtime.getRuntime().availableProcessors() + "	";
	        
	        /* Total amount of free memory available to the JVM */
	        System.out.println("Free memory (bytes): " + 
	        Runtime.getRuntime().freeMemory());
	        ret += "free mem: " + Runtime.getRuntime().freeMemory()/(1024L * 1024L) + "MB	";
	        
	        /* This will return Long.MAX_VALUE if there is no preset limit */
	        long maxMemory = Runtime.getRuntime().maxMemory();
	        /* Maximum amount of memory the JVM will attempt to use */
	        System.out.println("Maximum memory (bytes): " + 
	        		(maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory));
	 
	        ret += "max mem: " + maxMemory/(1024L * 1024L) + "MB	";
	        /* Total memory currently in use by the JVM */
	        System.out.println("Total memory (bytes): " + 
	        Runtime.getRuntime().totalMemory());
	         
	        ret += "total mem: " + Runtime.getRuntime().totalMemory()/(1024L * 1024L) + "MB	";
//		        NetworkInterface network = NetworkInterface.getByInetAddress(ip);
//		  
//		        byte[] mac = network.getHardwareAddress();
//		  
//		        System.out.print("Current MAC address : ");
//		        StringBuilder sb = new StringBuilder();
//		        for (int i = 0; i < mac.length; i++) {
//		            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));     
//		        }
//		        System.out.println(sb.toString());
	  
//	    } catch (UnknownHostException e) {C
//	        e.printStackTrace();
//		    } catch (SocketException e){
//		  
//		        e.printStackTrace();
//		  
	    }
	    catch (Exception e){
	        e.printStackTrace();
	    }
		
		return ret;
	}
	
	
}
