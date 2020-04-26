package a_star;

// interface to be implemented by concrete implementation to check if cost for "wald" has to be reduced
public interface WaldCheck<T extends GraphNode> {
	double checkCostReduction(T from, T connection, T to, T prev, double currentExhaustionPoints,
			double targetScorerCost);
}
