package assignment4;

public class Coordinate{
	private int x_coord;
	private int y_coord;
	public Coordinate(int x, int y) {
		x_coord = x;
		y_coord = y;
	}
	@Override
	public int hashCode() {
		return x_coord*100 + y_coord;
	}
	@Override
	public boolean equals(Object o) {
		return (this.x_coord == ((Coordinate)o).x_coord) && (this.y_coord == ((Coordinate)o).y_coord); 
	}
//	@Override
//	public int compareTo(Object o) {	//to help sort coordinates and see if there are multiple with same coordinate values
//		int
//	}
}
