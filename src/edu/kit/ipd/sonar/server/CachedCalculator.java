/*
 * This file is part of Sonar.
 *
 * Sonar is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 2 of the License
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Sonar.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.kit.ipd.sonar.server;

import edu.kit.ipd.sonar.server.centralities.CentralityImpl;
import java.util.ArrayList;

/**
 * Implements a transpernt caching for calculators.
 *
 * Implements a decorator pattern for calculators that caches results.
 * The caching implementation is transparent for the API, but needs to be backed
 * by a real calculation implementation like it is provided by GlobalCalculator
 * or PeerCalculator.
 *
 * The caching mechanism tries to not use the complete available memory. It will
 * try to free memory as soon as a certain limit is reached. To avoid multiple
 * caching instances with different caching tables every instance is backed
 * by the same concurrent-safe hashtable
 *
 * @author David Soria Parra <david.parra@student.kit.edu>
 */
class CachedCalculator implements Calculator {
    /**
     * Initialize a new cached calculator for the given Calculator object.
     *
     * @param calculator The calculator to decorate.
     */
    CachedCalculator(final Calculator calculator) {
        // begin-user-code
        // TODO Automatisch erstellter Konstruktoren-Stub
        // end-user-code
    }

    /**
     * Transparent calculation of the requested centralities on the given graph.
     *
     * This method either returns the cached result or delegates the
     * calculation to the decorated calculator.
     *
     * @see Calculator#calc(Graph g, ArrayList c,
     *          TimeBoundary boundary, int limit)
     *
     * @param g The graph to use for calculation.
     * @param c A list centralities to use.
     * @param boundary An optional time boundary. Null if unused
     * @param limit An optional limit. Negative or zero if unused.
     *
     * @throws CalculationFailedException If the calculation fails for
     * some reason.
     *
     * @return A cached graph if possible, otherwise a new calculated graph.
     */
    public Graph calc(final Graph g, final ArrayList<CentralityImpl> c,
            final TimeBoundary boundary, final int limit)
        throws CalculationFailedException {
        // begin-user-code
        // TODO Automatisch erstellter Methoden-Stub
        return null;
        // end-user-code
    }
}
