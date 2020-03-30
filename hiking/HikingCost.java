package hiking;

import a_star.Scorer;

public class HikingCost implements Scorer<Square> {
	@Override
	public double computeCost(Square from, Square to) {
		return to.getTerrain().getCost();
	}
}
