package Board;

import java.awt.Color;
import java.awt.Dimension;

import Players.Rules;
import Players.Team;

/**
 * Here we create window and put inside a chess board. We start Two players game
 * by default as well.
 * 
 * @author Miroslav.Shilov
 * 
 */
public class ChessRoom {

	ChessBoard board;

	// Chess board fields color
	Color lightColor = new Color(235, 245, 196, 255);
	Color darkColor = new Color(130, 69, 19, 255);

	ChessRoom() {

		board = new ChessBoard(new Rules(), new Jail(Team.WHITE), new Jail(
				Team.BLACK), darkColor, lightColor);

		board.startGameTwoPlayers();

		new ChessWindow(board, new Dimension(830, 700));

	}
}
