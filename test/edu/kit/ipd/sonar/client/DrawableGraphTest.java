/**
 * This file is part of Sonar.
 *
 * Sonar is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 2 of the License.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Sonar.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.kit.ipd.sonar.client;

import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.*;

public class DrawableGraphTest {

    @Test
    public void testGetNodeList() {
        ArrayList<DrawableNode> ns = new ArrayList<DrawableNode>();
        DrawableNode n1 = new DrawableNode("", 0, 0, new Color(0, 0, 0));
        DrawableNode n2 = new DrawableNode("", 0, 0, new Color(0, 0, 0));
        ns.add(n1);
        ns.add(n2);
        ArrayList<DrawableEdge> es = new ArrayList<DrawableEdge>();
        DrawableEdge e = new DrawableEdge("", 0, n1, n2);
        es.add(e);

        DrawableGraph g = new DrawableGraph(ns, es);

        assertSame(ns, g.getNodeList());
    }

    @Test
    public void testGetEdgeList() {
        ArrayList<DrawableNode> ns = new ArrayList<DrawableNode>();
        DrawableNode n1 = new DrawableNode("", 0, 0, new Color(0, 0, 0));
        DrawableNode n2 = new DrawableNode("", 0, 0, new Color(0, 0, 0));
        ns.add(n1);
        ns.add(n2);
        ArrayList<DrawableEdge> es = new ArrayList<DrawableEdge>();
        DrawableEdge e = new DrawableEdge("", 0, n1, n2);
        es.add(e);

        DrawableGraph g = new DrawableGraph(ns, es);

        assertSame(es, g.getEdgeList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncompleteGraph() {
        ArrayList<DrawableNode> ns = new ArrayList<DrawableNode>();
        DrawableNode n1 = new DrawableNode("", 0, 0, new Color(0, 0, 0));
        DrawableNode n2 = new DrawableNode("", 0, 0, new Color(0, 0, 0));
        ns.add(n1);
        // Didn't add n2
        ArrayList<DrawableEdge> es = new ArrayList<DrawableEdge>();
        DrawableEdge e = new DrawableEdge("", 0, n1, n2);
        es.add(e);

        DrawableGraph g = new DrawableGraph(ns, es); // Should fail
    }
}
