package a_star;

public interface Scorer<T extends GraphNode> {
	double computeCost(T from, T to);
}
