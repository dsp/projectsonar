/*
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

import org.junit.Test;
import static org.junit.Assert.*;

public class DrawableEdgeTest {

    @Test
    public void testGetTooltip() {
        Color c = new Color(0, 0, 0);
        DrawableNode s = new DrawableNode("", 0, 0,c);
        DrawableNode d = new DrawableNode("", 0, 0,c);
        DrawableEdge e = new DrawableEdge("tooltip", 0, s, d);

        assertEquals("tooltip", e.getTooltip());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTooltip() {
        Color c = new Color(0, 0, 0);
        DrawableNode s = new DrawableNode("", 0, 0, c);
        DrawableNode d = new DrawableNode("", 0, 0, c);
        DrawableEdge e = new DrawableEdge(null, 0, s, d); // Should fail
    }
    
    @Test
    public void testGetWidth() {
        Color c = new Color(0, 0, 0);
        DrawableNode s = new DrawableNode("", 0, 0, c);
        DrawableNode d = new DrawableNode("", 0, 0, c);
        DrawableEdge e = new DrawableEdge("", 0.78, s, d);

        assertEquals(0.78, e.getWidth(), 0.000001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeWidth() {
        Color c = new Color(0, 0, 0);
        DrawableNode s = new DrawableNode("", 0, 0, c);
        DrawableNode d = new DrawableNode("", 0, 0, c);
        DrawableEdge e = new DrawableEdge("", -0.1, s, d); // Should fail
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTooLargeWidth() {
        Color c = new Color(0, 0, 0);
        DrawableNode s = new DrawableNode("", 0, 0, c);
        DrawableNode d = new DrawableNode("", 0, 0, c);
        DrawableEdge e = new DrawableEdge("", 1.1, s, d); // Should fail
    }

    @Test
    public void testGetSource() {
        Color c = new Color(0, 0, 0);
        DrawableNode s = new DrawableNode("", 0, 0, c);
        DrawableNode d = new DrawableNode("", 0, 0, c);
        DrawableEdge e = new DrawableEdge("", 0, s, d);

        assertSame(s, e.getSource());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSource() {
        Color c = new Color(0, 0, 0);
        DrawableNode d = new DrawableNode("", 0, 0, c);
        DrawableEdge e = new DrawableEdge("", 0, null, d); // Should fail
    }

    @Test
    public void testGetDestination() {
        Color c = new Color(0, 0, 0);
        DrawableNode s = new DrawableNode("", 0, 0, c);
        DrawableNode d = new DrawableNode("", 0, 0, c);
        DrawableEdge e = new DrawableEdge("", 0, s, d);

        assertSame(d, e.getDestination());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullDestination() {
        Color c = new Color(0, 0, 0);
        DrawableNode s = new DrawableNode("", 0, 0, c);
        DrawableEdge e = new DrawableEdge("", 0, s, null); // Should fail
    }
}

