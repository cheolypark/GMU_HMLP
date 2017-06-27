package util.math;
 

/*
 *  Mathlib : A C Library of Special Functions
 *  Copyright (C) 1998 Ross Ihaka
 *  Copyright (C) 2000-2 the R Development Core Team
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
 *    double norm_rand(void);
 *
 *  DESCRIPTION
 *
 *    Random variates from the STANDARD normal distribution  N(0,1).
 *
 * Is called from  rnorm(..), but also rt(), rf(), rgamma(), ...
 */


public class RandomNorm {
	double C1 = 0.398942280401433;
	double C2 =	0.180025191068563;
	double A =  2.216035867166471;
	
	double g(double x){
		return (C1*Math.exp(-x*x/2.0)-C2*(A-x));
	}
	
	static public double Get()
	{
		 double a[] =
		    {
			0.0000000, 0.03917609, 0.07841241, 0.1177699,
			0.1573107, 0.19709910, 0.23720210, 0.2776904,
			0.3186394, 0.36012990, 0.40225010, 0.4450965,
			0.4887764, 0.53340970, 0.57913220, 0.6260990,
			0.6744898, 0.72451440, 0.77642180, 0.8305109,
			0.8871466, 0.94678180, 1.00999000, 1.0775160,
			1.1503490, 1.22985900, 1.31801100, 1.4177970,
			1.5341210, 1.67594000, 1.86273200, 2.1538750
		    };

		 double d[] =
		    {
			0.0000000, 0.0000000, 0.0000000, 0.0000000,
			0.0000000, 0.2636843, 0.2425085, 0.2255674,
			0.2116342, 0.1999243, 0.1899108, 0.1812252,
			0.1736014, 0.1668419, 0.1607967, 0.1553497,
			0.1504094, 0.1459026, 0.1417700, 0.1379632,
			0.1344418, 0.1311722, 0.1281260, 0.1252791,
			0.1226109, 0.1201036, 0.1177417, 0.1155119,
			0.1134023, 0.1114027, 0.1095039
		    };

		 double t[] =
		    {
			7.673828e-4, 0.002306870, 0.003860618, 0.005438454,
			0.007050699, 0.008708396, 0.010423570, 0.012209530,
			0.014081250, 0.016055790, 0.018152900, 0.020395730,
			0.022811770, 0.025434070, 0.028302960, 0.031468220,
			0.034992330, 0.038954830, 0.043458780, 0.048640350,
			0.054683340, 0.061842220, 0.070479830, 0.081131950,
			0.094624440, 0.112300100, 0.136498000, 0.171688600,
			0.227624100, 0.330498000, 0.584703100
		    };

		 double h[] =
		    {
			0.03920617, 0.03932705, 0.03950999, 0.03975703,
			0.04007093, 0.04045533, 0.04091481, 0.04145507,
			0.04208311, 0.04280748, 0.04363863, 0.04458932,
			0.04567523, 0.04691571, 0.04833487, 0.04996298,
			0.05183859, 0.05401138, 0.05654656, 0.05953130,
			0.06308489, 0.06737503, 0.07264544, 0.07926471,
			0.08781922, 0.09930398, 0.11555990, 0.14043440,
			0.18361420, 0.27900160, 0.70104740
		    };

/*----------- Constants and definitions for  Kinderman - Ramage --- */
/*
 *  REFERENCE
 *
 *    Kinderman A. J. and Ramage J. G. (1976).
 *    Computer generation of normal random variables.
 *    JASA 71, 893-896.
 */
		double s, u1, w, y, u2, u3, aa, tt, theta, R;
		int i;
 
		u1 = Math.random();
		s = 0.0;
		if (u1 > 0.5)
		    s = 1.0;
		u1 = u1 + u1 - s;
		u1 *= 32.0;
		i = (int) u1;
		if (i == 32)
		    i = 31;
		if (i != 0) {
		    u2 = u1 - i;
		    aa = a[i - 1];
		    while (u2 <= t[i - 1]) {
			u1 = Math.random();
			w = u1 * (a[i] - aa);
			tt = (w * 0.5 + aa) * w;
			for(;;) {
			    if (u2 > tt){
			    	y = aa + w;
					return (s == 1.0) ? -y : y;
			    }
			    u1 = Math.random();
			    if (u2 < u1)
				break;
			    tt = u1;
			    u2 = Math.random();
			}
			u2 = Math.random();
		    }
		    w = (u2 - t[i - 1]) * h[i - 1];
		}
		else {
		    i = 6;
		    aa = a[31];
		    for(;;) {
			u1 = u1 + u1;
			if (u1 >= 1.0)
			    break;
			aa = aa + d[i - 1];
			i = i + 1;
		    }
		    u1 = u1 - 1.0;
		    for(;;) {
			w = u1 * d[i - 1];
			tt = (w * 0.5 + aa) * w;
			for(;;) {
			    u2 = Math.random();
			    if (u2 > tt){
			    	y = aa + w;
					return (s == 1.0) ? -y : y;
			    }
			    u1 = Math.random();
			    if (u2 < u1)
				break;
			    tt = u1;
			}
			u1 = Math.random();
		    } 
		}
		 
		y = aa + w;
		return (s == 1.0) ? -y : y;
	}
	
	public static void main(String args[]) throws Exception {
    	 
		for( int i = 0; i < 10000; i++ ){
			System.out.print( RandomNorm.Get() + " \n" );
		}
	} 
}
