package hiking;

import java.util.ArrayList;

import a_star.Scorer;
import a_star.ScorerResult;

// implementation of Scorer interface to calculate estimated cost to destination 
public class Heuristic implements Scorer<Square> {
	private ScorerResult result = new ScorerResult();
	private int lowestTerrainCost = Integer.MAX_VALUE;;

	public Heuristic(ArrayList<Terrain> terrains) {
		// find lowest terrain cost
		for (int i = 0; i < terrains.size(); i++) {
			if (lowestTerrainCost > terrains.get(i).getCost()) {
				lowestTerrainCost = terrains.get(i).getCost();
			}
		}
	}

	@Override
	public ScorerResult computeCost(Square from, Square to, double currentExhaustionPoints) {
		// taxicab norm times lowest terrain cost equals the cost to destination
		result.setCost(Math.abs(from.getX() - to.getX()) + Math.abs(from.getY() - to.getY()) * lowestTerrainCost);

		return result;
	}
}
