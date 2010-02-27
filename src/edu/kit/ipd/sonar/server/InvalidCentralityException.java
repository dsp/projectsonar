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

/**
 * This exception can occur if an requested centrality does not exist.
 *
 * An example for this would be that the frontend requests a new calculation
 * for a given set of centralities from the RPCServiceImpl, but at least one
 * centrality is not present in the system.
 *
 * @author David Soria Parra <david.parra@student.kit.edu>
 */
public class InvalidCentralityException extends SonarException {
    /**
     * Holds the centrality object that is invalid.
     */
    private Centrality centrality;

    /**
     * Initialize a new Exception with an invalid centrality object.
     *
     * @param c The centrality
     */
    public InvalidCentralityException(final Centrality c) {
        super();
        centrality = c;
    }

    /**
     * Returns a reference to the invalid centrality object.
     *
     * @return The invalid centrality
     */
    public Centrality getCentrality() {
        return centrality;
    }
}
