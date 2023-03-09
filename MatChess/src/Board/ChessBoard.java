package Board;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

import Players.HumanPlayer;
import Players.MiniMaxPlayer;
import Players.MoveProcessor;
import Players.Player;
import Players.Rules;
import Players.Team;

/**
 * A chessboard is a gameboard used to play chess. It consists of 64 squares, 8
 * rows by 8 columns, on which the chess pieces are placed. It is square in
 * shape and uses two colours of squares, one light and one dark, in a chequered
 * pattern. During play, the board is oriented such that each player's
 * near-right corner square is a light square.
 * 
 * The columns of a chessboard are known as files, the rows are known as ranks,
 * and the lines of adjoining same-coloured squares (each running from one edge
 * of the board to an adjacent edge) are known as diagonals. Each square of the
 * board is named using algebraic, descriptive, or numeric chess notation;
 * algebraic notation is the FIDE standard. In algebraic notation, using White's
 * perspective, files are labeled a through h from left to right, and ranks are
 * labeled 1 through 8 from bottom to top; each square is identified by the file
 * and rank which it occupies. The a- through d-files comprise the queenside,
 * while the e- through h-files comprise the kingside.
 */

public class ChessBoard extends JPanel {

	/**
	 * MiniMax search depth. 1 to 5 .By default is 3.
	 */
	public static int search_depth = 3;

	private static final long serialVersionUID = -4569614389503458330L;

	private PieceSetup PS;

	/**
	 * Use this for "fifty Moves Rule". Free move increment , captive set it to
	 * zero.
	 */
	private int fiftyMoveCounter = 0;

	public int getFiftyMoveCounter() {
		return fiftyMoveCounter;
	}

	/**
	 * Set to zero if we capture some enemy piece.
	 * 
	 * @return
	 */
	public void setFiftyMoveCounter(int moveCounter) {
		this.fiftyMoveCounter = moveCounter;
	}

	private Jail whitePieceJail;

	public Jail getWhitePieceJail() {
		return whitePieceJail;
	}

	private Jail blackPieceJail;

	public Jail getBlackPieceJail() {
		return blackPieceJail;
	}

	/**
	 * This variable contain the all binary positions for 2 dimensional coordinate
	 * system -> positions of 1,2,4,8,16,32,64,128,256... This is needed to
	 * associate the chess board cells positions with the army positions. Example:
	 * The cell coordinate at point of x=6, y=7 respond to 256 in the
	 * "TO_BINARY_POSITIONS " (the position of 9th. element in the 64 bit long
	 * (army)).
	 */
	public final long[][] TO_BINARY_POSITIONS = initBinaryPositions();

	private MoveProcessor moveProcessor;

	public MoveProcessor getMoveProcessor() {
		return moveProcessor;
	}

	public PieceSetup getSetup() {
		return PS;
	}

	private BoardCell[][] boardCells;

	public BoardCell[][] getBoardCells() {
		return boardCells;
	}

	private GridLayout layout = new GridLayout(8, 8, 0, 0);
	private Color cellColor1;
	private Color cellColor2;
	private Player playerA;
	private Player playerB;
	private Rules rules;

	private Enum<Team> turn;

	private ChessWindow chessWindow;

	public ChessWindow getChessWindow() {
		return chessWindow;
	}

	public Enum<Team> getTurn() {
		return turn;
	}

	ChessBoard(Rules rules, Jail whitePieceJail, Jail blackPieceJail, Color cellColor1, Color cellColor2) {

		if (whitePieceJail.getJailTeamColor().equals(Team.WHITE)) {
			this.setWhitePieceJail(whitePieceJail);

		}
		if (blackPieceJail.getJailTeamColor().equals(Team.BLACK)) {
			this.blackPieceJail = blackPieceJail;
		}

		setRules(rules);
		PS = new PieceSetup(rules.new ChessSetup());
		this.cellColor1 = cellColor1;
		this.cellColor2 = cellColor2;
		setLayout(layout);
		this.boardCells = buildBoardCells();
		paintBoardCells();
		addCells(getBoardCells());
		initCells(new Dimension(50, 50));
		// addPieceImages();
		// this.repaint();
		moveProcessor = new MoveProcessor(this);

		setBorder(new MatteBorder(20, 20, 20, 20, new ImageIcon("border.png")));
	}

	private void initCells(Dimension dim) {
		for (int i = 0; i <= 7; i++) {
			for (int j = 0; j <= 7; j++) {
				getBoardCells()[i][j].initCellSize(dim);
			}
		}
	}

	protected void startGameAgainstComputer() {

		resetBoardToStartState();
		removePlayers();
		setPlayerA(new MiniMaxPlayer(this, Team.BLACK));
		setPlayerB(new HumanPlayer(this, Team.WHITE));
		addPlayers();
	}

	protected void startGameTwoPlayers() {
		resetBoardToStartState();
		removePlayers();
		setPlayerA(new HumanPlayer(this, Team.WHITE));
		setPlayerB(new HumanPlayer(this, Team.BLACK));
		addPlayers();
	}

