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

/**
 * This class represents a color that can be displayed in a browser.
 *
 * Objects of this class are immutable.
 */
public class Color {

    /** The amount of red in this color. */
    private final int red;

    /** The amount of green in this color. */
    private final int green;

    /** The amount of blue in this color. */
    private final int blue;

    /** The number of values a color can have. */
    private static final int RESOLUTION = 0x100;

    /** The maximal value. */
    public static final int MAX_VAL = RESOLUTION - 1;

    /**
     * Creates a new color with the given float values.
     *
     * The values need to be between 0.0 and 1.0.
     *
     * @param red The amount of red.
     * @param green The amount of green.
     * @param blue The amount of blue.
     */
    public Color(final float red, final float green, final float blue) {

        if (red < 0.0f || red > 1.0f) {
            throw new IllegalArgumentException(
              "The red-value must be between 0.0 and 1.0");
        }
        if (green < 0.0f || green > 1.0f) {
            throw new IllegalArgumentException(
              "The green-value must be between 0.0 and 1.0");
        }
        if (blue < 0.0f || blue > 1.0f) {
            throw new IllegalArgumentException(
              "The blue-value must be between 0.0 and 1.0");
        }


        // distribute the colors equally in [0 ... MAX_VAL]
        int r = (int) Math.floor(red   * RESOLUTION);
        int g = (int) Math.floor(green * RESOLUTION);
        int b = (int) Math.floor(blue  * RESOLUTION);

        //handle the special border-case for 1.0:
        if (r == RESOLUTION) { r = MAX_VAL; }
        if (g == RESOLUTION) { g = MAX_VAL; }
        if (b == RESOLUTION) { b = MAX_VAL; }

        this.red = r;
        this.green = g;
        this.blue = b;
    }

    /**
     * Creates a new color with the given int values.
     *
     * @param red The amount of red. Must be between 0 and 0xFF.
     * @param green The amount of green. Must be between 0 and 0xFF.
     * @param blue The amount of blue. Must be between 0 and 0xFF.
     */
    public Color(final int red, final int green, final int blue) {

        if (red < 0 || red > MAX_VAL) {
            throw new IllegalArgumentException(
               "The red-value must be between 0 and 0xFF.");
        }
        if (green < 0 || green > MAX_VAL) {
            throw new IllegalArgumentException(
               "The green-value must be between 0 and 0xFF.");
        }
        if (blue < 0 || blue > MAX_VAL) {
            throw new IllegalArgumentException(
               "The blue-value must be between 0 and 0xFF.");
        }

        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    /**
     * Returns the amount of blue in this color.
     *
     * @return The amount of blue
     */
    public int getBlue() {
        return this.blue;
    }

    /**
     * Returns the amount of red in this color.
     *
     * @return The amount of red.
     */
    public int getRed() {
        return this.red;
    }

    /**
     * Returns the amount of green in this color.
     *
     * @return The amount of green.
     */
    public int getGreen() {
        return this.green;
    }
}
