package Players;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;

import Board.ChessBoard;
import Board.PieceSetup;
import Players.Move.MOVE_PURPOSE;

public class Rules {

	public enum MOVE_DIRECTION {
		UP_TO_LEFT, UP_TO_RIGHT, DOWN_TO_LEFT, DOWN_TO_RIGHT, UP, DOWN, LEFT, RIGHT
	}

	public enum MOVES_TYPE {
		CHECK_PREVENTED, ALL
	}

	private Enum<Team> firstTurn = Team.WHITE;

	public Rules() {

	}

	public class ChessSetup {

		/*
		 * Here declare the all 12 boards.Because of name conflict of the Knight
		 * and the King we use BN and WN for the names of black and white
		 * knight. We use this variables to set the chess board start state and
		 * further as a mask for piece recognize.
		 */
		public final long BR = -9151314442816847872L;
		public final long BN = 4755801206503243776L;// black knight
		public final long BB = 2594073385365405696L;
		public final long BQ = 1152921504606846976L;
		public final long BK = 576460752303423488L;
		public final long BP = 71776119061217280L;

		public final long WR = 129L;
		public final long WN = 66L;// white knight
		public final long WB = 36L;
		public final long WQ = 16L;
		public final long WK = 8L;
		public final long WP = 65280L;

		/*
		 * For the first pawn move options we use a binary mask to keep the
		 * information for that which pawn has right to jump over first field to
		 * the third - first move or any other moves after the very first move.
		 * Example: 0 = not jump, 1 = jump
		 */
		public final long WP_PAWN_FIRST_MOVE_MASK = 65280L;
		public final long BP_PAWN_FIRST_MOVE_MASK = 71776119061217280L;

		public final long WP_ENPASSANT_MOVE_MASK = 0L;
		public final long BP_ENPASSANT_MOVE_MASK = 0L;

		public final boolean IS_WHITE_KING_MOVED = false;
		public final boolean IS_RIGHT_WHITE_ROOK_MOVED = false;

		public long getArmy() {

			long army = 0L;

			army = army | BR;
			army = army | BN;
			army = army | BB;
			army = army | BQ;
			army = army | BK;
			army = army | BP;

			army = army | WR;
			army = army | WN;
			army = army | WB;
			army = army | WQ;
			army = army | WK;
			army = army | WP;
			return army;

		}

		public void set(PieceSetup setup) {

			setup.BR = BR;
			setup.BN = BN;// black knight
			setup.BB = BB;
			setup.BQ = BQ;
			setup.BK = BK;
			setup.BP = BP;
			setup.WR = WR;
			setup.WN = WN;// white knight
			setup.WB = WB;
			setup.WQ = WQ;
			setup.WK = WK;
			setup.WP = WP;

			setup.setArmy(getArmy());

			setup.wp_enPassantMoveMask = WP_ENPASSANT_MOVE_MASK;
			setup.bp_enPassantMoveMask = BP_ENPASSANT_MOVE_MASK;

			setup.WP_FirstMoveMask = WP_PAWN_FIRST_MOVE_MASK;
			setup.BP_FirstMoveMask = BP_PAWN_FIRST_MOVE_MASK;
			setup.setWhiteKingMoved(IS_WHITE_KING_MOVED);
			setup.setRightWhiteRookMoved(IS_RIGHT_WHITE_ROOK_MOVED);

		}
	}

	// Fifty-move rule: If during the previous 50 moves no pawn has been moved
	// and no capture has been made, either player can claim a draw. The
	// addition of the seventy-five-move rule in 2014 requires the arbiter to
	// intervene and immediately declare the game drawn after 75 moves without a
	// pawn move or capture, without requiring a claim by either player. There
	// are several known endgames where it is possible to force a mate but it
	// requires more than 50 moves before a pawn move or capture is made;
	// examples include some endgames with two knights against a pawn and some
	// pawnless endgames such as queen against two bishops. Historically, FIDE
	// has sometimes revised the fifty-move rule to make exceptions for these
	// endgames, but these have since been repealed. Some correspondence chess
	// organizations do not enforce the fifty-move rule.[note 1]
	public static void updateFiftyMovesRuleCounter(ChessBoard board,
			boolean isCapture) {

		if (isCapture) {
			board.setFiftyMoveCounter(0);
		} else {
			board.setFiftyMoveCounter(board.getFiftyMoveCounter() + 1);
		}

		if (board.getFiftyMoveCounter() == 50) {
			JOptionPane.showMessageDialog(null,
					"The game is in drow(50 moves rule)", "Draw",
					JOptionPane.INFORMATION_MESSAGE);
			board.removePlayers();
			// board.resetBoardToStartState();
		}
	}

	// A game of chess ends when a player puts the opposing player's king in a
	// position that cannot avoid capture (checkmate). A game can also be won or
	// lost through concession. A chess match can also end in a draw. This can
	// happen through stalemate, mutual consent, checkmate being impossible to
	// achieve, and in other ways.
	// The 50 move rule means that if both players make 50 moves without
	// captures or pawn moves then the game is automatically a draw. This
	// usually happens in an end game if you had just a king left or a king and
	// a
	// few pieces and the opponent cannot checkmate you. So start counting to 50

