package test;

import static java.time.Duration.ofSeconds;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import hiking.Main;
import hiking.Square;

public class TestMaps {
	private List<String> fileNames = new ArrayList<String>(
			Arrays.asList("CircleMap", "FelsFront", "MinReduceWald", "VerfrühtesAbbiegenaufWald",
					"VerfrühtesAbbiegenaufWaldForce", "WaldErholung", "WaldRealErholung", "WaldVsFels", "WaldVsFels2"));

	private List<List<String>> correctPaths = new ArrayList<List<String>>(Arrays.asList(
			new ArrayList<String>(
					Arrays.asList("59", "58", "57", "56", "71", "86", "101", "116", "131", "146", "145", "144", "159",
							"174", "173", "172", "171", "170", "169", "168", "167", "166", "181", "196", "211", "210")),
			new ArrayList<String>(Arrays.asList("156", "141", "126", "125", "124", "123", "122", "121", "120", "135",
					"150", "165", "180", "195", "210")),
			new ArrayList<String>(Arrays.asList("14", "29", "28", "27", "26")),
			new ArrayList<String>(Arrays.asList("70", "69", "84", "99", "114")),
			new ArrayList<String>(Arrays.asList("70", "85", "84", "99", "114")),
			new ArrayList<String>(Arrays.asList("70", "69", "68", "67", "66", "65", "64")),
			new ArrayList<String>(Arrays.asList("70", "69", "68", "67", "66", "65", "80")),
			new ArrayList<String>(Arrays.asList("70", "85", "100", "99")),
			new ArrayList<String>(Arrays.asList("70", "85", "100", "99"))));

	@TestFactory
	public Iterator<DynamicTest> testExampleMaps() {
		return fileNames.stream().map(fileName -> DynamicTest.dynamicTest("Test " + fileName, () -> {
			Main main = new Main();
			main.parseCSV("./testExamples/" + fileName + ".csv");
			try {
				main.setUp();
			} catch (Exception e) {
				e.printStackTrace();
			}

			assertTimeoutPreemptively(ofSeconds(3), () -> {
				List<Square> route = main.routeFinder.findRoute(main.hikingGraph.getNode(main.getStartNodeId()),
						main.hikingGraph.getNode(main.getEndNodeId()));
				List<String> routeCompact = route.stream().map(Square::getId).collect(Collectors.toList());
				assertEquals(fileName, correctPaths.get(fileNames.indexOf(fileName)), routeCompact);
			});
		})).iterator();
	}
}
