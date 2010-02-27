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
package edu.kit.ipd.sonar.server;

import edu.kit.ipd.sonar.server.centralities.CentralityImpl;
import edu.kit.ipd.sonar.server.centralities.BetweennessCentrality;
import edu.kit.ipd.sonar.server.centralities.PageRankCentrality;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * Tests for the Graph implementation
 *
 * @author David Soria Parra <david.parra@student.kit.edu>
 */
public class GraphTest {
    @Test
    public void testAddEdge() {
        Graph g;
        Node n1, n2;
        Edge e1, e2;

        n1 = new Node(1, "Node 1", 1000);
        n2 = new Node(2, "Node 2", 1000);

        e1 = new Edge(n1, n2);
        e2 = new Edge(n2, n1);

        g = new Graph();
        assertEquals(0, g.getEdgeList().size());

        g.addEdge(e1);
        g.addEdge(e2);

        assertTrue(g.getEdgeList().contains(e1));
        assertTrue(g.getEdgeList().contains(e2));
    }

    @Test
    public void testAddNode() {
        Graph g;
        Node n1, n2;

        n1 = new Node(1, "Node 1", 1000);
        n2 = new Node(2, "Node 2", 1000);

        g = new Graph();

        assertEquals(0, g.getNodeList().size());

        g.addNode(n1);
        g.addNode(n2);

        assertTrue(g.getNodeList().containsValue(n1));
        assertTrue(g.getNodeList().containsValue(n2));
    }

    @Test(expected=NodeDoesNotExistException.class)
    public void testGetNodeById() throws NodeDoesNotExistException {
        Graph g;
        Node n1, n2;

        n1 = new Node(1, "Node 1", 1000);
        n2 = new Node(2, "Node 2", 1000);

        g = new Graph();
        g.addNode(n1);
        g.addNode(n2);

        assertEquals(n1, g.getNodeById(1));
        assertEquals(n2, g.getNodeById(2));

        g.getNodeById(3);
    }

    @Test
    public void testGetCentralities() {
        Graph g;
        Edge e;
        Node n1, n2;
        CentralityImpl c1, c2;

        c1 = new BetweennessCentrality();
        c2 = new BetweennessCentrality();

        g = new Graph();

        n1 = new Node(1, "Node 1", 1000);
        n1.addWeight(c1, 2.0);

        n2 = new Node(2, "Node 2", 1000);
        n2.addWeight(c2, 3.0);

        e = new Edge(n1, n2);
        g.addEdge(e);

        assertTrue(g.getCentralities().contains(c1));
        assertTrue(g.getCentralities().contains(c2));
    }

    @Test
    public void testGetCentralitiesMaxWeights() {
        Graph g;
        Edge e;
        Node n1, n2;
        CentralityImpl c1, c2;

        c1 = new BetweennessCentrality();
        c2 = new PageRankCentrality();

        g = new Graph();

        assertTrue(g.getCentralitiesMaxWeights().isEmpty());

        n1 = new Node(1, "Node 1", 1000);
        n1.addWeight(c1, 2.0);

        n2 = new Node(2, "Node 2", 1000);
        n2.addWeight(c2, 3.0);

        e = new Edge(n1, n2);

        g.addEdge(e);

        assertEquals(new Double(3.0), g.getCentralitiesMaxWeights().get(c2), 0.01);
        assertEquals(new Double(2.0), g.getCentralitiesMaxWeights().get(c1), 0.01);

        /* test observer pattern */
        n2.addWeight(c2, 5.0);
        assertEquals(new Double(5.0), g.getCentralitiesMaxWeights().get(c2), 0.01);
    }

    @Test
    public void testGetCentralitiesMinWeights() {
        Graph g;
        Edge e;
        Node n1, n2;
        CentralityImpl c1, c2;

        c1 = new BetweennessCentrality();
        c2 = new BetweennessCentrality();

        g = new Graph();

        assertTrue(g.getCentralitiesMinWeights().isEmpty());

        n1 = new Node(1, "Node 1", 1000);
        n1.addWeight(c1, 2.0);

        n2 = new Node(2, "Node 2", 1000);
        n1.addWeight(c1, 1.0);
        n2.addWeight(c2, 3.0);

        e = new Edge(n1, n2);
        g.addEdge(e);

        assertEquals(new Double(1.0), g.getCentralitiesMinWeights().get(c1), 0.01);

        /* test observer pattern */
        n2.addWeight(c2, -10.0);
        assertEquals(new Double(-10.0), g.getCentralitiesMinWeights().get(c2), 0.01);
    }

    @Test
    public void testGetMaxTimeBoundary() {
        Graph g;
        Node n1, n2, n3, n4;

        g = new Graph();

        n1 = new Node(1, "Node 1", 1000);
        n2 = new Node(2, "Node 2", 1000);
        n3 = new Node(3, "Node 3", 100);
        n4 = new Node(4, "Node 4", 10000);

        assertNull(g.getMaxTimeBoundary());

        g.addNode(n1);
        g.addNode(n2);
        g.addNode(n3);
        g.addNode(n4);

        assertEquals(100, g.getMaxTimeBoundary().getStart());
        assertEquals(10000, g.getMaxTimeBoundary().getEnd());
    }

    @Test(expected=NodeDoesNotExistException.class)
    public void testGetCentralNode() throws NodeDoesNotExistException {
        Graph g;
        Node n1;

        g = new Graph();

        n1 = new Node(1, "Node 1", 1000);

        assertNull(g.getCentralNode());

        /* this should thrown an exception */
        g.setCentralNode(n1);

        g.addNode(n1);
        g.setCentralNode(n1);
        assertEquals(n1, g.getCentralNode());
    }

    @Test
    public void testGetStateHash() {
        assertEquals(763232377, TestUtil.getGraphMock().getStateHash());
    }
}
