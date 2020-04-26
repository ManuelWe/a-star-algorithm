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
	private final WaldCheck<T> waldCheck;
	private Queue<RouteNode> openSet = new PriorityQueue<>();
	private int nodesChecked = 0;
	private double currentExhaustionPoints = 0;
	private ScorerResult scorerResult = new ScorerResult();

	public RouteFinder(Graph<T> graph, Scorer<T> nextNodeScorer, Scorer<T> targetScorer, WaldCheck<T> waldCheck) {
		this.graph = graph;
		this.nextNodeScorer = nextNodeScorer;
		this.targetScorer = targetScorer;
		this.waldCheck = waldCheck;
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
			System.out.println("Looking at node: " + next);

			if (next.getCurrent().equals(to)) {
				System.out.println("\nFound our destination!");

				List<T> route = new ArrayList<>();
				RouteNode<T> current = next;
				int breaks = 0;
				do {
					route.add(0, current.getCurrent());
					if (current.hasBreak())
						breaks++;
					current = allNodes.get(current.getPrevious());
				} while (current != null);

				System.out.println("Route: " + route);
				System.out.println("Breaks: " + breaks + " (+1 if break happend on last step)");
				return route;
			}

			graph.getConnections(next.getCurrent()).forEach(connection -> {
				RouteNode<T> previous = allNodes.get(next.getPrevious());
				if (previous != null) {
					scorerResult = nextNodeScorer.computeCost(next.getCurrent(), connection,
							previous.getExhaustionPoints());
				} else {
					scorerResult = nextNodeScorer.computeCost(next.getCurrent(), connection, 0);
				}
				if (scorerResult.getExhaustionPoints() >= 10) { // take break if exhaustion points are greater 10
					next.setBreak(true);
					next.setExhaustionPoints(scorerResult.getExhaustionPoints() - 10);
				} else {
					next.setExhaustionPoints(scorerResult.getExhaustionPoints());
				}
				double newScore = next.getRouteScore() + scorerResult.getCost();
				RouteNode<T> nextNode = allNodes.getOrDefault(connection, new RouteNode<>(connection));
				allNodes.put(connection, nextNode);

				double hScore;
				if (previous != null) {
					scorerResult = targetScorer.computeCost(connection, to, next.getExhaustionPoints());
					// check, if h score has to be reduced for route via "wald", if it is likely
					// that a break has to be taken
					hScore = waldCheck.checkCostReduction(next.getCurrent(), connection, to, next.getPrevious(),
							previous.getExhaustionPoints(), scorerResult.getCost());
				} else {
					scorerResult = targetScorer.computeCost(connection, to, 0);
					// check, if h score has to be reduced for route via "wald", if it is likely
					// that a break has to be taken
					hScore = waldCheck.checkCostReduction(next.getCurrent(), connection, to, next.getPrevious(), 0,
							scorerResult.getCost());
				}

				// check if new f score is lower than the existing
				if (nextNode.getEstimatedScore() > newScore + hScore) {
					nextNode.setPrevious(next.getCurrent());
					nextNode.setRouteScore(newScore);
					nextNode.setEstimatedScore(newScore + hScore);
					openSet = removeElementFromQueue(openSet, nextNode); // if a path to nextNode already exists it is
																			// substituted by the cheaper path
					openSet.add(nextNode);
					System.out.println("Found a better route to node: " + nextNode);
				}
			});
		}

		throw new IllegalStateException("No route found");
	}

	// remove supplied element from the queue
	public static Queue<RouteNode> removeElementFromQueue(Queue<RouteNode> queue, RouteNode element) {
		Queue<RouteNode> newQueue = new PriorityQueue<>();
		synchronized (queue) {
			if (queue == null) {
				return null;
			}

			int size = queue.size();
			if (size == 0) {
				return newQueue;
			}

			RouteNode tempElement = null;
			for (int i = 0; i < size; i++) {
				tempElement = queue.remove();
				if (tempElement.getCurrent().getId() != element.getCurrent().getId()) {
					newQueue.add(tempElement);
				}
			}
		}
		return newQueue;
	}

	public int getNodesChecked() {
		return nodesChecked;
	}
}
