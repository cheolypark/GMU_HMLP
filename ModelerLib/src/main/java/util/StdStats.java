package util;


/*************************************************************************
 *  Compilation:  javac StdStats.java
 *  Execution:    java StdStats < input.txt
 *
 *  Library of statistical functions.
 *
 *  The test client reads an array of real numbers from standard
 *  input, and computes the minimum, mean, maximum, and
 *  standard deviation.
 *
 *  The functions all throw a NullPointerException if the array
 *  passed in is null.

 *  % more tiny.txt
 *  5
 *  3.0 1.0 2.0 5.0 4.0
 *
 *  % java StdStats < tiny.txt
 *         min   1.000
 *        mean   3.000
 *         max   5.000
 *     std dev   1.581
 *
 *************************************************************************/

/**
 *  <i>Standard statistics</i>. This class provides methods for computing
 *  statistics such as min, max, mean, sample standard deviation, and
 *  sample variance.
 *  <p>
 *  For additional documentation, see
 *  <a href="http://introcs.cs.princeton.edu/22library">Section 2.2</a> of
 *  <i>Introduction to Programming in Java: An Interdisciplinary Approach</i>
 *  by Robert Sedgewick and Kevin Wayne.
 */
  


public final class StdStats {

    private StdStats() { }

    /**
      * Return maximum value in array, -infinity if no such value.
      */
    public static Double max(Double[] a) {
        Double max = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < a.length; i++) {
            if (a[i] > max) max = a[i];
        }
        return max;
    }

    /**
      * Return maximum value in subarray a[lo..hi], -infinity if no such value.
      */
    public static Double max(Double[] a, int lo, int hi) {
        if (lo < 0 || hi >= a.length || lo > hi)
            throw new RuntimeException("Subarray indices out of bounds");
        Double max = Double.NEGATIVE_INFINITY;
        for (int i = lo; i <= hi; i++) {
            if (a[i] > max) max = a[i];
        }
        return max;
    }

   /**
     * Return maximum value of array, Integer.MIN_VALUE if no such value
     */
    public static int max(int[] a) {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < a.length; i++) {
            if (a[i] > max) max = a[i];
        }
        return max;
    }

   /**
     * Return minimum value in array, +infinity if no such value.
     */
    public static Double min(Double[] a) {
        Double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < a.length; i++) {
            if (a[i] < min) min = a[i];
        }
        return min;
    }

    /**
      * Return minimum value in subarray a[lo..hi], +infinity if no such value.
      */
    public static Double min(Double[] a, int lo, int hi) {
        if (lo < 0 || hi >= a.length || lo > hi)
            throw new RuntimeException("Subarray indices out of bounds");
        Double min = Double.POSITIVE_INFINITY;
        for (int i = lo; i <= hi; i++) {
            if (a[i] < min) min = a[i];
        }
        return min;
    }

   /**
     * Return minimum value of array, Integer.MAX_VALUE if no such value
     */
    public static int min(int[] a) {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < a.length; i++) {
            if (a[i] < min) min = a[i];
        }
        return min;
    }

   /**
     * Return average value in array, NaN if no such value.
     */
    public static Double mean(Double[] a) {
        if (a.length == 0) return Double.NaN;
        Double sum = sum(a);
        return sum / a.length;
    }

   /**
     * Return average value in subarray a[lo..hi], NaN if no such value.
     */
    public static Double mean(Double[] a, int lo, int hi) {
        int length = hi - lo + 1;
        if (lo < 0 || hi >= a.length || lo > hi)
            throw new RuntimeException("Subarray indices out of bounds");
        if (length == 0) return Double.NaN;
        Double sum = sum(a, lo, hi);
        return sum / length;
    }

   /**
     * Return average value in array, NaN if no such value.
     */
    public static Double mean(int[] a) {
        if (a.length == 0) return Double.NaN;
        Double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum = sum + a[i];
        }
        return sum / a.length;
    }

   /**
     * Return sample variance of array, NaN if no such value.
     */
    public static Double var(Double[] a) {
        if (a.length == 0) return Double.NaN;
        Double avg = mean(a);
        Double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum += (a[i] - avg) * (a[i] - avg);
        }
        return sum / (a.length - 1);
    }

   /**
     * Return sample variance of subarray a[lo..hi], NaN if no such value.
     */
    public static Double var(Double[] a, int lo, int hi) {
        int length = hi - lo + 1;
        if (lo < 0 || hi >= a.length || lo > hi)
            throw new RuntimeException("Subarray indices out of bounds");
        if (length == 0) return Double.NaN;
        Double avg = mean(a, lo, hi);
        Double sum = 0.0;
        for (int i = lo; i <= hi; i++) {
            sum += (a[i] - avg) * (a[i] - avg);
        }
        return sum / (length - 1);
    }

   /**
     * Return sample variance of array, NaN if no such value.
     */
    public static Double var(int[] a) {
        if (a.length == 0) return Double.NaN;
        Double avg = mean(a);
        Double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum += (a[i] - avg) * (a[i] - avg);
        }
        return sum / (a.length - 1);
    }
 
   /**
     * Return population variance of array, NaN if no such value.
     */
    public static Double varp(Double[] a) {
        if (a.length == 0) return Double.NaN;
        Double avg = mean(a);
        Double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum += (a[i] - avg) * (a[i] - avg);
        }
        return sum / a.length;
    }

   /**
     * Return population variance of subarray a[lo..hi],  NaN if no such value.
     */
    public static Double varp(Double[] a, int lo, int hi) {
        int length = hi - lo + 1;
        if (lo < 0 || hi >= a.length || lo > hi)
            throw new RuntimeException("Subarray indices out of bounds");
        if (length == 0) return Double.NaN;
        Double avg = mean(a, lo, hi);
        Double sum = 0.0;
        for (int i = lo; i <= hi; i++) {
            sum += (a[i] - avg) * (a[i] - avg);
        }
        return sum / length;
    }


   /**
     * Return sample standard deviation of array, NaN if no such value.
     */
    public static Double stddev(Double[] a) {
        return Math.sqrt(var(a));
    }

   /**
     * Return sample standard deviation of subarray a[lo..hi], NaN if no such value.
     */
    public static Double stddev(Double[] a, int lo, int hi) {
        return Math.sqrt(var(a, lo, hi));
    }

   /**
     * Return sample standard deviation of array, NaN if no such value.
     */
    public static Double stddev(int[] a) {
        return Math.sqrt(var(a));
    }

   /**
     * Return population standard deviation of array, NaN if no such value.
     */
    public static Double stddevp(Double[] a) {
        return Math.sqrt(varp(a));
    }

   /**
     * Return population standard deviation of subarray a[lo..hi], NaN if no such value.
     */
    public static Double stddevp(Double[] a, int lo, int hi) {
        return Math.sqrt(varp(a, lo, hi));
    }

   /**
     * Return sum of all values in array.
     */
    public static Double sum(Double[] a) {
        Double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum += a[i];
        }
        return sum;
    }

   /**
     * Return sum of all values in subarray a[lo..hi].
     */
    public static Double sum(Double[] a, int lo, int hi) {
        if (lo < 0 || hi >= a.length || lo > hi)
            throw new RuntimeException("Subarray indices out of bounds");
        Double sum = 0.0;
        for (int i = lo; i <= hi; i++) {
            sum += a[i];
        }
        return sum;
    }

   /**
     * Return sum of all values in array.
     */
    public static int sum(int[] a) {
        int sum = 0;
        for (int i = 0; i < a.length; i++) {
            sum += a[i];
        }
        return sum;
    }

   /**
     * Plot points (i, a[i]) to standard draw.
     */
    public static void plotPoints(Double[] a) {
        int N = a.length;
        StdDraw.setXscale(0, N-1);
        StdDraw.setPenRadius(1.0 / (3.0 * N));
        for (int i = 0; i < N; i++) {
            StdDraw.point(i, a[i]);
        }
    }

   /**
     * Plot line segments connecting points (i, a[i]) to standard draw.
     */
    public static void plotLines(Double[] a) {
        int N = a.length;
        StdDraw.setXscale(0, N-1);
        StdDraw.setPenRadius();
        for (int i = 1; i < N; i++) {
            StdDraw.line(i-1, a[i-1], i, a[i]);
        }
    }

   /**
     * Plot bars from (0, a[i]) to (i, a[i]) to standard draw.
     */
    public static void plotBars(Double[] a) {
        int N = a.length;
        StdDraw.setXscale(0, N-1);
        for (int i = 0; i < N; i++) {
            StdDraw.filledRectangle(i, a[i]/2, .25, a[i]/2);
        }
    }


   /**
     * Test client.
     * Convert command-line arguments to array of Doubles and call various methods.
     */
    public static void main(String[] args) {
        Double[] a = StdArrayIO.readDouble1D();
        StdOut.printf("       min %7.3f\n", min(a));
        StdOut.printf("      mean %7.3f\n", mean(a));
        StdOut.printf("       max %7.3f\n", max(a));
        StdOut.printf("   std dev %7.3f\n", stddev(a));
    }
}
