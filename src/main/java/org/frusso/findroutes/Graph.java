package org.frusso.findroutes;

import java.util.ArrayList;
import java.util.List;

public class Graph {
	private final List<Vertex> vertexes = new ArrayList<Vertex>();
	private final List<Edge> edges = new ArrayList<Edge>();

	public void addVertex(Vertex v) {
		vertexes.add(v);
	}

	public boolean addEdge(Vertex v1, Vertex v2, int weight) {
		if (!vertexes.contains(v1) || !vertexes.contains(v1))
			throw new RuntimeException("Wrong vertices");

		return edges.add(new Edge(v1, v2, weight)) && edges.add(new Edge(v2, v1, weight));
	}

	public final List<Vertex> getVertexes() {
		return vertexes;
	}

	public final List<Edge> getEdges() {
		return edges;
	}
}