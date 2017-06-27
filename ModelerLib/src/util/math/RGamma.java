package util.math;

/*
 *  Mathlib : A C Library of Special Functions
 *  Copyright (C) 1998 Ross Ihaka
 *  Copyright (C) 2000--2008 The R Development Core Team
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, a copy is available at
 *  http://www.r-project.org/Licenses/
 *
 *  SYNOPSIS
 *
 *    #include <Rmath.h>
 *    double rgamma(double a, double scale);
 *
 *  DESCRIPTION
 *
 *    Random variates from the gamma distribution.
 *
 *  REFERENCES
 *
 *    [1] Shape parameter a >= 1.  Algorithm GD in:
 *
 *	  Ahrens, J.H. and Dieter, U. (1982).
 *	  Generating gamma variates by a modified
 *	  rejection technique.
 *	  Comm. ACM, 25, 47-54.
 *
 *
 *    [2] Shape parameter 0 < a < 1. Algorithm GS in:
 *
 *	  Ahrens, J.H. and Dieter, U. (1974).
 *	  Computer methods for sampling from gamma, beta,
 *	  poisson and binomial distributions.
 *	  Computing, 12, 223-246.
 *
 *    Input: a = parameter (mean) of the standard gamma distribution.
 *    Output: a variate from the gamma(a)-distribution
 */
 
public class RGamma {
	static public double Get( double a, double scale )
	{
		/* Constants : */
	    double sqrt32 = 5.656854;
	    double exp_m1 = 0.36787944117144232159;/* exp(-1) = 1/e */

	    /* Coefficients q[k] - for q0 = sum(q[k]*a^(-k))
	     * Coefficients a[k] - for q = q0+(t*t/2)*sum(a[k]*v^k)
	     * Coefficients e[k] - for exp(q)-1 = sum(e[k]*q^k)
	     */
	    double q1 = 0.04166669;
	    double q2 = 0.02083148;
	    double q3 = 0.00801191;
	    double q4 = 0.00144121;
	    double q5 = -7.388e-5;
	    double q6 = 2.4511e-4;
	    double q7 = 2.424e-4;

	    double a1 = 0.3333333;
	    double a2 = -0.250003;
	    double a3 = 0.2000062;
	    double a4 = -0.1662921;
	    double a5 = 0.1423657;
	    double a6 = -0.1367177;
	    double a7 = 0.1233795;

	    /* State variables [FIXME for threading!] :*/
	    double aa = 0.;
	    double aaa = 0.;
	    double s= 0, s2= 0, d = 0;    /* no. 1 (step 1) */
	    double q0= 0, b= 0, si= 0, c = 0;/* no. 2 (step 4) */

	    double e, p, q, r, t, u, v, w, x, ret_val;

	    if( a < 0.0 || scale <= 0.0) {
	    	if(scale == 0.) return 0.;
	    }

	    if (a < 1.) { /* GS algorithm for parameters a < 1 */
	    	if(a == 0)
	    		return 0.;
	    	e = 1.0 + exp_m1 * a;
			
	    	for(;;) {
			    p = e * Math.random();
			    if (p >= 1.0) {
				x = -Math.log((e - p) / a);
				if( RandomExp.Get() >= (1.0 - a) * Math.log(x) )
				    break;
			    } else {
				x = Math.exp(Math.log(p) / a);
				if( RandomExp.Get() >= x )
				    break;
			    }
			}
	    	
	    	return scale * x;
	    }

	    /* --- a >= 1 : GD algorithm --- */

	    /* Step 1: Recalculations of s2, s, d if a has changed */
	    if (a != aa) {
		aa = a;
		s2 = a - 0.5;
		s = Math.sqrt(s2);
		d = sqrt32 - s * 12.0;
	    }
	    /* Step 2: t = standard normal deviate,
	               x = (s,1/2) -normal deviate. */

	    /* immediate acceptance (i) */
	    t = RandomNorm.Get();
	    x = s + 0.5 * t;
	    ret_val = x * x;
	    if (t >= 0.0)
		return scale * ret_val;

	    /* Step 3: u = 0,1 - uniform sample. squeeze acceptance (s) */
	    u = Math.random();
	    if (d * u <= t * t * t)
		return scale * ret_val;

	    /* Step 4: recalculations of q0, b, si, c if necessary */

	    if (a != aaa) {
		aaa = a;
		r = 1.0 / a;
		q0 = ((((((q7 * r + q6) * r + q5) * r + q4) * r + q3) * r
		       + q2) * r + q1) * r;

		/* Approximation depending on size of parameter a */
		/* The constants in the expressions for b, si and c */
		/* were established by numerical experiments */

		if (a <= 3.686) {
		    b = 0.463 + s + 0.178 * s2;
		    si = 1.235;
		    c = 0.195 / s - 0.079 + 0.16 * s;
		} else if (a <= 13.022) {
		    b = 1.654 + 0.0076 * s2;
		    si = 1.68 / s + 0.275;
		    c = 0.062 / s + 0.024;
		} else {
		    b = 1.77;
		    si = 0.75;
		    c = 0.1515 / s;
		}
	    }
	    /* Step 5: no quotient test if x not positive */

	    if (x > 0.0) {
		/* Step 6: calculation of v and quotient q */
		v = t / (s + s);
		if( Math.abs(v) <= 0.25 )
		    q = q0 + 0.5 * t * t * ((((((a7 * v + a6) * v + a5) * v + a4) * v
					      + a3) * v + a2) * v + a1) * v;
		else
		    q = q0 - s * t + 0.25 * t * t + (s2 + s2) * Math.log(1.0 + v);


		/* Step 7: quotient acceptance (q) */
		if( Math.log(1.0 - u) <= q )
		    return scale * ret_val;
	    }

	    for(;;) {
		/* Step 8: e = standard exponential deviate
		 *	u =  0,1 -uniform deviate
		 *	t = (b,si)-double exponential (laplace) sample */
		e = RandomExp.Get();
		u = Math.random();
		u = u + u - 1.0;
		if (u < 0.0)
		    t = b - si * e;
		else
		    t = b + si * e;
		/* Step	 9:  rejection if t < tau(1) = -0.71874483771719 */
		if (t >= -0.71874483771719) {
		    /* Step 10:	 calculation of v and quotient q */
		    v = t / (s + s);
		    if( Math.abs(v) <= 0.25)
			q = q0 + 0.5 * t * t *
			    ((((((a7 * v + a6) * v + a5) * v + a4) * v + a3) * v
			      + a2) * v + a1) * v;
		    else
			q = q0 - s * t + 0.25 * t * t + (s2 + s2) * Math.log(1.0 + v);
		    /* Step 11:	 hat acceptance (h) */
		    /* (if q not positive go to step 8) */
		    if (q > 0.0) {
			w = expm1(q);
			/*  ^^^^^ original code had approximation with rel.err < 2e-7 */
			/* if t is rejected sample again at step 8 */
			if (c * Math.abs(u) <= w * Math.exp(e - 0.5 * t * t))
			    break;
		    }
		}
	    } /* repeat .. until  `t' is accepted */
	    x = s + 0.5 * t;
	    return scale * x * x;
	}
	
