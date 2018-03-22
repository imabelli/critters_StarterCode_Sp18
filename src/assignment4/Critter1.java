package assignment4;

/**
 * Critter 1 fights an opponent only when it has energy over 10. 
 * With each time step, the critter runs in direction 1.
 * Critter1 is represented by "1" on the view
 * @author Isabel Li
 * UTEID: ijl283
 *
 */
public class Critter1 extends Critter {
	
	/**
	 * Critter1 is modeled as a "1" in the grid
	 */
	@Override
	public String toString() {
		return "1";
	}
	
	/**
	 * how the critter behaves to resolve an encounter
	 * @param opponent specifies whom the Critter is fighting
	 */
	public boolean fight(String opponent) {
		if(this.getEnergy() > 30) {	//only fight if energy is over 30
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Critter1 runs in direction1  with each time step
	 */
	public void doTimeStep() {
		run(1);
	}
}
