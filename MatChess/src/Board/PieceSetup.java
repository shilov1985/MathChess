package Board;

import Players.Team;
import Players.Rules.ChessSetup;

/**
 * This class represent the army as a "long" variables- see "bitboard". We use a
 * binary mask to recognize a given piece.
 * 
 * ENPASSANT: for the first pawn move options we use a binary mask
 * (WP_FirstMoveMask and BP_FirstMoveMask) to keep the information for that
 * which pawn has right to move from its start field to the third(enpassant
 * capturing move for the enemy) - first move or any other moves after the very
 * first move. Example: 0 = not allow move(two squares move), 1 = allow move. If
 * we move some pawn in that way we enable possibility for it to be enpassant
 * captured. We keep track for every enpassant possibility capturing in
 * "wp_enPassantMoveMask" and "bp_enPassantMoveMask";
 * 
 * CASTLING:We use boolean variables to keep a track for the pieces which allow
 * us to move the King in Castling.
 * 
 * The all variables here will be updated during the game process.The Rules
 * class consist all start states of this variables and we use them every time
 * when we want to set this Setup to the game start state.
 */

public class PieceSetup {

	private long army;

	/**
	 * Use bitboard here as args
	 * 
	 * @param army
	 */
	public void setArmy(long army) {
		this.army = army;
	}

	/**
	 * @return a bitboard
	 */
	public long getArmy() {
		return army;
	}

	public long WP_FirstMoveMask;
	public long BP_FirstMoveMask;

	public long wp_enPassantMoveMask;
	public long bp_enPassantMoveMask;

	/*
	 * Here declare the all 12 bitboards.Because of name conflict of the Knight
	 * and the King we use BN and WN for the names of black and white knight. We
	 * use this variables to set the chess board start state and further as a
	 * mask for piece recognize.
	 */
	public long BR;
	public long BN;// black knight
	public long BB;
	public long BQ;
	public long BK;
	public long BP;

	public long WR;
	public long WN;// white knight
	public long WB;
	public long WQ;
	public long WK;
	public long WP;

	private boolean isWhiteKingMoved;

	public boolean isWhiteKingMoved() {
		return isWhiteKingMoved;
	}

	public void setWhiteKingMoved(boolean isWhiteKingMove) {
		this.isWhiteKingMoved = isWhiteKingMove;
	}

	private boolean isRightWhiteRookMoved;

	public boolean isRightWhiteRookMoved() {
		return isRightWhiteRookMoved;
	}

	public void setRightWhiteRookMoved(boolean isRightWhiteRookMove) {
		this.isRightWhiteRookMoved = isRightWhiteRookMove;
	}

	private boolean isLeftWhiteRookMoved;

	public boolean isLeftWhiteRookMoved() {
		return isLeftWhiteRookMoved;
	}

	public void setLeftWhiteRookMoved(boolean isLeftWhiteRookMoved) {
		this.isLeftWhiteRookMoved = isLeftWhiteRookMoved;
	}

	private boolean isBlackKingMoved;

	public boolean isBlackKingMoved() {
		return isBlackKingMoved;
	}

	public void setBlackKingMoved(boolean isBlackKingMoved) {
		this.isBlackKingMoved = isBlackKingMoved;

	}

	private boolean isRightBlackRookMoved;

	public boolean isRightBlackRookMoved() {
		return isRightBlackRookMoved;
	}

	public void setRightBlackRookMoved(boolean isRightBlackRookMoved) {
		this.isRightBlackRookMoved = isRightBlackRookMoved;

	}

	private boolean isLeftBlackRookMoved;

	public boolean isLeftBlackRookMoved() {
		return isLeftBlackRookMoved;
	}

	public void setLeftBlackRookMoved(boolean isLeftBlackRookMoved) {
		this.isLeftBlackRookMoved = isLeftBlackRookMoved;
	}

	PieceSetup(ChessSetup chessSetup) {
		chessSetup.set(this);
	}

	/**
	 * Get the value of some board by an index.Range for extract is 1 - 64 inc.
	 * Examle: index = 5;
	 * 
	 * piecesSet =
	 * 0000000000000000000000000000000000000000000000000000000000010000
	 * 
	 * result = 1;
	 * 
	 */

	public long getPiece(int index, long piecesSet) {
		if (index > 0 && index <= 64) {
			{
				return (((1 << 1) - 1) & (piecesSet >> (index - 1)));
			}
		}
		return -1;
	}

