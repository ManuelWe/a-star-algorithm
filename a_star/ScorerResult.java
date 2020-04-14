package a_star;

// Class used to return cost and exhaustion points from scorer implementations
public class ScorerResult {
	private double heuristic = 0;
	private double exhaustionPoints = 0;

	public double getCost() {
		return heuristic;
	}

	public void setCost(double heuristic) {
		this.heuristic = heuristic;
	}

	public double getExhaustionPoints() {
		return exhaustionPoints;
	}

	public void setExhaustionPoints(double exhaustionPoints) {
		this.exhaustionPoints = exhaustionPoints;
	}
}
