package hiking;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

import a_star.Graph;

// helper class used to draw the graph with the route
public class DrawGraph extends JPanel {
	private List<String> route;
	private Graph<Square> hikingGraph;
	private String[][] field;

	public DrawGraph(List<String> route, Graph<Square> hikingGraph, String[][] field) {
		this.route = route;
		this.hikingGraph = hikingGraph;
		this.field = field;
	}

	@Override
	public void paint(Graphics g) {
		final int squareWidth = 30;
		final int squareHeight = 30;
		String idString = null;
		char ch = 'A';

		g.setColor(Color.white);
		g.fillRect(0, 0, field.length * squareWidth + 60, field[0].length * squareHeight + 60);
		g.setColor(Color.BLACK);

		for (int y = 0; y < field[0].length + 1; y++) {
			if (y != 0) {
				g.drawString(Integer.toString(y), 5, y * squareHeight + squareHeight / 2);
			}
			for (int x = 0; x < field.length; x++) {
				idString = Integer.toString(x * field[0].length + y - 1);
				if (y == 0) {
					g.drawString(Character.toString(ch), x * squareWidth + squareWidth + squareWidth / 2,
							y * squareHeight + 20);
				} else {
					if (route.contains(idString)) {
						g.setColor(Color.RED);
					} else {
						switch (hikingGraph.getNode(idString).getTerrain().getCode()) {
						case 0:
							g.setColor(new Color(147, 144, 14));
							break;
						case 1:
							g.setColor(Color.BLUE);
							break;
						case 2:
							g.setColor(Color.LIGHT_GRAY);
							break;
						case 3:
							g.setColor(new Color(78, 116, 0));
							break;
						case 4:
							g.setColor(new Color(252, 255, 238));
							break;
						case 5:
							g.setColor(new Color(86, 79, 73));
							break;
						}
					}
					g.fillRect(x * squareWidth + 30, y * squareHeight, squareWidth, squareHeight);
					g.setColor(Color.BLACK);
					g.drawString(Integer.toString(hikingGraph.getNode(idString).getTerrain().getCode()),
							x * squareWidth + squareWidth / 2 + 30, y * squareHeight + squareHeight / 2);
				}
				ch++;
			}
		}
	}

}
