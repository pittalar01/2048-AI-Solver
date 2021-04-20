/**
 * 
 */
package game2048;

/**
 * @author Rachitha Pittala
 *
 */
import java.util.Arrays;
import java.util.Random;


public class GameBoard implements Cloneable {
	private static final Random random_number = new Random();
	private int[][] board;
	private int score;
	public static final int boardDimension = 4;
	private static final int target = 2048;


	public GameBoard() {
		score = 0;
		board = new int[boardDimension][boardDimension];
		addRandomTile();
		addRandomTile();
	}

	private int getEmptyTilesCount() {
		int emptyTilesCount = 0;
		for (int rowNumber = 0; rowNumber < boardDimension; rowNumber++)
			for (int columnNumber = 0; columnNumber < boardDimension; columnNumber++)
				if (board[rowNumber][columnNumber] == 0)
					emptyTilesCount++;
		return emptyTilesCount;
	}

	private void addRandomTile() {
		if (getEmptyTilesCount() > 0) {
			while (true) {
				int rowNumber = random_number.nextInt(boardDimension);
				int columnNumber = random_number.nextInt(boardDimension);
				if (board[rowNumber][columnNumber] == 0) {
					board[rowNumber][columnNumber] = Math.random() > 0.1 ? 2 : 4;
					break;
				}
			}
		}
	}

	public GameBoard cloneBoard() {
		GameBoard clone_board = null;
		try {
			super.clone();
			clone_board = new GameBoard();
			clone_board.score = score;
			for (int rowNumber = 0; rowNumber < boardDimension; rowNumber++)
				System.arraycopy(board[rowNumber], 0, clone_board.board[rowNumber], 0, board.length);
		} 
		catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return clone_board;
	}


	public void replaceBoard(GameBoard otherBoard) {
		for (int rowNumber = 0; rowNumber < boardDimension; rowNumber++)
			System.arraycopy(otherBoard.board[rowNumber], 0, board[rowNumber], 0, otherBoard.board.length);
		score = otherBoard.score;
	}


	private boolean isValidMove(GameBoard moved_board) {
		return !Arrays.deepEquals(board, moved_board.board) && moved_board != null;
	}

	public static void swap(int[] array, int x, int y) {
		int tempValue = array[x];
		array[x] = array[y];
		array[y] = tempValue;
	}


	public static void swap(int[][] array, int columnNumber, int x, int y) {
		int tempValue = array[x][columnNumber];
		array[x][columnNumber] = array[y][columnNumber];
		array[y][columnNumber] = tempValue;
	}


	private GameBoard moveLeft() {
		GameBoard new_board = cloneBoard();
		for (int rowNumber = 0; rowNumber < boardDimension; rowNumber++) {
			for (int columnNumber = 0; columnNumber < boardDimension - 1; columnNumber++) {
				int x = new_board.board[rowNumber][columnNumber];
				int y = columnNumber + 1;
				while (x != 0 && y <= boardDimension - 1) {
					if (new_board.board[rowNumber][y] != 0 && new_board.board[rowNumber][y] != x )
						break;
					else if (new_board.board[rowNumber][y] == x) {
						new_board.board[rowNumber][columnNumber] *= 2;
						new_board.board[rowNumber][y] = 0;
						new_board.score += (x * 2);
						break;
					} 
					else
						y++;
				}
			}
			for (int columnNumber = 1; columnNumber < boardDimension; columnNumber++) {
				if (new_board.board[rowNumber][columnNumber] != 0) {
					int x = columnNumber;
					while (x > 0 && new_board.board[rowNumber][x - 1] == 0) {
						swap(new_board.board[rowNumber], x - 1, x);
						x--;
					}
				}
			}
		}
		if (isValidMove(new_board))
			new_board.addRandomTile();
		return new_board;
	}


	private GameBoard moveRight() {
		GameBoard new_board = cloneBoard();
		for (int rowNumber = 0; rowNumber < boardDimension; rowNumber++) {
			for (int columnNumber = boardDimension - 1; columnNumber > 0; columnNumber--) {
				int x = new_board.board[rowNumber][columnNumber];
				int y = columnNumber - 1;
				while (x != 0 && y >= 0) {
					if (new_board.board[rowNumber][y] != 0 && new_board.board[rowNumber][y] != x )
						break;
					else if (new_board.board[rowNumber][y] == x) {
						new_board.board[rowNumber][columnNumber] *= 2;
						new_board.board[rowNumber][y] = 0;
						new_board.score += (x * 2);
						break;
					} 
					else
						y--;
				}
			}
			for (int columnNumber = boardDimension - 2; columnNumber >= 0; columnNumber--) {
				if (new_board.board[rowNumber][columnNumber] != 0) {
					int x = columnNumber;
					while (x < boardDimension - 1 && new_board.board[rowNumber][x + 1] == 0) {
						swap(new_board.board[rowNumber], x, x + 1);
						x++;
					}
				}
			}
		}
		if (isValidMove(new_board))
			new_board.addRandomTile();
		return new_board;
	}


	private GameBoard moveUp() {
		GameBoard new_board = cloneBoard();
		for (int columnNumber = 0; columnNumber < boardDimension; columnNumber++) {
			for (int rowNumber = 0; rowNumber < boardDimension - 1; rowNumber++) {
				int x = new_board.board[rowNumber][columnNumber];
				int y = rowNumber + 1;
				while (x != 0 && y <= boardDimension - 1 ) {
					if (new_board.board[y][columnNumber] != 0 && new_board.board[y][columnNumber] != x)
						break;
					else if (new_board.board[y][columnNumber] == x) {
						new_board.board[rowNumber][columnNumber] *= 2;
						new_board.board[y][columnNumber] = 0;
						new_board.score += (x * 2);
						break;
					} 
					else
						y++;
				}
			}
			for (int rowNumber = 1; rowNumber < boardDimension; rowNumber++) {
				if (new_board.board[rowNumber][columnNumber] != 0) {
					int x = rowNumber;
					while (x > 0 && new_board.board[x - 1][columnNumber] == 0 ) {
						swap(new_board.board, columnNumber, x - 1, x);
						x--;
					}
				}
			}
		}
		if (isValidMove(new_board))
			new_board.addRandomTile();

		return new_board;
	}


