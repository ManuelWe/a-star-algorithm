package a_star;

public class HeuristicResult {
	private double heuristic = 0;
	private double exhaustionPoints = 0;

	public double getHeuristic() {
		return heuristic;
	}

	public void setHeuristic(double heuristic) {
		this.heuristic = heuristic;
	}

	public double getExhaustionPoints() {
		return exhaustionPoints;
	}

	public void setExhaustionPoints(double exhaustionPoints) {
		this.exhaustionPoints = exhaustionPoints;
	}
}
