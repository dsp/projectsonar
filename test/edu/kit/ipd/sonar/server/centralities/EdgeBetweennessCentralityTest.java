/*
 * This file is part of Sonar.
 *
 * Sonar is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 2 of the License
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Sonar.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.kit.ipd.sonar.server.centralities;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

import edu.kit.ipd.sonar.server.Edge;
import edu.kit.ipd.sonar.server.Graph;
import edu.kit.ipd.sonar.server.Node;
import edu.kit.ipd.sonar.server.TestUtil;

/**
 * Tests the Edge Betweenness Centrality.
 *
 * @author Till Heistermann <till.heistermann@student.kit.edu>
 */
public class EdgeBetweennessCentralityTest {

    private static final double DOUBLE_DELTA = 0.001;

    /**
     * Tests if the Edge betweenness-Centrality behaves right when
     * receiving an empty graph.
     */
    @Test
    public void testEmptyGraph() {

        EdgeBetweennessCentrality ebc = new EdgeBetweennessCentrality();
        Graph graph = TestUtil.getEmtpyGraph();

        HashMap<Edge, Double> result = ebc.getWeight(graph);

        assertTrue(result.isEmpty());
    }


    /**
     * Tests if there is an IllegalArgumentException if the graph passed as
     * an Argument was null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullArgument() {
        EdgeBetweennessCentrality ebc = new EdgeBetweennessCentrality();
        ebc.getWeight(null);

    }

    /**
     * Tests if the edge betweenness-centrality calculates its values
     * correctly for a tree-graph.
     */
    @Test
    public void testTreeGraph() {

        EdgeBetweennessCentrality ebc = new EdgeBetweennessCentrality();
        Graph graph = TestUtil.getEmtpyGraph();
        Node node1 = new Node(0, "", 0);
        Node node2 = new Node(1, "", 0);
        Node node3 = new Node(3, "", 0);
        Node node4 = new Node(4, "", 0);

        Edge edge1 = new Edge(node1, node2);
        graph.addEdge(edge1);
        Edge edge2 = new Edge(node1, node3);
        graph.addEdge(edge2);
        Edge edge3 = new Edge(node1, node4);
        graph.addEdge(edge3);

        HashMap<Edge, Double> result = ebc.getWeight(graph);

        assertEquals(1.0, result.get(edge1), DOUBLE_DELTA);
        assertEquals(1.0, result.get(edge2), DOUBLE_DELTA);
        assertEquals(1.0, result.get(edge3), DOUBLE_DELTA);
    }

    /**
     * Tests if the edge-betweenness-centrality calculates its values
     * correctly for a slightly more complex graph.
     */
    @Test
    public void testAnotherGraph() {

        EdgeBetweennessCentrality ebc = new EdgeBetweennessCentrality();
        Graph graph = TestUtil.getEmtpyGraph();
        Node nodeA = new Node(1, "", 0);
        Node nodeB = new Node(2, "", 0);
        Node nodeC = new Node(3, "", 0);
        Node nodeD = new Node(4, "", 0);
        Node nodeE = new Node(5, "", 0);
        Node nodeF = new Node(6, "", 0);

        Edge edge1 = new Edge(nodeA, nodeB);
        graph.addEdge(edge1);
        Edge edge2 = new Edge(nodeB, nodeA);
        graph.addEdge(edge2);
        Edge edge3 = new Edge(nodeB, nodeC);
        graph.addEdge(edge3);
        Edge edge4 = new Edge(nodeC, nodeA);
        graph.addEdge(edge4);
        Edge edge5 = new Edge(nodeA, nodeD);
        graph.addEdge(edge5);
        Edge edge6 = new Edge(nodeD, nodeF);
        graph.addEdge(edge6);
        Edge edge7 = new Edge(nodeA, nodeE);
        graph.addEdge(edge7);
        Edge edge8 = new Edge(nodeE, nodeF);
        graph.addEdge(edge8);

        HashMap<Edge, Double> result = ebc.getWeight(graph);

        assertEquals(3.0, result.get(edge1), DOUBLE_DELTA);
        assertEquals(4.0, result.get(edge2), DOUBLE_DELTA);
        assertEquals(2.0, result.get(edge3), DOUBLE_DELTA);
        assertEquals(5.0, result.get(edge4), DOUBLE_DELTA);
        assertEquals(4.5, result.get(edge5), DOUBLE_DELTA);
        assertEquals(2.5, result.get(edge6), DOUBLE_DELTA);
        assertEquals(4.5, result.get(edge7), DOUBLE_DELTA);
        assertEquals(2.5, result.get(edge8), DOUBLE_DELTA);
    }

    /**
     * Tests if the edge-betweenness-centrality calculates its values
     * correctly for a linear graph.
     */
    @Test
    public void testLinearGraph() {

        EdgeBetweennessCentrality ebc = new EdgeBetweennessCentrality();
        Graph graph = TestUtil.getEmtpyGraph();
        Node node1 = new Node(1, "", 0);
        Node node2 = new Node(2, "", 0);
        Node node3 = new Node(3, "", 0);
        Node node4 = new Node(4, "", 0);

        Edge edge1 = new Edge(node1, node2);
        graph.addEdge(edge1);
        Edge edge2 = new Edge(node2, node1);
        graph.addEdge(edge2);
        Edge edge3 = new Edge(node3, node2);
        graph.addEdge(edge3);
        Edge edge4 = new Edge(node2, node3);
        graph.addEdge(edge4);
        Edge edge5 = new Edge(node3, node4);
        graph.addEdge(edge5);
        Edge edge6 = new Edge(node4, node3);
        graph.addEdge(edge6);

        HashMap<Edge, Double> result = ebc.getWeight(graph);

        assertEquals(3.0, result.get(edge1), DOUBLE_DELTA);
        assertEquals(3.0, result.get(edge2), DOUBLE_DELTA);
        assertEquals(4.0, result.get(edge3), DOUBLE_DELTA);
        assertEquals(4.0, result.get(edge4), DOUBLE_DELTA);
        assertEquals(3.0, result.get(edge5), DOUBLE_DELTA);
        assertEquals(3.0, result.get(edge6), DOUBLE_DELTA);
    }

}
