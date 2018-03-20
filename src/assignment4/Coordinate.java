package assignment4;

public class Coordinate implements Comparable{
	protected int x_coord;
	protected int y_coord;
	public Coordinate(int x, int y) {
		x_coord = x;
		y_coord = y;
	}
	@Override
	public int hashCode() {
		return ("" + x_coord + "|" +  y_coord).hashCode();
	}
	@Override
	public String toString() {
		return "x: " + x_coord + "      y: " + y_coord;
	}
	@Override
	public boolean equals(Object o) {
		return (this.x_coord == ((Coordinate)o).x_coord) && (this.y_coord == ((Coordinate)o).y_coord); 
	}
	
	@Override
	public int compareTo(Object o) {	//to help sort coordinates and see if there are multiple with same coordinate values
		int thisSumCoords = this.x_coord + this.y_coord;
		int thatSumCoords = ((Coordinate)o).x_coord + ((Coordinate)o).y_coord;
		return thatSumCoords - thisSumCoords;	//to sort based on coordinate sums and then identify multiply occupied coordinates
	}
}