	// My enemy is in checkmate if he have no legal
	// moves.
	public static boolean isInCheckmate(ChessBoard board) {

		// When we come here the players are already swapped in the
		// "doMove" method and we will get the all "enemy" legal moves list
		// only.

		List<Move> moves = new LinkedList<Move>();

		for (int x = 0; x <= 7; x++) {
			for (int y = 0; y <= 7; y++) {

				board.getMoveProcessor().getMovesForOnePiece(moves, x, y,
						false, MOVES_TYPE.CHECK_PREVENTED);

			}
		}

		if (moves.size() == 0) {
			if (board.getTurn().equals(Team.WHITE)) {
				JOptionPane.showMessageDialog(null,
						"The winner is BLACK player.", "Winner",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null,
						"The winner is WHITE player.", "Winner",
						JOptionPane.INFORMATION_MESSAGE);
			}

			board.removePlayers();
			return true;
		}

		return false;

	}

	public Enum<Team> getFirstTurn() {
		// TODO Auto-generated method stub
		return firstTurn;
	}

	public static class PawnRules extends Rules {

		static PawnRules rules = new PawnRules();

		private boolean isPossibleEnPassantMoveForBlack(PieceSetup set, long to) {

			if (set.isDestFieldEmpty(to)
					&& set.isEnabledEnPassantPosition(to, Team.WHITE)) {
				return true;
			}
			return false;
		}

		private boolean isPossibleEnPassantMoveForWhite(PieceSetup set, long to) {

			if (set.isDestFieldEmpty(to)
					&& set.isEnabledEnPassantPosition(to, Team.BLACK)) {
				return true;
			}
			return false;
		}

		public static void computeWhiteMoves(MoveProcessor moveProcessor,
				List<Move> moves, long pos, int startX, int startY,
				boolean isPaint, MOVES_TYPE compType) {

			int r = 1;

			if (moveProcessor.getArmy().getPiece(
					moveProcessor.toLinearPosition(startX, startY),
					moveProcessor.getArmy().WP_FirstMoveMask) == 1) {
				r = 2;
			}
			moveProcessor.getMoves(moves, rules, pos, startX - 1, startY, r,
					isPaint, MOVE_DIRECTION.UP, compType);

			moveProcessor.getMoves(moves, rules, pos, startX - 1, startY + 1,
					1, isPaint, MOVE_DIRECTION.UP_TO_RIGHT, compType);

			moveProcessor.getMoves(moves, rules, pos, startX - 1, startY - 1,
					1, isPaint, MOVE_DIRECTION.UP_TO_LEFT, compType);

		}

		public static void computeBlackMoves(MoveProcessor moveProcessor,
				List<Move> moves, long pos, int startX, int startY,
				boolean isPaint, MOVES_TYPE compType) {

			int r = 1;
			if (moveProcessor.getArmy().getPiece(
					moveProcessor.toLinearPosition(startX, startY),
					moveProcessor.getArmy().BP_FirstMoveMask) == 1) {
				r = 2;
			}
			moveProcessor.getMoves(moves, rules, pos, startX + 1, startY, r,
					isPaint, MOVE_DIRECTION.DOWN, compType);

			moveProcessor.getMoves(moves, rules, pos, startX + 1, startY + 1,
					1, isPaint, MOVE_DIRECTION.DOWN_TO_RIGHT, compType);

			moveProcessor.getMoves(moves, rules, pos, startX + 1, startY - 1,
					1, isPaint, MOVE_DIRECTION.DOWN_TO_LEFT, compType);

		}

		@Override
		public boolean isMoveAttack(ChessBoard board, long from, long to,
				MOVE_DIRECTION dir) {

			if ((board.isAnEnemyAt(to) && dir.equals(MOVE_DIRECTION.UP_TO_LEFT))
					|| (board.isAnEnemyAt(to) && dir
							.equals(MOVE_DIRECTION.UP_TO_RIGHT))
					|| (board.isAnEnemyAt(to) && dir
							.equals(MOVE_DIRECTION.DOWN_TO_LEFT))
					|| (board.isAnEnemyAt(to) && dir
							.equals(MOVE_DIRECTION.DOWN_TO_RIGHT))) {
				return true;
			}
			return false;
		}

		@Override
		public boolean isMoveFree(ChessBoard board, long from, long to,
				MOVE_DIRECTION dir) {
			if (board.getSetup().isDestFieldEmpty(to)
					&& dir.equals(MOVE_DIRECTION.UP)
					|| board.getSetup().isDestFieldEmpty(to)
					&& dir.equals(MOVE_DIRECTION.DOWN)) {
				return true;
			}
			return false;
		}

		@Override
		public boolean isMoveEnpassant(ChessBoard board, long from, long to,
				MOVE_DIRECTION dir) {

			if (board.getSetup().isWPawn(from)
					&& isPossibleEnPassantMoveForWhite(board.getSetup(), to)
					|| board.getSetup().isBPawn(from)
					&& isPossibleEnPassantMoveForBlack(board.getSetup(), to)) {
				return true;
			}
			return false;
		}

