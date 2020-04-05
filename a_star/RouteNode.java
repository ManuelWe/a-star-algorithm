package a_star;

import java.util.StringJoiner;

public class RouteNode<T extends GraphNode> implements Comparable<RouteNode> { // TODO remove public
	private final T current;
	private T previous;
	private double routeScore;
	private double estimatedScore;
	private double exhaustionPoints = 0; // exhaustionPoints up to this node
	private int breaks = 0; // up to this node

	RouteNode(T current) {
		this(current, null, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
	}

	RouteNode(T current, T previous, double routeScore, double estimatedScore) {
		this.current = current;
		this.previous = previous;
		this.routeScore = routeScore;
		this.estimatedScore = estimatedScore;
	}

	public T getCurrent() { // TODO remove public
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

	int getBreaks() {
		return breaks;
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
		if (exhaustionPoints >= 10) {
			exhaustionPoints -= 10;
			breaks++;
		}
		this.exhaustionPoints = exhaustionPoints;
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
				.add("previous=" + previous).add("routeScore=" + routeScore).add("estimatedScore=" + estimatedScore)
				.toString();
	}
}
