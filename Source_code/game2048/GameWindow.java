/**
 * 
 */
package game2048;

/**
 * @author Rachitha Pittala
 *
 */

import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;

import ai2048.SearchTree;

public class GameWindow extends JFrame {

	private JMenuBar menuBar;
	private JMenuItem newGame;
	private JMenuItem startAI;
	private JMenuItem pauseAI;
	private JTextField scoreValue;
	private JLabel[][] tiles;
	private GameBoard game_board;
	private int moves;
	private boolean autoPlayStatus;

	Font default_style = new Font(Font.SERIF, Font.BOLD, 18);
	Font tile_style = new Font(Font.SANS_SERIF, Font.BOLD, 30);

	public GameWindow() {
		game_board = new GameBoard();
		moves = 0;
		autoPlayStatus = false;

		menuBar = new JMenuBar();
		buildStartMenu();
		buildHelpMenu();
		startGame();
	}

	private void buildStartMenu() {
		final JMenu startMenu = new JMenu("Start");

		newGame = new JMenuItem("New Game");
		newGame.addActionListener((ActionEvent event) -> newGame());
		startMenu.add(newGame);

		final JMenu subMenuAI = new JMenu("AI");
		
		startAI = new JMenuItem("Start");
		startAI.addActionListener((ActionEvent event) -> autoPlay());
		subMenuAI.add(startAI);
		
		pauseAI = new JMenuItem("Pause");
		pauseAI.setEnabled(false);
		pauseAI.addActionListener((ActionEvent event) -> changeAutoPlayStatus());
		subMenuAI.add(pauseAI);

		startMenu.add(subMenuAI);

		final JMenuItem exitMenu = new JMenuItem("Exit");
		exitMenu.addActionListener((ActionEvent event) -> System.exit(0));
		startMenu.add(exitMenu);

		menuBar.add(startMenu);
	}

	private void buildHelpMenu() {
		final JMenu helpMenu = new JMenu("Help");

		final JMenuItem howtoplayMenu = new JMenuItem("How to Play?");
		howtoplayMenu.addActionListener((ActionEvent event) -> JOptionPane.showMessageDialog(null, "Use arrow keys to move tiles \n (right, left, up, down) ", null, 1));
		helpMenu.add(howtoplayMenu);
		
		final JMenuItem aboutMenu = new JMenuItem("About");
		aboutMenu.addActionListener((ActionEvent event) -> JOptionPane.showMessageDialog(null, "2048 Game \n Developed by: Rachitha Pittala", "About", 1));
		helpMenu.add(aboutMenu);

		menuBar.add(helpMenu);
	}

