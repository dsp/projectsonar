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
import edu.kit.ipd.sonar.server.centralities.Centrality;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class tests the AvailableCentralitiesArrivedEvent.
 *
 * @author Till Heistermann <till.heistermann@student.kit.edu>
 */
public class AvailableCentralitiesArrivedEventTest {

    /**
     * Tests if the same parameter-Object is given back again.
     */
    @Test
    public void testPassthrough() {
        ArrayList<Centrality> list = new ArrayList<Centrality>();

        //Centrality b = new BetweennessCentrality();
        //Centrality c = new CooperationCentrality();
        //Centrality o = new OutdegreeCentrality();
        //list.add(b);
        //list.add(c);
        //list.add(o);


        AvailableCentralitiesArrivedEvent event =
            new AvailableCentralitiesArrivedEvent(list);

        assertEquals("returned list is not identical with the initial one",
                    list,
                    event.getCentralities());

    }

    /**
     * Tests if a null-parameter is passed correctly.
     */
    @Test
    public void testNullParameter() {

        AvailableCentralitiesArrivedEvent event =
            new AvailableCentralitiesArrivedEvent(null);
        assertNull(event.getCentralities());

    }
}
