package Board;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class represent a single chess board field(cell).This is a JPanel
 * representation for storing of piece images.By using "paint" method we are
 * able to draw points inside this cell.Further we will use these points to
 * point the all legal moves on the board, simply by clicking on a given piece.
 */
public class BoardCell extends JPanel {

	private static final long serialVersionUID = 7901462374098725907L;

	/**
	 * Use this variable as a reference for the color of the point.
	 */
	private Color color = new Color(0, 0, 0, 0);

	/**
	 * X coordinate in 2 dimensional array(chess board)
	 */
	private int x;

	/**
	 * Y coordinate in 2 dimensional array(chess board)
	 */
	private int y;

	/**
	 * The dimension of the piece icon encapsulated here.
	 */
	private final Dimension2D iconDimention = new Dimension(50, 50);

	/**
	 * Get the coordinate in Y axis
	 * 
	 * @return
	 */

	public int get_Y() {
		return this.y;
	}

	/**
	 * Get the coordinate in X axis
	 * 
	 * @return
	 */
	public int get_X() {
		return this.x;
	}

	/**
	 * This constructor build a single chess board field. Every field can be located
	 * on the chess board by its X and Y coordinate.
	 * 
	 * @param posX
	 * @param posY
	 */
	BoardCell(int posX, int posY) {
		this.x = posX;
		this.y = posY;
		this.setLayout(new FlowLayout());
	}

	/**
	 * Set the point color of a legal move. The point is centered in the board cell.
	 * 
	 * @param color
	 */
	public void setPointColor(Color color) {
		this.color = color;
	}

	/**
	 * This method draw a oval in the center of the board cell. Here is encapsulated
	 * the size and position of this oval. We use: fillOval(20, 20, 16, 16);
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(color);
		g.fillOval(20, 20, 16, 16);
	}

	/**
	 * We use this method to get the piece icon image and scale it to fit in the
	 * board cell.
	 * 
	 * @see "setIcon" method.
	 * 
	 * @param path
	 * @param dim
	 * @param scale
	 * @return
	 */
	private JLabel getScaledImage(String path, Dimension2D dim, int scale) {
		ImageIcon i = new ImageIcon(path);
		Image rI = i.getImage().getScaledInstance((int) dim.getHeight(), (int) dim.getWidth(), scale);
		JLabel l = new JLabel(new ImageIcon(rI));
		return l;
	}

	/**
	 * Use this method to set the field piece icon. By default the icon dimension is
	 * 50x50 ,icon scale 16. Every time when we use this method we must use first
	 * "clearIcon()" to clear the field.
	 * 
	 * @param path
	 */
	public void setIcon(String path) {
		this.add(getScaledImage(path, iconDimention, 16));
	}

	/**
	 * Use this to clear the field icon. We remove the zero element.
	 * "this.remove(0);"
	 */
	public void clearIcon() {
		this.remove(0);
	}

	/**
	 * 
	 * Add in to the cell a translucent image to initialize the size of that cell.We
	 * use it because if we put this cell in to container which use a layout
	 * manager, if the cell is empty ,the size of that cell will be 0x0;
	 *
	 * @param dimension
	 */
	public void initCellSize(Dimension2D dimension) {
		BufferedImage ico = new BufferedImage((int) dimension.getWidth(), (int) dimension.getHeight(),
				BufferedImage.TRANSLUCENT);
		ImageIcon icon = new ImageIcon(ico);
		this.add(new JLabel(icon));
	}
}
