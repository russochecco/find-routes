package org.frusso.findroutes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class DijkstraAlgorithm {

	private final Set<Vertex> settled = new HashSet<Vertex>();
	private final Set<Vertex> unsettled = new HashSet<Vertex>();
	private final Map<Vertex, Vertex> predecessors = new HashMap<Vertex, Vertex>();
	private final Map<Vertex, Integer> distance = new HashMap<Vertex, Integer>();
	private final Stack<Vertex> stack = new Stack<Vertex>();
	private final Set<Vertex> targets = new HashSet<Vertex>();
	private final List<List<Vertex>> paths = new ArrayList<List<Vertex>>();
	private final List<Edge> edges;

	public DijkstraAlgorithm(Graph graph) {
		this.edges = new ArrayList<Edge>(graph.getEdges());
	}

	/**
	 * Method used to evaluate the minimal distances between the current vertex
	 * and the target
	 * 
	 * @param current
	 *            The current vertex
	 */
	private void findMinimalDistances(Vertex current) {
		List<Vertex> adjacentNodes = getNeighbors(current, true);
		for (Vertex target : adjacentNodes) {
			if (getShortestDistance(target) > getShortestDistance(current) + getDistance(current, target)) {
				distance.put(target, getShortestDistance(current) + getDistance(current, target));
				predecessors.put(target, current);
				unsettled.add(target);
			}
		}
	}

	/**
	 * Method used to evaluate the distance between two vertices
	 * 
	 * @param current
	 *            The current vertex
	 * @param target
	 *            The target
	 * @return The distance between the two vertices in input. Throws an
	 *         exception in case of there isn't any edge between the vertices
	 */
	private int getDistance(Vertex current, Vertex target) {
		for (Edge edge : edges) {
			if (edge.getSource().equals(current) && edge.getDestination().equals(target)) {
				return edge.getWeight();
			}
		}
		throw new RuntimeException("Impossible to evaluate the distance");
	}

	/**
	 * Method used to find the neighbors of the current source
	 * 
	 * @param current
	 *            The current vertex
	 * @param filtered
	 *            A flag to filter the unsettled edges
	 * @return The list of vertex found
	 */
	private List<Vertex> getNeighbors(Vertex current, boolean filtered) {
		List<Vertex> neighbors = new ArrayList<Vertex>();
		for (Edge edge : edges) {
			if (edge.getSource().equals(current) && (filtered ? !isSettled(edge.getDestination()) : true)) {
				neighbors.add(edge.getDestination());
			}
		}

		return neighbors;
	}

	/**
	 * Method used to find the vertex at the minimal distance from the source
	 * 
	 * @param vertexes
	 *            The set of vertices unsettled
	 * @return The vertex at the minimal distance from the source
	 */
	private Vertex getMinimum(Set<Vertex> vertexes) {
		Vertex minimum = null;
		for (Vertex vertex : vertexes) {
			if (minimum == null) {
				minimum = vertex;
			} else {
				if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
					minimum = vertex;
				}
			}
		}

		return minimum;
	}

	/**
	 * Method used to check if a vertex is unsettled
	 * 
	 * @param vertex
	 *            The vertex to check
	 * @return True if the vertex in input is settled
	 */
	private boolean isSettled(Vertex vertex) {
		return settled.contains(vertex);
	}

	/**
	 * Method to retrieve the shortest distance between the source and the
	 * target
	 * 
	 * @param target
	 *            The target
	 * @return The shortest distance between the source and the target
	 */
	private int getShortestDistance(Vertex target) {
		Integer d = distance.get(target);
		if (d == null) {
			return Integer.MAX_VALUE;
		} else {
			return d;
		}
	}

	/**
	 * Method used to enumerate the all possible existing paths between the
	 * source and the target
	 * 
	 * @param source
	 *            The source
	 * @param target
	 *            The target
	 */
	private void enumerate(Vertex source, Vertex target) {
		stack.push(source);
		targets.add(source);

		if (source.equals(target))
			paths.add(new ArrayList<Vertex>(stack));
		else {
			for (Vertex current : getNeighbors(source, false)) {
				if (!targets.contains(current)) {
					enumerate(current, target);
				}
			}
		}

		targets.remove(source);
		stack.pop();
	}

	/**
	 * Method used to retrieve all possible existing paths between the source
	 * and the target
	 * 
	 * @param source
	 *            The source
	 * @param target
	 *            The target
	 * @return The list of all paths found
	 */
	public final List<List<Vertex>> getAllPaths(Vertex source, Vertex target) {
		enumerate(source, target);
		return paths;
	}

	/**
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public LinkedList<Vertex> getShortestPath(Vertex source, Vertex target) {

		// clear
		settled.clear();
		unsettled.clear();
		distance.clear();
		predecessors.clear();

		// initialize
		distance.put(source, 0);
		unsettled.add(source);

		LinkedList<Vertex> shortest = new LinkedList<Vertex>();

		// explore all vertices of the graph and evaluate their distances
		while (unsettled.size() > 0) {
			Vertex current = getMinimum(unsettled);			
			settled.add(current);
			unsettled.remove(current);
			findMinimalDistances(current);
		}

		Vertex step = target;

		// check if a path exists
		if (predecessors.get(step) == null)
			throw new RuntimeException("Any path found");

		// add the source 
		shortest.add(step);

		// add all vertices on the shortest path
		while (predecessors.get(step) != null) {
			step = predecessors.get(step);
			shortest.add(step);
		}

		// sort properly the vertices
		Collections.reverse(shortest);

		return shortest;
	}
}