	public void addPlayers() {
		for (byte i = 0; i < getBoardCells().length; i++) {
			for (byte j = 0; j < getBoardCells()[i].length; j++) {
				getBoardCells()[i][j].addMouseListener(getPlayerA());
				getBoardCells()[i][j].addMouseListener(getPlayerB());
			}
		}
	}

	public void removePlayers() {
		for (byte i = 0; i < getBoardCells().length; i++) {
			for (byte j = 0; j < getBoardCells()[i].length; j++) {
				getBoardCells()[i][j].removeMouseListener(getPlayerA());
				getBoardCells()[i][j].removeMouseListener(getPlayerB());
			}
		}
		setPlayerA(null);
		setPlayerB(null);
	}

	/**
	 * After every move traverse all chess set to update the piece icon on the
	 * board.First of all here we clear all old piece icons and after that we add
	 * the all new ones.
	 */
	public void addPieceImages() {

		for (int x = 7; x >= 0; x--) {
			for (int y = 7; y >= 0; y--) {

				// Clear the cell images
				Component[] c = getBoardCells()[x][y].getComponents();
				// Clear images only if there is one.
				if (c.length != 0) {
					getBoardCells()[x][y].clearIcon();
				}
				// Add the cell images
				if (PS.isBBishop(TO_BINARY_POSITIONS[x][y])) {
					getBoardCells()[x][y].setIcon("blackbishop.png");

				} else if (PS.isWBishop(TO_BINARY_POSITIONS[x][y])) {
					getBoardCells()[x][y].setIcon("whitebishop.png");

				} else if (PS.isBKing(TO_BINARY_POSITIONS[x][y])) {
					getBoardCells()[x][y].setIcon("blackking.png");

				} else if (PS.isWKing(TO_BINARY_POSITIONS[x][y])) {
					getBoardCells()[x][y].setIcon("whiteking.png");

				} else if (PS.isBKnignt(TO_BINARY_POSITIONS[x][y])) {
					getBoardCells()[x][y].setIcon("blackknight.png");

				} else if (PS.isWKnignt(TO_BINARY_POSITIONS[x][y])) {
					getBoardCells()[x][y].setIcon("whiteknight.png");

				} else if (PS.isBPawn(TO_BINARY_POSITIONS[x][y])) {
					getBoardCells()[x][y].setIcon("blackpawn.png");

				} else if (PS.isWPawn(TO_BINARY_POSITIONS[x][y])) {
					getBoardCells()[x][y].setIcon("whitepawn.png");

				} else if (PS.isBQueen(TO_BINARY_POSITIONS[x][y])) {
					getBoardCells()[x][y].setIcon("blackqueen.png");

				} else if (PS.isWQueen(TO_BINARY_POSITIONS[x][y])) {
					getBoardCells()[x][y].setIcon("whitequeen.png");

				} else if (PS.isBRook(TO_BINARY_POSITIONS[x][y])) {
					getBoardCells()[x][y].setIcon("blackrook.png");

				} else if (PS.isWRook(TO_BINARY_POSITIONS[x][y])) {
					getBoardCells()[x][y].setIcon("whiterook.png");

				}
			}
		}
		revalidate();
		repaint();

	}

	// See "BINARY_POSITIONS" variable explanation at the top
	private long[][] initBinaryPositions() {

		long[][] BINARY_POSITIONS = new long[8][8];

		long c = 1;
		for (byte x = 7; x >= 0; x--) {
			for (int y = 7; y >= 0; y--) {
				BINARY_POSITIONS[x][y] = c;
				c = (c * 2);
			}
		}
		return BINARY_POSITIONS;
	}

	/**
	 * Build all board fields here.We set a border around every fields.By default
	 * the color of that border is black.
	 * 
	 * @return
	 */
	private BoardCell[][] buildBoardCells() {
		BoardCell[][] cells = new BoardCell[8][8];
		for (byte x = 0; x <= 7; x++) {
			for (int y = 0; y <= 7; y++) {
				// Add 2 dimensional coordinates of x and y as a args.
				// Every time when click on some cell , that cell will
				// correspond with these coordinates to call some functionality.
				cells[x][y] = new BoardCell(x, y);
				Border b = new MatteBorder(1, 1, 1, 1, Color.black);
				cells[x][y].setBorder(b);
			}
		}
		return cells;
	}

	/**
	 * Paint the chess board grid to default state and remove all color points.
	 */
	public void paintBoardCells() {

		for (byte i = 0; i < this.layout.getColumns(); i++) {
			for (byte j = 0; j < this.layout.getRows(); j++) {

				if (((i + j) % 2 == 0)) {
					this.getBoardCells()[i][j].setBackground(cellColor2);
				} else {
					this.getBoardCells()[i][j].setBackground(cellColor1);
				}
				this.getBoardCells()[i][j].setPointColor(new Color(0, 0, 0, 0));
			}
		}
		this.repaint();
	}

	/**
	 * Add all cells to the chess board container. Chess board container is a 8x8
	 * grid.At least one cell must contain the image for proper visualization in
	 * that grid.
	 */
	private void addCells(BoardCell[][] boardCells) {
		for (byte i = 0; i < boardCells.length; i++) {
			for (byte j = 0; j < boardCells[i].length; j++) {
				this.add(boardCells[i][j]);
			}
		}

	}