	private GameBoard moveDown() {
		GameBoard new_board = cloneBoard();
		for (int columnNumber = 0; columnNumber < boardDimension; columnNumber++) {
			for (int rowNumber = boardDimension - 1; rowNumber > 0; rowNumber--) {
				int x = new_board.board[rowNumber][columnNumber];
				int y = rowNumber - 1;
				while (x != 0 && y >= 0) {
					if (new_board.board[y][columnNumber] != 0 && new_board.board[y][columnNumber] != x)
						break;
					else if (new_board.board[y][columnNumber] == x) {
						new_board.board[rowNumber][columnNumber] *= 2;
						new_board.board[y][columnNumber] = 0;
						new_board.score += (x * 2);
						break;
					} 
					else
						y--;
				}
			}
			for (int rowNumber = boardDimension - 2; rowNumber >= 0; rowNumber--) {
				if (new_board.board[rowNumber][columnNumber] != 0) {
					int x = rowNumber;
					while ( x < boardDimension - 1 && new_board.board[x + 1][columnNumber] == 0 ) {
						 swap(new_board.board, columnNumber, x, x + 1);
						x++;
					}
				}
			}
		}
		if (isValidMove(new_board))
			new_board.addRandomTile();
		return new_board;
	}


	public GameBoard move(Constants.Directions directionName) {
		GameBoard moved_board;
		switch (directionName) {
		case UP:
			moved_board = moveUp();
			break;
		case RIGHT:
			moved_board = moveRight();
			break;
		case LEFT:
			moved_board = moveLeft();
			break;
		case DOWN:
			moved_board = moveDown();
			break;
		default:
			moved_board = null;
		}
		return (isValidMove(moved_board)) ? moved_board : null;
	}


	public boolean isGameOver() {
		for (int rowNumber = 0; rowNumber < boardDimension; rowNumber++)
			for (int columnNumber = 0; columnNumber < boardDimension; columnNumber++)
				if (board[rowNumber][columnNumber] == 0  
				|| (rowNumber > 0 && board[rowNumber - 1][columnNumber] == board[rowNumber][columnNumber]) 
				|| (columnNumber < boardDimension - 1 && board[rowNumber][columnNumber + 1] == board[rowNumber][columnNumber])
				|| (columnNumber > 0 && board[rowNumber][columnNumber - 1] == board[rowNumber][columnNumber]) 
				|| (rowNumber < boardDimension - 1 && board[rowNumber + 1][columnNumber] == board[rowNumber][columnNumber]))
					return false;
		return true;
	}

	public int getMaximumTile() {
		int maximum_tile = board[0][0];
		for (int rowNumber = 0; rowNumber < boardDimension; rowNumber++)
			for (int columnNumber = 0; columnNumber < boardDimension; columnNumber++)
				if (board[rowNumber][columnNumber] > maximum_tile)
					maximum_tile = board[rowNumber][columnNumber];
		return maximum_tile;
	}


	public boolean gameWon(){
		boolean isGameWon = false;
		int tileValue = getMaximumTile();
		if (tileValue == target) {
			isGameWon = true;
		}
		return isGameWon;
	}

	public int getScores() {
		return score;
	}


	private int getTileWeight(int rowNumber, int columnNumber) { 
		int tileWeight = 0; 
		if ((rowNumber == 0 && columnNumber == boardDimension - 1
				|| rowNumber == 0 && columnNumber == 0) 
				|| (rowNumber == boardDimension - 1 && columnNumber == 0) 
				|| (rowNumber == boardDimension - 1 && columnNumber == boardDimension - 1)) 
			tileWeight += (board[rowNumber][columnNumber] * Constants.Weights.CORNER.getWeight()); 

		else if ((rowNumber == 0 &&	columnNumber > 0 && columnNumber < boardDimension - 1) 
				|| (rowNumber == boardDimension - 1 && columnNumber > 0 && columnNumber < boardDimension - 1)
				|| (rowNumber > 0 && rowNumber < boardDimension - 1 && columnNumber == 0) 
				|| (rowNumber > 0 && rowNumber < boardDimension - 1 && columnNumber == boardDimension - 1)) 
			tileWeight += (board[rowNumber][columnNumber] * Constants.Weights.SIDE.getWeight()); 
		else 
			tileWeight += (board[rowNumber][columnNumber] * Constants.Weights.OTHER.getWeight());
		return tileWeight; 
	}


	public double estimate(int moves) {
		double estimationValue = 0;
		estimationValue += (Math.pow(getScores(), 2) + Math.pow(getMaximumTile(), 2) + getEmptyTilesCount() * Constants.Weights.EMPTY.getWeight());
		for (int rowNumber = 0; rowNumber < boardDimension; rowNumber++)
			for (int columnNumber = 0; columnNumber < boardDimension; columnNumber++)
				estimationValue += getTileWeight(rowNumber, columnNumber);
		estimationValue = (moves != 0) ? (estimationValue / moves) : 0;
		return estimationValue;
	}


	public int[][] getBoard() {
		return board;
	}

}



