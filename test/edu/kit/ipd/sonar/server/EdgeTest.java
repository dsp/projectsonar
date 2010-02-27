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
import edu.kit.ipd.sonar.server.centralities.CentralityImpl;
import edu.kit.ipd.sonar.server.centralities.BetweennessCentrality;
import edu.kit.ipd.sonar.server.Edge;
import edu.kit.ipd.sonar.server.InvalidCentralityException;
import edu.kit.ipd.sonar.server.Node;

import java.util.HashMap;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * Tests for the Edge implementation
 *
 * @author David Soria Parra <david.parra@student.kit.edu>
 */
public class EdgeTest {
    @Test
    public void testGetCleanCopy() {
        Edge e, ec;
        Node n1, n2;

        n1 = new Node(1, "Test", 0);
        n2 = new Node(2, "Test", 1);

        e = new Edge(n1, n2);
        e.setOriginalWeight(5.0);
        ec = e.getCleanCopy();

        assertEquals(5.0, ec.getOriginalWeight(), 0.01);
        assertNull(ec.getSourceNode());
        assertNull(ec.getDestinationNode());
    }

    @Test
    public void testGetOriginalWeight() {
        Edge e;
        Node n1, n2;

        n1 = new Node(1, "Test", 0);
        n2 = new Node(2, "Test", 1);

        e = new Edge(n1, n2);
        assertNull(e.getOriginalWeight());

        e = new Edge(n1, n2);
        e.setOriginalWeight(100.0);
        assertEquals(100.0, e.getOriginalWeight(), 0.01);
    }

    @Test
    public void testGetIncomingNode() {
        Edge e;
        Node n1;
        n1 = new Node(1, "Test", 0);
        e = new Edge(n1, null);

        assertSame(n1, e.getSourceNode());

        e = new Edge(null, n1);
        assertNull(e.getSourceNode());
    }

    @Test
    public void testGetOutgoingNode() {
        Edge e;
        Node n1;
        n1 = new Node(1, "Test", 0);
        e = new Edge(n1, null);

        assertNull(e.getDestinationNode());

        e = new Edge(null, n1);
        assertSame(n1, e.getDestinationNode());
    }

    @Test
    public void testGetTime() {
        Edge e;
        e = new Edge(null, null, 42);
        assertSame(42, e.getTime());
    }

    @Test
    public void testIsIncomingEdge() {
        Edge e;
        Node n1, n2;

        n1 = new Node(1, "Test", 0);
        n2 = new Node(2, "Test", 1);

        e = new Edge(n1, n2);
        assertTrue(e.isOutgoingEdge(n1));
        assertFalse(e.isOutgoingEdge(n2));

        e = new Edge(null, null);
        assertFalse(e.isOutgoingEdge(null));
    }

    @Test
    public void testIsOutgoingEdge() {
        Edge e;
        Node n1, n2;

        n1 = new Node(1, "Test", 0);
        n2 = new Node(2, "Test", 1);

        e = new Edge(n1, n2);
        assertTrue(e.isIncomingEdge(n2));
        assertFalse(e.isIncomingEdge(n1));

        e = new Edge(null, null);
        assertFalse(e.isIncomingEdge(null));
    }

    @Test(expected=InvalidCentralityException.class)
    public void testGetWeightForCentrality() throws InvalidCentralityException {
        Edge e;
        CentralityImpl c;
        Centrality cr;

        e = new Edge(null, null);
        c = new CentralityImpl() {
            /*
            public HashMap<Node, Double> getWeightForNodes(Graph g) {
                return null;
            }
            */

            public Type getType() {
                return Type.EdgeCentrality;
            }

            public int getRequiredAPIVersion() {
                return 0;
            }

            public int getVersion() {
                return 1;
            }

            public String getName() {
                return "Blub";
            }
            public HashMap<Edge, Double> getWeight(Graph g) {
                return null;
            }
        };

        e.addWeight(c, 2.0);
        e.addWeight(c, 3.0);

        assertEquals(3.0, e.getWeightForCentrality(c), 0.01);
        cr = new BetweennessCentrality();
        e.getWeightForCentrality(cr);
    }
}
