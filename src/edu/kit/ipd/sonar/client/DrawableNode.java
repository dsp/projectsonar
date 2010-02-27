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
package edu.kit.ipd.sonar.client;

/**
 * This class represents a node annotated with enough information for a
 * GraphDrawer to render it on screen.
 *
 * Objects of this class are immutable
 */
public class DrawableNode {

    /** This node's tooltip. */
    private final String tooltip;

    /** This node's size. Normalized value between 0 and 1 inclusive. */
    private final double size;

    /**
     * This node's distance to the center.
     *
     * Normalized value between 0 and 1 inclusive.
     */
    private final double distance;

    /** This node's color. */
    private final Color color;

    /**
     * Retrieves the tooltip containing user-readable information.
     *
     * @return A string to be displayed to the user.
     */
    public String getTooltip() {
        return this.tooltip;
    }

    /**
     * Returns the distance from the center.
     *
     * This is a normalized value between 0 and 1.
     *
     * @return A number representing how far away from the center this node
     *         should be drawn.
     */
    public double getDistance() {
        return this.distance;
    }

    /**
     * Returns the size of this node.
     *
     * This is a normalized value between 0 and 1.
     *
     * @return A number representing how big this node should be drawn.
     */
    public double getSize() {
        return this.size;
    }

    /**
     * Returns the color this node should be drawn in.
     *
     * @return The color of this node.
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Creates a new DrawableNode object with the given values.
     *
     * @param tooltip   The tooltip.
     * @param distance  The normalized distance to the center.
     * @param size      The normalized size of this node.
     * @param color     The color of this node.
     */
    public DrawableNode(final String tooltip, final double distance,
                        final double size, final Color color) {
        if (tooltip == null) {
            throw new IllegalArgumentException("Tooltip must not be null.");
        }
        if (distance < 0 || distance > 1) {
            throw new IllegalArgumentException("Distance must be between "
                                               + "0 and 1 inclusive.");
        }
        if (size < 0 || size > 1) {
            throw new IllegalArgumentException("Size must be between "
                                               + "0 and 1 inclusive.");
        }
        if (color == null) {
            throw new IllegalArgumentException("Color must not be null.");
        }

        this.tooltip = tooltip;
        this.distance = distance;
        this.size = size;
        this.color = color;
    }
}
