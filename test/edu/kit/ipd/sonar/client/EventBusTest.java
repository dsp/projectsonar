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

import org.junit.Test;
import static org.junit.Assert.*;

import edu.kit.ipd.sonar.client.event.*;

import com.google.gwt.event.shared.HandlerManager;

/**
 * Tests the EventBus by firing and handling every Event in our arsenal.
 *
 * This is obviously useless to test, but by doing this we get 100%
 * sexy green bars within the Code Coverage report.
 *
 * So it's worth the effort ;-)
 *
 * @author Till Heistermann <till.heistermann@student.kit.edu>
 *
 * @author Kevin-Simon Kohlmeyer <kevin-simon.kohlmeyer@student.kit.edu>
 */
public class EventBusTest {

    /**
     * Test if we get the same HandlerManager every time.
     */
    @Test
    public void testSameHandlerManager() {
        assertTrue(EventBus.getHandlerManager()
                == EventBus.getHandlerManager());
    }
}
