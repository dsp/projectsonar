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

import edu.kit.ipd.sonar.server.centralities.Centrality;
import edu.kit.ipd.sonar.server.centralities.BetweennessCentrality;
import edu.kit.ipd.sonar.server.centralities.OutdegreeCentrality;
import edu.kit.ipd.sonar.server.Edge;
import edu.kit.ipd.sonar.server.Node;
import edu.kit.ipd.sonar.server.NodeDoesNotExistException;
import java.util.Date;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * Tests for the Node implementation
 *
 * @author David Soria Parra <david.parra@student.kit.edu>
 */
public class NodeTest {
    @Test(expected=NodeDoesNotExistException.class)
    public void addEdge() throws NodeDoesNotExistException {
        Edge e;
        Node n1, n2, n3;

        n1 = new Node(1, "Test", 0);
        n2 = new Node(2, "Test", 0);
        n3 = new Node(3, "Test", 0);

        e = new Edge(n1, n2);
        n1.addEdge(e);

        e = new Edge(n1, n3);
        n1.addEdge(e);

        e = new Edge(n1, n1);
        n1.addEdge(e);

        e = new Edge(n2, n3);
        n1.addEdge(e);
    }

    @Test
    public void testGetId() {
        Node n;

        n = new Node(3, "Test", 0);
        assertSame(3, n.getId());

        n = new Node(-2, "Test", 0);
        assertSame(-2, n.getId());
    }

    @Test
    public void testGetName() {
        Node n;

        n = new Node(3, "Test", 0);
        assertSame("Test", n.getName());

        n = new Node(-2, "Test 2", 0);
        assertSame("Test 2", n.getName());
    }

    @Test
    public void testGetTime() {
        Node n;

        n = new Node(3, "Test", 0);
        assertSame("Test", n.getName());

        n = new Node(-2, "Test 2", (int) (new Date()).getTime() / 1000);
        assertSame("Test 2", n.getName());
    }
    @Test
    public void testGetEdges() throws NodeDoesNotExistException {
        Edge e1, e2;
        Node n1, n2, n3;

        n1 = new Node(1, "Test", 0);
        n2 = new Node(2, "Test", 0);
        n3 = new Node(3, "Test", 0);

        e1 = new Edge(n1, n2);
        n1.addEdge(e1);

        e2 = new Edge(n1, n3);
        n1.addEdge(e2);

        assertSame(2, n1.getEdges().size());
        assertTrue(n1.getEdges().contains(e1));
        assertTrue(n1.getEdges().contains(e2));

        // assertArrayEquals(n1.getEdges(), new Edge[] {e1, e2});

        Node n4 = new Node(3, "Test", 0);
        assertFalse(n4.getEdges().contains(e1));
        assertFalse(n4.getEdges().contains(e2));
        assertSame(0, n4.getEdges().size());
    }

    @Test
    public void testGetCleanCopy() throws NodeDoesNotExistException {
        Edge e1, e2;
        Node n1, n2, n3, nc;

        n1 = new Node(1, "Test", 0);
        n2 = new Node(2, "Test", 0);
        n3 = new Node(3, "Test", 0);

        e1 = new Edge(n1, n2);
        n1.addEdge(e1);

        e2 = new Edge(n1, n3);
        n1.addEdge(e2);

        nc = n1.getCleanCopy();
        assertSame(0, nc.getEdges().size());
        assertSame(1, nc.getId());
        assertSame("Test", nc.getName());
        assertSame(0, nc.getTime());
        assertSame(0, nc.getCentralities().size());
    }

    @Test(expected=InvalidCentralityException.class)
    public void testGetWeightForCentrality() throws InvalidCentralityException {
        Node n;
        Centrality c;

        n = new Node(1, "Test", 0);
        c = new OutdegreeCentrality();
        n.addWeight(c, 2.0);
        n.addWeight(c, 3.0);

        assertEquals(3.0, n.getWeightForCentrality(c), 0.01);
        c = new BetweennessCentrality();
        n.getWeightForCentrality(c);
    }
}
