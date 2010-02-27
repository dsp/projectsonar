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

import edu.kit.ipd.sonar.server.User;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the UserlistArrivedEvent.
 *
 * @author Till Heistermann <till.heistermann@student.kit.edu>
 */
public class UserlistArrivedEventTest {

    /**
     * tests if an Userlist is passed through correctly.
     */
    @Test
    public void testPassthrough() {
        ArrayList<User> list =
            new ArrayList<User>();
        UserlistArrivedEvent event =
            new UserlistArrivedEvent(list);
        assertSame("Userlist not passed through correctly",
                   list, event.getUsers());
    }

    /**
     * tests if a null-parameter is passed through correctly.
     */
    @Test
    public void testNullParameter() {
        UserlistArrivedEvent event =
            new UserlistArrivedEvent(null);
        assertNull(event.getUsers());
    }
}
