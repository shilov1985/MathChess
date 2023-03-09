package Players;

import Board.PieceSetup;
import Board.ChessBoard;

/**
 * This class represent a chess Move.Every Move contains: start position(startPos),destination position(destPos),
 * copy of the army (army) and all solders(BR,BN,BB...), copy of the enemy board(enemyBoard_12),
 * first pawn move mask for every pawn color(WP_FirstMoveMask and BP_FirstMoveMask),copy of the army in concrete moment(boardState),
 * a copy of the mask by which "LegalMoveProcessor" can determine that  some move is en passant(wp_enPassantMoveMask and bp_enPassantMoveMask)
 * and a boolean variable that says that is this move en passant (isEnPassantAttackMove).
 * 
 *   Here is important to clarify  the all cases for en passant enPassantMoveMask resetting .So we can separate this to 2 general cases.
 * And one more important think is that when we execute a en passant Move ,in all cases we capture some enemy pawn.
 * 
 *   Every Move belongs to a some move type - ENPASSANT, ATTACK, FREE, CASTLING, PROMOTION. When we execute a move we do that according 
 * to the "MOVE_TYPE" and for every move type we use a special execute method.
 * 
 * 
 * 1 case - Move an en passant pawn:
 * 
 *   a) move the pawn to another occupied by enemy field.In this case we capture the enemy and reset the 
 *   en passant mask (if it is settled before)using the start position of the pawn as a start point.
 *       We do this by using "executeRegularCaptureMove" method.
 *   
 *   b) move the pawn to another not occupied field.In this case we just move the pawn to another field and use start position of 
 *   the pawn as a start point to reset (if it is settled before) the enPassantMoveMask.
 *       We do this by using "executeFreeMove()" method.
 *   
 * 2 case - Capture en passant pawn:
 * 
 *   a) Capture an en passant pawn by its actual position.In this case we first check that the pawn we capture is en passant and if it is then capture it and
 *   reset its en passant mask by its start position.
 *     We use "executeRegularCaptureMove()" method.
 *     
 *      b) Capture an en passant pawn by its en passant position- the position behind the pawn!Capture the pawn and
 *   reset its en passant mask by its start position.
 *     We use "executeEnPassantMove()" method.
 */

public class Move {

	public static enum MOVE_PURPOSE {

		ENPASSANT, ATTACK, FREE, CASTLING, PROMOTION

	}

	public static enum INITIATOR {

		HUMAN, ALGORITHM

	}

	private MOVE_PURPOSE movePurpose;

	public MOVE_PURPOSE getMovePurpose() {
		return movePurpose;
	}

	private long startPos;

	public long getStartPos() {
		return startPos;
	}

	private long destPos;

	public long getDestPos() {
		return destPos;
	}

	private PieceSetup setup;

	public PieceSetup getSetup() {
		return setup;
	}

	private long enemyBoard_12;

	public long getEnemyBoard() {
		return enemyBoard_12;
	}

	public long WP_FirstMoveMask;
	public long BP_FirstMoveMask;

	private boolean isWhiteKingMoved;
	private boolean isRightWhiteRookMoved;
	private boolean isLeftWhiteRookMoved;

	private boolean isBlackKingMoved;
	private boolean isRightBlackRookMoved;
	private boolean isLeftBlackRookMoved;

	public long BR;
	public long BN;
	public long BB;
	public long BQ;
	public long BK;
	public long BP;
	public long WR;
	public long WN;
	public long WB;
	public long WQ;
	public long WK;
	public long WP;

	private long boardState;
	// wp_enPassantMoveMask and bp_enPassantMoveMask are updated by
	// "armyPositionsUpdate" method of Army class by some rules.
	private long wp_enPassantMoveMask;
	private long bp_enPassantMoveMask;

	private ChessBoard board;
	private int moveCounter;
	private Rules rule;

