package util;
  
/**
 * The Class williamsUtil.
 */
public class williamsUtil{

	// Must be between 0 and 360
	/** The direction. */
	public double direction = 135;

	// Radius of the earth in km
	/** The earth radius. */
	public final int earthRadius = 6371;
 
	// The velocity of the ship in knots
	/** The velocity. */
	public double velocity = 10;
	
	// The distance traveled in one time step
	/** The distance traveled. */
	public double distanceTraveled = 0;

	// The coordinate of the ship -> [latitude,longitude]
	// Possible values: from [-90,-180] to [90,180]
	/** The coord. */
	public double[] coord = { 0, 0 };
   
	/**
	 * Instantiates a new williams util.
	 */
	public williamsUtil() {
	}
  
	/**
	 * calculate (initial) bearing between two points see
	 * http://williams.best.vwh.net/avform.htm#Crs
	 *
	 * @param lat1 the latitudes 1 
	 * @param lon1 the longitude 1
	 * @param lat2 the latitudes 2
	 * @param lon2 the longitude 2
	 * @return the double
	 */
	public double initialBearing(double lat1, double lon1, double lat2,
			double lon2) {
		lat1 = toRadian(lat1);
		lat2 = toRadian(lat2);
		double dLon = toRadian(lon2 - lon1);

		double y = Math.sin(dLon) * Math.cos(lat2);
		double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
				* Math.cos(lat2) * Math.cos(dLon);
		return toBearing(Math.atan2(y, x));
	}

	/**
	 * calculate (final) bearing between two points see
	 * http://williams.best.vwh.net/avform.htm#Crs
	 *
	 * @param lat1 the latitudes 1 
	 * @param lon1 the longitude 1
	 * @param lat2 the latitudes 2
	 * @param lon2 the longitude 2
	 * @return the double
	 */
	public double finalBearing(double lat1, double lon1, double lat2,
			double lon2) {
		return (initialBearing(lat2, lon2, lat1, lon1) + 180) % 360;
	}

	/**
	 * Calculate destination point given start point, initial bearing (deg) and
	 * distance (km) see http://williams.best.vwh.net/avform.htm#LL
	 *
	 * @param distanceTraveled the distance traveled
	 * @return the double[]
	 */
	public double[] finalCoord(double distanceTraveled) {
		double lat1 = toRadian(coord[0]);
		double lon1 = toRadian(coord[1]);
		double bearing = toRadian(this.direction);
		double angularDistance = distanceTraveled / earthRadius;
		double lat2 = Math.asin(Math.sin(lat1) * Math.cos(angularDistance)
				+ Math.cos(lat1) * Math.sin(angularDistance)
				* Math.cos(bearing));
		double lon2 = lon1
				+ Math.atan2(Math.sin(bearing) * Math.sin(angularDistance)
						* Math.cos(lat1), Math.cos(angularDistance)
						- Math.sin(lat1) * Math.sin(lat2));
		// normalise to -180...+180
		lon2 = (lon2 + Math.PI) % (2 * Math.PI) - Math.PI;
		double[] finalCoord = { toDegree(lat2), toDegree(lon2) };
		return finalCoord;
	}