		@Override
		public boolean isMovePromotion(ChessBoard board, long from, long to,
				MOVE_DIRECTION dir) {

			long A8toH8positions = -72057594037927936L;
			long A1toH1positions = 255L;

			if (board.getSetup().isWPawn(from) && (to & A8toH8positions) == to) {

				if (dir.equals(MOVE_DIRECTION.UP_TO_LEFT)
						&& board.isAnEnemyAt(from << 9)) {
					return true;
				} else if (dir.equals(MOVE_DIRECTION.UP_TO_RIGHT)
						&& board.isAnEnemyAt(from << 7)) {
					return true;
				} else if (dir.equals(MOVE_DIRECTION.UP)
						&& board.isAnEnemyAt(from << 8)) {
					return false;
				} else if (dir.equals(MOVE_DIRECTION.UP)
						&& !board.isAnEnemyAt(from << 8)) {
					return true;
				}
			} else if (board.getSetup().isBPawn(from)
					&& (to & A1toH1positions) == to) {

				if (dir.equals(MOVE_DIRECTION.DOWN_TO_LEFT)
						&& board.isAnEnemyAt(from >> 7)) {
					return true;
				} else if (dir.equals(MOVE_DIRECTION.DOWN_TO_RIGHT)
						&& board.isAnEnemyAt(from >> 9)) {
					return true;
				} else if (dir.equals(MOVE_DIRECTION.DOWN)
						&& board.isAnEnemyAt(from >> 8)) {
					return false;
				} else if (dir.equals(MOVE_DIRECTION.DOWN)
						&& !board.isAnEnemyAt(from >> 8)) {
					return true;
				}

			}

			return false;
		}

		@Override
		boolean executeAttackMove(PieceSetup army, long startPos, long destPos,
				long boardState) {

			// If it is pawn attack over an enemy pawn , we reset the enemy
			// enpassant position in the mask -if have been settled before.
			army.resetEnpassantPosition(destPos);

			// If some pawn initialize attack over some enemy piece, we reset
			// its
			// enpassant position in the mask -if it have been settled before.
			army.resetEnpassantPosition(startPos);

			// Delete the position of the captive piece - set the piece mask!
			army.updatePieceMask(destPos, 0l);
			// Update the positions for the piece which attack - set the piece
			// mask!
			army.updatePieceMask(startPos, destPos);

			// Move the piece which attack ,to the new position.
			army.setArmy((boardState | destPos) ^ startPos);

			return true;

		}

		@Override
		boolean executeFreeMove(PieceSetup army, long startPos, long destPos,
				long boardState) {

			/**
			 * Here we set the en passant move mask according to the destPos
			 * position of a given pawn.This way approve the correct setup of
			 * the mask.
			 */
			if (army.isBPawn(startPos)) {
				if (isPawnMoveEnableEnemyEnpassantAttack(army, startPos,
						destPos)) {
					army.bp_enPassantMoveMask = army.bp_enPassantMoveMask
							| (destPos << 8);

				}
			} else if (army.isWPawn(startPos)) {
				if (isPawnMoveEnableEnemyEnpassantAttack(army, startPos,
						destPos)) {
					army.wp_enPassantMoveMask = army.wp_enPassantMoveMask
							| (destPos >> 8);

				}
			}

			army.resetEnpassantPosition(startPos);

			// update my board
			army.updatePieceMask(startPos, destPos);
			// update entire board
			// Here update the pickup position and destination position at same
			// time.
			army.setArmy((boardState | destPos) ^ startPos);
			return false;
		}

		@Override
		public void executePromotionMove(PieceSetup setup, long startPos,
				long destPos, long boardState) {

			// Delete the position of the captive piece - set
			// the piece mask!
			setup.updatePieceMask(destPos, 0l);
			// Update the positions for the piece which attack -
			// set the piece
			// mask!
			setup.updatePieceMask(startPos, destPos);

			// Move the piece which attack ,to the new position.
			setup.setArmy((boardState | destPos) ^ startPos);

		}

		@Override
		boolean executeEnPassantMove(PieceSetup army, long startPos, long destPos,
				long boardState) {

			if (army.isWPawn(startPos)) {
				// Capture the left black pawn
				if (destPos == (startPos << 9)) {

					// set the position of the enemy pawn to 0 - capture
					army.updatePieceMask(startPos << 1, 0l);
					// update pieces masks
					army.updatePieceMask(startPos, startPos << 9);
					// here update the white pawn position only(delete the old
					// position and set the new one).
					army.setArmy((boardState | startPos << 9) ^ startPos);
					// Here update the destination position only.
					// We can not update the pickup and destination positions at
					// the same time as we do in the regular piece capture
					// move.The method works for the regular piece capture
					// because in the case, the pickup position of some
					// captive piece turns in destination position for the
					// pieces which attack it.So here we delete the captured
					// piece.
					army.setArmy(army.getArmy() ^ (startPos << 1));

					// Here reset the en passant move mask in case of ""
					army.bp_enPassantMoveMask = army.bp_enPassantMoveMask
							^ startPos << 9;

					// Capture the right black pawn
				} else if (destPos == (startPos << 7)) {
					army.updatePieceMask(startPos >> 1, 0l);
					army.updatePieceMask(startPos, startPos << 7);
					army.setArmy((boardState | startPos << 7) ^ startPos);
					army.setArmy(army.getArmy() ^ (startPos >> 1));

					army.bp_enPassantMoveMask = army.bp_enPassantMoveMask
							^ startPos << 7;
				}
			} else if (army.isBPawn(startPos)) {
				// Capture the left white pawn
				if (destPos == (startPos >> 7)) {
					army.updatePieceMask(startPos << 1, 0l);
					army.updatePieceMask(startPos, startPos >> 7);
					army.setArmy((boardState | startPos >> 7) ^ startPos);
					army.setArmy(army.getArmy() ^ (startPos << 1));

					army.wp_enPassantMoveMask = army.wp_enPassantMoveMask
							^ startPos >> 7;
					// Capture the right white pawn
				} else if (destPos == (startPos >> 9)) {
					army.updatePieceMask(startPos >> 1, 0l);
					army.updatePieceMask(startPos, startPos >> 9);
					army.setArmy((boardState | startPos >> 9) ^ startPos);
					army.setArmy(army.getArmy() ^ (startPos >> 1));

					army.wp_enPassantMoveMask = army.wp_enPassantMoveMask
							^ startPos >> 9;
				}
			}

			return true;

		}

