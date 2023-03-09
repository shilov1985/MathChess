package Board;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Dimension2D;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class ChessWindow extends JFrame {

	/**
	 * This class represent the game window.We use GridBagLayout here and can set
	 * background picture also.
	 */
	private JMenuBar menuBar;
	private static final long serialVersionUID = -6978538842172122427L;
	private GridBagLayout layout;
	private GridBagConstraints constraints;
	private String backgroundPath = "backgroung.jpg";

	public ChessWindow(final ChessBoard board, Dimension dimension) {

		super("Chess the game");

		board.setWindow(this);
		setContentPane(getScaledImage(backgroundPath, dimension, 16));
		layout = new GridBagLayout();
		constraints = new GridBagConstraints();
		setLayout(layout);
		setSize(dimension.height, dimension.width);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);

		this.menuBar = new JMenuBar();

		JMenu gameMenu = new JMenu("Game");
		this.menuBar.add(gameMenu);

		JMenuItem newGameTwoPlayersItem = new JMenuItem("New game-2 players");

		newGameTwoPlayersItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				board.startGameTwoPlayers();
			}
		});
		gameMenu.add(newGameTwoPlayersItem);
		JMenuItem newGameVSComputerItem = new JMenuItem("New game VS computer");

		newGameVSComputerItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				board.startGameAgainstComputer();
			}
		});
		gameMenu.add(newGameVSComputerItem);
		JMenuItem difficultItem = new JMenuItem("Set the difficult level");

		difficultItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				new DifficultLevelWindow(board);

			}
		});

		gameMenu.add(difficultItem);
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);

			}
		});

		gameMenu.add(exitMenuItem);

		JMenu aboutMenu = new JMenu("About");

		JMenuItem aboutMenuItem = new JMenuItem("This program");
		aboutMenu.add(aboutMenuItem);
		aboutMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				new AboutWindow();

			}
		});

		this.menuBar.add(aboutMenu);

		setJMenuBar(menuBar);

		add(board.getWhitePieceJail(), 0, 1, GridBagConstraints.BOTH);

		add(board, 0, 2, GridBagConstraints.CENTER);

		add(board.getBlackPieceJail(), 0, 3, GridBagConstraints.BOTH);

		setVisible(true);
	}

	private void add(Component comp, int gridx, int gridy, int gridBag) {

		constraints.gridx = gridx;
		constraints.gridy = gridy;
		constraints.fill = gridBag;
		add(comp, constraints);
		setVisible(true);

	}

	private JLabel getScaledImage(String path, Dimension2D dim, int iconScale) {

		ImageIcon icon = new ImageIcon(path);
		Image resizedImage = icon.getImage().getScaledInstance((int) dim.getHeight(), (int) dim.getWidth(), iconScale);

		JLabel label = new JLabel(new ImageIcon(resizedImage));

		return label;

	}

	private class DifficultLevelWindow extends JFrame {
		/**
		 * 
		 */
		private static final long serialVersionUID = -8698701805330076027L;
		JSlider slider;

		public DifficultLevelWindow(ChessBoard board) {

			board.getChessWindow().setEnabled(false);

			slider = new JSlider(JSlider.HORIZONTAL, 1, 5, ChessBoard.search_depth);
			JButton okBtn = new JButton("Ok");
			okBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					int level = slider.getValue();
					dispose();
					ChessBoard.search_depth = level;
					board.getChessWindow().setEnabled(true);
				}

			});

			slider.setMinorTickSpacing(1);
			slider.setMajorTickSpacing(1);
			slider.setPaintTicks(true);
			slider.setPaintLabels(true);
			setLayout(new FlowLayout());
			JPanel panel = new JPanel();
			panel.add(slider);
			add(panel);
			add(okBtn);
			setVisible(true);
			setResizable(false);
			setLocationRelativeTo(board);
			this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			pack();
		}

	}

	private class AboutWindow extends JFrame {

		/**
		 * 
		 */
		private static final long serialVersionUID = -790613743336124892L;

		AboutWindow() {
			super("About this program");
			setSize(300, 320);
			setLocationRelativeTo(null);
			setLayout(null);
			JPanel panel = new JPanel();
			panel.setLayout(null);
			panel.setBounds(0, 0, 300, 300);
			setContentPane(panel);
			panel.setBackground(new Color(130, 69, 19, 255));
			panel.setBorder(BorderFactory.createRaisedBevelBorder());
			JLabel author = new JLabel("Miroslav Shilov");
			author.setFont(new Font("Serif", Font.BOLD, 19));
			author.setForeground(new Color(215, 255, 196, 255));
			author.setBounds(75, 0, 300, 50);
			setResizable(false);
			JLabel country = new JLabel("Bulgaria");
			country.setFont(new Font("Serif", Font.BOLD, 15));
			country.setForeground(Color.white);
			country.setBounds(110, 25, 300, 50);

			JLabel movesColor = new JLabel("move colors:");
			movesColor.setFont(new Font("Serif", Font.BOLD, 17));
			movesColor.setForeground(new Color(235, 245, 196, 255));
			movesColor.setBounds(20, 50, 300, 50);

			JLabel explanation = new JLabel(
					"<html>attack move<br><br>free move<br><br>en passant<br><br>castling<br><br>promotion</html>");
			explanation.setFont(new Font("Serif", Font.BOLD, 13));
			explanation.setForeground(new Color(235, 245, 196, 255));
			explanation.setBounds(80, 80, 300, 180);

			panel.add(author);
			panel.add(country);
			panel.add(movesColor);
			panel.add(explanation);

			setVisible(true);
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			g.setColor(Color.red);
			g.fillOval(50, 124, 18, 18);
			g.setColor(Color.green);
			g.fillOval(50, 158, 18, 18);
			g.setColor(Color.cyan);
			g.fillOval(50, 195, 18, 18);
			g.setColor(Color.orange);
			g.fillOval(50, 230, 18, 18);
			g.setColor(Color.yellow);
			g.fillOval(50, 265, 18, 18);
			g.setColor(new Color(235, 245, 196, 255));
			g.drawLine(20, 95, 275, 95);
			g.drawLine(30, 282, 30, 125);
		}

	}
}
