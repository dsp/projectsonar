/*
 * This file is part of Sonar.
 *$
 * This software is free software; you can redistribute it and/or$
 * modify it under the terms of the GNU Lesser General Public$
 * License version 2.1 as published by the Free Software Foundation$
 *$
 * This library is distributed in the hope that it will be useful,$
 * but WITHOUT ANY WARRANTY; without even the implied warranty of$
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU$
 * Lesser General Public License for more details.$
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Sonar.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.kit.ipd.sonar.server.centralities;

import edu.kit.ipd.sonar.server.Annotable;
import edu.kit.ipd.sonar.server.Graph;

import java.util.HashMap;

/**
 * The interface for centrality algorithms, that can be used within Sonar.
 *
 * This method provides the main interface for every implementation of a
 * centrality. Centralities calculated weights of nodes or edges in graphs.
 *<p/>
 * Please take a look at example implementations such as OutdegreeCentrality.
 *<p/>
 * Every algorithm that calculates centrality values needs to implement
 * this interface, otherwise it cannot be loaded into the Sonar system.
 *
 * @see Centrality
 * @see CentralityLoader
 *
 * @author David Soria Parra <david.parra@student.kit.edu>
 */
public abstract class CentralityImpl extends Centrality {
    /**
     * Creates a new centrality object.
     *
     * This method can be used to safely transfer the information
     * about the centrality over the wire without risking that the
     * implementations must be serializable which would break the
     * plugin system.
     *
     * @return A centrality
     */
    public Centrality getCentrality() {
        return new Centrality(getName(), getVersion(), getType());
    }


    /**
     * The required API Version.
     *
     * Sonar holds an API Version. A centrality can require
     * a minimum API version. If the version doesn't match,
     * the module will not be loaded.
     *<p/>
     * The api version is a positive integer.
     *
     * @return The required api version
     */
    public abstract int getRequiredAPIVersion();

    /**
     * Calculates the weight for the annotables of a graph.
     *
     * The method returns a mapping between the annotables and the
     * value as a double.
     *<p/>
     * Note that you are not allowed to add
     * the value itself to the graph nor will you get the graph
     * with the calculated values during run. You will always
     * get a graph without centralities.
     *
     * @param g The graph
     * @return The mapping
     */
    public abstract HashMap<? extends Annotable, Double> getWeight(Graph g);
}