	//Distance between points 
	//d=acos(sin(lat1)*sin(lat2)+cos(lat1)*cos(lat2)*cos(lon1-lon2))
	/**
	 * Gets the distance.
	 *
	 * @param lat1 the latitudes 1 
	 * @param lon1 the longitude 1
	 * @param lat2 the latitudes 2
	 * @param lon2 the longitude 2
	 * @return the distance
	 */
	public double getDistance(double lat1, double lon1, double lat2, double lon2 ){
		 return Math.acos(Math.sin(lat1)*Math.sin(lat2)+Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon1-lon2)); 
	}
	
	/**
	 * Gets the tc.
	 *
	 * @param lat1 the latitudes 1 
	 * @param lon1 the longitude 1
	 * @param lat2 the latitudes 2
	 * @param lon2 the longitude 2
	 * @param d the d
	 * @return the tc
	 */
	public double getTc(double lat1, double lon1, double lat2, double lon2, double d ){
		double tc1;
		if( Math.sin(lon2-lon1) < 0 )       
			tc1=Math.acos((Math.sin(lat2)-Math.sin(lat1)*Math.cos(d))/(Math.sin(d)*Math.cos(lat1)));    
		else       
			tc1=2*Math.PI-Math.acos((Math.sin(lat2)-Math.sin(lat1)*Math.cos(d))/(Math.sin(d)*Math.cos(lat1)));    
		return tc1;
	}
	
	/**
	 * Gets the new posistion.
	 *
	 * @param lat1 the latitudes 1 
	 * @param lon1 the longitude 1
	 * @param lat2 the latitudes 2
	 * @param lon2 the longitude 2
	 * @return the new posistion
	 */
	public double[] getNewPosistion(double lat1, double lon1, double lat2, double lon2){
		double newP[] = new double[2];
		
		double d = getDistance(lat1, lon1, lat2, lon2 );
		double tc = getTc(lat1, lon1, lat2, lon2, d );
		
		newP[0] = Math.asin(Math.sin(lat1)*Math.cos(d)+Math.cos(lat1)*Math.sin(d)*Math.cos(tc));
	    if( Math.cos(newP[0])==0)
	    	newP[1] = lon1;
	    else
	    	newP[1] = (lon1-Math.asin(Math.sin(tc)*Math.sin(d)/Math.cos(newP[0]))+Math.PI)%(2*Math.PI)-Math.PI;
	    
	    return newP;
	} 

	/**
	 * Compute distance traveled based on initial coordinate, bearing and time
	 * of travel in minutes.
	 *
	 * @param time the time
	 * @return the double
	 */
	public double distanceTraveled(double time) {
		return toKmM(velocity) * time;
	}

	/**
	 * Run.
	 */
	public void run() {
		for( int i = 0; i < 100 ; i++){
			
			coord = getNewPosistion(10, 20, 300, 300);
			  
			int x = 50 + (int)Math.round(coord[1] * 100);
			int y = 50 - (int)Math.round(coord[0] * 100);
			 
			double time = 10;
	
			distanceTraveled = distanceTraveled(time);
	
			coord[0] = 10;
			coord[1] = 20;
	
			double finalBearing = finalBearing(coord[0], coord[1], 20, 25);
			double[] finalCoord = finalCoord(distanceTraveled);
	
			this.coord = finalCoord;
			this.direction = finalBearing;
	 
			x = 50 + (int)Math.round(coord[1] * 100);
			y = 50 - (int)Math.round(coord[0] * 100);
			
			System.out.println(toString());
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Velocity: " + velocity + " knots\n" +
				"Distance Traveled: " + distanceTraveled + " km\n" +
				"Position:\n" +
				"  Lat: " + coord[0] + "\n" +
				"  Lon: " + coord[1] + "\n" +
				"Bearing: " + direction;
	}

	/*
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * - - - - - - - - - -
	 */

	// Methods for converting degrees/radians
	// convert degrees to radians
	/**
	 * To radian.
	 *
	 * @param degree the degree
	 * @return the double
	 */
	public double toRadian(double degree) {
		return degree * Math.PI / 180;
	}

	// convert radians to degrees (signed)
	/**
	 * To degree.
	 *
	 * @param radian the radian
	 * @return the double
	 */
	public double toDegree(double radian) {
		return radian * 180 / Math.PI;
	}

	// convert radians to degrees (as bearing: 0...360)
	/**
	 * To bearing.
	 *
	 * @param radian the radian
	 * @return the double
	 */
	public double toBearing(double radian) {
		return (toDegree(radian) + 360) % 360;
	}

	// Methods for converting [knots]/[km/h]

	/**
	 * Convert knots to km/h.
	 *
	 * @param knots The velocity to convert
	 * @return The velocity in km/h
	 */
	public double toKmH(double knots) {
		return knots * 1.8532;
	}

	/**
	 * Convert knots to km/m.
	 *
	 * @param knots The velocity to convert
	 * @return The velocity in km/m
	 */
	public double toKmM(double knots) {
		return toKmH(knots) / 60;
	}

	/*
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * - - - - - - - - - -
	 */
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		williamsUtil m = new williamsUtil();
	 
		m.run();
		//knots
		double velocityKnots = 10;
		double timeHour = 2;
		double distance = m.toKmM(velocityKnots) * timeHour;
		
		
	}

}
