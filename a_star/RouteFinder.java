package a_star;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class RouteFinder<T extends GraphNode> {
	private final Graph<T> graph;
	private final Scorer<T> nextNodeScorer;
	private final Scorer<T> targetScorer;
	private Queue<RouteNode> openSet = new PriorityQueue<>(); // TODO move into findRoute
	public int nodesChecked = 0; // TODO removed
	private double currentExhaustionPoints = 0;
	private HeuristicResult heuristicResult = new HeuristicResult();

	public RouteFinder(Graph<T> graph, Scorer<T> nextNodeScorer, Scorer<T> targetScorer) {
		this.graph = graph;
		this.nextNodeScorer = nextNodeScorer;
		this.targetScorer = targetScorer;
	}

	public List<T> findRoute(T from, T to) {
		Map<T, RouteNode<T>> allNodes = new HashMap<>();
		heuristicResult = targetScorer.computeCost(from, to, currentExhaustionPoints);
		currentExhaustionPoints = heuristicResult.getExhaustionPoints();
		RouteNode<T> start = new RouteNode<>(from, null, 0d, heuristicResult.getHeuristic());
		start.setExhaustionPoints(currentExhaustionPoints);
		allNodes.put(from, start);
		openSet.add(start);

		while (!openSet.isEmpty()) {
			nodesChecked++;
			// System.out.println(
			// "Open Set contains: " +
			// openSet.stream().map(RouteNode::getCurrent).collect(Collectors.toSet()));
			RouteNode<T> next = openSet.poll();
			// System.out.println("Looking at node: " + next);
			if (next.getCurrent().equals(to)) {
				System.out.println("Found our destination!");

				List<T> route = new ArrayList<>();
				RouteNode<T> current = next;
				List<String> exhP = new ArrayList<>(); // TODO remove
				int breaks = 0;
				do {
					route.add(0, current.getCurrent());
					if (current.getBreaks() == 1) {
						exhP.add(0, "B" + current.getExhaustionPoints());
					} else {
						exhP.add(0, current.getExhaustionPoints() + "");
					}
					breaks += current.getBreaks();
					current = allNodes.get(current.getPrevious());
				} while (current != null);

				System.out.println("Route: " + route);
				System.out.println("Breaks: " + breaks);
				System.out.println("Exhaustion Points: " + exhP);
				return route;
			}

			graph.getConnections(next.getCurrent()).forEach(connection -> {
				heuristicResult = nextNodeScorer.computeCost(next.getCurrent(), connection, next.getExhaustionPoints());
				// currentExhaustionPoints = heuristicResult.getExhaustionPoints();
				double newScore = next.getRouteScore() + heuristicResult.getHeuristic();
				RouteNode<T> nextNode = allNodes.getOrDefault(connection, new RouteNode<>(connection));
				allNodes.put(connection, nextNode);

				if (nextNode.getRouteScore() > newScore) {
					nextNode.setPrevious(next.getCurrent());
					nextNode.setRouteScore(newScore);
					heuristicResult = targetScorer.computeCost(connection, to,
							allNodes.get(next.getCurrent()).getExhaustionPoints());
					currentExhaustionPoints = heuristicResult.getExhaustionPoints();
					nextNode.setExhaustionPoints(currentExhaustionPoints);
					nextNode.setEstimatedScore(newScore + heuristicResult.getHeuristic());
					openSet.add(nextNode);
					// System.out.println("Found a better route to node: " + nextNode);
				}
			});
		}

		throw new IllegalStateException("No route found");
	}

	public Queue<RouteNode> getOpenSet() { // TODO: remove
		return openSet;
	}

}