	static public double expm1(double x)
	{
	    double y, a = Math.abs(x);

	    if (a > 0.697) return Math.exp(x) - 1;  /* negligible cancellation */

	    if (a > 1e-8)
	    	y = Math.exp(x) - 1;
	    else /* Taylor expansion, more accurate in this range */
		y = (x / 2 + 1) * x;

	    /* Newton step for solving   log(1 + y) = x   for y : */
	    /* WARNING: does not work for y ~ -1: bug in 1.5.0 */
	    y -= (1 + y) * (log1p (y) - x);
	    return y;
	}
	 
	static public double log1p(double x)
	{
	    /* series for log1p on the interval -.375 to .375
	     *				     with weighted error   6.35e-32
	     *				      log weighted error  31.20
	     *			    significant figures required  30.93
	     *				 decimal places required  32.01
	     */
	   	double alnrcs[] = {
		+.10378693562743769800686267719098e+1,
		-.13364301504908918098766041553133e+0,
		+.19408249135520563357926199374750e-1,
		-.30107551127535777690376537776592e-2,
		+.48694614797154850090456366509137e-3,
		-.81054881893175356066809943008622e-4,
		+.13778847799559524782938251496059e-4,
		-.23802210894358970251369992914935e-5,
		+.41640416213865183476391859901989e-6,
		-.73595828378075994984266837031998e-7,
		+.13117611876241674949152294345011e-7,
		-.23546709317742425136696092330175e-8,
		+.42522773276034997775638052962567e-9,
		-.77190894134840796826108107493300e-10,
		+.14075746481359069909215356472191e-10,
		-.25769072058024680627537078627584e-11,
		+.47342406666294421849154395005938e-12,
		-.87249012674742641745301263292675e-13,
		+.16124614902740551465739833119115e-13,
		-.29875652015665773006710792416815e-14,
		+.55480701209082887983041321697279e-15,
		-.10324619158271569595141333961932e-15,
		+.19250239203049851177878503244868e-16,
		-.35955073465265150011189707844266e-17,
		+.67264542537876857892194574226773e-18,
		-.12602624168735219252082425637546e-18,
		+.23644884408606210044916158955519e-19,
		-.44419377050807936898878389179733e-20,
		+.83546594464034259016241293994666e-21,
		-.15731559416479562574899253521066e-21,
		+.29653128740247422686154369706666e-22,
		-.55949583481815947292156013226666e-23,
		+.10566354268835681048187284138666e-23,
		-.19972483680670204548314999466666e-24,
		+.37782977818839361421049855999999e-25,
		-.71531586889081740345038165333333e-26,
		+.13552488463674213646502024533333e-26,
		-.25694673048487567430079829333333e-27,
		+.48747756066216949076459519999999e-28,
		-.92542112530849715321132373333333e-29,
		+.17578597841760239233269760000000e-29,
		-.33410026677731010351377066666666e-30,
		+.63533936180236187354180266666666e-31,
	    };

	   	int nlnrel = 22;
	   	double xmin = -0.999999985;
	   	double DBL_EPSILON =  2.22044604925031e-16;
	
	    if (x == 0.) return 0.;/* speed */
	    if (x == -1) return -1;

	    if (Math.abs(x) <= .375) {
	        /* Improve on speed (only);
		   again give result accurate to IEEE double precision: */
		if(Math.abs(x) < .5 * DBL_EPSILON)
		    return x;

		if( (0 < x && x < 1e-8) || (-1e-9 < x && x < 0))
		    return x * (1 - .5 * x);
		/* else */
		return x * (1 - x * Chebyshev.eval(x / .375, alnrcs, nlnrel));
	    }
	    
	    return Math.log(1 + x);
	}
	
}
