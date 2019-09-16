package util.math;

public class RPois {
	
	static public double GetSimple(double m) 
	{
		double a = Math.exp(-m), b = 1, i = 0;
		double u;
		
		while( true ){
			u = Math.random();
			b = u*b;
		
			if( b < a )
				return i;

			i++;
		}
	}
	
	static public double Get( double mu )
	{
		double a0	= -0.5;
		double a1	=  0.3333333;
		double a2	= -0.2500068;
		double a3	=  0.2000118;
		double a4	= -0.1661269;
		double a5	=  0.1421878;
		double a6	= -0.1384794;
		double a7	=  0.1250060;

		double one_7	= 0.1428571428571428571;
		double one_12	= 0.0833333333333333333;
		double one_24	= 0.0416666666666666667;
		double M_1_SQRT_2PI = 1 / Math.sqrt(2 * Math.PI);
		
	    /* Factorial Table (0:9)! */
	    double fact[] =   { 1., 1., 2., 6., 24., 120., 720., 5040., 40320., 362880. };

	    /* These are static --- persistent between calls for same mu : */
	    int l = 0, m = 0;

	    double b1= 0, b2= 0, c= 0, c0= 0, c1= 0, c2= 0, c3= 0;
	    double pp[] = new double[36], p0= 0, p= 0, q= 0, s= 0, d= 0, omega= 0;
	    double big_l= 0;/* integer "w/o overflow" */
	    double muprev = 0., muprev2 = 0.;/*, muold	 = 0.*/

	    /* Local Vars  [initialize some for -Wall]: */
	    double del, difmuk= 0., E= 0., fk= 0., fx, fy, g, px, py, t, u= 0., v, x;
	    double pois = -1.;
	    int k, kflag;
	    boolean big_mu = false, new_big_mu = false;

	    if( mu <= 0. )
	    	return 0.;

	    if( mu >= 10.){ 
	    	big_mu = true; 
	    }
	    else{ 
	    	big_mu = false;
	    }

	    if( big_mu == true )
	    	new_big_mu = false;

	    if (!(big_mu && mu == muprev)) {/* maybe compute new persistent par.s */

		if (big_mu) {
		    new_big_mu = true;
		    /* Case A. (recalculation of s,d,l	because mu has changed):
		     * The poisson probabilities pk exceed the discrete normal
		     * probabilities fk whenever k >= m(mu).
		     */
		    muprev = mu;
		    s = Math.sqrt(mu);
		    d = 6. * mu * mu;
		    big_l = Math.floor(mu - 1.1484);
		    /* = an upper bound to m(mu) for all mu >= 10.*/
		}
		else { /* Small mu ( < 10) -- not using normal approx. */

		    /* Case B. (start new table and calculate p0 if necessary) */

		    /*muprev = 0.;-* such that next time, mu != muprev ..*/
		    if (mu != muprev) {
			muprev = mu;
			m = Math.max(1, (int) mu);
			l = 0; /* pp[] is already ok up to pp[l] */
			q = p0 = p = Math.exp(-mu);
		    }

		    for(;;) {
			/* Step U. uniform sample for inversion method */
			u = Math.random();
			if (u <= p0)
			    return 0.;

			/* Step T. table comparison until the end pp[l] of the
			   pp-table of cumulative poisson probabilities
			   (0.458 > ~= pp[9](= 0.45792971447) for mu=10 ) */
			if (l != 0) {
			    for (k = (u <= 0.458) ? 1 : Math.min(l, m);  k <= l; k++)
				if (u <= pp[k])
				    return (double)k;
			    if (l == 35) /* u > pp[35] */
				continue;
			}
			/* Step C. creation of new poisson
			   probabilities p[l..] and their cumulatives q =: pp[k] */
			l++;
			for (k = l; k <= 35; k++) {
			    p *= mu / k;
			    q += p;
			    pp[k] = q;
			    if (u <= q) {
				l = k;
				return (double)k;
			    }
			}
			l = 35;
		    } /* end(repeat) */
		}/* mu < 10 */

	    } /* end {initialize persistent vars} */

	/* Only if mu >= 10 : ----------------------- */

	    /* Step N. normal sample */
	    g = mu + s * RandomNorm.Get();/* norm_rand() ~ N(0,1), standard normal */

	    if (g >= 0.) {
		pois = Math.floor(g);
		/* Step I. immediate acceptance if pois is large enough */
		if (pois >= big_l)
		    return pois;
		/* Step S. squeeze acceptance */
		fk = pois;
		difmuk = mu - fk;
		u = Math.random(); /* ~ U(0,1) - sample */
		if (d * u >= difmuk * difmuk * difmuk)
		    return pois;
	    }

	    /* Step P. preparations for steps Q and H.
	       (recalculations of parameters if necessary) */

	    if (new_big_mu || mu != muprev2) {
	        /* Careful! muprev2 is not always == muprev
		   because one might have exited in step I or S
		   */
	        muprev2 = mu;
		omega = M_1_SQRT_2PI / s;
		/* The quantities b1, b2, c3, c2, c1, c0 are for the Hermite
		 * approximations to the discrete normal probabilities fk. */

		b1 = one_24 / mu;
		b2 = 0.3 * b1 * b1;
		c3 = one_7 * b1 * b2;
		c2 = b2 - 15. * c3;
		c1 = b1 - 6. * b2 + 45. * c3;
		c0 = 1. - b1 + 3. * b2 - 15. * c3;
		c = 0.1069 / mu; /* guarantees majorization by the 'hat'-function. */
	    }

	    if (g >= 0.) {
			/* 'Subroutine' F is called (kflag=0 for correct return) */
		  kflag = 0;
		  if (pois < 10) { /* use factorials from table fact[] */
				px = -mu;
				py = Math.pow(mu, pois) / fact[(int)pois];
		    }
		    else {
				/* Case pois >= 10 uses polynomial approximation
				   a0-a7 for accuracy when advisable */
				del = one_12 / fk;
				del = del * (1. - 4.8 * del * del);
				v = difmuk / fk;
				if (Math.abs(v) <= 0.25)
				    px = fk * v * v * (((((((a7 * v + a6) * v + a5) * v + a4) *
							  v + a3) * v + a2) * v + a1) * v + a0)
					- del;
				else /* |v| > 1/4 */
				    px = fk * Math.log(1. + v) - difmuk - del;

				py = M_1_SQRT_2PI / Math.sqrt(fk);
			    }
			
		    x = (0.5 - difmuk) / s;
		    x *= x;/* x^2 */
		    fx = -0.5 * x;
		    fy = omega * (((c3 * x + c2) * x + c1) * x + c0);
	    
			/* Step Q. Quotient acceptance (rare case) */
			if (fy - u * fy <= py * Math.exp(px - fx))
				 return pois;
	    }
 
	    for(;;) {
		/* Step E. Exponential Sample */
	    	E = RandomExp.Get();	/* ~ Exp(1) (standard exponential) */

			/*  sample t from the laplace 'hat'
			    (if t <= -0.6744 then pk < fk for all mu >= 10.) */
			u = 2 * Math.random() - 1.;
			t = 1.8 + fsign(E, u);
			
			if (t > -0.6744) {
				pois = Math.floor(mu + s * t);
				fk = pois;
				difmuk = mu - fk;

				/* 'subroutine' F is called (kflag=1 for correct return) */
				kflag = 1;

				////////////////////////////////////////////////////////
				//Step_F: /* 'subroutine' F : calculation of px,py,fx,fy. */

			    if (pois < 10) { /* use factorials from table fact[] */
					px = -mu;
					py = Math.pow(mu, pois) / fact[(int)pois];
			    }
			    else {
					/* Case pois >= 10 uses polynomial approximation
					   a0-a7 for accuracy when advisable */
					del = one_12 / fk;
					del = del * (1. - 4.8 * del * del);
					v = difmuk / fk;
					if (Math.abs(v) <= 0.25)
					    px = fk * v * v * (((((((a7 * v + a6) * v + a5) * v + a4) *
								  v + a3) * v + a2) * v + a1) * v + a0)
						- del;
					else /* |v| > 1/4 */
					    px = fk * Math.log(1. + v) - difmuk - del;

					py = M_1_SQRT_2PI / Math.sqrt(fk);
				    }
				
			    x = (0.5 - difmuk) / s;
			    x *= x;/* x^2 */
			    fx = -0.5 * x;
			    fy = omega * (((c3 * x + c2) * x + c1) * x + c0);
		    
			    if (kflag > 0) {
					/* Step H. Hat acceptance (E is repeated on rejection) */
					if (c * Math.abs(u) <= py * Math.exp(px + E) - fy * Math.exp(fx + E))
						break;			    
				} else
				/* Step Q. Quotient acceptance (rare case) */
				if (fy - u * fy <= py * Math.exp(px - fx))
					break;
			}/* t > -.67.. */
	    }
	    
	    return pois;
	}
	
	static public double fsign(double x, double y)
	{ 
	    return ((y >= 0) ? Math.abs(x) : -Math.abs(x));
	}
	
	public static void main(String args[]) throws Exception {
		for( int i = 0 ; i < 100; i ++){
			System.out.print( RPois.Get(2) + " \n" ); // After test, Get are GetSimple is amost same.
			//System.out.print( RPois.GetSimple(2) + " \n" );
		}
		
	}
}
