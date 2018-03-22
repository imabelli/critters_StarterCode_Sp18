package assignment4;

/**
 * Coordinate class helps us find Critters based on coordinate location
 * @author Isabel Li
 *	UTEID: ijl283
 */
public class Coordinate implements Comparable{
	protected int x_coord;
	protected int y_coord;
	
	/**
	 * construct new coordinate object
	 * @param x is the x coordinate of the object
	 * @param y is the y coordinate of the object
	 */
	public Coordinate(int x, int y) {
		x_coord = x;
		y_coord = y;
	}
	/**
	 * so that we can have all equivalent coordinates have the same hashcode
	 */
	@Override
	public int hashCode() {
		return ("" + x_coord + "|" +  y_coord).hashCode();
	}
	
	/**
	 * to print coordinate while debugging
	 */
	@Override
	public String toString() {
		return "x: " + x_coord + "      y: " + y_coord;
	}
	
	/**
	 * to determine if two coordinates are equivalent based on values, not reference
	 */
	@Override
	public boolean equals(Object o) {
		return (this.x_coord == ((Coordinate)o).x_coord) && (this.y_coord == ((Coordinate)o).y_coord); 
	}
	
	/**
	 * in case we want to sort coordinate locations
	 */
	@Override
	public int compareTo(Object o) {	//to help sort coordinates and see if there are multiple with same coordinate values
		int thisSumCoords = this.x_coord + this.y_coord;
		int thatSumCoords = ((Coordinate)o).x_coord + ((Coordinate)o).y_coord;
		return thatSumCoords - thisSumCoords;	//to sort based on coordinate sums and then identify multiply occupied coordinates
	}
}
