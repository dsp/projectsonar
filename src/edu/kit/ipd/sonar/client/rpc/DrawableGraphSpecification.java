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
package edu.kit.ipd.sonar.client.rpc;

import edu.kit.ipd.sonar.server.TimeBoundary;
import edu.kit.ipd.sonar.server.User;
import edu.kit.ipd.sonar.server.centralities.Centrality;
import edu.kit.ipd.sonar.client.VisualizationMethod;

import java.util.Collections;
import java.util.List;

/**
 * Objects of this class unambigously specify a DrawableGraph (assuming
 * we don't suddenly use another database).
 *
 * DrawableGraphSpecifications do not contain any graph data.
 *
 * Objects of this class are immutable.
 */
public class DrawableGraphSpecification {

    /** The GraphSpecification for this graph. */
    private final GraphSpecification graphSpecification;

    /** The visualization methods used to visualize the centralities. */
    private final List<VisualizationMethod> visualizations;

    /** The hashcode. Cached for performance. */
    private final int hash;

    /**
     * Creates a new DrawableGraphSpecification with the given values.
     *
     * @param requestType       The request type.
     * @param cutoff            The maximal number of nodes for a global graph,
     *                              or the maximal hops for a peer graph.
     * @param timeBoundary      The time boundary.
     * @param user              The user in the center of this graph.
     *                              Must be null if this is not a peer graph.
     * @param centralities      An ArrayList containing the centralities that
     *                              should be calculated.
     * @param visualizations    An ArrayList containing the visualization
     *                              methods. They are assigned to the
     *                              centralities through the indices.
     */
    public DrawableGraphSpecification(final GraphType requestType,
            final int cutoff, final TimeBoundary timeBoundary, final User user,
            final List<Centrality> centralities,
            final List<VisualizationMethod> visualizations) {
        this(new GraphSpecification(requestType, cutoff, timeBoundary,
                                    user, centralities),
                    visualizations);
    }

    /**
     * Creates a new DrawableGraphSpecification with the given values.
     *
     * @param graphSpecification    The specification for the underlying Graph.
     * @param visualizations        An ArrayList containing the visualization
     *                              methods.
     */
    public DrawableGraphSpecification(
            final GraphSpecification graphSpecification,
            final List<VisualizationMethod> visualizations) {
        if (visualizations.size()
                != graphSpecification.getCentralities().size()) {
            throw new IllegalArgumentException("There must be as many"
                                        + "visualizations as centralities.");
        }
        this.graphSpecification = graphSpecification;
        this.visualizations = Collections.unmodifiableList(visualizations);

        this.hash = calcHashCode();
    }

    /**
     * Gets the visualization Methods.
     *
     * @return An ArrayList containing the visualization methods.
     */
    public List<VisualizationMethod> getVisualizationMethods() {
        return this.visualizations;
    }

    /**
     * Gets the underlying GraphSpecification.
     *
     * @return A GraphSpecification for this graph.
     */
    public GraphSpecification getGraphSpecification() {
        return this.graphSpecification;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof DrawableGraphSpecification)) {
            return false;
        }
        DrawableGraphSpecification o = (DrawableGraphSpecification) other;
        return this.graphSpecification.equals(o.graphSpecification)
                && this.visualizations.equals(o.visualizations);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return this.hash;
    }

    /** The base value for calculating the hashcode. */
    private static final int HC_INITIAL = 17;

    /** The multiplier for each step in the hashcode calculation. */
    private static final int HC_STEP = 59;

    /**
     * Called by the constructor, calculates the hashcode.
     *
     * @return The hashcode for this object.
     */
    private int calcHashCode() {
        int hc = HC_INITIAL;
        hc += this.graphSpecification.hashCode();
        hc *= HC_STEP;
        hc += this.visualizations.hashCode();
        return hc;
    }
}
