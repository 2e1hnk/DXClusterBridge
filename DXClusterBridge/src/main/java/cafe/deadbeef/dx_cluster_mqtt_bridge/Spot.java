package cafe.deadbeef.dx_cluster_mqtt_bridge;

import java.io.Serializable;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Spot implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5721456725541589120L;
	private String spotter;
	private Double frequency;
	private String band;
	private String dx;
	private String comments;
	private int time;
	private String gridsquare;
	private Date timestamp;
	private String spotter_gridsquare;
	private double spotter_latitude;
	private double spotter_longitude;
	private String dx_gridsquare;
	private double dx_latitude;
	private double dx_longitude;
	private Double distance_nm;
	private Double distance_km;
	private Double bearing;
	private String propagation_mode;

	public Spot() {
		this.setTimestamp(new Date());
	}

	public Spot(String spotter, Double frequency, String dx, String comments, int time) {
		this();
		this.setSpotter(spotter);
		this.setFrequency(frequency);
		this.setDx(dx);
		this.setComments(comments.trim());
		this.setTime(time);
	}

	public Spot(String spotter, Double frequency, String dx, String comments, int time, String gridsquare) {
		this(spotter, frequency, dx, comments, time);
		this.setGridsquare(gridsquare);
	}

	/**
	 * @return the spotter
	 */
	public String getSpotter() {
		return spotter;
	}

	/**
	 * @param spotter
	 *            the spotter to set
	 */
	public void setSpotter(String spotter) {
		this.spotter = spotter;
	}

	/**
	 * @return the frequency
	 */
	public Double getFrequency() {
		return frequency;
	}

	/**
	 * @param frequency
	 *            the frequency to set
	 */
	public void setFrequency(Double frequency) {
		this.frequency = frequency;

		if ( frequency >= 135.7 && frequency <= 137.8 ) {
			this.setBand("136kHz");
		}

		if ( frequency >= 472 && frequency <= 479 ) {
			this.setBand("600m");
		}

		if ( frequency >= 1810 && frequency <= 2000 ) {
			this.setBand("160m");
		}

		if ( frequency >= 3500 && frequency <= 4000 ) {
			this.setBand("80m");
		}

		if ( frequency >= 5258.5 && frequency <= 5406.5 ) {
			this.setBand("60m");
		}

		if ( frequency >= 7000 && frequency <= 7300 ) {
			this.setBand("40m");
		}

		if ( frequency >= 10100 && frequency <= 10150 ) {
			this.setBand("30m");
		}

		if ( frequency >= 14000 && frequency <= 14350 ) {
			this.setBand("20m");
		}

		if ( frequency >= 18068 && frequency <= 18168 ) {
			this.setBand("17m");
		}

		if ( frequency >= 21000 && frequency <= 21450 ) {
			this.setBand("15m");
		}

		if ( frequency >= 24890 && frequency <= 24990 ) {
			this.setBand("12m");
		}

		if ( frequency >= 28000 && frequency <= 26700 ) {
			this.setBand("10m");
		}

		if ( frequency >= 50000 && frequency <= 54000 ) {
			this.setBand("6m");
		}

		if ( frequency >= 70000 && frequency <= 70500 ) {
			this.setBand("4m");
		}

		if ( frequency >= 144000 && frequency <= 148000 ) {
			this.setBand("2m");
		}

		if ( frequency >= 219000 && frequency <= 225000 ) {
			this.setBand("1.25m");
		}

		if ( frequency >= 430000 && frequency <= 450000 ) {
			this.setBand("70cm");
		}

		if ( frequency >= 1240000 && frequency <= 1325000 ) {
			this.setBand("23cm");
		}

		if ( frequency >= 2310000 && frequency <= 2450000 ) {
			this.setBand("13cm");
		}

		if ( frequency >= 3400000 && frequency <= 3410000 ) {
			this.setBand("9cm");
		}

		if ( frequency >= 5650000 && frequency <= 5850000 ) {
			this.setBand("6cm");
		}

		if ( frequency >= 10000000 && frequency <= 10500000 ) {
			this.setBand("3cm");
		}

		if ( frequency >= 24000000 && frequency <= 24050000 ) {
			this.setBand("12mm");
		}

		if ( frequency >= 47000000 && frequency <= 47200000 ) {
			this.setBand("6mm");
		}

		if ( frequency >= 75500000 && frequency <= 81000000 ) {
			this.setBand("4mm");
		}

		if ( frequency >= 134000000 && frequency <= 136000000 ) {
			this.setBand("2mm");
		}

	}

	public String getBand() {
		return band;
	}

	public void setBand(String band) {
		this.band = band;
	}

	/**
	 * @return the dx
	 */
	public String getDx() {
		return dx;
	}

	/**
	 * @param dx
	 *            the dx to set
	 */
	public void setDx(String dx) {
		this.dx = dx;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments
	 *            the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
		
		// Attempt to extract info from comments field
		
		// Try standard VHF-style, like 'AB01cd<TR>EF23gh'
		Pattern pattern = Pattern.compile("(?<locator1>[A-Za-z0-9]{4,6})[<(](?<propmode>[A-Za-z]{0,2})[>)](?<locator2>[A-Za-z0-9]{4,6}).*");
		Matcher matcher = pattern.matcher(comments);
		while (matcher.find()) {
			this.setSpotter_gridsquare(matcher.group("locator1"));
			this.setDx_gridsquare(matcher.group("locator2"));
			this.setPropagation_mode(matcher.group("propmode"));
        }
		
		if ( this.getSpotter_gridsquare() != null ) {
			this.setSpotter_latitude(this.extractLatitudeFromLocator(this.getSpotter_gridsquare().toUpperCase()));
			this.setSpotter_longitude(this.extractLongitudeFromLocator(this.getSpotter_gridsquare().toUpperCase()));
		}
		
		if ( this.getDx_gridsquare() != null ) {
			this.setDx_latitude(this.extractLatitudeFromLocator(this.getDx_gridsquare().toUpperCase()));
			this.setDx_longitude(this.extractLongitudeFromLocator(this.getDx_gridsquare().toUpperCase()));
		}
		
		if ( this.getDx_gridsquare() != null && this.getSpotter_gridsquare() != null ) {
			this.setBearing(this.getBearingDegrees(this.getSpotter_gridsquare(), this.getDx_gridsquare()));
			this.setDistance_nm(this.calculateGreatCircleDistance(this.getSpotter_gridsquare(), this.getDx_gridsquare()));
			this.setDistance_km(1.852 * this.getDistance_nm());
		}
	}

	/**
	 * @return the time
	 */
	public int getTime() {
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(int time) {
		this.time = time;
	}

	/**
	 * @return the gridsquare
	 */
	public String getGridsquare() {
		return dx_gridsquare;
	}

	/**
	 * @param gridsquare
	 *            the gridsquare to set
	 */
	public void setGridsquare(String gridsquare) {
		this.gridsquare = gridsquare;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getSpotter_gridsquare() {
		return spotter_gridsquare;
	}

	public void setSpotter_gridsquare(String spotter_gridsquare) {
		this.spotter_gridsquare = spotter_gridsquare;
	}

	public String getDx_gridsquare() {
		return dx_gridsquare;
	}

	public void setDx_gridsquare(String dx_gridsquare) {
		this.dx_gridsquare = dx_gridsquare;
	}

	public Double getDistance_km() {
		return distance_km;
	}

	public void setDistance_km(Double distance_km) {
		this.distance_km = distance_km;
	}

	public Double getDistance_nm() {
		return distance_nm;
	}

	public void setDistance_nm(Double distance_nm) {
		this.distance_nm = distance_nm;
	}

	public String getPropagation_mode() {
		return propagation_mode;
	}

	public void setPropagation_mode(String propagation_mode) {
		this.propagation_mode = propagation_mode;
	}

	public double getSpotter_latitude() {
		return spotter_latitude;
	}

	public void setSpotter_latitude(double spotter_latitude) {
		this.spotter_latitude = spotter_latitude;
	}

	public double getSpotter_longitude() {
		return spotter_longitude;
	}

	public void setSpotter_longitude(double spotter_longitude) {
		this.spotter_longitude = spotter_longitude;
	}

	public double getDx_latitude() {
		return dx_latitude;
	}

	public void setDx_latitude(double dx_latitude) {
		this.dx_latitude = dx_latitude;
	}

	public double getDx_longitude() {
		return dx_longitude;
	}

	public void setDx_longitude(double dx_longitude) {
		this.dx_longitude = dx_longitude;
	}

	public Double getBearing() {
		return bearing;
	}

	public void setBearing(Double bearing) {
		this.bearing = bearing;
	}

	public String toString() {
		ObjectMapper objectMapper = new ObjectMapper();
		/*
		 return String.format(
		 		"{\"spotter\": \"%s\", \"frequency\": %1.2f, \"dx\": \"%s\", \"comments\": \"%s\", \"time\": %s, \"gridsquare\": \"%s\"}",
				this.getSpotter(), this.getFrequency(), this.getDx(), this.getComments(), this.getTime(),
				this.getGridsquare());
		*/
		String json = null;
		try {
			json = objectMapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return json;
		
	}

	// Helper functions
	
	public Double getBearingDegrees(String sourceLocator, String destinationLocator) {
		return getBearingDegrees(this.extractLatitudeFromLocator(sourceLocator), this.extractLongitudeFromLocator(sourceLocator), this.extractLatitudeFromLocator(destinationLocator), this.extractLongitudeFromLocator(destinationLocator));
	}
	
	public Double getBearingDegrees(Double srcLatitude, Double srcLongitude, Double dstLatitude, Double dstLongitude) {
		return (Math.toDegrees(getBearingRadians(srcLatitude, srcLongitude, dstLatitude, dstLongitude)) + 360 ) % 360;
	}
	
	public Double getBearingRadians(Double srcLatitude, Double srcLongitude, Double dstLatitude, Double dstLongitude) {
	    double srcLat = Math.toRadians(srcLatitude);
	    double dstLat = Math.toRadians(dstLatitude);
	    double dLng = Math.toRadians(dstLongitude - srcLongitude);

	    return Math.atan2(Math.sin(dLng) * Math.cos(dstLat),
	            Math.cos(srcLat) * Math.sin(dstLat) - 
	              Math.sin(srcLat) * Math.cos(dstLat) * Math.cos(dLng));
	}
	
	public String getLocator(double lat, double lng) {
		double longitude = lng + 180;
		longitude /= 2;
		char lonFirst = (char) ('A' + (longitude / 10));
		char lonSecond = (char) ('0' + longitude % 10);
		char lonThird = (char) ('A' + (longitude % 1) * 24);

		double latitude = lat + 90;
		char latFirst = (char) ('A' + (latitude / 10));
		char latSecond = (char) ('0' + latitude % 10);
		char latThird = (char) ('A' + (latitude % 1) * 24);

		StringBuilder sb = new StringBuilder();
		sb.append(lonFirst);
		sb.append(latFirst);
		sb.append(lonSecond);
		sb.append(latSecond);
		sb.append(("" + lonThird).toLowerCase());
		sb.append(("" + latThird).toLowerCase());

		return sb.toString();
	}

	public double extractLatitudeFromLocator(String locator) {
		String maidenhead = locator.toUpperCase();
		double latitude = -90 + 10 * (maidenhead.charAt(1) - 'A') + (maidenhead.charAt(3) - '0')
				+ 2.5 / 60 * (maidenhead.charAt(5) - 'A') + 2.5 / 60 / 2;
		return latitude;
	}

	public double extractLongitudeFromLocator(String locator) {
		String maidenhead = locator.toUpperCase();
		double longitude = -180 + 20 * (maidenhead.charAt(0) - 'A') + 2 * (maidenhead.charAt(2) - '0')
				+ 5.0 / 60 * (maidenhead.charAt(4) - 'A') + 5.0 / 60 / 2;
		return longitude;
	}
	
	public double calculateGreatCircleDistance(String locator1, String locator2) {
	     
	        double x1 = Math.toRadians(this.extractLatitudeFromLocator(locator1));
	        double y1 = Math.toRadians(this.extractLongitudeFromLocator(locator1));
	        double x2 = Math.toRadians(this.extractLatitudeFromLocator(locator2));
	        double y2 = Math.toRadians(this.extractLongitudeFromLocator(locator2));

	       /*************************************************************************
	        * Compute using law of cosines
	        *************************************************************************/
	        // great circle distance in radians
	        double angle1 = Math.acos(Math.sin(x1) * Math.sin(x2)
	                      + Math.cos(x1) * Math.cos(x2) * Math.cos(y1 - y2));

	        // convert back to degrees
	        angle1 = Math.toDegrees(angle1);

	        // each degree on a great circle of Earth is 60 nautical miles
	        double distance1 = 60 * angle1;

	        System.out.println(distance1 + " nautical miles");


	       /*************************************************************************
	        * Compute using Haversine formula
	        *************************************************************************/
	        double a = Math.pow(Math.sin((x2-x1)/2), 2)
	                 + Math.cos(x1) * Math.cos(x2) * Math.pow(Math.sin((y2-y1)/2), 2);

	        // great circle distance in radians
	        double angle2 = 2 * Math.asin(Math.min(1, Math.sqrt(a)));

	        // convert back to degrees
	        angle2 = Math.toDegrees(angle2);

	        // each degree on a great circle of Earth is 60 nautical miles
	        double distance2 = 60 * angle2;

	        return distance2;
	    
	}
}