	/**
	 * Method to update the army every time when a move is initiated. Here we
	 * use bitwise operations to recognize and update the pieces positions.
	 * 
	 * @param from
	 * @param to
	 */
	public void updatePieceMask(long from, long to) {
		if (isBRook(from)) {

			if (isRightBlackRook(from)) {
				isRightBlackRookMoved = true;
			} else if (isLeftBlackRook(from)) {
				isLeftBlackRookMoved = true;
			}

			BR = (BR | to) ^ from;
			//System.out.println("after move BR = " + Long.toBinaryString(BR));
		} else if (isBKnignt(from)) {
			BN = (BN | to) ^ from;
			// System.out.println("after move BN = " + Long.toBinaryString(BN));
		} else if (isBBishop(from)) {
			BB = (BB | to) ^ from;
			// System.out.println("after move BB = " + Long.toBinaryString(BB));
		} else if (isBQueen(from)) {
			BQ = (BQ | to) ^ from;
			// System.out.println("after move BQ = " + Long.toBinaryString(BQ));
		} else if (isBKing(from)) {

			isBlackKingMoved = true;
			BK = (BK | to) ^ from;

			// System.out.println("after move BK = " + Long.toBinaryString(BK));
		} else if (isBPawn(from)) {
			BP = (BP | to) ^ from;
			//System.out.println("after move BP = " + Long.toBinaryString(BP));

		} else

		if (isWRook(from)) {

			if (isRightWhiteRook(from)) {
				isRightWhiteRookMoved = true;
			} else if (isLeftWhiteRook(from)) {
				isLeftWhiteRookMoved = true;
			}

			WR = (WR | to) ^ from;
			// System.out.println("after move WR = " + Long.toBinaryString(WR));
		} else if (isWKnignt(from)) {
			WN = (WN | to) ^ from;
			// System.out.println("after move WN = " + Long.toBinaryString(WN));
		} else if (isWBishop(from)) {
			WB = (WB | to) ^ from;
			// System.out.println("after move WB = " + Long.toBinaryString(WB));
		} else if (isWQueen(from)) {
			WQ = (WQ | to) ^ from;
			// System.out.println("after move WQ = " + Long.toBinaryString(WQ));
		} else if (isWKing(from)) {

			isWhiteKingMoved = true;
			WK = (WK | to) ^ from;

			// System.out.println("after move WK = " + Long.toBinaryString(WK));
		} else if (isWPawn(from)) {
			WP = (WP | to) ^ from;

		}
	}

	private boolean isLeftWhiteRook(long from) {
		long leftRookStartPos = 128L;
		if ((from & leftRookStartPos) == from) {
			return true;
		}
		return false;
	}

	private boolean isLeftBlackRook(long from) {
		long leftBlackRookStartPos = -9223372036854775808L;
		if ((from & leftBlackRookStartPos) == from) {
			return true;
		}
		return false;
	}

	private boolean isRightWhiteRook(long from) {

		long rightWhiteRookStartPos = 1L;
		if ((from & rightWhiteRookStartPos) == from) {
			return true;
		}
		return false;
	}

	private boolean isRightBlackRook(long from) {

		long rightBlackRookStartPos = 72057594037927936L;
		if ((from & rightBlackRookStartPos) == from) {
			return true;
		}
		return false;
	}



	public boolean isWPawn(long bPosS) {
		if ((bPosS | WP) == WP) {
			return true;
		}
		return false;
	}

	public boolean isBPawn(long bPosS) {
		if ((bPosS | BP) == BP) {
			return true;
		}
		return false;
	}

	public boolean isWKing(long bPosS) {
		if ((bPosS | WK) == WK) {
			return true;
		}
		return false;
	}

	public boolean isBKing(long bPosS) {
		if ((bPosS | BK) == BK) {
			return true;
		}
		return false;
	}

	public boolean isWKnignt(long bPosS) {
		if ((bPosS | WN) == WN) {
			return true;
		}
		return false;
	}

	public boolean isBKnignt(long bPosS) {
		if ((bPosS | BN) == BN) {
			return true;
		}
		return false;
	}

	public boolean isBBishop(long bPosS) {
		if ((bPosS | BB) == BB) {
			return true;
		}
		return false;
	}

	public boolean isWBishop(long bPosS) {
		if ((bPosS | WB) == WB) {
			return true;
		}
		return false;
	}

	public boolean isBQueen(long bPosS) {
		if ((bPosS | BQ) == BQ) {
			return true;
		}
		return false;
	}

	public boolean isWQueen(long bPosS) {
		if ((bPosS | WQ) == WQ) {
			return true;
		}
		return false;
	}

	public boolean isWRook(long bPosS) {
		if ((bPosS | WR) == WR) {
			return true;
		}
		return false;
	}

	public boolean isBRook(long bPosS) {
		if ((bPosS | BR) == BR) {
			return true;
		}
		return false;
	}

	public long getBoardAt(long p) {
		if (isWRook(p)) {
			return WR;
		} else if (isBRook(p)) {
			return BR;
		} else if (isWQueen(p)) {
			return WQ;
		} else if (isBQueen(p)) {
			return BQ;
		} else if (isWBishop(p)) {
			return WB;
		} else if (isBBishop(p)) {
			return BB;
		} else if (isWKnignt(p)) {
			return WN;
		} else if (isBKnignt(p)) {
			return BN;
		} else if (isBKing(p)) {
			return BK;
		} else if (isWKing(p)) {
			return WK;
		} else if (isBPawn(p)) {
			return BP;
		} else if (isWPawn(p)) {
			return WP;
		}
		return 0;
	}

	public boolean isDestFieldEmpty(long to) {

		if ((getArmy() & to) != to) {
			return true;
		}
		return false;
	}

	public boolean isEnabledEnPassantPosition(long to, Team team) {

		if (team.equals(Team.WHITE)) {
			if ((wp_enPassantMoveMask & to) == to) {
				return true;
			}
		} else if (team.equals(Team.BLACK)) {
			if ((bp_enPassantMoveMask & to) == to) {
				return true;
			}
		}

		return false;
	}

	public void resetEnpassantPosition(long pos) {
		if (isBPawn(pos) && (((pos << 8) & bp_enPassantMoveMask) == (pos << 8))) {
			bp_enPassantMoveMask = bp_enPassantMoveMask ^ pos << 8;

		} else if (isWPawn(pos)
				&& (((pos >> 8) & wp_enPassantMoveMask) == (pos >> 8))) {
			wp_enPassantMoveMask = wp_enPassantMoveMask ^ pos >> 8;

		}

	}

}
