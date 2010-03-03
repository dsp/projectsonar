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
 * Provides Color conversions.
 *
 * This class provides utility methods for color calculations.
 *
 * @author David Soria Parra <david.parra@student.kit.edu>
 */
public final class ColorUtil {
    /** first 60 degrees hue are red to yellow in HSV. */
    private static final int RED_YELLOW = 1;

    /** second 60 degrees hue are yellow to green in HSV. */
    private static final int YELLOW_GREEN = 2;

    /** third 60 degrees hue are green to cyan in HSV. */
    private static final int GREEN_CYAN = 3;

    /** fourth 60 degrees hue are cyan to blue in HSV. */
    private static final int CYAN_BLUE = 4;

    /** fifth 60 degrees hue are blue to magenta in HSV. */
    private static final int BLUE_MAGENTA = 5;

    /** last 60 degrees hue are magenta to red in HSV. */
    private static final int MAGENTA_RED = 6;

    /**
     * Splits the 360 degree hue into 6 parts.
     */
    private static final float HUE_SPLIT = 60.f;

    /**
     * Maximum amount of the hue value.
     */
    private static final int HUE_MAX = 360;

    /**
     * Convert a HSV color vale to RGB colorspace.
     *
     * The method provides a convertion calculation to map a HSV 3-tupel to
     * back to the RGB colorspace.
     * <p/>
     * The HSV colorspace is a 3-tupel of a hue, expressed in 0-360 degree
     * angle a saturation value between 0.0 and 1.0 and a lightness value
     * between 0.0 and 1.0.
     * <p/>
     * HSV colorspaces are good to variate saturation and lightness
     * while the color stays the same. We primarly use this to display
     * different node weights as different saturation values of the same color.
     *
     * @param hue The hue as defined in HSV
     * @param sat The saturation to be used. Can be ranged from 0.0 to 1.0
     * @param val The value
     *
     * @return An RGB color
     */
    public static Color transformHSVtoRGB(final int hue, final float sat,
            final float val) {
        if (hue < 0 || hue > HUE_MAX) {
            throw new IllegalArgumentException("hue needs to be between"
                    + " 0 and 360");
        }

        if (sat < 0.0 || sat > 1.0) {
            throw new IllegalArgumentException("saturation needs to be between"
                    + " 0.0 and 1.0");
        }

        if (val < 0.0 || val > 1.0) {
            throw new IllegalArgumentException("value needs to be between"
                    + " 0.0 and 1.0");
        }

        /* check wikipedia or something similar for the actual algorithm,
           it's standard. */
        int h = (int) (hue / HUE_SPLIT);
        float f = (hue / HUE_SPLIT) - h;
        float p = val * (1 - sat);
        float q = val * (1 - sat * f);
        float t = val * (1 - sat * (1 - f));

        switch (h) {
            case RED_YELLOW:
                return new Color(q, val, p);
            case YELLOW_GREEN:
                return new Color(p, val, t);
            case GREEN_CYAN:
                return new Color(p, q, val);
            case CYAN_BLUE:
                return new Color(t, p, val);
            case BLUE_MAGENTA:
                return new Color(val, p, q);
            case MAGENTA_RED:
            default:                //<- default value to ensure 360 != 0¡
                return new Color(val, t, p);
        }
    }

    /**
     * Make util class not instantiable.
     */
    private ColorUtil() {
    }
}
