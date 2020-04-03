package a_star;

public interface Scorer<T extends GraphNode> {
	HeuristicResult computeCost(T from, T to, double currentExhaustionPoints);
}
