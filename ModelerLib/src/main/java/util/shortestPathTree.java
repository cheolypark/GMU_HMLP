package util;

import java.io.Serializable;
 
/**
 * The Class shortestPathTree.
 */
public class shortestPathTree implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3376155228000546436L;

	/**
	 * Gets the shortest path tree.
	 *
	 * @param n the node size
	 * @param m the arc size
	 * @param nodei the starting node 
	 * @param nodej the ending node
	 * @param weight the weight
	 * @param root the root
	 * @param mindistance the minimum distance
	 * @param treearc the tree arc
	 * @return the shortest path tree
	 */
	public void getshortestPathTree(int n, int m, int nodei[], int nodej[], int weight[], int root, int mindistance[], int treearc[])
	{
		int i,j,k,large,nodeu,nodev,nodey,start,index,last,p,lensum,lenu;
		int queue[] = new int[n+1];
		int firstedges[] = new int[n+2];
		int endnode[] = new int[m+1];
		int origin[] = new int[m+1];
		boolean mark[] = new boolean[n+1];
		// obtain a large number greater than all edge weights
		large = 1;
		for (i=1; i<=m; i++)
		large += (weight[i] > 0) ? weight[i] : 0;
		// set up the forward star representation of the graph
		k = 0;
		for (i=1; i<=n; i++) {
		firstedges[i] = k + 1;
		for (j=1; j<=m; j++) {
			
			if (nodei[j] == i) {
				k++;
				origin[k] = j;
				endnode[k] = nodej[j];
				}
				}
				}
				firstedges[n+1] = m + 1;
				for (i=1; i<=n; i++) {
				treearc[i] = 0;
				mark[i] = true;
				mindistance[i] = large;
				}
				mindistance[root] = 0;
				nodev = 1;
				nodey = nodev;
				nodeu = root;
				while (true) {
				lenu = mindistance[nodeu];
				start = firstedges[nodeu];
				if (start != 0) {
				index = nodeu + 1;
				while (true) {
				last = firstedges[index] - 1;
				if (last > -1) break;
				index++;
				}
				for (i=start; i<=last; i++) {
				p = endnode[i];
				lensum = weight[origin[i]] + lenu;
				if (mindistance[p] > lensum) {
				mindistance[p] = lensum;
				treearc[p] = nodeu;
				if (mark[p]) {
				mark[p] = false;
				queue[nodey] = p;
				nodey++;
				if (nodey > n) nodey = 1;
				}
				}
				}
				}
				if (nodev == nodey) break;
				nodeu = queue[nodev];
				mark[nodeu] = true;
				nodev++;
				if (nodev > n) nodev = 1;
				}
	}
}
