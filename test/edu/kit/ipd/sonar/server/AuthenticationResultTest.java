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

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests the AuthenticationResult-Class.
 * @author Till Heistermann <till.heistermann@student.kit.edu>
 */
public class AuthenticationResultTest {

    /**
     * tests the handling of successful login.
     */
    @Test
    public void testSuccessfulAuth() {
        User user = new User(42, "Bill Gates");
        AuthenticationResult ar
             = new AuthenticationResult(true, user);

        assertSame(true, ar.isSuccessful());
        assertSame(user, ar.getUser());
    }

    /**
     * tests the handling of a failed auth.
     */
    @Test
    public void testFailedAuth() {
        AuthenticationResult ar
             = new AuthenticationResult(false, null);

        assertSame(false, ar.isSuccessful());
        assertNull(ar.getUser());
    }

    /**
     * Tests if invalid arguments cause exceptions.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArgs1() {
        AuthenticationResult ar
             = new AuthenticationResult(true, null);
    }

    /**
     * Tests if invalid arguments cause exceptions.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArgs2() {
        AuthenticationResult ar
             = new AuthenticationResult(false, new User(42, "Linus Torvalds"));
    }

}
