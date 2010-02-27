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

import org.junit.Test;
import static org.junit.Assert.*;


/**
 * Tests the User-Class.
 * @author Till Heistermann <till.heistermann@student.kit.edu>
 */
public class UserTest {

    /**
     * tests the handling of a standard user-object.
     */
    @Test
    public void testNormalUser() {
        User user = new User(50, "Bill Gates");
        assertSame(50, (int) user.getId());
        assertEquals("Bill Gates", user.getName());
    }

    /**
     * tests a null-argument for the user id.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullValue1() {
            User user = new User(null, "Name");
    }

    /**
     * Tests a null-argument for the username.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullValue2() {
            User user = new User(42, null);
    }


}
