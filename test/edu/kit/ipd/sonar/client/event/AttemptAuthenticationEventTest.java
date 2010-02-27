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

import com.google.gwt.junit.client.GWTTestCase;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests an AttemptAuthentificationEvent for correct parameter passing.
 *
 * @author Till Heistermann <till.heistermann@student.kit.edu>
 *
 */
public class AttemptAuthenticationEventTest {

    /**
     * Tests if the Values passed to this event are put out correctly.
     */
    @Test
    public void testValueConsitancy() {
        AttemptAuthenticationEvent event1
            = new AttemptAuthenticationEvent(true, "User", "password");
        assertTrue(event1.isAdmin());
        assertEquals("user-value not passed through correctly",
                     "User", event1.getUsername());
        assertEquals("password nor passed through correctly",
                     "password", event1.getPassword());
    }

    /**
     * Tests if the Null-Values passed to this event are passed correctly.
     */
    @Test
    public void testNullParameter() {
        AttemptAuthenticationEvent event2
            = new AttemptAuthenticationEvent(false, null, null);
        assertFalse(event2.isAdmin());
        assertNull(event2.getUsername());
        assertNull(event2.getPassword());
    }


}
