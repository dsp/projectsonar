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

/**
 * Tests the Color-Class.
 *
 * @author Till Heistermann <till.heistermann@student.kit.edu>
 */
public class ColorTest {


    @Test
    public void testLegalIntToRgb(){

        //test "normal" values
        Color col = new Color(13, 42, 99);
        assertEquals(13, col.getRed());
        assertEquals(42, col.getGreen());
        assertEquals(99, col.getBlue());

        //test border-values
        Color col2 = new Color(0, 1, 0xFF);
        assertEquals(0, col2.getRed());
        assertEquals(1, col2.getGreen());
        assertEquals(0xFF, col2.getBlue());
    }

    @Test
    public void testLegalFloatToRgb(){

        //"normal" values
        Color col = new Color(0.25f, 0.5f, 0.75f);
        assertEquals(64, col.getRed());
        assertEquals(128, col.getGreen());
        assertEquals(192, col.getBlue());

        //border-values
        Color col2 = new Color(0.0f, 0.0f, 1.0f);
        assertEquals(0, col2.getRed());
        assertEquals(0, col2.getGreen());
        assertEquals(0xFF, col2.getBlue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntArgToLow() {
        Color col = new Color(-1, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntArgToHigh() {
        Color col = new Color(0xFF, 0xFF, 0x100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFloatArgToLow() {
        Color col = new Color(-0.00001f, 0.0f, 0.0f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFloatArgToHigh() {
        Color col = new Color(0.5f, 0.5f, 1.00001f);
    }

}
