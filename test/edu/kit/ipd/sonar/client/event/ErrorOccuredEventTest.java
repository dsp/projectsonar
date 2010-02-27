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

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the ErrorOccuredEvent.
 *
 * @author Till Heistermann <till.heistermann@student.kit.edu>
 */
public class ErrorOccuredEventTest {

    /**
     * tests if the error message is passed through correctly.
     */
    @Test
    public void testPassthrough() {
        String message = "ERROR !!!11elf";
        ErrorOccuredEvent event =
            new ErrorOccuredEvent(message);
        assertSame("Message not passed through correctly",
                   message, event.getErrorMessage());
    }

    /**
     * tests if a null-parameter is passed through correctly.
     */
    @Test
    public void testNullParameter() {
        ErrorOccuredEvent event =
            new ErrorOccuredEvent(null);
        assertNull(event.getErrorMessage());
        assertNull(event.getCommand());
    }
}
