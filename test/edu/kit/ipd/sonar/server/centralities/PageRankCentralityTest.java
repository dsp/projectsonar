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

import java.util.HashMap;

import org.junit.Test;

import edu.kit.ipd.sonar.server.Edge;
import edu.kit.ipd.sonar.server.Graph;
import edu.kit.ipd.sonar.server.Node;
import edu.kit.ipd.sonar.server.TestUtil;
import static org.junit.Assert.*;

/**
 * Tests the PageRankCentrality
 * @author Till Heistermann <till.heistermann@student.kit.edu>
 *
 */
public class PageRankCentralityTest {

    private static final double DOUBLE_DELTA = 0.01;

    /**
     * Tests if there is an IllegalArgumentException if the graph passed as
     * an Argument was null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullArgument() {
        PageRankCentrality prc = new PageRankCentrality();
        prc.getWeight(null);

    }

    /**
     * Tests if the Page-Rank-centrality behaves right when
     * receiving an empty graph.
     */
    @Test
    public void testEmptyGraph() {

        PageRankCentrality prc = new PageRankCentrality();
        Graph graph = TestUtil.getEmtpyGraph();

        HashMap<Node, Double> result = prc.getWeight(graph);

        assertTrue(result.isEmpty());
    }

    /**
     * Tests if the page-rank-centrality calculates its values
     * correctly for a simple normal graph.
     */
    @Test
    public void testNormalGraph() {

        PageRankCentrality prc = new PageRankCentrality();
        Graph graph = TestUtil.getEmtpyGraph();
        Node nodeA = new Node(1, "", 0);
        Node nodeB = new Node(2, "", 0);
        Node nodeC = new Node(3, "", 0);
        Node nodeD = new Node(4, "", 0);
        graph.addEdge(new Edge(nodeA, nodeC));
        graph.addEdge(new Edge(nodeC, nodeA));
        graph.addEdge(new Edge(nodeA, nodeB));
        graph.addEdge(new Edge(nodeB, nodeC));
        graph.addEdge(new Edge(nodeD, nodeC));

        HashMap<Node, Double> result = prc.getWeight(graph);

        assertEquals(1.490, result.get(nodeA), DOUBLE_DELTA);
        assertEquals(0.783, result.get(nodeB), DOUBLE_DELTA);
        assertEquals(1.577, result.get(nodeC), DOUBLE_DELTA);
        assertEquals(0.150, result.get(nodeD), DOUBLE_DELTA);
    }

    /**
     * Tests if the PageRank-centrality calculates its values
     * correctly for an edge-less-graph.
     * The values should be (1-d).
     */
    @Test
    public void testGraphWithoutEdges() {

        PageRankCentrality prc = new PageRankCentrality();
        Graph graph = TestUtil.getEmtpyGraph();
        Node node1 = new Node(0, "", 0);
        Node node2 = new Node(1, "", 0);
        Node node3 = new Node(2, "", 0);
        graph.addNode(node1);
        graph.addNode(node2);
        graph.addNode(node3);

        HashMap<Node, Double> result = prc.getWeight(graph);

        assertEquals(0.15, result.get(node1), DOUBLE_DELTA);
        assertEquals(0.15, result.get(node2), DOUBLE_DELTA);
        assertEquals(0.15, result.get(node3), DOUBLE_DELTA);
    }

}
