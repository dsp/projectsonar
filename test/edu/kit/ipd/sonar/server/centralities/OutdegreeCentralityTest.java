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

import org.junit.*;

import edu.kit.ipd.sonar.server.Edge;
import edu.kit.ipd.sonar.server.Graph;
import edu.kit.ipd.sonar.server.Node;
import edu.kit.ipd.sonar.server.TestUtil;
import static org.junit.Assert.*;

/**
 * Tests the OutdegreeCentrality - class.
 *
 * @author Till Heistermann <till.heistermann@student.kit.edu>
 */
public class OutdegreeCentralityTest {

    /**
     * Tests if the outdegree-centrality behaves right when
     * receiving an empty graph.
     */
    @Test
    public void testEmptyGraph() {

        OutdegreeCentrality oc = new OutdegreeCentrality();
        Graph graph = TestUtil.getEmtpyGraph();

        HashMap<Node, Double> result = oc.getWeight(graph);

        assertTrue(result.isEmpty());
    }

    /**
     * Tests if the outdegree-centrality calculates its values
     * correctly for a simple graph.
     */
    @Test
    public void testSimpleGraph() {

        OutdegreeCentrality oc = new OutdegreeCentrality();
        Graph graph = TestUtil.getEmtpyGraph();
        Node node1 = new Node(0, "", 0);
        Node node2 = new Node(1, "", 0);
        graph.addEdge(new Edge(node1, node2));

        HashMap<Node, Double> result = oc.getWeight(graph);

        assertEquals(new Double(1.0), result.get(node1));
        assertEquals(new Double(0.0), result.get(node2));
    }

    /**
     * Tests if the outdegree-centrality calculates its values
     * correctly for a slightly more complex graph.
     */
    @Test
    public void testComplexGraph() {

        OutdegreeCentrality oc = new OutdegreeCentrality();
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

        assertEquals(new Double(3.0), result.get(node1));
        assertEquals(new Double(1.0), result.get(node2));
        assertEquals(new Double(1.0), result.get(node3));
        assertEquals(new Double(0.0), result.get(node4));
    }

    /**
     * Tests if the outdegree-centrality calculates its values
     * correctly for a edge-less-graph.
     */
    @Test
    public void testGraphWithoutEdges() {

        OutdegreeCentrality oc = new OutdegreeCentrality();
        Graph graph = TestUtil.getEmtpyGraph();
        Node node1 = new Node(0, "", 0);
        Node node2 = new Node(1, "", 0);
        Node node3 = new Node(2, "", 0);
        graph.addNode(node1);
        graph.addNode(node2);
        graph.addNode(node3);

        HashMap<Node, Double> result = oc.getWeight(graph);

        assertEquals(new Double(0.0), result.get(node1));
        assertEquals(new Double(0.0), result.get(node2));
        assertEquals(new Double(0.0), result.get(node3));
    }

    /**
     * Tests if there is an IllegalArgumentException if the graph passed as
     * an Argument was null.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testNullArgument() {
        OutdegreeCentrality oc = new OutdegreeCentrality();
        oc.getWeight(null);
    }
}
