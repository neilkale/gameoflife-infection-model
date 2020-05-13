import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JSeparator;
import java.awt.Color;

public class Application extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final double SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static final double SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	
	private double randomLifeProb = 0.6;
	private int sizeConstant = 15;

	GameBoard gameboard;

	public Application() {
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyChar() == 'p') play();
				if(e.getKeyChar() == 'r') reset();
				if(e.getKeyChar() == 's') resetToSeed();
			}
		});

		gameboard = new GameBoard((int) (SCREEN_WIDTH / sizeConstant), (int) (SCREEN_HEIGHT / sizeConstant));
		setupFrame();

	}

	/**
	 * Renders the application visible, this will be run from the main method or
	 * another class that opens this application. TODO Create a main method and
	 * place this code in it.
	 */
	public void start() {
		this.setVisible(true);
		/**
		 * The game board can only be set up after the frame has been set visible, so
		 * that it can be passed the width and height of the frame.
		 */
		gameboard.setupPanel(this.getWidth(), this.getHeight());
	}

	/**
	 * This method is accessed by the play button [Menu location: Controls --> Play]
	 * It ends setup and locks the game board from further changes, then starts the
	 * Game of Life.
	 */
	private void play() {
		gameboard.endSetup();
		gameboard.nextStep();
		gameboard.repaint();
		if(gameboard.isEmpty()) reset();
	}
	
	private void reset() {
		gameboard.clear();
		gameboard.repaint();
		gameboard.setupPanel(this.getWidth(), this.getHeight());
	}
	
	private void resetToSeed() {
		gameboard.clear();
		gameboard.setToSeed();
		gameboard.repaint();
		gameboard.setupPanel(this.getWidth(), this.getHeight());
	}
	
	private void resetToRandom() {
		gameboard.clear();
		gameboard.randomizeSeed(randomLifeProb);
		gameboard.setToSeed();
		gameboard.repaint();
		gameboard.setupPanel(this.getWidth(), this.getHeight());
	}

	/**
	 * The preliminary setup of the Application. Creates the menu bar and its items,
	 * and places the game board on the application.
	 */
	private void setupFrame() {

		/**
		 * Creates the menu bar at the top of the screen
		 */
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		/**
		 * Creates the "controls" menu that opens a drop-down containing the "play" and
		 * "reset" options
		 */
		JMenu mnControlsMenu = new JMenu("Controls");
		menuBar.add(mnControlsMenu);

		/**
		 * This menu item functions as a button that runs the play() method TODO Write
		 * the play() method
		 */
		JMenuItem mntmControlsMenuItem_1Play = new JMenuItem("Play");
		mntmControlsMenuItem_1Play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				play();
			}
		});
		mnControlsMenu.add(mntmControlsMenuItem_1Play);
		
		JMenuItem mntmControlsMenuItem_2Reset = new JMenuItem("Reset");
		mntmControlsMenuItem_2Reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reset();
			}
		});
		mnControlsMenu.add(mntmControlsMenuItem_2Reset);
		
		JMenuItem mntmResetToSeed = new JMenuItem("Reset to Seed");
		mntmResetToSeed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetToSeed();
			}
		});
		mnControlsMenu.add(mntmResetToSeed);
		
		JMenuItem mntmResetToRandom = new JMenuItem("Reset to Random");
		mntmResetToRandom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetToRandom();
			}
		});
		mnControlsMenu.add(mntmResetToRandom);

		/**
		 * Sets the rest of the application to contain the gameboard.
		 */
		this.setContentPane(gameboard);

		/**
		 * Sets the frame to fullscreen by default on open.
		 */
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);

	}
}
