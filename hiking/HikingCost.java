package hiking;

import a_star.HeuristicResult;
import a_star.Scorer;

public class HikingCost implements Scorer<Square> {
	private HeuristicResult result = new HeuristicResult();
	private int breaks = 0;

	@Override
	public HeuristicResult computeCost(Square from, Square to, double currentExhaustionPoints) {
		result.setHeuristic(to.getTerrain().getCost());

		if (currentExhaustionPoints >= 10) {
			currentExhaustionPoints -= 10;
			result.setHeuristic(to.getTerrain().getCost() + 5);
			breaks++;
		}
		result.setExhaustionPoints(currentExhaustionPoints);
		return result;
	}

	public int getBreaks() {
		return breaks;
	}
}
