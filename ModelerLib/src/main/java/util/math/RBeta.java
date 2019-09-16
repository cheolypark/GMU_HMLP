package util.math;

public class RBeta {
	static public double Get( double aa, double bb )
	{
		double a, b, alpha;
	    double r, s, t, u1, u2, v, w, y, z;
	    double DBL_MAX = 1.79769e+308;
	    double DBL_MAX_EXP = 1024;
	    double M_LN2 = 0.69314718055994530942;

	    double expmax =	(DBL_MAX_EXP * M_LN2);/* = log(DBL_MAX) */
	    
	    boolean qsame = false;
	    /* FIXME:  Keep Globals (properly) for threading */
	    /* Uses these GLOBALS to save time when many rv's are generated : */
	    double beta = 0, gamma= 0, delta= 0, k1= 0, k2= 0;
	    double olda = -1.0;
	    double oldb = -1.0;

	    if( aa <= 0. || bb <= 0. )
	    	return -1;

	    /* Test if we need new "initializing" */
	    if( (olda == aa) && (oldb == bb) ) qsame = true;
	    if( qsame == false) { olda = aa; oldb = bb; }

	    a = Math.min(aa, bb);
	    b = Math.max(aa, bb); /* a <= b */
	    alpha = a + b;
 
	    if (a <= 1.0) {	/* --- Algorithm BC --- */

		/* changed notation, now also a <= b (was reversed) */

		if (!qsame) { /* initialize */
		    beta = 1.0 / a;
		    delta = 1.0 + b - a;
		    k1 = delta * (0.0138889 + 0.0416667 * a) / (b * beta - 0.777778);
		    k2 = 0.25 + (0.5 + 0.25 / delta) * a;
		}
		/* FIXME: "do { } while()", but not trivially because of "continue"s:*/
		for(;;) {
		    u1 = Math.random();
		    u2 = Math.random();
		    if (u1 < 0.5) {
			y = u1 * u2;
			z = u1 * y;
			if (0.25 * u2 + z - y >= k1)
			    continue;
		    } else {
			z = u1 * u1 * u2;
			if (z <= 0.25) {
				v = beta * Math.log(u1 / (1.0 - u1));	
				if( v <= expmax )			
					w = b * Math.exp(v);		
				else				
					w = DBL_MAX;
			    break;
			}
			if (z >= k2)
			    continue;
		    }

			v = beta * Math.log(u1 / (1.0 - u1));	
			if( v <= expmax )			
				w = b * Math.exp(v);		
			else				
				w = DBL_MAX;

		    if (alpha * (Math.log(alpha / (a + w)) + v) - 1.3862944 >= Math.log(z))
			break;
		}
		return (aa == a) ? a / (a + w) : w / (a + w);

	    }
	    else {		/* Algorithm BB */

		if (!qsame) { /* initialize */
		    beta = Math.sqrt((alpha - 2.0) / (2.0 * a * b - alpha));
		    gamma = a + 1.0 / beta;
		}
		do {
		    u1 = Math.random();
		    u2 = Math.random();

			v = beta * Math.log(u1 / (1.0 - u1));	
			if( v <= expmax )			
				w = a * Math.exp(v);		
			else				
				w = DBL_MAX;		    

		    z = u1 * u1 * u2;
		    r = gamma * v - 1.3862944;
		    s = a + r - w;
		    if (s + 2.609438 >= 5.0 * z)
			break;
		    t = Math.log(z);
		    if (s > t)
			break;
		}
		while (r + alpha * Math.log(alpha / (b + w)) < t);

		return (aa != a) ? b / (b + w) : w / (b + w);
	    }
	}
	
	public static void main(String args[]) throws Exception {
		for( int i = 0 ; i < 100; i ++){
			System.out.print( RBeta.Get(0.3,1.5) + " \n" ); // After test, Get are GetSimple is amost same.
			//System.out.print( RPois.GetSimple(2) + " \n" );
		}
		 
	}
}
