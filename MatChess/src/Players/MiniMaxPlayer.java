package Players;

import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

import Board.ChessBoard;
import Players.Rules.MOVES_TYPE;

/**
 * 
 * Minimax (sometimes MinMax, MM[1] or saddle point[2]) is a decision rule used
 * in artificial intelligence, decision theory, game theory, statistics, and
 * philosophy for minimizing the possible loss for a worst case (maximum loss)
 * scenario. When dealing with gains, it is referred to as "maximin" – to
 * maximize the minimum gain. Originally formulated for several-player zero-sum
 * game theory, covering both the cases where players take alternate moves and
 * those where they make simultaneous moves, it has also been extended to more
 * complex games and to general decision-making in the presence of uncertainty.
 * 
 * 
 * 
 *  @see <a href="https://en.wikipedia.org/wiki/Minimax">Java Dcoumentation</a>
 * 
 * 
 * @author Miroslav.Shilov
 *
 *         !!! For properly work of this AI , the "execute" and the "undo"
 *         method must works properly.
 */
public class MiniMaxPlayer extends Player {

	private Move bestMove;

	public MiniMaxPlayer(ChessBoard board, Players.Team team) {
		super(board, team);
		this.team = team;
		search_depth = ChessBoard.search_depth;
	}

	int search_depth;
	private Team team;

	@Override
	public void mouseReleased(MouseEvent arg0) {

		if (board.getTurn().equals(team)) {
			max(search_depth, board, Integer.MIN_VALUE, Integer.MAX_VALUE);

			bestMove.execute();
			board.swapPlayers();

			board.addPieceImages();
			board.getWhitePieceJail().update(board.getSetup());
			board.getBlackPieceJail().update(board.getSetup());
			Rules.isInCheckmate(board);

		}
	}

	private int max(int depth, ChessBoard board, int alpha, int beta) {

		if (depth == 0) {
			return board.getBoardRating();
		}
		List<Move> moves = new LinkedList<Move>();
		board.getMoveProcessor().geMovesOfAllPieces(moves, MOVES_TYPE.CHECK_PREVENTED);
		for (Move move : moves) {
			// Must use the all 4vcases for pawn promotion here,to find the best move.
			move.execute();
			board.swapPlayers();
			int rating = min(depth - 1, board, alpha, beta);

			undoMove(move);

			if (rating > alpha) {
				alpha = rating;

				if (depth == search_depth) {
					bestMove = move;
				}
			}
			if (alpha >= beta) {
				return alpha;
			}
		}
		return alpha;
	}

	private int min(int depth, ChessBoard board, int alpha, int beta) {

		if (depth == 0) {
			return board.getBoardRating();
		}
		List<Move> moves = new LinkedList<Move>();
		board.getMoveProcessor().geMovesOfAllPieces(moves, MOVES_TYPE.CHECK_PREVENTED);
		for (Move move : moves) {

			move.execute();
			board.swapPlayers();
			int rating = max(depth - 1, board, alpha, beta);
			undoMove(move);

			if (rating <= beta) {
				beta = rating;
			}
			if (alpha >= beta) {
				return beta;
			}
		}
		return beta;
	}

	private void undoMove(Move move) {
		move.undo();
		board.swapPlayers();
	}

	@Override
	public void mousePressed(MouseEvent event) {

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

}
