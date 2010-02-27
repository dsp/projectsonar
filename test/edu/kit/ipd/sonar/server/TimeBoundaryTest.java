/*
 * This file is part of Sonar.
 *
 * This software is free software; you can redistribute it and/or$
 * modify it under the terms of the GNU Lesser General Public$
 * License version 2.1 as published by the Free Software Foundation$
 *
 * This library is distributed in the hope that it will be useful,$
 * but WITHOUT ANY WARRANTY; without even the implied warranty of$
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU$
 * Lesser General Public License for more details.$
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Sonar.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.kit.ipd.sonar.server;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class contains tests for the class TimeBoundary.
 *
 * @author Kevin-Simon Kohlmeyer <kevin-simon.kohlmeyer@student.kit.edu>
 */
public class TimeBoundaryTest {

    @Test
    public void pointlessTestToGetCoverage() {
        new TimeBoundary();
    }

    @Test
    public void testGetters() {
        TimeBoundary tb = new TimeBoundary(5, 10);
        assertTrue(tb.getStart() == 5);
        assertTrue(tb.getEnd() == 10);
        assertTrue(tb.inBoundary(6));
        assertFalse(tb.inBoundary(4));
        assertFalse(tb.inBoundary(-4));
    }

    @Test
    public void testContains() {
        TimeBoundary a = new TimeBoundary( 5, 20);
        TimeBoundary b = new TimeBoundary(10, 15);
        TimeBoundary c = new TimeBoundary( 0, 10);
        TimeBoundary d = new TimeBoundary(12, 20);

        assertTrue(a.contains(b));
        assertFalse(a.contains(c));
        assertTrue(a.contains(d));

        assertFalse(b.contains(a));
        assertFalse(b.contains(c));
        assertFalse(b.contains(d));

        assertFalse(c.contains(a));
        assertFalse(c.contains(b));
        assertFalse(c.contains(d));

        assertFalse(d.contains(a));
        assertFalse(d.contains(b));
        assertFalse(d.contains(c));
    }

    @Test
    public void testEqualsAndHashcode() {
        TimeBoundary a = new TimeBoundary( 5, 20);
        TimeBoundary b = new TimeBoundary( 5, 20);
        TimeBoundary c = new TimeBoundary( 0, 10);
        TimeBoundary d = new TimeBoundary( 0,  8);

        assertTrue(a.equals(a));
        
        assertFalse(a.equals("foo"));

        assertTrue(a.equals(b));
        assertFalse(a.equals(c));
        assertTrue(b.equals(a));

        assertFalse(c.equals(d));

        assertTrue(a.hashCode() == b.hashCode());
    }
}