	// Swap the players turn every time when a move is initiated.
	public void swapPlayers() {
		if (turn.equals(Team.WHITE)) {
			turn = Team.BLACK;
		} else {
			turn = Team.WHITE;
		}

	}

	/**
	 * Method to get the chess board rating by calculating the rating of all alive
	 * pieces by values programmed for every piece(rating) in advance.Need some
	 * improvement here!!!
	 */
	public int getBoardRating() {
		int rating = 0;

		for (int i = 1; i <= 64; i++) {
			if (PS.getPiece(i, PS.BR) == 1) {
				rating = rating + 500;
			}
		}
		for (int i = 1; i <= 64; i++) {
			if (PS.getPiece(i, PS.BB) == 1) {
				rating = rating + 300;
			}
		}
		for (int i = 1; i <= 64; i++) {
			if (PS.getPiece(i, PS.BN) == 1) {
				rating = rating + 300;
			}
		}

		for (int i = 1; i <= 64; i++) {
			if (PS.getPiece(i, PS.BQ) == 1) {
				rating = rating + 900;
			}
		}
		for (int i = 1; i <= 64; i++) {
			if (PS.getPiece(i, PS.BK) == 1) {
				rating = rating + 10000;
			}
		}
		for (int i = 1; i <= 64; i++) {
			if (PS.getPiece(i, PS.BP) == 1) {
				rating = rating + 100;
			}
		}

		for (int i = 1; i <= 64; i++) {
			if (PS.getPiece(i, PS.WR) == 1) {
				rating = rating - 500;
			}
		}
		for (int i = 1; i <= 64; i++) {
			if (PS.getPiece(i, PS.WB) == 1) {
				rating = rating - 300;
			}
		}
		for (int i = 1; i <= 64; i++) {
			if (PS.getPiece(i, PS.WN) == 1) {
				rating = rating - 300;
			}
		}
		for (int i = 1; i <= 64; i++) {
			if (PS.getPiece(i, PS.WQ) == 1) {
				rating = rating - 900;
			}
		}
		for (int i = 1; i <= 64; i++) {
			if (PS.getPiece(i, PS.WK) == 1) {
				rating = rating - 10000;
			}
		}
		for (int i = 1; i <= 64; i++) {
			if (PS.getPiece(i, PS.WP) == 1) {
				rating = rating - 100;
			}
		}
		return rating;
	}

	public boolean isAFriendAt(long bPos) {
		if (((turn.equals(Team.BLACK)) && (PS.isBBishop(bPos) || PS.isBKnignt(bPos) || PS.isBQueen(bPos)
				|| PS.isBRook(bPos) || PS.isBKing(bPos) || PS.isBPawn(bPos)))
				|| ((turn.equals(Team.WHITE)) && (PS.isWBishop(bPos) || PS.isWKnignt(bPos) || PS.isWQueen(bPos)
						|| PS.isWRook(bPos) || PS.isWKing(bPos) || PS.isWPawn(bPos)))) {
			return true;
		}
		return false;
	}

	public boolean isAnEnemyAt(long bPos) {
		if ((turn.equals(Team.WHITE) && ((PS.isBBishop(bPos) || PS.isBKnignt(bPos) || PS.isBQueen(bPos)
				|| PS.isBRook(bPos) || PS.isBKing(bPos) || PS.isBPawn(bPos))))
				|| ((turn.equals(Team.BLACK)) && ((PS.isWBishop(bPos) || PS.isWKnignt(bPos) || PS.isWQueen(bPos)
						|| PS.isWRook(bPos) || PS.isWKing(bPos) || PS.isWPawn(bPos))))) {
			return true;
		}
		return false;
	}

	public void setWhitePieceJail(Jail whitePieceJail) {
		this.whitePieceJail = whitePieceJail;
	}

	public void setPlayerA(Player playerA) {
		this.playerA = playerA;
	}

	public Player getPlayerA() {
		return playerA;
	}

	public void setPlayerB(Player playerB) {
		this.playerB = playerB;
	}

	public Player getPlayerB() {
		return playerB;
	}

	public void setRules(Rules rules) {
		this.rules = rules;
	}

	public Rules getRules() {
		return rules;
	}

	/**
	 * Here prepare the board for new game.Reset the all states.
	 */
	public void resetBoardToStartState() {

		turn = getRules().getFirstTurn();
		getRules().new ChessSetup().set(PS);
		PS.setRightWhiteRookMoved(false);
		PS.setLeftWhiteRookMoved(false);
		PS.setWhiteKingMoved(false);
		PS.setRightBlackRookMoved(false);
		PS.setLeftBlackRookMoved(false);
		PS.setBlackKingMoved(false);
		setFiftyMoveCounter(0);
		addPieceImages();
		paintBoardCells();
		whitePieceJail.update(PS);
		blackPieceJail.update(PS);

	}

	public void setWindow(ChessWindow chessWindow) {
		this.chessWindow = chessWindow;

	}
}
