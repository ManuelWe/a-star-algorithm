package hiking;

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

import a_star.Graph;
import a_star.RouteFinder;

public class Main {
	private Graph<Square> hikingGraph;
	private RouteFinder<Square> routeFinder;
	private ArrayList<Terrain> terrains = new ArrayList<Terrain>();
	private String[][] field;

	public void parseCSV(int fieldHeight, int fieldWidth) {
		String pathToCSV = "./S_001_Daten.csv";
		File csvFile = new File(pathToCSV);

		if (csvFile.isFile()) {
			BufferedReader csvReader = null;
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
			System.out.println("File \"S_001_Daten.csv\" can not be found. Please add it to the projects root dir.");
			System.exit(0);
		}
	}

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
		routeFinder = new RouteFinder<>(hikingGraph, new Heuristic(), new Heuristic());
	}

	public void printGraph(List<String> route) {
		String idString = null;

		System.out.println("");
		for (int y = 0; y < field.length; y++) {
			for (int x = 0; x < field[y].length; x++) {
				idString = Integer.toString(x * field.length + y);
				if (route.contains(idString)) {
					System.out.printf("%-2s", "X");
				} else {
					System.out.printf("%-2s", hikingGraph.getNode(idString).getTerrain().getCode());
				}
			}
			System.out.println("");
		}
	}

	public static void main(String[] args) {
		Main main = new Main();
		main.parseCSV(15, 15);
		try {
			main.setUp();
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<Square> route = main.routeFinder.findRoute(main.hikingGraph.getNode("44"),
				main.hikingGraph.getNode("136"));

		List<String> routeCompact = route.stream().map(Square::getId).collect(Collectors.toList());
		System.out.println(routeCompact);
		main.printGraph(routeCompact);
	}
}
