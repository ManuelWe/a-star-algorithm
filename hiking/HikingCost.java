package hiking;

import a_star.Scorer;
import a_star.ScorerResult;

// implementation of Scorer to calculate cost to next node
public class HikingCost implements Scorer<Square> {
	private ScorerResult result = new ScorerResult();

	@Override
	public ScorerResult computeCost(Square from, Square to, double currentExhaustionPoints) {
		result.setCost(from.getTerrain().getCost());

		if (from.getTerrain().getCode() == 3) { // Wald
			result.setExhaustionPoints(currentExhaustionPoints / 2.0);
		} else if (from.getTerrain().getCode() == 5) { // Felswand
			result.setExhaustionPoints(currentExhaustionPoints + 3);
		} else if (from.getTerrain().getCode() == 1) { // Fluss
			result.setExhaustionPoints(currentExhaustionPoints + 4);
		} else {
			result.setExhaustionPoints(currentExhaustionPoints);
		}

		if (result.getExhaustionPoints() >= 10) {
			result.setExhaustionPoints(result.getExhaustionPoints() - 10);
			result.setCost(from.getTerrain().getCost() + 5);
		}

		return result;
	}
}
