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

import com.google.gwt.core.client.GWT;

/**
 * Contains constants for the different ways a centrality can be visualized.
 */
public enum VisualizationMethod {

    /**
     * Visualize by Node color.
     */
    COLOR(Type.NODE_VISUALIZATION),
    /**
     * Visualize by Node size.
     */
    SIZE(Type.NODE_VISUALIZATION),
    /**
     * Visualize by distance from the center.
     */
    DISTANCE(Type.NODE_VISUALIZATION),
    /**
     * Visualize by Edge width.
     */
    LINEWIDTH(Type.EDGE_VISUALIZATION);

    /**
     * The Type of the visualizationMethod.
     */
    private final Type type;

    /**
     * Defines the types the VisualizationMethod can have: They can operate
     * either on node- or on edge-centralities.
     */
    public enum Type {
        /**
         * A Visualization for Node-Centralities.
         */
        NODE_VISUALIZATION,
        /**
         * A Visualization for Node-Centralities.
         */
        EDGE_VISUALIZATION
        }

    /**
     * Constructor for a supported VisualizationMethod.
     * @param type the Type of the visualization.
     */
    private VisualizationMethod(final Type type) {
        this.type = type;
    }

    /**
     * Returns the type of the specific VisualizationMethod.
     * @return The type of the VisualizationMethod.
     */
    public Type getType() {
        return type;
    }

    /** The translation file for localization. */
    private static SonarMessages messages
            = (SonarMessages) GWT.create(SonarMessages.class);
    /**
     * Returns the name of the specific VisualizationMethod.
     * @return The name of the VisualizationMethod.
     */
    public String getName() {
        switch(this) {
            case COLOR:
                return messages.visualizationNameColor();
            case SIZE:
                return messages.visualizationNameSize();
            case DISTANCE:
                return messages.visualizationNameDistance();
            case LINEWIDTH:
                return messages.visualizationNameLinewidth();
            default:
                throw new Error("Unknown visualization error! "
                                + "Coding fail in VisualizationMethod.java!");
        }
    }
}
