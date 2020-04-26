package hiking;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JFrame;

import a_star.Graph;
import a_star.RouteFinder;

public class Main {
	public Graph<Square> hikingGraph;
	public RouteFinder<Square> routeFinder;
	private ArrayList<Terrain> terrains = new ArrayList<Terrain>();
	private String[][] field;
	private HikingCost hikingCost = new HikingCost();
	private int startNodeId;
	private int endNodeId;

	// parse csv for field, terrain info and start and end node ids
	public void parseCSV(String pathToCSV) {
		File csvFile = new File(pathToCSV);

		if (csvFile.isFile()) {
			BufferedReader csvReader = null;
			int fieldWidth = 0;
			int fieldHeight = 0;

			try {
				csvReader = new BufferedReader(new FileReader(pathToCSV));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}

			try {
				fieldWidth = csvReader.readLine().split(";").length;
				fieldHeight++;
				while (csvReader.readLine().split(";").length != 0) {
					fieldHeight++;
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			try {
				csvReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			field = new String[fieldWidth][fieldHeight];
			try {
				csvReader = new BufferedReader(new FileReader(pathToCSV));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}

			try {
				for (int i = 0; i < fieldHeight; i++) {
					String[] data = csvReader.readLine().split(";");
					for (int j = 0; j < data.length; j++) {
						field[j][i] = data[j];
					}
				}
				csvReader.readLine(); // skip empty row
				csvReader.readLine(); // skip header row
				String row = null;
				while ((row = csvReader.readLine()) != null) {
					String[] data = row.split(";");
					if (data.length != 0) {
						Terrain terrain = new Terrain(Integer.parseInt(data[0]), data[1], Integer.parseInt(data[2]));
						terrains.add(terrain);
					}
					if (terrains.size() == 1) { // row with startNode coordinates
						if (data.length > 4) { // check, if coordinates are provided
							startNodeId = Integer.parseInt(data[4]) * fieldHeight + Integer.parseInt(data[5]);
						} else {
							// if no coordinates, use default from requirements (X=2,Y=14)
							startNodeId = 2 * fieldHeight + 14;
						}
					}
					if (terrains.size() == 2) { // row with endNode coordinates
						if (data.length > 4) { // check, if coordinates are provided
							endNodeId = Integer.parseInt(data[4]) * fieldHeight + Integer.parseInt(data[5]);
						} else {
							// if no coordinates, use default from requirements (X=9,Y=1)
							endNodeId = 9 * fieldHeight + 1;
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				csvReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("File \"" + pathToCSV + "\" can not be found. Please add it to the projects root dir.");
			System.exit(0);
		}
	}

	public String getStartNodeId() {
		return Integer.toString(startNodeId);
	}

	public String getEndNodeId() {
		return Integer.toString(endNodeId);
	}

	// construct graph with nodes and connections from field
	public void setUp() throws Exception {
		Set<Square> squares = new HashSet<>();
		Set<String> squareNeighbours = new HashSet<>();
		Map<String, Set<String>> connections = new HashMap<>();
		int squareID = 0;
		String idString = null;

		for (int x = 0; x < field.length; x++) {
			for (int y = 0; y < field[x].length; y++) {
				idString = Integer.toString(squareID);
				squares.add(new Square(idString, x, y, terrains.get(Integer.parseInt(field[x][y]))));

				if (x > 0) {
					squareNeighbours.add(Integer.toString(squareID - field[x].length));
				}
				if (y > 0) {
					squareNeighbours.add(Integer.toString(squareID - 1));
				}
				if (x < field.length - 1) {
					squareNeighbours.add(Integer.toString(squareID + field[x].length));
				}
				if (y < field[x].length - 1) {
					squareNeighbours.add(Integer.toString(squareID + 1));
				}

				connections.put(idString, squareNeighbours);
				squareNeighbours = new HashSet<>();
				squareID++;
			}
		}

		hikingGraph = new Graph<>(squares, connections);
		routeFinder = new RouteFinder<>(hikingGraph, hikingCost, new Heuristic(terrains),
				new WaldCheckImplementation(terrains));
	}

	public void printGraph(List<String> route) {
		String idString = null;
		char ch = 'A';

		System.out.println("");
		for (int y = 0; y < field[0].length + 1; y++) {
			if (y == 0) {
				System.out.printf("%-4s", "");
			} else {
				System.out.printf("%-4s", y);
			}
			for (int x = 0; x < field.length; x++) {
				idString = Integer.toString(x * field[0].length + y - 1);
				if (y == 0) {
					System.out.printf("%-4s", ch);
				} else {
					if (route.contains(idString)) {
						System.out.printf("%-4s", "X");
					} else {
						System.out.printf("%-4s", hikingGraph.getNode(idString).getTerrain().getCode());
					}
				}
				ch++;
			}
			System.out.println("");
		}
	}

	public int computeRouteCost(List<Square> route) {
		int cost = 0;
		for (int i = 0; i < route.size() - 1; i++) {
			cost += route.get(i).getTerrain().getCost();
		}
		return cost;
	}

	public static void main(String[] args) {
		Main main = new Main();
		main.parseCSV("./S_001_DatenTest.csv");
		try {
			main.setUp();
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<Square> route = main.routeFinder.findRoute(main.hikingGraph.getNode(main.getStartNodeId()),
				main.hikingGraph.getNode(main.getEndNodeId()));
		List<String> routeCompact = route.stream().map(Square::getId).collect(Collectors.toList());

		// draw graph
		JFrame frame = new JFrame();
		frame.setSize(main.field.length * 30 + 75, main.field[0].length * 30 + 90);
		frame.getContentPane().add(new DrawGraph(routeCompact, main.hikingGraph, main.field));
		frame.setLocationRelativeTo(null);
		frame.setBackground(Color.LIGHT_GRAY);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		main.printGraph(routeCompact);
		System.out.println("\nRoute cost (without breaks): " + main.computeRouteCost(route));
		System.out.println("Examined Nodes: " + main.routeFinder.getNodesChecked());
	}
}
