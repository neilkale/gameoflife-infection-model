/**
 * 
 * @author Neil Kale
 *
 */
public class Cell {

	/**
	 * TODO: Add functionality for various other diseases, which will change these values. Account for the curve of death - higher chance
	 * as you approach the middle of the disease. Have the chance ebb away to 0, at which point the cell is considered "recovered."
	 * Differentiate between dead and recovered cells.
	 * 
	 * Current values are place-holders based on world-wide data available on COVID-19.
	 */
	public static final double COVID_CHANCE_OF_DEATH = 0.00;
	public static final double COVID_CHANCE_OF_RECOVERY = 0.00;
	
	/**
	 * Whether or not the cell is alive (true means alive, false means dead)
	 */
	private boolean alive;

	/*****
	 * CORONA MODEL *** Whether or not the cell is infected (true means infected,
	 * false means healthy)
	 */
	private boolean infected;

	/**
	 * The x position of the cell, always a positive integer.
	 */
	private int xPos;

	/**
	 * The y position of the cell, always a positive integer.
	 */
	private int yPos;

	/*****
	 * CORONA MODEL *** The number of alive neighbors this cell has. This should
	 * range from 0 to 8, inclusive.
	 */
	private int numNeighborsAlive;

	/*****
	 * CORONA MODEL *** The number of infected neighbors this cell has. This should
	 * range from 0 to 8, inclusive.
	 */
	private int numNeighborsInfected;

	/**
	 * Standard constructor, to be used when making a cell on the GameBoard
	 * 
	 * @param x The x position of the cell
	 * @param y The y position of the cell
	 */
	public Cell(int x, int y) {
		numNeighborsInfected = 0;
		numNeighborsAlive = 0;
		xPos = x;
		yPos = y;
		alive = false;
		infected = false;
	}

	/**
	 * Standard constructor, to be used when making a cell on the GameBoard
	 * 
	 * @param x The x position of the cell
	 * @param y The y position of the cell
	 */
	public Cell(Cell c) {
		numNeighborsAlive = c.numNeighborsAlive;
		numNeighborsInfected = c.numNeighborsInfected;
		xPos = c.xPos;
		yPos = c.yPos;
		alive = c.alive;
		infected = c.infected;
	}

	/*****
	 * CORONA MODEL *** Determines whether the cell should become infected or not.
	 */
	public void determineLifeStatus() {
		if (alive) {
			if (numNeighborsAlive < 2)
				setDead(); // underpopulation
			else if (numNeighborsAlive == 2 || numNeighborsAlive == 3)
				; // stayin' alive
			else if (numNeighborsAlive > 3)
				setDead(); // overpopulation

			if (!infected) {
				if(numNeighborsInfected > 0) setInfected();
			} else {
				double chanceOfElimination = COVID_CHANCE_OF_DEATH + COVID_CHANCE_OF_RECOVERY;
				if(Math.random() < chanceOfElimination) setDead();
			}
		}
		else if (numNeighborsAlive == 3)
			setAlive(); // reproduction
	}

	/**
	 * Sets the cell to alive.
	 */
	public void setAlive() {
		alive = true;
	}

	/*****
	 * CORONA MODEL *** Sets the cell to dead.
	 */
	public void setDead() {
		alive = false;
		infected = false;
	}

	/*****
	 * CORONA MODEL *** Sets the cell to healthy
	 */

	public void setHealthy() {
		infected = false;
		alive = true;
	}

	/*****
	 * CORONA MODEL *** Sets the cell to infected
	 */

	public void setInfected() {
		infected = true;
		alive = true;
	}

	/**
	 * Gets the current life status of the cell.
	 * 
	 * @return {@link #isAlive()}
	 */
	public boolean isAlive() {
		return alive;
	}

	/**
	 * Gets the current life status of the cell.
	 * 
	 * @return the opposite of {@link #isAlive()}
	 */
	public boolean isDead() {
		return !alive;
	}

	/*****
	 * CORONA MODEL *** Gets the current life status of the cell.
	 * 
	 * @return {@link #isAlive()}
	 */
	public boolean isInfected() {
		return infected;
	}

	/*****
	 * CORONA MODEL *** Gets the current life status of the cell.
	 * 
	 * @return {@link #isAlive()}
	 */
	public boolean isHealthy() {
		return !infected;
	}

	/**
	 * Gets the x position of this cell
	 * 
	 * @return {@link #xPos}
	 */
	public int getxPos() {
		return xPos;
	}

	/**
	 * @deprecated Sets the x position of the cell
	 * @param xPos New x position.
	 */
	public void setxPos(int xPos) {
		this.xPos = xPos;
	}

	/**
	 * Gets the y position of this cell
	 * 
	 * @return {@link #yPos}
	 */
	public int getyPos() {
		return yPos;
	}

	/**
	 * @deprecated Sets the x position of the cell
	 * @param yPos New y position.
	 */
	public void setyPos(int yPos) {
		this.yPos = yPos;
	}

	/**
	 * Gets the number of alive neighbors this cell has.
	 * 
	 * @return {@link #numNeighborsAlive}
	 */
	public int getNumNeighborsAlive() {
		return numNeighborsAlive;
	}

	/**
	 * Sets the number of alive neighbors to 0. This must be done before
	 * recalculating neighbors.
	 */
	public void clearNeighborsAlive() {
		numNeighborsAlive = 0;
	}

	/**
	 * Increments the number of alive neighbors by 1.
	 */
	public void addNeighborAlive() {
		numNeighborsAlive++;
	}

	/*****
	 * CORONA MODEL *** Gets the number of infected neighbors this cell has.
	 * 
	 * @return {@link #numNeighborsAlive}
	 */
	public int getNumNeighborsInfected() {
		return numNeighborsInfected;
	}

	/*****
	 * CORONA MODEL *** Sets the number of infected neighbors to 0. This must be
	 * done before recalculating neighbors.
	 */
	public void clearNeighborsInfected() {
		numNeighborsInfected = 0;
	}

	/*****
	 * CORONA MODEL *** Increments the number of infected neighbors by 1.
	 */
	public void addNeighborInfected() {
		numNeighborsInfected++;
	}

	/**
	 * Copies a cell
	 * 
	 * @return a new instance of cell with the same private variables and state as
	 *         this one
	 */
	public Cell copy() {
		Cell copy = new Cell(this);
		return copy;
	}
}
