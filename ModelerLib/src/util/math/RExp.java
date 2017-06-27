package util.math;

public class RExp {
	static public double Get( double scale )
	{
		if(	scale <= 0.0) {
			if(scale == 0.) return 0.;
		}
		
		return scale * RandomExp.Get();	
	}
}
