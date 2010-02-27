/*
 * This file is part of Sonar.
 *
 * This software is free software; you can redistribute it and/or$
 * modify it under the terms of the GNU Lesser General Public$
 * License version 2.1 as published by the Free Software Foundation$
 *
 * This library is distributed in the hope that it will be useful,$
 * but WITHOUT ANY WARRANTY; without even the implied warranty of$
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU$
 * Lesser General Public License for more details.$
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Sonar.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.kit.ipd.sonar.server;

import edu.kit.ipd.sonar.server.centralities.Centrality;
import java.util.HashMap;

/**
 * Interface for all classes that can be annotated with a centrality.
 *
 * Annotable classes can be weighted by one or more centralities. The annotable
 * class needs to be able to store these centralities and the assigned weight.
 * Usually the Annotable interface is implemented by a Node or an Edge as they
 * are the basic elements in a Graph for which centralities can be calculated.
 *
 * @author David Soria Parra <david.parra@student.kit.edu>
 */
public interface Annotable {
    /**
     * Add a weight for given centrality to the annotable object.
     *
     * The annotable object has to remember the mapping between the centrality
     * and the assigned weight and needs to be able to provide a list with this
     * Mapping.
     *
     * @param c The centrality to add
     * @param weight The weight for the object
     */
    void addWeight(Centrality c, double weight);

    /**
     * Returns a mapping between all centralities defined on this annotable
     * object and its calculated weight for the object.
     *
     * @return The mapping
     */
    HashMap<Centrality, Double> getCentralities();

    /**
     * Returns the weight of this object (in a graph) for a given centrality.
     *
     * @param c The requested centrality for which the weight should
     *          be returned
     *
     * @throws InvalidCentralityException If the provided centrality is no
     * valid.
     * @return The weight
     */
    double getWeightForCentrality(Centrality c)
        throws InvalidCentralityException;

    /**
     * Implements an observer pattern that can be used by a higher level
     * structure to get notifications about changes in the underlying
     * annotables.
     *
     * @param listener The actual listener implementation
     */
    void addListener(AnnotableListener listener);

    /**
     * The creation time of the node.
     *
     * Returns the time when the node was created in the database as a UNIX
     * timestamp.
     *
     * @return The time
     */
    int getTime();
}
