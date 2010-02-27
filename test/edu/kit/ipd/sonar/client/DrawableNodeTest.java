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

/**
 * Test for the DrawableNode implementation.
 *
 * @author Kevin-Simon Kohlmeyer
 */
public class DrawableNodeTest {

    @Test
    public void testGetColor() {
        Color c = new Color(10,10,10);
        DrawableNode n = new DrawableNode("", 0, 0, c);
        assertSame(c, n.getColor());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullColor() {
        // Should fail
        DrawableNode n = new DrawableNode("", 0, 0, null);
    }

    @Test
    public void testGetTooltip() {
        DrawableNode n = new DrawableNode("Tooltip", 0, 0, new Color(0,0,0));
        assertEquals("Tooltip", n.getTooltip());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullTooltip() {
        // Should fail
        DrawableNode n = new DrawableNode(null, 0, 0, new Color(0,0,0));
    }

    @Test
    public void testGetDistance() {
        DrawableNode n = new DrawableNode("", 0.8, 0, new Color(0,0,0));
        assertEquals(0.8, n.getDistance(), 0.00001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeDistance() {
        // Should fail
        DrawableNode n = new DrawableNode("", -0.1, 0, new Color(0,0,0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTooLargeDistance() {
        // Should fail
        DrawableNode n = new DrawableNode("", 1.1, 0, new Color(0,0,0));
    }

    @Test
    public void testGetSize() {
        DrawableNode n = new DrawableNode("", 0, 0.8, new Color(0,0,0));
        assertEquals(0.8, n.getSize(), 0.00001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeSize() {
        // Should fail
        DrawableNode n = new DrawableNode("", 0, -0.1, new Color(0,0,0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTooLargeSize() {
        DrawableNode n = new DrawableNode("", 0, 1.1, new Color(0,0,0));
    }
}

