package Players;

import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;
import Board.ChessBoard;
import Players.Move.MOVE_PURPOSE;
import Players.Rules.MOVES_TYPE;
import Players.Rules.PawnRules.PiecePromoter;

/*
 * This class represent a human player.We put as a arguments the entire chess board and  a team also.
 * By default the first turn is for the white player - human player.When we click on some piece the 
 * all legal moves is computed by the "LegalMoveProcessor" and stored in to List<Move>.After that we iterate over
 * these moves for legal ones according to our destination position. 
 * Important: The all legal moves is pointed (colored) by the "LegalMoveProcessor" class.
 */
public class HumanPlayer extends Player {

	private List<Move> moves;

	public HumanPlayer(ChessBoard board, Team team) {
		super(board, team);
		this.team = team;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent event) {
		// TODO Auto-generated method stub
		super.mousePressed(event);

		if (this.team.equals(board.getTurn())) {

			if (moves == null) {

				// Here reset the color of all cells.It is necessary because of
				// legal move pointing by the "LegalMoveProcessor".
				board.paintBoardCells();
				moves = new LinkedList<Move>();

				board.getMoveProcessor().getMovesForOnePiece(moves,
						cell.get_X(), cell.get_Y(), true, MOVES_TYPE.CHECK_PREVENTED);

				// If there is legal moves proceed further.
			} else if (moves != null) {

				// If destination position of some move match with the
				// destination
				// position that we get when click on some board cell, then we
				// execute that move.
				long dest = board.TO_BINARY_POSITIONS[cell.get_X()][cell.get_Y()];

				for (Move m : moves) {

					if (m.getDestPos() == dest) {

						boolean isCapture = m.execute();

						if (m.getMovePurpose().equals(MOVE_PURPOSE.PROMOTION)) {
							PiecePromoter.openPromotionWindow(dest, board);
						}

						Rules.updateFiftyMovesRuleCounter(board, isCapture);

						board.swapPlayers();

						board.addPieceImages();
						board.getWhitePieceJail().update(board.getSetup());
						board.getBlackPieceJail().update(board.getSetup());

						Rules.isInCheckmate(board);

					}
				}

				board.paintBoardCells();

				moves = null;

			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
