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
package edu.kit.ipd.sonar.server

import edu.kit.ipd.sonar.server.centralities.CentralityImpl
import java.util.ArrayList

/**
 * A trait that enables a calculator to have caching.
 *
 *<p>
 * This traits caches calculations transparently for any calculator.
 *</p>
 *
 * @author David Soria Parra <david.parra@student.kit.edu>
 */
trait Caching extends Calculator {
    protected val cache: Set[Int] = Set()

    @throws(classOf[CalculationFailedException])
    abstract override def calc(graph: Graph, centralities: ArrayList[CentralityImpl],
        bound: TimeBoundary, limit: java.lang.Integer, centralNode: Node): Graph = {
        if (!cache.contains(graph.getStateHash)) {
            return super.calc(graph, centralities, bound, limit, centralNode)
        }
        super.calc(graph, centralities, bound, limit, centralNode)
    }

}

// vim: set ts=4 sw=4 et:
