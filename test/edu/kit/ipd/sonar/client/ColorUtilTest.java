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
 * Tests the ColorUtil Class.
 *
 * @author Till Heistermann <till.heistermann@student.kit.edu>
 */
public class ColorUtilTest {

    /**
     * Tests the conversion of full black and white.
     */
    @Test
    public void testHsv2RgbBlackAndWhite(){

        Color black = ColorUtil.transformHSVtoRGB(0, 0f, 0f);
        assertEquals(0, black.getRed());
        assertEquals(0, black.getGreen());
        assertEquals(0, black.getBlue());

        Color white = ColorUtil.transformHSVtoRGB(0, 0f, 1f);
        assertEquals(255, white.getRed());
        assertEquals(255, white.getGreen());
        assertEquals(255, white.getBlue());
    }

    /**
     * Tests the conversion of the full colors in the corners
     * of the hsv-hexagon: red, yellow, green, cyan, blue, magenta.
     */
    @Test
    public void testHsv2RgbCornerColors(){
        Color red0 = ColorUtil.transformHSVtoRGB(0, 1f, 1f);
        assertEquals(255, red0.getRed());
        assertEquals(0, red0.getGreen());
        assertEquals(0, red0.getBlue());

        Color yellow = ColorUtil.transformHSVtoRGB(60, 1f, 1f);
        assertEquals(255, yellow.getRed());
        assertEquals(255, yellow.getGreen());
        assertEquals(0, yellow.getBlue());

        Color green = ColorUtil.transformHSVtoRGB(120, 1f, 1f);
        assertEquals(0, green.getRed());
        assertEquals(255, green.getGreen());
        assertEquals(0, green.getBlue());

        Color cyan = ColorUtil.transformHSVtoRGB(180, 1f, 1f);
        assertEquals(0, cyan.getRed());
        assertEquals(255, cyan.getGreen());
        assertEquals(255, cyan.getBlue());

        Color blue = ColorUtil.transformHSVtoRGB(240, 1f, 1f);
        assertEquals(0, blue.getRed());
        assertEquals(0, blue.getGreen());
        assertEquals(255, blue.getBlue());

        Color magenta = ColorUtil.transformHSVtoRGB(300, 1f, 1f);
        assertEquals(255, magenta.getRed());
        assertEquals(0, magenta.getGreen());
        assertEquals(255, magenta.getBlue());

        Color red360 = ColorUtil.transformHSVtoRGB(360, 1f, 1f);
        assertEquals(255, red360.getRed());
        assertEquals(0, red360.getGreen());
        assertEquals(0, red360.getBlue());
    }

    /**
     * Tests if the transition between colors is calculated correctly.
     */
    @Test
    public void testHsv2RgbTransitions(){

        // tests if there actually is purple between blue and magenta:
        // done to ensure the transition of hue-values works.
        Color purple = ColorUtil.transformHSVtoRGB(270, 1f, 1f);
        assertEquals(128, purple.getRed());
        assertEquals(0, purple.getGreen());
        assertEquals(255, purple.getBlue());

        // tests the grey in the middle between black and white
        // done to ensure the transition of brightness-values works
        Color grey = ColorUtil.transformHSVtoRGB(42, 0f, 0.5f);
        assertEquals(128, grey.getRed());
        assertEquals(128, grey.getGreen());
        assertEquals(128, grey.getBlue());

        // tests a light red with medium saturation.
        // done to ensure the transition of staturation-values works
        Color lightRed = ColorUtil.transformHSVtoRGB(0, 0.5f, 1f);
        assertEquals(255, lightRed.getRed());
        assertEquals(128, lightRed.getGreen());
        assertEquals(128, lightRed.getBlue());


    }


    /**
     * Tests if illegal arguments are handled correctly.
     */
    @Test
    public void testHsv2RgbIllegalArguments(){
        Color c;
        try{
            c = ColorUtil.transformHSVtoRGB(-1, 0.5f, 0.5f);
            fail();
        } catch(IllegalArgumentException e) {}

        try{
            c = ColorUtil.transformHSVtoRGB(361, 0.5f, 0.5f);
            fail();
        } catch(IllegalArgumentException e) {}

        try{
            c = ColorUtil.transformHSVtoRGB(100, -0.001f, 0.5f);
            fail();
        } catch(IllegalArgumentException e) {}

        try{
            c = ColorUtil.transformHSVtoRGB(100, 1.001f, 0.5f);
            fail();
        } catch(IllegalArgumentException e) {}
        try{
            c = ColorUtil.transformHSVtoRGB(100, 0.5f, -0.001f);
            fail();
        } catch(IllegalArgumentException e) {}
        try{
            c = ColorUtil.transformHSVtoRGB(100, 0.5f, 1.001f);
            fail();
        } catch(IllegalArgumentException e) {}

    }

}