	private void startGame() {
		final JPanel scorePanel = new JPanel();
		scorePanel.setBounds(new Rectangle(18, 20, 460, 40));
		getContentPane().add(scorePanel);
		scorePanel.setLayout(null);

		final JLabel scoreLabel = new JLabel("Score");
		scoreLabel.setFont(default_style);
		scoreLabel.setBounds(new Rectangle(240, 5, 70, 30));
		scorePanel.add(scoreLabel);

		scoreValue = new JTextField("0");
		scoreValue.setBackground(Color.white);
		scoreValue.setFont(default_style);
		scoreValue.setBounds(new Rectangle(300, 5, 150, 30));
		scoreValue.setEditable(false);
		scoreValue.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (!autoPlayStatus)
					moveKey(e);
			}
		});
		scorePanel.add(scoreValue);

		final JPanel boardPanel = new JPanel();
		boardPanel.setBounds(new Rectangle(18, 80, 460, 500));
		getContentPane().add(boardPanel);
		boardPanel.setLayout(null);

		tiles = new JLabel[GameBoard.boardDimension][GameBoard.boardDimension];
		int[][] board = game_board.getBoard();
		for (int rowNumber = 0; rowNumber < GameBoard.boardDimension; rowNumber++) {
			for (int columnNumber = 0; columnNumber < GameBoard.boardDimension; columnNumber++) {
				tiles[rowNumber][columnNumber] = new JLabel();
				tiles[rowNumber][columnNumber].setFont(tile_style);
				tiles[rowNumber][columnNumber].setHorizontalAlignment(SwingConstants.CENTER);
				String tile = tileToString(board[rowNumber][columnNumber]);
				tiles[rowNumber][columnNumber].setText(tile);
				tiles[rowNumber][columnNumber].setBounds(120 * columnNumber, 120 * rowNumber, 100, 100);
				tiles[rowNumber][columnNumber].setForeground(Color.black);
				tiles[rowNumber][columnNumber].setBackground(Constants.COLORS.get(tile)); 
				tiles[rowNumber][columnNumber].setOpaque(true);
				tiles[rowNumber][columnNumber].setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.lightGray));
				boardPanel.add(tiles[rowNumber][columnNumber]);
			}
		}

		getContentPane().setLayout(null);
		setBounds(new Rectangle(500, 50, 500, 620));
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setTitle("2048");
		setJMenuBar(menuBar);
		addWindowListener(new WindowListener() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit();
			}
			@Override
			public void windowOpened(WindowEvent e) {
				}
			@Override
			public void windowClosed(WindowEvent e) {
			}
			@Override
			public void windowIconified(WindowEvent e) {
			}
			@Override
			public void windowDeiconified(WindowEvent e) {
			}
			@Override
			public void windowActivated(WindowEvent e) {
			}
			@Override
			public void windowDeactivated(WindowEvent e) {
			}
		});
	}

	public static String tileToString(int number) {
		if (number == 0)
			return "";
		return String.valueOf(number);
	}

	private void updateTiles(GameBoard newBoard) {
		game_board.replaceBoard(newBoard);
		int[][] board = game_board.getBoard();
		for (int rowNumber = 0; rowNumber < GameBoard.boardDimension; rowNumber++) {
			for (int columnNumber = 0; columnNumber < GameBoard.boardDimension; columnNumber++) {
				String tile = tileToString(board[rowNumber][columnNumber]);
				tiles[rowNumber][columnNumber].setText(tile);
				tiles[rowNumber][columnNumber].setBackground(Constants.COLORS.get(tile));
			}
		}
		scoreValue.setText(String.valueOf(game_board.getScores()));
		moves++;

		if (game_board.isGameOver()) {
			int message = JOptionPane.showConfirmDialog(null, "Oops!! You lost the game. \n Do you want to try again?", "Game Over", 0, 1);
			if(message == JOptionPane.YES_OPTION) {
				newGame();
			}
			else
				System.exit(0);
		}

		if(game_board.gameWon()) {
			int message = JOptionPane.showConfirmDialog(null,  "You Won!! \n Do you want to play again?", "2048", 0, 1);
			if(message == JOptionPane.YES_OPTION) {
				newGame();
			}
			else
				System.exit(0);
		}
	}
	
	private void newGame() {
		game_board = new GameBoard();
		updateTiles(game_board);
		moves = 0;
		scoreValue.setEnabled(true);
		scoreValue.requestFocus();
	}

	private void changeAutoPlayStatus() {
		autoPlayStatus = !autoPlayStatus;
		newGame.setEnabled(!autoPlayStatus);
		pauseAI.setEnabled(autoPlayStatus);
		startAI.setEnabled(!autoPlayStatus);
	}

	private void autoPlay() {
		if (!game_board.isGameOver()) {
			Thread t = new Thread(() -> {
				try {
					int searchTreeDepth = 6;
					while (autoPlayStatus) {
						SearchTree search_tree = new SearchTree(game_board, searchTreeDepth);
						GameBoard temp_board = search_tree.findNextStep(moves);
						if (temp_board != null)
							updateTiles(temp_board);
						Thread.sleep(500);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
			synchronized (this) {
				changeAutoPlayStatus();
				t.start();
			}
		} else
			JOptionPane.showMessageDialog(null, "Game over!! You cannot start AI until you create a new game.", "Warning", 2);
	}


	private void exit() {
		int message = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Exit", 2, 3);
		if(message == JOptionPane.OK_OPTION) {
			System.exit(0);
		}
	}


	private void moveKey(final KeyEvent event) {
		GameBoard temp_board = null;
		int keyCode = event.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_LEFT:
			temp_board = game_board.move(Constants.Directions.LEFT);
			break;
		case KeyEvent.VK_RIGHT:
			temp_board = game_board.move(Constants.Directions.RIGHT);
			break;
		case KeyEvent.VK_UP:
			temp_board = game_board.move(Constants.Directions.UP);
			break;
		case KeyEvent.VK_DOWN:
			temp_board = game_board.move(Constants.Directions.DOWN);
			break;
		default:
			break;
		}
		if (temp_board != null)
			updateTiles(temp_board);
	}

	public static void main(String[] args) {
        GameWindow game = new GameWindow();
        game.setVisible(true);
   }
}
