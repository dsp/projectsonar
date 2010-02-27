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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Test;

import edu.kit.ipd.sonar.server.Edge;
import edu.kit.ipd.sonar.server.Graph;
import edu.kit.ipd.sonar.server.Node;
import edu.kit.ipd.sonar.server.TestUtil;

/**
 * Tests the Betweenness Centrality.
 *
 * @author Till Heistermann <till.heistermann@student.kit.edu>
 */
public class BetweennessCentralityTest {

    private static final double DOUBLE_DELTA = 0.001;

    /**
     * Tests if the betweenness-centrality behaves right when
     * receiving an empty graph.
     */
    @Test
    public void testEmptyGraph() {

        BetweennessCentrality bc = new BetweennessCentrality();
        Graph graph = TestUtil.getEmtpyGraph();

        HashMap<Node, Double> result = bc.getWeight(graph);

        assertTrue(result.isEmpty());
    }


    /**
     * Tests if there is an IllegalArgumentException if the graph passed as
     * an Argument was null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullArgument() {
        BetweennessCentrality bc = new BetweennessCentrality();
        bc.getWeight(null);

    }

    /**
     * Tests if the betweenness-centrality calculates its values
     * correctly for an edge-less-graph. the betweenness-values should
     * be zero.
     */
    @Test
    public void testGraphWithoutEdges() {

        BetweennessCentrality bc = new BetweennessCentrality();
        Graph graph = TestUtil.getEmtpyGraph();
        Node node1 = new Node(0, "", 0);
        Node node2 = new Node(1, "", 0);
        Node node3 = new Node(2, "", 0);
        graph.addNode(node1);
        graph.addNode(node2);
        graph.addNode(node3);

        HashMap<Node, Double> result = bc.getWeight(graph);

        assertEquals(0.0, result.get(node1), DOUBLE_DELTA);
        assertEquals(0.0, result.get(node2), DOUBLE_DELTA);
        assertEquals(0.0, result.get(node3), DOUBLE_DELTA);
    }

    /**
     * Tests if the betweenness-centrality calculates its values
     * correctly for a tree-graph.
     */
    @Test
    public void testTreeGraph() {

        BetweennessCentrality bc = new BetweennessCentrality();
        Graph graph = TestUtil.getEmtpyGraph();
        Node node1 = new Node(0, "", 0);
        Node node2 = new Node(1, "", 0);
        Node node3 = new Node(3, "", 0);
        Node node4 = new Node(4, "", 0);

        graph.addEdge(new Edge(node1, node2));
        graph.addEdge(new Edge(node2, node1));
        graph.addEdge(new Edge(node1, node3));
        graph.addEdge(new Edge(node3, node1));
        graph.addEdge(new Edge(node1, node4));
        graph.addEdge(new Edge(node4, node1));

        HashMap<Node, Double> result = bc.getWeight(graph);

        assertEquals(6.0, result.get(node1), DOUBLE_DELTA);
        assertEquals(0.0, result.get(node2), DOUBLE_DELTA);
        assertEquals(0.0, result.get(node3), DOUBLE_DELTA);
        assertEquals(0.0, result.get(node4), DOUBLE_DELTA);
    }

    /**
     * Tests if the betweenness-centrality calculates its values
     * correctly for a slightly more complex graph.
     */
    @Test
    public void testAnotherGraph() {

        BetweennessCentrality oc = new BetweennessCentrality();
        Graph graph = TestUtil.getEmtpyGraph();
        Node node1 = new Node(1, "", 0);
        Node node2 = new Node(2, "", 0);
        Node node3 = new Node(3, "", 0);
        Node node4 = new Node(4, "", 0);
        graph.addEdge(new Edge(node1, node2));
        graph.addEdge(new Edge(node1, node3));
        graph.addEdge(new Edge(node1, node4));
        graph.addEdge(new Edge(node2, node3));
        graph.addEdge(new Edge(node3, node1));

        HashMap<Node, Double> result = oc.getWeight(graph);

        assertEquals(3.0, result.get(node1), DOUBLE_DELTA);
        assertEquals(0.0, result.get(node2), DOUBLE_DELTA);
        assertEquals(2.0, result.get(node3), DOUBLE_DELTA);
        assertEquals(0.0, result.get(node4), DOUBLE_DELTA);
    }

    /**
     * Tests if the betweenness-centrality calculates its values
     * correctly for a linear graph.
     */
    @Test
    public void testLinearGraph() {

        BetweennessCentrality oc = new BetweennessCentrality();
        Graph graph = TestUtil.getEmtpyGraph();
        Node node1 = new Node(1, "", 0);
        Node node2 = new Node(2, "", 0);
        Node node3 = new Node(3, "", 0);
        Node node4 = new Node(4, "", 0);
        graph.addEdge(new Edge(node1, node2));
        graph.addEdge(new Edge(node2, node1));
        graph.addEdge(new Edge(node2, node3));
        graph.addEdge(new Edge(node3, node2));
        graph.addEdge(new Edge(node3, node4));
        graph.addEdge(new Edge(node4, node3));

        HashMap<Node, Double> result = oc.getWeight(graph);

        assertEquals(0.0, result.get(node1), DOUBLE_DELTA);
        assertEquals(4.0, result.get(node2), DOUBLE_DELTA);
        assertEquals(4.0, result.get(node3), DOUBLE_DELTA);
        assertEquals(0.0, result.get(node4), DOUBLE_DELTA);
    }

}
