package Players;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import Board.BoardCell;
import Board.ChessBoard;
import Board.PieceSetup;

public abstract class Player implements MouseListener {
	protected Team team;
	protected ChessBoard board;
	protected Rules rules;

	protected PieceSetup army;
	protected BoardCell cell;

	Player(ChessBoard board, Team team) {
		this.team = team;
		this.board = board;
		army = board.getSetup();
		rules = board.getRules();
	}

	@Override
	public void mousePressed(MouseEvent event) {
		if (event.getSource().getClass().equals(BoardCell.class)) {
			cell = (BoardCell) event.getSource();
		}
	}

	@Override
	public void mouseReleased(MouseEvent event) {

	}

}
