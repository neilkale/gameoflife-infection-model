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
import javax.swing.JScrollPane;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class Application extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final double SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static final double SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	
	private double randomLifeProb = 0.5;
	private int sizeConstant = 18;

	GameBoard gameboard;

	public Application() {		
		/**
		 * This code sets up the GridBag layout for the counter text.
		 */

		gameboard = new GameBoard((int) (SCREEN_WIDTH / sizeConstant), (int) (SCREEN_HEIGHT / sizeConstant));
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyChar() == 'p') play();
				if(e.getKeyChar() == 'r') reset();
				if(e.getKeyChar() == 's') resetToSeed();
			}
		});

		
		GridBagLayout gbl_gameboard = new GridBagLayout();
		gbl_gameboard.columnWidths = new int[]{0, 0};
		gbl_gameboard.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_gameboard.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_gameboard.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gameboard.setLayout(gbl_gameboard);
		
		JTextPane txtpnWeeks = new JTextPane();
		txtpnWeeks.setText("Day:");
		txtpnWeeks.setFont(new Font("Consolas", Font.PLAIN, 48));
		txtpnWeeks.setEditable(false);
		GridBagConstraints gbc_txtpnWeeks = new GridBagConstraints();
		gbc_txtpnWeeks.anchor = GridBagConstraints.NORTHEAST;
		gbc_txtpnWeeks.insets = new Insets(0, 0, 5, 0);
		gbc_txtpnWeeks.gridx = 0;
		gbc_txtpnWeeks.gridy = 0;
		gameboard.add(txtpnWeeks, gbc_txtpnWeeks);
		
		JTextPane txtpnCured = new JTextPane();
		txtpnCured.setEditable(false);
		txtpnCured.setFont(new Font("Consolas", Font.PLAIN, 48));
		txtpnCured.setText("Cured:");
		GridBagConstraints gbc_txtpnCured = new GridBagConstraints();
		gbc_txtpnCured.insets = new Insets(0, 0, 5, 0);
		gbc_txtpnCured.anchor = GridBagConstraints.NORTHEAST;
		gbc_txtpnCured.gridx = 0;
		gbc_txtpnCured.gridy = 2;
		gameboard.add(txtpnCured, gbc_txtpnCured);
		
		JTextPane txtpnHealthy = new JTextPane();
		txtpnHealthy.setEditable(false);
		txtpnHealthy.setText("Healthy:");
		txtpnHealthy.setFont(new Font("Consolas", Font.PLAIN, 48));
		GridBagConstraints gbc_txtpnHealthy = new GridBagConstraints();
		gbc_txtpnHealthy.anchor = GridBagConstraints.NORTHEAST;
		gbc_txtpnHealthy.insets = new Insets(0, 0, 5, 0);
		gbc_txtpnHealthy.gridx = 0;
		gbc_txtpnHealthy.gridy = 3;
		gameboard.add(txtpnHealthy, gbc_txtpnHealthy);
		
		JTextPane txtpnInfected = new JTextPane();
		txtpnInfected.setEditable(false);
		txtpnInfected.setText("Infected:");
		txtpnInfected.setFont(new Font("Consolas", Font.PLAIN, 48));
		GridBagConstraints gbc_txtpnInfected = new GridBagConstraints();
		gbc_txtpnInfected.anchor = GridBagConstraints.NORTHEAST;
		gbc_txtpnInfected.insets = new Insets(0, 0, 5, 0);
		gbc_txtpnInfected.gridx = 0;
		gbc_txtpnInfected.gridy = 4;
		gameboard.add(txtpnInfected, gbc_txtpnInfected);
		
		JTextPane txtpnDead = new JTextPane();
		txtpnDead.setEditable(false);
		txtpnDead.setText("Dead:");
		txtpnDead.setFont(new Font("Consolas", Font.PLAIN, 48));
		GridBagConstraints gbc_txtpnDead = new GridBagConstraints();
		gbc_txtpnDead.insets = new Insets(0, 0, 5, 0);
		gbc_txtpnDead.anchor = GridBagConstraints.NORTHEAST;
		gbc_txtpnDead.gridx = 0;
		gbc_txtpnDead.gridy = 5;
		gameboard.add(txtpnDead, gbc_txtpnDead);
		setupFrame();
		
		this.setFocusable(true);
		this.requestFocusInWindow();


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
		updateCounters();
	}

	/**
	 * This method is accessed by the play button [Menu location: Controls --> Play]
	 * It ends setup and locks the game board from further changes, then starts the
	 * Game of Life.
	 */
	private void play() {
		gameboard.addWeeks(1);
		gameboard.endSetup();
		gameboard.nextStep();
		gameboard.repaint();
		if(gameboard.isEmpty()) reset();
		updateCounters();
	}
	
	private void reset() {
		gameboard.clear();
		gameboard.repaint();
		gameboard.setupPanel(this.getWidth(), this.getHeight());
		updateCounters();
	}
	
	private void resetToSeed() {
		gameboard.clear();
		gameboard.repaint();
		gameboard.setupPanel(this.getWidth(), this.getHeight());
		gameboard.setToSeed();
		updateCounters();
	}
	
	private void resetToRandom() {
		gameboard.clear();
		gameboard.repaint();
		gameboard.setupPanel(this.getWidth(), this.getHeight());
		gameboard.randomizeSeed(randomLifeProb);
		gameboard.setToSeed();
		updateCounters();
	}
	
	private void updateCounters() {
		Component[] counters = gameboard.getComponents();
		
		for(Component c : counters) {
			if(c instanceof JTextPane) {
				String s = ((JTextPane) c).getText();
				s = s.trim();
				while(s.charAt(s.length()-1) != ':') s = s.substring(0, s.length()-1);
				if(s.equals("Day:")) s += gameboard.numDays;
				if(s.equals("Cured:")) s += gameboard.getNumCured();
				if(s.equals("Healthy:")) s += gameboard.getNumHealthy();
				if(s.equals("Infected:")) s += gameboard.getNumInfected();
				if(s.equals("Dead:")) s += gameboard.getNumDead();
				((JTextPane) c).setText(s);
			}
		}
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
