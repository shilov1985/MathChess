package Board;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.geom.Dimension2D;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Players.Team;

/**
 * This class represent a "Jail"(JPanel) for captive pieces. The jail contain 16
 * cells.
 */
public class Jail extends JPanel {

	private static final long serialVersionUID = -5183536654568579155L;
	private final Dimension2D dim = new Dimension(50, 50);
	private List<BoardCell> jailCells;
	private Team jailTeam;
	private int iconScale = 16;
	private int inputCounter = 0;

	public Team getJailTeamColor() {
		return jailTeam;
	}

	public Jail(Team jailColor) {

		jailTeam = jailColor;
		this.setLayout(new GridLayout(2, 8, 0, 0));
		this.setOpaque(false);
		jailCells = buildJailCells(16, dim);
		addCellsToJail(this, jailCells);
	}

	private void addCellsToJail(Jail jail, List<BoardCell> jailCells2) {
		for (int i = 0; i < jailCells2.size(); i++) {
			jail.add(jailCells2.get(i));
		}
	}

	private List<BoardCell> buildJailCells(int cellsNum, Dimension2D dim) {
		List<BoardCell> jailCells = new LinkedList<BoardCell>();
		for (int i = 0; i < cellsNum; i++) {
			BoardCell cell = new BoardCell(0, 0);
			cell.initCellSize(dim);
			cell.setOpaque(false);
			jailCells.add(cell);
		}
		return jailCells;
	}

	private JLabel getScaledImage(String pathToIcon) {
		ImageIcon icon = new ImageIcon(pathToIcon);
		Image resizedImage = icon.getImage().getScaledInstance((int) dim.getHeight(), (int) dim.getWidth(), iconScale);

		JLabel label = new JLabel(new ImageIcon(resizedImage));

		return label;
	}

	/**
	 * We cannot put more than 16 pieces in a given jail.
	 * 
	 * @param path
	 * @param numberOfPieces
	 */
	private void putPiece(String path, int numberOfPieces) {
		if (inputCounter >= 0 && inputCounter <= 15) {
			for (int i = 0; i < numberOfPieces; i++) {
				jailCells.get(inputCounter).add(getScaledImage(path));
				inputCounter++;
			}
		}
	}

	private void freeUp() {
		inputCounter = 0;
		for (int i = 0; i < this.jailCells.size(); i++) {

			// try {
			Component c = this.jailCells.get(i).getComponent(0);
			if (c != null) {
				this.jailCells.get(i).remove(0);
			}

			// } catch (Exception e) {

			// e.printStackTrace();
			// }
		}
	}

	private void initFreeCells() {
		for (int i = inputCounter; i < 16; i++) {
			jailCells.get(i).initCellSize(dim);
			repaint();
			revalidate();
		}
	}

	// Every time when a Move is initiated we update the jails.Here we use a
	// method "getPieceNumber" and by this method we count the all captured pieces
	// and add its images to the jail to visualize them.
	private void updateWhiteJail(PieceSetup army) {
		int c = 0;
		c = getPieceNumber(army, army.WB);

		switch (c) {
		case 0:
			this.putPiece("whitebishop.png", 2);
			break;
		case 1:
			this.putPiece("whitebishop.png", 1);
			break;
		}

		c = getPieceNumber(army, army.WK);
		switch (c) {
		case 0:
			this.putPiece("whiteking.png", 1);
			break;
		}

		c = getPieceNumber(army, army.WN);
		switch (c) {
		case 0:
			this.putPiece("whiteknight.png", 2);
			break;
		case 1:
			this.putPiece("whiteknight.png", 1);
			break;
		}

		c = getPieceNumber(army, army.WP);

		switch (c) {
		case 7:
			this.putPiece("whitepawn.png", 1);
			break;
		case 6:
			this.putPiece("whitepawn.png", 2);
			break;
		case 5:
			this.putPiece("whitepawn.png", 3);
			break;
		case 4:
			this.putPiece("whitepawn.png", 4);
			break;
		case 3:
			this.putPiece("whitepawn.png", 5);
			break;
		case 2:
			this.putPiece("whitepawn.png", 6);
			break;
		case 1:
			this.putPiece("whitepawn.png", 7);
			break;
		case 0:
			this.putPiece("whitepawn.png", 8);
			break;

		}

		c = getPieceNumber(army, army.WQ);
		switch (c) {
		case 0:
			this.putPiece("whitequeen.png", 1);
			break;
		}

		c = getPieceNumber(army, army.WR);
		switch (c) {
		case 0:
			this.putPiece("whiterook.png", 2);
			break;
		case 1:
			this.putPiece("whiterook.png", 1);
			break;

		}

	}

	private void updateBlackJail(PieceSetup army) {

		int c = 0;

		c = getPieceNumber(army, army.BB);

		switch (c) {
		case 0:
			this.putPiece("blackbishop.png", 2);
			break;
		case 1:
			this.putPiece("blackbishop.png", 1);
			break;
		}

		c = getPieceNumber(army, army.BK);
		switch (c) {
		case 0:
			this.putPiece("blackking.png", 1);
			break;
		}

		c = getPieceNumber(army, army.BN);
		switch (c) {
		case 0:
			this.putPiece("blackknight.png", 2);
			break;
		case 1:
			this.putPiece("blackknight.png", 1);
			break;
		}

		c = getPieceNumber(army, army.BP);

		switch (c) {
		case 7:
			this.putPiece("blackpawn.png", 1);
			break;
		case 6:
			this.putPiece("blackpawn.png", 2);
			break;
		case 5:
			this.putPiece("blackpawn.png", 3);
			break;
		case 4:
			this.putPiece("blackpawn.png", 4);
			break;
		case 3:
			this.putPiece("blackpawn.png", 5);
			break;
		case 2:
			this.putPiece("blackpawn.png", 6);
			break;
		case 1:
			this.putPiece("blackpawn.png", 7);
			break;
		case 0:
			this.putPiece("blackpawn.png", 8);
			break;

		}

		c = getPieceNumber(army, army.BQ);
		switch (c) {
		case 0:
			this.putPiece("blackqueen.png", 1);
			break;
		}

		c = getPieceNumber(army, army.BR);
		switch (c) {
		case 0:
			this.putPiece("blackrook.png", 2);
			break;
		case 1:
			this.putPiece("blackrook.png", 1);
			break;

		}

	}

	// Return the number of a given piece in a board or army
	private int getPieceNumber(PieceSetup a, long p) {
		int c = 0;
		for (int i = 1; i <= 64; i++) {
			if (a.getPiece(i, p) == 1) {
				c++;
			}
		}
		return c;
	}

	/**
	 * Iterate over the army to calculate the alive pieces and according to this
	 * information update the jails images. free up all jails -> update all jails ->
	 * init. free jail cells.
	 */
	public void update(PieceSetup chessSetup) {
		if (this.getJailTeamColor().equals(Team.WHITE)) {

			this.freeUp();
			this.updateWhiteJail(chessSetup);
			this.initFreeCells();

		} else if (this.getJailTeamColor().equals(Team.BLACK)) {

			this.freeUp();
			this.updateBlackJail(chessSetup);
			this.initFreeCells();

		}

	}

}