		public boolean isPawnMoveEnableEnemyEnpassantAttack(PieceSetup army,
				long from, long to) {

			// A Move can be enpassant only if its start position is the pawn
			// start
			// position and the destination position hit the enpassant fields
			// mask
			// -A4 to H4 for white and A5 to H5 for the black.
			if (((army.BP_FirstMoveMask & from) == from)) {
				// If the "to" position is the pawn en passsant move
				// mask(1095216660480L) or
				// A5 to H5 for the black pawns we get enpassant move.
				if ((to & 1095216660480L) == to) {
					return true;
				}
			} else if (((army.WP_FirstMoveMask & from) == from)) {
				// and A4 to H4 for the white pawns.
				if ((to & 4278190080L) == to) {
					return true;
				}
			}
			return false;
		}

		/**
		 * When a pawn advances to its eighth rank, as part of the move, it is
		 * promoted and must be exchanged for the player's choice of queen,
		 * rook, bishop, or knight of the same color.
		 * 
		 * @author Miroslav.Shilov
		 * 
		 */
		public static class PiecePromoter {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			PiecePromoter() {

			}

			public static void openPromotionWindow(final long destPos,
					final ChessBoard board) {

				board.getChessWindow().setEnabled(false);

				final JFrame window = new JFrame();
				final JButton queenBtn = new JButton("Queen");

				queenBtn.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {

						promoteInQueen(destPos, board.getSetup());
						board.addPieceImages();
						board.getChessWindow().setEnabled(true);
						window.dispose();

					}

				});
				JButton qrookBtn = new JButton("Rook");
				qrookBtn.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {

						promoteInRook(destPos, board);
						board.getChessWindow().setEnabled(true);
						window.dispose();
					}

				});
				JButton bishopBtn = new JButton("Bishop");
				bishopBtn.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {

						promoteInBishop(destPos, board);
						board.getChessWindow().setEnabled(true);
						window.dispose();
					}

				});
				JButton knightBtn = new JButton("Knight");
				knightBtn.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {

						promoteInKnight(destPos, board);
						board.getChessWindow().setEnabled(true);
						window.dispose();
					}

				});
				window.setUndecorated(true);
				window.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
				window.add(queenBtn);
				window.add(qrookBtn);
				window.add(bishopBtn);
				window.add(knightBtn);
				window.setSize(250, 250);
				window.setResizable(false);
				window.setLayout((new GridLayout(2, 2)));
				window.setLocationRelativeTo(null);
				window.setVisible(true);

			}

			protected static void promoteInKnight(long destPos, ChessBoard board) {
				if (board.getSetup().isWPawn(destPos)) {
					board.getSetup().updatePieceMask(destPos, 0l);
					board.getSetup().WN = board.getSetup().WN | destPos;
					board.addPieceImages();

				} else if (board.getSetup().isBPawn(destPos)) {
					board.getSetup().updatePieceMask(destPos, 0l);
					board.getSetup().BN = board.getSetup().BN | destPos;
					board.addPieceImages();

				}

			}

			protected static void promoteInBishop(long to, ChessBoard board) {
				if (board.getSetup().isWPawn(to)) {
					board.getSetup().updatePieceMask(to, 0l);
					board.getSetup().WB = board.getSetup().WB | to;
					board.addPieceImages();

				} else if (board.getSetup().isBPawn(to)) {
					board.getSetup().updatePieceMask(to, 0l);
					board.getSetup().BB = board.getSetup().BB | to;
					board.addPieceImages();

				}

			}

			protected static void promoteInRook(long destPos, ChessBoard board) {

				if (board.getSetup().isWPawn(destPos)) {
					board.getSetup().updatePieceMask(destPos, 0l);
					board.getSetup().WR = board.getSetup().WR | destPos;
					board.addPieceImages();

				} else if (board.getSetup().isBPawn(destPos)) {

					board.getSetup().updatePieceMask(destPos, 0l);
					board.getSetup().BQ = board.getSetup().BQ | destPos;
					board.addPieceImages();

				}

			}

			protected static void promoteInQueen(long destPos, PieceSetup setup) {

				if (setup.isWPawn(destPos)) {

					setup.updatePieceMask(destPos, 0l);
					setup.WQ = setup.WQ | destPos;

				} else if (setup.isBPawn(destPos)) {

					setup.updatePieceMask(destPos, 0l);
					setup.BQ = setup.BQ | destPos;

				}

			}

		}
	}

	public static class KingRules extends Rules {
		static KingRules rules = new KingRules();

		public static void computeMoves(MoveProcessor moveProcessor,
				List<Move> moves, long startPos, int startX, int startY,
				boolean isPaint, MOVES_TYPE compType) {

			moveProcessor.getMoves(moves, rules, startPos, startX, startY + 1,
					1, isPaint, MOVE_DIRECTION.RIGHT, compType);

			moveProcessor.getMoves(moves, rules, startPos, startX + 1, startY,
					1, isPaint, MOVE_DIRECTION.DOWN, compType);

			moveProcessor.getMoves(moves, rules, startPos, startX, startY - 1,
					1, isPaint, MOVE_DIRECTION.LEFT, compType);

			moveProcessor.getMoves(moves, rules, startPos, startX - 1, startY,
					1, isPaint, MOVE_DIRECTION.UP, compType);

			moveProcessor
					.getMoves(moves, rules, startPos, startX - 1, startY - 1,
							1, isPaint, MOVE_DIRECTION.UP_TO_LEFT, compType);

			moveProcessor.getMoves(moves, rules, startPos, startX + 1,
					startY + 1, 1, isPaint, MOVE_DIRECTION.DOWN_TO_RIGHT,
					compType);

			moveProcessor.getMoves(moves, rules, startPos, startX + 1,
					startY - 1, 1, isPaint, MOVE_DIRECTION.DOWN_TO_LEFT,
					compType);

			moveProcessor.getMoves(moves, rules, startPos, startX - 1,
					startY + 1, 1, isPaint, MOVE_DIRECTION.UP_TO_RIGHT,
					compType);

			// Try to get CASTLING moves here.
			moveProcessor.getMoves(moves, rules, startPos, startX, startY + 2,
					1, isPaint, MOVE_DIRECTION.RIGHT, compType);
			moveProcessor.getMoves(moves, rules, startPos, startX, startY - 2,
					1, isPaint, MOVE_DIRECTION.LEFT, compType);
		}

		@Override
		public boolean isMoveFree(ChessBoard board, long from, long to,
				MOVE_DIRECTION dir) {
			if (board.getSetup().isDestFieldEmpty(to)) {
				// Cut the free move that has been formed during the "Castling"
				// move generating.
				if ((from >> 2) == to || (from << 2) == to) {

				} else {
					return true;
				}

			}
			return false;
		}

		@Override
		public boolean isMoveAttack(ChessBoard board, long from, long to,
				MOVE_DIRECTION dir) {
			if (board.isAnEnemyAt(to)) {

				if ((from >> 2) == to || (from << 2) == to) {

				} else {
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean executeCastlingMove(PieceSetup setup, long startPos,
				long destPos, long boardState) {

			if ((startPos >> 2 == destPos)) {
				executeCastling_EtoG(setup, startPos, destPos, boardState);
			} else if ((startPos << 2 == destPos)) {
				executeCastling_EtoC(setup, startPos, destPos, boardState);
			}

			return false;
		}

		private void executeCastling_EtoC(PieceSetup setup, long startPos,
				long destPos, long boardState) {
			setup.updatePieceMask(startPos, destPos);
			setup.setArmy((boardState | destPos) ^ startPos);

			setup.updatePieceMask(startPos << 4, startPos << 1);
			setup.setArmy((setup.getArmy() | startPos << 1) ^ startPos << 4);
		}

		private void executeCastling_EtoG(PieceSetup setup, long startPos,
				long destPos, long boardState) {
			setup.updatePieceMask(startPos, destPos);
			setup.setArmy((boardState | destPos) ^ startPos);

			setup.updatePieceMask(startPos >> 3, startPos >> 1);
			setup.setArmy((setup.getArmy() | startPos >> 1) ^ startPos >> 3);
		}
	}

	public static class KnightRules extends Rules {
		static KnightRules rules = new KnightRules();

		public static void computeMoves(MoveProcessor moveProcessor,
				List<Move> moves, long pos, int startX, int startY,
				boolean isPaint, MOVES_TYPE compType) {

			moveProcessor.getMoves(moves, rules, pos, startX - 2, startY - 1,
					1, isPaint, MOVE_DIRECTION.UP_TO_LEFT, compType);

			moveProcessor.getMoves(moves, rules, pos, startX + 2, startY + 1,
					1, isPaint, MOVE_DIRECTION.DOWN_TO_RIGHT, compType);

			moveProcessor.getMoves(moves, rules, pos, startX + 2, startY - 1,
					1, isPaint, MOVE_DIRECTION.DOWN_TO_LEFT, compType);

			moveProcessor.getMoves(moves, rules, pos, startX - 2, startY + 1,
					1, isPaint, MOVE_DIRECTION.UP_TO_RIGHT, compType);

			moveProcessor.getMoves(moves, rules, pos, startX - 1, startY - 2,
					1, isPaint, MOVE_DIRECTION.UP_TO_LEFT, compType);

			moveProcessor.getMoves(moves, rules, pos, startX + 1, startY + 2,
					1, isPaint, MOVE_DIRECTION.DOWN_TO_RIGHT, compType);

			moveProcessor.getMoves(moves, rules, pos, startX + 1, startY - 2,
					1, isPaint, MOVE_DIRECTION.DOWN_TO_LEFT, compType);

			moveProcessor.getMoves(moves, rules, pos, startX - 1, startY + 2,
					1, isPaint, MOVE_DIRECTION.UP_TO_RIGHT, compType);

		}

	}

	public static class BishopRules extends Rules {

		static BishopRules rules = new BishopRules();

		public static void computeMoves(MoveProcessor moveProcessor,
				List<Move> moves, long pos, int startX, int startY,
				boolean isPaint, MOVES_TYPE compType) {

			moveProcessor.getMoves(moves, rules, pos, startX - 1, startY - 1,
					7, isPaint, MOVE_DIRECTION.UP_TO_LEFT, compType);

			moveProcessor.getMoves(moves, rules, pos, startX + 1, startY + 1,
					7, isPaint, MOVE_DIRECTION.DOWN_TO_RIGHT, compType);

			moveProcessor.getMoves(moves, rules, pos, startX + 1, startY - 1,
					7, isPaint, MOVE_DIRECTION.DOWN_TO_LEFT, compType);

			moveProcessor.getMoves(moves, rules, pos, startX - 1, startY + 1,
					7, isPaint, MOVE_DIRECTION.UP_TO_RIGHT, compType);

		}

	}

	public static class QueenRules extends Rules {

		static QueenRules rules = new QueenRules();

		public static void computeMoves(MoveProcessor moveProcessor,
				List<Move> moves, long pos, int startX, int startY,
				boolean isPaint, MOVES_TYPE compType) {

			moveProcessor.getMoves(moves, rules, pos, startX - 1, startY, 7,
					isPaint, MOVE_DIRECTION.UP, compType);

			moveProcessor.getMoves(moves, rules, pos, startX + 1, startY, 7,
					isPaint, MOVE_DIRECTION.DOWN, compType);

			moveProcessor.getMoves(moves, rules, pos, startX, startY - 1, 7,
					isPaint, MOVE_DIRECTION.LEFT, compType);

			moveProcessor.getMoves(moves, new QueenRules(), pos, startX,
					startY + 1, 7, isPaint, MOVE_DIRECTION.RIGHT, compType);

			moveProcessor
					.getMoves(moves, new QueenRules(), pos, startX - 1,
							startY - 1, 7, isPaint, MOVE_DIRECTION.UP_TO_LEFT,
							compType);

			moveProcessor.getMoves(moves, new QueenRules(), pos, startX - 1,
					startY + 1, 7, isPaint, MOVE_DIRECTION.UP_TO_RIGHT,
					compType);

			moveProcessor.getMoves(moves, new QueenRules(), pos, startX + 1,
					startY - 1, 7, isPaint, MOVE_DIRECTION.DOWN_TO_LEFT,
					compType);

			moveProcessor.getMoves(moves, new QueenRules(), pos, startX + 1,
					startY + 1, 7, isPaint, MOVE_DIRECTION.DOWN_TO_RIGHT,
					compType);

		}

	}

	public static class RookRules extends Rules {

		static RookRules rules = new RookRules();

		public static void computeMoves(MoveProcessor moveProcessor,
				List<Move> moves, long pos, int startX, int startY,
				boolean isPaint, MOVES_TYPE compType) {

			moveProcessor.getMoves(moves, rules, pos, startX, startY + 1, 7,
					isPaint, MOVE_DIRECTION.RIGHT, compType);

			moveProcessor.getMoves(moves, rules, pos, startX + 1, startY, 7,
					isPaint, MOVE_DIRECTION.DOWN, compType);

			moveProcessor.getMoves(moves, rules, pos, startX, startY - 1, 7,
					isPaint, MOVE_DIRECTION.LEFT, compType);

			moveProcessor.getMoves(moves, rules, pos, startX - 1, startY, 7,
					isPaint, MOVE_DIRECTION.UP, compType);

		}

	}

	public int updateX(int startX, MOVE_DIRECTION dir) {

		if (dir.equals(MOVE_DIRECTION.DOWN_TO_RIGHT)) {
			return startX + 1;
		} else if (dir.equals(MOVE_DIRECTION.DOWN_TO_LEFT)) {
			return startX + 1;
		} else if (dir.equals(MOVE_DIRECTION.UP_TO_RIGHT)) {
			return startX - 1;
		} else if (dir.equals(MOVE_DIRECTION.UP_TO_LEFT)) {
			return startX - 1;
		} else if (dir.equals(MOVE_DIRECTION.UP)) {
			return startX - 1;
		} else if (dir.equals(MOVE_DIRECTION.DOWN)) {
			return startX + 1;
		} else if (dir.equals(MOVE_DIRECTION.LEFT)) {
			return startX;
		} else if (dir.equals(MOVE_DIRECTION.RIGHT)) {
			return startX;
		}
		return 0;
	}

	public int updateY(int startY, MOVE_DIRECTION dir) {

		if (dir.equals(MOVE_DIRECTION.DOWN_TO_RIGHT)) {
			return startY + 1;
		} else if (dir.equals(MOVE_DIRECTION.DOWN_TO_LEFT)) {
			return startY - 1;
		} else if (dir.equals(MOVE_DIRECTION.UP_TO_RIGHT)) {
			return startY + 1;
		} else if (dir.equals(MOVE_DIRECTION.UP_TO_LEFT)) {
			return startY - 1;
		} else if (dir.equals(MOVE_DIRECTION.UP)) {
			return startY;
		} else if (dir.equals(MOVE_DIRECTION.DOWN)) {
			return startY;
		} else if (dir.equals(MOVE_DIRECTION.LEFT)) {
			return startY - 1;
		} else if (dir.equals(MOVE_DIRECTION.RIGHT)) {
			return startY + 1;
		}
		return 0;
	}

	public int updateRange(int range, MOVE_DIRECTION dir) {

		return range - 1;

	}

	private boolean isDownToRightMoveOver(int startX, int startY, int range) {

		if (startX > 7 || startY > 7 || range == 0) {
			return true;
		}
		return false;
	}

	private boolean isDownToLeftMoveOver(int startX, int startY, int range) {
		if (startX > 7 || startY < 0 || range == 0) {
			return true;
		}
		return false;
	}

	private boolean isUpToRightMoveOver(int startX, int startY, int range) {
		if (startX < 0 || startY > 7 || range == 0) {
			return true;
		}
		return false;
	}

	private boolean isUpToLeftMoveOver(int startX, int startY, int range) {
		if (startX < 0 || startY < 0 || range == 0) {
			return true;
		}
		return false;
	}

	private boolean isUpMoveOver(int startX, int startY, int range) {
		if (startX < 0 || range == 0) {
			return true;
		}
		return false;
	}

	private boolean isDownMoveOver(int startX, int startY, int range) {
		if (startX > 7 || range == 0) {
			return true;
		}
		return false;
	}

	private boolean isLeftMoveOver(int startX, int startY, int range) {
		if (startY < 0 || range == 0) {
			return true;
		}
		return false;
	}

	private boolean isRightMoveOver(int startX, int startY, int range) {
		if (startY > 7 || range == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Free move: free move can be done in all of the time according to the move
	 * rules.All free moves is green colored.
	 */
	public boolean isMoveFree(ChessBoard board, long from, long to,
			MOVE_DIRECTION dir) {
		if (board.getSetup().isDestFieldEmpty(to)) {
			return true;
		}
		return false;
	}

	/**
	 * Attack move is move that can capture some enemy piece.Attack move is red
	 * colored.
	 * 
	 */
	public boolean isMoveAttack(ChessBoard board, long from, long to,
			MOVE_DIRECTION dir) {
		if (board.isAnEnemyAt(to)) {
			return true;
		}
		return false;
	}

	/**
	 * En passant:
	 * 
	 * Examples of pawn moves: (left) promotion; (right) en passant When a pawn
	 * makes a two-step advance from its starting position and there is an
	 * opponent's pawn on a square next to the destination square on an adjacent
	 * file, then the opponent's pawn can capture it en passant ("in passing"),
	 * moving to the square the pawn passed over. This can be done only on the
	 * turn immediately following the enemy pawn's two-square advance;
	 * otherwise, the right to do so is forfeited. For example, in the animated
	 * diagram, the black pawn advances two squares from g7 to g5, and the white
	 * pawn on f5 can take it en passant on g6 (but only immediately after the
	 * black pawn's advance).
	 */

	public boolean isMoveEnpassant(ChessBoard board, long from, long to,
			MOVE_DIRECTION dir) {

		return false;
	}

	/**
	 * To castle, simply move the king two spaces to the left or right, OR move
	 * the king on top of the rook you want to castle with. The rook will jump
	 * across and to the other side of the king automatically!
	 * 
	 * You can’t castle any time you want to, though. Here are the rules for
	 * castling:
	 * 
	 * 1. Your king can not have moved- Once your king moves, you can no longer
	 * castle, even if you move the king back to the starting square. Many
	 * strategies involve forcing the opponent’s king to move just for this
	 * reason.
	 * 
	 * 2. Your rook can not have moved- If you move your rook, you can’t castle
	 * on that side anymore. Both the king and the rook you are castling with
	 * can’t have moved.
	 * 
	 * 3. Your king can NOT be in check- Though castling often looks like an
	 * appealing escape, you can’t castle while you are in check! Once you are
	 * out of check, then you can castle. Unlike moving, being checked does not
	 * remove the ability to castle later.
	 * 
	 * 4. Your king can not pass through check- If any square the king moves
	 * over or moves onto would put you in check, you can’t castle. You’ll have
	 * to get rid of that pesky attacking piece first!
	 * 
	 * 5. No pieces can be between the king and rook- All the spaces between the
	 * king and rook must be empty. This is part of why it’s so important to get
	 * your pieces out into the game as soon as possible!
	 */

	public boolean isMoveCastling(ChessBoard board, long from, long to,
			MOVE_DIRECTION dir) {

		if (board.getSetup().isWKing(from)) {

			if (isPossibleWhiteKingCastlingTo_G1(board, from, to)
			// && (!isWillBeInCheckInNextRightCell(board, from, this) &&
					// !isInCheck(board))
					|| isPossibleWhiteKingCastlingToC1(board, from, to))
			// && (!isWillBeInCheckInNextLeftCell(board, from, this) &&
			// !isInCheck(board)))
			{
				return true;
			}
		} else if (board.getSetup().isBKing(from)) {

			if (isPossibleBlackKingCastlingTo_G8(board, from, to)
			// && (!isWillBeInCheckInNextRightCell(board, from, this) &&
					// !isInCheck(board))
					|| isPossibleBlackKingCastlingToC8(board, from, to)) {
				// && (!isWillBeInCheckInNextLeftCell(board, from, this) &&
				// !isInCheck(board))) {
				return true;
			}
		}

		return false;

	}

	private boolean isPossibleWhiteKingCastlingToC1(ChessBoard board,
			long from, long to) {
		if (((board.getSetup().getArmy() & 112L) == 0)
				&& !board.getSetup().isWhiteKingMoved()
				&& !board.getSetup().isLeftWhiteRookMoved() && (to == 32L)) {
			return true;
		}
		return false;
	}

	private boolean isPossibleBlackKingCastlingToC8(ChessBoard board,
			long from, long to) {
		if (((board.getSetup().getArmy() & 8070450532247928832L) == 0)
				&& !board.getSetup().isBlackKingMoved()
				&& !board.getSetup().isLeftBlackRookMoved()
				&& (to == 2305843009213693952L)) {
			return true;
		}
		return false;
	}

	private boolean isPossibleWhiteKingCastlingTo_G1(ChessBoard board,
			long from, long to) {
		if (((board.getSetup().getArmy() & 6L) == 0)
				&& !board.getSetup().isWhiteKingMoved()
				&& !board.getSetup().isRightWhiteRookMoved() && (to == 2L)) {
			return true;
		}
		return false;
	}

	private boolean isPossibleBlackKingCastlingTo_G8(ChessBoard board,
			long from, long to) {
		if (((board.getSetup().getArmy() & 432345564227567616L) == 0)
				&& !board.getSetup().isBlackKingMoved()
				&& !board.getSetup().isRightBlackRookMoved()
				&& (to == 144115188075855872L)) {
			return true;
		}
		return false;
	}

	public boolean isMoveOver(int startX, int startY, int range,
			MOVE_DIRECTION dir) {
		if (dir.equals(MOVE_DIRECTION.DOWN_TO_RIGHT)) {
			if (isDownToRightMoveOver(startX, startY, range)) {
				return true;
			}
		} else if (dir.equals(MOVE_DIRECTION.DOWN_TO_LEFT)) {
			if (isDownToLeftMoveOver(startX, startY, range)) {
				return true;
			}
		} else if (dir.equals(MOVE_DIRECTION.UP_TO_RIGHT)) {
			if (isUpToRightMoveOver(startX, startY, range)) {
				return true;
			}
		} else if (dir.equals(MOVE_DIRECTION.UP_TO_LEFT)) {
			if (isUpToLeftMoveOver(startX, startY, range)) {
				return true;
			}
		} else if (dir.equals(MOVE_DIRECTION.UP)) {
			if (isUpMoveOver(startX, startY, range)) {
				return true;
			}
		} else if (dir.equals(MOVE_DIRECTION.DOWN)) {
			if (isDownMoveOver(startX, startY, range)) {
				return true;
			}
		} else if (dir.equals(MOVE_DIRECTION.LEFT)) {
			if (isLeftMoveOver(startX, startY, range)) {
				return true;
			}
		} else if (dir.equals(MOVE_DIRECTION.RIGHT)) {
			if (isRightMoveOver(startX, startY, range)) {
				return true;
			}
		}
		return false;
	}

	// This method works according to the current player.If the current player
	// is BLACK then this method check for chess state of the WHITE player and
	// vice versa.
	public boolean isInCheck(ChessBoard board) {

		// Form mask with all legal enemy moves for all enemy pieces that can
		// capture some current piece.
		long allAffectiveEnemyMovesMask = board.getMoveProcessor()
				.getAllAffectiveEnemyMovesMask();

		// If some enemy piece can reach my piece i am in chess.
		if (board.getTurn().equals(Team.WHITE)) {
			if (((board.getSetup().WK | allAffectiveEnemyMovesMask) == allAffectiveEnemyMovesMask)) {
				return true;
			}
		} else {
			if (((board.getSetup().BK | allAffectiveEnemyMovesMask) == allAffectiveEnemyMovesMask)) {
				return true;
			}
		}
		return false;
	}

	boolean executeEnPassantMove(PieceSetup army, long startPos, long destPos,
			long boardState) {

		return false;

	}

	boolean executeAttackMove(PieceSetup army, long startPos, long destPos,
			long boardState) {

		// Delete the position of the captive piece - set the piece mask!
		army.updatePieceMask(destPos, 0l);
		// Update the positions for the piece which attack - set the piece
		// mask!
		army.updatePieceMask(startPos, destPos);

		// Move the piece which attack ,to the new position.
		army.setArmy((boardState | destPos) ^ startPos);

		return true;

	}

	boolean executeFreeMove(PieceSetup setup, long startPos, long destPos,
			long boardState) {

		// update my board
		setup.updatePieceMask(startPos, destPos);
		// update entire board
		// Here update the pickup position and destination position at same
		// time.
		setup.setArmy((boardState | destPos) ^ startPos);
		return false;
	}

	public boolean executeCastlingMove(PieceSetup setup, long startPos,
			long destPos, long boardState) {

		return false;
	}

	public void executePromotionMove(PieceSetup setup, long startPos, long destPos,
			long boardState) {

	}

	public class ChessChecker {

	}

	public boolean isWillBeInCheckInNextRightCell(ChessBoard board, long from,
			Rules rule) {
		// We can not cross positions in which we are in check and here
		// we check this case.

		Move mo = new Move(board, from, from >> 1, MOVE_PURPOSE.FREE, rule);
		mo.execute();
		if (rule.isInCheck(board)) {
			mo.undo();
			return true;
		}
		mo.undo();
		return false;
	}

	public boolean isWillBeInCheckInNextLeftCell(ChessBoard board, long from,
			Rules rule) {
		// We can not cross positions in which we are in check and here
		// we check this case.

		Move mo = new Move(board, from, from << 1, MOVE_PURPOSE.FREE, rule);
		mo.execute();
		if (rule.isInCheck(board)) {
			mo.undo();
			return true;
		}
		mo.undo();
		return false;
	}

	/**
	 * Promotion:
	 * 
	 * When a pawn advances to its eighth rank, as part of the move, it is
	 * promoted and must be exchanged for the player's choice of queen, rook,
	 * bishop, or knight of the same color. Usually, the pawn is chosen to be
	 * promoted to a queen, but in some cases, another piece is chosen; this is
	 * called under promotion. In the animated diagram, the pawn on c7 can be
	 * advanced to the eighth rank and be promoted. There is no restriction on
	 * the piece promoted to, so it is possible to have more pieces of the same
	 * type than at the start of the game (e.g., two or more queens). If the
	 * required piece is not available (e.g. a second queen) an inverted rook is
	 * sometimes used as a substitute, but this is not recognized in
	 * FIDE-sanctioned games.
	 * 
	 */

	public boolean isMovePromotion(ChessBoard board, long from, long to,
			MOVE_DIRECTION dir) {

		return false;
	}
}
