package a_star;

// interface to be implemented to calculate distance to next node and distance to goal
public interface Scorer<T extends GraphNode> {
	ScorerResult computeCost(T from, T to, double currentExhaustionPoints);
}
