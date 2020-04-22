package a_star;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;

// a star search algorithm adapted from https://en.wikipedia.org/wiki/A*_search_algorithm#Pseudocode
public class RouteFinder<T extends GraphNode> {
	private final Graph<T> graph;
	private final Scorer<T> nextNodeScorer;
	private final Scorer<T> targetScorer;
	private Queue<RouteNode> openSet = new PriorityQueue<>(); // TODO move into findRoute
	public int nodesChecked = 0; // TODO removed
	private double currentExhaustionPoints = 0;
	private ScorerResult scorerResult = new ScorerResult();

	public RouteFinder(Graph<T> graph, Scorer<T> nextNodeScorer, Scorer<T> targetScorer) {
		this.graph = graph;
		this.nextNodeScorer = nextNodeScorer;
		this.targetScorer = targetScorer;
	}

	public List<T> findRoute(T from, T to) {
		Map<T, RouteNode<T>> allNodes = new HashMap<>();
		scorerResult = targetScorer.computeCost(from, to, currentExhaustionPoints);
		currentExhaustionPoints = scorerResult.getExhaustionPoints();
		RouteNode<T> start = new RouteNode<>(from, null, 0d, scorerResult.getCost());
		start.setExhaustionPoints(currentExhaustionPoints);
		allNodes.put(from, start);
		openSet.add(start);

		while (!openSet.isEmpty()) {
			nodesChecked++;
			System.out.println(
					"Open Set contains: " + openSet.stream().map(RouteNode::getCurrent).collect(Collectors.toSet()));
			RouteNode<T> next = openSet.poll();
//			if (nodesChecked > 1) {
//				if(allNodes.get(next.getPrevious()).get)
//				next.setExhaustionPoints(2);
//			}
			System.out.println("Looking at node: " + next);
			if (next.getCurrent().equals(to)) {
				System.out.println("Found our destination!");

				List<T> route = new ArrayList<>();
				RouteNode<T> current = next;
				List<String> exhP = new ArrayList<>(); // TODO remove
				int breaks = 0;
				do {
					route.add(0, current.getCurrent());
					exhP.add(0, current.getExhaustionPoints() + "");
					if (current.hasBreak())
						breaks++;
					current = allNodes.get(current.getPrevious());
				} while (current != null);

				System.out.println("Route: " + route);
				System.out.println("Breaks: " + breaks);
				System.out.println("Exhaustion Points: " + exhP);
				return route;
			}

			graph.getConnections(next.getCurrent()).forEach(connection -> {
				if (next.getPrevious() != null) {
					RouteNode<T> previous = allNodes.get(next.getPrevious());

					if (previous.getExhaustionPoints() >= 10) { // exhaustion will be resetted in nextNodeScorer
						previous.setBreak(true);
					}
					scorerResult = nextNodeScorer.computeCost(next.getCurrent(), connection,
							previous.getExhaustionPoints());
				} else {
					scorerResult = nextNodeScorer.computeCost(next.getCurrent(), connection, 0);
				}
				next.setExhaustionPoints(scorerResult.getExhaustionPoints());
				double newScore = next.getRouteScore() + scorerResult.getCost();
				RouteNode<T> nextNode = allNodes.getOrDefault(connection, new RouteNode<>(connection));
				allNodes.put(connection, nextNode);

				scorerResult = targetScorer.computeCost(connection, to, next.getExhaustionPoints());

				System.out.println(nextNode.getCurrent().getId() + " f:" + nextNode.getEstimatedScore() + " g:"
						+ newScore + " h:" + scorerResult.getCost());
				if (nextNode.getEstimatedScore() > newScore + scorerResult.getCost()) {
					nextNode.setPrevious(next.getCurrent());
					nextNode.setRouteScore(newScore);

//					currentExhaustionPoints = scorerResult.getExhaustionPoints();
//					nextNode.setExhaustionPoints(currentExhaustionPoints);
					nextNode.setEstimatedScore(newScore + scorerResult.getCost());
					openSet.add(nextNode);
					System.out.println("Found a better route to node: " + nextNode);
				}
			});
		}

		throw new IllegalStateException("No route found");
	}

	public Queue<RouteNode> getOpenSet() { // TODO: remove
		return openSet;
	}
}
