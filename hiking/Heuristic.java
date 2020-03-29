package hiking;

import a_star.Scorer;

public class Heuristic implements Scorer<Square> {
	@Override
	public double computeCost(Square from, Square to) {
		return Math.hypot(from.getX() - to.getX(), from.getY() - to.getY());
	}
}
