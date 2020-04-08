package hiking;

import java.util.ArrayList;

import a_star.HeuristicResult;
import a_star.Scorer;

public class Heuristic implements Scorer<Square> {
	private HeuristicResult result = new HeuristicResult();
	private double multiplicator;

	public Heuristic(ArrayList<Terrain> terrains) {
		int lowestTerrainCost = Integer.MAX_VALUE;
		for (int i = 0; i < terrains.size(); i++) {
			if (lowestTerrainCost > terrains.get(i).getCost()) {
				lowestTerrainCost = terrains.get(i).getCost();
			}
		}

		// find multiplicator, so that heuristic is optimistic
		if (lowestTerrainCost + 3 / 2 < terrains.get(5).getCost()
				&& lowestTerrainCost + 4 / 2 < terrains.get(1).getCost()) {
			multiplicator = lowestTerrainCost;
		} else {
			if (terrains.get(5).getCost() - 3 / 2 > terrains.get(1).getCost() - 4 / 2) {
				multiplicator = terrains.get(1).getCost() - 4 / 2 - 0.1;
			} else {
				multiplicator = terrains.get(5).getCost() - 3 / 2 - 0.1;
			}
		}
	}

	@Override
	public HeuristicResult computeCost(Square from, Square to, double currentExhaustionPoints) {
		if (from.getTerrain().getCode() == 3) { // Wald
			result.setHeuristic(Math.hypot(from.getX() - to.getX(), from.getY() - to.getY()) * multiplicator
					- currentExhaustionPoints / 2);
			result.setExhaustionPoints(currentExhaustionPoints / 2);
		} else if (from.getTerrain().getCode() == 5) { // Felswand
			result.setHeuristic(Math.hypot(from.getX() - to.getX(), from.getY() - to.getY()) * multiplicator + 3 / 2);
			result.setExhaustionPoints(currentExhaustionPoints + 3);
		} else if (from.getTerrain().getCode() == 1) { // Fluss
			result.setHeuristic(Math.hypot(from.getX() - to.getX(), from.getY() - to.getY()) * multiplicator + 4 / 2);
			result.setExhaustionPoints(currentExhaustionPoints + 4);
		} else {
			result.setHeuristic(Math.hypot(from.getX() - to.getX(), from.getY() - to.getY()) * multiplicator);
			result.setExhaustionPoints(currentExhaustionPoints);
		}

		return result;
	}
}