	public Move(ChessBoard board, long pickPos, long destPos,
			MOVE_PURPOSE typeOfMove, Rules rule) {

		this.rule = rule;
		this.movePurpose = typeOfMove;
		this.board = board;
		this.moveCounter = board.getFiftyMoveCounter();
		this.startPos = pickPos;
		this.destPos = destPos;
		this.setup = board.getSetup();
		this.boardState = setup.getArmy();

		isWhiteKingMoved = board.getSetup().isWhiteKingMoved();
		isRightWhiteRookMoved = board.getSetup().isRightWhiteRookMoved();
		isLeftWhiteRookMoved = board.getSetup().isLeftWhiteRookMoved();

		isBlackKingMoved = board.getSetup().isBlackKingMoved();
		isRightBlackRookMoved = board.getSetup().isRightBlackRookMoved();
		isLeftBlackRookMoved = board.getSetup().isLeftBlackRookMoved();

		this.WP_FirstMoveMask = setup.WP_FirstMoveMask;
		this.BP_FirstMoveMask = setup.BP_FirstMoveMask;

		this.wp_enPassantMoveMask = setup.wp_enPassantMoveMask;
		this.bp_enPassantMoveMask = setup.bp_enPassantMoveMask;

		this.BR = setup.BR;
		this.BN = setup.BN;
		this.BB = setup.BB;
		this.BQ = setup.BQ;
		this.BK = setup.BK;
		this.BP = setup.BP;
		this.WR = setup.WR;
		this.WN = setup.WN;
		this.WB = setup.WB;
		this.WQ = setup.WQ;
		this.WK = setup.WK;
		this.WP = setup.WP;
	}

	public boolean execute() {

		if (this.movePurpose.equals(MOVE_PURPOSE.ENPASSANT)) {

			return rule.executeEnPassantMove(setup, startPos, destPos,
					boardState);

		} else if (this.movePurpose.equals(MOVE_PURPOSE.ATTACK)) {

			return rule.executeAttackMove(setup, startPos, destPos, boardState);

		} else if (this.movePurpose.equals(MOVE_PURPOSE.FREE)) {

			rule.executeFreeMove(setup, startPos, destPos, boardState);

		} else if (this.movePurpose.equals(MOVE_PURPOSE.CASTLING)) {

			rule.executeCastlingMove(setup, startPos, destPos, boardState);

		} else if (this.movePurpose.equals(MOVE_PURPOSE.PROMOTION)) {

			rule.executePromotionMove(setup, startPos, destPos, boardState);

		}
		return false;

	}

	// We keep track of some variables to use them for undo move here.
	// Undo move is important because we use it in the "MoveProcessor"(undo a
	// move if that move cause a shess) and
	// "MiniMax"(for undo move during the DFS algorithm) classes.
	public void undo() {

		setup.setArmy(boardState);
		board.setFiftyMoveCounter(this.moveCounter);
		setup.WP_FirstMoveMask = this.WP_FirstMoveMask;
		setup.BP_FirstMoveMask = this.BP_FirstMoveMask;

		setup.wp_enPassantMoveMask = this.wp_enPassantMoveMask;
		setup.bp_enPassantMoveMask = this.bp_enPassantMoveMask;

		// System.out.println("after move bp_enPassantMoveMask = "
		// + Long.toBinaryString(bp_enPassantMoveMask));

		setup.setRightWhiteRookMoved(this.isRightWhiteRookMoved);
		setup.setLeftWhiteRookMoved(this.isLeftWhiteRookMoved);
		setup.setWhiteKingMoved(this.isWhiteKingMoved);

		setup.setRightBlackRookMoved(this.isRightBlackRookMoved);
		setup.setLeftBlackRookMoved(this.isLeftBlackRookMoved);
		setup.setBlackKingMoved(this.isBlackKingMoved);

		setup.BR = this.BR;
		setup.BN = this.BN;
		setup.BB = this.BB;
		setup.BQ = this.BQ;
		setup.BK = this.BK;
		setup.BP = this.BP;
		setup.WR = this.WR;
		setup.WN = this.WN;
		setup.WB = this.WB;
		setup.WQ = this.WQ;
		setup.WK = this.WK;
		setup.WP = this.WP;

	}

}
