import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GameBoard extends JPanel {

	/**
	 * Counter variables used to display information in the Application
	 */
	private int numCured = 0;
	private int numHealthy = 0;
	private int numInfected = 0;
	private int numDead = 0;
	/**
	 * Number of 'dead' cells at the beginning of the program. Subtract this value from counted numDead to get true numDead value.
	 */
	private int numDeadInitial;
	
	/**
	 * Set to 0 during setup. Resets when the board is reset.
	 */
	public int numDays; 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * A collection of cells, where cellArr[x][y] pairs with the cell located at
	 * (x,y).
	 */
	private Cell[][] cellArr;

	/**
	 * The width of the gameboard in cells
	 */
	private int numCellsWide;

	/**
	 * The height of the gameboard in cells
	 */
	private int numCellsHigh;

	/**
	 * The width of the GameBoard instance in the application (in pixels) Use this
	 * for drawing.
	 */
	private int panelWidth;
	/**
	 * The height of the GameBoard instance in the application (in pixels) Use this
	 * for drawing.
	 */
	private int panelHeight;
	/**
	 * The width of each cell in the application (in pixels) Use this for drawing.
	 */
	private int cellWidth;
	/**
	 * The height of each cell in the application (in pixels) Use this for drawing.
	 */
	private int cellHeight;
	/**
	 * A listener that tells the game board when the user has selected a cell for
	 * the initial configuration of the game.
	 */
	private MouseListener userCellSelectionListener;
	private int userSelectedPointX = -1;
	private int userSelectedPointY = -1;
	private boolean paintUserPoint = false;
	private boolean inSetup;
	
	/**
	 * This should be set in the {@link #endSetup()} method. It keeps track of the seed for a particular iteration, so that the program can be reset to the same seed
	 * Maybe someday the seed can be added to a list of "favorites" by means of coordinate pairs stored in a text file.
	 */
	private Cell[][] seedCellArr;

	/**
	 * Constructs an instance of gameboard.
	 * 
	 * @param w The number of cells horizontally
	 * @param h The number of cells vertically
	 */

	public GameBoard(int w, int h) {
		setBackground(Color.WHITE);
		numCellsWide = w;
		numCellsHigh = h;
		cellArr = new Cell[numCellsWide][numCellsHigh];
		seedCellArr = new Cell[numCellsWide][numCellsHigh];
		startCellArray(cellArr);
		startCellArray(seedCellArr);
	}

	/**
	 * This method overrides the standard setupPanel, it is specifically for the
	 * first call to setupPanel which will determine the size of the panel.
	 */
	public void setupPanel(int pWidth, int pHeight) {
		numDays = 0;
		numDeadInitial = 0;
		inSetup = true;
		this.panelWidth = pWidth;
		this.panelHeight = pHeight;
		this.setSize(panelWidth, panelHeight);
		cellWidth = panelWidth / numCellsWide;
		cellHeight = panelHeight / numCellsHigh + 1;
		this.addMouseListener(userCellSelectionListener = new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				userSelectedPointX = e.getX();
				userSelectedPointY = e.getY();
				paintUserPoint = true;
				repaint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

	}

	/**
	 * Helper method that creates copies of 2D cell arrays
	 * @param orig The array to be copied
	 * @return A copy of the original array.
	 */
	public static Cell[][] copyOf(Cell[][] orig){
		Cell[][] copy = new Cell[orig.length][orig[0].length];
		for(int i = 0; i < orig.length; i++) {
			for(int j = 0; j < orig[0].length; j++) {
				copy[i][j] = orig[i][j].copy();
			}
		}
		return copy;
	}

	/**
	 * This method draws the lines that will delineate the boxes for the game. It
	 * should only be called from the paint method!
	 * 
	 * @param g The graphics context passed from the paint method.
	 */

	public void drawLines(Graphics g) {
		// vertical lines
		g.setColor(Color.WHITE);
		for (double i = 1; i < numCellsWide; i++) {
			int vertLineX = (int) (i * cellWidth);
			g.drawLine(vertLineX, 0, vertLineX, panelHeight);
		}
		// horizontal lines
		for (double j = 1; j < numCellsHigh; j++) {
			int horizontalLineY = (int) (j * cellHeight);
			g.drawLine(0, horizontalLineY, panelWidth, horizontalLineY);
		}
	}

	/**
	 * The main paint method.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		clearCounters();
		drawLines(g);
		for(int i = 0; i < cellArr.length; i++) {
			for(int j = 0; j < cellArr[0].length; j++) {
				Cell currentCell = cellArr[i][j];
				if(currentCell.isCured()) {
					paintCell(g, currentCell, Color.GREEN);
					numCured++;
				}
				else if (currentCell.isHealthy()) {
					paintCell(g, currentCell, Color.WHITE);
					numHealthy++;
				}
				else if (currentCell.isInfected()) {
					paintCell(g, currentCell, Color.RED);
					numInfected++;
				}
				else if (currentCell.isDead()){
					paintCell(g, currentCell, Color.BLACK);
					numDead++;
				}
			}
		}
		if (paintUserPoint) {
//			Cell currentCell = getCellFromPoint(userSelectedPointX, userSelectedPointY);
			int cellX = userSelectedPointX / cellWidth;
			int cellY = userSelectedPointY / cellHeight;
			Cell currentCell = cellArr[cellX][cellY];
			if (currentCell.isDead()) {
				currentCell.setAlive();
				paintCell(g, currentCell, Color.WHITE);
				numDeadInitial--;
			}
			else if (currentCell.isAlive() && currentCell.isHealthy()){
				currentCell.setInfected();
				paintCell(g, currentCell, Color.RED);
			}
			else if (currentCell.isAlive() && currentCell.isInfected()){
				currentCell.setDead();
				paintCell(g, currentCell, Color.BLACK);
				numDeadInitial++;
			}
			paintUserPoint = false;
		}
	}
	
	/**
	 * This method fills a cell with the specified color. It
	 * should only be called from the paint method!
	 * @param g The graphics context passed from the paint method.
	 */
	private void paintCell(Graphics g, Cell cell, Color color) {
		g.setColor(color);
		g.fillRect(cell.getxPos()*cellWidth+1, cell.getyPos()*cellHeight+1, cellWidth-1, cellHeight-1);
	}

	/**
	 * Populates a cell array.
	 */
	private void startCellArray(Cell[][] cellArr) {
		for (int x = 0; x < cellArr.length; x++) {
			for (int y = 0; y < cellArr[0].length; y++) {
				cellArr[x][y] = new Cell(x, y);
			}
		}
	}

	/**
	 * Sets the cells to their new life status
	 */
	public void nextStep() {
		setNeighborsAlive();
		setNeighborsInfected();
		for (int x = 0; x < cellArr.length; x++) {
			for (int y = 0; y < cellArr[0].length; y++) {
				Cell currentCell = cellArr[x][y];
				currentCell.determineLifeStatus();
			}
		}
	}

	// Helper methods for nextStep()

	/**
	 * This is a helper method for {@link #nextStep()} It sets the number of
	 * alive neighbors for each cell.
	 */
	private void setNeighborsAlive() {
		for (int x = 0; x < cellArr.length; x++) {
			for (int y = 0; y < cellArr[0].length; y++) {
				Cell currentCell = cellArr[x][y];

				currentCell.clearNeighborsAlive();

				/**
				 * East (E)
				 */
				if (cellExists(x + 1, y))
					if (cellIsAlive(x + 1, y))
						currentCell.addNeighborAlive();

				/**
				 * Northeast (NE)
				 */
				if (cellExists(x + 1, y + 1))
					if (cellIsAlive(x + 1, y + 1))
						currentCell.addNeighborAlive();

				/**
				 * North (N)
				 */
				if (cellExists(x, y + 1))
					if (cellIsAlive(x, y + 1))
						currentCell.addNeighborAlive();

				/**
				 * Northwest (NW)
				 */
				if (cellExists(x - 1, y + 1))
					if (cellIsAlive(x - 1, y + 1))
						currentCell.addNeighborAlive();

				/**
				 * West (W)
				 */
				if (cellExists(x - 1, y))
					if (cellIsAlive(x - 1, y))
						currentCell.addNeighborAlive();

				/**
				 * Southwest (SW)
				 */
				if (cellExists(x - 1, y - 1))
					if (cellIsAlive(x - 1, y - 1))
						currentCell.addNeighborAlive();

				/**
				 * South (S)
				 */
				if (cellExists(x, y - 1))
					if (cellIsAlive(x, y - 1))
						currentCell.addNeighborAlive();

				/**
				 * Southeast (SE)
				 */
				if (cellExists(x + 1, y - 1))
					if (cellIsAlive(x + 1, y - 1))
						currentCell.addNeighborAlive();
			}
		}
	}
	
	/**
	 * This is a helper method for {@link #nextStep()} It sets the number of
	 * infected neighbors for each cell.
	 */
	private void setNeighborsInfected() {
		for (int x = 0; x < cellArr.length; x++) {
			for (int y = 0; y < cellArr[0].length; y++) {
				Cell currentCell = cellArr[x][y];

				currentCell.clearNeighborsInfected();

				/**
				 * East (E)
				 */
				if (cellExists(x + 1, y))
					if (cellIsInfected(x + 1, y))
						currentCell.addNeighborInfected();

				/**
				 * Northeast (NE)
				 */
				if (cellExists(x + 1, y + 1))
					if (cellIsInfected(x + 1, y + 1))
						currentCell.addNeighborInfected();

				/**
				 * North (N)
				 */
				if (cellExists(x, y + 1))
					if (cellIsInfected(x, y + 1))
						currentCell.addNeighborInfected();

				/**
				 * Northwest (NW)
				 */
				if (cellExists(x - 1, y + 1))
					if (cellIsInfected(x - 1, y + 1))
						currentCell.addNeighborInfected();

				/**
				 * West (W)
				 */
				if (cellExists(x - 1, y))
					if (cellIsInfected(x - 1, y))
						currentCell.addNeighborInfected();

				/**
				 * Southwest (SW)
				 */
				if (cellExists(x - 1, y - 1))
					if (cellIsInfected(x - 1, y - 1))
						currentCell.addNeighborInfected();

				/**
				 * South (S)
				 */
				if (cellExists(x, y - 1))
					if (cellIsInfected(x, y - 1))
						currentCell.addNeighborInfected();

				/**
				 * Southeast (SE)
				 */
				if (cellExists(x + 1, y - 1))
					if (cellIsInfected(x + 1, y - 1))
						currentCell.addNeighborInfected();
			}
		}
	}

	/**
	 * Determines whether a point is within the bounds of the game board.
	 * 
	 * @param x The width-wise position of the point
	 * @param y The height-wise position of the point
	 * @return True if the point is within the bounds, false if not.
	 */
	private boolean cellExists(int x, int y) {
		if (x >= 0 && y >= 0) {
			if (x < numCellsWide && y < numCellsHigh) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Determines whether the cell at given coordinates is alive or not. This method
	 * requires that the given coordinates are within the bounds of the game board.
	 * 
	 * @param x The width-wise position of the cell
	 * @param y The height-wise position of the cell
	 * @return True if the cell is alive, false if not.
	 */
	private boolean cellIsAlive(int x, int y) {
		if (cellArr[x][y].isAlive())
			return true;
		else
			return false;
	}
	
	/**
	 * Determines whether the cell at given coordinates is infected or not. This method
	 * requires that the given coordinates are within the bounds of the game board.
	 * 
	 * @param x The width-wise position of the cell
	 * @param y The height-wise position of the cell
	 * @return True if the cell is infected, false if not.
	 */
	private boolean cellIsInfected(int x, int y) {
		if (cellArr[x][y].isInfected())
			return true;
		else
			return false;
	}

	public void clear() {
		setBackground(Color.WHITE);
		cellArr = new Cell[numCellsWide][numCellsHigh];
		startCellArray(cellArr);
	}
	
	public void setToSeed() {
		cellArr = copyOf(seedCellArr);
	}
	
	/**
	 * This method is called by the application when setup is complete. It prevents
	 * the user from further editing the game board, thus allowing the game to run
	 * free of interference.
	 */
	public void endSetup() {
		if(inSetup) 
		{
		this.removeMouseListener(userCellSelectionListener);
		seedCellArr = copyOf(cellArr);
		}
		inSetup = false;
	}
	
	/**
	 * @return false if every cell on the gameboard is dead, true if there is at least one live cell.
	 */
	public boolean isEmpty() {
		for(int i = 0; i < cellArr.length; i++) {
			for(int j = 0; j < cellArr[0].length; j++) {
				Cell currentCell = cellArr[i][j];
				if(currentCell.isAlive()) return false;
			}
		}
		return true;
	}
	
	public void randomizeSeed(double probAlive) {
		for(int i = 0; i < seedCellArr.length; i++) {
			for(int j = 0; j < seedCellArr[0].length; j++) {
				Cell currentCell = seedCellArr[i][j];
				if(i == seedCellArr.length/2 && j == seedCellArr[0].length/2) currentCell.setInfected();
				else {
					
				if(Math.random() < probAlive) currentCell.setAlive();
				else {
					currentCell.setDead();
					numDeadInitial++;
				}
				}
			}
		}
	}
	
	/**
	 * Clears the counter variables. Executed during each paint step, before counting.
	 */
	private void clearCounters() {
		numCured = 0;
		numHealthy = 0;
		numInfected = 0;
		numDead = 0;
	}

	/**
	 * @return the numCured
	 */
	public int getNumCured() {
		return numCured;
	}
	
	/**
	 * @return the numHealthy
	 */
	public int getNumHealthy() {
		return numHealthy;
	}

	/**
	 * @return the numInfected
	 */
	public int getNumInfected() {
		return numInfected;
	}

	/**
	 * @return the numDead
	 */
	public int getNumDead() {
		return numDead-numDeadInitial;
	}

	/**
	 * @return the numWeeks
	 */
	public int getNumWeeks() {
		return numDays;
	}

	/**
	 * @param numDays the numWeeks to set
	 */
	public void addWeeks(int numToAdd) {
		this.numDays += numToAdd;
	}
	
	
	
}
