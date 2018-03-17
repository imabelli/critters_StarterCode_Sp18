package assignment4;
/* CRITTERS Critter.java
 * EE422C Project 4 submission by
 * Replace <...> with your actual data.
 * <Student1 Name>
 * <Student1 EID>
 * <Student1 5-digit Unique No.>
 * <Student2 Name>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Fall 2016
 */



import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* see the PDF for descriptions of the methods and fields in this class
 * you may add fields, methods or inner classes to Critter ONLY if you make your additions private
 * no new public, protected or default-package code or data can be added to Critter
 */


public abstract class Critter {
	private static String myPackage;
	private	static List<Critter> population = new java.util.ArrayList<Critter>();
	private static List<Critter> babies = new java.util.ArrayList<Critter>();
	private static char[][] worldArray = new char[Params.world_height + 2][Params.world_width + 2];
	private static Map<Coordinate, ArrayList<Critter>> critterAtLoc = new HashMap<Coordinate, ArrayList<Critter>>();
	private static ArrayList<Coordinate> multiplyOccupied = new ArrayList<Coordinate>();
	private static boolean needResolveConflicts = false;
	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}
	
	private static java.util.Random rand = new java.util.Random();
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}
	
	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}
	
	
	/* a one-character long string that visually depicts your critter in the ASCII interface */
	public String toString() { return ""; }
	
	private int energy = 0;
	protected int getEnergy() { return energy; }
	
	private int x_coord;
	private int y_coord;
	
	
	protected final void walk(int direction) {
		this.energy -= Params.walk_energy_cost;
		if(this.energy <= 0) {
			Critter.removeThisCritter(this);
		} else {
			if(direction == 0) {
				if(this.x_coord == Params.world_width - 1) {
					this.x_coord = 0;
				} else {
					this.x_coord++;
				}
			} else if(direction == 1) {
				if(this.x_coord == Params.world_width - 1) {
					this.x_coord = 0;
				} else {
					this.x_coord++;
				}
				if(this.y_coord == 0) {
					this.y_coord = Params.world_height - 1;
				} else {
					this.y_coord--;
				}
			} else if(direction == 2) {
				if(this.y_coord == 0) {
					this.y_coord = Params.world_height - 1;
				} else {
					this.y_coord--;
				}
			} else if(direction == 3) {
				if(this.x_coord == 0) {
					this.x_coord = Params.world_width - 1;
				} else {
					this.x_coord--;
				}
				if(this.y_coord == 0) {
					this.y_coord = Params.world_height - 1;
				} else {
					this.y_coord--;
				}
			} else if(direction == 4) {
				if(this.x_coord == 0) {
					this.x_coord = Params.world_width - 1;
				} else {
					this.x_coord--;
				}
			} else if(direction == 5) {
				if(this.x_coord == 0) {
					this.x_coord = Params.world_width - 1;
				} else {
					this.x_coord--;
				}
				if(this.y_coord == Params.world_height - 1) {
					this.y_coord = 0;
				} else {
					this.y_coord++;
				}
			} else if(direction == 6) {
				if(this.y_coord == Params.world_height - 1) {
					this.y_coord = 0;
				} else {
					this.y_coord++;
				}
			} else if(direction == 7) {
				if(this.x_coord == Params.world_width - 1) {
					this.x_coord = 0;
				} else {
					this.x_coord++;
				}
				if(this.y_coord == Params.world_height - 1) {
					this.y_coord = 0;
				} else {
					this.y_coord++;
				}
			}
		}
	}
	
	protected final void run(int direction) {
		
	}
	
	protected final void reproduce(Critter offspring, int direction) {
	}

	public abstract void doTimeStep();
	public abstract boolean fight(String oponent);
	
	/**
	 * create and initialize a Critter subclass.
	 * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
	 * an InvalidCritterException must be thrown.
	 * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
	 * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
	 * an Exception.)
	 * @param critter_class_name
	 * @throws InvalidCritterException
	 */
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {
		try {
			String className = "assignment4." + critter_class_name;
			Critter newCritter = (Critter) Class.forName(className).newInstance();
			newCritter.x_coord = getRandomInt(Params.world_width);
			newCritter.y_coord = getRandomInt(Params.world_height);
			newCritter.energy = Params.start_energy;
			population.add(newCritter);
			Coordinate loc = new Coordinate(newCritter.x_coord, newCritter.y_coord);
			if(critterAtLoc.get(loc) == null) {	//if there are no critters at that location
				ArrayList<Critter> critList = new ArrayList<Critter>();
				critList.add(newCritter);
				critterAtLoc.put(loc, critList);
			} else {
				critterAtLoc.get(loc).add(newCritter);
			}
		} catch(Throwable error) {
			System.err.println(error);
			throw new InvalidCritterException(critter_class_name);
		}
	}
	
	private static void checkMultiplyOccupied() {
		needResolveConflicts = false;
		multiplyOccupied.clear(); //redefine list of multiply occupied locations at each call
		for(Coordinate key : critterAtLoc.keySet()) {	
			if(critterAtLoc.get(key).size() > 1) {	//if there is more than one critter at this coordinate
				multiplyOccupied.add(key);
			}
		}
		if(multiplyOccupied.size() != 0) {	//if there is a multiply occupied location
			needResolveConflicts = true;
		}
	}
	
	/**
	 * Gets a list of critters of a specific type.
	 * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
	 * @return List of Critters.
	 * @throws InvalidCritterException
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		List<Critter> result = new java.util.ArrayList<Critter>();
	
		return result;
	}
	
	/**
	 * Prints out how many Critters of each type there are on the board.
	 * @param critters List of Critters.
	 */
	public static void runStats(List<Critter> critters) {
		System.out.print("" + critters.size() + " critters as follows -- ");
		java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
		for (Critter crit : critters) {
			String crit_string = crit.toString();
			Integer old_count = critter_count.get(crit_string);
			if (old_count == null) {
				critter_count.put(crit_string,  1);
			} else {
				critter_count.put(crit_string, old_count.intValue() + 1);
			}
		}
		String prefix = "";
		for (String s : critter_count.keySet()) {
			System.out.print(prefix + s + ":" + critter_count.get(s));
			prefix = ", ";
		}
		System.out.println();		
	}
	
	/* the TestCritter class allows some critters to "cheat". If you want to 
	 * create tests of your Critter model, you can create subclasses of this class
	 * and then use the setter functions contained here. 
	 * 
	 * NOTE: you must make sure that the setter functions work with your implementation
	 * of Critter. That means, if you're recording the positions of your critters
	 * using some sort of external grid or some other data structure in addition
	 * to the x_coord and y_coord functions, then you MUST update these setter functions
	 * so that they correctly update your grid/data structure.
	 */
	static abstract class TestCritter extends Critter {
		protected void setEnergy(int new_energy_value) {
			super.energy = new_energy_value;
		}
		
		protected void setX_coord(int new_x_coord) {
			super.x_coord = new_x_coord;
		}
		
		protected void setY_coord(int new_y_coord) {
			super.y_coord = new_y_coord;
		}
		
		protected int getX_coord() {
			return super.x_coord;
		}
		
		protected int getY_coord() {
			return super.y_coord;
		}
		

		/*
		 * This method getPopulation has to be modified by you if you are not using the population
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.
		 */
		protected static List<Critter> getPopulation() {
			return population;
		}
		
		/*
		 * This method getBabies has to be modified by you if you are not using the babies
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.  Babies should be added to the general population 
		 * at either the beginning OR the end of every timestep.
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}

	/**
	 * Clear the world of all critters, dead and alive
	 */
	public static void clearWorld() {
		// Complete this method.
	}
	
	public static void worldTimeStep() {
		int numPopulation = population.size();
		int numBabies = babies.size();
		for(int i = 0; i < numPopulation; i++) {
			population.get(i).doTimeStep();	//do time step on each member of population
		}
		checkMultiplyOccupied();	//fill ArrayList with coordinates with more than 1 critter
		while(needResolveConflicts) {
			resolveConflicts();
		}
		removeAllDead();
		// Complete this method.
	}
	
	/**
	 * resolve conflicts (multiply occupied locations) until each coordinate has at most 1 critter
	 */
	private static void resolveConflicts() {
		Coordinate currentLoc = multiplyOccupied.get(0);
		try {
			Critter c1 = critterAtLoc.get(currentLoc).get(0);
			Critter c2 = critterAtLoc.get(currentLoc).get(1);	//two critters to resolve encounter
			Critter winner = null;
			Critter loser = null;
			boolean c1Fight = c1.fight(c2.toString());
			boolean c2Fight = c2.fight(c1.toString());
			int c1Roll = -1;
			int c2Roll = -1;
			if(c1Fight && c2Fight) {
				c1Roll = Critter.getRandomInt(c1.energy);
				c2Roll = Critter.getRandomInt(c2.energy);
			} else if(c1Fight && !c2Fight) {
				c2Roll = 0;
				c1Roll = Critter.getRandomInt(c1.energy);
			} else if(!c1Fight && c2Fight) {
				c1Roll = 0;
				c2Roll = Critter.getRandomInt(c2.energy);
			} else if(!c1Fight && !c2Fight) {
				//they both don't want to fight and are both still in the same position, winner arbitrarily chosen
			}
			if(c1Roll == c2Roll) {	//if they both roll the same number, randomly determine winner
				int randDetermine = Critter.getRandomInt(10);
				if(randDetermine % 2 == 0) {
					winner = c1;
					loser = c2;
				} else {
					winner = c2;
					loser = c1;
				}
			} else if(c1Roll > c2Roll) {
				winner = c1;
				loser = c2;
			} else if(c2Roll > c1Roll) {
				winner = c2;
				loser = c1;
			}
			try {
				winner.energy += loser.energy/2;
				loser.energy = 0;
				removeThisCritter(loser);
			} catch(Throwable e) {
				System.out.println("winner loser null reference");
			}
		}
		catch(Throwable eror) {
			System.out.println("error in using multiply occupied arraylist as key for hashmap to get critters");
		}
		checkMultiplyOccupied();	//redefines multiply occupied locations
	}
	
	private static void removeThisCritter(Critter toRemove) {
		Coordinate atLoc = new Coordinate(toRemove.x_coord, toRemove.y_coord);
		ArrayList<Critter> crittersHereList = critterAtLoc.get(atLoc);
		if(crittersHereList == null) {
			System.err.println("removing critter at null key in hashmap");
		}
		for(int i = 0; i < crittersHereList.size(); i++) {
			if(crittersHereList.get(i).equals(toRemove)) {
				crittersHereList.remove(i);
				if(crittersHereList.size() == 0) {	//if there are no more critters at this location, remove that key from original hashmap
					critterAtLoc.remove(atLoc);	
				}
				population.remove(toRemove);	//remove critter from population list
				break;
			}
		}
	}
	
	/**
	 * removeDead removes the dead critters at the end of each world time step
	 */
	private static void removeAllDead() {
		for(Coordinate key: critterAtLoc.keySet()) {	//for every populated location
			try {
				for(int i = 0; i < critterAtLoc.get(key).size(); i++) {		//for every critter at given location
					if(critterAtLoc.get(key).get(i).energy <= 0) {
						removeThisCritter(critterAtLoc.get(key).get(i));
					}
				}
			} catch(Throwable e) {
				System.out.println("empty key still in hashset null pointer issue");
			}
		}
	}
	
	/**
	 * make empty world as 2D char array
	 */
	private static void initEmptyWorldView() {
		char[] topBottom = new char[Params.world_width + 2];
		topBottom[0] = '+';
		for(int i = 1; i < (Params.world_width + 1); i++) {
			topBottom[i] = '-';
		}
		topBottom[Params.world_width + 1] = '+';
		worldArray[0] = topBottom;
		for(int rowIndex = 1; rowIndex < Params.world_height + 1; rowIndex++) {
			for(int colIndex = 0; colIndex < Params.world_width + 2; colIndex++) {
				if(colIndex == 0 || colIndex == (Params.world_width + 1)) {
					worldArray[rowIndex][colIndex] = '|';
				} else {
					worldArray[rowIndex][colIndex] = ' ';
				}
			}
		}
		worldArray[Params.world_height + 1] = topBottom;
	}
	
	/** 
	 * add critters into originally empty array
	 */
	private static void populateWorldView() {
		for(int i = 0; i < population.size(); i++) {
			Critter currentCritter = population.get(i);
			worldArray[currentCritter.x_coord + 1][currentCritter.y_coord + 1] = currentCritter.toString().charAt(0);
		}
		for(int i = 0; i < babies.size(); i++) {
			Critter currentCritter = babies.get(i);
			worldArray[currentCritter.x_coord + 1][currentCritter.y_coord + 1] = currentCritter.toString().charAt(0);
		}
	}
	
	/**
	 * view component
	 */
	public static void displayWorld() {
		initEmptyWorldView();
		populateWorldView();
		for(int curRow = 0; curRow < Params.world_height + 2; curRow++) {
			System.out.println(worldArray[curRow]);
		}
	}
		
		
}
