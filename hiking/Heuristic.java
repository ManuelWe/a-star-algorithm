package hiking;

import a_star.HeuristicResult;
import a_star.Scorer;

public class Heuristic implements Scorer<Square> {
	private HeuristicResult result = new HeuristicResult();

	@Override
	public HeuristicResult computeCost(Square from, Square to, double currentExhaustionPoints) {
		if (from.getTerrain().getCode() == 3) { // Wald
			result.setHeuristic(
					Math.hypot(from.getX() - to.getX(), from.getY() - to.getY()) * 2 - currentExhaustionPoints / 2);
		}
		if (from.getTerrain().getCode() == 5) { // Felswand
			result.setHeuristic(Math.hypot(from.getX() - to.getX(), from.getY() - to.getY()) * 2 + 3 / 2);
		} else if (from.getTerrain().getCode() == 1) { // Fluss
			result.setHeuristic(Math.hypot(from.getX() - to.getX(), from.getY() - to.getY()) * 2 + 4 / 2);
		}
		result.setExhaustionPoints(from.getTerrain().computeExhaustionPoints(currentExhaustionPoints));
		return result;
	}
}
