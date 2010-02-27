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

import java.util.Collections;
import java.util.List;

/**
 * Objects of this class unambigously specify a Graph (assuming
 * we don't suddenly use another database).
 *
 * GraphSpecifications do not contain any graph data.
 *
 * Objects of this class are immutable.
 */
public class GraphSpecification {

    /** The Type (global or peer) of this graph. */
    private final GraphType requestType;

    /** The cutoff. Max. Nodes if global graph, hops if peer graph. */
    private final int cutoff;

    /** The timeframe the data should span. */
    private final TimeBoundary timeBoundary;

    /** The user in the center of the peer graph or null for a global graph. */
    private final User user;

    /** The centralities to be calculated. */
    private final List<Centrality> centralities;

    /** The hashcode. Cached for performance. */
    private final int hash;

    /**
     * Creates a new GraphSpecification with the given values.
     *
     * @param requestType   The request type.
     * @param cutoff        The maximal number of nodes for a global graph,
     *                          or the maximal hops for a peer graph.
     * @param timeBoundary  The time boundary. May not be null.
     * @param user          The user in the center of this graph.
     *                          Must be null if this is not a peer graph.
     * @param centralities  An ArrayList containing the centralities that
     *                          should be calculated. May not be null.
     *                          May not be empty.
     */
    public GraphSpecification(final GraphType requestType, final int cutoff,
            final TimeBoundary timeBoundary, final User user,
            final List<Centrality> centralities) {
        if (requestType == GraphType.GLOBAL_GRAPH && user != null) {
            throw new IllegalArgumentException("Global graphs must be"
                                             + "requested without a user");
        }
        if (requestType == GraphType.PEER_GRAPH && user == null) {
            throw new IllegalArgumentException("Peer graphs need a non-null"
                                             + "user");
        }
        if (centralities == null || centralities.isEmpty()) {
            throw new IllegalArgumentException("Centralities must not"
                                             + "be null or empty");
        }
        if (timeBoundary == null) {
            throw new IllegalArgumentException("TimeBoundary may not"
                                             + "be null");
        }
        this.requestType = requestType;
        this.cutoff = cutoff;
        this.timeBoundary = timeBoundary;
        this.user = user;
        this.centralities = Collections.unmodifiableList(centralities);

        this.hash = calcHashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof GraphSpecification)) {
            return false;
        }
        GraphSpecification o = (GraphSpecification) other;
        return this.requestType.equals(o.requestType)
            && this.cutoff == o.cutoff
            && this.timeBoundary.equals(o.timeBoundary)
            && this.user.equals(o.user)
            && this.centralities.equals(o.centralities);
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
     * @return The hashcode.
     */
    private int calcHashCode() {
        int hc = HC_INITIAL;
        hc += this.requestType.hashCode();
        hc *= HC_STEP;
        hc += this.cutoff;
        hc *= HC_STEP;
        hc += this.timeBoundary.hashCode();
        hc *= HC_STEP;
        if (this.user != null) {
            hc += this.user.hashCode();
        }
        hc *= HC_STEP;
        hc += centralities.hashCode();
        return hc;
    }

    /**
     * Gets the request type.
     *
     * @return The request type.
     */
    public GraphType getRequestType() {
        return this.requestType;
    }

    /**
     * Gets the time boundary.
     *
     * @return The time boundary.
     */
    public TimeBoundary getTimeBoundary() {
        return this.timeBoundary;
    }

    /**
     * Gets the centralities to be calculated.
     *
     * @return An ArrayList containing the centralities to be calculated.
     */
    public List<Centrality> getCentralities() {
        return this.centralities;
    }

    /**
     * Returns the user in the ccenter of this peer graph, or null if this
     * is not a peer graph.
     *
     * @return The central user if this is a peer graph, null otherwise.
     */
    public User getUser() {
        return this.user;
    }

    /**
     * Returns the cutoff.
     *
     * Should be interpreted as maximum nodes if this is a global graph
     * and as maximum hops if this is a peer graph.
     *
     * @return The cutoff
     */
    public int getCutoff() {
        return this.cutoff;
    }
}
