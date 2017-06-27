package util;
 
import java.io.Serializable;
import java.util.ArrayList;

 
/**
 * The Class ShortestPathAlgorithm.
 */
public class ShortestPathAlgorithm implements Serializable{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8744312712705985030L;

	/**
	 * The Class Data.
	 */
	public class Data implements Serializable{ 
		
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 6154319851164022746L;
		
		/**
		 * Instantiates a new data.
		 *
		 * @param s the source
		 * @param d the destination
		 */
		public Data(String s, String d){
			src = s; des = d;  
		}
		
		/**
		 * Instantiates a new data.
		 *
		 * @param s the source
		 * @param d the destination
		 * @param w the weight
		 */
		public Data(String s, String d, int w){
			src = s; des = d; weight = w;  
		}
		
		/** The source. */
		public String src;
		
		/** The destination. */
		public String des;
		
		/** The weight. */
		public int weight;
	}
	
	/** The array data. */
	public ArrayList<Data> arrayData = new ArrayList<Data>(); 
	
	/** The array node. */
	public ArrayList<String> arrayNode = new ArrayList<String>();
 
	/** The starting node. */
	int nodei[]; 
	
	/** The ending node. */
	int nodej[];	
	
	/** The weight. */
	int weight[];
	
	/** The tree arc. */
	int treearc[];
	
	/** The minimum distance. */
	int mindistance[];
	
	/** The SPT. */
	shortestPathTree SPT = new shortestPathTree();
	 
	/**
	 * Instantiates a new shortest path algorithm.
	 */
	public ShortestPathAlgorithm() {  
		Data data = new Data( "null", "null", 0);
		arrayData.add(data);
		arrayNode.add("null");
	}
	
	/**
	 * Sets the line.
	 *
	 * @param strSrc the string of source
	 * @param strDes the string of destination 
	 * @param d the data
	 */
	public void setLine( String strSrc, String strDes, Double d ){
		if( !arrayNode.contains(strSrc) )
			arrayNode.add(strSrc);
		
		if( !arrayNode.contains(strDes) )
			arrayNode.add(strDes);
		
		Data data = new Data( strSrc, strDes, d.intValue());
		arrayData.add(data); 
	}
	
	/**
	 * Sets the line both.
	 *
	 * @param strSrc the string of source
	 * @param strDes the string of destination 
	 * @param d the data 
	 */
	public void setLineBoth( String strSrc, String strDes, Double d ){
		setLine( strSrc, strDes, d );
		setLine( strDes, strSrc, d );
	}
	
	/**
	 * Initialize.
	 */
	public void init() {
		int n = arrayNode.size();
		treearc = new int[n+1];
		mindistance = new int[n+1];
		nodei = new int[arrayData.size()];
		nodej = new int[arrayData.size()];
		weight = new int[arrayData.size()];
		
		int i = 0; 
		for( Data data : arrayData ){
			nodei[i] = arrayNode.indexOf(data.src); 
			nodej[i] = arrayNode.indexOf(data.des); 
			weight[i] = data.weight;
			i++;
		}
	}
	 
	/**
	 * Gets the shortest path.
	 *
	 * @param strSrc the string of source
	 * @param strDes the string of destination 
	 * @return the shortest path
	 */
	public ArrayList<String> getShortestPath(String strSrc, String strDes){ 
		int n = arrayNode.size()-1;
		int m = arrayData.size()-1;
		int root = arrayNode.indexOf(strSrc); 
		SPT.getshortestPathTree(n,m,nodei,nodej,weight,root,mindistance,treearc);
					
	//	for (int i=1; i<=n; i++)
	//		if (i != root)
	//			System.out.println("Shortest path distance from node " + arrayNode.get(root) + " to node " + arrayNode.get(i) + " is " + mindistance[i]);
			
	//	System.out.println("\nEdges in the shortest path tree are:");
		
	//	for (int i=1; i<=n; i++)
	//		if (i != root){  
	//			System.out.println(" (" + arrayNode.get(treearc[i]) + ", " + arrayNode.get(i) + ")");
	//		}
		
		ArrayList<String> arrayTemp = new ArrayList<String>();
		getPathList(arrayTemp, strSrc, arrayNode.indexOf(strDes));		
		ArrayList<String> arrayResult = new ArrayList<String>();
		for( int i = arrayTemp.size()-1 ; i >= 0 ; i-- ){
			arrayResult.add(arrayTemp.get(i));
		}
		
		return arrayResult;
	}
	
	/**
	 * Gets the path list.
	 *
	 * @param arrayTemp the temporary array 
     * @param strSrc the string of source
	 * @param index the index
	 * @return the path list
	 */
	void getPathList(ArrayList<String> arrayTemp, String strSrc, int index ){
		if( index == arrayNode.indexOf(strSrc))
			return;
		arrayTemp.add(arrayNode.get(index));
		getPathList(arrayTemp, strSrc, treearc[index]); 
	}
	
	/**
	 * Test1.
	 */
	void Test1(){
		int n = 8;
		int m = 11;
		int root = 2;
		int treearc[] = new int[n+1];
		int mindistance[] = new int[n+1];
		int nodei[] = {0,4,6,5,3,5, 1,3,6,1, 2,5};
		int nodej[] = {0,2,4,2,6,1, 6,4,5,3, 6,3};
		int weight[] = {0,3,5,9,1,4,-3,7,4,3,-4,8};
		
		shortestPathTree mmm = new shortestPathTree();
		mmm.getshortestPathTree(n,m,nodei,nodej,weight,root,mindistance,treearc);
		for (int i=1; i<=n; i++)
			if (i != root)
				System.out.println("Shortest path distance from node " + root + " to node " + i + " is " + mindistance[i]);
			
		System.out.println("\nEdges in the shortest path tree are:");
		
		for (int i=1; i<=n; i++)
			if (i != root)
				System.out.println(" (" + treearc[i] + ", " + i + ")");
	}
 
	/**
	 * Test3.
	 */
	public void Test3(){  
		this.setLine("A1", "A3", 5.0);
		this.setLine("A1", "A2", 2.0);
		this.setLine("A3", "A2", 1.0);
		this.setLine("A2", "A3", 1.0);
		this.setLine("A2", "A4", 5.0);
		this.setLine("A3", "A4", 2.0);
		this.init();
		this.getShortestPath("A1", "A4");
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		ShortestPathAlgorithm test = new ShortestPathAlgorithm();
		test.Test3();
		

	}

}
