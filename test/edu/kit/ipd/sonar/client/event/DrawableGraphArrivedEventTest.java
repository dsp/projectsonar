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
package edu.kit.ipd.sonar.client.event;

import java.util.ArrayList;

import edu.kit.ipd.sonar.client.DrawableGraph;
import edu.kit.ipd.sonar.client.DrawableEdge;
import edu.kit.ipd.sonar.client.DrawableNode;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the DrawableGraphArrivedEvent.
 *
 * @author Till Heistermann <till.heistermann@student.kit.edu>
 */
public class DrawableGraphArrivedEventTest {

    /**
     * tests if an ordinary DrawableGraph
     * is passed through correctly.
     */
    @Test
    public void testPassthrough() {
        DrawableGraph dg =
            new DrawableGraph(new ArrayList<DrawableNode>(),
                              new ArrayList<DrawableEdge>());
        DrawableGraphArrivedEvent event =
            new DrawableGraphArrivedEvent(dg);
        assertSame("DrawableGraph not passed through correctly",
                   dg, event.getDrawableGraph());
    }

    /**
     * tests if a null-parameter is passed through correctly.
     */
    @Test
    public void testNullParameter() {
        DrawableGraphArrivedEvent event =
            new DrawableGraphArrivedEvent(null);
        assertNull(event.getDrawableGraph());
    }
}
