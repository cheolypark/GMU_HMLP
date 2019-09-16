package util.math;

import java.util.ArrayList;
import java.util.List;

public class doubleQuickSort {
	public doubleQuickSort(){
		
	}
	
	double[] A;
	public double[] sort(double[] P) {
		A = P;
        if (A == null || A.length == 0)
            return null;
        quicksort(A, 0, A.length - 1);
        return A; 
    }
	
	public List<Double> sortToHigher(List<Double> l) {
		A = new double[l.size()];
		
		int i = 0;
		for (Double d : l) {
			A[i++] = d;
		}
        
		if (A == null || A.length == 0)
            return null;
        
        quicksort(A, 0, A.length - 1);
        
        List<Double> ret = new ArrayList<Double>();
        
        for (i = A.length-1; i >= 0; i--) {
        	ret.add(A[i]);
        }
        
        return ret;
    }

    public void quicksort(double[] A, int left, int right) {
    	double pivot = A[left + (right - left) / 2];
        int i = left;
        int j = right;
        while (i <= j) {
            while (A[i] < pivot) {
                i++;
            }
            while (A[j] > pivot) {
                j--;
            }
            if (i <= j) {
                exchange(i, j);
                i++;
                j--;
            }
        }

        if(left < j)
            quicksort(A,left,j);
        if(i < right)
            quicksort(A,i,right);
    }

    public void exchange(int i, int j){
    	double temp = A[i];
        A[i]=A[j];
        A[j]=temp;
    }

    public String toString() {
        String s = "";
        s += "[" + A[0];
        for (int i = 1; i < A.length; i++) {
            s += ", " + A[i];
        }
        s += "]";
        return s;
    }
    
    public static void main(String[] args) {
    	doubleQuickSort q = new doubleQuickSort();
    	double[] A =  new double[3];
    	A[0] = 0.10;
    	A[1] = 2.0;
    	A[2] = 0.03;
    	q.sort(A); 
    }
}
