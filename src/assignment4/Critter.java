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



import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* see the PDF for descriptions of the methods and fields in this class
 * you may add fields, methods or inner classes to Critter ONLY if you make your additions private
 * no new public, protected or default-package code or data can be added to Critter
 */


/**
 * Critter is the abstract class that serves as the base class for all types of Critter, implements common functionality methods between subclasses of Critters
 * @author Isabel Li
 * @version 1.0
 */
public abstract class Critter {
	private static int numWalks = 0;
	private static int numNewLocsForWalking = 0;	//used in debugging
	private static int numLocsDelForWalking = 0;	//used in debugging
	private static String myPackage;
	private	static List<Critter> population = new java.util.ArrayList<Critter>();	//holds all critters alive or that died in current time step
	private static List<Critter> babies = new java.util.ArrayList<Critter>();	//babies formed each time step
	private static char[][] worldArray = new char[Params.world_height + 2][Params.world_width + 2];	//2D array holds information about world
	private static Map<Coordinate, ArrayList<Critter>> critterAtLocMap = new HashMap<Coordinate, ArrayList<Critter>>();	//critterAtLocMap Map: key is Critter location and value is all critters at that location
	private static ArrayList<Coordinate> multiplyOccupied = new ArrayList<Coordinate>();	//define an ARrayList of multiply occupied coordinates
	private static boolean needResolveConflicts = false;	//stores whether or not we need critters to keep fighting
	private boolean deadToRemove = false;	//dead, to be removed at end of time step. doesn't result in conflicts
	private boolean movedThisStep = false;	//helps determine whether critter can run away during a fight
	private boolean isFighting = false;	//isFighting, used so that we can call walk when Critter is fighting
	private boolean isRunningFight = false;	//helps determine whether the critter should be allowed to take over new spot when running away during a fight
	private int walkRunFightCallNum = 0;	//helper variable for same purpose as isRunnignFight
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}
	private static java.util.Random rand = new java.util.Random();
	
	/**
	 * getRandomInt returns a random integer between 0 and max
	 * @param max is the highest value the random number may be
	 * @return a random integer between 0 and max
	 */
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}
	
	/**
	 * setSeed is called when the user wants to seed random number generation
	 * @param new_seed is seed specified by user
	 */
	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}
	
	/**
	 * overriden by subclasses of Critter, determines visual display o Critter
	 */
	/* a one-character long string that visually depicts your critter in the ASCII interface */
	public String toString() { return ""; }
	
	private int energy = 0;	//amount of energy Critter has
	
	/**
	 * allows clients to access energy value of critter
	 * @return energy value of current critter
	 */
	protected int getEnergy() { return energy; }
	
	private int x_coord;	//determines critter's location on map
	private int y_coord;	//determines critter's location on map
	
	/**
	 * allows Critter to move around grid
	 * @param direction specifies the direction the critter is taking a step in
	 */
	protected final void walk(int direction) {
		if(isFighting && movedThisStep) {	//if fighting and already moved, do not move
			this.deadToRemove = true;
			this.energy = 0;
			return;
		}
		numWalks++;	//isa debug variable
		this.energy -= Params.walk_energy_cost;
		if(!isFighting) {
			movedThisStep = true;
			removeThisCritter(this, true); //remove Critter from previous location in map of locs and critters
		}	
		if(this.energy <= 0) {
			deadToRemove = true;	
		}
		if(direction == 0) {
			if(this.x_coord == Params.world_width - 1) {	//to wrap aroun
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
			Coordinate newLoc = new Coordinate(this.x_coord, this.y_coord);
			if(isFighting) {	//if fighting
				if(countNumAliveAtLocation(newLoc) > 0 && walkRunFightCallNum == 2) {	//if is fighting and location is already occupied, do not create new location in map
					this.deadToRemove = true;
					this.energy = 0;
					return;
				} if(countNumAliveAtLocation(newLoc) > 0 && walkRunFightCallNum == 0) {
					this.deadToRemove = true;
					this.energy = 0;
					return;
				}
				removeThisCritter(this, true);	//if there are no other alive critters, remove and allocate new spot
			}
			numNewLocsForWalking++;	//for debugging
			if(critterAtLocMap.get(newLoc) == null) {	//if there are no critters at that location
				ArrayList<Critter> critList = new ArrayList<Critter>();
				critList.add(this);
				critterAtLocMap.put(newLoc, critList);
			} else {
				critterAtLocMap.get(newLoc).add(this);
			}
		}
	
	/**
	 * makes the critter take two steps in specified direction during one time step
	 * @param direction specifies direction critter moves in
	 */
	protected final void run(int direction) {
		if(!isFighting) {
			movedThisStep = true;
		}
		walkRunFightCallNum = 0;
		if(isFighting) {
			isRunningFight = true;
			//this.energy -= Params.run_energy_cost;
		}
		if(isFighting && movedThisStep) {	//if already moved, don't move
			this.energy -= Params.run_energy_cost;
			return;
		}
		this.energy += (2*Params.walk_energy_cost);	//add energy that will be deducted from walking
		walkRunFightCallNum = 1;
		this.walk(direction);
		walkRunFightCallNum = 2;
		this.walk(direction);
		this.energy -= Params.run_energy_cost;	//deduct run energy cost
		if(this.energy <= 0) {
			deadToRemove = true;
			//Critter.removeThisCritter(this, false);
		} 
		//movedThisStep = true;	
	}
	
	/**
	 * reproduce sets values of offspring the Critter produced
	 * @param offspring	new Critter
	 * @param direction helps specify where new Critter is placed
	 */
	protected final void reproduce(Critter offspring, int direction) {
		int parentEnergy = this.energy;
		if(parentEnergy >= Params.min_reproduce_energy) {
			offspring.energy = parentEnergy/2;
			this.energy = parentEnergy/2;
			if(this.energy*2 < parentEnergy) {
				this.energy++;	//round up, based on instruction specifications
			}
			if(this.energy <= 0) {
				deadToRemove = true;
			}
			int childX = -1;
			int childY = -1;
			if(direction == 0) {
				if(this.x_coord == Params.world_width - 1) {	//helps determine where to place the critter
					childX = 0;
				} else {
					childX = this.x_coord + 1;
				}
				childY = this.y_coord;
			} else if(direction == 1) {
				if(this.x_coord == Params.world_width - 1) {
					childX = 0;
				} else {
					childX = this.x_coord + 1;
					
				}
				if(this.y_coord == 0) {
					childY = Params.world_height - 1;
				} else {
					childY = this.y_coord - 1;
				}
			} else if(direction == 2) {
				if(this.y_coord == 0) {
					childY = Params.world_height - 1;
				} else {
					childY = this.y_coord - 1;
				}
				childX = this.x_coord;
			} else if(direction == 3) {
				if(this.x_coord == 0) {
					childX = Params.world_width - 1;
				} else {
					childX = this.x_coord - 1;
				}
				if(this.y_coord == 0) {
					childY = Params.world_height - 1;
				} else {
					childY = this.y_coord;
				}
			} else if(direction == 4) {
				if(this.x_coord == 0) {
					childX = Params.world_width - 1;
				} else {
					childX = this.x_coord - 1;
				}
				childY = this.y_coord;
			} else if(direction == 5) {
				if(this.x_coord == 0) {
					childX = Params.world_width - 1;
				} else {
					childX = this.x_coord - 1;
				}
				if(this.y_coord == Params.world_height - 1) {	//wrap around
					childY = 0;
				} else {
					childY = this.y_coord + 1;
				}
			} else if(direction == 6) {
				if(this.y_coord == Params.world_height - 1) {
					childY = 0;
				} else {
					childY = this.y_coord + 1;
				}
				childX = this.x_coord;
			} else if(direction == 7) {
				if(this.x_coord == Params.world_width - 1) {
					childX = 0;
				} else {
					childX = this.x_coord + 1;
				}
				if(this.y_coord == Params.world_height - 1) {
					childY = 0;
				} else {
					childY = this.y_coord + 1;
				}
			}
			offspring.x_coord = childX;
			offspring.y_coord = childY;
//			Coordinate newLoc = new Coordinate(childX, childY);
//			if(critterAtLocMap.get(newLoc) == null) {	//if there are no critters at that location
//				ArrayList<Critter> critList = new ArrayList<Critter>();
//				critList.add(offspring);
//				critterAtLocMap.put(newLoc, critList);
//			} else {
//				critterAtLocMap.get(newLoc).add(offspring);
//			}
			babies.add(offspring);	//add new Critter to babies arraylist
		}
	}
	
	/**
	 * time step of each critter is defined in subclass
	 */
	public abstract void doTimeStep();
	
	/**
	 * way each critter fights in defined in subclass
	 * @param oponent specifies whom the critter is fighting
	 * @return whether or not the critter will fight
	 */
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
			String className = "assignment4." + critter_class_name;	//specify class name path
			Critter newCritter = (Critter) Class.forName(className).newInstance();
			newCritter.x_coord = getRandomInt(Params.world_width);	//place to put critter
			newCritter.y_coord = getRandomInt(Params.world_height);
			newCritter.energy = Params.start_energy;	//add new critter to population
			population.add(newCritter);
			Coordinate loc = new Coordinate(newCritter.x_coord, newCritter.y_coord);
			if(critterAtLocMap.get(loc) == null) {	//if there are no critters at that location
				ArrayList<Critter> critList = new ArrayList<Critter>();
				critList.add(newCritter);
				critterAtLocMap.put(loc, critList);
			} else {
				critterAtLocMap.get(loc).add(newCritter);
			}
		} catch(Throwable error) {
			//System.err.println(error);
			throw new InvalidCritterException(critter_class_name);
		}
	}
	
	/**
	 * check if there are multiple critters at a specific location
	 */
	private static void checkMultiplyOccupied() {
		needResolveConflicts = false;
		multiplyOccupied.clear(); //redefine list of multiply occupied locations at each call
		for(Coordinate key : critterAtLocMap.keySet()) {	
			if(countNumAliveAtLocation(key) > 1) {//if there is more than one critter at this coordinate
				multiplyOccupied.add(key);
			}
		}
		if(multiplyOccupied.size() > 0) {	//if there is a multiply occupied location
			needResolveConflicts = true;
		}
	}
	
	/**
	 * count number of critters alive at specific location
	 * @param coord specifies location we are checking for critters at
	 * @return number of critters alive at that location (not newly created algae)
	 */
	private static int countNumAliveAtLocation(Coordinate coord) {
		int numAlive = 0;
		for(int i = 0; i < critterAtLocMap.get(coord).size(); i++) {
			if(!(critterAtLocMap.get(coord).get(i).deadToRemove) && critterAtLocMap.get(coord).get(i).energy > 0) {
				if(!(critterAtLocMap.get(coord).get(i) instanceof Algae)) {	//don't count newly created algae
					numAlive++;
				}
			}
		}
		return numAlive;
	}
	
	/**
	 * Gets a list of critters of a specific type.
	 * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
	 * @return List of Critters.
	 * @throws InvalidCritterException
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		List<Critter> result = new java.util.ArrayList<Critter>();
		try {
			Class<?> critterClass = Class.forName("assignment4." + critter_class_name);
			for(int i = 0; i < population.size(); i++) {
				if( critterClass.isInstance((population.get(i)))) {	//getting instances of specified critter type
					result.add(population.get(i));;
				}
			}	
			Method method = critterClass.getMethod("runStats", List.class);
			Object o = method.invoke(null, result);
		}
		catch(Exception e) {
			throw new InvalidCritterException(critter_class_name);
		}
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
			if(new_energy_value <= 0) {
				super.deadToRemove = true;
				//super.removeThisCritter(this, false);
			}
		}
		
		/**
		 * setX_coord sets new x coordinate
		 * @param new_x_coord
		 */
		protected void setX_coord(int new_x_coord) {
			int direction = -1;
			if(new_x_coord > super.x_coord) {
				direction = 0;
			} else if(new_x_coord < super.x_coord) {	//determine direction for critter to walk in
				direction = 4;
			}
			int numSteps = Math.abs(new_x_coord - super.x_coord);
			int addEnergy = numSteps*Params.walk_energy_cost;	//so that we can use walk function
			super.energy += addEnergy;
			for(int i = 0; i < numSteps; i++) {
				walk(direction);
			}
			super.movedThisStep = false;
			System.out.println("");
		}	
		
		/**
		 * setY_coord sets new y coordinate
		 * @param new_y_coord
		 */
		protected void setY_coord(int new_y_coord) {
			int direction = -1;
			if(new_y_coord > super.y_coord) {
				direction = 6;
			} else if(new_y_coord < super.y_coord) {
				direction = 2;
			}
			int numSteps = Math.abs(new_y_coord - super.y_coord);
			int addEnergy = numSteps*Params.walk_energy_cost;	//so that we can use walk function
			super.energy += addEnergy;
			for(int i = 0; i < numSteps; i++) {
				walk(direction);
			}
			super.movedThisStep = false;
		}
		
		/**
		 * getX_coord returns x coordinate
		 * @return current x coordinate
		 */
		protected int getX_coord() {
			return super.x_coord;
		}
		
		/**
		 * getY_coord returnsn y coordinate
		 * @return y coordinate
		 */
		protected int getY_coord() {
			return super.y_coord;
		}
		

		/*
		 * This method getPopulation has to be modified by you if you are not using the population
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.
		 */
		/**
		 * getPopulation returns arraylist of population
		 * @return population arraylist
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
		/**
		 * getBabies returns arraylist of babies
		 * @return babies arraylist
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
		population.clear();
		babies.clear();
		critterAtLocMap.clear();
		
	}
	
	/**
	 * simulate a time step for the entire world
	 */
	public static void worldTimeStep() {
		int numPopulation = population.size();
		for(Critter c: population) {
			c.doTimeStep();	//do time step on each member of population	
		}
		checkMultiplyOccupied();	//fill ArrayList with coordinates with more than 1 critter
		while(needResolveConflicts) {	//while there is more than 1 conflict to resolve
			resolveConflicts();
		}
		for(Critter c : population) {	//deduct rest energy cost from everybody
			c.energy -= Params.rest_energy_cost;
			c.movedThisStep = false;
		}
		for(int i = 0; i < Params.refresh_algae_count; i++) {
			try {
				makeCritter("Algae");
			}
			catch(Throwable e) {
				System.out.println("error making algae");
			}
		}
		removeAllDead();	//remove all dead
		for(int i = 0; i < babies.size(); i++) {	//add babies to map
			population.add(babies.get(i));
			Coordinate newLoc = new Coordinate(babies.get(i).x_coord, babies.get(i).y_coord);	//add babies to the map
			if(critterAtLocMap.get(newLoc) == null) {	//if there are no critters at that location
				ArrayList<Critter> critList = new ArrayList<Critter>();
				critList.add(babies.get(i));
				critterAtLocMap.put(newLoc, critList);
			} else {
				critterAtLocMap.get(newLoc).add(babies.get(i));
			}
		}
		babies.clear();
	}
	
	/**
	 * resolve conflicts (multiply occupied locations) until each coordinate has at most 1 critter
	 */
	private static void resolveConflicts() {
		Coordinate currentLoc = multiplyOccupied.get(0);	//at least one multiply occupied location
		try {
			int indexFirstAlive = -1;
			int indexSecondAlive = -1;
			for(int i = 0; i < critterAtLocMap.get(currentLoc).size(); i++) {	//determine indexes of alive critters (some may be dead that are yet to be removed)
				if(!critterAtLocMap.get(currentLoc).get(i).deadToRemove && critterAtLocMap.get(currentLoc).get(i).energy > 0) {
					indexFirstAlive= i;
					break;
				}
			}
			for(int i = indexFirstAlive + 1; i < critterAtLocMap.get(currentLoc).size(); i++) {
				if(!critterAtLocMap.get(currentLoc).get(i).deadToRemove && critterAtLocMap.get(currentLoc).get(i).energy > 0) {
					indexSecondAlive = i;
					break;
				}
			}
			if(indexFirstAlive == -1 || indexSecondAlive == -1) {
				System.err.println("encounter not found when it's supposed to be found");
			}
			Critter c1 = critterAtLocMap.get(currentLoc).get(indexFirstAlive);
			Critter c2 = critterAtLocMap.get(currentLoc).get(indexSecondAlive);	//two critters to resolve encounter
			Critter winner = null;
			Critter loser = null;
			boolean c1Fight = c1.fight(c2.toString());
			c1.isFighting = c1Fight;
			boolean c2Fight = c2.fight(c1.toString());
			c2.isFighting = c2Fight;
			int c1Roll = -1;
			int c2Roll = -1;
			if((!c1.deadToRemove && !c2.deadToRemove) && (c1.x_coord == c2.x_coord) && (c1.y_coord == c2.y_coord) && c1.energy > 0 && c2.energy > 0) {
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
					c1Roll = 0;
					c2Roll = 0;
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
				} else if(c1Roll > c2Roll) {	//determine winner based on roll
					winner = c1;
					loser = c2;
				} else if(c2Roll > c1Roll) {
					winner = c2;
					loser = c1;
				}
				c1.isFighting = false;
				c2.isFighting = false;
				try {
					winner.energy += loser.energy/2;	//distribute appropriate energy values
					loser.energy = 0;
					loser.deadToRemove = true;
					//removeThisCritter(loser, false);
				} catch(Throwable e) {
					System.out.println("winner loser null reference: " + e);
				}
			}	
		}
		catch(Exception eror) {
			System.out.println("error in using multiplly occupied arraylist as key for hashmap to get critters: " );
			System.out.println(eror.getStackTrace());;
			//System.exit(1);
		}
		checkMultiplyOccupied();	//redefines multiply occupied locations
	}
	
	/**
	 * remove a specific critter from location in map (possibly from walking or because dead
	 * @param toRemove is the critter we need to remove
	 * @param justMoving if true, we don't delete the critter entirely- just moving its coordinate in map
	 */
	private static void removeThisCritter(Critter toRemove, boolean justMoving) {
		if(justMoving) {
			numLocsDelForWalking++;
		}
		Coordinate atLoc = new Coordinate(toRemove.x_coord, toRemove.y_coord);	//critter location we are removing from
		ArrayList<Critter> crittersHereList = critterAtLocMap.get(atLoc);	//get all critters there
//		Set<Coordinate> keySet = critterAtLocMap.keySet();	prints for debugging
//		System.out.println("All locations");
//		for (Coordinate cord :  keySet) {
//			System.out.println(cord + ": " + critterAtLocMap.get(cord).size());
//		}
//		if(!justMoving) {
//			System.out.println("coordinate to remove: " +  atLoc);	
//		}
		if(crittersHereList == null) {	//for debugging
			System.err.println(numWalks);
			System.err.println(numLocsDelForWalking);
			System.err.println(numNewLocsForWalking);
			System.err.println("removing critter at null key in hashmap");
		} else {
			//System.out.println(" crittersHereList.size(): " +  crittersHereList.size());
		}
		for(int i = 0; i < crittersHereList.size(); i++) {
			if(crittersHereList.get(i).equals(toRemove)) {
				crittersHereList.remove(i);
				if(crittersHereList.size() == 0) {	//if there are no more critters at this location, remove that key from original hashmap
					critterAtLocMap.remove(atLoc);	
				}
				if(!justMoving) {
					population.remove(toRemove);	//if we are killing the critter, remove it from population
				}
					//remove critter from population list
				break;
			}
		}
	}
	
	/**
	 * removeDead removes the dead critters at the end of each world time step
	 */
	private static void removeAllDead() {
		List<Critter> toBeRemoved = new ArrayList<Critter>();
		for(Coordinate key: critterAtLocMap.keySet()) {	//for every populated location
			try {
				for(int i = 0; i < critterAtLocMap.get(key).size(); i++) {		//for every critter at given location
					if(critterAtLocMap.get(key).get(i).energy <= 0 || critterAtLocMap.get(key).get(i).deadToRemove == true) {
						toBeRemoved.add(critterAtLocMap.get(key).get(i));
					}
				}
			} catch(Exception e) {
				System.err.println("remove all dead  ISSUE: " + e.getStackTrace());
			}
		}
		for(int i = 0; i < toBeRemoved.size(); i++) {
			removeThisCritter(toBeRemoved.get(i), false);
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
		topBottom[Params.world_width + 1] = '+';	//frame by definition	
		worldArray[0] = topBottom;	//set value of 2D array
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
		for(int i = 0; i < population.size(); i++) {	//populating view based on alive critters
			Critter currentCritter = population.get(i);	//add check to make sure critter is alive?
			worldArray[currentCritter.y_coord + 1][currentCritter.x_coord + 1] = currentCritter.toString().charAt(0);
		}
		for(int i = 0; i < babies.size(); i++) {
			Critter currentCritter = babies.get(i);
			worldArray[currentCritter.y_coord + 1][currentCritter.x_coord + 1] = currentCritter.toString().charAt(0);
		}
	}
	
	/**
	 * view component
	 */
	public static void displayWorld() {
		initEmptyWorldView();
		populateWorldView();
		for(int curRow = 0; curRow < Params.world_height + 2; curRow++) {
			System.out.println(String.valueOf(worldArray[curRow]).trim());
		}
//		System.out.println("debug view");
//		for(int curRow = 0; curRow < Params.world_height; curRow++) {
//			for(int curCol = 0; curCol < Params.world_width; curCol++) {
//				if(critterAtLocMap.get(new Coordinate(curCol, curRow)) != null) {
//					System.out.print(countNumAliveAtLocation(new Coordinate(curCol, curRow)) + " ");
//				}
//			}
//			System.out.println();
//		}
	}		
}
