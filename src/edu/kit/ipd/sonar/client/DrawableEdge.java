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
 * This represents an edge in the graph, annotated with information that
 * enables a GraphDrawer to draw this edge on the screen.
 *
 * Objects of this class are immutable.
 */
public class DrawableEdge {

    /** The tooltip of this edge. */
    private final String tooltip;

    /** The width of this edge. Normalized Value between 0 and 1 inclusive. */
    private final double width;

    /** The source of this edge. */
    private final DrawableNode source;

    /** The destination of this edge. */
    private final DrawableNode destination;

    /**
     * Retrieves the tooltip containing user-readable information.
     *
     * @return A string to be displayed to the user.
     */
    public String getTooltip() {
        return this.tooltip;
    }

    /**
     * Returns the width of this edge as a number between 0 and 1.
     *
     * This value is normalized, so 1 should be drawn the thickest possible,
     * while a value of 0 should be drawn the thinnest possible,
     * (but still visible).
     *
     * @return The width of the edge.
     */
    public double getWidth() {
        return this.width;
    }

    /**
     * Returns the source node of this edge.
     *
     * @return The source of this edge.
     */
    public DrawableNode getSource() {
        return this.source;
    }

    /**
     * Returns the destination node of this edge.
     *
     * @return The destination of this edge.
     */
    public DrawableNode getDestination() {
        return this.destination;
    }

    /**
     * Creates a new DrawableEdge with the given values.
     *
     * @param tooltip       The tooltip.
     * @param width         The width as a normalized value between 0 and 1.
     * @param source        The source of this edge.
     * @param destination   The destination of this edge.
     */
    public DrawableEdge(final String tooltip, final double width,
                        final DrawableNode source,
                        final DrawableNode destination) {
        if (tooltip == null) {
            throw new IllegalArgumentException("Tooltip must not be null.");
        }
        if (width < 0 || width > 1) {
            throw new IllegalArgumentException("Width must be between 0 and 1"
                                               + "inclusive.");
        }
        if (source == null) {
            throw new IllegalArgumentException("Source must not be null.");
        }
        if (destination == null) {
            throw new IllegalArgumentException("Destination must not be null.");
        }

        this.tooltip = tooltip;
        this.width = width;
        this.source = source;
        this.destination = destination;
    }
}
