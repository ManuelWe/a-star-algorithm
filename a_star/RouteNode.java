package a_star;

import java.util.StringJoiner;

// Object to save node information during search
public class RouteNode<T extends GraphNode> implements Comparable<RouteNode> { // TODO remove public
	private final T current;
	private T previous;
	private double routeScore; // g score up to this node
	private double estimatedScore; // f score up to this node
	private double exhaustionPoints = 0; // exhaustionPoints up to this node
	private boolean exhaustionBreak = false; // true if a break was taken on this route node

	RouteNode(T current) {
		this(current, null, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
	}

	RouteNode(T current, T previous, double routeScore, double estimatedScore) {
		this.current = current;
		this.previous = previous;
		this.routeScore = routeScore;
		this.estimatedScore = estimatedScore;
	}

	T getCurrent() {
		return current;
	}

	T getPrevious() {
		return previous;
	}

	double getRouteScore() {
		return routeScore;
	}

	double getEstimatedScore() {
		return estimatedScore;
	}

	double getExhaustionPoints() {
		return exhaustionPoints;
	}

	boolean hasBreak() {
		return exhaustionBreak;
	}

	void setPrevious(T previous) {
		this.previous = previous;
	}

	void setRouteScore(double routeScore) {
		this.routeScore = routeScore;
	}

	void setEstimatedScore(double estimatedScore) {
		this.estimatedScore = estimatedScore;
	}

	void setExhaustionPoints(double exhaustionPoints) {
		this.exhaustionPoints = exhaustionPoints;
	}

	void setBreak(boolean exhaustionBreak) {
		this.exhaustionBreak = exhaustionBreak;
	}

	@Override
	public int compareTo(RouteNode other) {
		if (this.estimatedScore > other.estimatedScore) {
			return 1;
		} else if (this.estimatedScore < other.estimatedScore) {
			return -1;
		} else {
			return 0;
		}
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", RouteNode.class.getSimpleName() + "[", "]").add("current=" + current)
				.add("previous=" + previous).add("GScore=" + routeScore).add("estimatedScore=" + estimatedScore)
				.toString();
	}
}
