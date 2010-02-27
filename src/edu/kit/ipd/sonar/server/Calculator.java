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
 * Interface for the calculation of centrality values of a graph.
 *
 * A calculator is used to create new limited graphs from existing graphs and
 * calculate the values for a given set of centralities on the graph.
 *<p/>
 * To generate new graphs a calculator has to copy the original nodes. Passing
 * by reference is not valid. A calculator implementation has to make sure that
 * the original graph is not changed so that it can be reused by a new
 * calculator.
 *<p/>
 * In the current implementation of Sonar all calculators use the
 * same instance of a graph to generate new graphs from it. This approach is
 * used to minimize memory footage of the application.
 *
 * @author David Soria Parra <david.parra@student.kit.edu>
 */
interface Calculator {

    /**
     * Calculates a new graph with centralities from the given parameters.
     *
     * The method has to create and return a new graph.
     *
     * @param g The graph to use for calculation.
     * @param c A list centralities to use.
     * @param boundary An optional time boundary. Null if unused
     * @param limit An optional limit. Negative or zero if unused.
     * @throws CalculationFailedException if the calculation fails somehow
     * @return A new graph
     */
    Graph calc(final Graph g, ArrayList<CentralityImpl> c,
        TimeBoundary boundary, int limit) throws CalculationFailedException;
}
