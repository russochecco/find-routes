package org.frusso.findroutes;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class DijkstraAlgorithmTests {

	private Graph graph;
	private Vertex source;
	private Vertex target;
	private DijkstraAlgorithm dijkstraAlgorithm;

	@Before
	public void setUp() {
		Vertex a = new Vertex("a");
		Vertex b = new Vertex("b");
		Vertex c = new Vertex("c");
		Vertex d = new Vertex("d");
		Vertex e = new Vertex("e");
		Vertex f = new Vertex("f");

		graph = new Graph();

		// set vertices
		graph.addVertex(a);
		graph.addVertex(b);
		graph.addVertex(c);
		graph.addVertex(d);
		graph.addVertex(e);
		graph.addVertex(f);

		// set edges
		graph.addEdge(a, b, 7);
		graph.addEdge(a, c, 9);
		graph.addEdge(a, f, 14);
		graph.addEdge(b, c, 10);
		graph.addEdge(b, d, 15);
		graph.addEdge(c, d, 11);
		graph.addEdge(c, f, 2);
		graph.addEdge(d, e, 6);
		graph.addEdge(e, f, 9);

		// set the algorithm
		dijkstraAlgorithm = new DijkstraAlgorithm(graph);
	}

	@Test
	public void testGetShortestPath() {
		String expected = "[a, c, f, e]";
		source = graph.getVertexes().get(0);
		assertEquals("a", source.getId());
		target = graph.getVertexes().get(4);
		assertEquals("e", target.getId());
		LinkedList<Vertex> shortestPath = dijkstraAlgorithm.getShortestPath(source, target);
		assertEquals(expected, shortestPath.toString());
		expected = "[e, f, c, a]";
		shortestPath = dijkstraAlgorithm.getShortestPath(target, source);
		assertEquals(expected, shortestPath.toString());
		System.out.println(String.format("Shortest path between '%s' and '%s': %s", source.getId(), target.getId(), shortestPath));
	}

	@Test
	public void testGetAllPaths() {
		final String expected = "[[a, b, c, d, e], [a, b, c, f, e], [a, b, d, c, f, e], [a, b, d, e], [a, c, b, d, e], [a, c, d, e], [a, c, f, e], [a, f, c, b, d, e], [a, f, c, d, e], [a, f, e]]";
		source = graph.getVertexes().get(0);
		assertEquals("a", source.getId());
		target = graph.getVertexes().get(4);
		assertEquals("e", target.getId());
		List<List<Vertex>> allPaths = dijkstraAlgorithm.getAllPaths(source, target);
		assertEquals(expected, allPaths.toString());
		System.out.println(String.format("Paths tested between '%s' and '%s': %s", source.getId(), target.getId(), allPaths));
	}
}